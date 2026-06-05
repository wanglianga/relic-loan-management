<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">撤展归还管理</span>
          <el-button type="primary" @click="openDialog()">新增归还</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="loanApplicationId" label="关联借展" width="120" />
        <el-table-column prop="returnCondition" label="归还品相" width="160" />
        <el-table-column prop="returnedTo" label="归还至" width="140" />
        <el-table-column prop="returnDate" label="归还日期" width="140" />
        <el-table-column prop="isExtension" label="是否延期" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isExtension ? 'danger' : 'success'">{{ row.isExtension ? '延期' : '按期' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="extensionReason" label="延期原因" width="180" />
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增归还记录" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联借展ID">
          <el-input-number v-model="form.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="归还品相">
          <el-input v-model="form.returnCondition" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="归还至">
          <el-input v-model="form.returnedTo" />
        </el-form-item>
        <el-form-item label="归还日期">
          <el-date-picker v-model="form.returnDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否延期">
          <el-checkbox v-model="form.isExtension">延期归还</el-checkbox>
        </el-form-item>
        <el-form-item v-if="form.isExtension" label="延期原因">
          <el-input v-model="form.extensionReason" type="textarea" :rows="2" />
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
import { getReturns, createReturn } from '../api'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)

const form = ref({
  loanApplicationId: null, returnCondition: '', returnedTo: '', returnDate: '', isExtension: false, extensionReason: ''
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getReturns()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取归还数据失败')
  }
}

function openDialog() {
  form.value = { loanApplicationId: null, returnCondition: '', returnedTo: '', returnDate: '', isExtension: false, extensionReason: '' }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    await createReturn(form.value)
    if (form.value.isExtension) {
      ElMessage.warning('已标记延期归还')
    } else {
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('创建失败')
  }
}
</script>
