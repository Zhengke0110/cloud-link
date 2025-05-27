<template>
    <ChartCard
        title="操作系统分布"
        chart-id="osChart"
        icon-class="text-emerald-600"
        icon-bg-class="bg-emerald-100"
        icon-path="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z"
    />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        os: string;
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
    const chartDom = document.getElementById('osChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);

    // 统计操作系统数据
    const osCount: Record<string, number> = {};
    props.data.forEach(item => {
        let os = item.os || 'Unknown';
        if (os === 'Unknown') {
            os = '未知系统';
        }
        osCount[os] = (osCount[os] || 0) + 1;
    });

    // 对数据进行排序并合并小数据
    const sortedData = Object.entries(osCount)
        .sort((a, b) => b[1] - a[1])
        .map(([name, value]) => ({ name, value }));

    // 只显示前5个操作系统，其余合并为"其他"
    const topOSes = sortedData.slice(0, 5);
    const otherCount = sortedData.slice(5).reduce((sum, item) => sum + item.value, 0);

    const finalData = [...topOSes];
    if (otherCount > 0) {
        finalData.push({ name: '其他系统', value: otherCount });
    }

    const option = {
        tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            },
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'horizontal',
            bottom: '0%',
            left: 'center',
            textStyle: {
                color: '#6b7280',
                fontSize: 12
            },
            itemWidth: 10,
            itemHeight: 10,
            itemGap: 15
        },
        series: [
            {
                name: '操作系统',
                type: 'pie',
                radius: ['30%', '65%'],
                center: ['50%', '45%'],
                color: ['#10b981', '#3b82f6', '#f59e0b', '#ef4444', '#8b5cf6', '#6b7280'],
                data: finalData,
                label: {
                    show: true,
                    position: 'inside',
                    formatter: function (params: any) {
                        if (params.percent > 5) {
                            return `${params.percent}%`;
                        }
                        return '';
                    },
                    fontSize: 12,
                    fontWeight: 'bold',
                    color: '#fff'
                },
                labelLine: {
                    show: false
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.3)'
                    },
                    label: {
                        show: true,
                        fontSize: 14,
                        fontWeight: 'bold'
                    }
                },
                animationType: 'scale',
                animationEasing: 'elasticOut',
                animationDelay: function () {
                    return Math.random() * 200;
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
