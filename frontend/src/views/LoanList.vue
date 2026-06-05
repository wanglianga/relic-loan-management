<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">借展申请管理</span>
          <el-button type="primary" @click="openDialog()">新增借展</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="contractNumber" label="合同编号" width="140" />
        <el-table-column prop="artifactName" label="藏品名" width="120">
          <template #default="{ row }">{{ artifacts.find(a => a.id === row.artifactId)?.name || '-' }}</template>
        </el-table-column>
        <el-table-column prop="applicantMuseum" label="出借方" width="120" />
        <el-table-column prop="borrowingVenue" label="借展场馆" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="状态" width="130">
          <template #default="{ row }">
            <StatusTag :status="row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'APPLIED'" size="small" type="success" @click="changeStatus(row, 'INSURANCE_CONFIRMED')">确认保单</el-button>
            <el-button v-if="row.status === 'INSURANCE_CONFIRMED'" size="small" type="warning" @click="changeStatus(row, 'PACKED')">确认装箱</el-button>
            <el-button v-if="row.status === 'PACKED'" size="small" type="warning" @click="changeStatus(row, 'HANDED_OVER')">确认点交</el-button>
            <el-button v-if="row.status === 'HANDED_OVER'" size="small" type="primary" @click="changeStatus(row, 'EXHIBITING')">开始展期</el-button>
            <el-button v-if="row.status === 'EXHIBITING'" size="small" @click="changeStatus(row, 'RETURNED')">确认归还</el-button>
            <el-button v-if="row.status === 'EXHIBITING' || row.status === 'OVERDUE'" size="small" type="success" @click="openExtendDialog(row)">延期展出</el-button>
            <el-button size="small" @click="goChain(row)">链路</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增借展申请" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="合同编号">
          <el-input v-model="form.contractNumber" />
        </el-form-item>
        <el-form-item label="选择藏品">
          <el-select v-model="form.artifactId" style="width: 100%" placeholder="请选择藏品">
            <el-option v-for="a in artifacts" :key="a.id" :label="a.name" :value="a.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出借方">
          <el-input v-model="form.applicantMuseum" />
        </el-form-item>
        <el-form-item label="借展场馆">
          <el-input v-model="form.borrowingVenue" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="extendDialogVisible" title="延期展出" width="500px" destroy-on-close>
      <el-form :model="extendForm" label-width="120px">
        <el-form-item label="当前结束日期">
          <el-input :value="currentLoan?.endDate" disabled />
        </el-form-item>
        <el-form-item label="新结束日期" required>
          <el-date-picker v-model="extendForm.newEndDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="延期原因">
          <el-input v-model="extendForm.extensionReason" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="extendDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExtension">确认延期</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getLoans, createLoan, updateLoanStatus, getArtifacts, extendLoan, canExtendLoan } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import StatusTag from '../components/StatusTag.vue'

const router = useRouter()
const tableData = ref([])
const artifacts = ref([])
const dialogVisible = ref(false)
const extendDialogVisible = ref(false)
const currentLoan = ref(null)
const form = ref({
  contractNumber: '', artifactId: null, applicantMuseum: '', borrowingVenue: '', startDate: '', endDate: ''
})
const extendForm = ref({
  newEndDate: '',
  extensionReason: ''
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const [loansRes, artRes] = await Promise.all([getLoans(), getArtifacts()])
    tableData.value = loansRes.data
    artifacts.value = artRes.data
  } catch (e) {
    ElMessage.error('获取数据失败')
  }
}

function openDialog() {
  form.value = { contractNumber: '', artifactId: null, applicantMuseum: '', borrowingVenue: '', startDate: '', endDate: '' }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    await createLoan(form.value)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('创建失败')
  }
}

async function changeStatus(row, status) {
  try {
    await updateLoanStatus(row.id, status)
    ElMessage.success('状态更新成功')
    fetchData()
  } catch (e) {
    ElMessage.error('状态更新失败')
  }
}

async function openExtendDialog(row) {
  currentLoan.value = row
  extendForm.value = {
    newEndDate: '',
    extensionReason: ''
  }
  
  try {
    const res = await canExtendLoan(row.id)
    if (!res.data.canExtend) {
      ElMessageBox.alert(
        '当前保额不足以覆盖藏品估值，无法确认展出延期。请先调整保单保额或与评估机构确认估值后再操作。',
        '保额不足',
        { type: 'warning', confirmButtonText: '我知道了' }
      )
      return
    }
    extendDialogVisible.value = true
  } catch (e) {
    ElMessage.error('检查延期条件失败')
  }
}

async function submitExtension() {
  if (!extendForm.value.newEndDate) {
    ElMessage.warning('请选择新的结束日期')
    return
  }
  try {
    await extendLoan(currentLoan.value.id, extendForm.value)
    ElMessage.success('延期成功')
    extendDialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || e.message || '延期失败')
  }
}

function goChain(row) {
  router.push(`/chain/${row.id}`)
}
</script>
