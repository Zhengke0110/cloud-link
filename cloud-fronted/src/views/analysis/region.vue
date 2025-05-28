<template>
    <!-- 日期选择器 -->
    <div class="mx-auto mb-8 max-w-6xl">
        <DateRangeSelector :loading="loading" @refresh="handleRefresh" @date-change="handleDateChange"
            ref="dateRangeSelectorRef" />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading && !regionData.length" class="flex justify-center items-center py-20">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
        <span class="ml-2 text-gray-600">数据加载中...</span>
    </div>

    <!-- 数据加载完成后显示图表 -->
    <div v-else-if="regionData.length > 0" class="mx-auto max-w-6xl" ref="chartsContainer">
        <!-- 数据刷新中的提示 -->
        <div v-if="loading" class="mb-4 p-2 bg-gray-50 border border-gray-200 rounded-lg">
            <div class="flex items-center">
                <div class="animate-spin w-4 h-4 border-2 border-blue-500 border-t-transparent rounded-full mr-2">
                </div>
                <span class="text-sm text-gray-600">正在更新数据...</span>
            </div>
        </div>

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
                <ProvinceChart ref="provinceChartRef" :data="filteredData" :loading="loading"
                    :date-range="currentDateRange" />
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
                <CityChart ref="cityChartRef" :data="filteredData" :loading="loading" />
            </div>
        </div>
    </div>

    <!-- 数据加载失败或无数据提示 -->
    <div v-else class="mx-auto max-w-5xl">
        <EmptyState :title="error ? '数据加载失败' : '暂无访问数据'"
            :description="error ? '获取区域分析数据时出现问题，请检查网络连接或稍后重试' : '当前时间范围内没有找到访问数据，请尝试调整日期范围或检查是否有访问记录'"
            :icon-type="error ? 'red' : 'blue'">
            <template #icon>
                <svg v-if="error" class="w-8 h-8 text-red-600" fill="none" stroke="currentColor"
                    viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z" />
                </svg>
                <svg v-else class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                        d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
            </template>
            <template #action>
                <div class="flex flex-col sm:flex-row gap-3 justify-center items-center">
                    <button @click="retryFetch" :disabled="loading"
                        class="inline-flex items-center px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-200 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                        <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none"
                            viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor"
                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                            </path>
                        </svg>
                        <svg v-else class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                        </svg>
                        {{ loading ? '重新加载中...' : '重新加载' }}
                    </button>
                    <button v-if="!error" @click="adjustDateRange"
                        class="inline-flex items-center px-6 py-3 bg-white text-gray-700 font-medium rounded-lg hover:bg-gray-50 focus:ring-4 focus:ring-gray-100 transition-all duration-200 border border-gray-200">
                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        调整日期范围
                    </button>
                </div>
            </template>
        </EmptyState>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useDebounceFn, useResizeObserver, useThrottleFn } from '@vueuse/core'
import DateRangeSelector from './components/DateRangeSelector.vue'
import ProvinceChart from './components/region/ProvinceChart.vue'
import CityChart from './components/region/CityChart.vue'
import EmptyState from '@/components/EmptyState.vue'
import { DataRegionDayApi } from '@/services/data'

// 定义 Props
interface Props {
    code: string
}

const props = defineProps<Props>()

const loading = ref<boolean>(false)
const regionData = ref<any[]>([])
const error = ref<string>('')
const currentDateRange = ref<{ startTime: string, endTime: string }>({
    startTime: '',
    endTime: ''
})
const dateRangeSelectorRef = ref()

// 图表容器和组件引用
const chartsContainer = ref()
const provinceChartRef = ref()
const cityChartRef = ref()

// 获取区域数据的API接口
const fetchRegionData = async (params: {
    code: string
    startTime: string
    endTime: string
}) => {
    try {
        loading.value = true
        error.value = ''
        const response = await DataRegionDayApi(params)
        return response || []
    } catch (err: any) {
        console.error('获取区域数据失败:', err)
        error.value = err.message || '获取数据失败'
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

// 节流处理图表重绘
const throttledResize = useThrottleFn(() => {
    // 调用图表组件的 resize 方法，假设图表组件暴露了 resize 方法
    if (provinceChartRef.value?.resize) {
        provinceChartRef.value.resize()
    }
    if (cityChartRef.value?.resize) {
        cityChartRef.value.resize()
    }
}, 200)

// 监听图表容器尺寸变化
useResizeObserver(chartsContainer, throttledResize)

// 防抖处理日期变化
const debouncedDateChange = useDebounceFn((dateRange: { startTime: string, endTime: string }) => {
    console.log('防抖处理日期变化:', dateRange)
    currentDateRange.value = dateRange
    // 可以在这里触发数据更新，但由于已有 handleRefresh，这里主要用于状态更新
}, 500)

// 防抖处理数据刷新
const debouncedRefresh = useDebounceFn(async (dateRange: { startTime: string, endTime: string }) => {
    currentDateRange.value = dateRange

    try {
        const params = {
            code: props.code,
            startTime: dateRange.startTime,
            endTime: dateRange.endTime
        }

        const data = await fetchRegionData(params)
        regionData.value = data
    } catch (err) {
        console.error('刷新图表失败:', err)
        error.value = '刷新数据失败'
    }
}, 800)

// 处理日期变化 - 使用防抖
const handleDateChange = (dateRange: { startTime: string, endTime: string }) => {
    debouncedDateChange(dateRange)
}

// 刷新图表数据 - 使用防抖
const handleRefresh = async (dateRange: { startTime: string, endTime: string }) => {
    debouncedRefresh(dateRange)
}

// 重新获取数据
const retryFetch = async () => {
    if (dateRangeSelectorRef.value) {
        const dateRange = dateRangeSelectorRef.value.getDateRange()
        await handleRefresh(dateRange)
    }
}

// 调整日期范围
const adjustDateRange = () => {
    if (dateRangeSelectorRef.value) {
        const today = new Date()
        const thirtyDaysAgo = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000)

        const newDateRange = {
            startTime: thirtyDaysAgo.toISOString().split('T')[0].replace(/-/g, ''),
            endTime: today.toISOString().split('T')[0].replace(/-/g, '')
        }

        handleRefresh(newDateRange)
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
    setTimeout(initData, 100)
})
</script>
