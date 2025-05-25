<template>
    <div>
        <!-- 标题和统计部分 -->
        <GsapAnimation animation="fadeInDown" :duration="0.7">
            <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-8 gap-4">
                <div class="flex items-center gap-3">
                    <div
                        class="flex h-12 w-12 items-center justify-center rounded-full bg-gradient-to-r from-indigo-500 to-purple-500 shadow-md shadow-indigo-200/30 text-white">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 10V3L4 14h7v7l9-11h-7z" />
                        </svg>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-gray-800">流量包信息</h3>
                        <p class="text-sm text-gray-500 mt-0.5">管理和监控您的流量使用情况</p>
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <span
                        class="inline-flex items-center rounded-full bg-indigo-50 px-3 py-1 text-sm font-medium text-indigo-800 ring-1 ring-inset ring-indigo-200/30">
                        <span class="flex h-1.5 w-1.5 rounded-full bg-indigo-500 mr-1.5"></span>
                        总共 {{ trafficData.total_record }} 个流量包
                    </span>
                </div>
            </div>
        </GsapAnimation>

        <!-- 流量包卡片容器 - 单列布局 -->
        <div class="space-y-5">
            <!-- 循环渲染流量包卡片 -->
            <GsapAnimation v-for="(traffic, index) in trafficData.current_data" :key="traffic.id" animation="fadeInUp"
                :delay="0.1 * index" :duration="0.6">
                <div class="group relative bg-white border rounded-xl shadow-sm transition-all duration-200 overflow-hidden"
                    :class="getCardBorderClass(traffic)">

                    <!-- 顶部彩色条纹 - 减小高度 -->
                    <div class="absolute inset-x-0 top-0 h-0.5 rounded-t-xl" :class="getStatusBarClass(traffic)"></div>

                    <!-- 卡片主体内容 -->
                    <div class="p-5 sm:p-6">
                        <div class="flex flex-col md:flex-row gap-4 md:gap-6">
                            <!-- 左侧内容区域 -->
                            <div class="flex-grow space-y-5">
                                <!-- 顶部标题和级别信息 -->
                                <div class="flex items-center gap-3">
                                    <!-- 流量包级别 -->
                                    <GsapAnimation animation="scale" :delay="0.2 + 0.1 * index" ::duration="0.5">
                                        <div class="flex-shrink-0 h-14 w-14 flex items-center justify-center rounded-lg text-white font-bold text-xl shadow-sm"
                                            :class="getLevelBgClass(traffic)">
                                            {{ traffic.level }}
                                        </div>
                                    </GsapAnimation>

                                    <div class="flex-grow">
                                        <!-- 标题和分类信息 -->
                                        <h4 class="font-semibold text-base text-gray-800">
                                            {{ traffic.level }} 级流量包
                                            <span
                                                class="inline-flex items-center ml-2 px-2 py-0.5 rounded-full text-xs font-medium"
                                                :class="getStatusBadgeClass(traffic)">
                                                {{ getStatusText(traffic) }}
                                            </span>
                                        </h4>

                                        <!-- 产品ID和订单号 - 使用@vueuse/core优化复制功能 -->
                                        <div class="flex flex-wrap items-center mt-1 text-xs text-gray-500 gap-2">
                                            <span class="inline-flex items-center px-2 py-0.5 rounded bg-gray-50">
                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                    class="h-3 w-3 mr-1 text-gray-400" fill="none" viewBox="0 0 24 24"
                                                    stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
                                                </svg>
                                                产品ID: {{ traffic.productId }}
                                            </span>

                                            <!-- 订单号带复制按钮 - 使用useClipboard和useTimeoutFn -->
                                            <div
                                                class="inline-flex items-center px-2 py-0.5 rounded bg-gray-50 group/copy relative">
                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                    class="h-3 w-3 mr-1 text-gray-400" fill="none" viewBox="0 0 24 24"
                                                    stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                                                </svg>
                                                订单号: {{ traffic.outTradeNo }}

                                                <!-- 复制按钮 - 使用@vueuse/core的useClipboard处理 -->
                                                <button @click="() => copyTrafficOrderNo(traffic.outTradeNo, index)"
                                                    class="ml-1.5 text-indigo-500 hover:text-indigo-700 transition-colors duration-200"
                                                    title="复制订单号"
                                                    :class="{ 'text-green-500': copyResults[index]?.copied }">
                                                    <svg v-if="!copyResults[index]?.copied"
                                                        xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5"
                                                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round"
                                                            stroke-width="2"
                                                            d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                                                    </svg>
                                                    <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5"
                                                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round"
                                                            stroke-width="2" d="M5 13l4 4L19 7" />
                                                    </svg>
                                                </button>

                                                <!-- 复制成功提示 - 使用useTransition控制动画 -->
                                                <Transition enter-active-class="transition duration-300 ease-out"
                                                    enter-from-class="transform opacity-0 scale-95 translate-y-2"
                                                    enter-to-class="transform opacity-100 scale-100 translate-y-0"
                                                    leave-active-class="transition duration-200 ease-in"
                                                    leave-from-class="transform opacity-100 scale-100 translate-y-0"
                                                    leave-to-class="transform opacity-0 scale-95 translate-y-2">
                                                    <div v-if="copyResults[index]?.copied"
                                                        class="absolute left-0 -bottom-6 bg-indigo-600 text-white text-xs px-2 py-0.5 rounded shadow-md whitespace-nowrap z-20">
                                                        已复制到剪贴板
                                                    </div>
                                                </Transition>
                                            </div>

                                            <span
                                                class="inline-flex items-center px-2 py-0.5 rounded bg-purple-50 text-purple-700 font-medium">
                                                {{ traffic.pluginType }}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <!-- 使用情况进度 -->
                                <GsapAnimation animation="fadeIn" :delay="0.3 + 0.1 * index" ::duration="0.8">
                                    <div class="bg-gray-50 p-4 rounded-lg border border-gray-100">
                                        <div class="flex items-center justify-between mb-2.5">
                                            <div class="flex items-center gap-2">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-500"
                                                    fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                                                </svg>
                                                <span class="text-sm font-medium text-gray-700">今日使用情况</span>
                                            </div>
                                            <div class="flex items-center gap-3">
                                                <span class="text-xs font-medium text-gray-700">
                                                    {{ traffic.dayUsed }}/{{ traffic.dayLimit }}
                                                </span>
                                                <span class="px-2 py-0.5 rounded text-xs font-medium"
                                                    :class="getUsageStatusClass(traffic)">
                                                    {{ getUsageStatusText(traffic) }}
                                                </span>
                                            </div>
                                        </div>

                                        <!-- 进度条 -->
                                        <div class="space-y-1.5">
                                            <div class="h-2 w-full bg-gray-200 rounded-full overflow-hidden">
                                                <GsapAnimation animation="slideIn" :delay="0.4 + 0.1 * index"
                                                    ::duration="1.0" :to="{ xPercent: 0, ease: 'power3.out' }">
                                                    <div class="h-full rounded-full"
                                                        :class="getUsageColorClass(traffic)"
                                                        :style="`width: ${getUsagePercentage(traffic)}%`"></div>
                                                </GsapAnimation>
                                            </div>
                                            <div class="flex justify-between text-xs text-gray-500">
                                                <span>使用率: {{ getUsagePercentage(traffic) }}%</span>
                                                <span>剩余: {{ traffic.dayLimit - traffic.dayUsed }} 次</span>
                                            </div>
                                        </div>
                                    </div>
                                </GsapAnimation>
                            </div>

                            <!-- 右侧过期时间和操作区域 -->
                            <div class="flex flex-col justify-between md:w-52 gap-4">
                                <!-- 过期时间 -->
                                <GsapAnimation animation="fadeInRight" :delay="0.3 + 0.05 * index" :duration="0.7">
                                    <div class="flex flex-row md:flex-col items-center md:items-start gap-3 p-3 rounded-lg"
                                        :class="getExpiryCardClass(traffic)">
                                        <div class="flex h-9 w-9 items-center justify-center rounded-full"
                                            :class="getExpiryIconClass(traffic)">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4.5 w-4.5" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                            </svg>
                                        </div>
                                        <div>
                                            <div class="text-xs font-medium" :class="getExpiryLabelClass(traffic)">
                                                {{ getExpiryLabel(traffic) }}
                                            </div>
                                            <div class="text-sm font-semibold mt-0.5"
                                                :class="getExpiryValueClass(traffic)">
                                                {{ formatExpiryDate(traffic.expiredDate) }}
                                            </div>
                                            <div class="text-xs mt-0.5" :class="getExpiryRemainingClass(traffic)">
                                                {{ getDaysRemaining(traffic.expiredDate) }}
                                            </div>
                                        </div>
                                    </div>
                                </GsapAnimation>

                                <!-- 操作按钮 -->
                                <div class="flex justify-end mt-auto md:mt-0">
                                    <GsapAnimation animation="fadeInUp" :delay="0.5 + 0.05 * index" :duration="0.6">
                                        <button @click="$emit('view-details', traffic.id)"
                                            class="inline-flex items-center rounded-lg px-3 py-2 text-sm font-medium text-white shadow-sm transition-all duration-200"
                                            :class="getActionButtonClass(traffic)">
                                            查看详情
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 ml-1.5"
                                                fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M9 5l7 7-7 7" />
                                            </svg>
                                        </button>
                                    </GsapAnimation>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </GsapAnimation>

            <!-- 空状态 -->
            <GsapAnimation v-if="!trafficData.current_data.length" animation="fadeIn" :delay="0.3" :duration="0.8">
                <div
                    class="flex flex-col items-center justify-center py-16 px-4 border border-dashed border-gray-300 rounded-xl bg-gray-50">
                    <div
                        class="h-16 w-16 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-500 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 10V3L4 14h7v7l9-11h-7z" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-medium text-gray-800 mb-2">暂无流量包</h3>
                    <p class="text-gray-500 text-center max-w-sm">您目前没有可用的流量包，请购买后再查看</p>
                    <button
                        class="mt-6 inline-flex items-center rounded-lg bg-gradient-to-r from-indigo-600 to-purple-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm hover:from-indigo-700 hover:to-purple-700">
                        购买流量包
                    </button>
                </div>
            </GsapAnimation>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';
