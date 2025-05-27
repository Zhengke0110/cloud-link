<template>
    <PageLayout tag="数据分析" title="访问趋势分析" description="查看您的短链接访问趋势，了解流量变化规律">
        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="isLoading" @refresh="throttledHandleRefresh"
                @date-change="debouncedHandleDateChange" ref="dateRangeSelectorRef" />
        </div>
        <!-- 加载状态 -->
        <div v-if="isLoading" class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
            <span class="ml-2 text-gray-600">数据加载中...</span>
        </div>

        <!-- 数据加载完成后显示内容 -->
        <template v-else-if="!error && state.length > 0">
            <!-- 趋势图表 -->
            <div class="mx-auto mb-8 max-w-6xl">
                <TrendChart :data="state" :loading="isLoading" />
            </div>

            <!-- 数据指标卡片 -->
            <div class="mx-auto max-w-6xl">
                <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
                    <StatCard label="总页面访问量" :value="totalStats.pv" description="PV (Page Views)" type="blue"
                        :loading="isLoading" />

                    <StatCard label="独立访客数" :value="totalStats.uv" description="UV (Unique Visitors)" type="emerald"
                        :loading="isLoading" />

                    <StatCard label="独立IP数" :value="totalStats.ip" description="IP Addresses" type="purple"
                        :loading="isLoading" />

                    <StatCard label="新访客数" :value="totalStats.newUV" description="New Visitors" type="orange"
                        :loading="isLoading" />
                </div>
            </div>
        </template>

        <!-- 数据加载失败或无数据提示 -->
        <div v-else class="mx-auto max-w-5xl">
            <EmptyState title="暂无趋势数据" description="当前没有找到访问趋势数据，请检查是否有访问记录或稍后重试" icon-type="blue">
                <!-- ...existing code... -->
                <template #action>
                    <button @click="throttledRetryInit" :disabled="isLoading"
                        class="inline-flex items-center px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-200 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                        <svg v-if="isLoading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none"
                            viewBox="0 0 24 24">
                            <!-- ...existing code... -->
                        </svg>
                        <svg v-else class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <!-- ...existing code... -->
                        </svg>
                        {{ isLoading ? '重新加载中...' : '重新加载' }}
                    </button>
                </template>
            </EmptyState>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';
import { useAsyncState, useDebounceFn, useThrottleFn, useLocalStorage } from '@vueuse/core';
import PageLayout from '@/components/PageLayout.vue';
import TrendChart from './components/trend/TrendChart.vue';
import StatCard from './components/trend/StatCard.vue';
import DateRangeSelector from './components/DateRangeSelector.vue';
import EmptyState from '@/components/EmptyState.vue';
import { DataTrendApi } from "@/services/data";

// 使用 useLocalStorage 缓存用户偏好
const cachedDateRange = useLocalStorage('trend-date-range', {
    startTime: '',
    endTime: ''
});

const dateRangeSelectorRef = ref();
const isInitialized = ref<boolean>(false);

// 获取趋势数据的API接口 - 性能优化版本
const fetchTrendData = async (params: {
    code: string
    type: string
    startTime: string
    endTime: string
}) => {
    try {
        console.log('正在获取趋势数据:', params);
        const response = await DataTrendApi(params);

        // 缓存成功的日期范围
        cachedDateRange.value = {
            startTime: params.startTime,
            endTime: params.endTime
        };

        return response || [];
    } catch (error) {
        console.error('获取趋势数据失败:', error);
        throw error;
    }
};

// 使用 useAsyncState 管理异步状态
const {
    state,
    isLoading,
    error,
    execute: executeFetch
} = useAsyncState(
    (params: any) => fetchTrendData(params),
    [],
    {
        immediate: false,
        resetOnExecute: false,
        onError: (err) => {
            console.error('异步状态错误:', err);
        }
    }
);

// 定义趋势数据项的接口
interface TrendDataItem {
    pvCount?: number;
    uvCount?: number;
    ipCount?: number;
    newUVCount?: number;
}

// 计算总统计数据 - 使用缓存优化
const totalStats = computed(() => {
    if (!state.value || state.value.length === 0) {
        return { pv: 0, uv: 0, ip: 0, newUV: 0 };
    }

    return state.value.reduce((acc: { pv: number, uv: number, ip: number, newUV: number }, item: TrendDataItem) => ({
        pv: acc.pv + (item.pvCount || 0),
        uv: acc.uv + (item.uvCount || 0),
        ip: acc.ip + (item.ipCount || 0),
        newUV: acc.newUV + (item.newUVCount || 0)
    }), { pv: 0, uv: 0, ip: 0, newUV: 0 });
});

// 防抖处理日期变化 - 500ms延迟
const debouncedHandleDateChange = useDebounceFn((dateRange: { startTime: string, endTime: string }) => {
    cachedDateRange.value = dateRange;
}, 500);

// 节流处理刷新操作 - 1秒内最多执行一次
const throttledHandleRefresh = useThrottleFn(async (dateRange: { startTime: string, endTime: string }) => {
    const params = {
        code: "04jw1SM0", // TODO: 替换为实际的项目code参数
        type: "Day",
        startTime: dateRange.startTime,
        endTime: dateRange.endTime
    };

    await executeFetch(0, params);
}, 1000);

// 初始化数据
const initData = async () => {
    if (isInitialized.value) return;

    if (dateRangeSelectorRef.value) {
        isInitialized.value = true;
        const dateRange = dateRangeSelectorRef.value.getDateRange();

        // 如果有缓存的日期范围，优先使用
        const finalDateRange = cachedDateRange.value.startTime && cachedDateRange.value.endTime
            ? cachedDateRange.value
            : dateRange;

        await throttledHandleRefresh(finalDateRange);
    }
};

// 节流处理重试操作
const throttledRetryInit = useThrottleFn(async () => {
    isInitialized.value = false;
    await initData();
}, 2000);

onMounted(async () => {
    await nextTick();
    await initData();
});
</script>

<style scoped></style>