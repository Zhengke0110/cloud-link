<template>
    <div class="group relative overflow-hidden rounded-lg border border-gray-100 bg-white p-0.5 shadow-sm hover:border-gray-200"
        :class="getBorderHoverClass" ref="cardRef">
        <!-- 渐变背景 -->
        <div class="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-10" :class="getGradientClass"></div>

        <GsapAnimation animation="fadeInUp" :duration="0.5" :delay="delay * 0.15">
            <div class="relative flex rounded-lg bg-white p-4 md:p-5" :class="getHoverBgClass">
                <!-- 图标容器 -->
                <div ref="iconRef"
                    class="feature-icon relative mr-3 md:mr-4 flex h-10 md:h-12 w-10 md:w-12 flex-shrink-0 items-center justify-center rounded-lg shadow-sm"
                    :class="[
                        getIconBgClass,
                        getIconTextClass,
                    ]">
                    <!-- 默认图标 -->
                    <slot name="icon">
                        <svg v-if="icon === 'lightning'" xmlns="http://www.w3.org/2000/svg"
                            class="h-5 md:h-6 w-5 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 10V3L4 14h7v7l9-11h-7z" />
                        </svg>
                        <svg v-else-if="icon === 'shield'" xmlns="http://www.w3.org/2000/svg"
                            class="h-5 md:h-6 w-5 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                        <svg v-else-if="icon === 'document'" xmlns="http://www.w3.org/2000/svg"
                            class="h-5 md:h-6 w-5 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                        </svg>
                        <svg v-else-if="icon === 'globe'" xmlns="http://www.w3.org/2000/svg"
                            class="h-5 md:h-6 w-5 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c-1.657 0-3-4.03-3-9s1.343-9 3-9m0 18c1.657 0 3-4.03 3-9s-1.343-9-3-9" />
                        </svg>
                    </slot>
                </div>

                <!-- 内容区域 -->
                <div class="flex-1">
                    <h4 class="text-base md:text-lg font-medium text-gray-800">
                        {{ title }}
                    </h4>
                    <p class="mt-1 md:mt-2 text-sm text-gray-600 leading-relaxed">
                        {{ description }}
                    </p>
                    <slot name="content"></slot>
                </div>
            </div>
        </GsapAnimation>
    </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';
import gsap from 'gsap';

// 定义组件属性
const props = defineProps({
    // 功能标题
    title: {
        type: String,
        required: true
    },
    // 功能描述
    description: {
        type: String,
        required: true
    },
    // 图标类型
    icon: {
        type: String,
        default: 'lightning',
        validator: (value: string) => ['lightning', 'shield', 'document', 'globe', 'custom'].includes(value)
    },
    // 颜色方案
    colorScheme: {
        type: String,
        default: 'indigo',
        validator: (value: string) => ['indigo', 'blue', 'purple', 'green', 'amber'].includes(value)
    },
    // 动画延迟
    delay: {
        type: Number,
        default: 0
    }
});

const cardRef = ref<HTMLElement | null>(null);
const iconRef = ref<HTMLElement | null>(null);

// 根据颜色方案计算样式类
const getGradientClass = computed(() => {
    const gradients = {
        indigo: 'bg-gradient-to-br from-indigo-500 via-purple-500 to-blue-500',
        blue: 'bg-gradient-to-br from-blue-500 via-cyan-500 to-teal-500',
        purple: 'bg-gradient-to-br from-purple-500 via-indigo-500 to-blue-500',
        green: 'bg-gradient-to-br from-green-500 via-teal-500 to-cyan-500',
        amber: 'bg-gradient-to-br from-amber-500 via-yellow-500 to-orange-500'
    };
    return gradients[props.colorScheme as keyof typeof gradients] || gradients.indigo;
});

