<template>
  <div>
    <h1>航線管理</h1>
  </div>
  <v-container>
    <!-- 起始機場搜尋欄 -->
    <v-autocomplete
      clearable
      label="起始機場"
      :items="allairports"
      v-model="searchFilters.originAirport"
    ></v-autocomplete>
    <!-- 抵達機場搜尋欄 -->
    <v-autocomplete
      clearable
      label="抵達機場"
      :items="allairports"
      
      v-model="searchFilters.destinationAirport"
    ></v-autocomplete>
    <label for="arrivalTime">起飛日期:</label>

    <VueDatePicker
      id="arrivalTime"
      v-model="searchFilters.arrivalTime"
      :format="'yyyy/MM/dd--HH:mm'"
      :enable-time="true"
    ></VueDatePicker>
    <label for="departureTime">降落日期:</label>
    <VueDatePicker
      id="departureTime"
      v-model="searchFilters.departureTime"
      :format="'yyyy/MM/dd--HH:mm'"
      :enable-time="true"
    ></VueDatePicker>
    <!-- 機型選擇框 -->
    <v-select
      clearable
      label="機型"
      :items="allModels"
      v-model="searchFilters.modelName"
    ></v-select>
    <v-btn prepend-icon="mdi mdi-magnify" @click="search"> 搜尋 </v-btn>
    <v-btn color="primary" @click="openAddDialog">新增航線</v-btn>
    <br />
    <br />
    <p>總數: {{ totalItems }}</p>
    <v-data-table
      :headers="headers"
      :items="items"
      :items-per-page="itemsPerPage"
      hide-default-footer
      class="elevation-1"
    >
      <template v-slot:item.actions="{ item }">
        <div class="d-flex ga-2 justify-end">
          <v-icon
            color="medium-emphasis"
            icon="mdi-pencil"
            size="small"
            @click="edit(item.id)"
          ></v-icon>

          <v-icon
            color="medium-emphasis"
            icon="mdi-delete"
            size="small"
            @click="remove(item.id)"
          ></v-icon>
        </div>
      </template>
    </v-data-table>
 <!-- 分頁組件Pagination -->
 <v-pagination v-model="currentPage" :length="totalPages"></v-pagination>
    <!-- 新增修改用對話框 -->
    <v-dialog v-model="dialog" max-width="700">
      <v-card>
        <!-- 動態標題 -->
        <v-card-title>
          {{ isEditing ? "編輯航線" : "新增航線" }}
        </v-card-title>
        <v-autocomplete
          clearable
          label="起始機場"
          :items="allairports"
          v-model="record.originAirport"
        ></v-autocomplete>
         <!-- 抵達機場搜尋欄 -->
         <v-autocomplete
          clearable
          label="抵達機場"
          :items="allairports"
          v-model="record.destinationAirport"
        ></v-autocomplete>
        <v-col cols="12" md="6">
          <label for="arrivalTime">起飛日期:</label>

          <VueDatePicker
            id="arrivalTime"
            v-model="record.arrivalTime"
            :format="'yyyy/MM/dd--HH:mm'"
            :enable-time="true"
          ></VueDatePicker>
        </v-col>
        <v-col cols="12" md="6">
          <label for="departureTime">降落日期:</label>
          <VueDatePicker
            id="departureTime"
            v-model="record.departureTime"
            :format="'yyyy/MM/dd--HH:mm'"
            :enable-time="true"
          ></VueDatePicker>
        </v-col> 
        <v-number-input
          v-model="record.economyPrice"
          :reverse="false"
          controlVariant="default"
          label="經濟艙票價"
          :hideInput="false"
          inset
        ></v-number-input>
        <v-number-input
          v-model="record.firstClassPrice"
          :reverse="false"
          controlVariant="default"
          label="頭等艙票價"
          :hideInput="false"
          inset
        ></v-number-input>

        <v-number-input
          v-model="record.businessPrice"
          :reverse="false"
          controlVariant="default"
          label="商務艙艙票價"
          :hideInput="false"
          inset
        ></v-number-input>
        <v-select
          clearable
          label="機型"
          :items="allModels"
          v-model="record.modelName"
        ></v-select>
        <!-- 操作按鈕 -->
         <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="dialog = false">取消</v-btn>
          <v-btn text color="primary" @click="save">保存</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 確認刪除對話框 -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="headline">確認刪除</v-card-title>
        <v-card-text> 您確定要刪除該記錄嗎？此操作無法撤銷。 </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="deleteDialog = false">取消</v-btn>
          <v-btn text color="red" @click="deleteItem">確定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { ref, watch, onMounted, shallowRef } from "vue";
import { ApiFlight, ApiAirport } from "@/utils/API";
import VueDatePicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import { filterItems } from "vuetify/lib/composables/filter";
onMounted(() => {
  getDistinctAirportName();
  getfullairports();
  resetRecord(); // 重置表單
});

const allairports = ref([]); //所有不重複機場數據
const allModels = ref(["01", "02"]); //所有不重複機型數據
const allfullairports=ref([])

const getDistinctAirportName = () => {
  ApiAirport.DistinctAirportName().then((res) => {
    allairports.value = res.data;
  });
};

const getfullairports = () => {
  ApiAirport.getAllAirports().then((res) => {
    allfullairports.value = res.data;
    console.log(allfullairports.value);
    
  });
}


