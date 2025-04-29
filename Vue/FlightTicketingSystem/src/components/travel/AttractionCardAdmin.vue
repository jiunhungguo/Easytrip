<template>
  <v-card
    class="mx-auto hover:shadow-xl hover:scale-[1.02]"
    max-width="320"
    elevation="3"
    rounded="xl">
    <!-- 圖片 -->
    <v-carousel
      v-if="photos.length > 0"
      height="180"
      hide-delimiter-background
      delimiter-icon="mdi-circle"
      show-arrows="hover"
      cycle
      class="rounded-t-xl">
      <v-carousel-item v-for="(photo, i) in photos" :key="i">
        <v-img :src="photo.url" height="180" cover />
      </v-carousel-item>
    </v-carousel>

    <!-- 名稱 / 地址 / 評分 / 分類 -->
    <v-card-title class="text-center text-lg font-semibold py-2">
      {{ attraction.name }}
    </v-card-title>
    <v-card-subtitle class="text-center pb-2">
      {{ attraction.address }}
    </v-card-subtitle>
    <v-card-text class="text-center">
      <v-rating
        :model-value="attraction.rating"
        color="yellow-darken-3"
        dense
        readonly
        half-increments
        size="18" />
      <div class="text-caption text-grey mt-1">
        {{ attraction.category?.join(", ") }}
      </div>
    </v-card-text>

    <!-- 按鈕（原本 actions 放回來） -->
    <v-card-actions class="justify-center">
      <v-btn color="primary" variant="outlined" @click="editDialog = true">
        修改
      </v-btn>
      <v-btn
        color="error"
        variant="outlined"
        class="ml-2"
        @click="deleteDialog = true">
        刪除
      </v-btn>
    </v-card-actions>

    <EditAttractionModal
      v-model="editDialog"
      :attraction="attraction"
      :cities="cities"
      @updated="$emit('edit')" />
  </v-card>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import EditAttractionModal from "./EditAttractionModal.vue";

const props = defineProps({ attraction: Object, cities: Array });
const emit = defineEmits(["delete", "edit"]);

const photos = ref([]);
const deleteDialog = ref(false);
const editDialog = ref(false);
const valid = ref(true);
const loading = ref(false);
const formRef = ref();

const form = reactive({
  name: props.attraction.name,
  cityName: props.attraction.city,
  address: props.attraction.address,
  description: props.attraction.description,
  rating: props.attraction.rating,
  category: Array.isArray(props.attraction.category)
    ? [...props.attraction.category]
    : [],
  latitude: props.attraction.latitude,
  longitude: props.attraction.longitude,
  imageFile: null,
});

const categoryInput = ref(form.category.join(", "));

const r = (v) => !!v || "必填";
const ratingRule = (v) => (v >= 0 && v <= 5) || "評分必須在 0 到 5 之間";

const confirmDelete = async () => {
  try {
    await axios.delete(
      `http://localhost:8080/attractions/${props.attraction.id}`
    );
    emit("delete");
    deleteDialog.value = false;
  } catch (err) {
    console.error("刪除失敗", err);
  }
};

onMounted(async () => {
  try {
    const res = await axios.get(
      `http://localhost:8080/photos/attraction/${props.attraction.id}`
    );
    photos.value = res.data.map((p) => ({
      ...p,
      url: p.url.startsWith("http") ? p.url : `http://localhost:8080${p.url}`,
    }));
  } catch (err) {
    console.error("圖片載入失敗", err);
  }
});
</script>
