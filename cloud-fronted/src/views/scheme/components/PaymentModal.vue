<template>
    <BaseModal :model-value="show" @update:model-value="$emit('close')" :title="getPaymentTitle()" content-padding="p-0"
        :close-on-backdrop="false">
        <div class="p-6">
            <!-- 订单信息 -->
            <div class="mb-6 p-4 bg-gray-50 rounded-lg">
                <h4 class="text-sm font-medium text-gray-900 mb-2">订单信息</h4>
                <div class="space-y-1 text-sm text-gray-600">
                    <div class="flex justify-between">
                        <span>商品名称：</span>
                        <span>{{ orderInfo.productTitle }}</span>
                    </div>
                    <div class="flex justify-between">
                        <span>订单号：</span>
                        <span class="font-mono text-xs">{{ orderInfo.outTradeNo }}</span>
                    </div>
                    <div class="flex justify-between font-medium text-gray-900">
                        <span>支付金额：</span>
                        <span class="text-red-600">¥{{ orderInfo.amount }}</span>
                    </div>
                </div>
            </div>

            <!-- 支付二维码区域 - 直接显示，不需要选择支付方式 -->
            <div class="mb-6">
                <div class="text-center p-6 bg-gray-50 rounded-lg">
                    <div class="w-48 h-48 mx-auto mb-4 bg-white rounded-lg shadow-sm flex items-center justify-center">
                        <div v-if="isGeneratingQR" class="text-gray-400">
                            <div class="animate-spin w-8 h-8 border-2 border-gray-300 border-t-blue-600 rounded-full">
                            </div>
                            <div class="mt-2 text-sm">生成中...</div>
                        </div>
                        <div v-else-if="qrCodeUrl" class="w-full h-full">
                            <img :src="qrCodeUrl" alt="支付二维码" class="w-full h-full object-contain rounded">
                        </div>
                        <div v-else class="text-gray-400 text-sm flex flex-col items-center justify-center">
                            <div class="mb-2">点击下方按钮生成支付码</div>
                            <svg class="w-10 h-10 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
                            </svg>
                        </div>
                    </div>
                    <div class="text-sm text-gray-600 mb-2" v-if="qrCodeUrl">
                        请使用{{ getPaymentMethodName() }}扫描二维码完成支付
                    </div>
                    <div class="text-xs text-gray-500" v-if="qrCodeUrl">
                        支付完成后页面将自动跳转
                    </div>
                </div>
            </div>
        </div>

        <!-- 底部按钮 -->
        <template #separateFooter>
            <div class="flex space-x-3 w-full">
                <button type="button"
                    class="flex-1 bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    @click="$emit('close')">
                    取消
                </button>
                <button type="button"
                    class="flex-1 bg-indigo-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
                    :disabled="isProcessing" @click="processPayment">
                    <span v-if="isProcessing">处理中...</span>
                    <span v-else>{{ qrCodeUrl ? '正在支付...' : '生成支付码' }}</span>
                </button>
            </div>
        </template>
    </BaseModal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import BaseModal from '@/components/BaseModal.vue'

// 组件属性
const props = defineProps({
    show: {
        type: Boolean,
        required: true,
        default: false
    },
    paymentType: {
        type: String,
        required: true,
        default: ''
    },
    orderInfo: {
        type: Object,
        required: true,
        default: () => ({
            outTradeNo: '',
            accountNo: '',
            productTitle: '',
            amount: 0
        })
    }
})

// 事件定义
const emit = defineEmits(['close', 'payment-success', 'payment-failed', 'weichat-pay-handler', 'alipay-handler'])

// 响应式数据
const isProcessing = ref(false)
const isGeneratingQR = ref(false)
const qrCodeUrl = ref('')

// 支付轮询定时器
let paymentPollingTimer: NodeJS.Timeout | null = null

// 获取支付方式名称
const getPaymentMethodName = () => {
    if (props.paymentType.includes('WECHAT_PAY')) {
        return '微信'
    } else if (props.paymentType.includes('ALI_PAY')) {
        return '支付宝'
    } else {
        return '指定的支付软件'
    }
}

// 获取支付标题
const getPaymentTitle = () => {
    return `${getPaymentMethodName()}支付`
}

// 监听模态框显示状态，重置数据
watch(() => props.show, (newVal) => {
    if (!newVal) {
        // 重置状态
        resetPaymentState()
    }
})

// 重置支付状态
const resetPaymentState = () => {
    isProcessing.value = false
    isGeneratingQR.value = false
    qrCodeUrl.value = ''
    if (paymentPollingTimer) {
        clearInterval(paymentPollingTimer)
        paymentPollingTimer = null
    }
}

// 处理支付
const processPayment = async () => {
    if (isProcessing.value) return

    isProcessing.value = true
    isGeneratingQR.value = true
    console.log(props)
    try {
        // 根据支付类型调用对应的支付处理函数
        if (props.paymentType.includes('WECHAT_PAY')) {
            emit('weichat-pay-handler', {
                outTradeNo: props.orderInfo.outTradeNo,
                accountNo: props.orderInfo.accountNo,
                tradeState: "SUCCESS"
            })
        }
        else if (props.paymentType.includes('ALI_PAY')) {

        }
        else {
            // 如果没有提供处理函数或支付类型不匹配，使用模拟二维码
            await new Promise(resolve => setTimeout(resolve, 1500))
            qrCodeUrl.value = generatePlaceholderQRCode(getPaymentMethodName())
        }
        emit('payment-success')
    } catch (error) {
        console.error('生成支付码失败:', error)
        emit('payment-failed')
    } finally {
        isGeneratingQR.value = false
        isProcessing.value = false
    }
}

// 生成占位符二维码（当没有真实二维码时使用）
const generatePlaceholderQRCode = (paymentMethod: string) => {
    return `data:image/svg+xml;base64,${btoa(`
        <svg width="200" height="200" xmlns="http://www.w3.org/2000/svg">
            <rect width="200" height="200" fill="white"/>
            <text x="100" y="100" text-anchor="middle" dy=".3em" font-family="Arial" font-size="14">
                ${paymentMethod}支付二维码
            </text>
        </svg>
    `)}`
}
</script>
