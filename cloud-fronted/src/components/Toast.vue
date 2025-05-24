<template>
    <Teleport to="body">
        <div class="fixed z-[9999] top-4 right-4 flex flex-col pointer-events-none w-80 max-w-[calc(100%-2rem)]">
            <TransitionGroup name="toast" tag="div" class="space-y-3">
                <div v-for="toast in toasts" :key="toast.id"
                    class="flex items-start p-4 rounded-xl shadow-lg pointer-events-auto transform transition-all duration-300 backdrop-blur-sm border-opacity-40 shadow-gray-800/5 relative overflow-hidden"
                    :class="[toastTypeClasses[toast.type], toast.position]">
                    <!-- 背景装饰效果 -->
                    <div class="absolute inset-0 -z-10 opacity-10 bg-gradient-to-br"
                        :class="toastGradientClasses[toast.type]"></div>

                    <!-- 左侧装饰条 -->
                    <div class="absolute left-0 top-0 bottom-0 w-1" :class="toastBarClasses[toast.type]"></div>

                    <!-- 图标区域 -->
                    <div class="flex-shrink-0 mr-3" v-if="toast.type !== 'default'">
                        <div class="flex items-center justify-center" :class="toastIconBgClasses[toast.type]">
                            <!-- 成功图标 -->
                            <svg v-if="toast.type === 'success'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                    clip-rule="evenodd" />
                            </svg>

                            <!-- 错误图标 -->
                            <svg v-else-if="toast.type === 'error'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                    clip-rule="evenodd" />
                            </svg>

                            <!-- 警告图标 -->
                            <svg v-else-if="toast.type === 'warning'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                                    clip-rule="evenodd" />
                            </svg>

                            <!-- 信息图标 -->
                            <svg v-else-if="toast.type === 'info'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2h1a1 1 0 100-2H9z"
                                    clip-rule="evenodd" />
                            </svg>
                        </div>
                    </div>

                    <!-- 内容区域 -->
                    <div class="flex-grow overflow-hidden min-w-0">
                        <div v-if="toast.title" class="mb-0.5 text-sm font-medium"
                            :class="toastTitleClasses[toast.type]">
                            {{ toast.title }}
                        </div>
                        <div class="text-sm text-gray-600">{{ toast.message }}</div>
                    </div>

                    <!-- 关闭按钮 -->
                    <button @click="() => removeToast(toast.id)"
                        class="flex-shrink-0 ml-3 p-0.5 rounded text-gray-400 transition-all duration-200 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 group">
                        <svg xmlns="http://www.w3.org/2000/svg"
                            class="h-4 w-4 transition-colors duration-200 group-hover:text-gray-600" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                clip-rule="evenodd" />
                        </svg>
                    </button>
                </div>
            </TransitionGroup>
        </div>
    </Teleport>
</template>

<script setup lang="ts">
import { useToast } from '@/composables/useToast';

// 获取toast状态和方法
const { toasts, removeToast } = useToast();

// 定义不同类型Toast的样式类
const toastTypeClasses = {
    success: 'bg-white/90 border border-emerald-100 text-gray-700',
    error: 'bg-white/90 border border-red-100 text-gray-700',
    warning: 'bg-white/90 border border-amber-100 text-gray-700',
    info: 'bg-white/90 border border-blue-100 text-gray-700',
    default: 'bg-white/90 border border-gray-100 text-gray-700'
};

// 定义不同类型Toast的渐变背景
const toastGradientClasses = {
    success: 'from-emerald-600 to-green-500',
    error: 'from-red-600 to-pink-500',
    warning: 'from-amber-500 to-yellow-400',
    info: 'from-blue-600 to-indigo-500',
    default: 'from-gray-500 to-gray-400'
};

// 定义不同类型Toast的左侧装饰条样式
const toastBarClasses = {
    success: 'bg-gradient-to-b from-emerald-400 to-green-500',
    error: 'bg-gradient-to-b from-red-400 to-pink-500',
    warning: 'bg-gradient-to-b from-amber-400 to-yellow-500',
    info: 'bg-gradient-to-b from-blue-400 to-indigo-500',
    default: 'bg-gradient-to-b from-gray-400 to-gray-500'
};

// 定义不同类型Toast的图标背景
const toastIconBgClasses = {
    success: 'text-emerald-500',
    error: 'text-red-500',
    warning: 'text-amber-500',
    info: 'text-blue-500',
    default: 'text-gray-500'
};

// 定义不同类型Toast的标题颜色
const toastTitleClasses = {
    success: 'text-emerald-700',
    error: 'text-red-700',
    warning: 'text-amber-700',
    info: 'text-blue-700',
    default: 'text-gray-700'
};
</script>
