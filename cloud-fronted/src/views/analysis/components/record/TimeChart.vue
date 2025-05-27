<template>
    <ChartCard title="访问时间分布" chart-id="timeChart" icon-class="text-rose-600" icon-bg-class="bg-rose-100"
        icon-path="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, watch } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        visitTime: number;
        [key: string]: any;
    }>;
}

const props = defineProps<Props>();

let chart: echarts.ECharts | null = null;

onMounted(() => {
    initChart();
    updateChart();
});

onUnmounted(() => {
    if (chart) {
        chart.dispose();
        chart = null;
    }
});

// 监听数据变化，自动更新图表
watch(() => props.data, () => {
    updateChart();
}, { deep: true });

function initChart() {
    const chartDom = document.getElementById('timeChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);
}

function processTimeData() {
    // 数据验证和错误处理
    if (!props.data || !Array.isArray(props.data) || props.data.length === 0) {
        return Array(24).fill(0);
    }

    // 按小时统计访问量
    const hourCount = Array(24).fill(0);

    props.data.forEach(item => {
        // 验证 visitTime 是否有效
        if (!item.visitTime || typeof item.visitTime !== 'number') return;

        try {
            const date = new Date(item.visitTime);
            // 检查日期是否有效
            if (isNaN(date.getTime())) return;

            const hour = date.getHours();
            if (hour >= 0 && hour < 24) {
                hourCount[hour]++;
            }
        } catch (error) {
            console.warn('处理访问时间数据时出错:', error, item);
        }
    });

    return hourCount;
}

function getChartOption(hourCount: number[]) {
    // 计算平均访问量
    const totalVisits = hourCount.reduce((sum, count) => sum + count, 0);
    const averageVisits = totalVisits / 24;

    return {
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
            formatter: (params: any) => {
                const data = params[0];
                const hour = data.dataIndex;
                const count = data.value;
                const nextHour = (hour + 1) % 24;
                return `
                    <div style="padding: 8px;">
                        <div style="font-weight: 600; margin-bottom: 4px;">
                            ${hour}:00 - ${nextHour}:00
                        </div>
                        <div style="color: #ec4899;">
                            访问量: ${count}
                        </div>
                        <div style="color: #f59e0b;">
                            平均访问量: ${averageVisits.toFixed(1)}
                        </div>
                        <div style="color: #6b7280; font-size: 12px; margin-top: 4px;">
                            占总访问量: ${((count / props.data.length) * 100).toFixed(1)}%
                        </div>
                    </div>
                `;
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: Array.from({ length: 24 }, (_, i) => `${i}时`),
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            },
            axisLabel: {
                color: '#6b7280',
                interval: 1 // 显示所有标签
            }
        },
        yAxis: {
            type: 'value',
            minInterval: 1, // 确保Y轴显示整数
            axisLine: {
                lineStyle: {
                    color: '#e5e7eb'
                }
            },
            axisLabel: {
                color: '#6b7280'
            },
            splitLine: {
                lineStyle: {
                    color: '#f3f4f6',
                    type: 'dashed'
                }
            }
        },
        series: [
            {
                name: '访问量',
                type: 'line',
                smooth: true,
                color: '#ec4899',
                data: hourCount,
                lineStyle: {
                    width: 3
                },
                symbolSize: 6,
                symbol: 'circle',
                areaStyle: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [
                            {
                                offset: 0,
                                color: 'rgba(236, 72, 153, 0.3)'
                            },
                            {
                                offset: 1,
                                color: 'rgba(236, 72, 153, 0.05)'
                            }
                        ]
                    }
                },
                emphasis: {
                    focus: 'series',
                    itemStyle: {
                        borderColor: '#ec4899',
                        borderWidth: 2
                    }
                },
                markLine: {
                    data: [
                        {
                            type: 'average',
                            name: '平均访问量',
                            yAxis: averageVisits,
                            lineStyle: {
                                color: '#f59e0b',
                                width: 2,
                                type: 'dashed'
                            },
                            label: {
                                show: true,
                                position: 'end',
                                formatter: '平均: {c}',
                                color: '#f59e0b',
                                fontSize: 12,
                                fontWeight: 'bold'
                            }
                        }
                    ],
                    silent: false,
                    symbol: ['none', 'none']
                }
            }
        ]
    };
}

function updateChart() {
    if (!chart) return;

    const hourCount = processTimeData();
    const option = getChartOption(hourCount);

    chart.setOption(option, true); // true 表示不合并，直接替换
}

defineExpose({
    resize: () => chart?.resize(),
    updateChart // 暴露更新方法供外部调用
});
</script>
