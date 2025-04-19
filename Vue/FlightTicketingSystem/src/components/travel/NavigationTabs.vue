<template>
  <div class="text-center my-6">
    <h1 class="text-4xl font-bold mb-4">請選擇操作</h1>

    <!-- 根據選擇的 tab 顯示內容 -->
    <div v-if="modelValue === 'cities'">
      <p class="text-lg"><v-icon class="mb-2">mdi-city</v-icon>查詢城市</p>
    </div>

    <div v-else-if="modelValue === 'addCities'">
      <p class="text-lg"><v-icon class="mb-2">mdi-plus-box</v-icon>增加城市</p>
    </div>

    <div v-else-if="modelValue === 'allCities'">
      <p class="text-lg"><v-icon class="mb-2">mdi-domain</v-icon>全部城市</p>
    </div>

    <div v-else-if="modelValue === 'attractions'">
      <p class="text-lg">
        <v-icon class="mb-2">mdi-map-marker</v-icon>查詢景點
      </p>
    </div>

    <div v-else-if="modelValue === 'addAttractions'">
      <p class="text-lg">
        <v-icon class="mb-2">mdi-plus-circle</v-icon>增加景點
      </p>
    </div>

    <div v-else-if="modelValue === 'allAttractions'">
      <p class="text-lg">
        <v-icon class="mb-2">mdi-image-filter-hdr</v-icon>全部景點
      </p>
    </div>
  </div>

  <!-- dropdowns -->
  <div class="flex justify-center gap-4 mb-6">
    <div class="dropdown-wrapper">
      <v-select
        v-model="selectedAction"
        :items="actions[selectedCategory]"
        item-title="label"
        item-value="value"
        label="選擇操作"
        :prepend-inner-icon="
          actionIcons[selectedCategory][selectedAction] || 'mdi-cog'
        "
        dense
        outlined
      />
    </div>
    <!-- 類別下拉 -->
    <div class="dropdown-wrapper">
      <v-select
        v-model="selectedCategory"
        :items="categories"
        item-title="label"
        item-value="value"
        label="選擇類別"
        prepend-inner-icon="mdi-menu"
        dense
        outlined
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

// 🍬 Vue 3 的 defineModel 語法糖，取代 props + emits
const modelValue = defineModel("selectedTab"); // 這就是 selectedTab 的 v-model

// 分類 + 動作的下拉選擇
const selectedCategory = ref("cities");
const selectedAction = ref("query");

// 對應分類名稱
const categories = [
  { label: "城市", value: "cities" },
  { label: "景點", value: "attractions" },
];

// 每個分類下對應的操作
const actions = {
  cities: [
    { label: "查詢", value: "query" },
    { label: "增加", value: "add" },
    { label: "全部", value: "all" },
  ],
  attractions: [
    { label: "查詢", value: "query" },
    { label: "增加", value: "add" },
    { label: "全部", value: "all" },
  ],
};

// 下拉 icon
const actionIcons = {
  cities: {
    query: "mdi mdi-magnify",
    add: "mdi mdi-map-marker-plus",
    all: "mdi mdi-check-all",
  },
  attractions: {
    query: "mdi mdi-magnify",
    add: "mdi mdi-map-marker-plus",
    all: "mdi mdi-check-all",
  },
};

// 對應操作與 tab 的 map
const actionMap = {
  cities: {
    query: "cities",
    add: "addCities",
    all: "allCities",
  },
  attractions: {
    query: "attractions",
    add: "addAttractions",
    all: "allAttractions",
  },
};

// 一有動作就更新 modelValue（v-model）
watch([selectedCategory, selectedAction], () => {
  modelValue.value = actionMap[selectedCategory.value][selectedAction.value];
});
</script>

<style>
.dropdown-container {
  justify-content: center;
}

.dropdown-wrapper {
  max-width: 360px; /* 跟你搜索框差不多宽 */
  width: 100%;
}
</style>
