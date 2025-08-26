<template>
  <div>
    <div class="relative md:ml-64 bg-blueGray-100">
      <admin-navbar />
      <header-stats />
      <div class="px-4 md:px-10 mx-auto w-full -m-24">

        <!-- Contenido específico de esta página -->
        <div class="flex flex-wrap mt-4">
          <div class="w-full mb-12 px-4">
            <card-table />
          </div>
          <div class="w-full mb-12 px-4">
            <card-table color="dark" />
          </div>
        </div>

        <footer-admin />
      </div>
    </div>
  </div>
</template>

<script>
import AdminNavbar from "@/components/Navbars/AdminNavbar.vue";
import FooterAdmin from "@/components/Footers/FooterAdmin.vue";
import CardTable from '@/components/Cards/CardTable.vue';

export default {
  name: 'tables-page',
  components: {
    AdminNavbar,
    FooterAdmin,
    CardTable
  }
}
</script>
