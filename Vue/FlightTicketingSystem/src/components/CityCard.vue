<template>
  <v-card
    class="mx-auto transition-all duration-300 hover:shadow-xl hover:scale-[1.02]"
    max-width="320"
    elevation="3"
    rounded="xl">
    <v-img
      :src="`http://localhost:8080${city.image}`"
      :alt="city.name"
      height="180"
      cover
      class="rounded-t-xl" />
    <v-card-title class="text-center text-lg font-semibold py-2">
      {{ city.name }}
    </v-card-title>
    <v-card-subtitle class="text-center pb-2">
      {{ city.country }}
    </v-card-subtitle>

    <v-card-actions class="justify-center pb-4">
      <v-btn
        color="primary"
        variant="outlined"
        size="small"
        @click="editDialog = true"
        >修改</v-btn
      >
      <v-btn
        color="error"
        variant="outlined"
        size="small"
        @click="deleteDialog = true"
        >刪除</v-btn
      >
    </v-card-actions>

    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card class="pa-4 rounded-lg">
        <v-card-title class="text-h6 font-weight-bold">
          確定要刪除?
        </v-card-title>
        <v-card-text class="text-body-2 text-medium-emphasis">
          此操作無法復原，是否確定刪除？ <strong>{{ city.name }}</strong>
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

    <v-dialog v-model="editDialog" max-width="500">
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
              prepend-icon=""
              prepend-inner-icon="mdi-camera" />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn
            color="primary"
            variant="outlined"
            @click="submitEdit"
            :loading="loading">
            確認
          </v-btn>
          <v-btn variant="outlined" text @click="editDialog = false"
            >取消</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script setup>
import { ref, reactive } from "vue";
import axios from "axios";

const props = defineProps({ city: Object });
const emit = defineEmits(["edit", "delete"]);

const editDialog = ref(false);
const deleteDialog = ref(false);

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
      const file = new FormData();
      file.append("image", form.imageFile);
      const res = await axios.post("http://localhost:8080/photos/upload", file);
      imageUrl = res.data.url;
    }

    await axios.put(`http://localhost:8080/cities/${props.city.id}`, {
      name: form.name,
      country: form.country,
      latitude: form.latitude,
      longitude: form.longitude,
      image: imageUrl,
    });

    editDialog.value = false;
    emit("edit");
  } catch (err) {
    console.error("更新失敗", err);
  } finally {
    loading.value = false;
  }
};

const confirmDelete = async () => {
  try {
    await axios.delete(`http://localhost:8080/cities/${props.city.id}`);
    emit("delete");
    deleteDialog.value = false;
  } catch (err) {
    console.error("刪除失敗", err);
  }
};
</script>
