<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">点交记录管理</span>
          <el-button type="primary" @click="openDialog()">新增点交</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="loanApplicationId" label="关联借展" width="120" />
        <el-table-column prop="handoverType" label="点交类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ typeMap[row.handoverType] || row.handoverType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handlerFrom" label="交出方" width="140" />
        <el-table-column prop="handlerTo" label="接收方" width="140" />
        <el-table-column prop="conditionDescription" label="品相描述" width="200" />
        <el-table-column prop="photos" label="照片URL" width="180" />
        <el-table-column prop="handoverTime" label="点交时间" width="160" />
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增点交记录" width="600px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="关联借展ID">
          <el-input-number v-model="form.loanApplicationId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="点交类型">
          <el-select v-model="form.handoverType" style="width: 100%" placeholder="请选择">
            <el-option label="出发点交" value="DEPARTURE" />
            <el-option label="到馆点交" value="ARRIVAL" />
            <el-option label="归还点交" value="RETURN" />
          </el-select>
        </el-form-item>
        <el-form-item label="交出方">
          <el-input v-model="form.handlerFrom" />
        </el-form-item>
        <el-form-item label="接收方">
          <el-input v-model="form.handlerTo" />
        </el-form-item>
        <el-form-item label="品相描述">
          <el-input v-model="form.conditionDescription" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="照片URL">
          <el-input v-model="form.photos" placeholder="输入照片链接地址" />
        </el-form-item>
        <el-form-item label="点交时间">
          <el-date-picker v-model="form.handoverTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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
import { getHandovers, createHandover } from '../api'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const typeMap = { DEPARTURE: '出发点交', ARRIVAL: '到馆点交', RETURN: '归还点交' }

const form = ref({
  loanApplicationId: null, handoverType: '', handlerFrom: '', handlerTo: '', conditionDescription: '', photos: '', handoverTime: ''
})

onMounted(() => { fetchData() })

async function fetchData() {
  try {
    const res = await getHandovers()
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('获取点交数据失败')
  }
}

function openDialog() {
  form.value = { loanApplicationId: null, handoverType: '', handlerFrom: '', handlerTo: '', conditionDescription: '', photos: '', handoverTime: '' }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const payload = { ...form.value }
    if (!payload.handoverTime) delete payload.handoverTime
    await createHandover(payload)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('创建失败')
  }
}
</script>
