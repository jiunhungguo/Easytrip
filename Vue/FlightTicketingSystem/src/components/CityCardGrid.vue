<template>
  <v-row
    justify="center"
    align="start"
    class="pa-6"
    style="gap: 2rem; flex-wrap: wrap">
    <div v-for="city in cities" :key="city.id" class="city-card-container">
      <CityCard
        :city="city"
        @edit="$emit('edit', city)"
        @delete="$emit('delete', city)" />
    </div>
  </v-row>
</template>

<script setup>
defineProps({
  cities: Array,
});
defineEmits(["edit", "delete"]);
import CityCard from "./CityCard.vue";
</script>

<style scoped>
.city-card-container {
  width: 260px;
  min-height: 320px;
}
</style>
