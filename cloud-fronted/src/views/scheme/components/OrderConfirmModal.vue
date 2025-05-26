<template>
    <BaseModal :model-value="show" @update:model-value="handleModalClose" :close-on-backdrop="!loading"
        content-padding="p-0" id="order-confirm-modal">

        <!-- 自定义渐变头部 -->
        <template #customHeader="{ close }">
            <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-4 sm:px-6 py-4">
                <div class="flex items-center justify-between">
                    <h3 class="text-lg sm:text-xl font-semibold text-white" id="modal-title">
                        确认订单
                    </h3>
                    <button @click="handleClose" :disabled="loading"
                        class="text-white hover:text-gray-200 transition-colors duration-200 p-1">
                        <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
            </div>
        </template>

        <!-- 模态框主要内容 -->
        <div class="bg-white px-4 sm:px-6 py-4 sm:py-6 max-h-[70vh] sm:max-h-[60vh] overflow-y-auto">
            <p class="text-gray-600 mb-4 sm:mb-6 text-center text-sm sm:text-base">
                您即将购买以下会员方案，请确认订单信息
            </p>

            <!-- 商品信息卡片 -->
            <div
                class="bg-gradient-to-r from-gray-50 to-gray-100 p-4 sm:p-6 rounded-xl mb-4 sm:mb-6 border border-gray-200">
                <div class="flex items-center justify-between mb-4">
                    <div>
                        <h4 class="text-base sm:text-lg font-semibold text-gray-900">{{ plan.title }}</h4>
                        <p class="text-xs sm:text-sm text-gray-500 mt-1">有效期: {{ plan.validDay }}天</p>
                    </div>
                    <div class="text-right">
                        <div class="text-xl sm:text-2xl font-bold text-blue-600">¥{{ plan.amount }}</div>
                        <div class="text-xs text-gray-500">原价</div>
                    </div>
                </div>

                <div class="border-t border-gray-200 pt-4 space-y-2">
                    <div class="flex justify-between text-sm">
                        <span class="text-gray-600">购买数量</span>
                        <span class="font-medium">1</span>
                    </div>
                    <div class="flex justify-between text-base sm:text-lg font-semibold">
                        <span class="text-gray-900">总计金额</span>
                        <span class="text-blue-600">¥{{ plan.amount }}</span>
                    </div>
                </div>
            </div>

            <!-- 支付方式选择 -->
            <div class="mb-4 sm:mb-6">
                <label class="block text-base sm:text-lg font-semibold text-gray-900 mb-3 sm:mb-4">选择支付方式</label>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                    <label
                        class="relative flex items-center p-3 sm:p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:shadow-md"
                        :class="{ 'border-blue-500 bg-blue-50 shadow-md': payType === 'WECHAT_PAY', 'border-gray-200 hover:border-gray-300': payType !== 'WECHAT_PAY' }">
                        <input type="radio" name="payType" value="WECHAT_PAY" v-model="payType" class="sr-only">
                        <div class="flex items-center">
                            <div class="w-5 h-5 sm:w-6 sm:h-6 rounded-full border-2 mr-3 flex items-center justify-center"
                                :class="{ 'border-blue-500 bg-blue-500': payType === 'WECHAT_PAY', 'border-gray-300': payType !== 'WECHAT_PAY' }">
                                <div v-if="payType === 'WECHAT_PAY'" class="w-2 h-2 rounded-full bg-white"></div>
                            </div>
                            <span class="font-medium text-gray-900 text-sm sm:text-base ml-2">💬 微信支付</span>
                        </div>
                    </label>

                    <label
                        class="relative flex items-center p-3 sm:p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:shadow-md"
                        :class="{ 'border-blue-500 bg-blue-50 shadow-md': payType === 'ALIPAY', 'border-gray-200 hover:border-gray-300': payType !== 'ALIPAY' }">
                        <input type="radio" name="payType" value="ALIPAY" v-model="payType" class="sr-only">
                        <div class="flex items-center">
                            <div class="w-5 h-5 sm:w-6 sm:h-6 rounded-full border-2 mr-3 flex items-center justify-center"
                                :class="{ 'border-blue-500 bg-blue-500': payType === 'ALIPAY', 'border-gray-300': payType !== 'ALIPAY' }">
                                <div v-if="payType === 'ALIPAY'" class="w-2 h-2 rounded-full bg-white"></div>
                            </div>
                            <span class="font-medium text-gray-900 text-sm sm:text-base ml-2">🅰️ 支付宝</span>
                        </div>
                    </label>
                </div>
            </div>

            <!-- 发票信息 -->
            <div class="border border-gray-200 rounded-xl p-4 sm:p-5">
                <div class="flex items-center justify-between mb-4">
                    <div class="flex items-center">
                        <input id="needInvoice" type="checkbox" v-model="needInvoice"
                            class="h-4 w-4 sm:h-5 sm:w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
                        <label for="needInvoice" class="ml-3 text-base sm:text-lg font-semibold text-gray-900">
                            需要发票
                        </label>
                    </div>
                    <div v-if="needInvoice" class="text-green-500 text-lg">✓</div>
                </div>

                <div v-if="needInvoice" class="space-y-4 animate-fade-in-down border-t border-gray-200 pt-4">
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-2">发票类型</label>
                            <select v-model="billType"
                                class="w-full pl-3 pr-8 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 rounded-lg">
                                <option value="PERSONAL">个人发票</option>
                                <option value="COMPANY">企业发票</option>
                            </select>
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-2">发票抬头</label>
                            <input type="text" v-model="billHeader" placeholder="请输入发票抬头"
                                class="w-full px-3 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">发票内容</label>
                        <input type="text" v-model="billContent" placeholder="软件服务"
                            class="w-full px-3 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                    </div>

                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-2">接收邮箱</label>
                            <input type="email" v-model="billReceiverEmail" placeholder="example@email.com"
                                class="w-full px-3 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-2">接收手机</label>
                            <input type="tel" v-model="billReceiverPhone" placeholder="请输入手机号码"
                                class="w-full px-3 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 底部按钮区域 -->
        <template #separateFooter>
            <div class="flex flex-col-reverse sm:flex-row-reverse gap-3 w-full">
                <button type="button"
                    class="w-full sm:w-auto inline-flex justify-center items-center rounded-xl border border-transparent shadow-sm px-4 sm:px-6 py-2.5 sm:py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-sm sm:text-base font-semibold text-white hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all duration-200 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
                    @click="submitOrder" :disabled="loading">
                    <span v-if="loading" class="mr-2">
                        <svg class="animate-spin h-4 w-4 sm:h-5 sm:w-5 text-white" xmlns="http://www.w3.org/2000/svg"
                            fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                            </circle>
                            <path class="opacity-75" fill="currentColor"
                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                            </path>
                        </svg>
                    </span>
                    {{ loading ? '处理中...' : '确认支付' }}
                </button>
                <button type="button"
                    class="w-full sm:w-auto inline-flex justify-center rounded-xl border border-gray-300 shadow-sm px-4 sm:px-6 py-2.5 sm:py-3 bg-white text-sm sm:text-base font-medium text-gray-700 hover:bg-gray-50 hover:border-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-all duration-200"
                    @click="handleClose" :disabled="loading">
                    取消
                </button>
            </div>
        </template>
    </BaseModal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import BaseModal from '@/components/BaseModal.vue';

