<template>
    <ChartCard
        title="设备类型分布"
        chart-id="deviceChart"
        icon-class="text-blue-600"
        icon-bg-class="bg-blue-100"
        icon-path="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
    />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        deviceType: string;
        [key: string]: any;
    }>;
}

const props = defineProps<Props>();

let chart: echarts.ECharts | null = null;

onMounted(() => {
    initChart();
});

onUnmounted(() => {
    if (chart) {
        chart.dispose();
        chart = null;
    }
});

function initChart() {
    const chartDom = document.getElementById('deviceChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);

    // 统计设备类型数据
    const deviceCount: Record<string, number> = {};
    props.data.forEach(item => {
        const deviceType = item.deviceType || 'Unknown';
        deviceCount[deviceType] = (deviceCount[deviceType] || 0) + 1;
    });

    const data = Object.entries(deviceCount).map(([name, value]) => ({ name, value }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            }
        },
        legend: {
            orient: 'vertical',
            left: 10,
            data: Object.keys(deviceCount),
            textStyle: {
                color: '#6b7280'
            }
        },
        series: [
            {
                name: '设备类型',
                type: 'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                color: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '16',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: data
            }
        ]
    };

    chart.setOption(option);
}

defineExpose({
    resize: () => chart?.resize()
});
</script>
