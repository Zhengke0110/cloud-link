<template>
  <div
    class="animate-fadeIn flex min-h-screen items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-12 motion-reduce:animate-none sm:px-6 lg:px-8">
    <div class="animate-slideUp max-w-lg text-center" :class="{
      'max-w-md': deviceType.isMobile,
      'max-w-xl': deviceType.isDesktop,
    }">
      <div class="flex justify-center">
        <img :src="ErrorIllustration" alt="Error illustration"
          class="mb-6 transform rounded-xl shadow-lg transition-all duration-300 hover:scale-105 hover:shadow-xl"
          :class="[
            deviceType.isMobile
              ? 'h-32 w-auto'
              : deviceType.isTablet
                ? 'h-48 w-auto'
                : 'h-64 w-auto',
          ]" />
      </div>

      <h1 class="font-extrabold tracking-tighter text-indigo-600" :class="[
        deviceType.isMobile
          ? 'text-7xl'
          : deviceType.isTablet
            ? 'text-8xl'
            : 'text-9xl',
      ]">
        404
      </h1>

      <h2 class="mt-4 font-bold tracking-tight text-gray-900" :class="[
        deviceType.isMobile
          ? 'text-2xl'
          : deviceType.isTablet
            ? 'text-2xl'
            : 'text-3xl',
      ]">
        页面未找到
      </h2>

      <p class="mt-3 text-gray-500" :class="[deviceType.isMobile ? 'text-base' : 'text-lg']">
        很抱歉，我们找不到您正在寻找的页面。可能它已被移动或不存在。
      </p>

      <div class="mt-8 flex justify-center gap-4" :class="{
        'flex-col': deviceType.isMobile,
        'gap-2': deviceType.isMobile,
      }">
        <button @click="goBack"
          class="rounded-lg bg-indigo-600 px-5 py-3 text-sm font-medium text-white shadow-md transition-all hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
          :class="{ 'w-full': deviceType.isMobile }">
          返回上一页
        </button>

        <router-link to="/"
          class="rounded-lg border border-indigo-100 bg-white px-5 py-3 text-sm font-medium text-indigo-600 shadow-md transition-all hover:bg-gray-50 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
          :class="{ 'w-full': deviceType.isMobile }">
          返回首页
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ErrorIllustration from "@/assets/error-illustration.svg";
import { deviceType } from "@/utils/flexible";

function goBack() {
  window.history.back();
}
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fadeIn {
  animation: fadeIn 0.6s ease-out forwards;
}

.animate-slideUp {
  animation: slideUp 0.8s ease-out forwards;
}

/* 减少动画对有偏好的用户的影响 */
@media (prefers-reduced-motion: reduce) {
  .motion-reduce:animate-none {
    animation: none !important;
  }
}
</style>