import { useClipboard, useTimeoutFn } from '@vueuse/core';
import { useDateFormat } from '@vueuse/core';

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


// 创建多个复制功能的状态对象
const copyResults = reactive<Record<number, { copied: boolean, text: string }>>({});

// 初始化剪贴板功能
const { copy, isSupported } = useClipboard({ copiedDuring: 2000 });

// 复制订单号函数 - 使用@vueuse/core的useClipboard
const copyTrafficOrderNo = async (text: string, index: number) => {
    try {
        // 使用useClipboard的copy方法
        await copy(text);

        // 设置当前索引的复制状态
        copyResults[index] = { copied: true, text };

        // 使用useTimeoutFn设置自动重置状态
        useTimeoutFn(() => {
            if (copyResults[index]?.copied) {
                copyResults[index].copied = false;
            }
        }, 2000);

        console.log("已复制订单号:", text, "索引:", index);
    } catch (err) {
        console.error('无法复制文本: ', err);
        // 如果浏览器不支持copy API，显示提示
        if (!isSupported.value) {
            alert(`请手动复制此订单号: ${text}`);
        }
    }
};

// 使用useDateFormat格式化日期
const formatExpiryDate = (dateString: string) => {
    return useDateFormat(dateString, 'YYYY-MM-DD').value;
};

