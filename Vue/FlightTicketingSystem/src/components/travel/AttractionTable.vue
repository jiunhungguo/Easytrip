<template>
  <v-data-table
    :headers="headers"
    :items="attractions"
    class="elevation-1 rounded"
  >
    <template #item.image="{ item }">
      <v-img
        :src="item.photoUrl || 'https://via.placeholder.com/96x64'"
        height="64"
        width="96"
        cover
        class="rounded"
      />
    </template>

    <template #item.category="{ item }">
      {{ item.category?.join(",") }}
    </template>

    <template #item.actions="{ item }">
      <v-btn
        size="small"
        color="primary"
        variant="outlined"
        @click="$emit('edit', item)"
      >
        修改
      </v-btn>
      <v-btn
        size="small"
        color="error"
        variant="outlined"
        class="ml-2"
        @click="$emit('delete', item)"
      >
        刪除
      </v-btn>
    </template>
  </v-data-table>
</template>

<script setup>
const props = defineProps({
  attractions: Array,
  headers: {
    type: Array,
    default: () => [],
  },
});
const emit = defineEmits(["edit", "delete"]);
</script>
