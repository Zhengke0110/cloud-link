<template>
    <PageLayout tag="数据分析" title="访问来源分析" description="分析您的短链接访问来源分布，了解流量入口">

        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
                ref="dateRangeSelectorRef" />
        </div>

        <!-- 图表容器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
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
            <SourceStatsCards :data="filteredData" />
        </div>

        <!-- 数据统计表格 -->
        <div class="mx-auto max-w-6xl">
            <SourceDataTable :data="filteredData" :loading="loading" />
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { frequentSourceData } from './config copy';
import PageLayout from '@/components/PageLayout.vue';
import DateRangeSelector from './components/DateRangeSelector.vue';
import SourceBarChart from './components/frequent/SourceBarChart.vue';
import SourcePieChart from './components/frequent/SourcePieChart.vue';
import SourceStatsCards from './components/frequent/SourceStatsCards.vue';
import SourceDataTable from './components/frequent/SourceDataTable.vue';

const dateRangeSelectorRef = ref();

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

        // TODO: 替换为实际的API请求
        await new Promise(resolve => setTimeout(resolve, 800));
        console.log('API请求参数:', params);

        return frequentSourceData;
    } catch (error) {
        console.error('获取来源数据失败:', error);
        return [];
    } finally {
        loading.value = false;
    }
};

// 根据日期范围过滤数据
const filteredData = computed(() => {
    if (!currentDateRange.value.startTime || !currentDateRange.value.endTime) {
        return sourceData.value;
    }
    return sourceData.value;
});

// 排序后的数据
const sortedData = computed(() => {
    return [...filteredData.value].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

// 处理日期变化
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    console.log('日期范围变化:', dateRange);
    currentDateRange.value = dateRange;
};

// 刷新数据
const handleRefresh = async (dateRange: { startTime: string, endTime: string }) => {
    currentDateRange.value = dateRange;

    try {
        const params = {
            code: "04jw1SM0", // TODO: 替换为实际的code参数
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        };

        const data = await fetchSourceData(params);
        sourceData.value = data;
    } catch (error) {
        console.error('刷新数据失败:', error);
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