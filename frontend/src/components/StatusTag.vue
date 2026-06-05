<template>
  <el-tag :type="tagType" :effect="effect">{{ label }}</el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: { type: String, required: true },
  effect: { type: String, default: 'light' }
})

const statusMap = {
  APPLIED: { label: '已申请', type: 'primary' },
  INSURANCE_CONFIRMED: { label: '保单确认', type: '' },
  PACKED: { label: '已装箱', type: 'warning' },
  HANDED_OVER: { label: '已点交', type: 'warning' },
  EXHIBITING: { label: '展期中', type: 'success' },
  RETURNED: { label: '已归还', type: 'info' },
  SUSPENDED: { label: '暂停', type: 'danger' },
  OVERDUE: { label: '逾期', type: 'warning' },
  DRAFT: { label: '草稿', type: 'info' },
  ACTIVE: { label: '生效', type: 'success' },
  ADJUSTING: { label: '调整中', type: 'warning' },
  EXPIRED: { label: '已过期', type: 'info' },
  CANCELLED: { label: '已取消', type: 'info' },
  PREPARING: { label: '准备中', type: 'primary' },
  IN_TRANSIT: { label: '运输中', type: 'primary' },
  DELIVERED: { label: '已到达', type: 'success' },
  ABNORMAL: { label: '异常', type: 'danger' },
}

const tagType = computed(() => statusMap[props.status]?.type || 'info')
const label = computed(() => statusMap[props.status]?.label || props.status)
</script>
