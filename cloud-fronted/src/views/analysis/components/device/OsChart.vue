<template>
    <ChartCard
        title="操作系统"
        chartId="os-chart"
        iconClass="text-blue-600"
        iconBgClass="bg-blue-50"
        iconPath="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
    >
        <template #icon>
            <svg class="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd" />
            </svg>
        </template>
    </ChartCard>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface OsDataItem {
    os: string | null;
    pvCount: number;
}

interface Props {
    data: OsDataItem[];
    loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
    loading: false
});

let osChart: echarts.ECharts | null = null;

// 格式化名称
function formatOsName(os: string | null): string {
    if (!os || os === 'Unknown') return '未知系统';
    return os;
}

// 获取颜色
function getOsColor(os: string | null): string {
    const colors = {
        'Windows': '#0078d4',
        'Mac OS X': '#000000',
        'Unknown': '#6b7280'
    };
    return colors[os as keyof typeof colors] || '#3b82f6';
}

// 初始化图表
function initChart() {
    const chartDom = document.getElementById('os-chart');
    if (!chartDom) return;

    osChart = echarts.init(chartDom);

    const sortedData = [...props.data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
    
    const pieData = sortedData.map(item => ({
        name: formatOsName(item.os),
        value: item.pvCount || 0,
        itemStyle: {
            color: getOsColor(item.os)
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
            name: '操作系统',
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

    osChart.setOption(option);
}

// 监听数据变化
watch(() => props.data, () => {
    if (osChart) {
        initChart();
    }
}, { deep: true });

// 调整图表大小
function handleResize() {
    osChart?.resize();
}

onMounted(() => {
    setTimeout(initChart, 100);
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    osChart?.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>
