<template>
  <button 
    type="submit"
    class="flex w-full justify-center rounded-md border border-transparent px-4 font-medium text-white shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
    :class="{
      'py-1.5 text-xs': deviceType.isMobile,
      'py-2 text-sm': !deviceType.isMobile,
      'bg-indigo-600 hover:bg-indigo-700': !disabled,
      'bg-indigo-400 cursor-not-allowed': disabled,
    }"
    :disabled="loading || disabled"
  >
    <span v-if="loading" class="inline-flex items-center">
      <i class="icon-[eos-icons--loading] mr-2 animate-spin" :class="{
        'size-3': deviceType.isMobile,
        'size-4': !deviceType.isMobile,
      }"></i>
      {{ loadingText }}
    </span>
    <span v-else>{{ text }}</span>
  </button>
</template>

<script setup lang="ts">
import { deviceType } from "@/utils/flexible";

defineProps({
  text: {
    type: String,
    required: true
  },
  loadingText: {
    type: String,
    default: "加载中..."
  },
  loading: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  }
});
</script>
