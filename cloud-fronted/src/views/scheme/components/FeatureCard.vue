<template>
    <div class="group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1"
        :class="getBorderHoverClass">
        <!-- 渐变背景 -->
        <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
            :class="getGradientClass"></div>

        <div class="relative flex rounded-xl bg-white p-4 md:p-5 transition-all duration-500 group-hover:bg-gradient-to-br"
            :class="getHoverBgClass">
            <!-- 图标容器 -->
            <div class="feature-icon relative mr-3 md:mr-5 flex h-10 md:h-14 w-10 md:w-14 flex-shrink-0 items-center justify-center rounded-xl transition-all duration-500 group-hover:scale-110"
                :class="[
                    getIconBgClass,
                    getIconTextClass,
                    'group-hover:' + getIconHoverBgClass,
                    'group-hover:text-white'
                ]">
                <!-- 默认图标 -->
                <slot name="icon">
                    <svg v-if="icon === 'lightning'" xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6"
                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M13 10V3L4 14h7v7l9-11h-7z" />
                    </svg>
                    <svg v-else-if="icon === 'shield'" xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6"
                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                    </svg>
                    <svg v-else-if="icon === 'document'" xmlns="http://www.w3.org/2000/svg"
                        class="h-5 md:h-6 w-5 md:w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                    </svg>
                    <svg v-else-if="icon === 'globe'" xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6"
                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c-1.657 0-3-4.03-3-9s1.343-9 3-9m0 18c1.657 0 3-4.03 3-9s-1.343-9-3-9" />
                    </svg>
                </slot>
            </div>

            <!-- 内容区域 -->
            <div class="flex-1">
                <h4 class="text-base md:text-lg font-semibold text-gray-900 transition-all duration-500"
                    :class="getTextHoverClass">
                    {{ title }}
                </h4>
                <p
                    class="mt-1 md:mt-2 text-xs md:text-sm text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                    {{ description }}
                </p>
                <slot name="content"></slot>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

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
    }
});

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

const getIconHoverBgClass = computed(() => {
    const hoverClasses = {
        indigo: 'bg-indigo-600',
        blue: 'bg-blue-600',
        purple: 'bg-purple-600',
        green: 'bg-green-600',
        amber: 'bg-amber-600'
    };
    return hoverClasses[props.colorScheme as keyof typeof hoverClasses] || hoverClasses.indigo;
});

const getTextHoverClass = computed(() => {
    const textClasses = {
        indigo: 'group-hover:text-indigo-700',
        blue: 'group-hover:text-blue-700',
        purple: 'group-hover:text-purple-700',
        green: 'group-hover:text-green-700',
        amber: 'group-hover:text-amber-700'
    };
    return textClasses[props.colorScheme as keyof typeof textClasses] || textClasses.indigo;
});

const getBorderHoverClass = computed(() => {
    const borderClasses = {
        indigo: 'hover:border-indigo-200',
        blue: 'hover:border-blue-200',
        purple: 'hover:border-purple-200',
        green: 'hover:border-green-200',
        amber: 'hover:border-amber-200'
    };
    return `${borderClasses[props.colorScheme as keyof typeof borderClasses] || borderClasses.indigo} hover:shadow-xl`;
});
</script>