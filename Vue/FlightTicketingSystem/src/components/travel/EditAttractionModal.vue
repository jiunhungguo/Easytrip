<template>
  <v-dialog v-model="model" max-width="700">
    <v-card>
      <v-card-title class="text-h6 font-weight-bold">編輯景點</v-card-title>
      <v-card-text>
        <v-form ref="formRef" v-model="valid" lazy-validation>
          <!-- 圖片輪播 -->
          <v-carousel
            v-if="photos.length > 0"
            height="180"
            hide-delimiter-background
            delimiter-icon="mdi-circle"
            show-arrows="hover"
            cycle
            class="rounded-t-xl mt-4">
            <v-carousel-item v-for="(photo, i) in photos" :key="i">
              <v-img :src="photo.url" height="180" cover />
            </v-carousel-item>
          </v-carousel>
          <v-text-field v-model="form.name" label="名稱" :rules="[required]" />
          <v-select
            v-model="form.cityName"
            :items="cities.map((c) => c.name)"
            label="所屬城市"
            :rules="[required]" />
          <v-text-field
            v-model="form.address"
            label="地址"
            :rules="[required]" />
          <v-textarea v-model="form.description" label="描述" />

          <v-text-field
            v-model.number="form.rating"
            label="評分"
            type="number"
            :rules="[required, ratingRule]" />

          <v-combobox
            v-model="form.category"
            label="分類"
            multiple
            chips
            deletable-chips
            hint="用逗號分隔或手動新增"
            persistent-hint />

          <v-text-field
            v-model.number="form.latitude"
            label="緯度"
            type="number"
            :rules="[required, number]" />
          <v-text-field
            v-model.number="form.longitude"
            label="經度"
            type="number"
            :rules="[required, number]" />

          <!-- 多圖上傳 -->
          <v-file-input
            label="圖片（可多選）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            v-model="form.imageFiles"
            multiple
            show-size
            @change="previewImages" />
        </v-form>
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn
          :disabled="!valid || loading"
          color="primary"
          variant="outlined"
          @click="submit">
          <v-progress-circular
            v-if="loading"
            indeterminate
            size="18"
            width="2"
            color="white"
            class="mr-2" />
          確認
        </v-btn>
        <v-btn text variant="outlined" @click="close">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, reactive, watch } from "vue";
import axios from "axios";

const props = defineProps({
  modelValue: Boolean,
  attraction: Object,
  cities: Array,
});
const emit = defineEmits(["update:modelValue", "updated"]);

const model = ref(props.modelValue);
watch(
  () => props.modelValue,
  (val) => (model.value = val)
);
watch(model, (val) => emit("update:modelValue", val));

const valid = ref(false);
const loading = ref(false);
const formRef = ref(null);
const photos = ref([]);

const form = reactive({
  name: "",
  cityName: "",
  address: "",
  description: "",
  rating: 0,
  category: [],
  latitude: null,
  longitude: null,
  imageFiles: [],
});

watch(
  () => props.attraction,
  async (a) => {
    if (a?.id) {
      form.name = a.name || "";
      form.cityName = a.city || "";
      form.address = a.address || "";
      form.description = a.description || "";
      form.rating = a.rating || 0;
      form.category = Array.isArray(a.category) ? [...a.category] : [];
      form.latitude = a.latitude || 0;
      form.longitude = a.longitude || 0;
      form.imageFiles = [];

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
        console.error("圖片載入失敗", err);
        photos.value = [];
      }
    }
  },
  { immediate: true }
);

const required = (v) => !!v || "必填";
const number = (v) => (typeof v === "number" && !isNaN(v)) || "必須是數字";
const ratingRule = (v) => (v >= 0 && v <= 5) || "0 ~ 5 之間";

const previewImages = () => {
  const files = Array.isArray(form.imageFiles) ? form.imageFiles : [];
  const previews = files.map((f) => ({
    url: URL.createObjectURL(f),
    name: f.name,
  }));
  photos.value = previews;
};

const close = () => {
  model.value = false;
};

const submit = async () => {
  const isValid = await formRef.value?.validate();
  if (!isValid) return;

  loading.value = true;

  try {
    const selectedCity = props.cities.find(
      (city) => city.name === form.cityName
    );

    await axios.put(
      `http://localhost:8080/attractions/${props.attraction.id}`,
      {
        name: form.name,
        address: form.address,
        cityId: selectedCity?.id,
        description: form.description,
        rating: form.rating,
        category: form.category,
        latitude: form.latitude,
        longitude: form.longitude,
      }
    );

    if (form.imageFiles.length > 0) {
      for (const file of form.imageFiles) {
        const formData = new FormData();
        formData.append("image", file);

        const uploadRes = await axios.post(
          "http://localhost:8080/photos/upload",
          formData,
          { headers: { "Content-Type": "multipart/form-data" } }
        );

        const uploadedUrl = uploadRes.data.url;

        await axios.post("http://localhost:8080/photos", {
          attractionId: props.attraction.id,
          url: uploadedUrl,
          caption: form.name,
        });
      }
    }

    emit("updated");
    close();
  } catch (err) {
    console.error("景點更新失敗", err);
  } finally {
    loading.value = false;
  }
};
</script>
