<template>
    <div class="grid grid-cols-1 gap-6 sm:grid-cols-3">
        <div class="bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-blue-100 text-sm font-medium">总访问量</p>
                    <p class="text-3xl font-bold mt-1">{{ totalPV.toLocaleString() }}</p>
                    <p class="text-blue-200 text-xs mt-1">PV (Page Views)</p>
                </div>
                <div class="bg-white bg-opacity-20 p-3 rounded-xl backdrop-blur-sm">
                    <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </div>
            </div>
        </div>

        <div class="bg-gradient-to-br from-emerald-500 to-emerald-600 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-emerald-100 text-sm font-medium">来源网站数</p>
                    <p class="text-3xl font-bold mt-1">{{ sourceCount }}</p>
                    <p class="text-emerald-200 text-xs mt-1">Source Websites</p>
                </div>
                <div class="bg-white bg-opacity-20 p-3 rounded-xl backdrop-blur-sm">
                    <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>
                    </svg>
                </div>
            </div>
        </div>

        <div class="bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl p-6 text-white shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="flex items-center justify-between">
                <div>
                    <p class="text-purple-100 text-sm font-medium">活跃来源数</p>
                    <p class="text-3xl font-bold mt-1">{{ activeSourcesCount }}</p>
                    <p class="text-purple-200 text-xs mt-1">Active Sources (≥5 PV)</p>
                </div>
                <div class="bg-white bg-opacity-20 p-3 rounded-xl backdrop-blur-sm">
                    <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"/>
                    </svg>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
    data: Array<{
        referer: string | null;
        pvCount: number;
    }>;
}

const props = defineProps<Props>();

// 总访问量
const totalPV = computed(() => {
    return props.data.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

// 来源网站数
const sourceCount = computed(() => {
    return props.data.length;
});

// 活跃来源数（访问量>=5的来源）
const activeSourcesCount = computed(() => {
    return props.data.filter(item => (item.pvCount || 0) >= 5).length;
});
</script>
