<template>
    <div v-if="show" class="fixed inset-0 z-50 overflow-y-auto" aria-labelledby="modal-title" role="dialog"
        aria-modal="true">
        <div class="flex items-center justify-center min-h-screen p-4 text-center">
            <!-- 背景遮罩 -->
            <div class="fixed inset-0 bg-black bg-opacity-50 transition-opacity backdrop-blur-sm" aria-hidden="true"
                @click="closeModal"></div>

            <!-- 模态框内容 -->
            <div
                class="relative bg-white rounded-xl text-left overflow-hidden shadow-2xl transform transition-all w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                <!-- 头部 -->
                <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-4 sm:px-6 py-4">
                    <div class="flex items-center justify-between">
                        <h3 class="text-lg sm:text-xl font-semibold text-white" id="modal-title">
                            确认订单
                        </h3>
                        <button @click="closeModal" :disabled="loading"
                            class="text-white hover:text-gray-200 transition-colors duration-200 p-1">
                            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>

                <!-- 内容区域 -->
                <div class="bg-white px-4 sm:px-6 py-4 sm:py-6">
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
                        <label
                            class="block text-base sm:text-lg font-semibold text-gray-900 mb-3 sm:mb-4">选择支付方式</label>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                            <label
                                class="relative flex items-center p-3 sm:p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:shadow-md"
                                :class="{ 'border-blue-500 bg-blue-50 shadow-md': payType === 'WECHAT_PAY', 'border-gray-200 hover:border-gray-300': payType !== 'WECHAT_PAY' }">
                                <input type="radio" name="payType" value="WECHAT_PAY" v-model="payType" class="sr-only">
                                <div class="flex items-center">
                                    <div class="w-5 h-5 sm:w-6 sm:h-6 rounded-full border-2 mr-3 flex items-center justify-center"
                                        :class="{ 'border-blue-500 bg-blue-500': payType === 'WECHAT_PAY', 'border-gray-300': payType !== 'WECHAT_PAY' }">
                                        <div v-if="payType === 'WECHAT_PAY'" class="w-2 h-2 rounded-full bg-white">
                                        </div>
                                    </div>
                                    <svg class="h-5 w-5 sm:h-6 sm:w-6 text-green-500 mr-2" viewBox="0 0 1024 1024"
                                        fill="currentColor">
                                        <path
                                            d="M663.04 798.72c33.792 20.48 67.584 33.792 105.472 40.96-72.704 51.2-167.936 81.92-271.36 81.92-30.72 0-61.44-3.072-92.16-9.216-92.16-15.36-174.08-61.44-235.52-125.952 15.36 3.072 33.792 6.144 53.248 6.144 92.16 0 174.08-30.72 240.64-80.896z m-286.72-184.32c-96.256 0-174.08-77.824-174.08-174.08s77.824-174.08 174.08-174.08 174.08 77.824 174.08 174.08-77.824 174.08-174.08 174.08z m286.72 0c-96.256 0-174.08-77.824-174.08-174.08s77.824-174.08 174.08-174.08 174.08 77.824 174.08 174.08-77.824 174.08-174.08 174.08z" />
                                    </svg>
                                    <span class="font-medium text-gray-900 text-sm sm:text-base">微信支付</span>
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
                                    <svg class="h-5 w-5 sm:h-6 sm:w-6 text-blue-500 mr-2" viewBox="0 0 1024 1024"
                                        fill="currentColor">
                                        <path
                                            d="M1024 561.6c0 56.32-71.68 122.88-158.72 158.72-78.848 32.768-178.176 43.008-286.72 25.6-46.08-7.168-89.088-18.432-129.024-32.768-89.088 71.68-194.56 120.832-307.2 120.832-61.44 0-118.784-15.36-168.96-43.008v-35.84c40.96 20.48 87.04 30.72 133.12 30.72 97.28 0 189.44-40.96 268.288-107.52-138.24-71.68-240.64-186.368-286.72-322.56h153.6c35.84 97.28 107.52 181.248 199.68 238.592 30.72-12.288 64.512-20.48 99.328-25.6 10.24-1.024 20.48-2.048 30.72-2.048 87.04-7.168 174.08 7.168 250.88 40.96 76.8 33.792 138.24 84.992 143.36 153.6z m-512-281.6h-358.4v-51.2h358.4v51.2z m0-102.4h-358.4v-51.2h358.4v51.2z" />
                                    </svg>
                                    <span class="font-medium text-gray-900 text-sm sm:text-base">支付宝</span>
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
                            <svg v-if="needInvoice" class="h-5 w-5 text-green-500" fill="currentColor"
                                viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                    clip-rule="evenodd" />
                            </svg>
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

                <!-- 模态框底部按钮 -->
                <div class="bg-gray-50 px-4 sm:px-6 py-3 sm:py-4 flex flex-col-reverse sm:flex-row-reverse gap-3">
                    <button type="button"
                        class="w-full sm:w-auto inline-flex justify-center items-center rounded-xl border border-transparent shadow-sm px-4 sm:px-6 py-2.5 sm:py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-sm sm:text-base font-semibold text-white hover:from-blue-700 hover:to-blue-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all duration-200 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
                        @click="submitOrder" :disabled="loading">
                        <span v-if="loading" class="mr-2">
                            <svg class="animate-spin h-4 w-4 sm:h-5 sm:w-5 text-white"
                                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                    stroke-width="4"></circle>
                                <path class="opacity-75" fill="currentColor"
                                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                                </path>
                            </svg>
                        </span>
                        {{ loading ? '处理中...' : '确认支付' }}
                    </button>
                    <button type="button"
                        class="w-full sm:w-auto inline-flex justify-center rounded-xl border border-gray-300 shadow-sm px-4 sm:px-6 py-2.5 sm:py-3 bg-white text-sm sm:text-base font-medium text-gray-700 hover:bg-gray-50 hover:border-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-all duration-200"
                        @click="closeModal" :disabled="loading">
                        取消
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue';

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
const closeModal = () => {
    if (loading.value) return;
    emit('close');
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
import { watch } from 'vue';
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

/* 自定义滚动条 */
.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
}
</style>
