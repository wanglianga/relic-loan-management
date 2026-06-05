<template>
  <div>
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: 600">借展链路详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>
      <div v-if="loading" style="text-align: center; padding: 40px">
        <el-icon class="is-loading" :size="32"><Loading /></el-icon>
        <div style="margin-top: 10px; color: #999">加载中...</div>
      </div>
      <div v-else-if="chainData.loan">
        <ChainTimeline :chain-data="chainData" />
      </div>
      <div v-else style="text-align: center; padding: 40px; color: #999">暂无链路数据</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getChain } from '../api'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import ChainTimeline from '../components/ChainTimeline.vue'

const route = useRoute()
const chainData = ref({})
const loading = ref(true)

onMounted(async () => {
  try {
    const loanId = route.params.loanId
    const res = await getChain(loanId)
    chainData.value = res.data
  } catch (e) {
    ElMessage.error('获取链路数据失败')
  } finally {
    loading.value = false
  }
})
</script>
