<template>
  <v-container
    fluid
    class="pa-6"
    style="background-color: #f9f9f9; min-height: 100vh"
  >
    <!-- Tabs -->
    <NavigationTabs :selectedTab="selectedTab" @select-tab="setSelectedTab" />

    <!-- Search Bar -->
    <v-slide-y-transition>
      <div v-if="['cities', 'attractions'].includes(selectedTab)">
        <SearchBar v-model="searchQuery" @search="handleSearch" />
      </div>
    </v-slide-y-transition>
    <div class="d-flex justify-space-between align-center mb-4">
      <v-btn-toggle v-model="viewMode" borderless>
        <v-btn icon value="Card"><v-icon>mdi-view-grid</v-icon></v-btn>
        <v-btn icon value="Table"><v-icon>mdi-table</v-icon></v-btn>
      </v-btn-toggle>
    </div>
    <div class="mb-4 font-weight-bold">
      <v-icon>mdi-table-eye</v-icon> Current View: {{ viewMode }}
    </div>
    <!-- Tab Content -->
    <v-window v-model="selectedTab" class="mt-6">
      <!-- Search City -->
      <v-window-item value="cities">
        <div v-if="viewMode === 'Card'">
          <CityCardGrid
            :cities="results"
            @edit="handleCityUpdated"
            @delete="handleCityDeleted"
          />
        </div>
        <div v-else>
          <v-data-table
            :headers="cityHeaders"
            :items="results"
            class="elevation-1 rounded"
          >
            <template #item.actions="{ item }">
              <v-btn
                size="small"
                color="primary"
                variant="outlined"
                @click="handleEdit(item)"
                >修改</v-btn
              >
              <v-btn
                size="small"
                color="error"
                variant="outlined"
                class="ml-2"
                @click="handleDelete(item)"
                >刪除</v-btn
              >
            </template>
          </v-data-table>
        </div>
      </v-window-item>

      <!-- All Cities -->
      <v-window-item value="allCities">
        <div v-if="viewMode === 'Card'">
          <CityCardGrid
            :cities="results"
            @edit="handleCityUpdated"
            @delete="handleCityDeleted"
          />
        </div>
        <div v-else>
          <v-data-table
            :headers="cityHeaders"
            :items="results"
            class="elevation-1 rounded"
          >
            <template #item.actions="{ item }">
              <v-btn
                size="small"
                color="primary"
                variant="outlined"
                @click="handleEdit(item)"
                >修改</v-btn
              >
              <v-btn
                size="small"
                color="error"
                variant="outlined"
                class="ml-2"
                @click="handleDelete(item)"
                >刪除</v-btn
              >
            </template>
          </v-data-table>
        </div>
      </v-window-item>

      <!-- Search Attraction -->
      <v-window-item value="attractions">
        <div v-if="viewMode === 'Card'">
          <AttractionCardGrid
            :attractions="results"
            :cities="cities"
            @edit="handleAttractionUpdated"
            @delete="handleAttractionDeleted"
          />
        </div>
        <div v-else>
          <v-data-table
            :headers="attractionHeaders"
            :items="results"
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
              {{ item.category?.join("， ") }}
            </template>
            <template #item.actions="{ item }">
              <v-btn
                size="small"
                color="primary"
                variant="outlined"
                @click="handleEdit(item)"
                >修改</v-btn
              >
              <v-btn
                size="small"
                color="error"
                variant="outlined"
                class="ml-2"
                @click="handleDelete(item)"
                >刪除</v-btn
              >
            </template>
          </v-data-table>
        </div>
      </v-window-item>

      <!-- All Attractions -->
      <v-window-item value="allAttractions">
        <div v-if="viewMode === 'Card'">
          <AttractionCardGrid
            :attractions="results"
            :cities="cities"
            @edit="handleAttractionUpdated"
            @delete="handleAttractionDeleted"
          />
        </div>
        <div v-else>
          <v-data-table
            :headers="attractionHeaders"
            :items="results"
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
              {{ item.category?.join("， ") }}
            </template>
            <template #item.actions="{ item }">
              <v-btn
                size="small"
                color="primary"
                variant="outlined"
                @click="handleEdit(item)"
                >修改</v-btn
              >
              <v-btn
                size="small"
                color="error"
                variant="outlined"
                class="ml-2"
                @click="handleDelete(item)"
                >刪除</v-btn
              >
            </template>
          </v-data-table>
        </div>
      </v-window-item>
    </v-window>

    <!-- Snackbar -->
    <v-snackbar
      v-model="snackbar"
      color="success"
      timeout="3000"
      location="bottom center"
    >
      {{ snackbarMessage }}
    </v-snackbar>
  </v-container>
  <CreateCityModal ref="createCityModal" @created="handleCityCreated" />
  <CreateAttractionModal
    ref="createAttractionModal"
    :cities="cities"
    @created="handleAttractionCreated"
  />
  <!-- 編輯 Dialog -->
  <EditCityModal
    v-if="selectedCity"
    v-model="editCityDialog"
    :city="selectedCity"
    @updated="handleCityUpdated"
  />
  <EditAttractionModal
    v-if="selectedAttraction"
    v-model="editAttractionDialog"
    :attraction="selectedAttraction"
    :cities="cities"
    @updated="handleAttractionUpdated"
  />
</template>

