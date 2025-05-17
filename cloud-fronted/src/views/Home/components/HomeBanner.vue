<template>
    <!-- 主横幅部分 -->
    <div class="animate-slideUp relative overflow-hidden bg-gradient-to-br from-indigo-50 to-blue-100 py-16">
        <!-- 背景装饰元素 -->
        <div class="absolute inset-0 z-0">
            <div class="absolute -top-24 -right-24 h-64 w-64 rounded-full bg-indigo-100 opacity-70 blur-3xl"></div>
            <div class="absolute top-1/2 -left-24 h-72 w-72 rounded-full bg-blue-100 opacity-70 blur-3xl"></div>
            <div class="absolute bottom-12 right-1/4 h-48 w-48 rounded-full bg-purple-100 opacity-50 blur-3xl"></div>
            <div class="grid-pattern absolute inset-0 opacity-10"></div>
        </div>

        <!-- 漂浮元素 -->
        <div class="absolute inset-0 z-0 overflow-hidden">
            <div
                class="floating-element absolute left-[10%] top-[20%] h-10 w-10 rounded-lg border border-indigo-200 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-delay absolute left-[80%] top-[15%] h-12 w-12 rotate-12 rounded-lg border border-blue-200 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-slow absolute left-[20%] bottom-[20%] h-8 w-8 -rotate-12 rounded-full border border-purple-200 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-delay-more absolute right-[15%] bottom-[25%] h-14 w-14 rotate-45 rounded-lg border border-indigo-200 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
        </div>

        <div class="container relative z-10 mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex flex-col-reverse items-center justify-between gap-8 md:flex-row"
                :class="{ 'text-center': deviceType.isMobile }">
                <div class="mb-6 w-full md:mb-0 md:max-w-xl md:text-left">
                    <div
                        class="mb-4 inline-block rounded-full bg-indigo-100 px-3 py-1 text-sm font-medium text-indigo-800">
                        简单 · 高效 · 安全
                    </div>

                    <h2 class="mb-4 bg-gradient-to-r from-indigo-600 to-blue-500 bg-clip-text font-bold tracking-tight text-transparent"
                        :class="[
                            deviceType.isMobile ? 'text-3xl' :
                                deviceType.isTablet ? 'text-4xl' : 'text-5xl'
                        ]">
                        Timu短链平台
                    </h2>

                    <p class="mb-8 text-gray-600" :class="[deviceType.isMobile ? 'text-base' : 'text-lg']">
                        轻松创建、管理和分析短链接，提升营销效果与转化率。利用精准数据，洞悉用户行为，做出明智决策。
                    </p>

                    <div class="flex flex-wrap gap-4" :class="{ 'justify-center': deviceType.isMobile }">
                        <button
                            class="flex items-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-6 py-3 font-medium text-white shadow-lg transition-all hover:translate-y-[-2px] hover:shadow-indigo-500/30 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z"
                                    clip-rule="evenodd" />
                            </svg>
                            立即创建短链
                        </button>
                        <button
                            class="flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-6 py-3 font-medium text-gray-700 shadow-sm transition-all hover:border-indigo-300 hover:bg-gray-50 hover:text-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                                <path fill-rule="evenodd"
                                    d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                                    clip-rule="evenodd" />
                            </svg>
                            观看演示
                        </button>
                    </div>

                </div>

                <div class="relative w-full max-w-md">
                    <div class="mockup-window relative z-20 rounded-xl border border-gray-200 bg-white/80 p-4 shadow-2xl backdrop-blur-sm"
                        :class="{ 'scale-90': deviceType.isMobile }">
                        <div class="mb-6 flex items-center justify-between border-b border-gray-100 pb-4">
                            <div class="flex items-center">
                                <div class="mr-2 h-3 w-3 rounded-full bg-red-500"></div>
                                <div class="mr-2 h-3 w-3 rounded-full bg-yellow-500"></div>
                                <div class="h-3 w-3 rounded-full bg-green-500"></div>
                            </div>
                            <div class="flex-1 text-center text-sm text-gray-500">Timu短链生成器</div>
                            <div class="w-16"></div>
                        </div>
                        <div class="space-y-4">
                            <div class="rounded-lg bg-gray-50 p-4">
                                <div class="mb-2 text-sm font-medium text-gray-700">原始链接</div>
                                <div
                                    class="flex items-center gap-2 rounded-md border border-gray-200 bg-white p-2 text-sm text-gray-500">
                                    <span
                                        class="truncate">https://www.example.com/promotion/summer-sale/2023/special-offer?ref=social&amp;campaign=summer</span>
                                </div>
                            </div>
                            <div class="mb-4 flex items-center">
                                <div class="h-px flex-1 bg-gray-200"></div>
                                <div class="mx-4">
                                    <svg xmlns="http://www.w3.org/2000/svg"
                                        class="h-6 w-6 animate-bounce text-indigo-500" fill="none" viewBox="0 0 24 24"
                                        stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                                    </svg>
                                </div>
                                <div class="h-px flex-1 bg-gray-200"></div>
                            </div>
                            <div class="rounded-lg bg-indigo-50 p-4">
                                <div class="mb-2 text-sm font-medium text-indigo-700">短链接</div>
                                <div
                                    class="flex items-center justify-between gap-2 rounded-md border border-indigo-200 bg-white p-2">
                                    <span class="text-base font-medium text-indigo-700">timu.link/s4Rxt8</span>
                                    <button
                                        class="rounded px-2 py-1 text-xs font-medium text-indigo-600 hover:bg-indigo-50">复制</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 装饰性元素 -->
                    <div class="absolute -bottom-6 -left-6 z-10 h-12 w-12 rounded-lg bg-gradient-to-br from-purple-500 to-indigo-500 shadow-lg"
                        :class="{ 'h-8 w-8 -bottom-4 -left-4': deviceType.isMobile }">
                    </div>
                    <div class="absolute -right-6 -top-6 z-10 h-10 w-10 rounded-lg bg-gradient-to-br from-blue-500 to-cyan-500 shadow-lg"
                        :class="{ 'h-7 w-7 -right-4 -top-4': deviceType.isMobile }">
                    </div>

                    <!-- 数据卡片装饰 -->
                    <div class="data-card absolute -right-10 -bottom-16 z-0 transform rounded-lg border border-gray-200 bg-white p-4 shadow-lg"
                        :class="{ 'hidden md:block': deviceType.isDesktop, 'right-0 -bottom-12 scale-75': deviceType.isMobile || deviceType.isTablet }">
                        <div class="mb-1 text-xs font-medium text-gray-500">总链接点击量</div>
                        <div class="text-lg font-bold text-gray-800">2,587</div>
                        <div class="flex items-center text-xs text-green-500">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mr-1 h-3 w-3" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                            </svg>
                            <span>12% 增长</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 特性指示器 - 修改为在所有设备上显示 -->
            <div class="mt-16 flex flex-wrap justify-center gap-4 gap-y-6 text-center md:gap-8">
                <div class="feature-item w-full sm:w-auto">
                    <div class="mb-2 flex items-center justify-center gap-2 font-medium text-gray-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                clip-rule="evenodd" />
                        </svg>
                        实时数据分析
                    </div>
                    <p class="text-sm text-gray-500">立即获取访问数据</p>
                </div>
                <div class="feature-item w-full sm:w-auto">
                    <div class="mb-2 flex items-center justify-center gap-2 font-medium text-gray-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                clip-rule="evenodd" />
                        </svg>
                        安全保障
                    </div>
                    <p class="text-sm text-gray-500">防止链接滥用</p>
                </div>
                <div class="feature-item w-full sm:w-auto">
                    <div class="mb-2 flex items-center justify-center gap-2 font-medium text-gray-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path
                                d="M5 4a1 1 0 00-2 0v7.268a2 2 0 000 3.464V16a1 1 0 102 0v-1.268a2 2 0 000-3.464V4zM11 4a1 1 0 10-2 0v1.268a2 2 0 000 3.464V16a1 1 0 102 0V8.732a2 2 0 000-3.464V4zM16 3a1 1 0 011 1v7.268a2 2 0 010 3.464V16a1 1 0 11-2 0v-1.268a2 2 0 010-3.464V4a1 1 0 011-1z" />
                        </svg>
                        可定制选项
                    </div>
                    <p class="text-sm text-gray-500">全方位自定义链接</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { deviceType } from "@/utils/flexible";
