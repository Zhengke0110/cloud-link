<template>
    <div
        class="group rounded-xl border p-6 shadow-md transition-all duration-300 hover:shadow-lg hover:scale-[1.02]"
        :class="cardClasses">
        <div class="flex items-center">
            <div class="flex-shrink-0">
                <div class="flex h-12 w-12 items-center justify-center rounded-xl shadow-lg" :class="iconBgClass">
                    <svg class="h-6 w-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="iconPath" />
                    </svg>
                </div>
            </div>
            <div class="ml-4 flex-1">
                <p class="mb-1 text-sm font-medium" :class="labelClass">{{ label }}</p>
                <p class="text-3xl font-bold" :class="valueClass">{{ formattedValue }}</p>
                <p class="mt-1 text-xs" :class="descClass">{{ description }}</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
    label: string;
    value: number;
    description: string;
    type: 'blue' | 'emerald' | 'purple' | 'orange';
}

const props = defineProps<Props>();

// 格式化数字显示
const formattedValue = computed(() => {
    if (props.value >= 1000000) {
        return (props.value / 1000000).toFixed(1) + 'M';
    } else if (props.value >= 1000) {
        return (props.value / 1000).toFixed(1) + 'K';
    }
    return props.value.toString();
});

// 根据类型定义样式映射
const styleMap = {
    blue: {
        cardClasses: 'bg-gradient-to-br from-blue-50 to-blue-100 border-blue-200',
        iconBgClass: 'bg-blue-500',
        labelClass: 'text-blue-700',
        valueClass: 'text-blue-900',
        descClass: 'text-blue-600',
        iconPath: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z'
    },
    emerald: {
        cardClasses: 'bg-gradient-to-br from-emerald-50 to-emerald-100 border-emerald-200',
        iconBgClass: 'bg-emerald-500',
        labelClass: 'text-emerald-700',
        valueClass: 'text-emerald-900',
        descClass: 'text-emerald-600',
        iconPath: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z'
    },
    purple: {
        cardClasses: 'bg-gradient-to-br from-purple-50 to-purple-100 border-purple-200',
        iconBgClass: 'bg-purple-500',
        labelClass: 'text-purple-700',
        valueClass: 'text-purple-900',
        descClass: 'text-purple-600',
        iconPath: 'M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z'
    },
    orange: {
        cardClasses: 'bg-gradient-to-br from-orange-50 to-orange-100 border-orange-200',
        iconBgClass: 'bg-orange-500',
        labelClass: 'text-orange-700',
        valueClass: 'text-orange-900',
        descClass: 'text-orange-600',
        iconPath: 'M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z'
    }
};

const currentStyle = computed(() => styleMap[props.type]);
const cardClasses = computed(() => currentStyle.value.cardClasses);
const iconBgClass = computed(() => currentStyle.value.iconBgClass);
const labelClass = computed(() => currentStyle.value.labelClass);
const valueClass = computed(() => currentStyle.value.valueClass);
const descClass = computed(() => currentStyle.value.descClass);
const iconPath = computed(() => currentStyle.value.iconPath);
</script>
