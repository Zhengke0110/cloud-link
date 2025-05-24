<template>
    <div ref="elementRef">
        <slot></slot>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import gsap from 'gsap';

// 修复类型定义
interface AnimationConfig {
    from: Record<string, any>;
    to: Record<string, any> & { ease?: string };
}

type AnimationType = 'fadeIn' | 'fadeInUp' | 'fadeInDown' | 'fadeInLeft' | 'fadeInRight' | 'scale' | 'bounce' | 'rotate' | 'slideIn';

// 修复属性定义，添加更严格的类型
const props = defineProps({
    animation: {
        type: String as () => AnimationType,
        default: 'fadeIn',
        validator: (value: string) => ['fadeIn', 'fadeInUp', 'fadeInDown', 'fadeInLeft', 'fadeInRight', 'scale', 'bounce', 'rotate', 'slideIn'].includes(value)
    },
    duration: {
        type: Number,
        default: 0.6,
    },
    delay: {
        type: Number,
        default: 0,
    },
    ease: {
        type: String,
        default: 'power2.out',
    },
    stagger: {
        type: Number,
        default: 0,
    },
    from: {
        type: Object,
        default: () => ({}),
    },
    to: {
        type: Object,
        default: () => ({}),
    },
    markers: {
        type: Boolean,
        default: false,
    },
    disabled: {
        type: Boolean,
        default: false
    }
});

const elementRef = ref<HTMLElement | null>(null);
let animation: gsap.core.Tween | null = null;

// 优化预定义动画，修复可能的类型问题
const animations: Record<AnimationType, AnimationConfig> = {
    fadeIn: {
        from: { autoAlpha: 0 },
        to: { autoAlpha: 1 },
    },
    fadeInUp: {
        from: { autoAlpha: 0, y: 30 },
        to: { autoAlpha: 1, y: 0 },
    },
    fadeInDown: {
        from: { autoAlpha: 0, y: -30 },
        to: { autoAlpha: 1, y: 0 },
    },
    fadeInLeft: {
        from: { autoAlpha: 0, x: 30 },
        to: { autoAlpha: 1, x: 0 },
    },
    fadeInRight: {
        from: { autoAlpha: 0, x: -30 },
        to: { autoAlpha: 1, x: 0 },
    },
    scale: {
        from: { autoAlpha: 0, scale: 0.8 },
        to: { autoAlpha: 1, scale: 1 },
    },
    bounce: {
        from: { autoAlpha: 0, y: -50 },
        to: { autoAlpha: 1, y: 0, ease: 'bounce.out' },
    },
    rotate: {
        from: { autoAlpha: 0, rotation: -5 },
        to: { autoAlpha: 1, rotation: 0 },
    },
    slideIn: {
        from: { autoAlpha: 0, xPercent: -100 },
        to: { autoAlpha: 1, xPercent: 0 },
    },
};

// 初始化动画的函数 - 重构为独立函数以便重用
const initAnimation = () => {
    // 如果动画被禁用或元素不可用，则跳过
    if (props.disabled || !elementRef.value) return;

    // 检查是否设置了减少动画的偏好
    const prefersReducedMotion = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches || false;

    if (prefersReducedMotion) {
        // 对于减少动画偏好，直接设置为最终状态而非动画
        gsap.set(elementRef.value, { autoAlpha: 1 });
        return;
    }

    try {
        // 确保动画存在于预定义列表或使用自定义配置
        const animConfig = animations[props.animation as AnimationType];
        if (!animConfig && !props.from) {
            console.warn(`未找到名为 ${props.animation} 的动画配置，且未提供自定义from配置`);
            return;
        }

        // 使用预定义动画或自定义动画配置
        const fromProps = animConfig?.from || props.from;
        const toProps = animConfig?.to || props.to;

        // 清理任何现有动画
        if (animation) {
            animation.kill();
        }

        // 创建初始状态
        gsap.set(elementRef.value, fromProps);

        // 创建动画，应用适当的缓动函数
        const ease = props.ease || toProps.ease || 'power2.out';
        animation = gsap.to(elementRef.value, {
            ...toProps,
            ease,
            duration: props.duration,
            delay: props.delay,
            onComplete: () => {
                // 确保动画完成时元素处于正确状态
                if (elementRef.value) {
                    // 有些情况下，可能需要确保特定属性被设置
                    if (toProps.autoAlpha === 1) {
                        gsap.set(elementRef.value, { visibility: 'visible' });
                    }
                }
            }
        });

        // 为开发调试添加标记
        if (props.markers) {
            console.log(`GSAP Animation: ${props.animation}, Duration: ${props.duration}, Delay: ${props.delay}, Ease: ${ease}`);
        }
    } catch (error) {
        console.error('GSAP动画初始化错误:', error);
        // 确保元素可见，即使动画失败
        if (elementRef.value) {
            gsap.set(elementRef.value, { autoAlpha: 1 });
        }
    }
};

// 监听props变化以重新初始化动画
watch(() => [props.animation, props.duration, props.delay, props.disabled], () => {
    // 当关键属性变化时重新初始化动画
    if (elementRef.value) {
        initAnimation();
    }
});

onMounted(() => {
    initAnimation();
});

onBeforeUnmount(() => {
    // 清理动画以避免内存泄漏
    if (animation) {
        animation.kill();
        animation = null;
    }
});
</script>
