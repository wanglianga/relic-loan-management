import axios from 'axios'
const api = axios.create()

export const getArtifacts = () => api.get('/api/artifacts')
export const getArtifact = (id) => api.get(`/api/artifacts/${id}`)
export const createArtifact = (data) => api.post('/api/artifacts', data)
export const updateArtifact = (id, data) => api.put(`/api/artifacts/${id}`, data)

export const getLoans = () => api.get('/api/loans')
export const getLoan = (id) => api.get(`/api/loans/${id}`)
export const createLoan = (data) => api.post('/api/loans', data)
export const updateLoan = (id, data) => api.put(`/api/loans/${id}`, data)
export const updateLoanStatus = (id, status) => api.put(`/api/loans/${id}/status`, { status })

export const getInsurances = () => api.get('/api/insurances')
export const getInsurance = (id) => api.get(`/api/insurances/${id}`)
export const createInsurance = (data) => api.post('/api/insurances', data)
export const updateInsurance = (id, data) => api.put(`/api/insurances/${id}`, data)

export const getTransports = () => api.get('/api/transports')
export const getTransport = (id) => api.get(`/api/transports/${id}`)
export const createTransport = (data) => api.post('/api/transports', data)
export const updateTransport = (id, data) => api.put(`/api/transports/${id}`, data)
export const reportVibration = (id, data) => api.post(`/api/transports/${id}/vibration`, data)

export const getHandovers = () => api.get('/api/handovers')
export const getHandover = (id) => api.get(`/api/handovers/${id}`)
export const createHandover = (data) => api.post('/api/handovers', data)

export const getMonitorings = () => api.get('/api/monitorings')
export const getMonitoring = (id) => api.get(`/api/monitorings/${id}`)
export const createMonitoring = (data) => api.post('/api/monitorings', data)

export const getReturns = () => api.get('/api/returns')
export const getReturn = (id) => api.get(`/api/returns/${id}`)
export const createReturn = (data) => api.post('/api/returns', data)

export const getChain = (loanId) => api.get(`/api/chain/${loanId}`)

export default api
