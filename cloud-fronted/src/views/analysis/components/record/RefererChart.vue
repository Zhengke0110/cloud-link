<template>
    <ChartCard title="访问来源分布" chart-id="refererChart" icon-class="text-amber-600" icon-bg-class="bg-amber-100"
        icon-path="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        referer: string;
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
    const chartDom = document.getElementById('refererChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);

    // 统计来源网站数据
    const refererCount: Record<string, number> = {};
    props.data.forEach(item => {
        let referer = item.referer || '直接访问';
        if (referer === '') {
            referer = '直接访问';
        }
        refererCount[referer] = (refererCount[referer] || 0) + 1;
    });

    // 对数据进行排序并合并小数据
    const sortedData = Object.entries(refererCount)
        .sort((a, b) => b[1] - a[1])
        .map(([name, value]) => ({ name, value }));

    // 只显示前5个来源，其余合并为"其他"
    const topReferers = sortedData.slice(0, 5);
    const otherCount = sortedData.slice(5).reduce((sum, item) => sum + item.value, 0);

    const finalData = [...topReferers];
    if (otherCount > 0) {
        finalData.push({ name: '其他来源', value: otherCount });
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
                name: '访问来源',
                type: 'pie',
                radius: ['30%', '65%'],
                center: ['50%', '45%'],
                color: ['#f59e0b', '#10b981', '#3b82f6', '#ef4444', '#8b5cf6', '#6b7280'],
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
