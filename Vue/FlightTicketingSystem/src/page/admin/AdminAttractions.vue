<template>
  <v-container>
    <div class="d-flex justify-space-between align-center mb-4">
      <v-btn-toggle v-model="viewMode" borderless>
        <v-btn icon value="Table"><v-icon>mdi-table</v-icon></v-btn>
        <v-btn icon value="Card"><v-icon>mdi-view-grid</v-icon></v-btn>
      </v-btn-toggle>
    </div>

    <div class="mb-4 font-weight-bold">
      <v-icon>mdi-table-eye</v-icon> Current View: {{ viewMode }}
    </div>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
      class="mb-4"
    />

    <!-- Table View -->
    <v-slide-y-transition>
      <div v-show="viewMode === 'Table' && !loading">
        <v-data-table
          :headers="headers"
          :items="attractions"
          class="elevation-1 rounded"
        >
          <template #item.image="{ item }">
            <v-img
              :src="
                item.photoUrl ||
                'https://via.placeholder.com/96x64?text=No+Image'
              "
              height="64"
              width="96"
              cover
              class="rounded"
            />
          </template>
          <template #item.category="{ item }">
            {{ item.category?.join("， ") }}
          </template>
          <template #item.actions="{ item }">
            <v-btn
              size="small"
              color="primary"
              variant="outlined"
              class="hover:scale-105"
              @click="edit(item)"
              >Edit</v-btn
            >
            <v-btn
              size="small"
              color="error"
              variant="outlined"
              class="ml-2 hover:scale-105"
              @click="remove(item)"
              >Delete</v-btn
            >
          </template>
        </v-data-table>
      </div>
    </v-slide-y-transition>

    <!-- Card View -->
    <v-slide-y-transition>
      <div v-show="viewMode === 'Card' && !loading">
        <v-row justify="center" align="stretch" class="gap-4">
          <v-col v-for="a in attractions" :key="a.id" cols="12" sm="6" md="4">
            <v-card class="rounded-lg elevation-2" style="width: 360px">
              <v-img
                :src="
                  a.photoUrl ||
                  'https://via.placeholder.com/300x180?text=No+Image'
                "
                height="280px"
                cover
                class="rounded-t-lg"
              />
              <v-card-title class="font-weight-bold">{{ a.name }}</v-card-title>
              <v-card-subtitle class="text-grey">{{ a.city }}</v-card-subtitle>
              <v-card-text>
                <div>{{ a.address }}</div>
                <v-rating
                  v-if="a.rating"
                  :model-value="a.rating"
                  readonly
                  dense
                  color="amber"
                  size="20"
                />
                <div class="mt-1 text-caption text-grey-darken-1">
                  {{ a.category?.join(", ") }}
                </div>
              </v-card-text>
              <v-card-actions class="justify-center">
                <v-btn color="primary" variant="outlined" @click="edit(a)"
                  >Edit</v-btn
                >
                <v-btn color="error" variant="outlined" @click="remove(a)"
                  >Delete</v-btn
                >
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </div>
    </v-slide-y-transition>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const viewMode = ref("Table");
const loading = ref(true);

const headers = [
  { title: "Name", key: "name" },
  { title: "City", key: "city" },
  { title: "Rating", key: "rating" },
  { title: "Category", key: "category" },
  { title: "Address", key: "address" },
  { title: "Actions", key: "actions", sortable: false },
];

const attractions = ref([]);

const fetchAttractionPhoto = async (attractionId) => {
  try {
    const { data } = await axios.get(
      `http://localhost:8080/photos/attraction/${attractionId}`
    );
    return data[0]?.url ? `http://localhost:8080${data[0].url}` : null;
  } catch (e) {
    console.warn("⚠️ 無法載入景點圖片", attractionId);
    return null;
  }
};

const fetchData = async () => {
  loading.value = true;
  try {
    const { data } = await axios.get("http://localhost:8080/attractions", {
      params: { page: 0, size: 9 },
    });
    const rawAttractions = data.content;

    // 串接每個景點的圖片（第一張）
    const withPhotos = await Promise.all(
      rawAttractions.map(async (a) => {
        const photoUrl = await fetchAttractionPhoto(a.id);
        return { ...a, photoUrl };
      })
    );

    attractions.value = withPhotos;
  } catch (err) {
    console.error("❌ 載入景點失敗", err);
  } finally {
    loading.value = false;
  }
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
