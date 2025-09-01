import axios from 'axios';

let isConfigLoaded = false;

async function loadAxiosConfig() {
  if (!isConfigLoaded) {
    try {
      const response = await fetch('/config.json');
      const config = await response.json();

      axios.defaults.baseURL = config.baseURL;

      axios.defaults.withCredentials = true;

      isConfigLoaded = true;
    } catch (error) {
      console.error('Error cargando config.json:', error);
    }
  }
}

axios.interceptors.request.use(
  async (config) => {
    if (!isConfigLoaded) {
      await loadAxiosConfig();
    }
    return config;
  },
  error => Promise.reject(error)
);

axios.interceptors.response.use(
  response => response,
  error => {
    const status = error.response?.status;
    const data = error.response?.data;
    const code = data?.code || data?.message || data;

    if (status === 401) {
      const isAuthError =
        code === 'TOKEN_INVALID' ||
        (typeof code === 'string' && code.toLowerCase().includes('expirado'));

      if (isAuthError) {
        if (!window.__loggingOut) {
          window.__loggingOut = true;
          fetch(`${axios.defaults.baseURL}/auth/logout`, {
            method: 'POST',
            credentials: 'include'
          }).finally(() => {
            window.location.href = '/';
          });
        }
        return;
      }
    }

    return Promise.reject(error);
  }
);

export default axios;
