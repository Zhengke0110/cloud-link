<template>
    <GsapAnimation animation="fadeInUp" :delay="0.1 * (colorIndex % 5)" :duration="0.8" ease="power3.out">
        <div :class="[
            'card-container group relative overflow-hidden rounded-xl border p-0.5 shadow-sm',
            enableHover ? 'hover:border-indigo-200 border-gray-200' : 'border-gray-100'
        ]" ref="cardElement" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
            <!-- 卡片边框渐变 -->
            <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
                :class="getBorderGradient(colorIndex)"></div>

            <!-- 卡片内容 -->
            <div class="relative overflow-hidden rounded-xl bg-white">
                <!-- 顶部标题栏 -->
                <div class="relative overflow-hidden p-5" :class="getHeaderGradient(colorIndex)">
                    <!-- 装饰圆圈 - 完全不透明设计 -->
                    <div class="absolute -top-10 -right-10 h-24 w-24 rounded-full bg-white/10"></div>
                    <div class="absolute -bottom-16 -left-8 h-32 w-32 rounded-full bg-white/10"></div>

                    <div class="relative z-10 flex items-center justify-between">
                        <!-- 标题区域 -->
                        <GsapAnimation animation="fadeInLeft" :delay="0.2 + 0.1 * (colorIndex % 5)" :duration="0.6"
                            ease="back.out(1.2)">
                            <h3 class="text-xl font-bold text-white drop-shadow-sm">
                                <slot name="title">{{ title }}</slot>
                            </h3>
                        </GsapAnimation>

                        <!-- 操作按钮区域 -->
                        <GsapAnimation animation="fadeInRight" :delay="0.3 + 0.1 * (colorIndex % 5)" :duration="0.6">
                            <div class="flex items-center gap-2">
                                <slot name="header-actions">
                                    <span class="text-sm text-white/80">操作</span>
                                </slot>
                            </div>
                        </GsapAnimation>
                    </div>
                </div>

                <!-- 卡片主体内容 - 纯白背景 -->
                <div class="card-body p-6 bg-white">
                    <GsapAnimation animation="fadeIn" :delay="0.4 + 0.1 * (colorIndex % 5)" :duration="0.6">
                        <slot></slot>
                    </GsapAnimation>

                    <!-- 底部操作按钮 -->
                    <GsapAnimation animation="fadeInUp" :delay="0.5 + 0.1 * (colorIndex % 5)" :duration="0.6">
                        <div class="mt-5 flex justify-end space-x-3">
                            <slot name="footer-actions">
                                <!-- 默认底部操作按钮 -->
                            </slot>
                        </div>
                    </GsapAnimation>
                </div>
            </div>
        </div>
    </GsapAnimation>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import gsap from 'gsap';
import GsapAnimation from '@/components/GsapAnimation.vue';
import {
    getHeaderGradient,
    getBorderGradient,
    getIconColor,
    getActionButtonBg
} from "@/utils/ColorSchemeProvider";

// 组件属性定义
const props = defineProps({
    // 卡片标题
    title: {
        type: String,
        default: ''
    },
    // 用于颜色方案的索引
    colorIndex: {
        type: Number,
        default: 0
    },
    // 是否启用悬浮效果
    enableHover: {
        type: Boolean,
        default: true
    }
});

// 暴露颜色工具方法供模板使用
defineExpose({
    getIconColor,
    getActionButtonBg
});

// GSAP动画引用
const cardElement = ref(null);

// 鼠标悬停动画处理
const handleMouseEnter = () => {
    if (!props.enableHover) return;

    gsap.to(cardElement.value, {
        y: -3, // 从 -8 降至 -3，减少位移
        scale: 1.01, // 从 1.02 降至 1.01，减少缩放
        // 大幅降低阴影强度和扩散
        boxShadow: '0 8px 15px -3px rgba(0, 0, 0, 0.03), 0 4px 6px -2px rgba(0, 0, 0, 0.02)',
        duration: 0.3, // 从 0.4 降至 0.3，加快响应
        ease: 'power1.out' // 从 power2.out 改为 power1.out，更柔和
    });
};

