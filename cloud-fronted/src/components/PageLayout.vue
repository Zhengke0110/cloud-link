<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 via-white to-gray-50 py-12 md:py-16">
        <!-- 背景插槽，允许自定义背景元素 -->
        <slot name="background"></slot>

        <!-- 顶部装饰线条 -->
        <div ref="topGradient"
            class="absolute left-0 right-0 top-0 h-1.5 bg-gradient-to-r from-indigo-400 via-blue-500 to-indigo-400 opacity-85 shadow-sm"
            :style="{ backgroundSize: '200% 100%' }">
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 页面标题区域 -->
            <div v-if="title" class="mb-10 text-center md:mb-12">
                <GsapAnimation animation="fadeIn" :duration="0.8" :disabled="prefersReducedMotion">
                    <div>
                        <!-- 顶部标签 -->
                        <GsapAnimation v-if="tag" animation="fadeInDown" :delay="0.2" :duration="0.6"
                            :disabled="prefersReducedMotion">
                            <span ref="tagElement"
                                class="mb-4 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm uppercase tracking-wider"
                                :class="{ 'transform -translate-y-0.5 bg-indigo-100': isTagHovered }">
                                {{ tag }}
                            </span>
                        </GsapAnimation>

                        <!-- 标题部分 -->
                        <GsapAnimation animation="fadeInUp" :delay="0.3" :duration="0.7"
                            :disabled="prefersReducedMotion">
                            <h2 ref="titleElement"
                                class="mb-5 text-2xl font-bold md:text-3xl lg:text-5xl transition-transform duration-300"
                                :class="{ 'scale-101': isTitleHovered }">
                                <slot name="title">
                                    <span
                                        class="bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-blue-600">
                                        {{ title }}
                                    </span>
                                </slot>
                            </h2>
                        </GsapAnimation>

                        <!-- 分隔装饰 -->
                        <GsapAnimation :from="{ width: 0, opacity: 0 }" :to="{ width: '60px', opacity: 0.8 }"
                            :delay="0.4" :duration="0.6" :disabled="prefersReducedMotion">
                            <div ref="separator"
                                class="mx-auto mb-5 h-[3px] bg-gradient-to-r from-purple-500 via-blue-500 to-purple-500 rounded-md opacity-80 transition-all duration-300"
                                :class="{ 'w-[80px]': isSeparatorHovered, 'w-[60px]': !isSeparatorHovered }">
                            </div>
                        </GsapAnimation>

                        <!-- 描述文本 -->
                        <GsapAnimation v-if="description" animation="fadeInUp" :delay="0.5" :duration="0.7"
                            :disabled="prefersReducedMotion">
                            <p
                                class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base lg:text-lg leading-relaxed tracking-wide">
                                {{ description }}
                            </p>
                        </GsapAnimation>

                        <!-- 底部装饰线条 -->
                        <GsapAnimation :from="{ width: 0, opacity: 0 }" :to="{ width: '120px', opacity: 0.5 }"
                            :delay="0.6" :duration="0.5" :disabled="prefersReducedMotion">
                            <div ref="bottomSeparator"
                                class="mx-auto mt-6 h-[2px] bg-gradient-to-r from-purple-500/30 via-blue-500/50 to-purple-500/30 rounded-md transition-all duration-300"
                                :class="{ 'w-[140px] opacity-80': isBottomSeparatorHovered, 'w-[120px] opacity-50': !isBottomSeparatorHovered }">
                            </div>
                        </GsapAnimation>
                    </div>
                </GsapAnimation>
            </div>

            <!-- 页面内容区域 -->
            <div>
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import gsap from 'gsap';
import { useElementHover, useMediaQuery } from '@vueuse/core';
import GsapAnimation from './GsapAnimation.vue';

// 定义属性
defineProps({
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
const tagElement = ref<HTMLSpanElement | null>(null);
const titleElement = ref<HTMLHeadingElement | null>(null);
const separator = ref<HTMLDivElement | null>(null);
const bottomSeparator = ref<HTMLDivElement | null>(null);

// 使用 useElementHover 监听元素悬停状态
const isTagHovered = useElementHover(tagElement);
const isTitleHovered = useElementHover(titleElement);
const isSeparatorHovered = useElementHover(separator);
const isBottomSeparatorHovered = useElementHover(bottomSeparator);

// 使用 useMediaQuery 检测用户减少动画的设置
const prefersReducedMotion = useMediaQuery('(prefers-reduced-motion: reduce)');

// 顶部渐变动画初始化
onMounted(() => {
    if (!prefersReducedMotion.value && topGradient.value) {
        gsap.to(topGradient.value, {
            backgroundPosition: "200% 0%",
            duration: 8,
            repeat: -1,
            ease: "none",
        });
    }
});

// 当用户改变偏好设置时响应
watch(prefersReducedMotion, (reduced) => {
    if (reduced && topGradient.value) {
        // 停止顶部渐变动画
        gsap.killTweensOf(topGradient.value);
    } else if (!reduced && topGradient.value) {
        // 重新启动顶部渐变动画
        gsap.to(topGradient.value, {
            backgroundPosition: "200% 0%",
            duration: 8,
            repeat: -1,
            ease: "none",
        });
    }
});

// 为分隔线添加脉冲动画
onMounted(() => {
    if (!prefersReducedMotion.value && separator.value) {
        gsap.to(separator.value, {
            opacity: 0.9,
            duration: 1.5,
            repeat: -1,
            yoyo: true,
            ease: "sine.inOut"
        });
    }
});
</script>