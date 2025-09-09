import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/components/Navbars/AdminNavbar.vue'
import Login from '@/views/auth/Login.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Enviroments from '@/views/admin/Enviroments.vue'
import Tests from '@/views/admin/Tests.vue'
import Users from '@/views/admin/Users.vue'
import Audit from '@/views/admin/Audit.vue'

const routes = [
  { path: '/', component: Login },
  {
    path: '/admin',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'Tests', component: Tests },
      { path: 'environments', component: Enviroments },
      { path: 'users', component: Users },
      { path: 'audit', component: Audit },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
