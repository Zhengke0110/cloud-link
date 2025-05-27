<template>
    <div class="bg-white rounded-xl shadow-md border border-gray-100 p-6 transition-all duration-300 hover:shadow-lg">
        <div class="flex items-center justify-between mb-6">
            <div>
                <h4 class="text-lg font-bold text-gray-900 flex items-center">
                    <svg class="w-5 h-5 text-emerald-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    设备类型详情
                </h4>
                <p class="text-sm text-gray-500 mt-1">各设备访问量排行</p>
            </div>
            <div class="flex items-center space-x-2 text-sm text-gray-500 bg-emerald-50 px-3 py-1.5 rounded-lg">
                <svg class="w-4 h-4 text-emerald-600" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" />
                </svg>
                <span class="text-emerald-700 font-medium">列表</span>
            </div>
        </div>
        <div class="space-y-3">
            <div v-for="(item, index) in sortedData" :key="index"
                class="flex items-center justify-between p-4 rounded-lg bg-gradient-to-r from-emerald-50 to-emerald-50/50 hover:from-emerald-100 hover:to-emerald-100/50 transition-all duration-300 border border-emerald-100 hover:border-emerald-200">
                <div class="flex items-center space-x-3">
                    <div class="w-3 h-3 rounded-full shadow-sm" :style="{ backgroundColor: getDeviceColor(item.deviceType) }"></div>
                    <span class="font-medium text-gray-900">{{ formatDeviceName(item.deviceType) }}</span>
                </div>
                <div class="text-right">
                    <div class="text-sm font-bold text-emerald-700">{{ item.pvCount.toLocaleString() }}</div>
                    <div class="text-xs text-emerald-600">{{ ((item.pvCount / totalPV) * 100).toFixed(1) }}%</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface DeviceDataItem {
    deviceType: string | null;
    pvCount: number;
}

interface Props {
    data: DeviceDataItem[];
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
function formatDeviceName(deviceType: string | null): string {
    if (!deviceType || deviceType === 'UNKNOWN') return '未知设备';
    if (deviceType === 'COMPUTER') return '电脑';
    if (deviceType === 'MOBILE') return '手机';
    if (deviceType === 'TABLET') return '平板';
    return deviceType;
}

// 获取颜色
function getDeviceColor(deviceType: string | null): string {
    const colors = {
        'COMPUTER': '#10b981',
        'MOBILE': '#f59e0b',
        'TABLET': '#8b5cf6',
        'UNKNOWN': '#6b7280'
    };
    return colors[deviceType as keyof typeof colors] || '#3b82f6';
}
</script>
