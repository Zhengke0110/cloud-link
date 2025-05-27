<template>
    <div class="bg-white rounded-xl shadow-md border border-gray-100 p-6 transition-all duration-300 hover:shadow-lg">
        <div class="flex items-center justify-between mb-6">
            <div>
                <h4 class="text-lg font-bold text-gray-900 flex items-center">
                    <svg class="w-5 h-5 text-orange-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    浏览器详情
                </h4>
                <p class="text-sm text-gray-500 mt-1">各浏览器访问量排行</p>
            </div>
            <div class="flex items-center space-x-2 text-sm text-gray-500 bg-orange-50 px-3 py-1.5 rounded-lg">
                <svg class="w-4 h-4 text-orange-600" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" />
                </svg>
                <span class="text-orange-700 font-medium">列表</span>
            </div>
        </div>
        <div class="space-y-3">
            <div v-for="(item, index) in sortedData" :key="index"
                class="flex items-center justify-between p-4 rounded-lg bg-gradient-to-r from-orange-50 to-orange-50/50 hover:from-orange-100 hover:to-orange-100/50 transition-all duration-300 border border-orange-100 hover:border-orange-200">
                <div class="flex items-center space-x-3">
                    <div class="w-3 h-3 rounded-full shadow-sm" :style="{ backgroundColor: getBrowserColor(item.browserName) }"></div>
                    <span class="font-medium text-gray-900">{{ formatBrowserName(item.browserName) }}</span>
                </div>
                <div class="text-right">
                    <div class="text-sm font-bold text-orange-700">{{ item.pvCount.toLocaleString() }}</div>
                    <div class="text-xs text-orange-600">{{ ((item.pvCount / totalPV) * 100).toFixed(1) }}%</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface BrowserDataItem {
    browserName: string | null;
    pvCount: number;
}

interface Props {
    data: BrowserDataItem[];
}

const props = defineProps<Props>();

// 排序后的数据
const sortedData = computed(() => {
    return [...props.data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

// 总访问量
const totalPV = computed(() => {
    return props.data.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

// 格式化名称
function formatBrowserName(browserName: string | null): string {
    if (!browserName || browserName === 'Unknown') return '未知浏览器';
    return browserName;
}

// 获取颜色
function getBrowserColor(browserName: string | null): string {
    const colors = {
        'Chrome': '#4285f4',
        'Safari': '#ff6b00',
        'Firefox': '#ff7139',
        'Edge': '#0078d7',
        'Unknown': '#6b7280'
    };
    return colors[browserName as keyof typeof colors] || '#3b82f6';
}
</script>
