<template>
    <BaseModal :model-value="modelValue" title="流量包详情" content-padding="p-0" @update:model-value="closeModal">
        <div class="p-6">
            <!-- 流量包状态卡片 -->
            <div class="mb-6 rounded-lg bg-gradient-to-r from-blue-50 to-indigo-50 p-4 border border-blue-100">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-800">{{ getLevelText(trafficData?.level) }}</h3>
                        <p class="text-sm text-gray-600 mt-1">产品ID: {{ trafficData?.productId }}</p>
                    </div>
                    <div class="text-right">
                        <div class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                            :class="getStatusClass()">
                            {{ getStatusText() }}
                        </div>
                    </div>
                </div>
            </div>

            <!-- 使用情况 -->
            <div class="mb-6">
                <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-medium text-gray-700">今日使用情况</span>
                    <span class="text-sm text-gray-600">
                        {{ trafficData?.dayUsed || 0 }} / {{ trafficData?.dayLimit || 0 }}
                    </span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2">
                    <div class="bg-gradient-to-r from-blue-500 to-indigo-600 h-2 rounded-full transition-all duration-300"
                        :style="{ width: `${getUsagePercentage()}%` }"></div>
                </div>
                <p class="text-xs text-gray-500 mt-1">
                    剩余 {{ getRemainingUsage() }} 次使用
                </p>
            </div>

            <!-- 详细信息 -->
            <div class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div class="bg-gray-50 rounded-lg p-3">
                        <div class="text-xs text-gray-500 mb-1">账户编号</div>
                        <div class="text-sm font-medium text-gray-900">{{ trafficData?.accountNo }}</div>
                    </div>
                    <div class="bg-gray-50 rounded-lg p-3">
                        <div class="text-xs text-gray-500 mb-1">订单号</div>
                        <div class="text-sm font-medium text-gray-900 truncate" :title="trafficData?.outTradeNo">
                            {{ trafficData?.outTradeNo }}
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div class="bg-gray-50 rounded-lg p-3">
                        <div class="text-xs text-gray-500 mb-1">服务类型</div>
                        <div class="text-sm font-medium text-gray-900">{{ getPluginTypeText(trafficData?.pluginType) }}
                        </div>
                    </div>
                    <div class="bg-gray-50 rounded-lg p-3">
                        <div class="text-xs text-gray-500 mb-1">到期时间</div>
                        <div class="text-sm font-medium text-gray-900">{{ formatExpiredDate(trafficData?.expiredDate) }}
                        </div>
                    </div>
                </div>

                <div v-if="trafficData?.totalLimit" class="bg-gray-50 rounded-lg p-3">
                    <div class="text-xs text-gray-500 mb-1">总量限制</div>
                    <div class="text-sm font-medium text-gray-900">{{ trafficData.totalLimit }}</div>
                </div>
            </div>

            <!-- 操作提示 -->
            <div class="mt-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                <div class="flex">
                    <svg class="h-5 w-5 text-yellow-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                            clip-rule="evenodd" />
                    </svg>
                    <div class="ml-3">
                        <h3 class="text-sm font-medium text-yellow-800">使用提醒</h3>
                        <div class="mt-1 text-sm text-yellow-700">
                            <p>流量包将在到期时间自动失效，请及时使用或续费。</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 页脚按钮 -->
        <template #separateFooter>
            <button type="button" @click="closeModal"
                class="inline-flex w-full justify-center rounded-md border border-gray-300 bg-white px-4 py-2 text-base font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 sm:ml-3 sm:w-auto sm:text-sm">
                关闭
            </button>
            <button type="button"
                class="inline-flex w-full justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-base font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 sm:w-auto sm:text-sm">
                续费
            </button>
        </template>
    </BaseModal>
</template>

<script setup lang="ts">
import BaseModal from '@/components/BaseModal.vue';

interface TrafficDetail {
    id: number;
    dayLimit: number;
    dayUsed: number;
    totalLimit: number | null;
    accountNo: number;
    outTradeNo: string;
    level: string;
    expiredDate: string;
    pluginType: string;
    productId: number;
}

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    trafficData: {
        type: Object as () => TrafficDetail | null,
        default: null
    }
});

const emit = defineEmits(['update:modelValue']);

const closeModal = () => {
    emit('update:modelValue', false);
};

// 获取等级文本
const getLevelText = (level?: string) => {
    const levelMap: Record<string, string> = {
        'FIRST': '基础版',
        'SECOND': '标准版',
        'THIRD': '专业版',
        'FOURTH': '企业版'
    };
    return levelMap[level || ''] || level || '未知';
};

// 获取插件类型文本
const getPluginTypeText = (pluginType?: string) => {
    const typeMap: Record<string, string> = {
        'SHORT_LINK': '短链接服务',
        'QR_CODE': '二维码服务',
        'ANALYTICS': '数据分析服务'
    };
    return typeMap[pluginType || ''] || pluginType || '未知';
};

// 格式化到期时间
const formatExpiredDate = (dateString?: string) => {
    if (!dateString) return '未知';
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch {
        return '格式错误';
    }
};

// 计算使用百分比
const getUsagePercentage = () => {
    if (!props.trafficData?.dayLimit) return 0;
    const percentage = (props.trafficData.dayUsed / props.trafficData.dayLimit) * 100;
    return Math.min(percentage, 100);
};

// 获取剩余使用次数
const getRemainingUsage = () => {
    if (!props.trafficData) return 0;
    return Math.max(0, props.trafficData.dayLimit - props.trafficData.dayUsed);
};

// 获取状态样式类
const getStatusClass = () => {
    if (!props.trafficData) return 'bg-gray-100 text-gray-800';

    const now = new Date();
    const expiredDate = new Date(props.trafficData.expiredDate);

    if (expiredDate <= now) {
        return 'bg-red-100 text-red-800';
    }

    if (props.trafficData.dayUsed >= props.trafficData.dayLimit) {
        return 'bg-yellow-100 text-yellow-800';
    }

    return 'bg-green-100 text-green-800';
};

// 获取状态文本
const getStatusText = () => {
    if (!props.trafficData) return '未知';

    const now = new Date();
    const expiredDate = new Date(props.trafficData.expiredDate);

    if (expiredDate <= now) {
        return '已过期';
    }

    if (props.trafficData.dayUsed >= props.trafficData.dayLimit) {
        return '已用完';
    }

    return '正常';
};
</script>

<style scoped>
/* 自定义样式 */
.truncate {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
