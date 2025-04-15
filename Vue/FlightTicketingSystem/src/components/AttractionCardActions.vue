<template>
  <v-card-actions class="justify-center py-2">
    <v-btn
      color="primary"
      size="small"
      variant="outlined"
      @click="$emit('edit', attraction)"
    >
      編輯
    </v-btn>
    <v-btn
      color="error"
      size="small"
      variant="outlined"
      @click="$emit('delete', attraction.id, attraction.name)"
    >
      刪除
    </v-btn>
  </v-card-actions>
</template>

<script setup>
defineProps({
  attraction: Object,
});
defineEmits(["edit", "delete"]);
</script>
