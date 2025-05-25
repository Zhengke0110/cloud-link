<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 via-white to-gray-50 py-12 md:py-16">
        <!-- 背景插槽，允许自定义背景元素 -->
        <slot name="background"></slot>

        <!-- 顶部装饰线条 -->
        <div ref="topGradient"
            class="absolute left-0 right-0 top-0 h-1.5 bg-gradient-to-r from-indigo-400 via-blue-500 to-indigo-400 opacity-85 shadow-sm">
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 页面标题区域 -->
            <div v-if="title" ref="titleSection" class="mb-10 text-center md:mb-12 opacity-0">
                <!-- 顶部标签 -->
                <span v-if="tag" ref="tagElement"
                    class="mb-4 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm uppercase tracking-wider">
                    {{ tag }}
                </span>

                <!-- 标题部分 -->
                <h2 ref="titleElement" class="mb-5 text-2xl font-bold md:text-3xl lg:text-5xl">
                    <slot name="title">
                        <span class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-blue-600">
                            {{ title }}
                        </span>
                    </slot>
                </h2>

                <!-- 分隔装饰 -->
                <div ref="separator"
                    class="mx-auto mb-5 h-[3px] w-[60px] bg-gradient-to-r from-purple-500 via-blue-500 to-purple-500 rounded-md opacity-80">
                </div>

                <!-- 描述文本 -->
                <p v-if="description" ref="descElement"
                    class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base lg:text-lg leading-relaxed tracking-wide">
                    {{ description }}
                </p>

                <!-- 底部装饰线条 -->
                <div ref="bottomSeparator"
                    class="mx-auto mt-6 h-[2px] w-[120px] bg-gradient-to-r from-purple-500/30 via-blue-500/50 to-purple-500/30 rounded-md">
                </div>
            </div>

            <!-- 页面内容区域 -->
            <div ref="contentArea">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import gsap from 'gsap';
import { useElementHover, useMediaQuery, useMounted } from '@vueuse/core';

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

// 引用元素
const topGradient = ref<HTMLDivElement | null>(null);
const titleSection = ref<HTMLDivElement | null>(null);
const tagElement = ref<HTMLSpanElement | null>(null);
const titleElement = ref<HTMLHeadingElement | null>(null);
const separator = ref<HTMLDivElement | null>(null);
const descElement = ref<HTMLParagraphElement | null>(null);
const bottomSeparator = ref<HTMLDivElement | null>(null);
const contentArea = ref<HTMLDivElement | null>(null);

// 使用 useElementHover 监听元素悬停状态
const isTagHovered = useElementHover(tagElement);
const isTitleHovered = useElementHover(titleElement);
const isSeparatorHovered = useElementHover(separator);
const isBottomSeparatorHovered = useElementHover(bottomSeparator);

// 使用 useMediaQuery 检测用户减少动画的设置
const prefersReducedMotion = useMediaQuery('(prefers-reduced-motion: reduce)');

// 使用 useMounted 可以替代标准的 onMounted
const isMounted = useMounted();

// 初始化动画
const initAnimations = () => {
    // 如果用户偏好减少动画，则直接显示所有内容
    if (prefersReducedMotion.value) {
        // 直接设置元素为可见
        gsap.set([titleSection.value, tagElement.value, titleElement.value,
        separator.value, descElement.value, bottomSeparator.value],
            { opacity: 1, y: 0, x: 0, scale: 1 });
        return;
    }

    // 创建时间线
    const tl = gsap.timeline({ defaults: { ease: "power3.out" } });

    // 顶部渐变动画
    if (topGradient.value) {
        gsap.to(topGradient.value, {
            backgroundPosition: "200% 0%",
            duration: 8,
            repeat: -1,
            ease: "none",
        });
    }

    // 标题区域动画
    if (titleSection.value) {
        tl.to(titleSection.value, {
            opacity: 1,
            duration: 0.8,
            ease: "power2.out"
        });
    }

    // 标签动画
    if (tagElement.value) {
        tl.from(tagElement.value, {
            y: -20,
            opacity: 0,
            duration: 0.6,
        }, "-=0.4");

        // 动态监听 hover 状态
        watch(isTagHovered, (hovered) => {
            gsap.to(tagElement.value, {
                y: hovered ? -2 : 0,
                backgroundColor: hovered ? "#e0e7ff" : "#eef2ff",
                duration: 0.2
            });
        });
    }

    // 标题动画
    if (titleElement.value) {
        tl.from(titleElement.value, {
            y: 30,
            opacity: 0,
            duration: 0.7
        }, "-=0.4");

        // 监听 hover 状态
        watch(isTitleHovered, (hovered) => {
            gsap.to(titleElement.value, {
                scale: hovered ? 1.01 : 1,
                duration: 0.3
            });
        });
    }

    // 分隔线动画
    if (separator.value) {
        tl.from(separator.value, {
            width: 0,
            opacity: 0,
            duration: 0.6
        }, "-=0.5");

        // 监听 hover 状态
        watch(isSeparatorHovered, (hovered) => {
            gsap.to(separator.value, {
                width: hovered ? 80 : 60,
                duration: 0.3
            });
        });

        // 脉冲动画
        gsap.to(separator.value, {
            opacity: 0.9,
            duration: 1.5,
            repeat: -1,
            yoyo: true,
            ease: "sine.inOut"
        });
    }

    // 描述文本动画
    if (descElement.value) {
        tl.from(descElement.value, {
            y: 20,
            opacity: 0,
            duration: 0.7
        }, "-=0.4");
    }

    // 底部分隔线动画
    if (bottomSeparator.value) {
        tl.from(bottomSeparator.value, {
            width: 0,
            opacity: 0,
            duration: 0.5
        }, "-=0.5");

        // 监听 hover 状态
        watch(isBottomSeparatorHovered, (hovered) => {
            gsap.to(bottomSeparator.value, {
                width: hovered ? 140 : 120,
                opacity: hovered ? 0.8 : 0.5,
                duration: 0.3
            });
        });
    }
};

// 使用 useMounted 和 watch 替代 onMounted
watch(isMounted, (mounted) => {
    if (mounted && props.autoInitAnimate && typeof gsap !== 'undefined') {
        // 等待下一帧以确保DOM已完全渲染
        requestAnimationFrame(() => {
            initAnimations();
        });
    }
}, { immediate: true });

// 当用户改变偏好设置时响应
watch(prefersReducedMotion, (reduced) => {
    if (reduced) {
        // 停止所有动画元素的动画
        const elementsToKill = [
            topGradient.value,
            titleSection.value,
            tagElement.value,
            titleElement.value,
            separator.value,
            descElement.value,
            bottomSeparator.value
        ];

        // 移除所有补间动画
        elementsToKill.forEach(el => {
            if (el) gsap.killTweensOf(el);
        });

        // 直接设置元素为可见
        gsap.set([titleSection.value, tagElement.value, titleElement.value,
        separator.value, descElement.value, bottomSeparator.value],
            { opacity: 1, y: 0, x: 0, scale: 1 });
    } else if (isMounted.value) {
        // 如果用户从减少动画切换回正常模式，重新初始化动画
        initAnimations();
    }
});
</script>