// 判断是否过期 (使用@vueuse/core日期工具)
const isExpired = (dateString: string) => {
    const expiryDate = new Date(dateString);
    const today = new Date();

    // 设置时间为00:00:00以确保计算整天数
    expiryDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    return expiryDate < today;
};

// 判断是否即将过期（7天内）
const isExpiringSoon = (dateString: string) => {
    const expiryDate = new Date(dateString);
    const today = new Date();

    // 设置时间为00:00:00以确保计算整天数
    expiryDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    const diffTime = expiryDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    return diffDays <= 7 && diffDays >= 0;
};

// 检查流量是否不足（小于20%）
const isLowBalance = (traffic: Traffic) => {
    return traffic.dayLimit > 0 && ((traffic.dayLimit - traffic.dayUsed) / traffic.dayLimit < 0.2);
};

// 获取流量包状态
const getTrafficStatus = (traffic: Traffic) => {
    if (isExpired(traffic.expiredDate)) return 'expired';
    if (isLowBalance(traffic)) return 'low';
    return 'active';
};

// 获取卡片边框类 - 减弱边框效果
const getCardBorderClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'border-red-100';
    if (status === 'low') return 'border-amber-100';
    return 'border-gray-100';
};

// 获取状态条类 - 保持颜色但弱化效果
const getStatusBarClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'bg-red-500';
    if (status === 'low') return 'bg-amber-500';
    return 'bg-emerald-500';
};

// 获取等级背景类 - 弱化渐变效果
const getLevelBgClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'bg-red-500';
    if (status === 'low') return 'bg-amber-500';
    return 'bg-indigo-500';
};

