<template>
    <ChartCard
        title="浏览器分布"
        chart-id="browserChart"
        icon-class="text-purple-600"
        icon-bg-class="bg-purple-100"
        icon-path="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9-9a9 9 0 00-9 9m0 0a9 9 0 019-9"
    />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        browserName: string;
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
    const chartDom = document.getElementById('browserChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);

    // 统计浏览器数据
    const browserCount: Record<string, number> = {};
    props.data.forEach(item => {
        const browser = item.browserName || 'Unknown';
        browserCount[browser] = (browserCount[browser] || 0) + 1;
    });

    const data = Object.entries(browserCount);

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            },
            axisLabel: {
                color: '#6b7280'
            }
        },
        yAxis: {
            type: 'category',
            data: data.map(item => item[0]),
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            },
            axisLabel: {
                color: '#6b7280'
            }
        },
        series: [
            {
                name: '浏览器',
                type: 'bar',
                color: '#8b5cf6',
                data: data.map(item => item[1]),
                itemStyle: {
                    borderRadius: [0, 4, 4, 0]
                }
            }
        ]
    };

    chart.setOption(option);
}

defineExpose({
    resize: () => chart?.resize()
});
</script>
