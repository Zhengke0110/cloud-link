<template>
  <div class="reveal-element mb-8 text-center md:mb-10">
    <!-- 顶部标签 -->
    <span class="mb-3 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm">
      {{ tag }}
    </span>

    <!-- 标题部分 -->
    <h2
      class="mb-4 bg-gradient-to-r from-indigo-600 to-blue-600 bg-clip-text text-2xl font-bold text-transparent md:text-3xl lg:text-5xl">
      {{ title }}
    </h2>

    <!-- 分隔装饰 - 增加了一个小分隔符来增强间距感 -->
    <div
      class="mx-auto mb-4 h-1 w-16 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600 opacity-70">
    </div>

    <!-- 描述文本 - 增加了顶部外边距和最大宽度控制 -->
    <p class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base lg:text-lg">
      {{ description }}
    </p>

    <!-- 底部装饰线条 -->
    <div class="mx-auto mt-6 h-1 w-24 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600"></div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  tag: {
    type: String,
    required: true,
    default: "标签"
  },
  title: {
    type: String,
    required: true,
    default: "标题"
  },
  description: {
    type: String,
    required: true,
    default: "描述文本"
  }
});
</script>

<style scoped>
/* 元素显示动画 */
.reveal-element {
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-element.revealed {
  opacity: 1;
  transform: translateY(0);
}

/* 移动端优化 */
@media (max-width: 640px) {
  .reveal-element {
    opacity: 0.1;
  }
}

/* 修复一些动画问题 */
@media (prefers-reduced-motion: reduce) {
  .reveal-element {
    opacity: 1 !important;
    transform: none !important;
    transition: none !important;
  }
}

/* 让渐变背景有动画效果 */
[class*="bg-gradient-to-r"] {
  background-size: 200% 200%;
  animation: gradientShift 6s ease infinite;
}

/* 卡片颜色循环显示动画 */
@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0% 50%;
  }
}

/* 添加标题悬停效果 */
h2 {
  position: relative;
  transition: transform 0.3s ease;
}

h2:hover {
  transform: scale(1.01);
}

/* 分隔线动画效果 */
div.h-1 {
  transition: width 0.3s ease;
}

div.h-1:hover {
  width: 32px;
}
</style>
