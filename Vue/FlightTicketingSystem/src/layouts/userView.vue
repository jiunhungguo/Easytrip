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
    <div class="relative w-[67%] mx-auto rounded-3xl overflow-hidden shadow-lg">
      <img
        src="@/assets/ArcticTern.webp"
        alt="Dreamy travel background"
        class="w-full h-full object-cover"
      />

      <!-- Text -->
      <div class="absolute inset-0 flex mt-12 justify-start">
        <h1
          class="text-white text-4xl md:text-6xl font-semibold drop-shadow-md px-4 text-left pl-12"
        >
          You are one step away from<br />
          your dream trip
        </h1>
      </div>
    </div>
    <!-- Footer -->
    <Footer></Footer>
  </main>
</template>

<script setup>
import { ref } from "vue";
import "flatpickr/dist/flatpickr.min.css";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import NavigationTabsUser from "@/components/travel/NavigationTabsUser.vue";

const selectedTab = ref("航班");
</script>

<style scoped></style>
