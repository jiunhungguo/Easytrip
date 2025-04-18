<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title>編輯景點</v-card-title>
      <v-card-text>
        <v-form ref="formRef" v-model="valid">
          <!-- 1. 顯示現有圖片 -->
          <div v-if="photos.length" class="mb-4">
            <v-carousel
              height="200"
              hide-delimiter-background
              delimiter-icon="mdi-circle"
              show-arrows="hover"
              cycle
              class="rounded">
              <v-carousel-item v-for="(p, i) in photos" :key="i">
                <v-img :src="p.url" height="200" cover />
              </v-carousel-item>
            </v-carousel>
          </div>

          <!-- 3. 其他欄位 -->
          <v-text-field v-model="form.name" label="名稱" :rules="[r]" />
          <v-select
            v-model="form.cityId"
            :items="cities"
            item-title="name"
            item-value="id"
            label="城市"
            :rules="[r]" />
          <v-text-field v-model="form.address" label="地址" :rules="[r]" />
          <v-text-field
            v-model.number="form.rating"
            label="評分 (0–5)"
            type="number"
            :rules="[r, ratingRule]" />
          <v-textarea
            v-model="form.description"
            label="描述"
            :rules="[r]"
            rows="2" />
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
          <v-text-field v-model="categoryInput" label="分類（用逗號分隔）" />
          <!-- 2. 上傳新圖片 -->
          <v-file-input
            v-model="newImage"
            label="上傳新圖片（可選）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            clearable
            @change="previewImage" />
          <v-img
            v-if="previewUrl"
            :src="previewUrl"
            height="160"
            class="mb-3 rounded"
            cover />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          variant="outlined"
          color="primary"
          :loading="loading"
          @click="submit"
          >儲存</v-btn
        >
        <v-btn variant="outlined" text @click="dialog = false">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from "vue";
import axios from "axios";

const props = defineProps({
  modelValue: Boolean,
  attraction: Object,
  cities: Array,
});
const emit = defineEmits(["update:modelValue", "updated"]);

const dialog = ref(props.modelValue);
const valid = ref(true);
const formRef = ref();
const loading = ref(false);

const form = ref({
  name: "",
  cityId: null,
  address: "",
  rating: 0,
  description: "",
  latitude: 0,
  longitude: 0,
  category: [],
});

const photos = ref([]); // 原始圖片
const newImage = ref(null); // 新圖片
const previewUrl = ref(""); // 新圖片預覽

const categoryInput = ref("");

watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(
  () => dialog.value,
  (val) => emit("update:modelValue", val)
);

watch(
  () => props.attraction,
  async (a) => {
    if (a) {
      form.value = {
        name: a.name,
        cityId: props.cities.find((c) => c.name === a.city)?.id ?? null,
        address: a.address,
        rating: a.rating,
        description: a.description,
        latitude: a.latitude,
        longitude: a.longitude,
        category: Array.isArray(a.category) ? [...a.category] : [],
      };
      categoryInput.value = form.value.category.join(", ");
      newImage.value = null;
      previewUrl.value = "";

      try {
        const res = await axios.get(
          `http://localhost:8080/photos/attraction/${a.id}`
        );
        photos.value = res.data.map((p) => ({
          ...p,
          url: p.url.startsWith("http")
            ? p.url
            : `http://localhost:8080${p.url}`,
        }));
      } catch (err) {
        photos.value = [];
        console.warn("⚠️ 圖片載入失敗", err);
      }
    }
  },
  { immediate: true }
);

const r = (v) => !!v || "必填";
const ratingRule = (v) => (v >= 0 && v <= 5) || "評分必須 0~5";

const previewImage = () => {
  if (newImage.value instanceof File) {
    previewUrl.value = URL.createObjectURL(newImage.value);
  }
};

const submit = async () => {
  const result = await formRef.value?.validate();
  if (!result) return;
  loading.value = true;

  try {
    const payload = {
      ...form.value,
      category: categoryInput.value
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean),
    };

    await axios.put(
      `http://localhost:8080/attractions/${props.attraction.id}`,
      payload
    );

    if (newImage.value instanceof File) {
      const imageData = new FormData();
      imageData.append("image", newImage.value);

      const uploadRes = await axios.post(
        "http://localhost:8080/photos/upload",
        imageData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      const uploadedUrl = uploadRes.data.url;

      await axios.post("http://localhost:8080/photos", {
        attractionId: props.attraction.id,
        url: uploadedUrl,
        caption: form.value.name,
      });
    }

    emit("updated");
    dialog.value = false;
  } catch (e) {
    console.error("更新或圖片上傳失敗", e);
  } finally {
    loading.value = false;
  }
};
</script>
