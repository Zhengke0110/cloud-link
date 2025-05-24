<template>
    <div class="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <!-- 取消按钮 -->
        <button type="button" @click="$emit('cancel')"
            class="mt-2 w-full rounded-lg border border-gray-300 px-4 py-3 text-center text-base font-medium text-gray-700 transition-colors duration-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">
            {{ cancelText }}
        </button>

        <!-- 提交按钮 -->
        <button :type="submitType" :disabled="isLoading || disabled"
            class="flex w-full items-center justify-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-3 text-base font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg disabled:cursor-not-allowed disabled:opacity-70 sm:w-auto">
            <svg v-if="isLoading" :class="['animate-spin', 'h-5', 'w-5', 'animate-spin ', 'text-white']"
                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" aria-label="加载中">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                </path>
            </svg>

            {{ isLoading ? loadingText : submitText }}
        </button>
    </div>
</template>

<script setup lang="ts">

type ButtonType = 'submit' | 'button' | 'reset';
defineProps({
    // 提交按钮文本
    submitText: {
        type: String,
        default: '确认'
    },
    // 取消按钮文本
    cancelText: {
        type: String,
        default: '取消'
    },
    // 加载中文本
    loadingText: {
        type: String,
        default: '处理中...'
    },
    // 是否处于加载状态
    isLoading: {
        type: Boolean,
        default: false
    },
    // 按钮是否禁用
    disabled: {
        type: Boolean,
        default: false
    },
    // 提交按钮类型
    submitType: {
        type: String as () => ButtonType,
        default: 'submit',
        validator: (value: string) => ['submit', 'button', 'reset'].includes(value)
    }
});

defineEmits(['cancel', 'submit']);
</script>
