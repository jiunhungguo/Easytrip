<template>
  <v-container class="py-8">
    <h1 class="text-2xl font-bold mb-6 text-center">
      TripAdvisor Style Attractions
    </h1>

    <v-row>
      <v-col
        v-for="attraction in attractions"
        :key="attraction.id"
        cols="12"
        sm="6"
        md="4">
        <TripAttractionCard
          :attraction="attraction"
          :photos="attraction.photos" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import TripAttractionCard from "@/components/TripAttractionCard.vue";

const attractions = [
  {
    id: 1,
    name: "Colosseum",
    city: "Rome",
    rating: 4.7,
    category: ["Ancient Ruins"],
    photos: [
      { url: "https://images.unsplash.com/photo-1600607687939-ce8a8f6f1c5b" },
      { url: "https://images.unsplash.com/photo-1570528722015-50ad7aa1ba0e" },
    ],
  },
  {
    id: 2,
    name: "Eiffel Tower",
    city: "Paris",
    rating: 4.8,
    category: ["Landmark"],
    photos: [
      { url: "https://images.unsplash.com/photo-1502602898657-3e91760cbb34" },
      { url: "https://images.unsplash.com/photo-1491553895911-0055eca6402d" },
    ],
  },
  {
    id: 3,
    name: "Great Wall",
    city: "Beijing",
    rating: 4.6,
    category: ["Historical Site"],
    photos: [
      { url: "https://images.unsplash.com/photo-1594032194509-6f566cbdccba" },
      { url: "https://images.unsplash.com/photo-1599446547077-113b7c79914c" },
    ],
  },
];
</script>
