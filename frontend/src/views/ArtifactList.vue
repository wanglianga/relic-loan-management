<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">藏品档案管理</span>
          <div>
            <el-input v-model="searchName" placeholder="按名称搜索" style="width: 180px; margin-right: 10px" clearable />
            <el-select v-model="searchGrade" placeholder="按等级筛选" style="width: 140px; margin-right: 10px" clearable>
              <el-option label="一级" value="一级" />
              <el-option label="二级" value="二级" />
              <el-option label="三级" value="三级" />
            </el-select>
            <el-button type="primary" @click="openDialog()">新增藏品</el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredData" stripe style="width: 100%">
        <el-table-column prop="name" label="名称" width="140" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="grade" label="等级" width="80" />
        <el-table-column prop="appraisedValue" label="估值(万元)" width="120" />
        <el-table-column prop="dimensions" label="尺寸" width="120" />
        <el-table-column prop="humidityRequirement" label="恒湿要求" width="120" />
        <el-table-column prop="loanDeadline" label="借展期限" width="120" />
        <el-table-column prop="museumName" label="所属博物馆" width="140" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="success" @click="openValuationDialog(row)">调整估值</el-button>
            <el-button size="small" @click="viewValuationHistory(row)">历史记录</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑藏品' : '新增藏品'" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类别">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="form.grade" style="width: 100%">
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
          </el-select>
        </el-form-item>
        <el-form-item label="估值(万元)">
          <el-input-number v-model="form.appraisedValue" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="尺寸">
          <el-input v-model="form.dimensions" />
        </el-form-item>
        <el-form-item label="恒湿要求">
          <el-input v-model="form.humidityRequirement" />
        </el-form-item>
        <el-form-item label="借展期限">
          <el-input v-model="form.loanDeadline" />
        </el-form-item>
        <el-form-item label="所属博物馆">
          <el-input v-model="form.museumName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="valuationDialogVisible" title="调整估值" width="700px" destroy-on-close>
      <el-form :model="valuationForm" label-width="120px">
        <el-form-item label="藏品名称">
          <el-input :value="currentArtifact?.name" disabled />
        </el-form-item>
        <el-form-item label="当前估值(万元)">
          <el-input-number :model-value="currentArtifact?.appraisedValue" disabled style="width: 100%" />
        </el-form-item>
        <el-form-item label="新估值(万元)" required>
          <el-input-number v-model="valuationForm.newValue" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="调整原因" required>
          <el-input v-model="valuationForm.adjustmentReason" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="评估机构">
          <el-input v-model="valuationForm.appraisalInstitution" />
        </el-form-item>
        <el-form-item label="关联借展ID">
          <el-input-number v-model="valuationForm.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="关联保单ID">
          <el-input-number v-model="valuationForm.insurancePolicyId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="保险条款变化">
          <el-input v-model="valuationForm.insuranceTermsChange" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="新增保费(万元)">
          <el-input-number v-model="valuationForm.additionalPremium" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="借展合同影响">
          <el-input v-model="valuationForm.contractImpact" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="原估值依据">
          <el-input v-model="valuationForm.originalValuationBasis" type="textarea" :rows="2" placeholder="估值下调时请填写原估值依据，以备争议查询" />
        </el-form-item>
        <el-form-item label="调整人">
          <el-input v-model="valuationForm.adjustedBy" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="valuationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitValuationAdjustment">提交调整</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="historyDialogVisible" title="估值调整历史记录" width="900px" destroy-on-close>
      <div v-if="currentArtifact">
        <p style="margin-bottom: 16px; font-weight: 600">{{ currentArtifact.name }} - 估值调整历史</p>
        <el-table :data="valuationHistory" stripe style="width: 100%">
          <el-table-column prop="createdAt" label="调整时间" width="180" />
          <el-table-column prop="originalValue" label="原估值(万元)" width="120" />
          <el-table-column prop="newValue" label="新估值(万元)" width="120" />
          <el-table-column label="调整类型" width="100">
            <template #default="{ row }">
              <el-tag :type="row.isValueIncrease ? 'danger' : 'success'" size="small">
                {{ row.isValueIncrease ? '上调' : '下调' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="adjustmentReason" label="调整原因" />
          <el-table-column prop="appraisalInstitution" label="评估机构" width="140" />
          <el-table-column prop="adjustedBy" label="调整人" width="100" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getArtifacts, createArtifact, updateArtifact, getValuationAdjustmentsByArtifact, createValuationAdjustment } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const valuationDialogVisible = ref(false)
const historyDialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const searchName = ref('')
const searchGrade = ref('')
const currentArtifact = ref(null)
const valuationHistory = ref([])

const form = ref({
  name: '', category: '', grade: '', appraisedValue: 0, dimensions: '', humidityRequirement: '', loanDeadline: '', museumName: ''
})

const valuationForm = ref({
  artifactId: null,
  originalValue: null,
  newValue: null,
  adjustmentReason: '',
  appraisalInstitution: '',
  loanApplicationId: null,
  insurancePolicyId: null,
  insuranceTermsChange: '',
  additionalPremium: null,
  contractImpact: '',
  originalValuationBasis: '',
  adjustedBy: ''
})

const filteredData = computed(() => {
  return tableData.value.filter(item => {
    const nameMatch = !searchName.value || item.name?.includes(searchName.value)
    const gradeMatch = !searchGrade.value || item.grade === searchGrade.value
    return nameMatch && gradeMatch
  })
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getArtifacts()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取藏品数据失败')
  }
}

function openDialog(row) {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    form.value = { ...row }
  } else {
    isEdit.value = false
    editId.value = null
    form.value = { name: '', category: '', grade: '', appraisedValue: 0, dimensions: '', humidityRequirement: '', loanDeadline: '', museumName: '' }
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    if (isEdit.value) {
      await updateArtifact(editId.value, form.value)
      ElMessage.success('更新成功')
    } else {
      await createArtifact(form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

function openValuationDialog(row) {
  currentArtifact.value = row
  valuationForm.value = {
    artifactId: row.id,
    originalValue: row.appraisedValue,
    newValue: row.appraisedValue,
    adjustmentReason: '',
    appraisalInstitution: '',
    loanApplicationId: null,
    insurancePolicyId: null,
    insuranceTermsChange: '',
    additionalPremium: null,
    contractImpact: '',
    originalValuationBasis: '',
    adjustedBy: ''
  }
  valuationDialogVisible.value = true
}

async function submitValuationAdjustment() {
  if (!valuationForm.value.newValue) {
    ElMessage.warning('请填写新估值')
    return
  }
  if (!valuationForm.value.adjustmentReason) {
    ElMessage.warning('请填写调整原因')
    return
  }
  try {
    await createValuationAdjustment(valuationForm.value)
    ElMessage.success('估值调整成功')
    valuationDialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || e.message || '操作失败')
  }
}

async function viewValuationHistory(row) {
  currentArtifact.value = row
  try {
    const res = await getValuationAdjustmentsByArtifact(row.id)
    valuationHistory.value = res.data
    historyDialogVisible.value = true
  } catch (e) {
    ElMessage.error('获取历史记录失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确认删除该藏品？', '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    fetchData()
  } catch {}
}
</script>
