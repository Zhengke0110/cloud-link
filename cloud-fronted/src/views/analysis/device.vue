<template>
    <!-- 日期选择器 -->
    <div class="mx-auto mb-8 max-w-6xl">
        <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
            ref="dateRangeSelectorRef" />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center py-20">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
        <span class="ml-2 text-gray-600">数据加载中...</span>
    </div>

    <!-- 有数据时显示图表 -->
    <template v-else-if="deviceData && hasData">
        <!-- 图表容器 -->
        <div class="mx-auto max-w-6xl">
            <div :class="chartGridClasses" class="mb-8">
                <!-- 操作系统分布图表 -->
                <OsChart :data="memoizedOsData" :loading="loading" />

                <!-- 设备类型分布图表 -->
                <DeviceChart :data="memoizedDeviceData" :loading="loading" />

                <!-- 浏览器分布图表 -->
                <BrowserChart :data="memoizedBrowserData" :loading="loading" />
            </div>

            <!-- 详细统计表格 -->
            <div :class="detailGridClasses">
                <!-- 操作系统详情列表 -->
                <OsDetailList :data="memoizedOsData" />

                <!-- 设备类型详情列表 -->
                <DeviceDetailList :data="memoizedDeviceData" />

                <!-- 浏览器详情列表 -->
                <BrowserDetailList :data="memoizedBrowserData" />
            </div>
        </div>
    </template>

    <!-- 数据加载失败或无数据提示 -->
    <div v-else class="mx-auto max-w-6xl">
        <EmptyState title="暂无设备数据" description="当前没有找到设备访问数据，请检查是否有访问记录或稍后重试" icon-type="blue">
            <template #icon>
                <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                        d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
            </template>
            <template #action>
                <button @click="handleRefreshData" :disabled="loading"
                    class="inline-flex items-center px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-200 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                    <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none"
                        viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                        </circle>
                        <path class="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
                    <svg v-else class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                    </svg>
                    {{ loading ? '重新加载中...' : '重新加载' }}
                </button>
            </template>
        </EmptyState>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useDebounceFn, useWindowSize, useMemoize } from '@vueuse/core';
import DateRangeSelector from './components/DateRangeSelector.vue';
import OsChart from './components/device/OsChart.vue';
import DeviceChart from './components/device/DeviceChart.vue';
import BrowserChart from './components/device/BrowserChart.vue';
import OsDetailList from './components/device/OsDetailList.vue';
import DeviceDetailList from './components/device/DeviceDetailList.vue';
import BrowserDetailList from './components/device/BrowserDetailList.vue';
import EmptyState from '@/components/EmptyState.vue';
import { DataDeviceApi } from "@/services/data"

// 定义 Props
interface Props {
    code: string
}

const props = defineProps<Props>()

// 添加加载状态和数据管理
const loading = ref<boolean>(false);
const deviceData = ref();
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
});
const dateRangeSelectorRef = ref();

// 窗口尺寸响应式
const { width } = useWindowSize();

// 响应式布局计算
const isSmallScreen = computed(() => width.value < 768);
const isMediumScreen = computed(() => width.value >= 768 && width.value < 1024);

// 图表网格样式 - 响应式调整
const chartGridClasses = computed(() => {
    if (isSmallScreen.value) {
        return 'grid grid-cols-1 gap-4';
    } else if (isMediumScreen.value) {
        return 'grid grid-cols-1 gap-6 md:grid-cols-2';
    } else {
        return 'grid grid-cols-1 gap-8 lg:grid-cols-3';
    }
});

// 详情列表网格样式 - 响应式调整
const detailGridClasses = computed(() => {
    if (isSmallScreen.value) {
        return 'grid grid-cols-1 gap-4';
    } else if (isMediumScreen.value) {
        return 'grid grid-cols-1 gap-6 md:grid-cols-2';
    } else {
        return 'grid grid-cols-1 gap-8 lg:grid-cols-3';
    }
});

// 检查是否有数据
const hasData = computed(() => {
    if (!deviceData.value) return false;
    const { os, device, browser } = deviceData.value;
    return (os && os.length > 0) || (device && device.length > 0) || (browser && browser.length > 0);
});

// 创建缓存的数据处理函数
const memoizedSortData = useMemoize((data: any[]) => {
    if (!data || !Array.isArray(data)) return [];
    return [...data].sort((a, b) => (b.count || b.pvCount || 0) - (a.count || a.pvCount || 0));
});

const memoizedFilterData = useMemoize((data: any[], minCount: number = 0) => {
    if (!data || !Array.isArray(data)) return [];
    return data.filter(item => (item.count || item.pvCount || 0) >= minCount);
});

// 处理和缓存各类设备数据
const memoizedOsData = computed(() => {
    if (!deviceData.value?.os) return [];
    const sortedData = memoizedSortData(deviceData.value.os);
    return memoizedFilterData(sortedData, 0);
});

const memoizedDeviceData = computed(() => {
    if (!deviceData.value?.device) return [];
    const sortedData = memoizedSortData(deviceData.value.device);
    return memoizedFilterData(sortedData, 0);
});

const memoizedBrowserData = computed(() => {
    if (!deviceData.value?.browser) return [];
    const sortedData = memoizedSortData(deviceData.value.browser);
    return memoizedFilterData(sortedData, 0);
});

// 添加数据过滤和API相关函数
const fetchDeviceData = async (params: {
    code: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true;
        const response = await DataDeviceApi(params);
        return response;

    } catch (error) {
        console.error('获取设备数据失败:', error);
        // TODO: 添加错误处理和用户提示
        return null;
    } finally {
        loading.value = false;
    }
};

// 防抖处理日期变化
const debouncedDateChange = useDebounceFn((dateRange: { startTime: string, endTime: string }) => {
    console.log('日期范围变化:', dateRange);
    currentDateRange.value = dateRange;
}, 300);

// 防抖处理刷新数据
const debouncedRefresh = useDebounceFn(async (dateRange: { startTime: string, endTime: string }) => {
    currentDateRange.value = dateRange;

    try {
        const params = {
            code: props.code,
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        };

        const data = await fetchDeviceData(params);
        deviceData.value = data;
    } catch (error) {
        console.error('刷新图表失败:', error);
        // TODO: 添加错误提示给用户
    }
}, 500);

// 处理日期变化
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    debouncedDateChange(dateRange);
};

// 刷新图表数据
const handleRefresh = async (dateRange: { startTime: string, endTime: string }) => {
    debouncedRefresh(dateRange);
};

// 处理重新加载数据（不需要防抖，因为是用户主动点击）
const handleRefreshData = async () => {
    if (dateRangeSelectorRef.value) {
        const dateRange = dateRangeSelectorRef.value.getDateRange();
        // 直接调用原始刷新函数，不经过防抖
        currentDateRange.value = dateRange;

        try {
            const params = {
                code: props.code,
                startTime: dateRange.startTime,
                endTime: dateRange.endTime
            };

            const data = await fetchDeviceData(params);
            deviceData.value = data;
        } catch (error) {
            console.error('刷新图表失败:', error);
            // TODO: 添加错误提示给用户
        }
    }
};

// 初始化数据
const initData = async () => {
    if (dateRangeSelectorRef.value) {
        const dateRange = dateRangeSelectorRef.value.getDateRange();
        await handleRefresh(dateRange);
    }
};

onMounted(() => {
    // 延迟初始化，确保子组件已挂载
    setTimeout(() => {
        initData();
    }, 100);
});
</script>

<style scoped></style>