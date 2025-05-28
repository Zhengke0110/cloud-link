<template>
    <div class="mb-6 flex justify-between items-center">
        <!-- 左侧可以放置搜索、筛选等功能 -->
        <div class="flex items-center gap-4">
            <slot name="left-actions"></slot>
        </div>
        
        <!-- 右侧操作按钮 -->
        <div class="flex items-center gap-3">
            <slot name="right-actions">
                <button
                    class="flex items-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-6 py-3 font-medium text-white shadow-lg transition-all duration-300 hover:shadow-xl hover:shadow-indigo-200 hover:scale-105"
                    @click="handleCreateClick">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
                            clip-rule="evenodd" />
                    </svg>
                    创建新链接
                </button>
            </slot>
        </div>
    </div>
</template>

<script setup lang="ts">
interface Emits {
    (e: 'create-click'): void;
}

const emit = defineEmits<Emits>();

const handleCreateClick = () => {
    emit('create-click');
};
</script>
