<template>
  <v-card class="rounded-xl shadow-md">
    <v-card-item class="relative pa-0">
      <v-carousel
        v-if="photos.length > 0"
        height="200"
        hide-delimiter-background
        show-arrows-on-hover
        cycle>
        <v-carousel-item v-for="(photo, i) in photos" :key="i">
          <v-img :src="photo.url" cover height="200" />
        </v-carousel-item>
      </v-carousel>

      <v-btn
        icon
        class="position-absolute"
        style="top: 8px; right: 8px; z-index: 1"
        :color="isFavorite ? 'red' : 'grey'"
        variant="outlined"
        size="small"
        @click="toggleFavorite">
        <v-icon>{{ isFavorite ? "mdi-heart" : "mdi-heart-outline" }}</v-icon>
      </v-btn>
    </v-card-item>

    <v-card-text>
      <div class="font-bold text-lg mb-1">
        {{ attraction.name }}
      </div>
      <div class="text-sm text-grey-darken-1 mb-1">
        {{ attraction.address }}
      </div>
      <v-rating
        :model-value="attraction.rating"
        color="green"
        dense
        readonly
        half-increments
        size="18" />
      <div class="text-caption text-grey mt-1">
        {{ attraction.category?.join(", ") }}
      </div>
    </v-card-text>

    <v-card-actions class="justify-center">
      <AttractionCardActions
        :attraction="attraction"
        @edit="$emit('edit', attraction)"
        @delete="$emit('delete', attraction.id, attraction.name)"
        @show-images="$emit('show-images', attraction.id, attraction.name)" />
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { ref, onMounted } from "vue";
import AttractionCardActions from "./AttractionCardActions.vue";
import axios from "axios";

const props = defineProps({
  attraction: Object,
});

const photos = ref([]);
const isFavorite = ref(false);

const toggleFavorite = () => {
  isFavorite.value = !isFavorite.value;
};

onMounted(async () => {
  axios.get(`/photos/attraction/${props.attraction.id}`).then((res) => {
    photos.value = res.data.map((photo) => ({
      ...photo,
      url: `http://localhost:8080${photo.url}`,
    }));
  });
});
</script>
