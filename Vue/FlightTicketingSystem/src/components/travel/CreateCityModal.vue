<template>
  <BaseDialog v-model="dialog" @confirm="handleConfirm">
    <template #title>新增城市</template>

    <v-form ref="formRef" v-model="formValid">
      <v-text-field
        label="城市名稱"
        v-model="newCity.name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="國家"
        v-model="newCity.country"
        :rules="[rules.required]"
      />
      <v-text-field
        label="經度"
        v-model="newCity.longitude"
        type="number"
        :rules="[rules.required, rules.number]"
      />
      <v-text-field
        label="緯度"
        v-model="newCity.latitude"
        type="number"
        :rules="[rules.required, rules.number]"
      />
    </v-form>

    <template #actions>
      <v-btn text @click="dialog = false">取消</v-btn>
      <v-btn color="primary" @click="handleConfirm">新增</v-btn>
    </template>
  </BaseDialog>
</template>

<script setup>
import { ref, watch } from "vue";
import BaseDialog from "./BaseDialog.vue";

const props = defineProps({
  modelValue: Boolean,
});
const emit = defineEmits(["update:modelValue", "created"]);

const dialog = ref(props.modelValue);
watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(dialog, (val) => emit("update:modelValue", val));

const formRef = ref();
const formValid = ref(false);

const newCity = ref({
  name: "",
  country: "",
  longitude: "",
  latitude: "",
});

const rules = {
  required: (v) => !!v || "必填",
  number: (v) => !isNaN(parseFloat(v)) || "需為數字",
};

function handleConfirm() {
  if (!formRef.value?.validate()) return;
  emit("created", newCity.value);
  dialog.value = false;
  // 重置表單（可選）
  newCity.value = {
    name: "",
    country: "",
    longitude: "",
    latitude: "",
  };
}
</script>

<style scoped></style>
