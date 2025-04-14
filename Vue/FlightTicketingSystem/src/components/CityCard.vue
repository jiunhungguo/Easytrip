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
      class="rounded-t-xl"></v-img>

    <v-card-title class="text-center text-lg font-semibold py-2">
      {{ city.name }}
    </v-card-title>
    <v-card-subtitle class="text-center pb-2">
      {{ city.country }}
    </v-card-subtitle>

    <v-card-actions class="justify-center pb-4">
      <v-btn color="primary" variant="flat" @click="$emit('edit', city)"
        >修改</v-btn
      >
      <v-btn color="error" variant="flat" @click="$emit('delete', city.id)"
        >刪除</v-btn
      >
    </v-card-actions>
  </v-card>
</template>

<script setup>
defineProps({
  city: Object,
});
defineEmits(["edit", "delete"]);
</script>
