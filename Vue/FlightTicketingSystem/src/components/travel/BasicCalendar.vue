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
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title class="text-h6">新增行程</v-card-title>
      <v-card-text>
        <v-select
          label="選擇城市"
          :items="cities.map((c) => c.name)"
          v-model="selectedCity"
          @change="updateSpots"
          :rules="[required]" />
        <v-select
          label="選擇景點"
          :items="filteredSpots"
          item-title="name"
          v-model="selectedSpot"
          :rules="[required]" />
        <v-text-field
          label="開始時間"
          v-model="startTime"
          type="datetime-local"
          :rules="[required]" />
        <v-text-field
          label="結束時間"
          v-model="endTime"
          type="datetime-local"
          :rules="[required]" />
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn variant="outlined" @click="dialog = false">取消</v-btn>
        <v-btn color="primary" variant="flat" @click="saveEvent">新增</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useCalendarStore } from "@/stores/calendarStore";
import { useCityStore } from "@/stores/cityStore";
import { useToast } from "vue-toastification";
const toast = useToast();
const cityStore = useCityStore();
const calendarRef = ref(null);

const dialog = ref(false);
const selectedCity = ref("");
const selectedSpot = ref("");
const startTime = ref("");
const endTime = ref("");

const cities = computed(() => cityStore.cities);
const filteredSpots = computed(() => {
  return cities.value.find((c) => c.name === selectedCity.value)?.spots || [];
});

const required = (v) => !!v || "必填";

const updateSpots = () => {
  selectedSpot.value = "";
};

const saveEvent = () => {
  if (!selectedCity.value || !selectedSpot.value || !startTime.value) return;

  calendarRef.value.getApi().addEvent({
    title: `${selectedCity.value} - ${selectedSpot.value}`,
    start: startTime.value,
    end: endTime.value,
    color: "#3B82F6",
  });
  dialog.value = false;
  toast.success("行程已新增 ✅");
};

const calendarOptions = {
  plugins: [dayGridPlugin, interactionPlugin],
  headerToolbar: {
    left: "prev,next today",
    center: "title",
    right: "",
  },
  buttonText: {
    today: "今日",
    prev: "<",
    next: ">",
  },
  initialView: "dayGridMonth",
  locale: "zh-tw",
  firstDay: 1,
  selectable: true,
  editable: true,
  events: [],
  select: (info) => {
    dialog.value = true;
    startTime.value = info.startStr;
    endTime.value = info.endStr;
  },
};
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
.swal-confirm-btn,
.swal-cancel-btn {
  color: white !important;
}

.swal-confirm-btn {
  background-color: #3b82f6 !important; /* blue-500 */
}

.swal-cancel-btn {
  background-color: #9ca3af !important; /* gray-400 */
}
</style>
