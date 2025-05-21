<template>
    <div class="animate-slideUp mx-auto w-full rounded-2xl bg-white shadow-lg" :class="{
        'max-w-xs p-5': deviceType.isMobile,
        'max-w-md p-6': deviceType.isTablet,
        'max-w-lg p-8': deviceType.isDesktop,
    }">
        <!-- 标题区域 -->
        <div class="mb-6 text-center" :class="{ 'mb-8': !isMobile }">
            <h1 class="font-bold tracking-tight text-indigo-600" :class="{
                'text-xl': deviceType.isMobile,
                'text-2xl': deviceType.isTablet,
                'text-3xl': deviceType.isDesktop,
            }">
                {{ title }}
            </h1>
            <p class="mt-2 text-gray-600" :class="{
                'text-xs': deviceType.isMobile,
                'text-sm': deviceType.isTablet,
                'text-base': deviceType.isDesktop,
            }">
                {{ subtitle }}
            </p>
        </div>

        <!-- 通用错误信息 -->
        <div v-if="generalError" class="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
            <p class="text-sm text-red-600" :class="{ 'text-xs': deviceType.isMobile }">
                {{ generalError }}
            </p>
        </div>

        <!-- 表单内容由插槽提供 -->
        <slot></slot>
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { deviceType } from "@/utils/flexible";

// 接收的属性
defineProps({
    // 页面标题
    title: {
        type: String,
        required: true
    },
    // 页面副标题
    subtitle: {
        type: String,
        required: true
    },
    // 通用错误信息
    generalError: {
        type: String,
        default: ""
    }
});

// 计算属性，用于简化模板判断
const isMobile = computed(() => deviceType.value.isMobile);
</script>

<style scoped>
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

.animate-slideUp {
    animation: slideUp 0.8s ease-out forwards;
}
</style>