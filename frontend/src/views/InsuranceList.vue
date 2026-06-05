<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">保单管理</span>
          <el-button type="primary" @click="openDialog()">新增保单</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="policyNumber" label="保单号" width="140" />
        <el-table-column prop="loanApplicationId" label="关联借展" width="120" />
        <el-table-column prop="insuredAmount" label="保险金额(万元)" width="140" />
        <el-table-column prop="deductible" label="免赔额(万元)" width="130" />
        <el-table-column prop="terms" label="条款" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" />
          </template>
        </el-table-column>
        <el-table-column prop="validFrom" label="有效期起" width="120" />
        <el-table-column prop="validTo" label="有效期止" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button v-if="row.status === 'DRAFT'" size="small" type="success" @click="confirmPolicy(row)">确认保单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑保单' : '新增保单'" width="600px" destroy-on-close>
      <el-form :model="form" label-width="120px">
        <el-form-item label="保单号">
          <el-input v-model="form.policyNumber" />
        </el-form-item>
        <el-form-item label="关联借展ID">
          <el-input-number v-model="form.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="保险金额(万元)">
          <el-input-number v-model="form.insuredAmount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="免赔额(万元)">
          <el-input-number v-model="form.deductible" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="条款">
          <el-input v-model="form.terms" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="有效期起">
          <el-date-picker v-model="form.validFrom" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="有效期止">
          <el-date-picker v-model="form.validTo" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-card v-if="adjustments.length > 0" shadow="hover" style="margin-top: 20px">
      <template #header>
        <span style="font-weight: 600">估值调整记录</span>
      </template>
      <el-table :data="adjustments" stripe style="width: 100%">
        <el-table-column prop="policyNumber" label="保单号" width="140" />
        <el-table-column prop="insuredAmount" label="保险金额(万元)" width="140" />
        <el-table-column prop="valuationAdjustments" label="估值调整" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getInsurances, createInsurance, updateInsurance } from '../api'
import { ElMessage } from 'element-plus'
import StatusTag from '../components/StatusTag.vue'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = ref({
  policyNumber: '', loanApplicationId: null, insuredAmount: 0, deductible: 0, terms: '', validFrom: '', validTo: ''
})

const adjustments = computed(() => {
  return tableData.value.filter(item => item.valuationAdjustments != null && item.valuationAdjustments !== '')
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getInsurances()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取保单数据失败')
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
    form.value = { policyNo: '', loanId: null, coverageAmount: 0, deductible: 0, terms: '', validFrom: '', validTo: '' }
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    if (isEdit.value) {
      await updateInsurance(editId.value, form.value)
      ElMessage.success('更新成功')
    } else {
      await createInsurance({ ...form.value, status: 'DRAFT' })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

async function confirmPolicy(row) {
  try {
    await updateInsurance(row.id, { ...row, status: 'ACTIVE' })
    ElMessage.success('保单已确认')
    fetchData()
  } catch (e) {
    ElMessage.error('确认失败')
  }
}
</script>
