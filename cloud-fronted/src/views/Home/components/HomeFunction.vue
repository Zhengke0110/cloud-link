<template>
  <div class="bg-white py-16">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <h3 class="mb-12 text-center font-bold text-gray-900" :class="[deviceType.isMobile ? 'text-2xl' : 'text-3xl']">
        功能特性
      </h3>
      <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
        <div
          class="animate-fadeIn rounded-xl border border-gray-200 bg-white p-6 shadow-md transition-all duration-300 hover:border-indigo-200 hover:shadow-xl"
          style="animation-delay: 0.1s">
          <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-blue-100 text-blue-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <h4 class="mb-2 text-xl font-semibold">链接防刷</h4>
          <p class="text-sm text-gray-600">
            清晰地识别链接是否真实访问，赋予链接更好的分析，掌握线索质，分辨虚假。
          </p>
        </div>

        <div
          class="animate-fadeIn rounded-xl border border-gray-200 bg-white p-6 shadow-md transition-all duration-300 hover:border-indigo-200 hover:shadow-xl"
          style="animation-delay: 0.2s">
          <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h4 class="mb-2 text-xl font-semibold">永久有效</h4>
          <p class="text-sm text-gray-600">
            无需担心过期失效，无虑的链接使用无忧。
          </p>
        </div>

        <div
          class="animate-fadeIn rounded-xl border border-gray-200 bg-white p-6 shadow-md transition-all duration-300 hover:border-indigo-200 hover:shadow-xl"
          style="animation-delay: 0.3s">
          <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-blue-100 text-blue-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          </div>
          <h4 class="mb-2 text-xl font-semibold">访问统计</h4>
          <p class="text-sm text-gray-600">
            短链数据管理统计分析中，开放破条访问数据，全链路数据分析，快速精准判断分析结构。
          </p>
        </div>

        <div
          class="animate-fadeIn rounded-xl border border-gray-200 bg-white p-6 shadow-md transition-all duration-300 hover:border-indigo-200 hover:shadow-xl"
          style="animation-delay: 0.4s">
          <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z" />
            </svg>
          </div>
          <h4 class="mb-2 text-xl font-semibold">投放分享</h4>
          <p class="text-sm text-gray-600">
            清晰识别访问渠道识别，掌握小程序到各个分享的关键。
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { deviceType } from "@/utils/flexible";

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
.animate-fadeIn {
  opacity: 0;
  animation: fadeIn 0.6s ease-out forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.visible {
  opacity: 1;
}

@media (prefers-reduced-motion: reduce) {
  .animate-fadeIn {
    animation: none;
    opacity: 1;
  }
}
</style>