//搜尋用初始數據
const searchFilters = ref({
  originAirport: "",
  destinationAirport: "",
  arrivalTime: "",
  departureTime: "",
  modelName: "",
  originIata: "",
  destinationIata: "",
  
});

// 默認初始數據
const DEFAULT_RECORD = {
  originAirport: "",
  destinationAirport: "",
  arrivalTime: "",
  departureTime: "",
  modelName: "",
  firstClassPrice:0,
  businessPrice:0,
  economyPrice:0,
};
// 重置記錄
function resetRecord() {
  record.value = { ...DEFAULT_RECORD };
}
//搜尋函式
function search() {
  // 格式化日期為後端所需的格式
  const formattedDepartureTime = formatDate(searchFilters.value.departureTime);
  const formattedArrivalTime = formatDate(searchFilters.value.arrivalTime);
  

console.log(searchFilters.value);

console.log(formattedArrivalTime);

// 呼叫 API 搜尋航班
ApiFlight.searchFlights(
    searchFilters.value.originAirport,
   searchFilters.value.destinationAirport,
   formattedDepartureTime,
   formattedDepartureTime,
   searchFilters.value.modelName,
    currentPage.value,
    itemsPerPage.value,
)
    .then((res) => {
      console.log(res.data);
      items.value = res.data.content; // 更新表格數據
      totalItems.value = res.data.totalElements; // 總數據條數
      totalPages.value = res.data.totalPages; // 總頁數
    })
    
}

//開啟新增對話框函式
function openAddDialog() {
  resetRecord(); // 重置表單
  isEditing.value = false; // 設置為新增模式
 
  dialog.value = true; // 打開對話框
}

// 保存方法
function save() {
  if (
    !record.value.originAirport ||
    !record.value.destinationAirport ||
    !record.value.modelName ||
    !record.value.firstClassPrice ||
    !record.value.businessPrice ||
    !record.value.economyPrice
  ) {
    alert("請填寫所有必填字段！");
    return;
  }

  if (isEditing.value) {

    record.value.arrivalTime = formatDate(record.value.arrivalTime);
    record.value.departureTime = formatDate(record.value.departureTime);
    
    // 更新機場數據
    ApiFlight.updateFlight(record.value.id,record.value)
      .then(() => {
        
        dialog.value = false; // 關閉對話框
        search();
      })
      .catch((err) => {
        alert("更新失敗！");
      });
  } else {

    record.value.arrivalTime = formatDate(record.value.arrivalTime);
    record.value.departureTime = formatDate(record.value.departureTime);
    // 新增機場數據
      ApiFlight.createFlight(record.value)
      .then(() => {
        dialog.value = false; // 關閉對話框
        search();
      })
      .catch((err) => {
        alert("新增失敗！");
      });
  }
}

// 表格標題資訊
const headers = ref([
  { title: "航線 ID", value: "id", sortable: true, align: "start" }, //sortable: true 表示可排序
  { title: "起始機場", value: "originAirport", sortable: true },
  { title: "抵達機場", value: "destinationAirport", sortable: true },
  { title: "起飛時間", value: "arrivalTime", sortable: true },
  { title: "抵達時間", value: "departureTime", sortable: true },
  { title: "機型", value: "modelName", sortable: true },
  { title: "頭等艙票價", value: "firstClassPrice", sortable: true },
  { title: "商務艙票價", value: "businessPrice", sortable: true },
  { title: "經濟艙票價", value: "economyPrice", sortable: true },

  { title: "操作", key: "actions", align: "end", sortable: false },
]);

const items = ref([]); // 表格內容數據
const totalItems = ref(0); // 總數據條數
const itemsPerPage = ref(20); // 每頁顯示數據條數
const deleteDialog = shallowRef(false); // 控制刪除對話框的顯示狀態
const record = ref(); // 存儲當前選中的記錄

// 新增修改對話框顯示狀態
const dialog = shallowRef(false);

// 是否處於編輯模式
const isEditing = shallowRef(false);
function edit(id) {
  isEditing.value = true;

  const found = items.value.find((item) => item.id === id);

  // 直接使用展開運算符賦值
  record.value = { ...found };

  dialog.value = true; // 打開對話框
}

function remove(id) {
  const found = items.value.find((item) => item.id === id);
  if (!found) {
    alert("未找到該記錄！");
    return;
  }

  // 更新 record 為選中的記錄，並顯示刪除確認對話框
  record.value = { ...found };
  deleteDialog.value = true; // 顯示刪除對話框
}

function deleteItem() {
  if (!record.value) {
    alert("未選中刪除的記錄！");
    return;
  }

  ApiFlight.deleteFlight(record.value.id)
    .then(() => {
      alert("刪除成功！");
      deleteDialog.value = false; // 關閉刪除對話框
      search(); // 刷新數據
    })
    .catch((err) => {
      alert("刪除失敗！");
    });
}


const currentPage = ref(1); // 當前頁數
const totalPages = ref(1); // 總頁數
// 監聽當前頁數變化，自動加載新數據
watch(currentPage, () => {
  search();
});


function formatDate(date) {
  if (!date) return null; // 如果日期為空，返回 null
  const d = new Date(date); // 確保是 Date 對象
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0'); // 月份從 0 開始
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');

  // 返回格式化的字符串
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
</script>

<style scoped></style>
