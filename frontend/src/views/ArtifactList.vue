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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getArtifacts, createArtifact, updateArtifact } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const searchName = ref('')
const searchGrade = ref('')

const form = ref({
  name: '', category: '', grade: '', appraisedValue: 0, dimensions: '', humidityRequirement: '', loanDeadline: '', museumName: ''
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
    form.value = { name: '', category: '', grade: '', valuation: 0, dimensions: '', humidityRequirement: '', loanPeriod: '', museum: '' }
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

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确认删除该藏品？', '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    fetchData()
  } catch {}
}
</script>
