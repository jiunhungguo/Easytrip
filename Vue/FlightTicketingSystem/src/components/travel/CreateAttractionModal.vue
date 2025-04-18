<template>
  <BaseDialog v-model="dialog" @confirm="handleConfirm">
    <template #title>新增景點</template>

    <v-form ref="formRef" v-model="formValid">
      <v-text-field
        label="景點名稱"
        v-model="newAttraction.name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="地址"
        v-model="newAttraction.address"
        :rules="[rules.required]"
      />
      <v-select
        label="城市"
        v-model="newAttraction.city"
        :items="cities"
        item-title="name"
        item-value="name"
        :rules="[rules.required]"
      />
      <v-text-field
        label="評分"
        v-model="newAttraction.rating"
        type="number"
        :rules="[rules.required, rules.rating]"
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
  cities: Array,
});
const emit = defineEmits(["update:modelValue", "created"]);

const dialog = ref(props.modelValue);
watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);
watch(dialog, (val) => emit("update:modelValue", val));

const newAttraction = ref({
  name: "",
  address: "",
  city: "",
  rating: "",
});

const formRef = ref();
const formValid = ref(false);

const rules = {
  required: (v) => !!v || "必填",
  rating: (v) => (!isNaN(parseFloat(v)) && v >= 0 && v <= 5) || "0～5 分數範圍",
};

function handleConfirm() {
  if (!formRef.value?.validate()) return;
  emit("created", newAttraction.value);
  dialog.value = false;
  newAttraction.value = {
    name: "",
    address: "",
    city: "",
    rating: "",
  };
}
</script>

<style scoped></style>
