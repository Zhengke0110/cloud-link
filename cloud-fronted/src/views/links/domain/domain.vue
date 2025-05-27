<template>
    <PageLayout tag="域名管理" title="查看您的域名配置" description="管理和查看您的短链接域名设置">
        <!-- 域名列表 -->
        <div class="mx-auto max-w-6xl delay-300">
            <!-- 加载状态 -->
            <div v-if="loading" class="flex items-center justify-center py-12">
                <div class="flex items-center space-x-2">
                    <div class="h-4 w-4 animate-spin rounded-full border-2 border-blue-600 border-t-transparent"></div>
                    <span class="text-gray-600">正在加载域名数据...</span>
                </div>
            </div>

            <!-- 错误状态 -->
            <div v-else-if="error" class="flex items-center justify-center py-12">
                <div class="text-center">
                    <div class="mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="mx-auto h-12 w-12 text-red-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z" />
                        </svg>
                    </div>
                    <h3 class="text-lg font-medium text-gray-900 mb-2">加载失败</h3>
                    <p class="text-gray-600 mb-4">获取域名数据时出现错误</p>
                    <button @click="fetchDomainList"
                        class="rounded-lg bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 transition-colors">
                        重试
                    </button>
                </div>
            </div>

            <!-- 域名卡片 - 仅在数据加载完成且有数据时显示 -->
            <div v-else-if="domainData.length > 0" class="space-y-4">
                <LinkCard v-for="(domain, index) in domainData" :key="domain.id" :title="domain.value"
                    :colorIndex="getDomainColorIndex(domain, index)">
                    <!-- 顶部操作按钮 -->
                    <template #header-actions>
                        <span class="text-sm text-white/80">域名信息</span>
                    </template>

                    <!-- 域名内容 -->
                    <div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                        <!-- 域名值 -->
                        <InfoField label="域名" :value="domain.value" valueClass="text-indigo-600 font-semibold"
                            borderClass="border-indigo-100" bgClass="bg-indigo-50">
                            <div class="flex items-center justify-between w-full">
                                <span>{{ domain.value }}</span>
                                <IconActionButton icon="copy" variant="light" size="sm" title="复制域名"
                                    customClass="text-indigo-600 hover:bg-indigo-100" />
                            </div>
                        </InfoField>

                        <!-- 域名类型 -->
                        <InfoField label="域名类型" :value="getDomainTypeText(domain.domainType)"
                            :borderClass="domain.domainType === 'OFFICIAL' ? 'border-emerald-100' : 'border-blue-100'"
                            :bgClass="domain.domainType === 'OFFICIAL' ? 'bg-emerald-50' : 'bg-blue-50'"
                            :valueClass="domain.domainType === 'OFFICIAL' ? 'text-emerald-600' : 'text-blue-600'">
                            <template #icon>
                                <span class="h-2 w-2 rounded-full"
                                    :class="domain.domainType === 'OFFICIAL' ? 'bg-emerald-500' : 'bg-blue-500'"></span>
                            </template>
                        </InfoField>
                    </div>

                    <div class="mb-4 grid grid-cols-1 gap-4 sm:grid-cols-2">
                        <!-- 账户编号 -->
                        <InfoField label="账户编号" :value="domain.accountNo || '系统默认'"
                            :valueClass="domain.accountNo ? 'text-gray-800' : 'text-gray-500'" icon="user" />

                        <!-- 状态 -->
                        <InfoField label="状态" :value="getStatusText(domain.del)"
                            :borderClass="domain.del === 0 ? 'border-emerald-100' : 'border-red-100'"
                            :bgClass="domain.del === 0 ? 'bg-emerald-50' : 'bg-red-50'"
                            :valueClass="domain.del === 0 ? 'text-emerald-600' : 'text-red-600'">
                            <template #icon>
                                <span class="h-2 w-2 rounded-full"
                                    :class="domain.del === 0 ? 'bg-emerald-500' : 'bg-red-500'"></span>
                            </template>
                        </InfoField>
                    </div>

                    <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                        <!-- 创建时间 -->
                        <InfoField label="创建时间" :value="formatDate(domain.gmtCreate)" icon="clock"
                            :iconClass="getIconColor(getDomainColorIndex(domain, index), 0)" />

                        <!-- 修改时间 -->
                        <InfoField label="修改时间" :value="formatDate(domain.gmtModified)" icon="update"
                            :iconClass="getIconColor(getDomainColorIndex(domain, index), 1)" />
                    </div>

                    <!-- 底部操作按钮 -->
                    <template #footer-actions>

                        <button
                            class="flex items-center gap-1.5 rounded-lg px-4 py-2 text-sm text-white transition-colors duration-300"
                            :class="getActionButtonBg(getDomainColorIndex(domain, index))">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path
                                    d="M11 3a1 1 0 100 2h2.586l-6.293 6.293a1 1 0 101.414 1.414L15 6.414V9a1 1 0 102 0V4a1 1 0 00-1-1h-5z" />
                                <path
                                    d="M5 5a2 2 0 00-2 2v8a2 2 0 002 2h8a2 2 0 002-2v-3a1 1 0 10-2 0v3H5V7h3a1 1 0 000-2H5z" />
                            </svg>
                            创建短链
                        </button>
                    </template>
                </LinkCard>
            </div>

            <!-- 无域名数据提示 - 仅在数据加载完成但无数据时显示 -->
            <EmptyState v-else title="暂无域名数据" description="当前没有配置任何域名，请联系管理员添加域名" iconType="blue">
                <template #icon>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M3 5a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2h-2.22l.123.489.804.804A1 1 0 0113 18H7a1 1 0 01-.707-1.707l.804-.804L7.22 15H5a2 2 0 01-2-2V5zm5.771 7H9a1 1 0 110 2H8.771l-.123-.489L8.52 13h2.96l-.128.511-.123.489zM11 6a1 1 0 11-2 0 1 1 0 012 0z"
                            clip-rule="evenodd" />
                    </svg>
                </template>
            </EmptyState>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">

