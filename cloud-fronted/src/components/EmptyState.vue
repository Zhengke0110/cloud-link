<template>
  <div class="rounded-xl border border-gray-100 bg-white p-10 text-center shadow-sm">
    <div class="mb-4 inline-flex h-16 w-16 items-center justify-center rounded-full" :class="iconBgClass">
      <slot name="icon">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" :class="iconClass" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" 
            d="M2 6a2 2 0 012-2h4l2 2h4a2 2 0 012 2v1H8a3 3 0 00-3 3v1.5a1.5 1.5 0 01-3 0V6z" 
            clip-rule="evenodd" />
          <path d="M6 12a2 2 0 012-2h8a2 2 0 012 2v2a2 2 0 01-2 2H2h2a2 2 0 002-2v-2z" />
        </svg>
      </slot>
    </div>
    <h3 class="mb-1 text-lg font-medium text-gray-900">{{ title }}</h3>
    <p class="mb-4 text-gray-500">
      {{ description }}
    </p>
    <slot name="action"></slot>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

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
  }
});

// 根据类型计算图标背景颜色类
const iconBgClass = computed(() => {
  const colorMap: Record<string, string> = {
    indigo: 'bg-indigo-100',
    blue: 'bg-blue-100',
    red: 'bg-red-100',
    green: 'bg-green-100',
    purple: 'bg-purple-100',
    yellow: 'bg-yellow-100',
    gray: 'bg-gray-100'
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