const props = defineProps({
    show: {
        type: Boolean,
        default: false
    },
    plan: {
        type: Object,
        required: true
    }
});

const emit = defineEmits(['close', 'submit']);

// 支付相关状态
const payType = ref('WECHAT_PAY');
const loading = ref(false);

// 发票相关状态
const needInvoice = ref(false);
const billType = ref('NO_BILL');
const billHeader = ref('');
const billContent = ref('');
const billReceiverPhone = ref('');
const billReceiverEmail = ref('');

// 监听发票需求变化
function watchNeedInvoice() {
    if (needInvoice.value) {
        billType.value = 'PERSONAL';
    } else {
        billType.value = 'NO_BILL';
    }
}

// 关闭模态框
const handleModalClose = (value: boolean) => {
    if (!value && !loading.value) {
        emit('close');
    }
};

// 处理关闭按钮点击
const handleClose = () => {
    if (!loading.value) {
        emit('close');
    }
};

// 提交订单
const submitOrder = () => {
    loading.value = true;

    // 构建订单参数
    const orderParams = {
        productId: props.plan.id,
        buyNum: 1,
        clientType: 'PC',
        payType: payType.value,
        totalAmount: props.plan.amount,
        payAmount: props.plan.amount,
        token: '', // 从用户状态或本地存储获取
        billType: billType.value,
        billHeader: billHeader.value,
        billContent: billContent.value,
        billReceiverPhone: billReceiverPhone.value,
        billReceiverEmail: billReceiverEmail.value
    };

    emit('submit', orderParams);
};

// 监听发票需求变化
watch(needInvoice, watchNeedInvoice);
</script>

<style scoped>
.animate-fade-in-down {
    animation: fadeInDown 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

@keyframes fadeInDown {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
