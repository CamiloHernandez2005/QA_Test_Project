import apiClient from "@/axios";

// Servicio para usuarios
export default {
  getUsers() {
    return apiClient.get("/users");
  },
};
