<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">运输记录管理</span>
          <el-button type="primary" @click="openDialog()">新增运输</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="loanApplicationId" label="关联借展" width="120" />
        <el-table-column prop="humidityBox" label="恒湿箱" width="120" />
        <el-table-column prop="escortPersonnel" label="押运人员" width="120" />
        <el-table-column prop="route" label="路线" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" />
          </template>
        </el-table-column>
        <el-table-column prop="vibrationExceeded" label="震动超标" width="100">
          <template #default="{ row }">
            <el-tag :type="row.vibrationExceeded ? 'danger' : 'success'">{{ row.vibrationExceeded ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="departureTime" label="出发时间" width="140" />
        <el-table-column prop="arrivalTime" label="到达时间" width="140" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openVibrationDialog(row)">震动上报</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增运输记录" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联借展ID">
          <el-input-number v-model="form.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="恒湿箱">
          <el-input v-model="form.humidityBox" />
        </el-form-item>
        <el-form-item label="押运人员">
          <el-input v-model="form.escortPersonnel" />
        </el-form-item>
        <el-form-item label="路线">
          <el-input v-model="form.route" />
        </el-form-item>
        <el-form-item label="出发时间">
          <el-date-picker v-model="form.departureTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="到达时间">
          <el-date-picker v-model="form.arrivalTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="vibrationDialogVisible" title="震动上报" width="400px" destroy-on-close>
      <el-form :model="vibrationForm" label-width="100px">
        <el-form-item label="震动值(g)">
          <el-input-number v-model="vibrationForm.vibrationValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="上报时间">
          <el-date-picker v-model="vibrationForm.reportTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="vibrationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleVibration">上报</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTransports, createTransport, reportVibration } from '../api'
import { ElMessage } from 'element-plus'
import StatusTag from '../components/StatusTag.vue'

const tableData = ref([])
const dialogVisible = ref(false)
const vibrationDialogVisible = ref(false)
const currentTransportId = ref(null)
const VIBRATION_THRESHOLD = 2.5

const form = ref({
  loanApplicationId: null, humidityBox: '', escortPersonnel: '', route: '', departureTime: '', arrivalTime: ''
})

const vibrationForm = ref({
  vibrationValue: 0, reportTime: ''
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getTransports()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取运输数据失败')
  }
}

function openDialog() {
  form.value = { loanId: null, humidityBox: '', escort: '', route: '', departureTime: '', arrivalTime: '' }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    await createTransport({ ...form.value, status: 'IN_TRANSIT', vibrationExceeded: false })
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('创建失败')
  }
}

function openVibrationDialog(row) {
  currentTransportId.value = row.id
  vibrationForm.value = { vibrationValue: 0, reportTime: '' }
  vibrationDialogVisible.value = true
}

async function handleVibration() {
  try {
    const exceeded = vibrationForm.value.vibrationValue > VIBRATION_THRESHOLD
    await reportVibration(currentTransportId.value, {
      vibrationData: `value=${vibrationForm.value.vibrationValue}g threshold=2.5g`,
      exceeded: exceeded
    })
    if (exceeded) {
      ElMessage.warning('震动值超过阈值！已标记为超标')
    } else {
      ElMessage.success('震动上报成功，数值正常')
    }
    vibrationDialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('上报失败')
  }
}
</script>
