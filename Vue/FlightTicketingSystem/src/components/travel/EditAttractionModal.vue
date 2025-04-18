<template>
  <BaseDialog v-model="dialog" @confirm="handleConfirm">
    <template #title>編輯景點</template>

    <v-form ref="formRef" v-model="formValid">
      <v-text-field
        label="景點名稱"
        v-model="localAttraction.name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="地址"
        v-model="localAttraction.address"
        :rules="[rules.required]"
      />
      <v-select
        label="城市"
        v-model="localAttraction.city"
        :items="cities"
        item-title="name"
        item-value="name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="評分"
        v-model="localAttraction.rating"
        type="number"
        :rules="[rules.required, rules.rating]"
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
  attraction: Object,
  cities: Array,
});
const emit = defineEmits(["update:modelValue", "updated"]);

const dialog = ref(props.modelValue);
watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(dialog, (val) => emit("update:modelValue", val));

const formRef = ref();
const formValid = ref(false);

const localAttraction = ref({ ...props.attraction });
watch(
  () => props.attraction,
  (val) => {
    if (val) localAttraction.value = { ...val };
  }
);

const rules = {
  required: (v) => !!v || "必填",
  rating: (v) => (!isNaN(parseFloat(v)) && v >= 0 && v <= 5) || "0～5 分數範圍",
};

function handleConfirm() {
  if (!formRef.value?.validate()) return;
  emit("updated", localAttraction.value);
  dialog.value = false;
}
</script>

<style scoped></style>
