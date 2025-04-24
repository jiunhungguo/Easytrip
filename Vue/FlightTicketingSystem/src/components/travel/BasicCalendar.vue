<template>
  <div class="bg-white rounded-xl p-6 shadow max-w-4xl mx-auto">
    <h2 class="text-xl font-bold mb-4 flex items-center gap-2">
      <i class="mdi mdi-calendar-blank-outline text-green-500 text-2xl"></i>
      行程日曆
    </h2>
    <FullCalendar
      class="custom-calendar"
      :options="calendarOptions"
      ref="calendarRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import Swal from "sweetalert2";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useCalendarStore } from "@/stores/calendarStore";
import { useCityStore } from "@/stores/cityStore";

const calendar = useCalendarStore();
const cityStore = useCityStore();

const select = async (info) => {
  const cityOptions = cityStore.cities.reduce((obj, city) => {
    obj[city.id] = city.name;
    return obj;
  }, {});

  const { value: cityId } = await Swal.fire({
    title: "選擇城市",
    input: "select",
    inputOptions: cityOptions,
    inputPlaceholder: "請選擇城市",
    showCancelButton: true,
  });

  if (!cityId) return;

  const selectedCity = cityStore.cities.find((c) => c.id == cityId);
  if (!selectedCity || !selectedCity.spots.length) {
    Swal.fire("錯誤", "此城市沒有景點！", "error");
    return;
  }

  const spotOptions = selectedCity.spots.reduce((obj, spot) => {
    obj[spot.id] = spot.name;
    return obj;
  }, {});

  const { value: spotId } = await Swal.fire({
    title: "選擇景點",
    input: "select",
    inputOptions: spotOptions,
    inputPlaceholder: "請選擇景點",
    showCancelButton: true,
  });

  if (!spotId) return;

  const selectedSpot = selectedCity.spots.find((s) => s.id == spotId);

  calendar.addEvent({
    title: `${selectedCity.name}：${selectedSpot.name}`,
    start: info.startStr,
    end: info.endStr,
  });

  Swal.fire("新增成功", `${selectedSpot.name} 已加入行程`, "success");
};

const calendarOptions = ref({
  plugins: [dayGridPlugin, interactionPlugin],
  locale: "zh-tw", // 語系換成繁體中文
  firstDay: 1, // 從星期一開始
  initialView: "dayGridMonth",
  editable: true,
  selectable: true,
  events: computed(() => calendar.events),

  customButtons: {
    today: {
      text: "今日",
    },
  },

  buttonText: {
    prev: "<",
    next: ">",
  },

  headerToolbar: {
    left: "prev,next today",
    center: "title",
    right: "",
  },

  select: select, // 你的 callback
});

const calendarRef = ref(null);

onMounted(async () => {
  await nextTick();
  setTimeout(() => {
    const calendarApi = calendarRef.value.getApi?.();
    if (calendarApi) {
      calendarApi.updateSize();
    }
  }, 100); // 延遲一點，給 DOM 一口氣喘
});
</script>

<style scoped>
/* 自定義 FullCalendar 的 header toolbar 按鈕 */
:deep(.fc .fc-button) {
  background-color: #10b981; /* Tailwind 的綠色 */
  color: white;
  font-weight: 600;
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 1rem;
  transition: background-color 0.2s;
}

:deep(.fc .fc-button:hover) {
  background-color: #059669; /* 深一點的綠 */
}

:deep(.fc .fc-button.fc-button-active) {
  background-color: #047857;
}

:deep(.fc .fc-button.fc-today-button) {
  background-color: #3b82f6; /* blue-500 */
  color: white;
  font-weight: 600;
  border: none;
  padding: 8px 16px;
  border-radius: 0.5rem;
  transition: background-color 0.2s;
}

:deep(.fc .fc-button.fc-today-button:hover) {
  background-color: #2563eb; /* blue-600 */
}

:deep(.fc .fc-button-group > .fc-button:not(:last-child)) {
  border-right: 1px solid white;
}
:deep(.fc .fc-button-group > .fc-button:last-child) {
  border-left: 1px solid white;
}
</style>