const handleMouseLeave = () => {
    if (!props.enableHover) return;

    gsap.to(cardElement.value, {
        y: 0,
        scale: 1,
        // 进一步减轻默认阴影
        boxShadow: '0 2px 4px -1px rgba(0, 0, 0, 0.015), 0 1px 2px -1px rgba(0, 0, 0, 0.01)',
        duration: 0.4, // 从 0.5 降至 0.4
        ease: 'power2.out' // 保持柔和的退出效果
    });
};

// 页面卸载时的退出动画
onMounted(() => {
    // 添加页面初始化时的设置，确保默认样式正确
    gsap.set(cardElement.value, {
        boxShadow: '0 2px 4px -1px rgba(0, 0, 0, 0.015), 0 1px 2px -1px rgba(0, 0, 0, 0.01)', // 进一步减轻初始阴影
        background: 'transparent' // 确保背景透明
    });

    // 添加页面卸载前的侦听器来处理退出动画
    const handleBeforeUnload = () => {
        gsap.to(cardElement.value, {
            opacity: 0,
            y: 20,
            duration: 0.3,
            ease: 'power2.in'
        });
    };

    // 可选：添加到组件生命周期
    onBeforeUnmount(() => {
        handleBeforeUnload();
    });

});
</script>

<style scoped>
/* 卡片阴影效果 - 大幅减轻阴影强度 */
.card-container {
    box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.015),
        0 1px 2px -1px rgba(0, 0, 0, 0.01);
    will-change: transform, box-shadow;
    background-color: #ffffff;
    /* 使用纯白色背景 */
    transition: border-color 0.3s ease;
    /* 添加边框过渡效果 */
}

/* 减少悬停时的边框变化 */
.card-container:hover {
    border-color: rgba(99, 102, 241, 0.1);
    /* 更轻微的边框颜色变化 */
}

/* 标题栏光效 - 大幅减少反光强度 */
[class*="header-gradient-"]::after {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background: linear-gradient(120deg,
            rgba(255, 255, 255, 0.08) 0%,
            /* 从 0.2 降至 0.08 */
            rgba(255, 255, 255, 0) 70%);
    opacity: 0;
    transition: opacity 0.3s ease-out;
    /* 从 0.25s 增至 0.3s */
}

[class*="header-gradient-"]:hover::after {
    opacity: 0.6;
    /* 从 1 降至 0.6，减少光效强度 */
}

/* 移除卡片内容的渐变光效 */
.card-body {
    position: relative;
    z-index: 1;
    background-color: #ffffff;
    /* 确保内容区域纯白色 */
}

.card-body::before {
    content: none;
    /* 移除伪元素内容，去除光效 */
}

/* 悬停时的阴影优化 - 更加轻微 */
.card-container:hover {
    box-shadow: 0 4px 8px -2px rgba(0, 0, 0, 0.02),
        0 2px 4px -1px rgba(0, 0, 0, 0.015) !important;
}

/* 移动端特别优化 */
@media (max-width: 640px) {
    button {
        min-height: 44px;
    }

    /* 移动端禁用悬停效果 */
    .card-container {
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.01) !important;
        border-color: rgba(0, 0, 0, 0.08);
        /* 更轻微的边框 */
    }

    .card-container:hover {
        transform: none !important;
        /* 移动端禁用变换效果 */
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.01) !important;
    }

    /* 更新移动端背景颜色确保清晰度 */
    .card-body {
        background-color: #ffffff;
    }

    /* 移动端卡片内容添加轻微的间距 */
    .card-body {
        padding: 1.25rem;
    }
}

/* 高对比度模式优化 */
@media (prefers-contrast: more) {
    .card-container {
        box-shadow: none;
        border: 1px solid rgba(0, 0, 0, 0.2);
    }
}

/* 无障碍优化 */
</style>