import LinkCard from '../components/LinkCard.vue';
import EmptyState from '@/components/EmptyState.vue';
import InfoField from '@/components/Form/InfoField.vue';
import PageLayout from '@/components/PageLayout.vue';
import IconActionButton from '../components/IconActionButton.vue';
import { DomainGetListsApi } from '@/services/links'

// 导入颜色方案工具
import {
    getIconColor,
    getActionButtonBg,
    getLinkColorIndex
} from "@/utils/ColorSchemeProvider";
import { formatDate } from '@/utils/DateUtils';
import { onMounted, ref } from 'vue';

// 添加响应式数据
const domainData = ref<any[]>([]);
const loading = ref<boolean>(false);
const error = ref<any>(null);

onMounted(async () => {
    await fetchDomainList();
});

const fetchDomainList = async () => {
    try {
        loading.value = true;
        error.value = null; // 重置错误状态
        const response = await DomainGetListsApi()
        domainData.value = response || []; // 确保数据是数组
    } catch (err) {
        error.value = err;
        console.error('获取域名列表失败:', err);
        domainData.value = []; // 错误时重置为空数组
    } finally {
        loading.value = false;
    }
};

// 获取域名颜色索引
const getDomainColorIndex = (domain: any, index: number) => {
    return getLinkColorIndex(domain, index);
};

// 获取域名类型文本
const getDomainTypeText = (domainType: string) => {
    switch (domainType) {
        case 'OFFICIAL':
            return '官方域名';
        case 'CUSTOM':
            return '自定义域名';
        default:
            return domainType;
    }
};

// 获取状态文本
const getStatusText = (del: number) => {
    return del === 0 ? '正常' : '已删除';
};
</script>
