<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>編輯城市</v-card-title>
      <v-card-text>
        <v-form ref="formRef" v-model="valid">
          <!-- 圖片預覽 -->
          <v-img
            v-if="city?.image"
            :src="previewUrl"
            height="160"
            class="mb-3 rounded"
            cover />

          <!-- 表單欄位 -->
          <v-text-field v-model="form.name" label="名稱" :rules="[r]" />
          <v-text-field v-model="form.country" label="國家" :rules="[r]" />
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

          <!-- 圖片上傳 -->
          <v-file-input
            v-model="form.imageFile"
            label="上傳新圖片（可選）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            clearable
            @change="updatePreview" />
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
        <v-btn variant="text" @click="dialog = false">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, reactive } from "vue";
import axios from "axios";

const props = defineProps({ modelValue: Boolean, city: Object });
const emit = defineEmits(["update:modelValue", "updated"]);

const dialog = ref(props.modelValue);
const valid = ref(true);
const formRef = ref();
const loading = ref(false);

// 預覽圖
const previewUrl = ref("");

const form = reactive({
  name: "",
  country: "",
  latitude: 0,
  longitude: 0,
  imageFile: null,
});

watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(
  () => dialog.value,
  (val) => emit("update:modelValue", val)
);

watch(
  () => props.city,
  (newCity) => {
    if (newCity) {
      form.name = newCity.name;
      form.country = newCity.country;
      form.latitude = newCity.latitude;
      form.longitude = newCity.longitude;
      form.imageFile = null;
      previewUrl.value = newCity.image
        ? `http://localhost:8080${newCity.image}`
        : "";
    }
  },
  { immediate: true }
);

// 更新預覽圖
const updatePreview = () => {
  if (form.imageFile && form.imageFile instanceof File) {
    previewUrl.value = URL.createObjectURL(form.imageFile);
  } else {
    previewUrl.value = props.city?.image
      ? `http://localhost:8080${props.city.image}`
      : "";
  }
};

const r = (v) => !!v || "必填";

const submit = async () => {
  const result = await formRef.value?.validate();
  if (!result) return;
  loading.value = true;

  try {
    let imageUrl = props.city.image;

    // 如果有新圖片，先上傳
    if (form.imageFile) {
      const file = new FormData();
      file.append("image", form.imageFile);
      const res = await axios.post("http://localhost:8080/photos/upload", file);
      imageUrl = res.data.url;
    }

    // 再更新城市資料
    await axios.put(`http://localhost:8080/cities/${props.city.id}`, {
      name: form.name,
      country: form.country,
      latitude: form.latitude,
      longitude: form.longitude,
      image: imageUrl,
    });

    emit("updated");
    dialog.value = false;
  } catch (err) {
    console.error("更新失敗", err);
  } finally {
    loading.value = false;
  }
};
</script>
