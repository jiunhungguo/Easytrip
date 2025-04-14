<template>
  <v-card-actions class="justify-center pb-4">
    <v-btn color="primary" size="small" @click="dialog = true">修改</v-btn>
    <v-btn color="error" size="small" @click="confirmDelete">刪除</v-btn>
  </v-card-actions>

  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>編輯城市</v-card-title>
      <v-card-text>
        <v-form ref="formRef" v-model="valid">
          <v-text-field v-model="form.name" label="城市名稱" :rules="[r]" />
          <v-text-field v-model="form.country" label="國家" :rules="[r]" />
          <v-text-field v-model="form.latitude" label="緯度" :rules="[r]" />
          <v-text-field v-model="form.longitude" label="經度" :rules="[r]" />
          <v-file-input
            v-model="form.imageFile"
            label="上傳新圖片"
            accept="image/*"
            prepend-icon="mdi-camera" />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="dialog = false">取消</v-btn>
        <v-btn color="primary" @click="submitEdit" :loading="loading">
          更新
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, reactive } from "vue";
import axios from "axios";

const props = defineProps({ city: Object });
const emit = defineEmits(["updated", "deleted"]);

const dialog = ref(false);
const valid = ref(true);
const loading = ref(false);
const formRef = ref();

const form = reactive({
  name: props.city.name,
  country: props.city.country,
  latitude: props.city.latitude,
  longitude: props.city.longitude,
  imageFile: null,
});

const r = (v) => !!v || "必填";

const submitEdit = async () => {
  if (!formRef.value.validate()) return;
  loading.value = true;
  let imageUrl = props.city.image;

  try {
    if (form.imageFile) {
      const fd = new FormData();
      fd.append("image", form.imageFile);
      const res = await axios.post("http://localhost:8080/photos/upload", fd);
      imageUrl = res.data.url;
    }

    await axios.put(`http://localhost:8080/cities/${props.city.id}`, {
      name: form.name,
      country: form.country,
      latitude: form.latitude,
      longitude: form.longitude,
      image: imageUrl,
    });

    dialog.value = false;
    emit("updated");
  } catch (err) {
    console.error("更新失敗", err);
  } finally {
    loading.value = false;
  }
};

const confirmDelete = async () => {
  const confirmed = confirm("確定刪除這個城市？");
  if (!confirmed) return;

  try {
    await axios.delete(`http://localhost:8080/cities/${props.city.id}`);
    emit("deleted");
  } catch (err) {
    console.error("刪除失敗", err);
  }
};
</script>
