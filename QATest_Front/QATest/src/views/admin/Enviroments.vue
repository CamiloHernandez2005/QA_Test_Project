<template>
  <div>
    <div class="px-4 w-full mt-2">
      <div class="card">
        <!-- 🔹 DataTable principal -->
        <DataTable
          v-model:expandedRows="expandedRows"
          :value="servers"
          dataKey="id"
          tableStyle="min-width: 60rem"
          sortMode="single"
          :sortOrder="-1"
          paginator
          :rows="5"
          class="rounded-xl overflow-hidden shadow-md"
        >
          <!-- 🔹 Header con botones -->
          <template #header>
            <div class="flex flex-wrap justify-between items-center gap-4 w-full">
              <!-- Izquierda -->
              <div>
                <Button
                  label="Add Server"
                  icon="pi pi-plus"
                  @click="onAdd"
                />
              </div>

              <!-- Derecha -->
              <div class="flex items-center gap-2">
                

                <!-- Botón Expand/Collapse único -->
                <Button
                  :label="allExpanded ? 'Collapse All' : 'Expand All'"
                  :icon="allExpanded ? 'pi pi-minus' : 'pi pi-plus'"
                  variant="text"
                  @click="toggleExpandAll"
                />
                <span class="p-input-icon-left">
                  <InputText v-model="globalFilter" placeholder="Search..." class="w-48" />
                </span>
              </div>
            </div>
          </template>

          <!-- 🔹 Columnas principales -->
          <Column expander style="width: 5rem" />
          <Column field="name" header="Name" sortable />
          <Column field="description" header="Description" sortable />
          <Column field="lastUpdated" header="Last Updated" sortable />

          <!-- 🔹 Columna acciones -->
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

          <!-- 🔹 Expansión: regiones -->
          <template #expansion="slotProps">
            <div class="p-4">
              <h5 class="font-semibold mb-3">Regions for {{ slotProps.data.name }}</h5>
              <DataTable
                :value="slotProps.data.regions"
                responsiveLayout="scroll"
                class="border border-slate-200 rounded-lg"
              >
                <Column field="region" header="Region" sortable />
                <Column field="ip" header="IP" sortable />
                <Column field="port" header="Port" sortable />
                <Column field="lastUpdated" header="Last Updated" sortable />

                <!-- Acciones región -->
                <Column header="Actions" bodyClass="text-center" style="width: 7rem">
                  <template #body="regionProps">
                    <div class="flex justify-center gap-2">
                      <Button
                        icon="pi pi-pencil"
                        class="p-button-rounded p-button-info p-button-sm"
                        @click="onEditRegion(slotProps.data, regionProps.data)"
                      />
                      <Button
                        icon="pi pi-trash"
                        class="p-button-rounded p-button-danger p-button-sm"
                        @click="onDeleteRegion(slotProps.data, regionProps.data)"
                      />
                    </div>
                  </template>
                </Column>
              </DataTable>
            </div>
          </template>
        </DataTable>
      </div>
    </div>

    <div class="px-4 md:px-10 mx-auto w-full -m-24">
      <FooterAdmin />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import FooterAdmin from '@/components/Footers/FooterAdmin.vue'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import { CustomerService } from '@/service/CustomerService'

const servers = ref([])
const expandedRows = ref({})
const globalFilter = ref('')
const allExpanded = ref(false) // estado del botón expand/collapse

onMounted(() => {
  CustomerService.getServersLarge().then((data) => (servers.value = data))
})

// 🔹 Expandir / Colapsar todo
const toggleExpandAll = () => {
  if (allExpanded.value) {
    expandedRows.value = {}
  } else {
    expandedRows.value = servers.value.reduce((acc, server) => {
      acc[server.id] = true
      return acc
    }, {})
  }
  allExpanded.value = !allExpanded.value
}

// 🔹 Métodos acciones
const onAdd = () => console.log('Add server')
const onEdit = (row) => console.log('Edit server:', row)
const onDelete = (row) => console.log('Delete server:', row)
const onEditRegion = (server, region) =>
  console.log(`Edit region ${region.region} of server ${server.name}`, region)
const onDeleteRegion = (server, region) =>
  console.log(`Delete region ${region.region} of server ${server.name}`, region)
</script>
