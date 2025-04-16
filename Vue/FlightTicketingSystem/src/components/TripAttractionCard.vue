<template>
  <v-container>
    <div class="d-flex justify-space-between align-center mb-4">
      <h1 class="text-h5 font-weight-bold">現在模式：{{ viewMode }}</h1>
      <v-btn-toggle v-model="viewMode" borderless>
        <v-btn icon value="table"><v-icon>mdi-table</v-icon></v-btn>
        <v-btn icon value="card"><v-icon>mdi-view-grid</v-icon></v-btn>
      </v-btn-toggle>
    </div>

    <div class="mb-4 text-red font-weight-bold">
      🔍 Current View: {{ viewMode }}
    </div>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
      class="mb-4" />

    <!-- Table View -->
    <v-slide-y-transition>
      <v-data-table
        v-if="viewMode === 'table' && !loading"
        :headers="headers"
        :items="attractions"
        class="elevation-1 rounded">
        <template #item.image="{ item }">
          <v-img
            :src="item.photos[0]?.url"
            height="64"
            width="96"
            cover
            class="rounded" />
        </template>
        <template #item.actions="{ item }">
          <v-btn
            size="small"
            color="primary"
            variant="outlined"
            @click="edit(item)"
            >Edit</v-btn
          >
          <v-btn size="small" color="error" class="ml-2" @click="remove(item)"
            >Delete</v-btn
          >
        </template>
      </v-data-table>
    </v-slide-y-transition>

    <!-- Card View -->
    <v-slide-y-transition>
      <v-row v-if="viewMode === 'card' && !loading">
        <v-col v-for="a in attractions" :key="a.id" cols="12" sm="6" md="4">
          <v-card class="rounded-lg elevation-2">
            <v-img
              :src="a.photos[0]?.url"
              height="180px"
              cover
              class="rounded-t-lg" />
            <v-card-title class="font-weight-bold">{{ a.name }}</v-card-title>
            <v-card-subtitle class="text-grey">{{ a.city }}</v-card-subtitle>
            <v-card-text>
              <v-rating
                :model-value="a.rating"
                readonly
                dense
                color="amber"
                size="20" />
              <div class="mt-1 text-caption text-grey-darken-1">
                {{ a.category?.join(", ") }}
              </div>
            </v-card-text>
            <v-card-actions>
              <v-btn color="primary" variant="outlined" @click="edit(a)"
                >Edit</v-btn
              >
              <v-btn color="error" variant="flat" @click="remove(a)"
                >Delete</v-btn
              >
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-slide-y-transition>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";

const viewMode = ref("table");
const loading = ref(true);

const headers = [
  { title: "Image", key: "image" },
  { title: "Name", key: "name" },
  { title: "City", key: "city" },
  { title: "Rating", key: "rating" },
  { title: "Category", key: "category" },
  { title: "Actions", key: "actions", sortable: false },
];

const attractions = ref([]);

const fetchData = async () => {
  loading.value = true;
  // 模擬延遲載入
  setTimeout(() => {
    attractions.value = [
      {
        id: 1,
        name: "Colosseum",
        city: "Rome",
        rating: 4.7,
        category: ["Ancient Ruins"],
        photos: [
          {
            url: "https://images.unsplash.com/photo-1600607687939-ce8a8f6f1c5b",
          },
        ],
      },
      {
        id: 2,
        name: "Eiffel Tower",
        city: "Paris",
        rating: 4.8,
        category: ["Landmark"],
        photos: [
          {
            url: "https://images.unsplash.com/photo-1502602898657-3e91760cbb34",
          },
        ],
      },
      {
        id: 3,
        name: "Great Wall",
        city: "Beijing",
        rating: 4.6,
        category: ["Historical Site"],
        photos: [
          {
            url: "https://images.unsplash.com/photo-1594032194509-6f566cbdccba",
          },
        ],
      },
    ];
    loading.value = false;
  }, 1000);
};

const edit = (item) => {
  console.log("Edit:", item);
};

const remove = (item) => {
  console.log("Delete:", item);
};

onMounted(() => {
  fetchData();
});
</script>
