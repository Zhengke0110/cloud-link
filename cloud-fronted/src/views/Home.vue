<template>
  <div class="animate-fadeIn motion-reduce:animate-none">
    <!-- 主横幅部分 -->
    <div class="animate-slideUp container mx-auto px-4 py-16 sm:px-6 lg:px-8">
      <div class="flex flex-col items-center justify-between md:flex-row"
        :class="{ 'text-center': deviceType.isMobile }">
        <div class="mb-10 text-center md:mb-0 md:max-w-xl md:text-left">
          <h2 class="mb-3 font-bold tracking-tight text-gray-900" :class="[
            deviceType.isMobile
              ? 'text-3xl'
              : deviceType.isTablet
                ? 'text-4xl'
                : 'text-5xl',
          ]">
            Timu短链平台
          </h2>
          <p class="mb-8 text-gray-600" :class="[deviceType.isMobile ? 'text-lg' : 'text-xl']">
            简单易用的渠道短链接统计工具
          </p>
          <button
            class="rounded-lg bg-indigo-600 px-6 py-3 text-white shadow-md transition-all hover:bg-indigo-700 hover:shadow-xl focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
            :class="[deviceType.isMobile ? 'text-base w-full' : 'text-lg font-semibold']">
            创建短链
          </button>
        </div>
        <div class="relative" :class="[
          deviceType.isMobile
            ? 'h-48 w-48'
            : deviceType.isTablet
              ? 'h-56 w-56'
              : 'h-64 w-64',
        ]">
          <div
            class="absolute h-full w-full transform rounded-lg bg-indigo-600 shadow-2xl transition-all duration-300 hover:scale-105 hover:shadow-indigo-300">
          </div>
          <div
            class="absolute top-6 left-6 h-full w-full transform rounded-lg bg-indigo-500 shadow-2xl transition-all duration-300 hover:scale-105 hover:shadow-indigo-300">
          </div>
          <div
            class="absolute top-12 left-12 h-full w-full transform rounded-lg bg-indigo-400 shadow-2xl transition-all duration-300 hover:scale-105 hover:shadow-indigo-200">
          </div>
        </div>
      </div>
    </div>

    <!-- 应用场景部分 -->
    <div class="bg-white py-16">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <h3 class="mb-12 text-center font-bold text-gray-900" :class="[deviceType.isMobile ? 'text-2xl' : 'text-3xl']">
          应用场景
        </h3>
        <div class="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4">
          <div class="animate-fadeIn rounded-xl bg-white p-6 shadow-md transition-all duration-300 hover:shadow-xl"
            style="animation-delay: 0.1s">
            <div class="mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
              </svg>
            </div>
            <h4 class="mb-2 text-xl font-semibold">电商平台</h4>
            <p class="text-gray-600">
              优化分享链接，提升用户体验和转化率，帮助电商平台实现更高效的营销策略。
            </p>
          </div>

          <div class="animate-fadeIn rounded-xl bg-white p-6 shadow-md transition-all duration-300 hover:shadow-xl"
            style="animation-delay: 0.2s">
            <div class="mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-blue-100 text-blue-600">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <h4 class="mb-2 text-xl font-semibold">社群运营</h4>
            <p class="text-gray-600">
              追踪社群链接分享情况，精准衡量传播效果，助力社群精细化运营。
            </p>
          </div>

          <div class="animate-fadeIn rounded-xl bg-white p-6 shadow-md transition-all duration-300 hover:shadow-xl"
            style="animation-delay: 0.3s">
            <div class="mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-blue-100 text-blue-600">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
            <h4 class="mb-2 text-xl font-semibold">数据统计</h4>
            <p class="text-gray-600">
              收集链接访问数据，分析用户行为，提供深度洞察支持决策制定。
            </p>
          </div>

          <div class="animate-fadeIn rounded-xl bg-white p-6 shadow-md transition-all duration-300 hover:shadow-xl"
            style="animation-delay: 0.4s">
            <div class="mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <h4 class="mb-2 text-xl font-semibold">渠道分析</h4>
            <p class="text-gray-600">
              识别高效推广渠道，优化营销资源分配，提升整体推广效果。
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 电商平台功能详情 -->
    <div class="bg-gray-50 py-16">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="lg:grid lg:grid-cols-2 lg:gap-8">
          <div class="mb-10 lg:mb-0">
            <h3 class="mb-6 text-2xl font-bold text-gray-900">
              电商平台应用详情
            </h3>
            <div class="space-y-6">
              <div class="flex">
                <div class="mr-4 flex h-12 w-12 items-center justify-center rounded-full bg-indigo-600 text-white">
                  01
                </div>
                <div>
                  <p class="text-gray-600">
                    电商及社交等各类app中展示不同业务场景转发分享时，使用短链可引导触碰导购。便于分享传播，提高转化率。
                  </p>
                </div>
              </div>

              <div class="flex">
                <div class="mr-4 flex h-12 w-12 items-center justify-center rounded-full bg-indigo-600 text-white">
                  02
                </div>
                <div>
                  <p class="text-gray-600">
                    分享后台精准，渠道识别以及引流统计清晰，给予更佳用户体验！
                  </p>
                </div>
              </div>

              <div class="flex">
                <div class="mr-4 flex h-12 w-12 items-center justify-center rounded-full bg-indigo-600 text-white">
                  03
                </div>
                <div>
                  <p class="text-gray-600">
                    短链接转发及转发后，在有限的字数里完整呈现信息，提高传播效果！
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div class="relative">
            <div class="grid animate-pulse grid-cols-3 gap-4">
              <div class="col-span-1 h-32 rounded-lg bg-indigo-200"></div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-300"></div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-200"></div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-300"></div>
              <div class="col-span-1 flex h-32 items-center justify-center rounded-lg bg-indigo-500">
                <div class="font-bold text-white">变更追踪</div>
              </div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-300"></div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-200"></div>
              <div class="col-span-1 h-32 rounded-lg bg-indigo-300"></div>
              <div class="col-span-1 flex h-32 items-center justify-center rounded-lg bg-indigo-500">
                <div class="font-bold text-white">提高转化效果</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能特性部分 -->
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
