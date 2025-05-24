<template>
    <!-- 移除最外层的reveal-element和delay-300类，由PageLayout统一管理 -->
    <div class="max-w-full">
        <div class="profile-card bg-white rounded-2xl shadow-xl overflow-hidden transform transition-all duration-500">
            <!-- 顶部信息栏 - 优化背景 -->
            <div
                class="relative h-56 md:h-64 bg-gradient-to-r from-indigo-600 via-blue-600 to-indigo-600 flex justify-center">
                <!-- 装饰图形 -->
                <div class="absolute inset-0 overflow-hidden">
                    <div class="absolute -top-10 -right-10 w-40 h-40 rounded-full bg-white/10"></div>
                    <div class="absolute -bottom-20 -left-10 w-60 h-60 rounded-full bg-white/10"></div>
                    <!-- 波浪图形装饰 -->
                    <div class="absolute bottom-0 left-0 right-0 h-16 opacity-20">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" class="w-full h-full">
                            <path fill="#ffffff" fill-opacity="1"
                                d="M0,128L48,138.7C96,149,192,171,288,160C384,149,480,107,576,90.7C672,75,768,85,864,106.7C960,128,1056,160,1152,154.7C1248,149,1344,107,1392,85.3L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z">
                            </path>
                        </svg>
                    </div>
                    <!-- 粒子装饰 -->
                    <div class="particles absolute inset-0"></div>
                </div>

                <!-- 头像 - 增强效果 -->
                <div class="avatar-container absolute -bottom-16 w-36 h-36">
                    <div
                        class="avatar-border absolute inset-0 rounded-full bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-500 p-1">
                        <div class="avatar-frame rounded-full border-4 border-white shadow-lg overflow-hidden bg-white">
                            <img :src="userData.headImg" alt="用户头像" class="w-full h-full object-cover" />
                        </div>
                    </div>
                    <div
                        class="avatar-ring absolute inset-[-6px] rounded-full border-2 border-dashed border-indigo-300/50 animate-spin-slow">
                    </div>
                </div>
            </div>

            <!-- 用户信息内容 -->
            <div class="pt-24 pb-8 px-6 md:px-10">
                <!-- 用户名和认证状态 -->
                <div
                    class="flex flex-col sm:flex-row items-center justify-center sm:justify-between mb-8 space-y-3 sm:space-y-0">
                    <div class="flex items-center flex-wrap justify-center sm:justify-start gap-3">
                        <h3 class="text-2xl font-bold text-gray-800 hover-lift">{{ userData.username }}</h3>
                        <span class="badge-glow px-3 py-1 rounded-full text-xs font-medium 
                            bg-indigo-100 text-indigo-800 shadow-sm">{{ userData.auth === 'DEFAULT' ? '普通用户' : '高级用户'
                            }}</span>
                    </div>
                    <div class="flex items-center text-sm text-gray-500 space-x-1">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-indigo-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        <span>账户创建于：{{ userData.create_time }}</span>
                    </div>
                </div>

                <!-- 账户信息卡片组 - 优化布局和交互 -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- 账户基本信息 -->
                    <div
                        class="info-card group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-6 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">
                        <!-- 替换原来的渐变背景，使用更柔和的效果 -->
                        <div
                            class="card-decoration absolute inset-0 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                            <div
                                class="absolute inset-x-0 top-0 h-1.5 bg-gradient-to-r from-indigo-500 to-blue-500 rounded-t-xl">
                            </div>
                            <div
                                class="absolute inset-y-0 right-0 w-1.5 bg-gradient-to-b from-blue-500 to-indigo-500 rounded-r-xl">
                            </div>
                            <div
                                class="absolute inset-x-0 bottom-0 h-1.5 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-b-xl">
                            </div>
                            <div
                                class="absolute inset-y-0 left-0 w-1.5 bg-gradient-to-b from-indigo-500 to-blue-500 rounded-l-xl">
                            </div>
                        </div>

                        <div class="relative z-10 rounded-xl transition-all duration-500">
                            <div class="flex items-center justify-between mb-4">
                                <h4
                                    class="text-lg font-semibold text-gray-900 transition-all duration-300 group-hover:text-indigo-700">
                                    账户信息
                                </h4>
                                <span
                                    class="text-xs text-gray-400 group-hover:text-indigo-400 transition-colors duration-300">基本资料</span>
                            </div>

                            <ul class="space-y-4">
                                <li class="info-item flex items-center transition-all duration-300">
                                    <div
                                        class="flex h-10 w-10 items-center justify-center rounded-full bg-indigo-50 text-indigo-500 transition-all duration-300 group-hover:bg-indigo-100 group-hover:text-indigo-600 group-hover:shadow-sm">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                        </svg>
                                    </div>
                                    <div class="ml-4">
                                        <div
                                            class="text-xs text-gray-500 transition-colors duration-300 group-hover:text-indigo-500">
                                            账户ID</div>
                                        <div class="text-sm font-medium text-gray-800 transition-colors duration-300">
                                            {{ userData.accountNo }}
                                        </div>
                                    </div>
                                </li>
                                <li class="info-item flex items-center transition-all duration-300">
                                    <div
                                        class="flex h-10 w-10 items-center justify-center rounded-full bg-blue-50 text-blue-500 transition-all duration-300 group-hover:bg-blue-100 group-hover:text-blue-600 group-hover:shadow-sm">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                        </svg>
                                    </div>
                                    <div class="ml-4">
                                        <div
                                            class="text-xs text-gray-500 transition-colors duration-300 group-hover:text-blue-500">
                                            电子邮箱</div>
                                        <div class="text-sm font-medium text-gray-800 transition-colors duration-300">
                                            {{ userData.mail }}
                                        </div>
                                    </div>
                                </li>
                                <li class="info-item flex items-center transition-all duration-300">
                                    <div
                                        class="flex h-10 w-10 items-center justify-center rounded-full bg-purple-50 text-purple-500 transition-all duration-300 group-hover:bg-purple-100 group-hover:text-purple-600 group-hover:shadow-sm">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                                        </svg>
                                    </div>
                                    <div class="ml-4">
                                        <div
                                            class="text-xs text-gray-500 transition-colors duration-300 group-hover:text-purple-500">
                                            联系电话</div>
                                        <div class="text-sm font-medium text-gray-800 transition-colors duration-300">
                                            {{ userData.phone }}
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <!-- 账户统计数据 - 美化设计 -->
                    <div
                        class="stats-card group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-6 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">
                        <!-- 替换原来的渐变背景，使用更柔和的效果 -->
                        <div
                            class="card-decoration absolute inset-0 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                            <div
                                class="absolute inset-x-0 top-0 h-1.5 bg-gradient-to-r from-blue-500 to-cyan-500 rounded-t-xl">
                            </div>
                            <div
                                class="absolute inset-y-0 right-0 w-1.5 bg-gradient-to-b from-cyan-500 to-blue-500 rounded-r-xl">
                            </div>
                            <div
                                class="absolute inset-x-0 bottom-0 h-1.5 bg-gradient-to-r from-blue-500 to-cyan-500 rounded-b-xl">
                            </div>
                            <div
                                class="absolute inset-y-0 left-0 w-1.5 bg-gradient-to-b from-blue-500 to-cyan-500 rounded-l-xl">
                            </div>
                        </div>

                        <div class="relative z-10 rounded-xl transition-all duration-500">
                            <div class="flex items-center justify-between mb-5">
                                <h4
                                    class="text-lg font-semibold text-gray-900 transition-all duration-300 group-hover:text-blue-700">
                                    账户统计
                                </h4>
                                <span
                                    class="text-xs text-gray-400 group-hover:text-blue-400 transition-colors duration-300">数据总览</span>
                            </div>

                            <div class="grid grid-cols-2 gap-4">
                                <div v-for="stat in accountStats" :key="stat.id"
                                    class="stat-box group/item relative overflow-hidden text-center p-4 rounded-lg bg-white border border-gray-100 shadow-sm transition-all duration-300"
                                    :class="`hover:border-${stat.color}-200 hover:shadow-md`">
                                    <!-- 添加背景高光效果 -->
                                    <div
                                        :class="`absolute inset-0 bg-gradient-to-br from-${stat.color}-50 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300`">
                                    </div>
                                    <div class="relative z-10">
                                        <div
                                            :class="`text-2xl font-bold text-${stat.color}-600 mb-1 transition-all duration-300`">
                                            {{ stat.value }}<span v-if="stat.unit" class="text-lg"> {{ stat.unit
                                                }}</span>
                                        </div>
                                        <div class="text-xs text-gray-500 flex items-center justify-center gap-1">
                                            <svg v-if="stat.icon === 'link'" xmlns="http://www.w3.org/2000/svg"
                                                :class="`h-3.5 w-3.5 text-${stat.color}-400`" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'click'" xmlns="http://www.w3.org/2000/svg"
                                                :class="`h-3.5 w-3.5 text-${stat.color}-400`" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M15 15l-2 5L9 9l11 4-5 2zm0 0l5 5M7.188 2.239l.777 2.897M5.136 7.965l-2.898-.777M13.95 4.05l-2.122 2.122m-5.657 5.656l-2.12 2.122" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'group'" xmlns="http://www.w3.org/2000/svg"
                                                :class="`h-3.5 w-3.5 text-${stat.color}-400`" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                            </svg>
                                            <svg v-else-if="stat.icon === 'time'" xmlns="http://www.w3.org/2000/svg"
                                                :class="`h-3.5 w-3.5 text-${stat.color}-400`" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                            </svg>
                                            <span>{{ stat.title }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 按钮区域 - 优化样式和动画 -->
                <div class="mt-12 flex flex-wrap justify-center gap-4">
                    <button @click="$emit('update-profile')"
                        class="btn-primary px-6 py-3 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 text-white font-medium shadow-md transition-all duration-300 hover:shadow-lg hover:shadow-indigo-200 active:scale-95">
                        <div class="flex items-center gap-2">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                            <span>编辑个人信息</span>
                        </div>
                    </button>
                    <button @click="$emit('change-password')"
                        class="btn-secondary px-6 py-3 rounded-lg border border-gray-200 bg-white text-gray-700 font-medium shadow-sm transition-all duration-300 hover:border-indigo-300 hover:bg-indigo-50 hover:text-indigo-600 hover:shadow-md active:scale-95">
                        <div class="flex items-center gap-2">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                            <span>修改密码</span>
                        </div>
                    </button>
                </div>
            </div>
        </div>

        <!-- 使用SecurityTip组件替代原来的安全提示 -->
        <div class="mt-6">
            <SecurityTip />
        </div>
    </div>
</template>

<script setup lang="ts">
import SecurityTip from './SecurityTip.vue';

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
/* 粒子效果 */
.particles {
    background-image: radial-gradient(circle, rgba(255, 255, 255, 0.2) 1px, transparent 1px);
    background-size: 15px 15px;
}

/* 头像动画 */
.avatar-container {
    transform: translateY(0);
    transition: all 0.3s ease;
}

.avatar-ring {
    opacity: 0.6;
}

.animate-spin-slow {
    animation: spin 15s linear infinite;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

.profile-card:hover .avatar-container {
    transform: translateY(-5px);
}

/* 卡片效果增强 */
.info-card,
.stats-card {
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    background: linear-gradient(to bottom right, rgba(255, 255, 255, 1), rgba(249, 250, 251, 0.8));
}

.info-card:hover,
.stats-card:hover {
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

/* 按钮悬浮效果 */
.btn-primary,
.btn-secondary {
    position: relative;
    overflow: hidden;
}

.btn-primary::after,
.btn-secondary::after {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(to right,
            transparent,
            rgba(255, 255, 255, 0.2),
            transparent);
    transition: all 0.6s ease;
}

.btn-primary:hover::after,
.btn-secondary:hover::after {
    left: 100%;
}

/* 悬浮提升效果 */
.hover-lift {
    transition: all 0.3s ease;
}

.hover-lift:hover {
    transform: translateY(-2px);
    color: #4f46e5;
}

/* 统计卡片图标效果 */
.stat-box svg {
    transition: all 0.3s ease;
}

.stat-box:hover svg {
    transform: scale(1.2) rotate(15deg);
}

/* 元素显示动画 */
.reveal-element {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-element.delay-300 {
    transition-delay: 0.3s;
}

.reveal-element.revealed {
    opacity: 1;
    transform: translateY(0);
}

/* 新增卡片装饰样式 */
.card-decoration {
    z-index: 1;
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {

    .animate-spin-slow,
    .badge-glow::before,
    .btn-primary::after,
    .btn-secondary::after {
        animation: none !important;
        transition: none !important;
    }

    .reveal-element {
        opacity: 1;
        transform: none;
    }
}
</style>