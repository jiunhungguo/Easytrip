<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title class="text-h6 font-weight-bold">編輯城市</v-card-title>

      <v-card-text>
        <v-form ref="formRef" @submit.prevent="submit">
          <v-text-field
            v-model="form.name"
            label="城市名稱"
            :error-messages="errors.name"
            required />
          <v-text-field
            v-model="form.country"
            label="國家"
            :error-messages="errors.country"
            required />
          <v-text-field
            v-model="form.latitude"
            label="經度"
            type="number"
            :error-messages="errors.latitude"
            required />
          <v-text-field
            v-model="form.longitude"
            label="緯度"
            type="number"
            :error-messages="errors.longitude"
            required />

          <v-file-input
            v-model="form.photo"
            label="上傳照片（可選）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            show-size />
        </v-form>
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn color="primary" variant="outlined" @click="submit">確認</v-btn>
        <v-btn text variant="outlined" @click="dialog = false">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from "vue";
import axios from "axios";

const props = defineProps({
  city: Object, // 傳入要編輯的城市資料
});

const emit = defineEmits(["updated"]);

const dialog = ref(false);
const formRef = ref();

const form = ref({
  name: "",
  country: "",
  latitude: "",
  longitude: "",
  photo: null,
});

const errors = ref({
  name: [],
  country: [],
  latitude: [],
  longitude: [],
});

watch(dialog, (val) => {
  if (val && props.city) {
    // 開啟 dialog 時填入原始值
    form.value = {
      name: props.city.name || "",
      country: props.city.country || "",
      latitude: props.city.latitude || "",
      longitude: props.city.longitude || "",
      photo: null,
    };
  }
});

const validate = () => {
  let valid = true;
  const f = form.value;

  errors.value.name = f.name ? [] : ["城市名稱為必填"];
  errors.value.country = f.country ? [] : ["國家為必填"];
  errors.value.latitude = f.latitude ? [] : ["經度為必填"];
  errors.value.longitude = f.longitude ? [] : ["緯度為必填"];

  valid =
    errors.value.name.length === 0 &&
    errors.value.country.length === 0 &&
    errors.value.latitude.length === 0 &&
    errors.value.longitude.length === 0;

  return valid;
};

const submit = async () => {
  if (!validate()) return;

  try {
    // 更新城市基本資料
    await axios.put(`/cities/${props.city.id}`, {
      name: form.value.name,
      country: form.value.country,
      latitude: form.value.latitude,
      longitude: form.value.longitude,
    });

    // 如果有選圖片，另外發送上傳圖片的請求
    if (form.value.photo) {
      const formData = new FormData();
      formData.append("file", form.value.photo);
      formData.append("cityId", props.city.id);
      formData.append("caption", form.value.name);

      await axios.post("/photos", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
    }

    emit("updated");
    dialog.value = false;
  } catch (err) {
    console.error("❌ 更新城市失敗", err);
  }
};
</script>
