<template>
    <ChartCard
        title="浏览器"
        chartId="browser-chart"
        iconClass="text-orange-600"
        iconBgClass="bg-orange-50"
    >
        <template #icon>
            <svg class="w-5 h-5 text-orange-600" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M4.083 9h1.946c.089-1.546.383-2.97.837-4.118A6.004 6.004 0 004.083 9zM10 2a8 8 0 100 16 8 8 0 000-16zm0 2c-.076 0-.232.032-.465.262-.238.234-.497.623-.737 1.182-.389.907-.673 2.142-.766 3.556h3.936c-.093-1.414-.377-2.649-.766-3.556-.24-.559-.499-.948-.737-1.182C10.232 4.032 10.076 4 10 4zm3.971 5c-.089-1.546-.383-2.97-.837-4.118A6.004 6.004 0 0115.917 9h-1.946zm-2.003 2H8.032c.093 1.414.377 2.649.766 3.556.24.559.499.948.737 1.182.233.23.389.262.465.262.076 0 .232-.032.465-.262.238-.234.497-.623.737-1.182.389-.907.673-2.142.766-3.556zm1.166 4.118c.454-1.148.748-2.572.837-4.118h1.946a6.004 6.004 0 01-2.783 4.118zm-6.268 0C6.412 13.97 6.118 12.546 6.03 11H4.083a6.004 6.004 0 002.783 4.118z" clip-rule="evenodd" />
            </svg>
        </template>
    </ChartCard>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface BrowserDataItem {
    browserName: string | null;
    pvCount: number;
}

interface Props {
    data: BrowserDataItem[];
    loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
    loading: false
});

let browserChart: echarts.ECharts | null = null;

// 格式化名称
function formatBrowserName(browserName: string | null): string {
    if (!browserName || browserName === 'Unknown') return '未知浏览器';
    return browserName;
}

// 获取颜色
function getBrowserColor(browserName: string | null): string {
    const colors = {
        'Chrome': '#4285f4',
        'Safari': '#ff6b00',
        'Firefox': '#ff7139',
        'Edge': '#0078d7',
        'Unknown': '#6b7280'
    };
    return colors[browserName as keyof typeof colors] || '#3b82f6';
}

// 初始化图表
function initChart() {
    const chartDom = document.getElementById('browser-chart');
    if (!chartDom) return;

    browserChart = echarts.init(chartDom);

    const sortedData = [...props.data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
    
    const pieData = sortedData.map(item => ({
        name: formatBrowserName(item.browserName),
        value: item.pvCount || 0,
        itemStyle: {
            color: getBrowserColor(item.browserName)
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
            name: '浏览器',
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

    browserChart.setOption(option);
}

// 监听数据变化
watch(() => props.data, () => {
    if (browserChart) {
        initChart();
    }
}, { deep: true });

// 调整图表大小
function handleResize() {
    browserChart?.resize();
}

onMounted(() => {
    setTimeout(initChart, 100);
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    browserChart?.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>
