<template>
  <v-app>
    <RouterView></RouterView>
  </v-app>
</template>

<script setup>
import { onMounted } from "vue";
import { useAuthStore } from "./stores/auth";

const authStore = useAuthStore();

onMounted(() => {
  // 應用啟動時檢查並恢復登入狀態
  authStore.checkLoginStatus();
});
</script>

<style></style>
