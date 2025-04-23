<template>
  <div class="bg-white rounded-xl p-6 shadow max-w-4xl mx-auto">
    <h2 class="text-xl font-bold mb-4 flex items-center gap-2">
      <i class="mdi mdi-calendar-blank-outline text-green-500 text-2xl"></i>
      行程日曆
    </h2>
    <FullCalendar
      class="custom-calendar"
      :options="calendarOptions"
      ref="calendarRef"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";

const calendarOptions = ref({
  plugins: [dayGridPlugin, interactionPlugin],
  headerToolbar: {
    start: "prev,next",
    center: "",
    end: "today",
  },
  customButtons: {
    today: {
      text: "今日",
    },
  },
  buttonText: {
    prev: "<",
    next: ">",
  },
  initialView: "dayGridMonth",
  locale: "zh-tw", // 語系換成繁體中文
  firstDay: 1,
  headerToolbar: {
    left: "prev,next today",
    center: "title",
    right: "",
  },
  selectable: true,
  editable: true,
  events: [
    {
      title: "🛫 出發台北",
      start: "2025-04-25",
      color: "#34D399",
    },
    {
      title: "🎢 台中玩耍",
      start: "2025-04-27",
      color: "#60A5FA",
    },
  ],
  eventClick(info) {
    alert(`你點到了：${info.event.title}`);
  },
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
