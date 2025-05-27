<template>
    <div class="bg-white rounded-xl shadow-md border border-gray-100 p-6 transition-all duration-300 hover:shadow-lg">
        <div class="flex items-center justify-between mb-6">
            <div>
                <h4 class="text-lg font-bold text-gray-900 flex items-center">
                    <svg class="w-5 h-5 text-blue-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    操作系统详情
                </h4>
                <p class="text-sm text-gray-500 mt-1">各系统访问量排行</p>
            </div>
            <div class="flex items-center space-x-2 text-sm text-gray-500 bg-blue-50 px-3 py-1.5 rounded-lg">
                <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" />
                </svg>
                <span class="text-blue-700 font-medium">列表</span>
            </div>
        </div>
        <div class="space-y-3">
            <div v-for="(item, index) in sortedData" :key="index"
                class="flex items-center justify-between p-4 rounded-lg bg-gradient-to-r from-blue-50 to-blue-50/50 hover:from-blue-100 hover:to-blue-100/50 transition-all duration-300 border border-blue-100 hover:border-blue-200">
                <div class="flex items-center space-x-3">
                    <div class="w-3 h-3 rounded-full shadow-sm" :style="{ backgroundColor: getOsColor(item.os) }"></div>
                    <span class="font-medium text-gray-900">{{ formatOsName(item.os) }}</span>
                </div>
                <div class="text-right">
                    <div class="text-sm font-bold text-blue-700">{{ item.pvCount.toLocaleString() }}</div>
                    <div class="text-xs text-blue-600">{{ ((item.pvCount / totalPV) * 100).toFixed(1) }}%</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface OsDataItem {
    os: string | null;
    pvCount: number;
}

interface Props {
    data: OsDataItem[];
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
function formatOsName(os: string | null): string {
    if (!os || os === 'Unknown') return '未知系统';
    return os;
}

// 获取颜色
function getOsColor(os: string | null): string {
    const colors = {
        'Windows': '#0078d4',
        'Mac OS X': '#000000',
        'Unknown': '#6b7280'
    };
    return colors[os as keyof typeof colors] || '#3b82f6';
}
</script>
