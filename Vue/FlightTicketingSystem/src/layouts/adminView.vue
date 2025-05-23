<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <v-layout>
    <v-navigation-drawer
      v-model="drawer"
      expand-on-hover
      rail
      @mouseenter="isHovered = true"
      @mouseleave="isHovered = false"
    >
      <v-list density="compact" item-props :items="items" nav>
        <v-list-item v-for="item in items" @click="$router.push(item.path)">
          <v-icon :icon="item.prependIcon"></v-icon>
          <span v-if="isHovered">{{ item.title }}</span>
        </v-list-item>
      </v-list>

      <template #append>
        <v-list-item
          class="ma-2"
          link
          nav
          prepend-icon="mdi-logout"
          title="登出"
          @click="logoutChange()"
        />
      </template>
    </v-navigation-drawer>

    <v-app-bar border="b" class="ps-4" flat>
      <v-app-bar-nav-icon
        v-if="$vuetify.display.smAndDown"
        @click="drawer = !drawer"
      />

      <v-app-bar-title class="ms-auto"
        ><img
          src="@/assets/Easytrip_text.png"
          alt="My Icon"
          width="12%"
          height="12%"
      /></v-app-bar-title>
      <h5 style="margin-left: auto">
        用戶名: {{ authStore.user ? authStore.user.username : "未登入" }}
      </h5>

      <template #append>
        <v-btn class="text-none me-2" height="48" icon slim>
          <v-avatar color="surface-light" class="mdi mdi-account" size="32" />

          <v-menu activator="parent">
            <v-list density="compact" nav>
              <v-list-item
                append-icon="mdi-cog-outline"
                link
                title="Settings"
              />

              <v-list-item
                append-icon="mdi-logout"
                link
                title="登出"
                @click="logoutChange()"
              />
            </v-list>
          </v-menu>
        </v-btn>
      </template>
    </v-app-bar>

    <v-main>
      <div class="pa-4">
        <router-view></router-view>
      </div>
    </v-main>
  </v-layout>
</template>

<script setup>
import { ref, watch } from "vue";
import { useAuthStore } from "@/stores/auth"; // 引入 Pinia store
import { logout } from "@/utils/logout"; // 導入登出函數
import { useRouter } from "vue-router"; // 引入 vue-router

const router = useRouter(); // 使用 vue-router

const authStore = useAuthStore(); // 使用 store
const drawer = ref(true);
const isHovered = ref(false); // 追蹤懸停狀態
// 使用 watch 監控 drawer 的狀態變化
watch(drawer, (newVal, oldVal) => {
  console.log(
    `側邊欄狀態改變：從 ${oldVal ? "展開" : "縮起"} 到 ${
      newVal ? "展開" : "縮起"
    }`
  );
  // 在這裡可以執行其他邏輯，例如記錄狀態或觸發動畫
});

const logoutChange = () => {
  logout(); // 將 router 實例傳遞給 logout 函數
  router.push("/login"); // 確保這行沒有問題，直接將用戶跳轉到登入頁(看需不需要回登入)
  console.log("登出成功");
};

const items = ref([
  {
    title: "登入畫面",
    prependIcon: "mdi-view-dashboard-outline",
    link: true,
    path: "/login",
  },
  {
    title: "會員管理",
    prependIcon: "mdi mdi-account-group",
    link: true,
    path: "/admin/members",
  },
  {
    title: "機場管理",
    prependIcon: "mdi-airport",
    link: true,
    path: "/admin/airport",
  },
  {
    title: "航線管理",
    prependIcon: "mdi mdi-airplane-takeoff",
    link: true,
    path: "/admin/flight",
  },
  {
    title: "票務訂單管理",
    prependIcon: "mdi mdi-ticket",
    link: false,
    path: "/admin/Ticket",
  },
  {
    title: "座位管理",
    prependIcon: "mdi mdi-seat",
    link: false,
    path: "/admin/Seats",
  },
  {
    title: "旅行管理",
    prependIcon: "mdi mdi-island",
    link: false,
    path: "/admin/travel",
  },

  {
    title: "商品管理",
    prependIcon: "mdi mdi-gift-outline",
    link: true,
    path: "/admin/products",
  },
  // {
  //   title: "商品管理",
  //   prependIcon: "mdi mdi-gift-outline",
  //   link: true,
  //   path: "/admin/testtt",
  // },
]);
</script>
