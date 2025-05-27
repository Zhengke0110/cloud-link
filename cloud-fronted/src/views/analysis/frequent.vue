<template>
    <PageLayout tag="数据分析" title="访问来源分析" description="分析您的短链接访问来源分布，了解流量入口">

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

        <!-- 有数据时显示图表和统计 -->
        <template v-else-if="sourceData.length > 0">
            <!-- 图表容器 -->
            <div class="mx-auto mb-8 max-w-6xl">
                <div :class="chartGridClasses">
                    <!-- 柱状图 -->
                    <div class="relative">
                        <SourceBarChart :data="sortedData" :loading="loading" />
                    </div>

                    <!-- 饼图 -->
                    <div class="relative">
                        <SourcePieChart :data="sortedData" :loading="loading" />
                    </div>
                </div>
            </div>

            <!-- 统计概览卡片 -->
            <div class="mx-auto mb-8 max-w-6xl">
                <SourceStatsCards :data="memoizedFilteredData" />
            </div>

            <!-- 数据统计表格 -->
            <div class="mx-auto max-w-6xl">
                <SourceDataTable :data="memoizedFilteredData" :loading="loading" />
            </div>
        </template>

        <!-- 数据加载失败或无数据提示 -->
        <div v-else class="mx-auto max-w-6xl">
            <EmptyState title="暂无访问来源数据" description="当前没有找到访问来源数据，请检查是否有访问记录或稍后重试" icon-type="blue">
                <template #icon>
                    <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                            d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
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
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useDebounceFn, useWindowSize, useMemoize } from '@vueuse/core';
import PageLayout from '@/components/PageLayout.vue';
import DateRangeSelector from './components/DateRangeSelector.vue';
import SourceBarChart from './components/frequent/SourceBarChart.vue';
import SourcePieChart from './components/frequent/SourcePieChart.vue';
import SourceStatsCards from './components/frequent/SourceStatsCards.vue';
import SourceDataTable from './components/frequent/SourceDataTable.vue';
import EmptyState from '@/components/EmptyState.vue';
import { DataFrequentApi } from '@/services/data'
const dateRangeSelectorRef = ref();

// 窗口尺寸响应式
const { width } = useWindowSize();

// 响应式布局计算
const isSmallScreen = computed(() => width.value < 1024);
const isMediumScreen = computed(() => width.value >= 1024 && width.value < 1440);

// 图表网格样式
const chartGridClasses = computed(() => {
    if (isSmallScreen.value) {
        return 'grid grid-cols-1 gap-4';
    } else if (isMediumScreen.value) {
        return 'grid grid-cols-1 gap-6 lg:grid-cols-2';
    } else {
        return 'grid grid-cols-2 gap-8';
    }
});

// 数据加载状态
const loading = ref<boolean>(false);
const sourceData = ref<any[]>([]);
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
});

// 获取来源数据的API接口
const fetchSourceData = async (params: {
    code: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true;
        const response = await DataFrequentApi(params);
        return response
    } catch (error) {
        console.error('获取来源数据失败:', error);
        return [];
    } finally {
        loading.value = false;
    }
};

// 创建缓存的数据处理函数
const memoizedFilterData = useMemoize((data: any[], startTime: string, endTime: string) => {
    if (!startTime || !endTime) {
        return data;
    }
    // 这里可以添加更复杂的过滤逻辑
    return data;
});

const memoizedSortData = useMemoize((data: any[]) => {
    return [...data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

// 根据日期范围过滤数据（使用缓存）
const memoizedFilteredData = computed(() => {
    return memoizedFilterData(
        sourceData.value,
        currentDateRange.value.startTime,
        currentDateRange.value.endTime
    );
});

// 排序后的数据（使用缓存）
const sortedData = computed(() => {
    return memoizedSortData(memoizedFilteredData.value);
});


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
            // TODO: [访问来源] 动态获取项目code，替换硬编码值
            code: "04jw1SM0",
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        };

        const data = await fetchSourceData(params);
        sourceData.value = data;
    } catch (error) {
        console.error('刷新数据失败:', error);
        // TODO: 添加错误提示给用户
    }
}, 500);

// 处理日期变化
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    debouncedDateChange(dateRange);
};

// 刷新数据
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
                // TODO: [访问来源] 动态获取项目code，替换硬编码值
                code: "04jw1SM0",
                startTime: dateRange.startTime,
                endTime: dateRange.endTime
            };

            const data = await fetchSourceData(params);
            sourceData.value = data;
        } catch (error) {
            console.error('刷新数据失败:', error);
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

onMounted(async () => {
    // 延迟初始化，确保子组件已挂载
    setTimeout(initData, 100);
});
</script>

<style scoped></style>