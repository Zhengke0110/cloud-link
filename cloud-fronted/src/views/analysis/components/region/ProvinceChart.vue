<template>
    <ChartCard title="省份访问分布" chart-id="provinceChart" icon-class="text-blue-600" icon-bg-class="bg-blue-100"
        icon-path="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import ChartCard from '../ChartCard.vue'

interface Props {
    data: any[]
    loading?: boolean
    dateRange?: { startTime: string, endTime: string }
}

const props = defineProps<Props>()

let chart: echarts.ECharts | null = null

const processProvinceData = () => {
    const provinceMap = new Map()

    props.data.forEach(item => {
        if (item.province) {
            const existingCount = provinceMap.get(item.province) || 0
            provinceMap.set(item.province, existingCount + item.ipCount)
        }
    })

    return Array.from(provinceMap.entries())
        .map(([name, value]) => ({ name, value }))
        .sort((a, b) => b.value - a.value)
        .slice(0, 15)
}

const getChartOption = (data: any[]) => ({
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)',
        backgroundColor: 'rgba(255, 255, 255, 0.95)',
        borderColor: '#e5e7eb',
        borderWidth: 1,
        textStyle: {
            color: '#374151',
            fontSize: 12
        }
    },
    legend: {
        type: 'scroll',
        orient: 'vertical',
        right: 20,
        top: 20,
        bottom: 20,
        itemWidth: 10,
        itemHeight: 10,
        textStyle: {
            fontSize: 11,
            color: '#6b7280'
        }
    },
    series: [
        {
            name: '访问量',
            type: 'pie',
            radius: ['45%', '75%'],
            center: ['40%', '50%'],
            data: data,
            emphasis: {
                itemStyle: {
                    shadowBlur: 15,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.2)'
                }
            },
            label: {
                show: false
            },
            labelLine: {
                show: false
            },
            itemStyle: {
                borderRadius: 4,
                borderColor: '#fff',
                borderWidth: 2
            }
        }
    ]
})

function initChart() {
    const chartDom = document.getElementById('provinceChart')
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

    const processedData = processProvinceData()

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
