<template>
    <div class="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden">
        <div class="px-6 py-5 border-b border-gray-200 bg-gradient-to-r from-gray-50 to-gray-100/50">
            <div class="flex items-center justify-between">
                <div>
                    <h3 class="text-xl font-bold text-gray-900 flex items-center">
                        <svg class="w-5 h-5 text-blue-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M3 3a1 1 0 000 2v8a2 2 0 002 2h2.586l-1.293 1.293a1 1 0 101.414 1.414L10 15.414l2.293 2.293a1 1 0 001.414-1.414L12.414 15H15a2 2 0 002-2V5a1 1 0 100-2H3zm11.707 4.707a1 1 0 00-1.414-1.414L10 9.586 8.707 8.293a1 1 0 00-1.414 0l-2 2a1 1 0 101.414 1.414L8 10.414l1.293 1.293a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                        </svg>
                        详细数据
                    </h3>
                    <p class="text-sm text-gray-600 mt-1">访问来源详细统计信息</p>
                </div>
                <div class="flex items-center space-x-3">
                    <div class="bg-white px-4 py-2 rounded-lg border border-gray-200 text-sm text-gray-700 shadow-sm">
                        <span class="text-gray-500">共</span>
                        <span class="font-semibold text-blue-600 mx-1">{{ data.length }}</span>
                        <span class="text-gray-500">条记录</span>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="loading" class="flex items-center justify-center py-12">
            <div class="flex items-center space-x-3 text-gray-500">
                <svg class="animate-spin w-6 h-6" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none"/>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
                </svg>
                <span>正在加载数据...</span>
            </div>
        </div>

        <div v-else-if="!data.length" class="py-12">
            <EmptyState title="暂无数据" description="当前时间范围内没有访问来源数据" iconType="gray">
                <template #icon>
                    <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
                    </svg>
                </template>
            </EmptyState>
        </div>

        <div v-else class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zM3 10a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H4a1 1 0 01-1-1v-6zM14 9a1 1 0 00-1 1v6a1 1 0 001 1h2a1 1 0 001-1v-6a1 1 0 00-1-1h-2z"/>
                                </svg>
                                排名
                            </div>
                        </th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path fill-rule="evenodd" d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0l-3-3a2 2 0 012.828-2.828L10 3.172l1.586-1.586zM10 6a2 2 0 100 4 2 2 0 000-4zm0 6a2 2 0 100 4 2 2 0 000-4z" clip-rule="evenodd"/>
                                </svg>
                                来源网站
                            </div>
                        </th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z"/>
                                </svg>
                                访问量
                            </div>
                        </th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z"/>
                                    <path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z"/>
                                </svg>
                                占比
                            </div>
                        </th>
                        <th class="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                            <div class="flex items-center">
                                <svg class="w-4 h-4 text-gray-400 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                                </svg>
                                状态
                            </div>
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="(item, index) in data" :key="index" class="hover:bg-gray-50 transition-colors duration-200 group">
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="flex items-center">
                                <span class="inline-flex items-center justify-center w-9 h-9 text-sm font-bold text-white rounded-full transition-all duration-200 shadow-sm group-hover:shadow-md" :class="getRankColor(index)">
                                    {{ index + 1 }}
                                </span>
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="flex items-center">
                                <div class="flex-shrink-0 w-11 h-11">
                                    <div class="w-11 h-11 rounded-xl flex items-center justify-center shadow-sm border-2 border-white transition-all duration-200 hover:shadow-md group-hover:scale-105" :style="{ backgroundColor: getSourceColor(item.referer) }">
                                        <span class="text-white text-sm font-bold">
                                            {{ getSourceInitial(item.referer) }}
                                        </span>
                                    </div>
                                </div>
                                <div class="ml-4">
                                    <div class="text-sm font-semibold text-gray-900">
                                        {{ formatReferer(item.referer) }}
                                    </div>
                                    <div class="text-xs text-gray-500 mt-0.5 flex items-center">
                                        <svg class="w-3 h-3 text-gray-400 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0l-3-3a2 2 0 012.828-2.828L10 3.172l1.586-1.586z" clip-rule="evenodd"/>
                                        </svg>
                                        {{ item.referer || '直接访问' }}
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="text-lg font-bold text-gray-900">{{ item.pvCount.toLocaleString() }}</div>
                            <div class="text-xs text-gray-500 flex items-center mt-1">
                                <svg class="w-3 h-3 text-gray-400 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                                PV
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="flex items-center space-x-3">
                                <div class="flex-1">
                                    <div class="w-24 bg-gray-200 rounded-full h-2.5 overflow-hidden">
                                        <div class="h-2.5 rounded-full transition-all duration-700 ease-out" :style="{ width: (item.pvCount / totalPV * 100) + '%', backgroundColor: getSourceColor(item.referer) }"></div>
                                    </div>
                                </div>
                                <span class="text-sm font-bold text-gray-900 min-w-0">
                                    {{ ((item.pvCount / totalPV) * 100).toFixed(1) }}%
                                </span>
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <span class="inline-flex items-center px-3 py-1.5 text-xs font-semibold rounded-full transition-all duration-200 group-hover:shadow-sm" :class="getStatusClass(item.pvCount)">
                                <span class="w-2 h-2 rounded-full mr-2 animate-pulse" :class="getStatusDotClass(item.pvCount)"></span>
                                {{ getStatusText(item.pvCount) }}
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import EmptyState from '@/components/EmptyState.vue';

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

// 总访问量
const totalPV = computed(() => {
    return props.data.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

// 格式化来源网站名称
const formatReferer = (referer: string | null): string => {
    if (!referer) return '直接访问';
    return referer.replace(/\.com$/, '').replace(/\.net$/, '').replace(/\.cn$/, '');
};

// 获取来源首字母
const getSourceInitial = (referer: string | null): string => {
    if (!referer) return 'D';
    const name = formatReferer(referer);
    return name.charAt(0).toUpperCase();
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

// 获取排名颜色
const getRankColor = (index: number): string => {
    if (index === 0) return 'bg-yellow-500';
    if (index === 1) return 'bg-gray-400';
    if (index === 2) return 'bg-orange-600';
    return 'bg-blue-500';
};

// 获取状态样式
const getStatusClass = (pvCount: number): string => {
    if (pvCount >= 20) return 'bg-red-100 text-red-800';
    if (pvCount >= 10) return 'bg-yellow-100 text-yellow-800';
    if (pvCount >= 5) return 'bg-green-100 text-green-800';
    return 'bg-gray-100 text-gray-800';
};

// 获取状态文本
const getStatusText = (pvCount: number): string => {
    if (pvCount >= 20) return '热门';
    if (pvCount >= 10) return '活跃';
    if (pvCount >= 5) return '正常';
    return '较少';
};

// 获取状态点样式类
const getStatusDotClass = (pvCount: number): string => {
    if (pvCount >= 20) return 'bg-red-500';
    if (pvCount >= 10) return 'bg-yellow-500';
    if (pvCount >= 5) return 'bg-emerald-500';
    return 'bg-gray-400';
};
</script>
