<template>
    <PageLayout tag="数据分析" title="访问数据分析" description="深入了解您的短链接访问情况，包括用户设备、地域分布、访问时间等详细数据">

        <!-- 加载状态 -->
        <div v-if="loading" class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
            <span class="ml-2 text-gray-600">数据加载中...</span>
        </div>

        <!-- 数据加载完成后显示图表 -->
        <template v-else-if="Data">
            <!-- 图表容器 - 第一行：设备类型和操作系统 -->
            <div ref="chartsContainer" class="grid grid-cols-1 gap-6 lg:grid-cols-2">
                <div ref="deviceChartContainer">
                    <DeviceChart v-if="deviceChartVisible" ref="deviceChartRef" :data="Data.data" />
                </div>
                <div ref="osChartContainer">
                    <OSChart v-if="osChartVisible" ref="osChartRef" :data="Data.data" />
                </div>
            </div>

            <!-- 图表容器 - 第二行：浏览器和访问来源 -->
            <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
                <div ref="browserChartContainer">
                    <BrowserChart v-if="browserChartVisible" ref="browserChartRef" :data="Data.data" />
                </div>
                <div ref="refererChartContainer">
                    <RefererChart v-if="refererChartVisible" ref="refererChartRef" :data="Data.data" />
                </div>
            </div>

            <!-- 图表容器 - 第三行：地域分布 -->
            <div ref="regionChartContainer">
                <RegionChart v-if="regionChartVisible" ref="regionChartRef" :data="Data.data" />
            </div>

            <!-- 图表容器 - 第四行：访问时间分布 -->
            <div ref="timeChartContainer">
                <TimeChart v-if="timeChartVisible" ref="timeChartRef" :data="Data.data" />
            </div>

            <!-- 数据统计概览 -->
            <DataOverview :total="Data.total" :data="Data.data" />
        </template>

        <!-- 数据加载失败或无数据提示 -->
        <div v-else class="mx-auto max-w-5xl">
            <EmptyState title="暂无访问数据" description="当前没有找到访问记录数据，请检查是否有访问记录或稍后重试" icon-type="blue">
                <template #icon>
                    <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                            d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                    </svg>
                </template>
                <template #action>
                    <button @click="initData" :disabled="loading"
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
import { ref, onMounted } from 'vue';
import { useResizeObserver, useIntersectionObserver, useThrottleFn } from '@vueuse/core';
import PageLayout from '@/components/PageLayout.vue';
import DeviceChart from './components/record/DeviceChart.vue';
import OSChart from './components/record/OSChart.vue';
import BrowserChart from './components/record/BrowserChart.vue';
import RefererChart from './components/record/RefererChart.vue';
import RegionChart from './components/record/RegionChart.vue';
import TimeChart from './components/record/TimeChart.vue';
import DataOverview from './components/record/DataOverview.vue';
import EmptyState from '@/components/EmptyState.vue';
import { DataPageRecordApi } from '@/services/data'


const Data = ref()
const loading = ref(true)

// 图表容器引用
const chartsContainer = ref();
const deviceChartContainer = ref();
const osChartContainer = ref();
const browserChartContainer = ref();
const refererChartContainer = ref();
const regionChartContainer = ref();
const timeChartContainer = ref();

// 图表组件引用
const deviceChartRef = ref();
const osChartRef = ref();
const browserChartRef = ref();
const refererChartRef = ref();
const regionChartRef = ref();
const timeChartRef = ref();

// 图表可见性状态
const deviceChartVisible = ref(false);
const osChartVisible = ref(false);
const browserChartVisible = ref(false);
const refererChartVisible = ref(false);
const regionChartVisible = ref(false);
const timeChartVisible = ref(false);

// 在组件挂载时获取实际数据
const initData = async () => {
    try {
        loading.value = true
        const params = {
            // TODO: [访问记录] 动态获取项目code，替换硬编码值
            code: "04jw1SM0",
            size: 20,
            page: 1
        }
        const result = await DataPageRecordApi(params)
        Data.value = result
    } catch (error) {
        console.error('初始化数据失败:', error)
        Data.value = null
    } finally {
        loading.value = false
    }
}

// 节流处理图表重绘
const throttledResize = useThrottleFn(() => {
    deviceChartRef.value?.resize();
    osChartRef.value?.resize();
    browserChartRef.value?.resize();
    refererChartRef.value?.resize();
    regionChartRef.value?.resize();
    timeChartRef.value?.resize();
}, 200);

// 监听图表容器尺寸变化
useResizeObserver(chartsContainer, throttledResize);

// 懒加载图表 - 只有当图表容器进入视口时才渲染
useIntersectionObserver(
    deviceChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            deviceChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

useIntersectionObserver(
    osChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            osChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

useIntersectionObserver(
    browserChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            browserChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

useIntersectionObserver(
    refererChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            refererChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

useIntersectionObserver(
    regionChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            regionChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

useIntersectionObserver(
    timeChartContainer,
    ([{ isIntersecting }]) => {
        if (isIntersecting && Data.value) {
            timeChartVisible.value = true;
        }
    },
    { threshold: 0.1 }
);

onMounted(() => {
    initData()
});
</script>
