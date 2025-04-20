<template>
  <v-row
    justify="start"
    align="start"
    class="pa-6"
    style="gap: 24px; flex-wrap: wrap">
    <div
      v-for="attraction in attractions"
      :key="attraction.id"
      class="attraction-card-container">
      <AttractionCard
        :attraction="attraction"
        :cities="cities"
        @edit="$emit('edit', attraction)"
        @delete="$emit('delete', attraction)" />
    </div>
  </v-row>
</template>

<script setup>
defineProps({
  attractions: Array,
  cities: Array,
});
defineEmits(["edit", "delete"]);
import AttractionCard from "./AttractionCardAdmin.vue";
</script>

<style scoped>
.attraction-card-container {
  width: 300px;
  min-height: 300px;
}
</style>
