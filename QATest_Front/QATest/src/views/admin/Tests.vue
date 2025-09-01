<template>
  <div class="flex flex-col min-h-screen bg-slate-100 font-sans">
    <!-- 🔹 Contenido principal -->
    <div class="flex-grow">
      <div class="px-6 w-full mt-6">
        <div class="card bg-white shadow-lg rounded-2xl p-6">
          <!-- Selector de aplicación -->
          <div class="flex justify-between items-center mb-6">
            <Dropdown
              v-model="selectedApp"
              :options="apps"
              optionLabel="label"
              optionValue="value"
              placeholder="Select Application"
              class="w-64"
            >
              <template #option="slotProps">
                <div class="flex items-center gap-2">
                  <i :class="slotProps.option.icon"></i>
                  <span>{{ slotProps.option.label }}</span>
                </div>
              </template>
              <template #value="slotProps">
                <div class="flex items-center gap-2" v-if="slotProps.value">
                  <i :class="apps.find(a => a.value === slotProps.value)?.icon"></i>
                  <span>{{ apps.find(a => a.value === slotProps.value)?.label }}</span>
                </div>
                <span v-else>Select Application</span>
              </template>
            </Dropdown>
          </div>

          <!-- 🔹 Estado vacío cuando no hay app -->
          <div
            v-if="!selectedApp"
            class="flex flex-col items-center justify-center py-16 text-center text-slate-500 border-2 border-dashed border-slate-300 rounded-xl"
          >
            <i class="pi pi-database text-5xl mb-4 text-slate-400"></i>
            <p class="text-lg font-medium">No application selected</p>
            <p class="text-sm text-slate-400 mt-1">
              Choose an application from the dropdown above to view data.
            </p>
          </div>

          <!-- 🔹 Stats + Tabla cuando sí hay app -->
          <div v-else>
            <!-- Stats resumen -->
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
              <div class="bg-slate-50 rounded-xl shadow p-4 flex flex-col">
                <span class="text-sm text-gray-500">Total Records</span>
                <span class="text-2xl font-bold mt-1">{{ selectedData.length }}</span>
              </div>
              <div class="bg-slate-50 rounded-xl shadow p-4 flex flex-col">
                <span class="text-sm text-gray-500">Last Updated</span>
                <span class="text-2xl font-bold mt-1">
                  {{ selectedData[0]?.lastUpdated || '-' }}
                </span>
              </div>
              <div class="bg-slate-50 rounded-xl shadow p-4 flex flex-col">
                <span class="text-sm text-gray-500">Active Users</span>
                <span class="text-2xl font-bold mt-1">12</span>
              </div>
            </div>

            <!-- Tabla dinámica -->
            <DataTable
              v-if="selectedData.length"
              :value="selectedData"
              dataKey="id"
              tableStyle="min-width: 60rem"
              paginator
              :rows="5"
              :globalFilterFields="['name','result','lastUpdated']"
              class="rounded-xl overflow-hidden shadow-md"
              :filters="filters"
            >
              <!-- Header con buscador -->
              <template #header>
                <div class="flex flex-wrap justify-between items-center gap-4 w-full">
                  <div>
                    <Button
                      label="Add Record"
                      icon="pi pi-plus"
                      @click="onAdd"
                    />
                  </div>
                  <div class="flex items-center gap-2">
                    <span class="p-input-icon-left">
                      <InputText placeholder="Search..." class="w-48" />
                    </span>
                  </div>
                </div>
              </template>

              <!-- Columnas -->
              <Column field="name" header="Name" sortable />

              <!-- 🔹 Result con colores -->
              <Column field="result" header="Result" sortable>
                <template #body="slotProps">
                  <span
                    :class="[
                      'px-2 py-1 rounded-full text-xs font-semibold',
                      slotProps.data.result === 'Successful' ? 'bg-green-100 text-green-700' : '',
                      slotProps.data.result === 'Error' ? 'bg-red-100 text-red-700' : '',
                      slotProps.data.result === 'Idle' ? 'bg-yellow-100 text-yellow-700' : ''
                    ]"
                  >
                    {{ slotProps.data.result }}
                  </span>
                </template>
              </Column>

              <Column field="lastUpdated" header="Last Updated" sortable />

              <!-- Acciones -->
              <Column header="Actions" bodyClass="text-center" style="width: 8rem">
                <template #body="slotProps">
                  <div class="flex justify-center gap-2">
                    <Button
                      icon="pi pi-pencil"
                      class="p-button-rounded p-button-info p-button-sm"
                      @click="onEdit(slotProps.data)"
                    />
                    <Button
                      icon="pi pi-trash"
                      class="p-button-rounded p-button-danger p-button-sm"
                      @click="onDelete(slotProps.data)"
                    />
                  </div>
                </template>
              </Column>
            </DataTable>
          </div>
        </div>
      </div>
    </div>

    <!-- 🔹 Footer ocupa todo el ancho -->
    <div class="w-full mt-10">
      <FooterAdmin />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import FooterAdmin from '@/components/Footers/FooterAdmin.vue'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Dropdown from 'primevue/dropdown'

// 🔹 Aplicaciones disponibles con íconos
const apps = [
  { label: 'App A', value: 'appA', icon: 'pi pi-server' },
  { label: 'App B', value: 'appB', icon: 'pi pi-cloud' },
  { label: 'App C', value: 'appC', icon: 'pi pi-cog' }
]

const selectedApp = ref(null)

// 🔹 Data simulada
const data = {
  appA: [
    { id: 1, name: 'Server A1', result: 'Successful', lastUpdated: '2025-08-20' },
    { id: 2, name: 'Server A2', result: 'Idle', lastUpdated: '2025-08-22' }
  ],
  appB: [
    { id: 3, name: 'Server B1', result: 'Successful', lastUpdated: '2025-08-19' },
    { id: 4, name: 'Server B2', result: 'Error', lastUpdated: '2025-08-21' }
  ],
  appC: [
    { id: 5, name: 'Server C1', result: 'Successful', lastUpdated: '2025-08-18' }
  ]
}

// 🔹 Datos según la app seleccionada
const selectedData = computed(() => {
  return selectedApp.value ? data[selectedApp.value] : []
})

// 🔹 Métodos
const onAdd = () => console.log('Add record for', selectedApp.value)
const onEdit = (row) => console.log('Edit row:', row)
const onDelete = (row) => console.log('Delete row:', row)
</script>