// 获取状态标签类 - 弱化样式
const getStatusBadgeClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'bg-red-50 text-red-700';
    if (status === 'low') return 'bg-amber-50 text-amber-700';
    return 'bg-emerald-50 text-emerald-700';
};

// 获取状态文本
const getStatusText = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return '已过期';
    if (status === 'low') return '余额不足';
    return '可用';
};

// 获取过期卡片类 - 弱化背景
const getExpiryCardClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'bg-red-50/70';
    if (isExpiringSoon(traffic.expiredDate)) return 'bg-amber-50/70';
    return 'bg-emerald-50/70';
};

// 获取过期图标类 - 弱化背景
const getExpiryIconClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'bg-red-100/80 text-red-600';
    if (isExpiringSoon(traffic.expiredDate)) return 'bg-amber-100/80 text-amber-600';
    return 'bg-emerald-100/80 text-emerald-600';
};

// 获取过期标签类
const getExpiryLabelClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'text-red-600';
    if (isExpiringSoon(traffic.expiredDate)) return 'text-amber-600';
    return 'text-emerald-600';
};

// 获取过期值类
const getExpiryValueClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'text-red-800';
    if (isExpiringSoon(traffic.expiredDate)) return 'text-amber-800';
    return 'text-emerald-800';
};

// 获取过期剩余天数类
const getExpiryRemainingClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return 'text-red-700';
    if (isExpiringSoon(traffic.expiredDate)) return 'text-amber-700';
    return 'text-emerald-700';
};

// 获取过期标签文本
const getExpiryLabel = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired') return '已过期';
    if (isExpiringSoon(traffic.expiredDate)) return '即将过期';
    return '有效期至';
};

// 计算剩余天数
const getDaysRemaining = (dateString: string) => {
    const expiryDate = new Date(dateString);
    const today = new Date();

    // 设置时间为00:00:00以确保计算整天数
    expiryDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    const diffTime = expiryDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays < 0) {
        return '已过期' + Math.abs(diffDays) + '天';
    } else if (diffDays === 0) {
        return '今日过期';
    } else if (diffDays <= 30) {
        return `剩余 ${diffDays} 天`;
    } else {
        const months = Math.floor(diffDays / 30);
        return `剩余约 ${months} 个月`;
    }
};

// 获取使用百分比
const getUsagePercentage = (traffic: Traffic) => {
    if (traffic.dayLimit === 0) return 0;
    return Math.round((traffic.dayUsed / traffic.dayLimit) * 100);
};

// 获取使用状态文本
const getUsageStatusText = (traffic: Traffic) => {
    if (isExpired(traffic.expiredDate)) return '不可用';

    const remainingPercentage = ((traffic.dayLimit - traffic.dayUsed) / traffic.dayLimit) * 100;
    if (remainingPercentage <= 20) return '余额不足';
    if (remainingPercentage <= 50) return '使用中等';
    return '充足';
};

// 获取使用状态样式
const getUsageStatusClass = (traffic: Traffic) => {
    if (isExpired(traffic.expiredDate))
        return 'bg-red-50/80 text-red-700';

    const remainingPercentage = ((traffic.dayLimit - traffic.dayUsed) / traffic.dayLimit) * 100;
    if (remainingPercentage <= 20)
        return 'bg-amber-50/80 text-amber-700';
    if (remainingPercentage <= 50)
        return 'bg-yellow-50/80 text-yellow-700';
    return 'bg-emerald-50/80 text-emerald-700';
};

// 获取进度条颜色类
const getUsageColorClass = (traffic: Traffic) => {
    if (isExpired(traffic.expiredDate))
        return 'bg-gray-400'; // 过期显示灰色

    const remainingPercentage = ((traffic.dayLimit - traffic.dayUsed) / traffic.dayLimit) * 100;
    if (remainingPercentage <= 20)
        return 'bg-gradient-to-r from-red-500 to-red-600';
    if (remainingPercentage <= 50)
        return 'bg-gradient-to-r from-amber-500 to-amber-600';
    return 'bg-gradient-to-r from-emerald-500 to-teal-600';
};

// 获取操作按钮样式 - 弱化hover效果
const getActionButtonClass = (traffic: Traffic) => {
    const status = getTrafficStatus(traffic);
    if (status === 'expired')
        return 'bg-gray-500 hover:bg-gray-600 transition-colors';
    if (status === 'low')
        return 'bg-amber-500 hover:bg-amber-600 transition-colors';
    return 'bg-indigo-500 hover:bg-indigo-600 transition-colors';
};
</script>
