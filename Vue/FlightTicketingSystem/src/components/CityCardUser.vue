<template>
  <div
    class="rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-transform hover:scale-[1.03] w-[280px] bg-white"
  >
    <!-- 圖片 -->
    <img
      :src="city.image || 'https://placehold.co/280x160?text=No+Image'"
      class="w-full h-40 object-cover"
    />

    <!-- 名稱/國家 -->
    <div class="p-4 text-center">
      <h2 class="text-lg font-semibold text-gray-800">{{ city.name }}</h2>
      <p class="text-sm text-gray-500">{{ city.country }}</p>

      <!-- 按鈕列 -->
      <div class="mt-4 flex justify-center gap-6">
        <!-- 查看景點 -->
        <button @click="$emit('show-attractions', city)" title="查看景點">
          <i class="mdi mdi-eye text-xl text-blue-600 hover:text-blue-800"></i>
        </button>
        <!-- 設為航班目的地 -->
        <button @click="$emit('set-destination', city.name)" title="設為目的地">
          <i
            class="mdi mdi-airplane text-xl text-green-600 hover:text-green-800"
          ></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  city: Object,
});
defineEmits(["show-attractions", "set-destination"]);
</script>
