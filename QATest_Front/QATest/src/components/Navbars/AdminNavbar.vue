<template>
  <div class="flex h-screen text-sm font-sans">
    <!-- Sidebar -->
    <aside
      :class="[
        'shadow-xl flex flex-col',
        'transition-[width,transform] ease-in-out duration-500',
        isCollapsed ? 'w-16' : 'w-60',
        'bg-slate-800 text-white'
      ]"
    >
      <!-- Header Sidebar -->
      <div class="flex items-center justify-center px-3 h-14 border-b border-slate-700">
        <span v-if="!isCollapsed" class="font-semibold tracking-wide transition-opacity duration-300">AXEXO</span>
      </div>

      <!-- Links Sidebar -->
      <nav class="flex-1 overflow-y-auto mt-2 space-y-1">
        <RouterLink
          to="/admin/dashboard"
          class="flex items-center px-3 py-2 hover:bg-slate-700 rounded transition-all duration-300 ease-in-out"
        >
          <i class="pi pi-home text-lg"></i>
          <span v-if="!isCollapsed" class="ml-3 transition-opacity duration-300">Dashboard</span>
        </RouterLink>

        <RouterLink
          to="/admin/environments"
          class="flex items-center px-3 py-2 hover:bg-slate-700 rounded transition-all duration-300 ease-in-out"
        >
          <i class="pi pi-globe text-lg"></i>
          <span v-if="!isCollapsed" class="ml-3 transition-opacity duration-300">Environments</span>
        </RouterLink>

        <RouterLink
          to="/"
          class="flex items-center px-3 py-2 hover:bg-slate-700 rounded transition-all duration-300 ease-in-out"
        >
          <i class="pi pi-sign-out text-lg"></i>
          <span v-if="!isCollapsed" class="ml-3 transition-opacity duration-300">Exit</span>
        </RouterLink>
      </nav>
    </aside>

    <!-- Área principal -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- Header Main -->
      <div class="shadow flex items-center h-14 px-4 bg-slate-800 text-white">
        <!-- Botón de abrir/cerrar sidebar -->
        <button @click="toggleCollapse" class="p-2 hover:bg-slate-700 rounded mr-3">
          <i :class="isCollapsed ? 'pi pi-bars' : 'pi pi-times'"></i>
        </button>

        <Menubar :model="items" class="flex-1 border-none bg-transparent text-white text-sm">
          <template #end>
            <div class="flex items-center gap-2">
              <InputText placeholder="Search" type="text" class="w-28 sm:w-auto text-sm text-black" />
              <Avatar image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png" shape="circle" />
            </div>
          </template>
        </Menubar>
      </div>

      <!-- Contenido dinámico -->
      <main class="flex-1 overflow-y-auto p-4 bg-slate-100">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import Menubar from 'primevue/menubar'
import InputText from 'primevue/inputtext'
import Avatar from 'primevue/avatar'

const isCollapsed = ref(true)

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

const items = ref([
  { label: 'Home', icon: 'pi pi-home', to: '/admin/dashboard' },
  { label: 'Environments', icon: 'pi pi-globe', to: '/admin/environments' },
  { label: 'Exit', icon: 'pi pi-sign-out', to: '/auth/login' }
])
</script>
