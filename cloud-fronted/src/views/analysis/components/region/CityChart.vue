<template>
    <ChartCard title="城市访问 TOP 10" chart-id="cityChart" icon-class="text-green-600" icon-bg-class="bg-green-100"
        icon-path="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import ChartCard from '../ChartCard.vue'

interface Props {
    data: any[]
    loading?: boolean
}

const props = defineProps<Props>()

let chart: echarts.ECharts | null = null

const processCityData = () => {
    return props.data
        .filter(item => item.city && item.province)
        .map(item => ({
            name: item.city,
            fullName: `${item.province} ${item.city}`,
            value: item.ipCount,
            province: item.province
        }))
        .sort((a, b) => b.value - a.value)
        .slice(0, 10)
}

const getChartOption = (data: any[]) => ({
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow',
            shadowStyle: {
                color: 'rgba(99, 102, 241, 0.1)'
            }
        },
        backgroundColor: 'rgba(255, 255, 255, 0.95)',
        borderColor: '#e5e7eb',
        borderWidth: 1,
        textStyle: {
            color: '#374151',
            fontSize: 12
        },
        formatter: function (params: any) {
            const item = params[0]
            const dataItem = data[item.dataIndex]
            return `${dataItem.fullName}<br/>访问量: ${item.value}`
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '15%',
        top: '10%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        data: data.map(item => item.name),
        axisLabel: {
            rotate: 45,
            interval: 0,
            fontSize: 11,
            color: '#6b7280'
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
    },
    yAxis: {
        type: 'value',
        name: '访问量',
        nameTextStyle: {
            fontSize: 12,
            color: '#6b7280'
        },
        axisLabel: {
            color: '#6b7280',
            fontSize: 11
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
    series: [
        {
            name: '访问量',
            type: 'bar',
            data: data.map((item, index) => ({
                value: item.value,
                name: item.fullName,
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: `hsl(${(index * 36) % 360}, 70%, 65%)` },
                        { offset: 1, color: `hsl(${(index * 36) % 360}, 70%, 45%)` }
                    ]),
                    borderRadius: [4, 4, 0, 0]
                }
            })),
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowColor: 'rgba(0, 0, 0, 0.2)'
                }
            },
            barWidth: '60%'
        }
    ]
})

function initChart() {
    const chartDom = document.getElementById('cityChart')
    if (!chartDom) return

    if (chart) {
        chart.dispose()
        chart = null
    }

    chart = echarts.init(chartDom)
    updateChart()
}

function updateChart() {
    if (!chart || props.loading) return

    const processedData = processCityData()

    if (processedData.length === 0) {
        chart.clear()
        return
    }

    const option = getChartOption(processedData)
    chart.setOption(option)
}

onMounted(() => {
    initChart()
})

onUnmounted(() => {
    if (chart) {
        chart.dispose()
        chart = null
    }
})

watch(() => [props.data, props.loading], () => {
    updateChart()
}, { deep: true })

defineExpose({
    resize: () => chart?.resize()
})
</script>
