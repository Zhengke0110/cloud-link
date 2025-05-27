<template>
    <div class="rounded-xl bg-white p-6 shadow-md">
        <div class="mb-4">
            <h3 class="text-lg font-semibold text-gray-800">时间范围</h3>
        </div>

        <div class="flex flex-wrap items-center gap-4">
            <div class="flex items-center gap-2">
                <label class="text-sm font-medium text-gray-700">起始时间：</label>
                <input type="date" v-model="startDate" @change="onDateChange" :max="endDate"
                    class="rounded-lg border border-gray-300 px-3 py-2 text-sm transition-colors focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-200" />
            </div>

            <div class="flex items-center gap-2">
                <label class="text-sm font-medium text-gray-700">结束时间：</label>
                <input type="date" v-model="endDate" @change="onDateChange" :min="startDate"
                    class="rounded-lg border border-gray-300 px-3 py-2 text-sm transition-colors focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-200" />
            </div>

            <button @click="handleRefresh" :disabled="loading"
                class="flex items-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-5 py-2.5 text-sm font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg hover:shadow-indigo-200 disabled:cursor-not-allowed disabled:from-gray-400 disabled:to-gray-400 disabled:shadow-none">
                <svg v-if="loading" class="h-4 w-4 animate-spin" xmlns="http://www.w3.org/2000/svg" fill="none"
                    viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                    </circle>
                    <path class="opacity-75" fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                        d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                        clip-rule="evenodd" />
                </svg>
                {{ loading ? '加载中...' : '刷新数据' }}
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Props {
    loading?: boolean
}

interface Emits {
    (e: 'refresh', params: { startTime: string, endTime: string }): void
    (e: 'dateChange', params: { startTime: string, endTime: string }): void
}

defineProps<Props>()
const emit = defineEmits<Emits>()

const startDate = ref<string>('')
const endDate = ref<string>('')

const formatDateToString = (date: string): string => {
    return date.replace(/-/g, '')
}

const onDateChange = () => {
    if (startDate.value && endDate.value) {
        emit('dateChange', {
            startTime: formatDateToString(startDate.value),
            endTime: formatDateToString(endDate.value)
        })
    }
}

const handleRefresh = () => {
    if (!startDate.value || !endDate.value) {
        console.warn('请选择有效的日期范围')
        return
    }

    emit('refresh', {
        startTime: formatDateToString(startDate.value),
        endTime: formatDateToString(endDate.value)
    })
}

const initDefaultDates = () => {
    const today = new Date()
    const oneMonthAgo = new Date(today)
    oneMonthAgo.setMonth(today.getMonth() - 1)

    endDate.value = today.toISOString().split('T')[0]
    startDate.value = oneMonthAgo.toISOString().split('T')[0]
}

onMounted(() => {
    initDefaultDates()
})

defineExpose({
    getDateRange: () => ({
        startTime: formatDateToString(startDate.value),
        endTime: formatDateToString(endDate.value)
    })
})
</script>
