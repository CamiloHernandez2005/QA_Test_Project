import { createRouter, createWebHistory } from 'vue-router'

import Index from '@/views/Index.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import Dashboard from '@/views/admin/Dashboard.vue'

const routes = [
  { path: '/', component:Index},
  { path: '/auth/login', component:Login},
  { path: '/auth/register', component:Register} ,
  { path: '/admin/dashboard', component:Dashboard }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
