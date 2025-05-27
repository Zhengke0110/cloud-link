<template>
    <ChartCard title="访问趋势" chartId="trend-chart" iconClass="text-blue-600" iconBgClass="bg-blue-100">
        <template #icon>
            <svg class="h-6 w-6 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                    d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z"
                    clip-rule="evenodd" />
            </svg>
        </template>
    </ChartCard>

    <!-- 数据指标显示 -->
    <div class="absolute top-4 right-4">
        <div class="flex items-center space-x-2 rounded-lg bg-gray-50 px-3 py-1.5 text-sm text-gray-600">
            <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                    d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z"
                    clip-rule="evenodd" />
            </svg>
            <span>最近30天数据</span>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface TrendDataItem {
    dateTimeStr: string | null;
    pvCount?: number;
    uvCount?: number;
    ipCount?: number;
    newUVCount?: number;
}

interface Props {
    data: TrendDataItem[];
    loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
    loading: false
});

let trendChart: echarts.ECharts | null = null;

// 处理数据并格式化日期
const processedData = computed(() => {
    return props.data.map(item => ({
        ...item,
        formattedDate: formatDate(item.dateTimeStr)
    }));
});

// 格式化日期字符串
function formatDate(dateStr: string | null): string {
    if (!dateStr) return '';
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    return `${year}-${month}-${day}`;
}

// 初始化趋势图表
function initTrendChart() {
    const chartElement = document.getElementById('trend-chart');
    if (!chartElement) return;

    trendChart = echarts.init(chartElement);

    const dates = processedData.value.map(item => item.formattedDate);
    const pvData = processedData.value.map(item => item.pvCount || 0);
    const uvData = processedData.value.map(item => item.uvCount || 0);
    const ipData = processedData.value.map(item => item.ipCount || 0);
    const newUVData = processedData.value.map(item => item.newUVCount || 0);

    const option = {
        title: {
            show: false
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            },
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        legend: {
            data: ['页面访问量', '独立访客', '独立IP', '新访客'],
            bottom: 20,
            textStyle: {
                color: '#6b7280',
                fontSize: 13
            },
            itemGap: 30
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            top: '5%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: dates,
                axisLabel: {
                    color: '#9ca3af',
                    fontSize: 12
                },
                axisLine: {
                    lineStyle: {
                        color: '#e5e7eb'
                    }
                },
                axisTick: {
                    lineStyle: {
                        color: '#e5e7eb'
                    }
                }
            }
        ],
        yAxis: [
            {
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
            }
        ],
        series: [
            {
                name: '页面访问量',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#3b82f6'
                        }, {
                            offset: 1, color: 'rgba(59, 130, 246, 0)'
                        }]
                    }
                },
                emphasis: {
                    focus: 'series'
                },
                data: pvData,
                itemStyle: {
                    color: '#3b82f6'
                }
            },
            {
                name: '独立访客',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#10b981'
                        }, {
                            offset: 1, color: 'rgba(16, 185, 129, 0)'
                        }]
                    }
                },
                emphasis: {
                    focus: 'series'
                },
                data: uvData,
                itemStyle: {
                    color: '#10b981'
                }
            },
            {
                name: '独立IP',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#8b5cf6'
                        }, {
                            offset: 1, color: 'rgba(139, 92, 246, 0)'
                        }]
                    }
                },
                emphasis: {
                    focus: 'series'
                },
                data: ipData,
                itemStyle: {
                    color: '#8b5cf6'
                }
            },
            {
                name: '新访客',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                    width: 3
                },
                areaStyle: {
                    opacity: 0.1,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#f59e0b'
                        }, {
                            offset: 1, color: 'rgba(245, 158, 11, 0)'
                        }]
                    }
                },
                emphasis: {
                    focus: 'series'
                },
                data: newUVData,
                itemStyle: {
                    color: '#f59e0b'
                }
            }
        ]
    };

    trendChart.setOption(option);
}

// 调整图表大小
function handleResize() {
    trendChart?.resize();
}

// 监听数据变化，重新渲染图表
watch(() => props.data, () => {
    if (trendChart) {
        initTrendChart();
    }
}, { deep: true });

onMounted(() => {
    initTrendChart();
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    trendChart?.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>
