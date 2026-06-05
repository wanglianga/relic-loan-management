import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoans, getArtifacts, getInsurances, getTransports, getHandovers, getMonitorings, getReturns } from '../api'

export const useLoanStore = defineStore('loan', () => {
  const loans = ref([])
  const artifacts = ref([])
  const insurances = ref([])
  const transports = ref([])
  const handovers = ref([])
  const monitorings = ref([])
  const returns = ref([])
  const loading = ref(false)

  async function fetchLoans() {
    loading.value = true
    try {
      const res = await getLoans()
      loans.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchArtifacts() {
    loading.value = true
    try {
      const res = await getArtifacts()
      artifacts.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchInsurances() {
    loading.value = true
    try {
      const res = await getInsurances()
      insurances.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchTransports() {
    loading.value = true
    try {
      const res = await getTransports()
      transports.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchHandovers() {
    loading.value = true
    try {
      const res = await getHandovers()
      handovers.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchMonitorings() {
    loading.value = true
    try {
      const res = await getMonitorings()
      monitorings.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchReturns() {
    loading.value = true
    try {
      const res = await getReturns()
      returns.value = res.data
    } finally {
      loading.value = false
    }
  }

  async function fetchAll() {
    loading.value = true
    try {
      const [loansRes, artifactsRes, insurancesRes, transportsRes, handoversRes, monitoringsRes, returnsRes] = await Promise.allSettled([
        getLoans(),
        getArtifacts(),
        getInsurances(),
        getTransports(),
        getHandovers(),
        getMonitorings(),
        getReturns()
      ])
      if (loansRes.status === 'fulfilled') loans.value = loansRes.value.data
      if (artifactsRes.status === 'fulfilled') artifacts.value = artifactsRes.value.data
      if (insurancesRes.status === 'fulfilled') insurances.value = insurancesRes.value.data
      if (transportsRes.status === 'fulfilled') transports.value = transportsRes.value.data
      if (handoversRes.status === 'fulfilled') handovers.value = handoversRes.value.data
      if (monitoringsRes.status === 'fulfilled') monitorings.value = monitoringsRes.value.data
      if (returnsRes.status === 'fulfilled') returns.value = returnsRes.value.data
    } finally {
      loading.value = false
    }
  }

  return {
    loans, artifacts, insurances, transports, handovers, monitorings, returns, loading,
    fetchLoans, fetchArtifacts, fetchInsurances, fetchTransports, fetchHandovers, fetchMonitorings, fetchReturns, fetchAll
  }
})
