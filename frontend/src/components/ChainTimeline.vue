<template>
  <el-steps :active="activeStep" align-center>
    <el-step title="借展申请" :description="stepDesc(0)" />
    <el-step title="保单确认" :description="stepDesc(1)" />
    <el-step title="装箱运输" :description="stepDesc(2)" />
    <el-step title="点交确认" :description="stepDesc(3)" />
    <el-step title="展期监测" :description="stepDesc(4)" />
    <el-step title="撤展归还" :description="stepDesc(5)" />
  </el-steps>
  <div class="chain-detail">
    <el-timeline>
      <el-timeline-item
        v-for="(event, idx) in events"
        :key="idx"
        :type="event.type"
        :timestamp="event.timestamp"
        placement="top"
      >
        <el-card shadow="hover">
          <h4>{{ event.title }}</h4>
          <p v-for="(line, i) in event.details" :key="i">{{ line }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  chainData: { type: Object, default: () => ({}) }
})

const statusStepMap = {
  APPLIED: 0,
  INSURANCE_CONFIRMED: 1,
  PACKED: 2,
  HANDED_OVER: 3,
  EXHIBITING: 4,
  RETURNED: 5,
}

const activeStep = computed(() => {
  if (!props.chainData.loan) return 0
  return statusStepMap[props.chainData.loan.status] ?? 0
})

const stepDesc = (step) => {
  const loan = props.chainData.loan
  if (!loan) return ''
  const current = statusStepMap[loan.status] ?? 0
  if (step < current) return '已完成'
  if (step === current) return '进行中'
  return '待处理'
}

const events = computed(() => {
  const list = []
  const d = props.chainData
  if (d.loan) {
    list.push({ type: 'primary', timestamp: d.loan.startDate || '', title: '借展申请', details: [`合同编号: ${d.loan.contractNumber}`, `出借方: ${d.loan.applicantMuseum}`, `借展场馆: ${d.loan.borrowingVenue}`] })
  }
  if (d.insurance) {
    list.push({ type: d.insurance.status === 'ACTIVE' ? 'success' : 'warning', timestamp: d.insurance.validFrom || '', title: '保单确认', details: [`保单号: ${d.insurance.policyNumber}`, `保险金额: ${d.insurance.insuredAmount}`, `状态: ${d.insurance.status}`] })
  }
  if (d.transport) {
    const vibExceeded = d.transport.vibrationExceeded
    list.push({ type: vibExceeded ? 'danger' : 'success', timestamp: d.transport.departureTime || '', title: '装箱运输', details: [`恒湿箱: ${d.transport.humidityBox || '-'}`, `押运人员: ${d.transport.escortPersonnel || '-'}`, vibExceeded ? '⚠ 震动超标' : '运输正常'] })
  }
  if (d.handover) {
    list.push({ type: 'success', timestamp: d.handover.handoverTime || '', title: '点交确认', details: [`类型: ${d.handover.handoverType || '-'}`, `交出方: ${d.handover.handlerFrom || '-'}`, `接收方: ${d.handover.handlerTo || '-'}`] })
  }
  if (d.monitoring) {
    const condFail = d.monitoring.conditionCompliant === false
    list.push({ type: condFail ? 'danger' : 'success', timestamp: d.monitoring.monitoringDate || '', title: '展期监测', details: [`温度: ${d.monitoring.temperature ?? '-'}℃`, `湿度: ${d.monitoring.humidity ?? '-'}%`, condFail ? '⚠ 条件不符' : '条件合规'] })
  }
  if (d.returnRecord) {
    const isExt = d.returnRecord.isExtension
    list.push({ type: isExt ? 'danger' : 'success', timestamp: d.returnRecord.returnDate || '', title: '撤展归还', details: [`归还至: ${d.returnRecord.returnedTo || '-'}`, isExt ? `⚠ 延期: ${d.returnRecord.extensionReason || ''}` : '按期归还'] })
  }
  return list
})
</script>

<style scoped>
.chain-detail {
  margin-top: 30px;
}
</style>
