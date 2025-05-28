<template>
    <div class="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div class="mx-auto max-w-6xl px-4 sm:px-6 lg:px-8">
            <!-- 页面标题区域 -->
            <div class="py-6 border-b border-gray-100">
                <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between">
                    <div>
                        <h1 class="text-2xl font-bold text-gray-900 flex items-center">
                            <svg class="w-6 h-6 text-blue-600 mr-3" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z" />
                                <path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z" />
                            </svg>
                            数据分析
                        </h1>
                        <p class="mt-2 text-sm text-gray-600">
                            {{ currentTab.description }}
                        </p>
                    </div>
                    <div class="mt-4 sm:mt-0">
                        <div class="flex items-center space-x-2 text-sm text-gray-500 bg-blue-50 px-3 py-1.5 rounded-lg">
                            <component :is="currentTab.icon" class="w-4 h-4 text-blue-600" />
                            <span class="text-blue-700 font-medium">{{ currentTab.tag }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 选项卡导航 -->
            <div class="py-4">
                <nav class="flex space-x-1" aria-label="Tabs">
                    <button
                        v-for="tab in tabs"
                        :key="tab.key"
                        @click="handleTabChange(tab.key)"
                        :class="[
                            'flex items-center px-4 py-2.5 text-sm font-medium rounded-lg transition-all duration-200',
                            activeTab === tab.key
                                ? 'bg-blue-100 text-blue-700 border border-blue-200'
                                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50 border border-transparent'
                        ]"
                        :aria-current="activeTab === tab.key ? 'page' : undefined"
                    >
                        <component :is="tab.icon" 
                            :class="[
                                'w-4 h-4 mr-2',
                                activeTab === tab.key ? 'text-blue-600' : 'text-gray-400'
                            ]" 
                        />
                        {{ tab.name }}
                        <span v-if="activeTab === tab.key" 
                            class="ml-2 inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-blue-200 text-blue-800">
                            当前
                        </span>
                    </button>
                </nav>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, h } from 'vue'

// 定义选项卡接口
interface Tab {
    key: string
    name: string
    tag: string
    description: string
    icon: any
}

// 定义props
interface Props {
    activeTab: string
}

// 定义emits
interface Emits {
    (e: 'tab-change', tab: string): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 图标组件
const TrendIcon = () => h('svg', {
    fill: 'currentColor',
    viewBox: '0 0 20 20'
}, [
    h('path', {
        fillRule: 'evenodd',
        d: 'M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z',
        clipRule: 'evenodd'
    })
])

const RegionIcon = () => h('svg', {
    fill: 'currentColor',
    viewBox: '0 0 20 20'
}, [
    h('path', {
        fillRule: 'evenodd',
        d: 'M3 6a3 3 0 013-3h10a1 1 0 01.8 1.6L14.25 8l2.55 3.4A1 1 0 0116 13H6a1 1 0 00-1 1v3a1 1 0 11-2 0V6z',
        clipRule: 'evenodd'
    })
])

const RecordIcon = () => h('svg', {
    fill: 'none',
    stroke: 'currentColor',
    viewBox: '0 0 24 24'
}, [
    h('path', {
        strokeLinecap: 'round',
        strokeLinejoin: 'round',
        strokeWidth: '2',
        d: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z'
    })
])

const FrequentIcon = () => h('svg', {
    fill: 'currentColor',
    viewBox: '0 0 20 20'
}, [
    h('path', {
        fillRule: 'evenodd',
        d: 'M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z',
        clipRule: 'evenodd'
    })
])

const DeviceIcon = () => h('svg', {
    fill: 'none',
    stroke: 'currentColor',
    viewBox: '0 0 24 24'
}, [
    h('path', {
        strokeLinecap: 'round',
        strokeLinejoin: 'round',
        strokeWidth: '2',
        d: 'M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z'
    })
])

// 选项卡配置
const tabs: Tab[] = [
    {
        key: 'trend',
        name: '访问趋势',
        tag: '趋势分析',
        description: '查看您的短链接访问趋势，了解流量变化规律',
        icon: TrendIcon
    },
    {
        key: 'region',
        name: '地区分布',
        tag: '区域分析',
        description: '查看不同省份和城市的访问量分布情况',
        icon: RegionIcon
    },
    {
        key: 'record',
        name: '访问记录',
        tag: '数据分析',
        description: '深入了解您的短链接访问情况，包括用户设备、地域分布、访问时间等详细数据',
        icon: RecordIcon
    },
    {
        key: 'frequent',
        name: '访问来源',
        tag: '数据分析',
        description: '分析您的短链接访问来源分布，了解流量入口',
        icon: FrequentIcon
    },
    {
        key: 'device',
        name: '设备分析',
        tag: '设备分析',
        description: '分析访问用户的设备类型、操作系统和浏览器分布情况',
        icon: DeviceIcon
    }
]

// 当前选中的选项卡信息
const currentTab = computed(() => {
    return tabs.find(tab => tab.key === props.activeTab) || tabs[0]
})

// 处理选项卡变化
const handleTabChange = (tab: string) => {
    emit('tab-change', tab)
}
</script>
