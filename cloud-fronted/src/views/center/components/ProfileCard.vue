<template>
    <!-- 移除最外层的reveal-element和delay-300类，由PageLayout统一管理 -->
    <div class="max-w-full">
        <!-- 全新的扁平风格卡片，对整个卡片添加淡入动画 -->
        <GsapAnimation animation="fadeIn" :duration="0.8">
            <div class="bg-white rounded-3xl shadow-lg overflow-hidden">
                <!-- 顶部横幅设计 - 使用分割线风格 -->
                <div class="relative flex flex-col items-center">
                    <!-- 顶部横条背景 - 使用双色线性渐变（保持原样） -->
                    <div class="w-full h-28 bg-gradient-to-r from-sky-400 to-indigo-500"></div>

                    <!-- 波浪分隔线 -->
                    <div class="absolute -bottom-1 w-full">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 120" class="w-full">
                            <path fill="#ffffff"
                                d="M0,64L80,53.3C160,43,320,21,480,32C640,43,800,85,960,90.7C1120,96,1280,64,1360,48L1440,32L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z">
                            </path>
                        </svg>
                    </div>

                    <!-- 简洁现代的头像设计 - 保持原始样式 -->
                    <!-- 使用GsapAnimation但保持原有的头像样式 -->
                    <div class="absolute top-10 ring-4 ring-white rounded-full shadow-lg">
                        <GsapAnimation animation="scale" :duration="0.7" :delay="0.3">
                            <div class="rounded-full w-36 h-36 overflow-hidden">
                                <img :src="userData.headImg" alt="用户头像" class="w-full h-full object-cover" />
                            </div>
                        </GsapAnimation>
                    </div>
                </div>

                <!-- 用户信息内容 - 采用居中设计 -->
                <div class="pt-20 pb-8 px-6 md:px-10">
                    <!-- 用户名和认证状态 - 居中简洁布局，添加向上淡入动画 -->
                    <GsapAnimation animation="fadeInUp" :duration="0.6" :delay="0.6">
                        <div class="text-center mb-8">
                            <h3 class="text-2xl font-bold text-gray-800">{{ userData.username }}</h3>
                            <div class="flex items-center justify-center mt-2 gap-2">
                                <span class="px-3 py-1 bg-indigo-50 text-indigo-600 text-xs font-medium rounded-full">
                                    {{ userData.auth === 'DEFAULT' ? '普通用户' : '高级用户' }}
                                </span>
                                <span class="text-sm text-gray-500 flex items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 text-gray-400"
                                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                    {{ userData.create_time }}
                                </span>
                            </div>
                        </div>
                    </GsapAnimation>

                    <!-- 分割线，添加淡入动画 -->
                    <GsapAnimation animation="fadeIn" :duration="0.5" :delay="0.8">
                        <div
                            class="w-full max-w-xs mx-auto h-px bg-gradient-to-r from-transparent via-gray-200 to-transparent mb-8">
                        </div>
                    </GsapAnimation>

                    <!-- 用户卡片 - 简洁扁平风格 -->
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-10">
                        <!-- 账户信息卡片 - 简约设计，添加从左侧进入的动画 -->
                        <GsapAnimation animation="fadeInLeft" :duration="0.7" :delay="0.9">
                            <div class="bg-gray-50 rounded-2xl p-6 border border-gray-100">
                                <h4 class="flex items-center space-x-2 text-gray-700 font-medium mb-4">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500"
                                        viewBox="0 0 20 20" fill="currentColor">
                                        <path fill-rule="evenodd"
                                            d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                                            clip-rule="evenodd" />
                                    </svg>
                                    <span>账户信息</span>
                                </h4>

                                <div class="space-y-4">
                                    <div class="flex items-center p-3 bg-white rounded-xl">
                                        <div
                                            class="flex-shrink-0 w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center text-indigo-500">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 2a1 1 0 00-1 1v1a1 1 0 002 0V3a1 1 0 00-1-1zM4 4h3a3 3 0 006 0h3a2 2 0 012 2v9a2 2 0 01-2 2H4a2 2 0 01-2-2V6a2 2 0 012-2zm2.5 7a1.5 1.5 0 100-3 1.5 1.5 0 000 3zm2.45 4a2.5 2.5 0 10-4.9 0h4.9zM12 9a1 1 0 100 2h3a1 1 0 100-2h-3zm-1 4a1 1 0 011-1h2a1 1 0 110 2h-2a1 1 0 01-1-1z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                        <div class="ml-3">
                                            <p class="text-xs text-gray-500">账户ID</p>
                                            <p class="text-sm font-medium text-gray-800">{{ userData.accountNo }}</p>
                                        </div>
                                    </div>

                                    <div class="flex items-center p-3 bg-white rounded-xl">
                                        <div
                                            class="flex-shrink-0 w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center text-blue-500">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path
                                                    d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                                                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
                                            </svg>
                                        </div>
                                        <div class="ml-3">
                                            <p class="text-xs text-gray-500">电子邮箱</p>
                                            <p class="text-sm font-medium text-gray-800">{{ userData.mail }}</p>
                                        </div>
                                    </div>

                                    <div class="flex items-center p-3 bg-white rounded-xl">
                                        <div
                                            class="flex-shrink-0 w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center text-purple-500">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path
                                                    d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
                                            </svg>
                                        </div>
                                        <div class="ml-3">
                                            <p class="text-xs text-gray-500">联系电话</p>
                                            <p class="text-sm font-medium text-gray-800">{{ userData.phone }}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </GsapAnimation>

                        <!-- 账户统计卡片 - 简约网格布局，添加从右侧进入的动画 -->
                        <GsapAnimation animation="fadeInRight" :duration="0.7" :delay="0.9">
                            <div class="bg-gray-50 rounded-2xl p-6 border border-gray-100">
                                <h4 class="flex items-center space-x-2 text-gray-700 font-medium mb-4">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500"
                                        viewBox="0 0 20 20" fill="currentColor">
                                        <path
                                            d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z" />
                                    </svg>
                                    <span>账户统计</span>
                                </h4>

                                <div class="grid grid-cols-2 gap-3">
                                    <div v-for="stat in accountStats" :key="stat.id"
                                        :class="`bg-white p-3 rounded-xl border border-${stat.color}-100`">
                                        <div
                                            :class="`flex items-center justify-center w-8 h-8 rounded-lg bg-${stat.color}-100 text-${stat.color}-500 mx-auto mb-2`">
                                            <svg v-if="stat.icon === 'link'" xmlns="http://www.w3.org/2000/svg"
                                                class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'click'" xmlns="http://www.w3.org/2000/svg"
                                                class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M6.672 1.911a1 1 0 10-1.932.518l.259.966a1 1 0 001.932-.518l-.26-.966zM2.429 4.74a1 1 0 10-.517 1.932l.966.259a1 1 0 00.517-1.932l-.966-.26zm8.814-.569a1 1 0 00-1.415-1.414l-.707.707a1 1 0 101.415 1.415l.707-.708zm-7.071 7.072l.707-.707A1 1 0 003.465 9.12l-.708.707a1 1 0 001.415 1.415zm3.2-5.171a1 1 0 00-1.3 1.3l4 10a1 1 0 001.823.075l1.38-2.759 3.018 3.02a1 1 0 001.414-1.415l-3.019-3.02 2.76-1.379a1 1 0 00-.076-1.822l-10-4z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'group'" xmlns="http://www.w3.org/2000/svg"
                                                class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                                <path
                                                    d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'time'" xmlns="http://www.w3.org/2000/svg"
                                                class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                        <div :class="`text-xl font-bold text-${stat.color}-600 text-center`">
                                            {{ stat.value }}<span v-if="stat.unit" class="text-sm ml-0.5">{{ stat.unit
                                            }}</span>
                                        </div>
                                        <div class="text-xs text-gray-500 text-center mt-1">{{ stat.title }}</div>
                                    </div>
                                </div>
                            </div>
                        </GsapAnimation>
                    </div>

                    <!-- 操作按钮 - 扁平简约风格，添加向上淡入动画 -->
                    <GsapAnimation animation="fadeInUp" :duration="0.6" :delay="1.2">
                        <div class="flex flex-col sm:flex-row items-center justify-center gap-3 sm:gap-5">
                            <button @click="$emit('update-profile')"
                                class="w-full sm:w-auto px-6 py-3 rounded-xl bg-indigo-500 hover:bg-indigo-600 text-white font-medium transition-colors duration-200 flex items-center justify-center gap-2">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path
                                        d="M17.414 2.586a2 2 0 00-2.828 0L7 10.172V13h2.828l7.586-7.586a2 2 0 000-2.828z" />
                                    <path fill-rule="evenodd"
                                        d="M2 6a2 2 0 012-2h4a1 1 0 010 2H4v10h10v-4a1 1 0 112 0v4a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"
                                        clip-rule="evenodd" />
                                </svg>
                                <span>编辑个人信息</span>
                            </button>
                            <button @click="$emit('change-password')"
                                class="w-full sm:w-auto px-6 py-3 rounded-xl bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium transition-colors duration-200 flex items-center justify-center gap-2">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z"
                                        clip-rule="evenodd" />
                                </svg>
                                <span>修改密码</span>
                            </button>
                        </div>
                        <!-- 添加按钮操作提示说明 -->
                        <div class="text-center mt-4 text-sm text-gray-500">
                            <p>您可以通过上方按钮编辑个人信息或修改账户密码，确保您的账户安全</p>
                            <p class="mt-1 text-xs text-indigo-500">定期更新您的个人信息有助于我们为您提供更好的服务体验</p>
                        </div>
                    </GsapAnimation>
                </div>
            </div>
        </GsapAnimation>

        <!-- 使用SecurityTip组件，添加淡入动画 -->
        <GsapAnimation animation="fadeIn" :duration="0.6" :delay="1.5">
            <div class="mt-6">
                <SecurityTip />
            </div>
        </GsapAnimation>
    </div>
</template>

<script setup lang="ts">
import SecurityTip from './SecurityTip.vue';
import GsapAnimation from '@/components/GsapAnimation.vue';

// 定义用户数据和账户统计的接口
interface UserData {
    username: string;
    headImg: string;
    auth: string;
    create_time: string;
    accountNo: string | number;
    mail: string;
    phone: string;
    [key: string]: any; // 允许其他可能的字段
}

interface AccountStat {
    id: number | string;
    title: string;
    value: number | string;
    color: string;
    icon: string;
    unit?: string;
}

// 定义组件属性
defineProps<{
    userData: UserData;
    accountStats: AccountStat[];
}>();

// 定义事件
defineEmits<{
    'update-profile': [];
    'change-password': [];
}>();
</script>

<style scoped>
/* 移除没必要的动画，使用GSAP替代 */
@media (prefers-reduced-motion: reduce) {
    * {
        animation: none !important;
        transition: none !important;
    }
}
</style>