</script>

<style scoped>
.grid-pattern {
    background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%236366F1' fill-opacity='0.1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

@keyframes floating {
    0% {
        transform: translateY(0px) rotate(0deg);
    }

    50% {
        transform: translateY(-15px) rotate(2deg);
    }

    100% {
        transform: translateY(0px) rotate(0deg);
    }
}

.floating-element {
    animation: floating 6s ease-in-out infinite;
}

.floating-element-delay {
    animation: floating 8s ease-in-out infinite;
    animation-delay: 1s;
}

.floating-element-slow {
    animation: floating 10s ease-in-out infinite;
    animation-delay: 2s;
}

.floating-element-delay-more {
    animation: floating 9s ease-in-out infinite;
    animation-delay: 3s;
}

.mockup-window {
    transition: all 0.3s ease;
}

.mockup-window:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.data-card {
    animation: slideIn 0.5s ease-out forwards;
    animation-delay: 0.5s;
    opacity: 0;
}

@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(30px);
    }

    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.feature-item {
    position: relative;
    max-width: 180px;
    opacity: 0;
    animation: fadeUp 0.5s ease-out forwards;
}

.feature-item:nth-child(1) {
    animation-delay: 0.2s;
}

.feature-item:nth-child(2) {
    animation-delay: 0.4s;
}

.feature-item:nth-child(3) {
    animation-delay: 0.6s;
}

@keyframes fadeUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 添加移动端特定样式 */
@media (max-width: 768px) {
    .mockup-window {
        margin: 0 auto;
    }

    .feature-item {
        max-width: none;
        padding: 0 1rem;
    }
}
</style>