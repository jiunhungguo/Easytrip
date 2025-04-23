<template>
  <!-- Search bar -->
  <div class="flex justify-center items-center gap-4 mb-10">
    <button
      @click="setMode('AI')"
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md"
    >
      智能推薦
    </button>
    <button
      @click="setMode('Favourite')"
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md"
    >
      我的最愛
    </button>
    <button
      @click="setMode('All')"
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md"
    >
      所有城市
    </button>
    <button
      @click="setMode('Calendar')"
      class="bg-green-500 hover:bg-green-600 text-white font-bold px-6 py-3 rounded-full transition duration-300 shadow-md"
    >
      行程計劃
    </button>
  </div>

  <div v-if="currentMode === 'All'" class="mx-auto" style="padding-left: 7em">
    <CityCardGrid :cities="allCities" />
  </div>

  <div v-if="currentMode === 'Favourite'">
    <div
      v-if="favouriteCities.length > 0"
      class="mx-auto"
      style="padding-left: 7em"
    >
      <CityCardGrid :cities="favouriteCities" />
    </div>
    <div v-else class="text-center text-gray-500 mt-10">
      暫無收藏的城市，快去探索一下吧！
    </div>
  </div>

  <div v-if="currentMode === 'AI'" class="p-6 max-w-xl mx-auto">
    <h2 class="text-xl font-bold mb-4">智能推薦景點</h2>

    <textarea
      v-model="userPrompt"
      rows="4"
      placeholder="請輸入您的需求"
      class="w-full p-3 border border-gray-300 rounded mb-4 resize-none"
    ></textarea>

    <button
      @click="getRecommendation"
      :disabled="loading || !userPrompt"
      class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded disabled:opacity-50 transition"
    >
      {{ loading ? "正在思考中..." : "智能推薦" }}
    </button>

    <div v-if="recommendation" class="mt-6 space-y-4">
      <h3 class="text-lg font-semibold">AI 推薦：</h3>
      <div
        v-for="(item, index) in recommendation"
        :key="index"
        class="p-4 border border-gray-200 rounded shadow-sm"
      >
        <p class="font-bold">{{ item.name }}</p>
        <p class="text-sm text-gray-600">{{ item.attractions }}</p>
        <div class="flex justify-end mt-4">
          <v-btn
            size="small"
            color="blue"
            variant="outlined"
            @click="bookCity(item.name)"
          >
            <i class="mdi mdi-airplane mr-2"></i> 設為目的地
          </v-btn>
        </div>
      </div>
    </div>
  </div>
  <div v-if="currentMode === 'Calendar'">
    <FullCalendarBasic />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watchEffect, nextTick } from "vue";
import { useCityStore } from "@/stores/cityStore";
import { useTravelStore } from "@/stores/travelStore";
import { useFavouriteStore } from "@/stores/favourtieStore";
import CityCardGrid from "./CityCardGridUser.vue";
import axios from "axios";
import FullCalendarBasic from "@/components/travel/BasicCalendar.vue";

const cityStore = useCityStore();
const travel = useTravelStore();
const favouriteStore = useFavouriteStore();

const allCities = ref([]);
const currentMode = ref("");

const favouriteCities = computed(() => {
  const cityMap = new Map(cityStore.cities.map((city) => [city.id, city]));
  return favouriteStore.allFavourites
    .map((fav) => cityMap.get(fav.cityId))
    .filter((city) => city);
});

const setMode = async (mode) => {
  travel.hideImage();
  currentMode.value = mode;

  if (mode === "Favourite") {
    await favouriteStore.loadFavourites();
  }
};

const userPrompt = ref("");
const loading = ref(false);
const recommendation = ref([]);

const bookCity = (cityName) => {
  travel.setDestination(cityName);
  travel.selectedTab = "航班";
};

const getRecommendation = async () => {
  loading.value = true;
  recommendation.value = [];

  try {
    const res = await axios.post(
      "https://openrouter.ai/api/v1/chat/completions",
      {
        model: "deepseek/deepseek-chat:free",
        messages: [
          {
            role: "system",
            content:
              '你是一位旅遊推薦助手，請根據使用者的需求推薦 5 個城市，必須使用 JSON 格式回應，例如 [{"name": "台北", "attractions": "台北101，士林夜市，故宮博物院，象山，台北動物園"}]',
          },
          {
            role: "user",
            content: userPrompt.value,
          },
        ],
      },
      {
        headers: {
          Authorization: `Bearer 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`,
          "HTTP-Referer": "http://localhost:5173",
        },
      }
    );

    const text = res.data.choices[0].message.content;
    console.log(text);

    try {
      recommendation.value = JSON.parse(text);
    } catch (e) {
      recommendation.value = [{ name: "回傳格式錯誤", description: text }];
    }
  } catch (err) {
    console.error("推薦失敗：", err);
    recommendation.value = [
      {
        name: "無法取得推薦",
        description: "可能是 API 金鑰錯誤或網路有問題",
      },
    ];
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await cityStore.fetchCities();
  allCities.value = cityStore.cities;
});
</script>
<style scoped></style>
