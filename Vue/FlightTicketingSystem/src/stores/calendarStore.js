// calendarStore.js
import { defineStore } from "pinia";
import { ref } from "vue";

export const useCalendarStore = defineStore("calendar", () => {
  const events = ref([
    {
      id: "1",
      title: "偽裝會議",
      start: "2025-04-24T10:00:00",
      end: "2025-04-24T11:00:00",
    },
  ]);

  const addEvent = (event) => {
    event.id = Date.now().toString(); // 就說是ID生成器
    events.value.push(event);
  };

  const removeEvent = (id) => {
    events.value = events.value.filter((e) => e.id !== id);
  };

  const updateEvent = (updated) => {
    const index = events.value.findIndex((e) => e.id === updated.id);
    if (index !== -1)
      events.value[index] = { ...events.value[index], ...updated };
  };

  return { events, addEvent, removeEvent, updateEvent };
});
