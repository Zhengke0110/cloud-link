<template>
  <GsapAnimation animation="fadeIn" :duration="0.5" :delay="0.1"
    class="rounded-xl border border-gray-100 bg-white/90 p-6 sm:p-8 md:p-10 text-center shadow-sm transition-all duration-300 hover:shadow-md backdrop-blur-sm">
    <GsapAnimation animation="scale" :duration="0.6" :delay="0.3" ease="back.out(1.7)"
      class="mb-5 inline-flex h-16 w-16 items-center justify-center rounded-full" :class="iconBgClass">
      <slot name="icon">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 transition-all duration-300" :class="iconClass"
          viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M2 6a2 2 0 012-2h4l2 2h4a2 2 0 012 2v1H8a3 3 0 00-3 3v1.5a1.5 1.5 0 01-3 0V6z"
            clip-rule="evenodd" />
          <path d="M6 12a2 2 0 012-2h8a2 2 0 012 2v2a2 2 0 01-2 2H2h2a2 2 0 002-2v-2z" />
        </svg>
      </slot>
    </GsapAnimation>

    <!-- 文本内容 -->
    <GsapAnimation animation="fadeInUp" :delay="0.4" :duration="0.5">
      <h3 class="mb-2 text-lg sm:text-xl font-medium text-gray-900">{{ title }}</h3>
      <p class="mb-5 text-gray-500 max-w-sm mx-auto leading-relaxed">
        {{ description }}
      </p>
    </GsapAnimation>

    <!-- 操作按钮 -->
    <GsapAnimation animation="fadeInUp" :delay="0.5" :duration="0.5" class="mt-6">
      <slot name="action"></slot>
    </GsapAnimation>
  </GsapAnimation>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';

const props = defineProps({
  // 标题文本
  title: {
    type: String,
    default: '暂无数据'
  },
  // 描述文本
  description: {
    type: String,
    default: '当前没有可显示的数据'
  },
  // 图标颜色类型 (indigo, blue, red, green, purple, yellow, gray)
  iconType: {
    type: String,
    default: 'indigo'
  },
  // 是否禁用动画
  disableAnimation: {
    type: Boolean,
    default: false
  }
});

// 根据类型计算图标背景颜色类
const iconBgClass = computed(() => {
  const colorMap: Record<string, string> = {
    indigo: 'bg-indigo-100 ring-1 ring-indigo-50 shadow-sm',
    blue: 'bg-blue-100 ring-1 ring-blue-50 shadow-sm',
    red: 'bg-red-100 ring-1 ring-red-50 shadow-sm',
    green: 'bg-green-100 ring-1 ring-green-50 shadow-sm',
    purple: 'bg-purple-100 ring-1 ring-purple-50 shadow-sm',
    yellow: 'bg-yellow-100 ring-1 ring-yellow-50 shadow-sm',
    gray: 'bg-gray-100 ring-1 ring-gray-50 shadow-sm'
  };

  return colorMap[props.iconType] || colorMap.indigo;
});

// 根据类型计算图标颜色类
const iconClass = computed(() => {
  const colorMap: Record<string, string> = {
    indigo: 'text-indigo-600',
    blue: 'text-blue-600',
    red: 'text-red-600',
    green: 'text-green-600',
    purple: 'text-purple-600',
    yellow: 'text-yellow-600',
    gray: 'text-gray-600'
  };

  return colorMap[props.iconType] || colorMap.indigo;
});
</script>

<style scoped>
/* 空状态容器样式 */
:deep(.gsap-animation) {
  will-change: transform, opacity;
}

/* 按钮悬停效果保留 */
:deep(button) {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

:deep(button:hover) {
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 响应式优化 */
@media (max-width: 640px) {
  :deep(.icon-container) {
    height: 3.5rem;
    width: 3.5rem;
  }

  :deep(svg) {
    height: 1.75rem;
    width: 1.75rem;
  }
}
</style>
