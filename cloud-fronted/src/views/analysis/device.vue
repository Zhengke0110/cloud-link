<template>
    <PageLayout tag="设备分析" title="设备信息统计" description="分析访问用户的设备类型、操作系统和浏览器分布情况">
        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
                ref="dateRangeSelectorRef" />
        </div>

        <!-- 图表容器 -->
        <div class="mx-auto max-w-6xl">
            <div class="grid grid-cols-1 gap-8 lg:grid-cols-3 mb-8">
                <!-- 操作系统分布图表 -->
                <OsChart :data="deviceData.os" :loading="loading" />

                <!-- 设备类型分布图表 -->
                <DeviceChart :data="deviceData.device" :loading="loading" />

                <!-- 浏览器分布图表 -->
                <BrowserChart :data="deviceData.browser" :loading="loading" />
            </div>

            <!-- 详细统计表格 -->
            <div class="grid grid-cols-1 gap-8 lg:grid-cols-3">
                <!-- 操作系统详情列表 -->
                <OsDetailList :data="deviceData.os" />

                <!-- 设备类型详情列表 -->
                <DeviceDetailList :data="deviceData.device" />

                <!-- 浏览器详情列表 -->
                <BrowserDetailList :data="deviceData.browser" />
            </div>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { deviceInfoData } from './config copy';
import PageLayout from '@/components/PageLayout.vue';
import DateRangeSelector from './components/DateRangeSelector.vue';
import OsChart from './components/device/OsChart.vue';
import DeviceChart from './components/device/DeviceChart.vue';
import BrowserChart from './components/device/BrowserChart.vue';
import OsDetailList from './components/device/OsDetailList.vue';
import DeviceDetailList from './components/device/DeviceDetailList.vue';
import BrowserDetailList from './components/device/BrowserDetailList.vue';

// 添加加载状态和数据管理
const loading = ref<boolean>(false);
const deviceData = ref(deviceInfoData);
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
});
const dateRangeSelectorRef = ref();

// 添加数据过滤和API相关函数
const fetchDeviceData = async (params: {
    code: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true;

        // TODO: 替换为实际的API请求
        await new Promise(resolve => setTimeout(resolve, 500));
        return deviceInfoData;

    } catch (error) {
        console.error('获取设备数据失败:', error);
        return deviceInfoData;
    } finally {
        loading.value = false;
    }
};

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
            code: "04jw1SM0", // TODO: 替换为实际的code参数
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        };

        const data = await fetchDeviceData(params);
        deviceData.value = data;
    } catch (error) {
        console.error('刷新图表失败:', error);
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