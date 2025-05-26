<template>
    <PageLayout tag="会员方案选择" title="选择适合您的会员方案" description="升级会员享受更多短链接生成权限和高级功能">
        <!-- 会员方案卡片 - 使用MembershipPlanCard组件 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
            <MembershipPlanCard v-for="(plan, index) in membershipPlans" :key="plan.id" :plan="plan"
                :color-index="index" :transition-delay="index" :is-popular="isPopularPlan(plan)"
                @select="openOrderModal" />
        </div>

        <!-- 会员优势说明 - 移动端优化 -->
        <div class="mt-12 md:mt-24 max-w-4xl mx-auto opacity-0 translate-y-5 transition-all duration-800 ease-out transition-delay-500"
            :class="{ 'opacity-100 translate-y-0': isRevealed }">
            <div class="mb-6 md:mb-8 text-center">
                <h3 class="text-lg md:text-2xl font-bold text-gray-900 mb-1 md:mb-2">会员专属优势</h3>
                <p class="text-sm md:text-base text-gray-600">升级会员后您将获得以下优势</p>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 md:gap-6">
                <!-- 使用FeatureCard组件替换原有的优势卡片 -->
                <FeatureCard v-if="hasDayTimesFeature" title="更高生成配额"
                    :description="`会员每日可生成更多短链接，满足您的批量运营需求。黑金会员每日可生成${getMaxDayTimes()}次，远超普通用户。`" icon="lightning"
                    colorScheme="indigo" />

                <FeatureCard v-if="hasUnlimitedFeature" title="无限制跳转"
                    description="普通用户有跳转限制，会员用户享受无限制跳转次数，适合大型活动和持续性营销活动。" icon="shield" colorScheme="blue" />

                <FeatureCard v-if="hasDataViewFeature" title="详细数据分析" description="所有方案均支持数据查看功能，帮助您了解短链接的使用情况和效果。"
                    icon="document" colorScheme="purple" />

                <FeatureCard v-if="hasCustomDomainFeature" title="自定义域名"
                    description="黑金会员专享功能，可使用自定义域名生成短链接，提高品牌辨识度和专业形象。" icon="globe" colorScheme="green" />
            </div>
        </div>

        <!-- 常见问题 - 移动端优化 -->
        <div class="mt-12 md:mt-24 max-w-3xl mx-auto opacity-0 translate-y-5 transition-all duration-800 ease-out transition-delay-700"
            :class="{ 'opacity-100 translate-y-0': isRevealed }">
            <div class="mb-5 md:mb-8 text-center">
                <h3 class="text-lg md:text-2xl font-bold text-gray-900 mb-1 md:mb-2">常见问题</h3>
                <p class="text-sm md:text-base text-gray-600">关于会员方案的常见问题解答</p>
            </div>

            <div class="space-y-3 md:space-y-4">
                <!-- 使用FaqItem组件替换原来的div -->
                <FaqItem question="如何升级会员？" answer="选择您心仪的会员方案，点击'立即购买'按钮，按照系统提示完成支付即可立即升级。" />

                <FaqItem question="会员到期后会怎样？" answer="会员到期后，您的账户将自动降级为免费用户，已创建的短链接仍然有效，但会受到免费用户的使用限制。" />

                <FaqItem question="会员有效期是多久？"
                    :answer="`我们提供多种会员时长选择，目前黄金会员和黑金会员的有效期为${getMaxValidDay()}天，具体以您选择的方案为准。`" />
            </div>
        </div>

        <!-- 订单确认模态框 -->
        <OrderConfirmModal :show="showOrderModal" :plan="selectedPlan || {}" @close="showOrderModal = false"
            @submit="submitOrder" />

        <!-- 支付模态框 -->
        <PaymentModal v-if="showPaymentModal" :show="showPaymentModal" :payment-type="paymentType"
            :order-info="orderInfo" @close="showPaymentModal = false" @payment-success="handlePaymentSuccess"
            @payment-failed="handlePaymentFailed" @weichat-pay-handler="WeChatPayApi" />
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import MembershipPlanCard from './components/MembershipPlanCard.vue';
import FeatureCard from './components/FeatureCard.vue';
import FaqItem from './components/FaqItem.vue';
import PageLayout from '@/components/PageLayout.vue';
import OrderConfirmModal from './components/OrderConfirmModal.vue';
import PaymentModal from './components/PaymentModal.vue';
import { ProductGetListApi, ProductGetOrderTokenApi, ProductOrderConfirmApi, WeChatPayApi, ProductGetOrderStatusApi } from '@/services/shop'
import { useToast } from '@/composables/useToast'
const toast = useToast();
// 定义会员方案接口
interface MembershipPlan {
    id: number;
    title: string;
    detail: string;
    img: string | null;
    level: 'FIRST' | 'SECOND' | 'THIRD' | string;
    oldAmount: number;
    amount: number;
    pluginType: string;
    dayTimes: number;
    totalTimes: number | null;
    validDay: number;
}

