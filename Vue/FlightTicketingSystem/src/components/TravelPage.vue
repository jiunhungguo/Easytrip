<template>
  <v-container
    fluid
    class="pa-6"
    style="background-color: #f9f9f9; min-height: 100vh">
    <NavigationTabs :selectedTab="selectedTab" @select-tab="setSelectedTab" />
    <SearchBar v-model="searchQuery" @search="handleSearch" />

    <v-row class="mt-6" dense>
      <v-col
        v-if="selectedTab === 'cities'"
        v-for="city in results"
        :key="city.id"
        cols="12"
        sm="6"
        md="4"
        lg="3">
        <CityCard :city="city" @edit="editCity" @delete="confirmDeleteCity" />
      </v-col>
    </v-row>
    <v-row class="mt-6" dense v-if="selectedTab === 'attractions'">
      <v-col
        v-for="attraction in results"
        :key="attraction.id"
        cols="12"
        sm="6"
        md="4"
        lg="3">
        <AttractionCard
          :attraction="attraction"
          @edit="editAttraction"
          @delete="confirmDeleteAttraction"
          @show-images="showAttractionImages" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from "vue";
import NavigationTabs from "./NavigationTabs.vue";
import SearchBar from "./SearchBar.vue";
import CityCard from "./CityCard.vue";
import AttractionCard from "./AttractionCard.vue";
import axios from "axios";

const selectedTab = ref("cities");

const setSelectedTab = (tab) => {
  selectedTab.value = tab;
  results.value = [];
};

const searchQuery = ref("");
const results = ref([]);

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
</script>
