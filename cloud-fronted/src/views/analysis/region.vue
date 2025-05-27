<template>
    <PageLayout tag="区域分析" title="访问地区分布统计" description="查看不同省份和城市的访问量分布情况">
        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
                ref="dateRangeSelectorRef" />
        </div>

        <!-- 图表容器 -->
        <div class="mx-auto max-w-6xl">
            <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
                <!-- 省份访问分布图表 -->
                <ProvinceChart :data="filteredData" :loading="loading" :date-range="currentDateRange" />

                <!-- 城市访问TOP10图表 -->
                <CityChart :data="filteredData" :loading="loading" />
            </div>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { regionDayData } from './config copy'
import PageLayout from '@/components/PageLayout.vue'
import DateRangeSelector from './components/DateRangeSelector.vue'
import ProvinceChart from './components/region/ProvinceChart.vue'
import CityChart from './components/region/CityChart.vue'

const loading = ref<boolean>(false)
const regionData = ref<any[]>([])
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
})
const dateRangeSelectorRef = ref()

// 获取区域数据的API接口
const fetchRegionData = async (params: {
    code: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true

        // TODO: 替换为实际的API请求
        await new Promise(resolve => setTimeout(resolve, 500))
        return regionDayData

    } catch (error) {
        console.error('获取区域数据失败:', error)
        return []
    } finally {
        loading.value = false
    }
}

// 根据日期范围过滤数据
const filteredData = computed(() => {
    if (!currentDateRange.value.startTime || !currentDateRange.value.endTime) {
        return regionData.value
    }

    return regionData.value.filter(item => {
        const itemDate = item.date || item.timestamp || item.createTime
        if (!itemDate) return true

        const itemDateStr = typeof itemDate === 'string' ?
            itemDate.replace(/-/g, '') :
            itemDate.toString()

        return itemDateStr >= currentDateRange.value.startTime &&
            itemDateStr <= currentDateRange.value.endTime
    })
})

// 处理日期变化
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    console.log('日期范围变化:', dateRange)
    currentDateRange.value = dateRange
}

// 刷新图表数据
const handleRefresh = async (dateRange: { startTime: string, endTime: string }) => {
    currentDateRange.value = dateRange

    try {
        const params = {
            code: "04jw1SM0", // TODO: 替换为实际的code参数
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        }

        const data = await fetchRegionData(params)
        regionData.value = data
    } catch (error) {
        console.error('刷新图表失败:', error)
    }
}

// 初始化数据
const initData = async () => {
    if (dateRangeSelectorRef.value) {
        const dateRange = dateRangeSelectorRef.value.getDateRange()
        await handleRefresh(dateRange)
    }
}

onMounted(async () => {
    // 延迟初始化，确保子组件已挂载
    setTimeout(initData, 100)
})
</script>
