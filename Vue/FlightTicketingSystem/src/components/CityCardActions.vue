<template>
  <div>
    <v-card-actions class="justify-center pb-4">
      <v-btn color="primary" size="small" @click="dialog = true">修改</v-btn>
      <v-btn color="error" size="small" @click="confirmDelete">刪除</v-btn>
    </v-card-actions>

    <v-dialog v-model="dialog" max-width="500">
      <v-card>
        <v-card-title>編輯城市</v-card-title>
        <v-card-text>
          <v-form ref="formRef" v-model="valid">
            <v-text-field v-model="form.name" label="城市名稱" :rules="[r]" />
            <v-text-field v-model="form.country" label="國家" :rules="[r]" />
            <v-text-field v-model="form.latitude" label="緯度" :rules="[r]" />
            <v-text-field v-model="form.longitude" label="經度" :rules="[r]" />
            <v-file-input
              v-model="form.imageFile"
              label="上傳新圖片"
              accept="image/*"
              prepend-icon="mdi-camera"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="dialog = false">取消</v-btn>
          <v-btn color="primary" @click="submitEdit" :loading="loading">
            更新
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
