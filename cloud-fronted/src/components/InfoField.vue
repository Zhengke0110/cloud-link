<template>
    <div class="info-field">
        <div class="mb-1 text-xs text-gray-500">{{ label }}</div>
        <div class="flex items-center gap-2 rounded-lg border p-2 text-sm font-medium no-hover-animation" :class="[
            valueClass,
            borderClass || 'border-gray-100',
            bgClass || 'bg-gray-50'
        ]">
            <slot name="icon">
                <svg v-if="icon" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" :class="iconClass"
                    viewBox="0 0 20 20" fill="currentColor">
                    <path v-if="icon === 'clock'" fill-rule="evenodd"
                        d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                        clip-rule="evenodd" />
                    <path v-else-if="icon === 'update'" fill-rule="evenodd"
                        d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                        clip-rule="evenodd" />
                    <path v-else-if="icon === 'dot'" d="M8 9a1 1 0 100-2 1 1 0 000 2z" />
                </svg>
            </slot>

            <slot>
                <span :class="{ 'break-all': breakAll }">{{ value }}</span>
            </slot>

            <slot name="suffix"></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
defineProps({
    // 字段标签
    label: {
        type: String,
        required: true
    },
    // 字段值
    value: {
        type: [String, Number],
        default: ''
    },
    // 是否允许文本换行
    breakAll: {
        type: Boolean,
        default: false
    },
    // 内置图标类型: clock, update, dot, null
    icon: {
        type: String,
        default: ''
    },
    // 图标样式
    iconClass: {
        type: String,
        default: ''
    },
    // 文本颜色样式
    valueClass: {
        type: String,
        default: 'text-gray-800'
    },
    // 边框样式
    borderClass: {
        type: String,
        default: ''
    },
    // 背景样式
    bgClass: {
        type: String,
        default: ''
    }
});
</script>

<style scoped>
/* 禁用上移动画效果 */
.info-field .no-hover-animation {
    transform: none !important;
    transition: none !important;
}

/* 确保不受 PageLayout 或其他组件中通用选择器的影响 */
:deep(.info-field) {
    transform: none !important;
}

.info-field:hover {
    transform: none !important;
    box-shadow: none !important;
}

/* 阻止 InfoField 上移动画 */
.info-field>div,
.info-field>div:hover {
    transform: none !important;
    box-shadow: none !important;
}
</style>
