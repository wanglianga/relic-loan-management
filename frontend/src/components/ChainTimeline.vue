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
  if (!props.chainData.loanApplication) return 0
  return statusStepMap[props.chainData.loanApplication.status] ?? 0
})

const stepDesc = (step) => {
  const loan = props.chainData.loanApplication
  if (!loan) return ''
  const current = statusStepMap[loan.status] ?? 0
  if (step < current) return '已完成'
  if (step === current) return '进行中'
  return '待处理'
}

const events = computed(() => {
  const list = []
  const d = props.chainData
  if (d.loanApplication) {
    list.push({ type: 'primary', timestamp: d.loanApplication.startDate || '', title: '借展申请', details: [`合同编号: ${d.loanApplication.contractNumber}`, `出借方: ${d.loanApplication.applicantMuseum}`, `借展场馆: ${d.loanApplication.borrowingVenue}`] })
  }
  if (d.insurances && d.insurances.length > 0) {
    const ins = d.insurances[0]
    list.push({ type: ins.status === 'ACTIVE' ? 'success' : 'warning', timestamp: ins.validFrom || '', title: '保单确认', details: [`保单号: ${ins.policyNumber}`, `保险金额: ${ins.insuredAmount}`, `状态: ${ins.status}`] })
  }
  if (d.transports && d.transports.length > 0) {
    const t = d.transports[0]
    const vibExceeded = t.vibrationExceeded
    list.push({ type: vibExceeded ? 'danger' : 'success', timestamp: t.departureTime || '', title: '装箱运输', details: [`恒湿箱: ${t.humidityBox || '-'}`, `押运人员: ${t.escortPersonnel || '-'}`, vibExceeded ? '⚠ 震动超标' : '运输正常'] })
  }
  if (d.handovers && d.handovers.length > 0) {
    const h = d.handovers[0]
    list.push({ type: 'success', timestamp: h.handoverTime || '', title: '点交确认', details: [`类型: ${h.handoverType || '-'}`, `交出方: ${h.handlerFrom || '-'}`, `接收方: ${h.handlerTo || '-'}`] })
  }
  if (d.monitorings && d.monitorings.length > 0) {
    const m = d.monitorings[0]
    const condFail = m.conditionCompliant === false
    list.push({ type: condFail ? 'danger' : 'success', timestamp: m.monitoringDate || '', title: '展期监测', details: [`温度: ${m.temperature ?? '-'}℃`, `湿度: ${m.humidity ?? '-'}%`, condFail ? '⚠ 条件不符' : '条件合规'] })
  }
  if (d.returns && d.returns.length > 0) {
    const r = d.returns[0]
    const isExt = r.isExtension
    list.push({ type: isExt ? 'danger' : 'success', timestamp: r.returnDate || '', title: '撤展归还', details: [`归还至: ${r.returnedTo || '-'}`, isExt ? `⚠ 延期: ${r.extensionReason || ''}` : '按期归还'] })
  }
  return list
})
</script>

<style scoped>
.chain-detail {
  margin-top: 30px;
}
</style>
