<template>
  <!-- Header -->
  <header class="sticky top-0 z-50 bg-white shadow-sm py-4 px-4">
    <div class="max-w-screen-xl mx-auto flex items-center justify-between">
      <div class="flex items-center">
        <img
          src="@/assets/Easytrip_text.png"
          alt="Easytrip Logo"
          class="w-36 h-auto object-contain transition-transform duration-300 hover:scale-105" />
      </div>
      <nav class="hidden md:flex space-x-8 text-base font-semibold">
        <a
          href="#"
          class="relative group text-gray-700 hover:text-black transition-colors duration-200">
          探索
          <span
            class="absolute bottom-0 left-0 w-0 h-0.5 bg-black transition-all duration-300 group-hover:w-full"></span>
        </a>
        <a
          href="#"
          class="relative group text-gray-700 hover:text-black transition-colors duration-200">
          會員
          <span
            class="absolute bottom-0 left-0 w-0 h-0.5 bg-black transition-all duration-300 group-hover:w-full"></span>
        </a>
        <a
          href="#"
          class="relative group text-gray-700 hover:text-black transition-colors duration-200">
          里程
          <span
            class="absolute bottom-0 left-0 w-0 h-0.5 bg-black transition-all duration-300 group-hover:w-full"></span>
        </a>
      </nav>
      <div class="flex items-center gap-4 text-base">
        <span class="mdi mdi-web text-xl text-gray-700"></span>
        <img
          src="https://flagcdn.com/tw.svg"
          width="30"
          alt="Taiwan"
          class="rounded transition-transform duration-300 hover:scale-105" />
        <span>TWD</span>
        <img
          src="@/assets/Easytrip.png"
          alt="User Avatar"
          class="rounded-full w-10 h-10 transition-transform duration-300 hover:scale-110" />
      </div>
    </div>
  </header>

  <!-- Main Content -->
  <main class="mt-10">
    <!-- Title -->
    <h1 class="text-4xl font-extrabold text-center mb-8">易趣輕鬆飛</h1>

    <!-- Tabs -->
    <div class="flex justify-center gap-6 text-base font-semibold mb-8">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        @click="selectedTab = tab.name"
        class="relative group px-4 py-2 transition-colors duration-300"
        :class="
          selectedTab === tab.name ? 'text-black font-bold' : 'text-gray-500'
        ">
        <span class="flex items-center gap-1">
          <i :class="['mdi', tab.icon]"></i>
          {{ tab.name }}
        </span>
        <span
          class="absolute left-0 bottom-0 h-0.5 bg-black transition-all duration-300"
          :class="
            selectedTab === tab.name ? 'w-full' : 'w-0 group-hover:w-full'
          ">
        </span>
      </button>
    </div>

    <!-- Search Bar -->
    <div
      v-show="selectedTab === '航班'"
      class="flex flex-wrap justify-center items-center gap-4 mb-10">
      <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-52 shadow-sm">
        <span class="mr-2 mdi mdi-airplane-takeoff text-gray-500"></span>
        <strong>From:</strong>
        <input
          type="text"
          placeholder="Origin"
          class="bg-transparent outline-none ml-2 w-full placeholder:text-gray-400" />
      </div>

      <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-52 shadow-sm">
        <span class="mr-2 mdi mdi-airplane-landing text-gray-500"></span>
        <strong>To:</strong>
        <input
          type="text"
          placeholder="Destination"
          class="bg-transparent outline-none ml-2 w-full placeholder:text-gray-400" />
      </div>

      <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-64 shadow-sm">
        <span class="mr-2 mdi mdi-calendar-range text-gray-500"></span>
        <input
          type="text"
          ref="dateInput"
          placeholder="Start → End"
          class="bg-transparent outline-none w-full placeholder:text-gray-400"
          readonly />
      </div>

      <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-40 shadow-sm">
        <span class="mr-2 mdi mdi-account-multiple text-gray-500"></span>
        <span>1 Traveler</span>
      </div>

      <button
        class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md">
        搜尋
      </button>
    </div>

    <!-- 禮品用：禮品欄位 -->
    <div
      v-show="selectedTab === '禮品'"
      class="flex justify-center items-center gap-4 mb-10">
      <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-full max-w-2xl shadow-sm">
        <span class="mr-2 mdi mdi-gift-outline text-gray-500"></span>
        <input
          type="text"
          placeholder="請輸入禮品關鍵字..."
          class="bg-transparent outline-none w-full placeholder:text-gray-400" />
      </div>
      <button
        class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md">
        搜尋
      </button>
    </div>

    <!-- 發現更多目的地用：目的地欄位 -->
    <div v-show="selectedTab === '發現更多目的地'">
      <UserSearchBar v-model="searchQuery" />
    </div>

    <router-view />

    <!-- Footer -->
    <footer class="bg-gray-50 border-t border-gray-200 py-10 mt-16">
      <div
        class="max-w-screen-xl mx-auto px-4 grid grid-cols-1 md:grid-cols-4 gap-8 text-sm text-gray-600">
        <!-- About -->
        <div>
          <h3 class="text-base font-semibold mb-3">關於我們</h3>
          <ul class="space-y-2">
            <li><a href="#" class="hover:underline">公司簡介</a></li>
            <li><a href="#" class="hover:underline">媒體報導</a></li>
            <li><a href="#" class="hover:underline">合作夥伴</a></li>
          </ul>
        </div>

        <!-- Explore -->
        <div>
          <h3 class="text-base font-semibold mb-3">探索</h3>
          <ul class="space-y-2">
            <li><a href="#" class="hover:underline">熱門景點</a></li>
            <li><a href="#" class="hover:underline">熱門城市</a></li>
            <li><a href="#" class="hover:underline">即將出發</a></li>
          </ul>
        </div>

        <!-- Support -->
        <div>
          <h3 class="text-base font-semibold mb-3">支援</h3>
          <ul class="space-y-2">
            <li><a href="#" class="hover:underline">聯絡我們</a></li>
            <li><a href="#" class="hover:underline">常見問題</a></li>
            <li><a href="#" class="hover:underline">取消政策</a></li>
          </ul>
        </div>

        <!-- Follow Us -->
        <div>
          <h3 class="text-base font-semibold mb-3">追蹤我們</h3>
          <div class="flex space-x-4">
            <a
              href="#"
              class="mdi mdi-facebook text-2xl text-gray-600 hover:text-blue-600"></a>
            <a
              href="#"
              class="mdi mdi-instagram text-2xl text-gray-600 hover:text-pink-500"></a>
            <a
              href="#"
              class="mdi mdi-twitter text-2xl text-gray-600 hover:text-blue-400"></a>
          </div>
        </div>
      </div>

      <div class="mt-10 text-center text-xs text-gray-400">
        © 2025 Easytrip. All rights reserved.
      </div>
    </footer>
  </main>
</template>

<script setup>
import { ref, onMounted } from "vue";
import flatpickr from "flatpickr";
import "flatpickr/dist/flatpickr.min.css";
import UserSearchBar from "@/components/UserSearchBar.vue";

const selectedTab = ref("航班");
const searchQuery = ref("");

const tabs = [
  { name: "航班", icon: "mdi-airplane" },
  { name: "禮品", icon: "mdi-gift" },
  { name: "發現更多目的地", icon: "mdi-map-marker-radius" },
];

const dateInput = ref(null);

onMounted(() => {
  flatpickr(dateInput.value, {
    mode: "range",
    minDate: "today",
    dateFormat: "M d",
    defaultDate: [new Date(), new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)],
    onChange: (dates, str) => {
      console.log("Selected range:", str);
    },
  });
});
</script>

<style scoped>
input[readonly] {
  cursor: pointer;
}
a span {
  display: block;
  height: 2px;
  background: black;
  transition: width 0.3s ease;
}
</style>
