import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/components/Navbars/AdminNavbar.vue'
import Login from '@/views/auth/Login.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Enviroments from '@/views/admin/Enviroments.vue'
import Tests from '@/views/admin/Tests.vue'

const routes = [
  { path: '/', component: Login },
  {
    path: '/admin',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'Tests', component: Tests },
      { path: 'environments', component: Enviroments },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
