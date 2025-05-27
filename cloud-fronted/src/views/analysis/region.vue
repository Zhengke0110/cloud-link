<template>
    <PageLayout tag="区域分析" title="访问地区分布统计" description="查看不同省份和城市的访问量分布情况">
        <!-- 日期选择器 -->
        <div class="mx-auto mb-8 max-w-6xl">
            <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
                ref="dateRangeSelectorRef" />
        </div>

        <!-- 图表容器 -->
        <div class="mx-auto max-w-6xl">
            <div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
                <!-- 省份访问分布图表 -->
                <div
                    class="bg-white rounded-xl shadow-md border border-gray-100 p-6 transition-all duration-300 hover:shadow-lg">
                    <div class="flex items-center justify-between mb-6">
                        <div>
                            <h3 class="text-lg font-bold text-gray-900 flex items-center">
                                <svg class="w-5 h-5 text-blue-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path fill-rule="evenodd"
                                        d="M3 6a3 3 0 013-3h10a1 1 0 01.8 1.6L14.25 8l2.55 3.4A1 1 0 0116 13H6a1 1 0 00-1 1v3a1 1 0 11-2 0V6z"
                                        clip-rule="evenodd" />
                                </svg>
                                省份分布
                            </h3>
                            <p class="text-sm text-gray-500 mt-1">各省份访问量统计</p>
                        </div>
                        <div
                            class="flex items-center space-x-2 text-sm text-gray-500 bg-blue-50 px-3 py-1.5 rounded-lg">
                            <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z" />
                            </svg>
                            <span class="text-blue-700 font-medium">区域图</span>
                        </div>
                    </div>
                    <ProvinceChart :data="filteredData" :loading="loading" :date-range="currentDateRange" />
                </div>

                <!-- 城市访问TOP10图表 -->
                <div
                    class="bg-white rounded-xl shadow-md border border-gray-100 p-6 transition-all duration-300 hover:shadow-lg">
                    <div class="flex items-center justify-between mb-6">
                        <div>
                            <h3 class="text-lg font-bold text-gray-900 flex items-center">
                                <svg class="w-5 h-5 text-emerald-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path
                                        d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z" />
                                </svg>
                                城市TOP10
                            </h3>
                            <p class="text-sm text-gray-500 mt-1">访问量前十城市</p>
                        </div>
                        <div
                            class="flex items-center space-x-2 text-sm text-gray-500 bg-emerald-50 px-3 py-1.5 rounded-lg">
                            <svg class="w-4 h-4 text-emerald-600" fill="currentColor" viewBox="0 0 20 20">
                                <path
                                    d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z" />
                            </svg>
                            <span class="text-emerald-700 font-medium">排行榜</span>
                        </div>
                    </div>
                    <CityChart :data="filteredData" :loading="loading" />
                </div>
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

        // TODO: 替换为实际的区域数据API请求
        // const response = await api.getRegionAnalysis(params)
        // return response.data

        // 模拟API请求延迟
        await new Promise(resolve => setTimeout(resolve, 500))
        return regionDayData

    } catch (error) {
        console.error('获取区域数据失败:', error)
        // TODO: 添加错误处理和用户提示
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
            code: "04jw1SM0", // TODO: 替换为实际的项目code参数
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        }

        const data = await fetchRegionData(params)
        regionData.value = data
    } catch (error) {
        console.error('刷新图表失败:', error)
        // TODO: 添加错误提示给用户
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
