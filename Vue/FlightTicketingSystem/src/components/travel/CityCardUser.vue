<template>
  <div
    class="rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-transform hover:scale-[1.03] w-[280px] bg-white">
    <!-- 圖片 -->
    <v-img
      :src="`http://localhost:8080${city.image}`"
      :alt="city.name"
      height="180"
      cover
      class="rounded-t-xl" />

    <!-- 名稱/國家 -->
    <div class="p-4 text-center">
      <h2 class="text-lg font-semibold text-gray-800">{{ city.name }}</h2>
      <p class="text-sm text-gray-500">{{ city.country }}</p>

      <!-- 按鈕列 -->
      <div class="mt-4 flex justify-center gap-6">
        <!-- 查看景點 -->
        <v-btn
          color="primary"
          variant="outlined"
          size="small"
          @click="travel.openAttractions(city)">
          <i class="mdi mdi-eye text-xl text-blue-600 hover:text-blue-800"></i>
          查看景點
        </v-btn>
        <!-- 設為航班目的地 -->
        <v-btn
          color="green"
          variant="outlined"
          size="small"
          @click="travel.setDestination(city.name)">
          <i
            class="mdi mdi-airplane text-xl text-green-600 hover:text-green-800"></i>
          設為目的地
        </v-btn>
      </div>
    </div>
  </div>

  <v-dialog v-model="travel.showAttractionModal" max-width="700">
    <template #default>
      <v-card class="pa-6 elevation-0">
        <v-card-title class="text-h6 font-bold">
          {{ travel.currentCity.name }} 的熱門景點
        </v-card-title>

        <v-card-text>
          <!-- Swiper 景點主輪播 -->
          <div class="relative">
            <div
              class="absolute -left-10 top-1/2 -translate-y-1/2 z-50 swiper-custom-prev cursor-pointer">
              <v-icon size="32">mdi-chevron-left</v-icon>
            </div>
            <div
              class="absolute -right-10 top-1/2 -translate-y-1/2 z-9999 swiper-custom-next cursor-pointer">
              <v-icon size="32">mdi-chevron-right</v-icon>
            </div>
            <swiper
              :slides-per-view="1"
              :space-between="16"
              :modules="[Pagination, Navigation]"
              pagination
              :navigation="{
                nextEl: '.swiper-custom-next',
                prevEl: '.swiper-custom-prev',
              }"
              loop
              class="rounded-lg">
              <swiper-slide
                v-for="(attraction, index) in travel.currentCity.attractions"
                :key="attraction.id || index">
                <v-card
                  flat
                  class="d-flex flex-column justify-between h-full px-6 py-4">
                  <div>
                    <!-- 大圖 + 縮圖預覽 -->
                    <div class="mb-4">
                      <swiper
                        :modules="[Thumbs]"
                        :thumbs="{ swiper: thumbs[index] }"
                        :slides-per-view="1"
                        class="rounded mb-2">
                        <swiper-slide
                          v-for="(photo, j) in attraction.photos"
                          :key="photo.id">
                          <v-img
                            :src="`http://localhost:8080${photo.url}`"
                            cover
                            class="rounded"
                            height="250">
                            <template #placeholder>
                              <div
                                class="flex items-center justify-center h-full bg-gray-100">
                                載入中...
                              </div>
                            </template>
                            <template #default> </template>
                          </v-img>
                        </swiper-slide>
                      </swiper>

                      <!-- 小縮圖 swiper -->
                      <swiper
                        :modules="[Thumbs, FreeMode]"
                        slides-per-view="4"
                        free-mode
                        watch-slides-progress
                        @swiper="(swiper) => (thumbs[index] = swiper)"
                        class="rounded-sm">
                        <swiper-slide
                          v-for="(photo, j) in attraction.photos"
                          :key="photo.id + '-thumb'">
                          <v-img
                            :src="`http://localhost:8080${photo.url}`"
                            height="60"
                            class="rounded-sm border border-gray-300"
                            cover />
                        </swiper-slide>
                      </swiper>
                    </div>

                    <h3 class="text-lg font-semibold mb-2">
                      {{ attraction.name }}
                    </h3>
                    <p class="text-sm text-gray-600 mb-1">
                      ⭐ 評價：{{ attraction.rating }}
                    </p>
                    <p class="text-sm text-gray-600 mb-1">
                      📍 地址：{{ attraction.address }}
                    </p>
                    <p class="text-sm text-gray-600 mb-1">
                      🏷️ 分類：{{ attraction.category?.join(", ") }}
                    </p>
                    <p class="text-sm mt-2 text-gray-700">
                      {{ attraction.description }}
                    </p>
                  </div>
                </v-card>
              </swiper-slide>
            </swiper>
          </div>
        </v-card-text>

        <v-card-actions class="justify-end mt-4">
          <v-btn @click="travel.showAttractionModal = false" text color="grey">
            關閉
          </v-btn>
        </v-card-actions>
      </v-card>
    </template>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import { useTravelStore } from "@/stores/travelStore";
import { Swiper, SwiperSlide } from "swiper/vue";
import { Pagination, Thumbs, FreeMode, Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/thumbs";
import "swiper/css/free-mode";
import "swiper/css/navigation";

defineProps({
  city: Object,
});

const travel = useTravelStore();
const thumbs = ref([]);
</script>

<style>
.v-overlay__scrim {
  background-color: rgba(33, 33, 33, 0.25) !important;
}
.swiper-pagination-bullet {
  background: rgba(0, 0, 0, 0.5);
  width: 8px;
  height: 8px;
  opacity: 1;
}
.swiper-pagination-bullet-active {
  background: black;
}
</style>
