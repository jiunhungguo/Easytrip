import { computed } from "vue";
import CityTable from "@/components/travel/CityTable.vue";
import CityCardGrid from "@/components/travel/CityCardGridAdmin.vue";
import AttractionTable from "@/components/travel/AttractionTable.vue";
import AttractionCardGrid from "@/components/travel/AttractionCardGridAdmin.vue";

export function useTabView(
  viewMode,
  results,
  cities,
  handleEdit,
  handleDelete,
  cityHeaders,
  attractionHeaders
) {
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
        headers: cityHeaders,
        onEdit: handleEdit,
        onDelete: handleDelete,
      };
    }

    if (["attractions", "allAttractions"].includes(tab)) {
      return {
        attractions: results.value,
        cities: cities.value,
        headers: attractionHeaders,
        onEdit: handleEdit,
        onDelete: handleDelete,
      };
    }

    return {};
  };

  return {
    getViewComponent,
    getViewProps,
  };
}
