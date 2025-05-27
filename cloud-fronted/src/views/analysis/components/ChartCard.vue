<template>
    <div class="rounded-xl bg-white p-6 shadow-md transition-shadow duration-300 hover:shadow-lg">
        <div class="mb-4 flex items-center gap-3">
            <div :class="iconBgClass" class="rounded-lg p-2">
                <slot name="icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" :class="iconClass" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="iconPath" />
                    </svg>
                </slot>
            </div>
            <h3 class="text-lg font-semibold text-gray-800">{{ title }}</h3>
        </div>
        <div :id="chartId" :class="chartClass"></div>
    </div>
</template>

<script setup lang="ts">
interface Props {
    title: string;
    chartId: string;
    chartClass?: string;
    iconClass?: string;
    iconBgClass?: string;
    iconPath?: string;
}

withDefaults(defineProps<Props>(), {
    chartClass: 'h-80 w-full',
    iconClass: 'text-blue-600',
    iconBgClass: 'bg-blue-100',
    iconPath: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z'
});
</script>
