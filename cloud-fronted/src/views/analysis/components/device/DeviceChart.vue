<template>
    <ChartCard
        title="设备类型"
        chartId="device-chart"
        iconClass="text-emerald-600"
        iconBgClass="bg-emerald-50"
    >
        <template #icon>
            <svg class="w-5 h-5 text-emerald-600" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M3 5a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2h-2.22l.123.489.804.804A1 1 0 0113 18H7a1 1 0 01-.707-1.707l.804-.804L7.22 15H5a2 2 0 01-2-2V5zm5.771 7H5V5h10v7H8.771z" clip-rule="evenodd" />
            </svg>
        </template>
    </ChartCard>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface DeviceDataItem {
    deviceType: string | null;
    pvCount: number;
}

interface Props {
    data: DeviceDataItem[];
    loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
    loading: false
});

let deviceChart: echarts.ECharts | null = null;

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

// 初始化图表
function initChart() {
    const chartDom = document.getElementById('device-chart');
    if (!chartDom) return;

    deviceChart = echarts.init(chartDom);

    const sortedData = [...props.data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
    
    const pieData = sortedData.map(item => ({
        name: formatDeviceName(item.deviceType),
        value: item.pvCount || 0,
        itemStyle: {
            color: getDeviceColor(item.deviceType)
        }
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: { color: '#374151' },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        series: [{
            name: '设备类型',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: pieData,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
            label: {
                show: false
            },
            labelLine: {
                show: false
            }
        }]
    };

    deviceChart.setOption(option);
}

// 监听数据变化
watch(() => props.data, () => {
    if (deviceChart) {
        initChart();
    }
}, { deep: true });

// 调整图表大小
function handleResize() {
    deviceChart?.resize();
}

onMounted(() => {
    setTimeout(initChart, 100);
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    deviceChart?.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>
