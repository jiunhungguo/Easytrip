<template>
  <v-container
    fluid
    class="pa-6"
    style="background-color: #f9f9f9; min-height: 100vh"
  >
    <NavigationTabs :selectedTab="selectedTab" @select-tab="setSelectedTab" />
    <SearchBar v-model="searchQuery" @search="handleSearch" />

    <v-row class="mt-6" dense v-if="selectedTab === 'cities' && results.length">
      <v-col
        v-if="selectedTab === 'cities'"
        v-for="city in results"
        :key="city.id"
        cols="12"
        sm="6"
        md="4"
        lg="3"
      >
        <CityCard
          :city="city"
          @edit="handleCityUpdated"
          @delete="handleCityDeleted"
        />
      </v-col>
    </v-row>
    <v-row class="mt-6" dense v-if="selectedTab === 'attractions'">
      <v-col
        v-for="attraction in results"
        :key="attraction.id"
        cols="12"
        sm="6"
        md="4"
        lg="3"
      >
        <AttractionCard
          :attraction="attraction"
          :cities="cities"
          @edit="handleAttractionUpdated"
          @delete="handleAttractionDeleted"
        />
      </v-col>
    </v-row>

    <v-snackbar
      v-model="snackbar"
      color="success"
      timeout="3000"
      location="bottom center"
    >
      {{ snackbarMessage }}
    </v-snackbar>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import NavigationTabs from "./NavigationTabs.vue";
import SearchBar from "./SearchBar.vue";
import CityCard from "./CityCard.vue";
import AttractionCard from "./AttractionCard.vue";

const selectedTab = ref("cities");

const setSelectedTab = (tab) => {
  selectedTab.value = tab;
  results.value = [];
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
    console.log(cities.value);
  } catch (err) {
    console.error("載入城市列表失敗", err);
  }
};

const reloadCities = async () => {
  searchQuery.value = "";
  const res = await axios.get("http://localhost:8080/cities");
  results.value = res.data;
  getCityList();
};

const reloadAttractions = async () => {
  searchQuery.value = "";
  const res = await axios.get("http://localhost:8080/attractions");
  const attractions = await Promise.all(
    res.data.map(async (attr) => {
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

const handleCityUpdated = () => {
  showSuccess("城市編輯成功！");
  reloadCities();
};

const handleCityDeleted = () => {
  showSuccess("城市刪除成功！");
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

onMounted(() => {
  getCityList();
});
</script>
