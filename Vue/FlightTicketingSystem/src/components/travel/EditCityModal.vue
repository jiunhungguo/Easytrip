<template>
  <BaseDialog v-model="dialog" @confirm="handleConfirm">
    <template #title>編輯城市</template>

    <v-form ref="formRef" v-model="formValid">
      <v-text-field
        label="城市名稱"
        v-model="localCity.name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="國家"
        v-model="localCity.country"
        :rules="[rules.required]"
      />
      <v-text-field
        label="經度"
        v-model="localCity.longitude"
        type="number"
        :rules="[rules.required, rules.number]"
      />
      <v-text-field
        label="緯度"
        v-model="localCity.latitude"
        type="number"
        :rules="[rules.required, rules.number]"
      />
    </v-form>

    <template #actions>
      <v-btn text @click="dialog = false">取消</v-btn>
      <v-btn color="primary" @click="handleConfirm">保存</v-btn>
    </template>
  </BaseDialog>
</template>

<script setup>
import { ref, watch } from "vue";
import BaseDialog from "./BaseDialog.vue";

const props = defineProps({
  modelValue: Boolean,
  city: Object,
});
const emit = defineEmits(["update:modelValue", "updated"]);

const dialog = ref(props.modelValue);
watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(dialog, (val) => emit("update:modelValue", val));

const localCity = ref({ ...props.city });
watch(
  () => props.city,
  (newVal) => {
    if (newVal) localCity.value = { ...newVal };
  }
);

const formRef = ref();
const formValid = ref(false);

const rules = {
  required: (v) => !!v || "必填",
  number: (v) => !isNaN(parseFloat(v)) || "需為數字",
};

function handleConfirm() {
  if (!formRef.value?.validate()) return;
  emit("updated", localCity.value);
  dialog.value = false;
}
</script>

<style scoped></style>
