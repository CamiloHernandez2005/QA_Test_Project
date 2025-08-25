import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import '@/assets/styles/main.css';
import 'primeicons/primeicons.css';

// Importa la versión clara
import Lara from '@primeuix/themes/lara';
import '@fortawesome/fontawesome-free/css/all.min.css'



import App from './App.vue';
import router from './router';

const app = createApp(App);

app.use(PrimeVue, {
  theme: {
    preset: Lara,
    options: {
      darkModeSelector: false // fuerza modo claro
    }
  }
})

app.use(ToastService);
app.use(createPinia());
app.use(router);

app.mount('#app');
