import { defineStore } from "pinia";
import axios from "axios";

export const useCityStore = defineStore("city", {
  state: () => ({
    cities: [],
    loading: false,
  }),
  actions: {
    async fetchCities(page = 0, size = 9) {
      this.loading = true;
      try {
        const res = await axios.get("http://localhost:8080/cities", {
          params: { page, size },
        });
        this.cities = res.data.content;
      } catch (err) {
        console.error("載入城市失敗", err);
      } finally {
        this.loading = false;
      }
    },
    async searchByName(name) {
      try {
        const res = await axios.get(
          `http://localhost:8080/cities/city/${name}`
        );
        return Array.isArray(res.data) ? res.data : [res.data];
      } catch (err) {
        console.error("搜尋城市失敗", err);
        return [];
      }
    },
  },
});
