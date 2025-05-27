<template>
    <PageLayout tag="数据分析" title="访问数据分析" description="深入了解您的短链接访问情况，包括用户设备、地域分布、访问时间等详细数据">

        <!-- 图表容器 - 第一行：设备类型和操作系统 -->
        <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
            <DeviceChart ref="deviceChartRef" :data="Data.data" />
            <OSChart ref="osChartRef" :data="Data.data" />
        </div>

        <!-- 图表容器 - 第二行：浏览器和访问来源 -->
        <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
            <BrowserChart ref="browserChartRef" :data="Data.data" />
            <RefererChart ref="refererChartRef" :data="Data.data" />
        </div>

        <!-- 图表容器 - 第三行：地域分布 -->
        <RegionChart ref="regionChartRef" :data="Data.data" />

        <!-- 图表容器 - 第四行：访问时间分布 -->
        <TimeChart ref="timeChartRef" :data="Data.data" />

        <!-- 数据统计概览 -->
        <DataOverview :total="Data.total" :data="Data.data" />
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { pageRecordData } from './config copy';
import PageLayout from '@/components/PageLayout.vue';
import DeviceChart from './components/record/DeviceChart.vue';
import OSChart from './components/record/OSChart.vue';
import BrowserChart from './components/record/BrowserChart.vue';
import RefererChart from './components/record/RefererChart.vue';
import RegionChart from './components/record/RegionChart.vue';
import TimeChart from './components/record/TimeChart.vue';
import DataOverview from './components/record/DataOverview.vue';

// TODO: 替换为实际的网络请求获取数据
// const fetchRecordData = async (params: { code: string }) => {
//     try {
//         const response = await fetch('/api/analysis/record', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(params)
//         })
//         const result = await response.json()
//         return result
//     } catch (error) {
//         console.error('获取访问记录数据失败:', error)
//         return { data: [], total: 0 }
//     }
// }

// TODO: 暂时使用本地数据，实际项目中需要替换为网络请求
const Data = ref(pageRecordData)

// TODO: 在组件挂载时获取实际数据
// const initData = async () => {
//     try {
//         const params = {
//             code: "04jw1SM0", // TODO: 替换为实际的code参数
//         }
//         const result = await fetchRecordData(params)
//         Data.value = result
//     } catch (error) {
//         console.error('初始化数据失败:', error)
//     }
// }

// 图表组件引用
const deviceChartRef = ref();
const osChartRef = ref();
const browserChartRef = ref();
const refererChartRef = ref();
const regionChartRef = ref();
const timeChartRef = ref();

// 窗口大小改变时重绘所有图表
function handleResize() {
    deviceChartRef.value?.resize();
    osChartRef.value?.resize();
    browserChartRef.value?.resize();
    refererChartRef.value?.resize();
    regionChartRef.value?.resize();
    timeChartRef.value?.resize();
}

onMounted(() => {
    // TODO: 取消注释以启用实际数据获取
    // initData()

    // 处理窗口大小改变，重绘图表
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    // 移除事件监听
    window.removeEventListener('resize', handleResize);
});
</script>
