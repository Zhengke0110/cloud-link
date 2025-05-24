<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 via-white to-gray-50 py-10 md:py-14">
        <!-- 背景装饰效果优化 -->
        <div class="absolute inset-0 z-0 opacity-20">
            <DecorativeBackground />
        </div>

        <!-- 添加微妙的顶部装饰线条 -->
        <div
            class="absolute left-0 right-0 top-0 h-1 bg-gradient-to-r from-indigo-400 via-blue-500 to-indigo-400 opacity-80">
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 页面标题区域 (整合PageHeader) -->
            <div v-if="title" class="reveal-element mb-8 text-center md:mb-10">
                <!-- 顶部标签 -->
                <span v-if="tag"
                    class="mb-3 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm">
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
                <p v-if="description" class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base lg:text-lg">
                    {{ description }}
                </p>

                <!-- 底部装饰线条 -->
                <div
                    class="mx-auto mt-6 h-1 w-24 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600">
                </div>
            </div>

            <!-- 页面内容区域 -->
            <slot></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
import DecorativeBackground from '@/components/DecorativeBackground.vue';
import { onMounted } from 'vue';
import { initPageAnimations } from '@/utils/AnimationUtils';

// 定义属性
const props = defineProps({
    // 页面标签 (可选)
    tag: {
        type: String,
        default: ''
    },
    // 页面标题 (可选)
    title: {
        type: String,
        default: ''
    },
    // 页面描述 (可选)
    description: {
        type: String,
        default: ''
    },
    // 是否自动初始化动画
    autoInitAnimate: {
        type: Boolean,
        default: true
    }
});

// 自动初始化页面动画
onMounted(() => {
    if (props.autoInitAnimate) {
        initPageAnimations();
    }
});
</script>

<style scoped>
/* 元素显示动画 */
:deep(.reveal-element) {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

:deep(.reveal-element.delay-300) {
    transition-delay: 0.3s;
}

:deep(.reveal-element.delay-500) {
    transition-delay: 0.5s;
}

:deep(.reveal-element.revealed) {
    opacity: 1;
    transform: translateY(0);
}

/* 移动端优化 */
@media (max-width: 640px) {
    :deep(.reveal-element) {
        opacity: 0.1;
    }
}

/* 修复一些动画问题 */
@media (prefers-reduced-motion: reduce) {
    :deep(.reveal-element) {
        opacity: 1 !important;
        transform: none !important;
        transition: none !important;
    }
}

/* 让渐变背景有动画效果 */
[class*="bg-gradient-to-r"],
[class*="bg-gradient-to-b"] {
    background-size: 200% 200%;
    animation: gradientShift 12s ease infinite;
}

/* 更平滑的渐变切换 */
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

/* 更紧凑的间距处理 */
:deep(.space-y-4) {
    margin-top: 0.75rem;
}

:deep(.mb-8) {
    margin-bottom: 1.5rem;
}

:deep(.py-3) {
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
}

/* 添加微妙的阴影效果增强视觉层次 */
:deep(.rounded-lg),
:deep(.rounded-xl) {
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

/* 响应式调整 */
@media (min-width: 768px) {
    :deep(.space-y-4) {
        margin-top: 1rem;
    }

    :deep(.mb-8) {
        margin-bottom: 2rem;
    }
}

/* 从PageHeader组件整合的样式 */
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
