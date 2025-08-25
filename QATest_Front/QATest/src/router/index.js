import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/components/Navbars/AdminNavbar.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import Tables from '@/views/admin/Tables.vue'
import Enviroments from '@/views/admin/Enviroments.vue'

const routes = [
  { path: '/', component: Login },
  { path: '/auth/register', component: Register },

  {
    path: '/admin',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'environments', component: Enviroments },
      { path: 'tables', component: Tables }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
