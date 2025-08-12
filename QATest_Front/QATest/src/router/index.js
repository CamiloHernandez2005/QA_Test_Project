import { createRouter, createWebHistory } from 'vue-router'

import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Settings from '@/views/admin/Settings.vue'
import Tables from '@/views/admin/Tables.vue'

const routes = [
  { path: '/auth/login', component: Login },
  { path: '/auth/register', component: Register },
  { path: '/admin/dashboard', component: Dashboard },
  { path: '/admin/settings', component: Settings },
  { path: '/admin/tables', component: Tables },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
