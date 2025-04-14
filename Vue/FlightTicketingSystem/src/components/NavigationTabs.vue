<template>
  <div class="text-center my-6">
    <h1 class="text-4xl font-bold mb-4">請選擇操作</h1>

    <v-tabs
      v-model="selectedTab"
      align-tabs="center"
      color="black"
      class="mb-6"
      slider-color="black">
      <v-tab value="cities">
        <v-icon class="mr-2">mdi-city</v-icon> 查詢城市
      </v-tab>
      <v-tab value="attractions">
        <v-icon class="mr-2">mdi-map-marker</v-icon> 查詢景點
      </v-tab>
      <v-tab value="allCities">
        <v-icon class="mr-2">mdi-domain</v-icon> 全部城市
      </v-tab>
      <v-tab value="allAttractions">
        <v-icon class="mr-2">mdi-image-filter-hdr</v-icon> 全部景點
      </v-tab>
      <v-tab value="addCities">
        <v-icon class="mr-2">mdi-plus-box</v-icon> 增加城市
      </v-tab>
      <v-tab value="addAttractions">
        <v-icon class="mr-2">mdi-plus-circle</v-icon> 增加景點
      </v-tab>
    </v-tabs>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  selectedTab: String,
});
const emit = defineEmits(["select-tab"]);

const selectedTab = ref(props.selectedTab);

watch(selectedTab, (val) => {
  emit("select-tab", val);
});
</script>