// 会员方案数据
const membershipPlans = ref<MembershipPlan[]>([]);
const isRevealed = ref(false);

// 订单相关状态
const showOrderModal = ref(false);
const selectedPlan = ref<MembershipPlan | null>(null);

// 支付相关状态
const showPaymentModal = ref(false);
const paymentType = ref('');
const orderInfo = ref({
    outTradeNo: '',
    accountNo: '',
    productTitle: '',
    amount: 0
});

// 从API获取会员方案数据
const fetchMembershipPlans = async () => {
    try {
        const response = await ProductGetListApi()
        membershipPlans.value = response;
    } catch (error) {
        console.error('获取会员方案失败', error);
    }
};

onMounted(() => {
    fetchMembershipPlans();
});

// 根据数据计算的特性存在标志
const hasDayTimesFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.dayTimes > 0);
});

const hasUnlimitedFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('不限制'));
});

const hasDataViewFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('数据查看'));
});

const hasCustomDomainFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('自定义域名'));
});

// 获取最大的每日生成次数
const getMaxDayTimes = () => {
    return Math.max(...membershipPlans.value.map(plan => plan.dayTimes || 0));
};

// 获取最长有效期
const getMaxValidDay = () => {
    return Math.max(...membershipPlans.value.map(plan => plan.validDay || 0));
};

// 判断是否是最受欢迎的方案
const isPopularPlan = (plan: any) => {
    // 假设第二个方案(黄金会员)是最受欢迎的
    return plan.level === 'SECOND';
};

// 打开订单模态框
const openOrderModal = (plan: any) => {
    selectedPlan.value = plan;
    showOrderModal.value = true;
};

// TODO:提交订单
const submitOrder = async (orderParams: any) => {
    try {
        // 1.获取订单令牌
        const token = await ProductGetOrderTokenApi()
        const orderData = {
            ...orderParams,
            token: token
        };
        // 2.提交订单
        const orderResponse = await ProductOrderConfirmApi(orderData);
        const accountNo = orderResponse.account_no;
        const outTradeNo = orderResponse.out_trade_no;
        const payType = orderResponse.pay_type;

        // 3.设置支付信息并打开支付模态框
        paymentType.value = payType;
        orderInfo.value = {
            outTradeNo: outTradeNo,
            accountNo: accountNo,
            productTitle: selectedPlan.value?.title || '默认',
            amount: orderData.payAmount || 0
        };

        // 关闭订单确认模态框，打开支付模态框
        showOrderModal.value = false;
        showPaymentModal.value = true;

    } catch (error) {
        console.error('提交订单出错:', error);
        alert('提交订单出错，请重试');
        showOrderModal.value = false;
    }
};


// 支付成功回调
const handlePaymentSuccess = async (outTradeNo: string) => {
    toast.success('支付成功!');
    showPaymentModal.value = false;
    setTimeout(async () => {
        const res = await ProductGetOrderStatusApi(outTradeNo);
        const statusMap: Record<string, string> = {
            PAY: "已支付",
            NEW: "未支付"
        };
        const statusMessage = statusMap[res] || "订单已取消";
        toast.info(`订单状态: ${statusMessage}`);
    }, 1000);

};

// 支付失败回调
const handlePaymentFailed = () => {
    console.log('支付失败');
    toast.error('支付失败，请重试');
};

// 添加页面动画效果
onMounted(() => {
    // 标记为已显示，触发动画
    setTimeout(() => {
        isRevealed.value = true;
    }, 100);

    // TODO: 获取用户当前会员状态，用于展示推荐方案或当前方案状态
    // 示例:
    // try {
    //   const response = await api.get('/user/membership-status');
    //   const { currentPlan, expirationDate } = response.data;
    //   // 根据用户当前状态进行界面调整
    // } catch (error) {
    //   console.error('获取会员状态失败', error);
    // }

    // 触摸设备交互优化
    if ('ontouchstart' in window) {
        const cards = document.querySelectorAll('.membership-card');
        cards.forEach(card => {
            card.addEventListener('touchstart', () => {
                card.classList.add('active:scale-[0.98]');
            });
            card.addEventListener('touchend', () => {
                setTimeout(() => {
                    card.classList.remove('active:scale-[0.98]');
                }, 100);
            });
        });
    }
});

</script>

<style>
@media (prefers-reduced-motion: reduce) {
    .transition-all {
        transition: none !important;
    }

    [class*='hover:'] {
        transform: none !important;
    }

    [class*='translate-'] {
        transform: none !important;
    }

    .opacity-0 {
        opacity: 1 !important;
    }
}
</style>