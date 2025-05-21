<template>
    <svg :class="['animate-spin', size]" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
        :aria-label="label">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor"
            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
        </path>
    </svg>
</template>

<script setup lang="ts">
defineProps({
    size: {
        type: String,
        default: 'h-5 w-5'
    },
    label: {
        type: String,
        default: '加载中'
    }
});
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
