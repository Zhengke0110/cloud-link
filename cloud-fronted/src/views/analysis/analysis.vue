<template>
    <div class="min-h-screen bg-gray-50">
        <!-- Header选择器 -->
        <AnalysisHeader :active-tab="activeTab" @tab-change="handleTabChange" />

        <!-- 动态组件内容区域 -->
        <div class="pb-8">
            <component :is="currentComponent" :key="activeTab" :code="code" />
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AnalysisHeader from './components/AnalysisHeader.vue'

// 动态导入组件
import TrendPage from './trend.vue'
import RegionPage from './region.vue'
import RecordPage from './record.vue'
import FrequentPage from './frequent.vue'
import DevicePage from './device.vue'

const route = useRoute()
const router = useRouter()

// 从路由参数获取code
const code = computed(() => route.query.code as string)

// 当前激活的选项卡
const activeTab = ref<string>('trend')

// 组件映射
const componentMap = {
    trend: TrendPage,
    region: RegionPage,
    record: RecordPage,
    frequent: FrequentPage,
    device: DevicePage
}

// 当前显示的组件
const currentComponent = computed(() => {
    return componentMap[activeTab.value as keyof typeof componentMap] || TrendPage
})

// 处理选项卡切换
const handleTabChange = (tab: string) => {
    activeTab.value = tab

    // 更新URL参数
    router.push({
        query: {
            ...route.query,
            tab
        }
    })
}

// 监听路由变化，同步选项卡状态
watch(
    () => route.query.tab,
    (newTab) => {
        if (newTab && typeof newTab === 'string' && componentMap[newTab as keyof typeof componentMap]) {
            activeTab.value = newTab
        }
    },
    { immediate: true }
)

// 初始化时从URL获取选项卡状态
if (route.query.tab && typeof route.query.tab === 'string') {
    const tabFromQuery = route.query.tab
    if (componentMap[tabFromQuery as keyof typeof componentMap]) {
        activeTab.value = tabFromQuery
    }
}

console.log('code', code.value)
</script>
