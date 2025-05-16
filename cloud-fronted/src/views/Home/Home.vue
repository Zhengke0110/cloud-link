<template>
  <div class="animate-fadeIn motion-reduce:animate-none">
    <HomeBanner />
    <HomeScenarios />
    <HomeDetail />
    <!-- 使用抽取出来的功能特性组件 -->
    <HomeFunction />

    <!-- 底部号召行动区 -->
    <div class="bg-gradient-to-r from-indigo-600 to-blue-500 py-16 text-white">
      <div class="container mx-auto px-4 text-center sm:px-6 lg:px-8">
        <h3 class="font-bold" :class="[deviceType.isMobile ? 'text-2xl' : 'text-3xl']">
          简单易用的渠道短链接统计工具
        </h3>
        <p class="mt-4" :class="[deviceType.isMobile ? 'text-base' : 'text-lg']">
          开始使用Timu短链平台，优化您的营销策略
        </p>
        <button
          class="mt-8 rounded-lg bg-white px-8 py-3 font-semibold text-indigo-600 shadow-lg transition-all hover:bg-gray-100 hover:shadow-xl focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-indigo-500 focus:outline-none"
          :class="{ 'w-full px-4 text-base': deviceType.isMobile, 'text-lg': !deviceType.isMobile }">
          开始试用
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { deviceType } from "@/utils/flexible";
import HomeBanner from "./components/HomeBanner.vue";
import HomeScenarios from "./components/HomeScenarios.vue";
import HomeDetail from "./components/HomeDetail.vue";
import HomeFunction from "./components/HomeFunction.vue";

// 添加滚动动画效果
onMounted(() => {
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("visible");
        }
      });
    },
    { threshold: 0.1 }
  );

  document.querySelectorAll(".animate-fadeIn").forEach((el) => {
    observer.observe(el);
  });
});
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
  opacity: 0;
  animation: fadeIn 0.6s ease-out forwards;
}

.animate-slideUp {
  opacity: 0;
  animation: slideUp 0.8s ease-out forwards;
}

/* 减少动画对有偏好的用户的影响 */
@media (prefers-reduced-motion: reduce) {
  .motion-reduce\:animate-none {
    animation: none !important;
  }
}

.visible {
  opacity: 1;
}
</style>