<style scoped>
.city-card-container {
  width: 260px;
  min-height: 320px;
}
</style>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import NavigationTabs from "@/components/NavigationTabs.vue";
import SearchBar from "@/components/travel/SearchBarAdmin.vue";
import CityCardGrid from "@/components/CityCardGrid.vue";
import AttractionCardGrid from "@/components/AttractionCardGrid.vue";
import CreateCityModal from "@/components/CreateCityModal.vue";
import CreateAttractionModal from "@/components/CreateAttractionModal.vue";
import EditCityModal from "@/components/EditCityModal.vue";
import EditAttractionModal from "@/components/EditAttractionModal.vue";

const selectedTab = defineModel("selectedTab");
const currentTab = ref("cities");
const viewMode = ref("Card");
const createCityModal = ref(null);
const createAttractionModal = ref(null);

const cityHeaders = [
  { title: "名稱", key: "name" },
  { title: "國家", key: "country" },
  { title: "經度", key: "longitude" },
  { title: "緯度", key: "latitude" },
  { title: "操作", key: "actions", sortable: false },
];

const attractionHeaders = [
  { title: "名稱", key: "name" },
  { title: "城市", key: "city" },
  { title: "地址", key: "address" },
  { title: "評分", key: "rating" },
  { title: "分類", key: "category" },
  { title: "操作", key: "actions", sortable: false },
];

const setSelectedTab = (tab) => {
  selectedTab.value = tab;

  if (tab === "addCities" && createCityModal.value) {
    createCityModal.value.dialog = true;
    return;
  }
  if (tab === "addAttractions" && createAttractionModal.value) {
    createAttractionModal.value.dialog = true;
    return;
  }
  if (tab === "allCities") {
    reloadCities();
  } else if (tab === "allAttractions") {
    reloadAttractions();
  } else {
    results.value = [];
    searchQuery.value = "";
  }
};

const searchQuery = ref("");
const results = ref([]);

const snackbar = ref(false);
const snackbarMessage = ref("");
const showSuccess = (msg) => {
  snackbarMessage.value = msg;
  snackbar.value = true;
};

const apiPaths = {
  cities: "http://localhost:8080/cities/city",
  attractions: "http://localhost:8080/attractions",
};

const handleSearch = async (done) => {
  try {
    const base = apiPaths[selectedTab.value];
    const query = searchQuery.value?.trim();
    if (!query) {
      done();
      return;
    }

    const url = `${base}/${encodeURIComponent(query)}`;
    const response = await axios.get(url);

    if (selectedTab.value === "attractions") {
      const attractions = await Promise.all(
        response.data.map(async (attr) => {
          try {
            const photoRes = await axios.get(
              `http://localhost:8080/photos/attraction/${attr.id}`
            );
            const relativeUrl = photoRes.data[0]?.url || "";
            attr.photoUrl = relativeUrl
              ? `http://localhost:8080${relativeUrl}`
              : "";
          } catch {
            attr.photoUrl = "";
          }
          return attr;
        })
      );
      results.value = attractions;
    } else {
      results.value = response.data;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    done();
  }
};

const cities = ref([]);

const getCityList = async () => {
  try {
    const res = await axios.get("http://localhost:8080/cities");
    cities.value = res.data.content;
  } catch (err) {
    console.error("載入城市列表失敗", err);
  }
};

const reloadCities = async () => {
  searchQuery.value = "";
  const res = await axios.get("http://localhost:8080/cities");
  results.value = res.data.content;
  getCityList();
};

const reloadAttractions = async () => {
  searchQuery.value = "";
  const res = await axios.get("http://localhost:8080/attractions");
  const attractionsRaw = res.data.content || [];

  const attractions = await Promise.all(
    attractionsRaw.map(async (attr) => {
      try {
        const photoRes = await axios.get(
          `http://localhost:8080/photos/attraction/${attr.id}`
        );
        const relativeUrl = photoRes.data[0]?.url || "";
        attr.photoUrl = relativeUrl
          ? `http://localhost:8080${relativeUrl}`
          : "";
      } catch {
        attr.photoUrl = "";
      }
      return attr;
    })
  );
  results.value = attractions;
};

const selectedCity = ref(null);
const selectedAttraction = ref(null);

const editCityDialog = ref(false);
const editAttractionDialog = ref(false);

const handleEdit = (item) => {
  if (selectedTab.value === "cities" || selectedTab.value === "allCities") {
    selectedCity.value = item;
    editCityDialog.value = true;
  } else if (
    selectedTab.value === "attractions" ||
    selectedTab.value === "allAttractions"
  ) {
    selectedAttraction.value = item;
    editAttractionDialog.value = true;
  }
};

const handleCityUpdated = () => {
  showSuccess("城市編輯成功！");
  reloadCities();
};

const handleCityDeleted = () => {
  showSuccess("城市刪除成功！");
  reloadCities();
};

const handleCityCreated = () => {
  showSuccess("城市添加成功！");
  reloadCities();
};

const handleAttractionUpdated = () => {
  showSuccess("景點編輯成功！");
  reloadAttractions();
};

const handleAttractionDeleted = () => {
  showSuccess("景點刪除成功！");
  reloadAttractions();
};

const handleAttractionCreated = () => {
  showSuccess("景點增添成功！");
  reloadAttractions();
};

onMounted(() => {
  getCityList();
});
</script>
