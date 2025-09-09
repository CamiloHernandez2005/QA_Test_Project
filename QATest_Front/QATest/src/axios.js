import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080", // 👉 aquí va la URL de tu backend
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
