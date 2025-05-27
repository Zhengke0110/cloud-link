<template>
    <ChartCard 
        title="访问来源排行" 
        :chart-id="chartId"
        icon-class="text-blue-600"
        icon-bg-class="bg-blue-50"
        icon-path="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z">
        <template #icon>
            <div class="flex items-center space-x-2 text-sm text-gray-500 bg-blue-50 px-3 py-1.5 rounded-lg absolute top-4 right-6">
                <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                </svg>
                <span class="text-blue-700 font-medium">PV统计</span>
            </div>
        </template>
    </ChartCard>
    
    <!-- 加载状态遮罩 -->
    <div v-if="loading" class="absolute inset-0 bg-white bg-opacity-75 flex items-center justify-center z-10 rounded-xl">
        <div class="flex items-center space-x-2 text-gray-500">
            <svg class="animate-spin w-5 h-5" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
            </svg>
            <span>加载中...</span>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        referer: string | null;
        pvCount: number;
    }>;
    loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
    loading: false
});

const chartId = `source-bar-chart-${Math.random().toString(36).substr(2, 9)}`;
let chart: echarts.ECharts | null = null;

// 格式化来源网站名称
const formatReferer = (referer: string | null): string => {
    if (!referer) return '直接访问';
    return referer.replace(/\.com$/, '').replace(/\.net$/, '').replace(/\.cn$/, '');
};

// 初始化图表
const initChart = async () => {
    await nextTick();
    
    const container = document.getElementById(chartId);
    if (!container || props.loading) return;

    // 如果图表已存在，先销毁
    if (chart) {
        chart.dispose();
        chart = null;
    }

    chart = echarts.init(container);

    updateChartData();
};

// 更新图表数据
const updateChartData = () => {
    if (!chart || props.loading) return;

    const sortedData = [...props.data].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
    const sources = sortedData.map(item => formatReferer(item.referer));
    const values = sortedData.map(item => item.pvCount || 0);

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
            },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '10%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: sources,
            axisLabel: {
                color: '#9ca3af',
                fontSize: 12,
                rotate: 45
            },
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            }
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                color: '#9ca3af',
                fontSize: 12
            },
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            },
            splitLine: {
                lineStyle: {
                    color: '#f3f4f6',
                    type: 'dashed'
                }
            }
        },
        series: [{
            name: '访问量',
            type: 'bar',
            data: values,
            itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#3b82f6' },
                    { offset: 1, color: '#1d4ed8' }
                ]),
                borderRadius: [4, 4, 0, 0]
            },
            emphasis: {
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: '#60a5fa' },
                        { offset: 1, color: '#3b82f6' }
                    ])
                }
            }
        }]
    };

    chart.setOption(option, true);
};

// 调整图表大小
const handleResize = () => {
    chart?.resize();
};

// 监听数据变化
watch(() => props.data, () => {
    if (!props.loading && chart) {
        updateChartData();
    }
}, { deep: true });

// 监听加载状态
watch(() => props.loading, (newLoading) => {
    if (!newLoading) {
        setTimeout(initChart, 100);
    }
});

onMounted(() => {
    setTimeout(initChart, 100);
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    if (chart) {
        chart.dispose();
        chart = null;
    }
    window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.absolute {
    position: absolute;
}
</style>
