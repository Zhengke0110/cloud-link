<template>
    <BaseModal :modelValue="modelValue" @update:modelValue="$emit('update:modelValue', $event)" :title="title" :id="id"
        content-padding="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
        <div class="sm:flex sm:items-start">
            <div
                class="mx-auto flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                </svg>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                <div class="mt-2">
                    <p class="text-sm text-gray-500">{{ message }}</p>
                    <div class="mt-3 rounded-md bg-gray-50 p-3">
                        <p class="text-sm font-medium text-gray-700">{{ detailsTitle }}：</p>
                        <slot name="details">
                            <!-- 默认详情内容 -->
                            <template v-for="(value, key) in details" :key="key">
                                <p class="mt-1 text-sm text-gray-500">{{ key }}: {{ value }}</p>
                            </template>
                        </slot>
                    </div>
                </div>
            </div>
        </div>

        <!-- 使用分离的页脚插槽 -->
        <template #separateFooter>
            <button type="button" @click="$emit('confirm')" :disabled="isLoading"
                class="inline-flex w-full justify-center rounded-md border border-transparent bg-red-600 px-4 py-2 text-base font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-70 sm:ml-3 sm:w-auto sm:text-sm">
                <!-- 内联加载图标 -->
                <svg v-if="isLoading" class="animate-spin mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg"
                    fill="none" viewBox="0 0 24 24" aria-label="加载中">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                </svg>
                {{ isLoading ? loadingText : confirmText }}
            </button>
            <button type="button" @click="$emit('update:modelValue', false)"
                class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-white px-4 py-2 text-base font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                {{ cancelText }}
            </button>
        </template>
    </BaseModal>
</template>

<script setup lang="ts">
import BaseModal from '@/components/BaseModal.vue';

// 定义组件属性
defineProps({
    // 控制模态框显示/隐藏的双向绑定值
    modelValue: {
        type: Boolean,
        required: true
    },
    // 模态框标题
    title: {
        type: String,
        default: '确认删除'
    },
    // 模态框ID，用于accessibility
    id: {
        type: String,
        default: 'confirm-delete-modal'
    },
    // 确认消息文本
    message: {
        type: String,
        default: '您确定要删除此项吗？此操作无法撤销。'
    },
    // 详情区域标题
    detailsTitle: {
        type: String,
        default: '详情'
    },
    // 详情信息对象 (key-value格式)
    details: {
        type: Object,
        default: () => ({})
    },
    // 确认按钮文本
    confirmText: {
        type: String,
        default: '确认删除'
    },
    // 取消按钮文本
    cancelText: {
        type: String,
        default: '取消'
    },
    // 加载中按钮文本
    loadingText: {
        type: String,
        default: '正在删除...'
    },
    // 是否为加载状态
    isLoading: {
        type: Boolean,
        default: false
    }
});

// 定义组件事件
defineEmits(['update:modelValue', 'confirm']);
</script>

<style scoped>
@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

.animate-spin {
    animation: spin 1s linear infinite;
}
</style>
