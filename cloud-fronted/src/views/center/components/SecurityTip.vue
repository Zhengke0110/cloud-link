<template>
    <div class="security-tip bg-white rounded-xl border border-gray-100 p-4 shadow-sm text-sm text-gray-500 flex items-center gap-3">
        <div class="flex-shrink-0 w-10 h-10 bg-blue-50 rounded-full flex items-center justify-center text-blue-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
        </div>
        <div>
            <slot>为了确保您的账户安全，建议定期更新密码并启用两步验证。如有任何安全相关问题，请及时联系客服。</slot>
        </div>
    </div>
</template>

<script setup lang="ts">
// SecurityTip组件负责显示安全提示信息
// 它接受一个默认插槽，如果没有提供内容，将显示默认的安全提示
</script>

<style scoped>
/* 安全提示区域样式 */
.security-tip {
    transition: all 0.3s ease;
}

.security-tip:hover {
    border-color: rgba(99, 102, 241, 0.2);
    box-shadow: 0 4px 8px -2px rgba(99, 102, 241, 0.1);
}
</style>