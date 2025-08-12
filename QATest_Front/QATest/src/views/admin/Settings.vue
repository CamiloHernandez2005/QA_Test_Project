<template>
  <div>
    <sidebar />
    <div class="relative md:ml-64 bg-blueGray-100">
      <admin-navbar />
      <header-stats />
      <div class="px-4 md:px-10 mx-auto w-full -m-24">

        <!-- Contenido específico de esta página -->
        <div class="flex flex-wrap">
          <div class="w-full lg:w-8/12 px-4">
            <card-settings />
          </div>
          <div class="w-full lg:w-4/12 px-4">
            <card-profile />
          </div>
        </div>

        <footer-admin />
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar/Sidebar.vue";
import AdminNavbar from "@/components/Navbars/AdminNavbar.vue";
import HeaderStats from "@/components/Headers/HeaderStats.vue";
import FooterAdmin from "@/components/Footers/FooterAdmin.vue";

import CardSettings from '@/components/Cards/CardSettings.vue';
import CardProfile from '@/components/Cards/CardProfile.vue';

export default {
  name: 'settings-page',
  components: {
    Sidebar,
    AdminNavbar,
    HeaderStats,
    FooterAdmin,
    CardSettings,
    CardProfile
  }
}
</script>
