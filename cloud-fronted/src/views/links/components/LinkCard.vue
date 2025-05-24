<template>
    <GsapAnimation animation="fadeInUp" :delay="0.1 * (colorIndex % 5)" :duration="0.8" ease="power3.out">
        <div :class="[
            'card-container group relative overflow-hidden rounded-xl border bg-white/80 p-0.5 shadow-md',
            enableHover ? 'hover:border-indigo-200' : 'border-gray-100'
        ]" ref="cardElement" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
            <!-- 卡片边框渐变 -->
            <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
                :class="getBorderGradient(colorIndex)"></div>

            <!-- 卡片内容 -->
            <div class="relative overflow-hidden rounded-xl enhanced-glassmorphism">
                <!-- 顶部标题栏 -->
                <div class="relative overflow-hidden p-5" :class="getHeaderGradient(colorIndex)">
                    <!-- 装饰圆圈 - 增强模糊效果 -->
                    <div class="absolute -top-10 -right-10 h-24 w-24 rounded-full bg-white/20 backdrop-blur-lg"></div>
                    <div class="absolute -bottom-16 -left-8 h-32 w-32 rounded-full bg-white/15 backdrop-blur-lg"></div>

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

                <!-- 卡片主体内容 -->
                <div class="card-body p-6 backdrop-blur-xl bg-white/85">
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
        y: -8,
        scale: 1.02,
        boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
        duration: 0.4,
        ease: 'power2.out'
    });
};

const handleMouseLeave = () => {
    if (!props.enableHover) return;

    gsap.to(cardElement.value, {
        y: 0,
        scale: 1,
        boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.05), 0 4px 6px -2px rgba(0, 0, 0, 0.03)',
        duration: 0.5,
        ease: 'power3.out'
    });
};

// 页面卸载时的退出动画
onMounted(() => {
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
/* 增强的毛玻璃效果 */
.enhanced-glassmorphism {
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
}

/* 卡片阴影效果 - 不需要过渡属性，因为由GSAP处理 */
.card-container {
    box-shadow:
        0 10px 15px -3px rgba(0, 0, 0, 0.05),
        0 4px 6px -2px rgba(0, 0, 0, 0.03);
    will-change: transform, box-shadow;
}

/* 标题栏光效 */
[class*="header-gradient-"]::after {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background: linear-gradient(120deg,
            rgba(255, 255, 255, 0.3) 0%,
            rgba(255, 255, 255, 0) 70%);
    opacity: 0;
    transition: opacity 0.25s ease-out;
}

[class*="header-gradient-"]:hover::after {
    opacity: 1;
}

/* 移动端优化 */
@media (max-width: 640px) {
    button {
        min-height: 44px;
    }

    .enhanced-glassmorphism {
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
    }
}

/* 无障碍优化 - GSAP组件已内置此功能 */
</style>
