<template>
  <v-container
    fluid
    class="pa-6"
    style="background-color: #f9f9f9; min-height: 100vh"
  >
    <!-- Tabs -->
    <NavigationTabs :selectedTab="selectedTab" @select-tab="setSelectedTab" />

    <!-- Search Bar -->
    <SearchBar v-model="searchQuery" @search="handleSearch" />

    <!-- Tab Content -->
    <v-window v-model="selectedTab" class="mt-6">
      <!-- Search City -->
      <v-window-item value="cities">
        <CityCardGrid
          :cities="results"
          @edit="handleCityUpdated"
          @delete="handleCityDeleted"
        />
      </v-window-item>
      <!-- All Cities -->
      <v-window-item value="allCities">
        <CityCardGrid
          :cities="results"
          @edit="handleCityUpdated"
          @delete="handleCityDeleted"
        />
      </v-window-item>
      <!-- Search Attraction -->
      <v-window-item value="attractions">
        <AttractionCardGrid
          :attractions="results"
          :cities="cities"
          @edit="handleAttractionUpdated"
          @delete="handleAttractionDeleted"
        />
      </v-window-item>
      <!-- All Attractions -->
      <v-window-item value="allAttractions">
        <AttractionCardGrid
          :attractions="results"
          :cities="cities"
          @edit="handleAttractionUpdated"
          @delete="handleAttractionDeleted"
        />
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
</template>

<style scoped>
.city-card-container {
  width: 260px;
  min-height: 320px;
}
</style>

<script setup>
import { ref, watch, onMounted } from "vue";
import axios from "axios";
import NavigationTabs from "@/components/NavigationTabs.vue";
import SearchBar from "@/components/SearchBar.vue";
import CityCardGrid from "@/components/CityCardGrid.vue";
import AttractionCardGrid from "@/components/AttractionCardGrid.vue";
import CreateCityModal from "@/components/CreateCityModal.vue";
import CreateAttractionModal from "@/components/CreateAttractionModal.vue";

const selectedTab = ref("cities");

const createCityModal = ref(null); // 取得 modal 的 ref
const createAttractionModal = ref(null); // 取得 modal 的 ref

const setSelectedTab = (tab) => {
  selectedTab.value = tab;

  if (tab === "addCities" && createCityModal.value) {
    createCityModal.value.dialog = true;
    // selectedTab.value = "cities";
    return;
  }
  if (tab === "addAttractions" && createAttractionModal.value) {
    createAttractionModal.value.dialog = true;
    // selectedTab.value = "attractions";
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

const fetchCities = async () => {
  const response = await axios.get("/cities");
  results.value = response.data.content;
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
