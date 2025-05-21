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
            <!-- 页面标题区域 -->
            <PageHeader v-if="title" :tag="tag" :title="title" :description="description" />

            <!-- 页面内容区域 -->
            <slot></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
import DecorativeBackground from '@/components/DecorativeBackground.vue';
import PageHeader from '@/components/PageHeader'; // 确保使用正确的导入路径
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
</style>
