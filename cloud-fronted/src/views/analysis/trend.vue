<template>
    <PageLayout tag="数据分析" title="访问趋势分析" description="查看您的短链接访问趋势，了解流量变化规律">

        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
                ref="dateRangeSelectorRef" />
        </div>

        <!-- 趋势图表 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <TrendChart :data="trendData" :loading="loading" />
        </div>

        <!-- 数据指标卡片 -->
        <div class="mx-auto max-w-6xl">
            <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
                <StatCard label="总页面访问量" :value="totalStats.pv" description="PV (Page Views)" type="blue"
                    :loading="loading" />

                <StatCard label="独立访客数" :value="totalStats.uv" description="UV (Unique Visitors)" type="emerald"
                    :loading="loading" />

                <StatCard label="独立IP数" :value="totalStats.ip" description="IP Addresses" type="purple"
                    :loading="loading" />

                <StatCard label="新访客数" :value="totalStats.newUV" description="New Visitors" type="orange"
                    :loading="loading" />
            </div>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { trendData as mockTrendData } from './config copy';
import PageLayout from '@/components/PageLayout.vue';
import TrendChart from './components/trend/TrendChart.vue';
import StatCard from './components/trend/StatCard.vue';
import DateRangeSelector from './components/DateRangeSelector.vue';

const loading = ref<boolean>(false);
const trendData = ref<any[]>([]);
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
});
const dateRangeSelectorRef = ref();

// 获取趋势数据的API接口
const fetchTrendData = async (params: {
    code: string
    type: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true;

        // TODO: 替换为实际的趋势数据API请求
        // const response = await api.getTrendData(params);
        // return response.data;

        // 模拟网络延迟
        await new Promise(resolve => setTimeout(resolve, 500));

        console.log('API请求参数:', params);

        // 暂时使用模拟数据
        return mockTrendData;

    } catch (error) {
        console.error('获取趋势数据失败:', error);
        // TODO: 添加错误处理和用户提示
        return [];
    } finally {
        loading.value = false;
    }
};

// 计算总统计数据
const totalStats = computed(() => {
    if (trendData.value.length === 0) {
        return { pv: 0, uv: 0, ip: 0, newUV: 0 };
    }

    return trendData.value.reduce((acc, item) => ({
        pv: acc.pv + (item.pvCount || 0),
        uv: acc.uv + (item.uvCount || 0),
        ip: acc.ip + (item.ipCount || 0),
        newUV: acc.newUV + (item.newUVCount || 0)
    }), { pv: 0, uv: 0, ip: 0, newUV: 0 });
});

// 处理日期变化
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    console.log('日期范围变化:', dateRange);
    currentDateRange.value = dateRange;
};

// 刷新图表数据
const handleRefresh = async (dateRange: { startTime: string, endTime: string }) => {
    currentDateRange.value = dateRange;

    try {
        const params = {
            code: "04jw1SM0", // TODO: 替换为实际的项目code参数
            type: "Day",
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        };

        const data = await fetchTrendData(params);
        trendData.value = data;
    } catch (error) {
        console.error('刷新图表失败:', error);
        // TODO: 添加错误提示给用户
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