import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import ArtifactList from '../views/ArtifactList.vue'
import LoanList from '../views/LoanList.vue'
import InsuranceList from '../views/InsuranceList.vue'
import TransportList from '../views/TransportList.vue'
import HandoverList from '../views/HandoverList.vue'
import MonitoringList from '../views/MonitoringList.vue'
import ReturnList from '../views/ReturnList.vue'
import LoanChain from '../views/LoanChain.vue'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: Dashboard },
  { path: '/artifacts', component: ArtifactList },
  { path: '/loans', component: LoanList },
  { path: '/insurances', component: InsuranceList },
  { path: '/transports', component: TransportList },
  { path: '/handovers', component: HandoverList },
  { path: '/monitorings', component: MonitoringList },
  { path: '/returns', component: ReturnList },
  { path: '/chain/:loanId', component: LoanChain },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
