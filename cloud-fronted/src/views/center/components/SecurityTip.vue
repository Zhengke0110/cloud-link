<template>
    <div
        class="security-tip relative overflow-hidden rounded-2xl border border-blue-100 bg-gradient-to-br from-blue-50 to-indigo-50 shadow-sm transition-all duration-300">
        <!-- 装饰元素 -->
        <div class="absolute top-0 right-0 -mt-4 -mr-4 w-24 h-24 bg-blue-100 rounded-full opacity-20"></div>
        <div class="absolute bottom-0 left-0 -mb-8 -ml-8 w-32 h-32 bg-indigo-100 rounded-full opacity-20"></div>

        <!-- 安全提示内容 -->
        <div class="p-6 relative z-10">
            <div class="flex items-start space-x-4">
                <!-- 图标 - 移除悬停变换效果 -->
                <div
                    class="shield-icon flex-shrink-0 w-12 h-12 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center shadow-md relative overflow-hidden">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                    </svg>
                    <!-- 移除动画效果 -->
                </div>

                <!-- 文本内容 -->
                <div class="flex-grow">
                    <div class="flex items-center justify-between mb-2">
                        <h3 class="text-lg font-bold text-gray-800">账户安全提示</h3>
                        <span
                            class="security-status px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700 border border-green-200 shadow-sm flex items-center gap-1">
                            <span class="w-1.5 h-1.5 bg-green-500 rounded-full"></span>
                            安全状态良好
                        </span>
                    </div>

                    <p class="text-sm text-gray-600 mb-4">
                        保护您的账户安全对我们来说至关重要。请定期更新密码并启用多重验证，以确保您的个人资料和数据始终受到保护。
                    </p>

                    <!-- 安全提示列表 - 移除悬停效果 -->
                    <div class="security-checklist space-y-3">
                        <div class="security-check-item flex items-center text-sm text-gray-700">
                            <div class="w-6 h-6 rounded-full bg-green-100 flex items-center justify-center mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-green-600"
                                    viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                        clip-rule="evenodd" />
                                </svg>
                            </div>
                            <span>定期更新您的密码（建议每3个月更新一次）</span>
                        </div>
                        <div class="security-check-item flex items-center text-sm text-gray-700">
                            <div class="w-6 h-6 rounded-full bg-green-100 flex items-center justify-center mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-green-600"
                                    viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                        clip-rule="evenodd" />
                                </svg>
                            </div>
                            <span>使用强密码（包含大小写字母、数字和特殊字符）</span>
                        </div>
                        <div class="security-check-item flex items-center text-sm text-gray-700">
                            <div class="w-6 h-6 rounded-full bg-yellow-100 flex items-center justify-center mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-yellow-600" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                                </svg>
                            </div>
                            <span>启用双因素认证以增强账户安全性</span>
                        </div>
                    </div>

                    <!-- 操作按钮 - 简化悬停效果 -->
                    <div class="mt-5 flex space-x-3">
                        <button
                            class="security-btn px-4 py-2 rounded-lg bg-gradient-to-r from-blue-500 to-indigo-500 text-white text-sm font-medium flex items-center gap-1.5 shadow-sm">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                            </svg>
                            安全检查
                        </button>
                        <button
                            class="security-btn-outline px-4 py-2 rounded-lg border border-gray-200 bg-white text-gray-600 text-sm font-medium flex items-center gap-1.5">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            了解更多
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
// 可以在这里添加组件逻辑（如果需要）
</script>

<style scoped>
.security-tip {
    /* 移除悬停时的样式变化 */
    backdrop-filter: blur(10px);
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.shield-icon {
    /* 移除视角效果 */
    transition: all 0.3s ease;
}

/* 移除悬停效果 */
.security-btn {
    transition: background-color 0.2s ease;
}

.security-btn:active {
    transform: translateY(1px);
}

.security-btn-outline {
    transition: all 0.2s ease;
}

.security-btn-outline:active {
    transform: translateY(1px);
}

/* 仅保留最基本的无障碍支持 */
@media (prefers-reduced-motion: reduce) {
    * {
        transition: none !important;
        animation: none !important;
    }
}
</style>