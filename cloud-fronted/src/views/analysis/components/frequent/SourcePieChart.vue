<template>
    <ChartCard 
        title="来源占比" 
        :chart-id="chartId"
        icon-class="text-emerald-600"
        icon-bg-class="bg-emerald-50"
        icon-path="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z">
        <template #icon>
            <div class="flex items-center space-x-2 text-sm text-gray-500 bg-emerald-50 px-3 py-1.5 rounded-lg absolute top-4 right-6">
                <svg class="w-4 h-4 text-emerald-600" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z"/>
                    <path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z"/>
                </svg>
                <span class="text-emerald-700 font-medium">分布图</span>
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

const chartId = `source-pie-chart-${Math.random().toString(36).substr(2, 9)}`;
let chart: echarts.ECharts | null = null;

// 格式化来源网站名称
const formatReferer = (referer: string | null): string => {
    if (!referer) return '直接访问';
    return referer.replace(/\.com$/, '').replace(/\.net$/, '').replace(/\.cn$/, '');
};

// 获取来源颜色
const getSourceColor = (referer: string | null): string => {
    const colors = [
        '#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6',
        '#06b6d4', '#84cc16', '#f97316', '#ec4899', '#6366f1'
    ];
    if (!referer) return colors[0];
    const index = referer.length % colors.length;
    return colors[index];
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
    const pieData = sortedData.map((item) => ({
        name: formatReferer(item.referer),
        value: item.pvCount || 0,
        itemStyle: {
            color: getSourceColor(item.referer)
        }
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            top: 'center',
            textStyle: {
                color: '#6b7280',
                fontSize: 12
            }
        },
        series: [{
            name: '访问来源',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['65%', '50%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: 16,
                    fontWeight: 'bold',
                    color: '#374151'
                },
                scale: true,
                scaleSize: 10
            },
            labelLine: {
                show: false
            },
            data: pieData
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
