<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { LayoutMenu } from "@/config";
import { useRoute } from "vue-router";
import Toast from '@/components/Toast.vue';

const route = useRoute();

</script>

<template>
  <template v-if="route.meta?.layout === LayoutMenu.BasicLayout">
    <BasicLayout />
  </template>
  <template v-else>
    <router-view v-slot="{ Component }">
      <transition name="page" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </template>

  <!-- 添加优化后的Toast组件 -->
  <Toast />
</template>

<style>
.page-enter-active,
.page-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.page-enter-from,
.page-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
