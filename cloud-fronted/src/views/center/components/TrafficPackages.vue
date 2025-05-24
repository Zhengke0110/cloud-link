<template>
    <!-- 移除最外层的reveal-element和delay-500类，由PageLayout统一管理 -->
    <div>
        <div class="flex items-center justify-between mb-6">
            <h3 class="text-xl font-bold text-gray-800 flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-600" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
                流量包信息
            </h3>
            <span class="badge-glow px-3 py-1 rounded-full text-xs font-medium 
                bg-indigo-100 text-indigo-800 shadow-sm">总共 {{ trafficData.total_record }} 个流量包</span>
        </div>

        <div class="grid grid-cols-1 gap-4">
            <div v-for="(traffic, index) in trafficData.current_data" :key="traffic.id"
                class="traffic-card group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-6 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">

                <!-- 卡片装饰效果 -->
                <div
                    class="card-decoration absolute inset-0 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                    <div
                        class="absolute inset-x-0 top-0 h-1.5 bg-gradient-to-r from-purple-500 to-indigo-500 rounded-t-xl">
                    </div>
                    <div
                        class="absolute inset-y-0 right-0 w-1.5 bg-gradient-to-b from-indigo-500 to-purple-500 rounded-r-xl">
                    </div>
                    <div
                        class="absolute inset-x-0 bottom-0 h-1.5 bg-gradient-to-r from-indigo-500 to-purple-500 rounded-b-xl">
                    </div>
                    <div
                        class="absolute inset-y-0 left-0 w-1.5 bg-gradient-to-b from-purple-500 to-indigo-500 rounded-l-xl">
                    </div>
                </div>

                <div class="relative z-10">
                    <div class="flex items-center justify-between mb-4">
                        <div class="flex items-center gap-3">
                            <div
                                class="flex h-10 w-10 items-center justify-center rounded-full bg-purple-50 text-purple-500 transition-all duration-300 group-hover:bg-purple-100 group-hover:text-purple-600 group-hover:shadow-sm">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M13 10V3L4 14h7v7l9-11h-7z" />
                                </svg>
                            </div>
                            <div>
                                <h4
                                    class="text-lg font-semibold text-gray-900 transition-all duration-300 group-hover:text-purple-700">
                                    {{ traffic.level }} 级流量包
                                </h4>
                                <div class="text-xs text-gray-500">产品ID: {{ traffic.productId }}</div>
                            </div>
                        </div>
                        <span
                            class="text-xs bg-purple-50 text-purple-700 px-2 py-1 rounded-full border border-purple-100">
                            {{ traffic.pluginType }}
                        </span>
                    </div>

                    <div class="grid grid-cols-2 gap-6 mt-4">
                        <!-- 每日用量 -->
                        <div class="relative">
                            <div class="flex justify-between items-center mb-2">
                                <span class="text-xs text-gray-500">今日用量</span>
                                <span class="text-xs font-medium text-indigo-600">{{ traffic.dayUsed }}/{{
                                    traffic.dayLimit }}</span>
                            </div>
                            <div class="h-2 w-full bg-gray-100 rounded-full overflow-hidden">
                                <div class="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full"
                                    :style="`width: ${(traffic.dayUsed / traffic.dayLimit) * 100}%`"></div>
                            </div>
                        </div>

                        <!-- 截止日期 -->
                        <div class="flex items-center gap-2">
                            <div
                                class="flex h-8 w-8 items-center justify-center rounded-full bg-indigo-50 text-indigo-500">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                </svg>
                            </div>
                            <div>
                                <div class="text-xs text-gray-500">到期日期</div>
                                <div class="text-sm font-medium text-gray-800">{{
                                    formatExpiryDate(traffic.expiredDate) }}</div>
                            </div>
                        </div>
                    </div>

                    <div class="mt-6 pt-4 border-t border-gray-100">
                        <div class="flex items-center justify-between text-sm">
                            <div class="flex items-center gap-2 text-gray-500">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                                </svg>
                                <span>订单号: {{ traffic.outTradeNo.substring(0, 8) }}...</span>
                            </div>
                            <button
                                class="text-indigo-600 hover:text-indigo-800 transition-colors duration-300 flex items-center gap-1 group-hover:underline"
                                @click="$emit('view-details', traffic.id)">
                                查看详情
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 5l7 7-7 7" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
// 定义流量包数据接口
interface Traffic {
    id: string | number;
    level: string;
    productId: string | number;
    pluginType: string;
    dayUsed: number;
    dayLimit: number;
    expiredDate: string;
    outTradeNo: string;
    [key: string]: any;
}

interface TrafficDataType {
    total_record: number;
    current_data: Traffic[];
}

// 定义组件属性
defineProps<{
    trafficData: TrafficDataType;
}>();

// 定义事件
defineEmits<{
    'view-details': [id: string | number];
}>();

// 格式化日期
const formatExpiryDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' });
};
</script>

<style scoped>
/* 卡片效果增强 */
.traffic-card {
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    background: linear-gradient(to bottom right, rgba(255, 255, 255, 1), rgba(249, 250, 251, 0.8));
}

.traffic-card:hover {
    box-shadow: 0 10px 20px -3px rgba(124, 58, 237, 0.12), 0 4px 8px -2px rgba(124, 58, 237, 0.06);
    border-color: rgba(124, 58, 237, 0.2);
    background: linear-gradient(to bottom right, rgba(255, 255, 255, 1), rgba(250, 245, 255, 0.9));
}

/* 徽章闪光效果 */
.badge-glow {
    position: relative;
    overflow: hidden;
    transition: all 0.3s ease;
}

.badge-glow:hover {
    transform: translateY(-2px);
}

.badge-glow::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: linear-gradient(to right,
            transparent,
            rgba(255, 255, 255, 0.3),
            transparent);
    transform: rotate(30deg);
    animation: shimmer 3s infinite;
    pointer-events: none;
}

@keyframes shimmer {
    0% {
        transform: translateX(-150%) rotate(30deg);
    }

    100% {
        transform: translateX(150%) rotate(30deg);
    }
}

/* 卡片装饰样式 */
.card-decoration {
    z-index: 1;
}

/* 元素显示动画 */
.reveal-element {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-element.delay-500 {
    transition-delay: 0.5s;
}

.reveal-element.revealed {
    opacity: 1;
    transform: translateY(0);
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {
    .badge-glow::before {
        animation: none !important;
        transition: none !important;
    }

    .reveal-element {
        opacity: 1;
        transform: none;
    }
}
</style>