const getHoverBgClass = computed(() => {
    const bgClasses = {
        indigo: 'group-hover:from-white group-hover:to-indigo-50',
        blue: 'group-hover:from-white group-hover:to-blue-50',
        purple: 'group-hover:from-white group-hover:to-purple-50',
        green: 'group-hover:from-white group-hover:to-green-50',
        amber: 'group-hover:from-white group-hover:to-amber-50'
    };
    return bgClasses[props.colorScheme as keyof typeof bgClasses] || bgClasses.indigo;
});

const getIconBgClass = computed(() => {
    const bgClasses = {
        indigo: 'bg-indigo-100',
        blue: 'bg-blue-100',
        purple: 'bg-purple-100',
        green: 'bg-green-100',
        amber: 'bg-amber-100'
    };
    return bgClasses[props.colorScheme as keyof typeof bgClasses] || bgClasses.indigo;
});

const getIconTextClass = computed(() => {
    const textClasses = {
        indigo: 'text-indigo-600',
        blue: 'text-blue-600',
        purple: 'text-purple-600',
        green: 'text-green-600',
        amber: 'text-amber-600'
    };
    return textClasses[props.colorScheme as keyof typeof textClasses] || textClasses.indigo;
});



// 创建悬停动画
onMounted(() => {
    if (cardRef.value && iconRef.value) {
        // 创建一个上下文，用于存储不同的动画时间线
        const ctx = gsap.context(() => {
            // 卡片悬停动画
            cardRef.value?.addEventListener('mouseenter', () => {
                gsap.to(cardRef.value, {
                    y: -5,
                    duration: 0.3,
                    ease: 'power2.out',
                    boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1)'
                });

                // 图标悬停动画
                gsap.to(iconRef.value, {
                    scale: 1.05,
                    duration: 0.3,
                    ease: 'power2.out',
                    backgroundColor: getIconHoverColor(),
                    color: 'white'
                });
            });

            // 卡片离开动画
            cardRef.value?.addEventListener('mouseleave', () => {
                gsap.to(cardRef.value, {
                    y: 0,
                    duration: 0.3,
                    ease: 'power2.out',
                    boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)'
                });

                // 图标恢复动画
                gsap.to(iconRef.value, {
                    scale: 1,
                    duration: 0.3,
                    ease: 'power2.out',
                    backgroundColor: getIconNormalColor(),
                    color: getIconTextColor()
                });
            });
        }, cardRef); // 将上下文绑定到卡片元素

        // 当组件卸载时清理上下文
        return () => ctx.revert();
    }
});

// 获取图标悬停时的背景颜色
const getIconHoverColor = () => {
    const colors: Record<string, string> = {
        indigo: '#4f46e5', // indigo-600
        blue: '#2563eb',   // blue-600
        purple: '#9333ea', // purple-600
        green: '#16a34a',  // green-600
        amber: '#d97706'   // amber-600
    };
    return colors[props.colorScheme] || colors.indigo;
};

// 获取图标正常状态的背景颜色
const getIconNormalColor = () => {
    const colors: Record<string, string> = {
        indigo: '#eef2ff', // indigo-50
        blue: '#eff6ff',   // blue-50
        purple: '#f5f3ff', // purple-50
        green: '#f0fdf4',  // green-50
        amber: '#fffbeb'   // amber-50
    };
    return colors[props.colorScheme] || colors.indigo;
};

// 获取图标文字颜色
const getIconTextColor = () => {
    const colors: Record<string, string> = {
        indigo: '#4f46e5', // indigo-600
        blue: '#2563eb',   // blue-600
        purple: '#9333ea', // purple-600
        green: '#16a34a',  // green-600
        amber: '#d97706'   // amber-600
    };
    return colors[props.colorScheme] || colors.indigo;
};

const getBorderHoverClass = computed(() => {
    const borderClasses = {
        indigo: 'hover:border-indigo-200',
        blue: 'hover:border-blue-200',
        purple: 'hover:border-purple-200',
        green: 'hover:border-green-200',
        amber: 'hover:border-amber-200'
    };
    return borderClasses[props.colorScheme as keyof typeof borderClasses] || borderClasses.indigo;
});
</script>