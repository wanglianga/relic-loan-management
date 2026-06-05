<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">借展申请数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value active">{{ stats.active }}</div>
            <div class="stat-label">进行中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value returned">{{ stats.returned }}</div>
            <div class="stat-label">已归还</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-value error">{{ stats.abnormal }}</div>
            <div class="stat-label">异常暂停</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600">最近借展申请</span>
          </template>
          <el-table :data="recentLoans" stripe style="width: 100%" @row-click="goChain">
            <el-table-column prop="contractNumber" label="合同编号" width="140" />
            <el-table-column prop="artifactName" label="藏品名" width="120">
              <template #default="{ row }">{{ store.artifacts.find(a => a.id === row.artifactId)?.name || '-' }}</template>
            </el-table-column>
            <el-table-column prop="applicantMuseum" label="出借方" width="120" />
            <el-table-column prop="borrowingVenue" label="借展场馆" width="120" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <StatusTag :status="row.status" />
              </template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始日期" width="120" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover">
          <template #header>
            <span style="font-weight: 600">最近异常事件</span>
          </template>
          <div v-if="abnormalEvents.length === 0" style="color: #999; text-align: center; padding: 20px">暂无异常</div>
          <el-timeline v-else>
            <el-timeline-item
              v-for="(evt, idx) in abnormalEvents"
              :key="idx"
              :type="evt.type"
              :timestamp="evt.time"
              placement="top"
            >
              <div>{{ evt.title }}</div>
              <div style="color: #999; font-size: 12px">{{ evt.detail }}</div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useLoanStore } from '../stores/loan'
import StatusTag from '../components/StatusTag.vue'

const router = useRouter()
const store = useLoanStore()

onMounted(() => {
  store.fetchAll()
})

const stats = computed(() => {
  const loans = store.loans
  return {
    total: loans.length,
    active: loans.filter(l => ['INSURANCE_CONFIRMED', 'PACKED', 'HANDED_OVER', 'EXHIBITING'].includes(l.status)).length,
    returned: loans.filter(l => l.status === 'RETURNED').length,
    abnormal: loans.filter(l => ['SUSPENDED', 'OVERDUE'].includes(l.status)).length,
  }
})

const recentLoans = computed(() => {
  return [...store.loans].slice(0, 10)
})

const abnormalEvents = computed(() => {
  const events = []
  store.transports.forEach(t => {
    if (t.vibrationExceeded) {
      events.push({ type: 'danger', time: t.departureTime || '', title: '震动超标', detail: `运输ID: ${t.id}` })
    }
  })
  store.monitorings.forEach(m => {
    if (m.conditionCompliant === false) {
      events.push({ type: 'danger', time: m.monitoringDate || '', title: '条件不符', detail: `温度: ${m.temperature}℃ 湿度: ${m.humidity}%` })
    }
  })
  store.insurances.forEach(ins => {
    if (ins.status === 'ADJUSTING') {
      events.push({ type: 'warning', time: ins.validFrom || '', title: '估值调整', detail: `保单号: ${ins.policyNumber}` })
    }
  })
  store.returns.forEach(r => {
    if (r.isExtension) {
      events.push({ type: 'warning', time: r.returnDate || '', title: '延期归还', detail: r.extensionReason || '' })
    }
  })
  return events.slice(0, 10)
})

function goChain(row) {
  router.push(`/chain/${row.id}`)
}
</script>

<style scoped>
.stat-card {
  text-align: center;
  padding: 10px 0;
}
.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: #409eff;
}
.stat-value.active {
  color: #67c23a;
}
.stat-value.returned {
  color: #909399;
}
.stat-value.error {
  color: #f56c6c;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 8px;
}
</style>
