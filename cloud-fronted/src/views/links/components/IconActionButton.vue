<template>
  <button :class="[
    'rounded-full p-1.5 transition-colors flex items-center justify-center',
    variant === 'light' ? lightVariantClass : darkVariantClass,
    size === 'sm' ? 'h-7 w-7' : size === 'lg' ? 'h-9 w-9' : 'h-8 w-8',
    disabled ? 'opacity-50 cursor-not-allowed' : '',
    customClass
  ]" :disabled="disabled" :title="title" @click="$emit('click', $event)">
    <!-- 使用插槽渲染图标 -->
    <slot>
      <!-- 默认图标 -->
      <svg v-if="icon" :class="[
        size === 'sm' ? 'h-4 w-4' : size === 'lg' ? 'h-6 w-6' : 'h-5 w-5',
        variant === 'light' ? '' : 'text-white'
      ]" viewBox="0 0 20 20" fill="currentColor">
        <!-- 编辑图标 -->
        <path v-if="icon === 'edit'"
          d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />

        <!-- 删除图标 -->
        <path v-else-if="icon === 'delete'" fill-rule="evenodd"
          d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
          clip-rule="evenodd" />

        <!-- 复制图标 - 修复：将两个路径合并到一个条件块中 -->
        <g v-else-if="icon === 'copy'">
          <path d="M8 3a1 1 0 011-1h2a1 1 0 110 2H9a1 1 0 01-1-1z" />
          <path d="M6 3a2 2 0 00-2 2v11a2 2 0 002 2h8a2 2 0 002-2V5a2 2 0 00-2-2 3 3 0 01-3 3H9a3 3 0 01-3-3z" />
        </g>

        <!-- 分享图标 -->
        <g v-else-if="icon === 'share'">
          <path d="M8 2a1 1 0 000 2h2a1 1 0 100-2H8z" />
          <path
            d="M3 5a2 2 0 002-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v6h-4.586l1.293-1.293a1 1 0 00-1.414-1.414l-3 3a1 1 0 000 1.414l3 3a1 1 0 001.414-1.414L10.414 13H15v3a2 2 0 01-2 2H5a2 2 0 01-2-2V5zM15 11h2a1 1 0 110 2h-2v-2z" />
        </g>

        <!-- 查看图标 -->
        <path v-else-if="icon === 'view'" fill-rule="evenodd"
          d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
          clip-rule="evenodd" />
      </svg>
    </slot>
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';

// 按钮的属性
const props = defineProps({
  // 按钮图标类型
  icon: {
    type: String,
    default: '',
    validator: (value: string) => {
      return ['', 'edit', 'delete', 'copy', 'share', 'view'].includes(value);
    }
  },
  // 按钮变体: dark (深色背景) 或 light (浅色背景)
  variant: {
    type: String,
    default: 'dark',
    validator: (value: string) => {
      return ['dark', 'light'].includes(value);
    }
  },
  // 按钮尺寸
  size: {
    type: String,
    default: 'md',
    validator: (value: string) => {
      return ['sm', 'md', 'lg'].includes(value);
    }
  },
  // 自定义类名
  customClass: {
    type: String,
    default: ''
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  },
  // 按钮提示文本
  title: {
    type: String,
    default: ''
  },
  // 按钮类型
  type: {
    type: String,
    default: 'danger',
    validator: (value: string) => {
      return ['default', 'primary', 'danger', 'warning'].includes(value);
    }
  }
});

// 发出的事件
defineEmits(['click']);

// 根据不同变体和类型计算样式类
const darkVariantClass = computed(() => {
  if (props.type === 'danger') {
    return 'bg-white/10 text-white hover:bg-red-400';
  } else if (props.type === 'primary') {
    return 'bg-white/10 text-white hover:bg-blue-400';
  } else if (props.type === 'warning') {
    return 'bg-white/10 text-white hover:bg-yellow-400';
  }
  return 'bg-white/10 text-white hover:bg-white/20';
});

const lightVariantClass = computed(() => {
  if (props.type === 'danger') {
    return 'bg-gray-100 text-gray-700 hover:bg-red-100 hover:text-red-600';
  } else if (props.type === 'primary') {
    return 'bg-gray-100 text-gray-700 hover:bg-blue-100 hover:text-blue-600';
  } else if (props.type === 'warning') {
    return 'bg-gray-100 text-gray-700 hover:bg-yellow-100 hover:text-yellow-600';
  }
  return 'bg-gray-100 text-gray-700 hover:bg-gray-200';
});
</script>
