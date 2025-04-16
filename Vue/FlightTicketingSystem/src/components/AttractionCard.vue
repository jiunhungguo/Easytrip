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

    <!-- 按鈕 -->
    <AttractionCardActions
      :attraction="attraction"
      @edit="editDialog = true"
      @delete="deleteDialog = true" />

    <!-- 🗑 刪除 Dialog -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card class="pa-4 rounded-lg">
        <v-card-title class="text-h6 font-weight-bold"
          >確定要刪除?</v-card-title
        >
        <v-card-text class="text-body-2">
          確定要刪除 <strong>{{ attraction.name }}</strong
          >？
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn variant="outlined" color="error" @click="confirmDelete"
            >刪除</v-btn
          >
          <v-btn variant="outlined" text @click="deleteDialog = false"
            >取消</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- ✏️ 編輯 Dialog -->
    <v-dialog v-model="editDialog" max-width="500">
      <v-card>
        <v-card-title>編輯景點</v-card-title>
        <v-card-text>
          <v-form ref="formRef" v-model="valid">
            <v-text-field v-model="form.name" label="景點名稱" :rules="[r]" />
            <v-select
              v-model="form.cityName"
              :items="cities.map((city) => city.name)"
              item-title="name"
              item-value="id"
              label="城市" />
            <v-text-field v-model="form.address" label="地址" :rules="[r]" />
            <v-text-field
              v-model.number="form.rating"
              label="評分 (0-5)"
              type="number"
              :rules="[r, ratingRule]" />
            <v-textarea
              v-model="form.description"
              label="描述"
              :rules="[r]"
              rows="3" />
            <v-text-field
              v-model.number="form.latitude"
              label="緯度"
              type="number"
              :rules="[r]" />
            <v-text-field
              v-model.number="form.longitude"
              label="經度"
              type="number"
              :rules="[r]" />
            <v-text-field
              v-model="categoryInput"
              label="分類 (用逗號分隔)"
              :rules="[r]" />
            <v-file-input
              v-model="form.imageFile"
              label="上傳新圖片（可選）"
              accept="image/*"
              prepend-icon=""
              prepend-inner-icon="mdi-camera"
              clearable />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn
            variant="outlined"
            color="primary"
            :loading="loading"
            @click="submitEdit"
            >確認</v-btn
          >
          <v-btn variant="outlined" text @click="editDialog = false"
            >取消</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import AttractionCardActions from "./AttractionCardActions.vue";

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

const submitEdit = async () => {
  const result = await formRef.value?.validate();
  if (!result) return;

  loading.value = true;
  try {
    const selectedCity = props.cities.find(
      (city) => city.name === form.cityName
    );

    // 更新 attraction 本體
    await axios.put(
      `http://localhost:8080/attractions/${props.attraction.id}`,
      {
        name: form.name,
        address: form.address,
        cityId: selectedCity?.id,
        description: form.description,
        rating: form.rating,
        category: categoryInput.value
          .split(",")
          .map((s) => s.trim())
          .filter(Boolean),
        latitude: form.latitude,
        longitude: form.longitude,
      }
    );

    // 圖片上傳（可選）
    if (form.imageFile) {
      const imageData = new FormData();
      imageData.append("image", form.imageFile);

      const uploadRes = await axios.post(
        "http://localhost:8080/photos/upload",
        imageData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      const uploadedUrl = uploadRes.data.url;

      await axios.post("http://localhost:8080/photos", {
        attractionId: props.attraction.id,
        url: uploadedUrl,
        caption: form.name,
      });
    }

    // ✅ 再次取得圖片資料，更新畫面
    const res = await axios.get(
      `http://localhost:8080/photos/attraction/${props.attraction.id}`
    );
    photos.value = res.data.map((p) => ({
      ...p,
      url: p.url.startsWith("http") ? p.url : `http://localhost:8080${p.url}`,
    }));

    emit("edit");
    editDialog.value = false;
  } catch (err) {
    console.error("更新或上傳失敗", err);
  } finally {
    loading.value = false;
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
