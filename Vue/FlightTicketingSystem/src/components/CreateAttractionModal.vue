<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title>新增景點</v-card-title>
      <v-card-text>
        <v-form ref="formRef" v-model="valid">
          <v-text-field v-model="form.name" label="景點名稱" :rules="[r]" />
          <v-select
            v-model="form.cityName"
            :items="cities.map((city) => city.name)"
            label="城市"
            :rules="[r]" />
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
            v-model="form.latitude"
            label="緯度"
            type="number"
            :rules="[r]" />
          <v-text-field
            v-model="form.longitude"
            label="經度"
            type="number"
            :rules="[r]" />
          <v-text-field
            v-model="categoryInput"
            label="分類 (用逗號分隔)"
            :rules="[r]" />
          <v-file-input
            v-model="form.imageFile"
            label="上傳圖片（必填）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            :rules="[r]" />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          color="primary"
          variant="outlined"
          :loading="loading"
          @click="submit"
          >確認</v-btn
        >
        <v-btn variant="outlined" @click="dialog = false">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const props = defineProps({
  cities: Array,
});
const emit = defineEmits(["created"]);

const dialog = ref(false);
const formRef = ref();
const valid = ref(true);
const loading = ref(false);

const form = ref({
  name: "",
  cityName: "",
  address: "",
  description: "",
  rating: null,
  latitude: null,
  longitude: null,
  imageFile: null,
});
const categoryInput = ref("");

const r = (v) => !!v || "必填";
const ratingRule = (v) => (v >= 0 && v <= 5) || "評分必須在 0 到 5 之間";

const submit = async () => {
  const result = await formRef.value?.validate();
  if (!result) return;

  try {
    loading.value = true;

    const selectedCity = props.cities.find(
      (city) => city.name === form.value.cityName
    );
    const createRes = await axios.post("http://localhost:8080/attractions", {
      name: form.value.name,
      address: form.value.address,
      cityId: selectedCity?.id,
      description: form.value.description,
      rating: form.value.rating,
      category: categoryInput.value
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean),
      latitude: form.value.latitude,
      longitude: form.value.longitude,
    });

    const attractionId = createRes.data.id;

    if (form.value.imageFile) {
      const imageData = new FormData();
      imageData.append("image", form.value.imageFile);

      const uploadRes = await axios.post(
        "http://localhost:8080/photos/upload",
        imageData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      const uploadedUrl = uploadRes.data.url;

      await axios.post("http://localhost:8080/photos", {
        attractionId,
        url: uploadedUrl,
        caption: form.value.name,
      });
    }

    emit("created");
    dialog.value = false;
    resetForm();
  } catch (err) {
    console.error("新增失敗", err);
  } finally {
    loading.value = false;
  }
};

const resetForm = () => {
  form.value = {
    name: "",
    cityName: "",
    address: "",
    description: "",
    rating: null,
    latitude: null,
    longitude: null,
    imageFile: null,
  };
  categoryInput.value = "";
};
defineExpose({ dialog });
</script>
