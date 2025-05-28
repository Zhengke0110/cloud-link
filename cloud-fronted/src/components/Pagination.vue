<template>
    <div class="space-y-4">
        <!-- 分页导航 -->
        <div class="flex justify-center">
            <nav class="flex items-center space-x-2">
                <!-- 上一页按钮 -->
                <button @click="handlePrevPage" :disabled="currentPage <= 1" :class="[
                    'rounded-lg border p-2.5 transition-all duration-200',
                    currentPage <= 1
                        ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-400'
                        : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50 hover:border-gray-400 shadow-sm hover:shadow',
                ]">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                            clip-rule="evenodd" />
                    </svg>
                </button>

                <!-- 页码按钮 - 简单模式 (总页数 <= 7) -->
                <template v-if="totalPages <= 7">
                    <button v-for="page in totalPages" :key="page" @click="handleGoToPage(page)"
                        class="rounded-md border px-3 py-2" :class="getPageButtonClass(page)">
                        {{ page }}
                    </button>
                </template>

                <!-- 页码按钮 - 复杂模式 (总页数 > 7) -->
                <template v-else>
                    <!-- 首页 -->
                    <button @click="handleGoToPage(1)" class="rounded-md border px-3 py-2"
                        :class="getPageButtonClass(1)">
                        1
                    </button>

                    <!-- 左省略号 -->
                    <span v-if="showLeftEllipsis" class="px-2 text-gray-500">...</span>

                    <!-- 中间页码 -->
                    <button v-for="page in middlePages" :key="page" @click="handleGoToPage(page)"
                        class="rounded-md border px-3 py-2" :class="getPageButtonClass(page)">
                        {{ page }}
                    </button>

                    <!-- 右省略号 -->
                    <span v-if="showRightEllipsis" class="px-2 text-gray-500">...</span>

                    <!-- 尾页 -->
                    <button @click="handleGoToPage(totalPages)" class="rounded-md border px-3 py-2"
                        :class="getPageButtonClass(totalPages)">
                        {{ totalPages }}
                    </button>
                </template>

                <!-- 下一页按钮 -->
                <button @click="handleNextPage" :disabled="currentPage >= totalPages" :class="[
                    'rounded-lg border p-2.5 transition-all duration-200',
                    currentPage >= totalPages
                        ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-400'
                        : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50 hover:border-gray-400 shadow-sm hover:shadow',
                ]">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                            clip-rule="evenodd" />
                    </svg>
                </button>
            </nav>
        </div>

        <!-- 页面大小选择器 -->
        <div v-if="showPageSizeSelector"
            class="flex items-center justify-center gap-2 text-sm text-gray-600 bg-gray-50 rounded-lg p-3">
            <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>每页显示</span>
            <select v-model="internalPageSize" @change="handlePageSizeChange"
                class="rounded-md border border-gray-300 bg-white px-2 py-1 text-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500">
                <option v-for="size in pageSizeOptions" :key="size" :value="size">{{ size }}</option>
            </select>
            <span>条，总计</span>
            <span class="font-semibold text-blue-600">{{ totalCount.toLocaleString() }}</span>
            <span>条数据</span>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'

// 定义 Props
interface Props {
    currentPage: number
    totalPages: number
    totalCount: number
    pageSize: number
    pageSizeOptions?: number[]
    showPageSizeSelector?: boolean
}

const props = withDefaults(defineProps<Props>(), {
    pageSizeOptions: () => [10, 20, 50, 100],
    showPageSizeSelector: true
})

// 定义 Emits
interface Emits {
    (e: 'update:currentPage', value: number): void
    (e: 'update:pageSize', value: number): void
    (e: 'pageChange', page: number): void
    (e: 'pageSizeChange', size: number): void
}

const emit = defineEmits<Emits>()

// 内部状态管理
const internalPageSize = ref(props.pageSize)

// 监听 pageSize 属性变化
watch(() => props.pageSize, (newValue) => {
    internalPageSize.value = newValue
})

// 计算属性 - 左省略号显示条件
const showLeftEllipsis = computed(() => props.currentPage > 3)

// 计算属性 - 右省略号显示条件
const showRightEllipsis = computed(() => props.currentPage < props.totalPages - 2)

// 计算属性 - 中间页码
const middlePages = computed(() => {
    const result = []
    let start, end

    if (props.totalPages <= 7) {
        return []
    } else if (props.currentPage <= 3) {
        // 当前页靠近开始
        start = 2
        end = 5
    } else if (props.currentPage >= props.totalPages - 2) {
        // 当前页靠近结束
        start = props.totalPages - 4
        end = props.totalPages - 1
    } else {
        // 当前页在中间
        start = props.currentPage - 1
        end = props.currentPage + 1
    }

    for (let i = start; i <= end; i++) {
        if (i > 1 && i < props.totalPages) {
            result.push(i)
        }
    }
    return result
})

// 获取页码按钮样式
const getPageButtonClass = (page: number) => {
    return page === props.currentPage
        ? 'border-indigo-500 bg-indigo-500 text-white'
        : 'border-gray-300 bg-white text-gray-700 hover:bg-gray-50'
}

// 事件处理函数
const handleGoToPage = (page: number) => {
    if (page !== props.currentPage && page >= 1 && page <= props.totalPages) {
        emit('update:currentPage', page)
        emit('pageChange', page)
    }
}

const handlePrevPage = () => {
    if (props.currentPage > 1) {
        const newPage = props.currentPage - 1
        emit('update:currentPage', newPage)
        emit('pageChange', newPage)
    }
}

const handleNextPage = () => {
    if (props.currentPage < props.totalPages) {
        const newPage = props.currentPage + 1
        emit('update:currentPage', newPage)
        emit('pageChange', newPage)
    }
}

const handlePageSizeChange = () => {
    emit('update:pageSize', internalPageSize.value)
    emit('pageSizeChange', internalPageSize.value)
}
</script>
