<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">展期监测管理</span>
          <el-button type="primary" @click="openDialog()">新增监测</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="loanApplicationId" label="关联借展" width="120" />
        <el-table-column prop="showcaseCondition" label="展柜条件" width="140" />
        <el-table-column prop="securityPlan" label="安保方案" width="140" />
        <el-table-column prop="temperature" label="温度(℃)" width="100" />
        <el-table-column prop="humidity" label="湿度(%)" width="100" />
        <el-table-column prop="conditionCompliant" label="条件符合" width="100">
          <template #default="{ row }">
            <el-tag :type="row.conditionCompliant ? 'success' : 'danger'">{{ row.conditionCompliant ? '符合' : '不符' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="monitoringDate" label="监测日期" width="140" />
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增监测记录" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联借展ID">
          <el-input-number v-model="form.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="展柜条件">
          <el-input v-model="form.showcaseCondition" />
        </el-form-item>
        <el-form-item label="安保方案">
          <el-input v-model="form.securityPlan" />
        </el-form-item>
        <el-form-item label="温度(℃)">
          <el-input-number v-model="form.temperature" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="湿度(%)">
          <el-input-number v-model="form.humidity" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="监测日期">
          <el-date-picker v-model="form.monitoringDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMonitorings, createMonitoring } from '../api'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)

const form = ref({
  loanApplicationId: null, showcaseCondition: '', securityPlan: '', temperature: 20, humidity: 50, monitoringDate: ''
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getMonitorings()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取监测数据失败')
  }
}

function openDialog() {
  form.value = { loanApplicationId: null, showcaseCondition: '', securityPlan: '', temperature: 20, humidity: 50, monitoringDate: '' }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const temp = form.value.temperature
    const hum = form.value.humidity
    const conditionCompliant = temp >= 18 && temp <= 22 && hum >= 45 && hum <= 55
    await createMonitoring({ ...form.value, conditionCompliant })
    if (!conditionCompliant) {
      ElMessage.warning('条件不符！温度需20±2℃，湿度需50±5%')
    } else {
      ElMessage.success('创建成功，条件合规')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('创建失败')
  }
}
</script>
