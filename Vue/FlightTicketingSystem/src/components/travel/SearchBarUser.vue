<template>
  <!-- Search bar -->
  <div class="flex justify-center items-center gap-4 mb-10">
    <!-- <div
        class="flex items-center bg-gray-100 px-4 py-3 rounded-full text-sm w-full max-w-2xl shadow-sm">
        <span class="mr-2 mdi mdi-map-marker-outline text-gray-500"></span>
        <input
          v-model="modelValue"
          type="text"
          placeholder="請輸入城市、景點或地區名稱..."
          class="bg-transparent outline-none w-full placeholder:text-gray-400" />
      </div> -->
    <button
      @click="
        () => {
          travel.hideImage();
        }
      "
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md">
      智能推薦
    </button>
    <button
      @click="
        () => {
          travel.hideImage();
        }
      "
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md">
      我的最愛
    </button>
    <button
      @click="
        () => {
          travel.hideImage();
          handleSearch();
        }
      "
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md">
      所有城市
    </button>
  </div>

  <div v-if="searchAllCities" class="mx-auto" style="padding-left: 7em">
    <CityCardGrid :cities="allCities" />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useCityStore } from "@/stores/cityStore";
import { useAttractionStore } from "@/stores/attractionStore";
import { useTravelStore } from "@/stores/travelStore";
import CityCardGrid from "@/components/travel/CityCardGridUser.vue";
import AttractionCardGrid from "@/components/travel/AttractionCardGridAdmin.vue";

const modelValue = defineModel({ local: true });

const cityStore = useCityStore();
const attractionStore = useAttractionStore();
const travel = useTravelStore();

const allCities = ref([]);
const searchAllCities = ref(false);

const handleSearch = () => {
  searchAllCities.value = true;
};

onMounted(async () => {
  await cityStore.fetchCities();
  allCities.value = cityStore.cities;
});
</script>
