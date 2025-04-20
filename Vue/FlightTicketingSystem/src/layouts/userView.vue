<template>
  <!-- Header -->
  <Header></Header>

  <!-- Main Content -->
  <main class="mt-10">
    <!-- Title -->
    <h1 class="text-4xl font-extrabold text-center mb-8">易趣輕鬆飛</h1>

    <!-- Tabs -->
    <NavigationTabsUser v-model:selectedTab="selectedTab"></NavigationTabsUser>

    <!--Image-->
    <HomePageImage></HomePageImage>
    <!-- Footer -->
    <Footer></Footer>
  </main>
</template>

<script setup>
import { ref } from "vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import NavigationTabsUser from "@/components/travel/NavigationTabsUser.vue";
import HomePageImage from "@/components/HomePageImage.vue";

const selectedTab = ref("航班");
</script>

<style scoped></style>
