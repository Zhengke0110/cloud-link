<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 via-white to-gray-50 py-12 md:py-16">
        <!-- 背景插槽，允许自定义背景元素 -->
        <slot name="background"></slot>

        <!-- 顶部装饰线条 -->
        <div
            class="absolute left-0 right-0 top-0 h-1.5 bg-gradient-to-r from-indigo-400 via-blue-500 to-indigo-400 opacity-85 shadow-sm animate-gradient">
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 页面标题区域 -->
            <div v-if="title" class="reveal-element mb-10 text-center md:mb-12">
                <!-- 顶部标签 -->
                <span v-if="tag"
                    class="mb-4 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm uppercase tracking-wider hover:bg-indigo-100 transform transition duration-200 hover:-translate-y-0.5">
                    {{ tag }}
                </span>

                <!-- 标题部分 -->
                <h2
                    class="mb-5 text-2xl font-bold md:text-3xl lg:text-5xl transition-all duration-300 ease-in-out hover:scale-101">
                    <slot name="title">
                        <span class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-blue-600">
                            {{ title }}
                        </span>
                    </slot>
                </h2>

                <!-- 分隔装饰 - 简化但保留自定义样式类 -->
                <div class="separator mx-auto mb-5"></div>

                <!-- 描述文本 -->
                <p v-if="description"
                    class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base lg:text-lg leading-relaxed tracking-wide">
                    {{ description }}
                </p>

                <!-- 底部装饰线条 -->
                <div class="bottom-separator mx-auto mt-6"></div>
            </div>

            <!-- 页面内容区域 -->
            <slot></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
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
/* 保留自定义动画定义 */
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

@keyframes pulseGlow {

    0%,
    100% {
        opacity: 0.7;
    }

    50% {
        opacity: 0.9;
    }
}

/* Tailwind 不支持的动画类 */
.animate-gradient {
    background-size: 200% 200%;
    animation: gradientShift 8s ease infinite;
}

/* 为hover:scale-101添加自定义值 */
.hover\:scale-101:hover {
    transform: scale(1.01);
    text-shadow: 0 0 10px rgba(79, 70, 229, 0.25);
}

/* 分隔符样式保留自定义类 */
.separator {
    height: 3px;
    width: 60px;
    background: linear-gradient(90deg, #8b5cf6, #3b82f6, #8b5cf6);
    border-radius: 3px;
    opacity: 0.8;
    transition: width 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    animation: pulseGlow 3s infinite;
}

.separator:hover {
    width: 80px;
}

.bottom-separator {
    height: 2px;
    width: 120px;
    background: linear-gradient(90deg, rgba(139, 92, 246, 0.3), rgba(59, 130, 246, 0.5), rgba(139, 92, 246, 0.3));
    border-radius: 2px;
    transition: all 0.3s ease;
}

.bottom-separator:hover {
    width: 140px;
    opacity: 0.8;
}

/* 元素显示动画优化 - 保留深度选择器 */
:deep(.reveal-element) {
    opacity: 0;
    transform: translateY(20px);
    transition:
        opacity 0.7s cubic-bezier(0.22, 1, 0.36, 1),
        transform 0.7s cubic-bezier(0.22, 1, 0.36, 1);
    will-change: opacity, transform;
}

:deep(.reveal-element.revealed) {
    opacity: 1;
    transform: translateY(0);
}

/* 延迟类 */
:deep(.reveal-element.delay-300) {
    transition-delay: 0.3s;
}

:deep(.reveal-element.delay-500) {
    transition-delay: 0.5s;
}

:deep(.reveal-element.delay-700) {
    transition-delay: 0.7s;
}

/* 卡片元素的hover效果 */
:deep([class*="rounded-"]) {
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

:deep([class*="rounded-"]:hover) {
    transform: translateY(-2px);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.08), 0 4px 6px -2px rgba(0, 0, 0, 0.04);
}

/* 保留无障碍性调整 */
@media (prefers-reduced-motion: reduce) {

    *,
    ::before,
    ::after {
        animation-duration: 0.01ms !important;
        animation-iteration-count: 1 !important;
        transition-duration: 0.01ms !important;
        scroll-behavior: auto !important;
    }

    :deep(.reveal-element),
    .separator,
    .bottom-separator {
        opacity: 1 !important;
        transform: none !important;
        transition: none !important;
    }
}

/* 移动端优化 */
@media (max-width: 640px) {
    :deep(.reveal-element) {
        opacity: 0.5;
        transform: translateY(10px);
    }

    .separator {
        height: 2px;
        width: 50px;
    }

    .bottom-separator {
        height: 1.5px;
        width: 100px;
    }
}
</style>
