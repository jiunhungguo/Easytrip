import { computed } from "vue";
import CityTable from "@/components/travel/CityTable.vue";
import CityCardGrid from "@/components/CityCardGrid.vue";
import AttractionTable from "@/components/travel/AttractionTable.vue";
import AttractionCardGrid from "@/components/AttractionCardGrid.vue";

export function useTabView(viewMode, results, cities, emitMap) {
  const getViewComponent = (tab) => {
    const isCard = viewMode.value === "Card";
    const map = {
      cities: isCard ? CityCardGrid : CityTable,
      allCities: isCard ? CityCardGrid : CityTable,
      attractions: isCard ? AttractionCardGrid : AttractionTable,
      allAttractions: isCard ? AttractionCardGrid : AttractionTable,
    };
    return map[tab];
  };

  const getViewProps = (tab) => {
    if (["cities", "allCities"].includes(tab)) {
      return {
        cities: results.value,
        ...emitMap.city,
      };
    }

    if (["attractions", "allAttractions"].includes(tab)) {
      return {
        attractions: results.value,
        cities: cities.value,
        ...emitMap.attraction,
      };
    }

    return {};
  };

  return {
    getViewComponent,
    getViewProps,
  };
}
