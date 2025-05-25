<template>
    <PageLayout tag="会员方案选择" title="选择适合您的会员方案" description="升级会员享受更多短链接生成权限和高级功能">
        <!-- 会员方案卡片 - 使用MembershipPlanCard组件 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
            <MembershipPlanCard v-for="(plan, index) in membershipPlans" :key="plan.id" :plan="plan"
                :color-index="index" :transition-delay="index" :is-popular="isPopularPlan(plan)"
                @select="handlePlanSelection" />
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
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { Data } from './config';
import MembershipPlanCard from './components/MembershipPlanCard.vue';
import FeatureCard from './components/FeatureCard.vue';
import FaqItem from './components/FaqItem.vue';
import PageLayout from '@/components/PageLayout.vue';

// 会员方案数据
const membershipPlans = ref(Data);
const isRevealed = ref(false);

// TODO: 从API获取会员方案数据
// 示例: 
// const fetchMembershipPlans = async () => {
//   try {
//     const response = await api.get('/membership-plans');
//     membershipPlans.value = response.data;
//   } catch (error) {
//     console.error('获取会员方案失败', error);
//   }
// };
// 
// onMounted(() => {
//   fetchMembershipPlans();
// });

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

// 处理会员方案选择
const handlePlanSelection = (plan: any) => {
    console.log('选择了会员方案:', plan);
    // TODO: 发起会员购买请求，根据返回结果跳转到支付页面
    // 示例:
    // try {
    //   const response = await api.post('/orders/create', { planId: plan.id });
    //   const { orderId, paymentUrl } = response.data;
    //   window.location.href = paymentUrl; // 或使用路由导航到内部支付页面
    // } catch (error) {
    //   console.error('创建订单失败', error);
    //   // 显示错误通知
    // }
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
/* 自定义Tailwind类 - 只保留无法用Tailwind直接实现的样式 */
.transition-delay-100 {
    transition-delay: 0.1s;
}

.transition-delay-250 {
    transition-delay: 0.25s;
}

.transition-delay-400 {
    transition-delay: 0.4s;
}

.transition-delay-500 {
    transition-delay: 0.5s;
}

.transition-delay-700 {
    transition-delay: 0.7s;
}

/* 处理媒体查询对动画的优先级 */
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