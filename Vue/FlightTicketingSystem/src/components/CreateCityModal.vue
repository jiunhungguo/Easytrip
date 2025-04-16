<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title class="text-h6 font-weight-bold">新增城市</v-card-title>

      <v-card-text>
        <v-form ref="formRef" v-model="valid" @submit.prevent="submit">
          <v-text-field
            v-model="form.name"
            label="城市名稱"
            :rules="[required]" />
          <v-text-field
            v-model="form.country"
            label="國家"
            :rules="[required]" />
          <v-text-field
            v-model="form.longitude"
            label="緯度"
            type="number"
            :rules="[required, isNumber]" />
          <v-text-field
            v-model="form.latitude"
            label="經度"
            type="number"
            :rules="[required, isNumber]" />
          <v-file-input
            v-model="form.photo"
            label="上傳照片（必填）"
            accept="image/*"
            prepend-icon=""
            prepend-inner-icon="mdi-camera"
            show-size
            :rules="[required]" />
        </v-form>
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn color="primary" variant="outlined" @click="submit">新增</v-btn>
        <v-btn variant="outlined" text @click="dialog = false">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const emit = defineEmits(["created"]);
const dialog = ref(false);
const formRef = ref();
const valid = ref(true);

const form = ref({
  name: "",
  country: "",
  latitude: "",
  longitude: "",
  photo: null,
});

// 驗證規則
const required = (v) => !!v || "必填";
const isNumber = (v) => (v === "" || isNaN(v) ? "請輸入數字" : true);

const submit = async () => {
  const result = await formRef.value?.validate();
  if (!result) return;

  try {
    const formData = new FormData();
    formData.append("image", form.value.photo);
    const uploadRes = await axios.post(
      "http://localhost:8080/photos/upload",
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    const imageUrl = uploadRes.data.url;

    await axios.post("http://localhost:8080/cities", {
      name: form.value.name,
      country: form.value.country,
      latitude: form.value.latitude,
      longitude: form.value.longitude,
      image: imageUrl,
    });

    emit("created");
    dialog.value = false;
    form.value = {
      name: "",
      country: "",
      latitude: "",
      longitude: "",
      photo: null,
    };
  } catch (err) {
    console.error("❌ 新增城市失敗", err);
  }
};

defineExpose({ dialog });
</script>
