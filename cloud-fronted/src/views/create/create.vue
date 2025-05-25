<template>
    <PageLayout tag="短链接创建工具" title="创建您的短链接" description="输入您想要缩短的链接，快速生成短链接">
        <!-- 创建短链卡片 -->
        <GsapAnimation animation="fadeInUp" :delay="0.3" :duration="0.8">
            <div class="max-w-2xl mx-auto">
                <div
                    class="group relative overflow-hidden rounded-2xl border border-gray-100 bg-white p-0.5 shadow-lg transition-all duration-500 hover:shadow-xl hover:-translate-y-2 hover:z-20">

                    <!-- 渐变边框 -->
                    <div
                        class="absolute inset-0 rounded-2xl opacity-0 transition-opacity duration-500 group-hover:opacity-100 bg-gradient-to-br from-indigo-400 via-blue-500 to-indigo-600">
                    </div>

                    <!-- 卡片内容 -->
                    <div class="relative bg-white rounded-2xl overflow-hidden">
                        <!-- 顶部标题栏 -->
                        <div
                            class="p-6 pb-4 text-center relative overflow-hidden bg-gradient-to-r from-indigo-600 via-blue-700 to-indigo-600">
                            <!-- 装饰圆圈 -->
                            <div class="absolute -top-10 -right-10 w-24 h-24 rounded-full bg-white/10"></div>
                            <div class="absolute -bottom-16 -left-8 w-32 h-32 rounded-full bg-white/5"></div>

                            <h3 class="text-xl md:text-2xl font-bold text-white relative z-10">生成短链接</h3>
                            <p class="mt-1 text-white/80 text-sm">输入完整链接以创建短链接</p>
                        </div>

                        <!-- 输入表单 -->
                        <div class="p-6">
                            <form @submit.prevent="createShortLink" class="space-y-6">
                                <!-- 原始链接输入 -->
                                <div>
                                    <label for="originalUrl"
                                        class="block text-sm font-medium text-gray-700 mb-1">完整链接地址</label>
                                    <div class="relative">
                                        <input type="text" id="originalUrl" v-model="linkForm.originalUrl"
                                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                                            placeholder="请输入您想要缩短的链接，如 https://example.com/page" required
                                            @input="validateUrl" />
                                        <button v-if="linkForm.originalUrl" type="button"
                                            @click="linkForm.originalUrl = ''; urlError = ''"
                                            class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </button>
                                    </div>
                                    <p v-if="urlError" class="mt-1 text-xs text-red-500">{{ urlError }}</p>
                                    <p v-else class="mt-1 text-xs text-gray-500">输入以http://或https://开头的有效网址</p>
                                </div>

                                <!-- 标题输入 -->
                                <div>
                                    <label for="title" class="block text-sm font-medium text-gray-700 mb-1">链接标题
                                        (选填)</label>
                                    <div class="relative">
                                        <input type="text" id="title" v-model="linkForm.title"
                                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                                            placeholder="为您的链接添加一个易于识别的标题" maxlength="50" />
                                        <button v-if="linkForm.title" type="button" @click="linkForm.title = ''"
                                            class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </button>
                                    </div>
                                    <p class="mt-1 text-xs text-gray-500">最多50个字符</p>
                                </div>

                                <!-- 分组选择 - 新增 -->
                                <div>
                                    <label for="groupId"
                                        class="block text-sm font-medium text-gray-700 mb-1">选择分组</label>
                                    <div class="relative">
                                        <select id="groupId" v-model="linkForm.groupId"
                                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 appearance-none bg-white touch-manipulation">
                                            <option v-for="group in groupData" :key="group.id" :value="group.id">
                                                {{ group.title }}
                                            </option>
                                        </select>
                                        <div
                                            class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-gray-500">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                    </div>
                                    <p class="mt-1 text-xs text-gray-500">选择要归属的分组</p>
                                </div>

                                <!-- 过期时间选择 -->
                                <div>
                                    <label for="expired" class="block text-sm font-medium text-gray-700 mb-1">过期时间
                                        (选填)</label>

                                    <!-- 预设过期时间选项 -->
                                    <div class="mb-3 flex flex-wrap gap-2">
                                        <button type="button" @click="setPresetExpiry(1)"
                                            class="px-3 py-1.5 text-xs rounded-full border transition-colors duration-200 touch-manipulation min-h-[44px] sm:min-w-0 min-w-[calc(50%-0.5rem)] flex justify-center items-center"
                                            :class="presetExpiry === 1 ? 'bg-indigo-50 border-indigo-300 text-indigo-700' : 'border-gray-300 text-gray-600'">
                                            1天后
                                        </button>
                                        <button type="button" @click="setPresetExpiry(7)"
                                            class="px-3 py-1.5 text-xs rounded-full border transition-colors duration-200 touch-manipulation min-h-[44px] sm:min-w-0 min-w-[calc(50%-0.5rem)] flex justify-center items-center"
                                            :class="presetExpiry === 7 ? 'bg-indigo-50 border-indigo-300 text-indigo-700' : 'border-gray-300 text-gray-600'">
                                            1周后
                                        </button>
                                        <button type="button" @click="setPresetExpiry(30)"
                                            class="px-3 py-1.5 text-xs rounded-full border transition-colors duration-200 touch-manipulation min-h-[44px] sm:min-w-0 min-w-[calc(50%-0.5rem)] flex justify-center items-center"
                                            :class="presetExpiry === 30 ? 'bg-indigo-50 border-indigo-300 text-indigo-700' : 'border-gray-300 text-gray-600'">
                                            1个月后
                                        </button>
                                        <button type="button" @click="setPresetExpiry(0)"
                                            class="px-3 py-1.5 text-xs rounded-full border transition-colors duration-200 touch-manipulation min-h-[44px] sm:min-w-0 min-w-[calc(50%-0.5rem)] flex justify-center items-center"
                                            :class="presetExpiry === 0 ? 'bg-indigo-50 border-indigo-300 text-indigo-700' : 'border-gray-300 text-gray-600'">
                                            自定义
                                        </button>
                                    </div>

                                    <!-- 日期时间选择器 -->
                                    <div v-if="presetExpiry === 0" class="relative">
                                        <div class="flex flex-col sm:flex-row gap-2">
                                            <!-- 日期选择器 -->
                                            <div class="flex-grow relative">
                                                <input type="date" v-model="expiryDate"
                                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 appearance-none pr-10 min-h-[48px] text-base"
                                                    :min="minDate" />
                                                <div
                                                    class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-gray-500">
                                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                                        viewBox="0 0 20 20" fill="currentColor">
                                                        <path fill-rule="evenodd"
                                                            d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z"
                                                            clip-rule="evenodd" />
                                                    </svg>
                                                </div>
                                            </div>

                                            <!-- 时间选择器 -->
                                            <div class="flex-grow relative">
                                                <input type="time" v-model="expiryTime"
                                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 appearance-none pr-10 min-h-[48px] text-base" />
                                                <div
                                                    class="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-gray-500">
                                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5"
                                                        viewBox="0 0 20 20" fill="currentColor">
                                                        <path fill-rule="evenodd"
                                                            d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                            clip-rule="evenodd" />
                                                    </svg>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 日期预览 -->
                                    <div v-if="formattedExpiryDate"
                                        class="mt-2 text-xs bg-indigo-50 text-indigo-700 rounded-md p-2 flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 flex-shrink-0"
                                            viewBox="0 0 20 20" fill="currentColor">
                                            <path fill-rule="evenodd"
                                                d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                clip-rule="evenodd" />
                                        </svg>
                                        <span>链接将于 <strong>{{ formattedExpiryDate }}</strong> 过期</span>
                                    </div>
                                    <p class="mt-1 text-xs text-gray-500">{{ formattedExpiryDate ? '设置后，链接在过期时间后将不可访问' :
                                        '如果不设置，链接将永不过期' }}</p>
                                </div>

                                <!-- 提交按钮 -->
                                <div>
                                    <button type="submit" :disabled="isSubmitting || !isFormValid"
                                        class="w-full py-3 px-4 bg-gradient-to-r from-indigo-600 to-blue-600 text-white font-medium rounded-lg shadow-md hover:shadow-lg hover:shadow-indigo-200 transition-all duration-300 flex items-center justify-center disabled:opacity-70 disabled:cursor-not-allowed disabled:bg-gradient-to-r disabled:from-gray-400 disabled:to-gray-500 min-h-[44px] touch-manipulation">
                                        <svg v-if="isSubmitting" class="animate-spin -ml-1 mr-2 h-5 w-5 text-white"
                                            xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                                stroke-width="4"></circle>
                                            <path class="opacity-75" fill="currentColor"
                                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                                            </path>
                                        </svg>
                                        {{ isSubmitting ? '生成中...' : '生成短链接' }}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- 结果展示区域 -->
                <GsapAnimation v-if="shortLinkResult" animation="fadeIn" :delay="0.2" :duration="0.7">
                    <div class="mt-8">
                        <div class="bg-white rounded-2xl border border-gray-100 shadow-lg overflow-hidden">
                            <!-- 结果标题栏 -->
                            <div class="p-4 md:p-5 bg-gradient-to-r from-green-600 to-teal-600 text-white">
                                <div class="flex items-center justify-between">
                                    <h3 class="text-base md:text-lg font-medium">短链接已生成</h3>
                                    <span
                                        class="bg-white/20 text-xs font-medium px-2 py-0.5 rounded-full">复制后即可使用</span>
                                </div>
                            </div>

                            <!-- 结果内容 -->
                            <div class="p-4 md:p-6">
                                <!-- 短链接信息 -->
                                <div class="space-y-4 md:space-y-5">
                                    <!-- 短链接 -->
                                    <div>
                                        <div class="flex items-center justify-between mb-1">
                                            <span class="text-xs font-medium text-gray-500">短链接</span>
                                            <span class="text-xs text-green-600 font-medium">已成功创建</span>
                                        </div>
                                        <div class="flex flex-col md:flex-row gap-2 md:gap-0">
                                            <div
                                                class="bg-gray-50 flex-grow rounded-lg md:rounded-l-lg md:rounded-r-none border border-gray-200 p-2 md:p-3 font-medium text-indigo-600 break-all md:border-r-0">
                                                {{ shortLinkResult.shortUrl }}
                                            </div>
                                            <button @click="copyToClipboard(shortLinkResult.shortUrl)"
                                                class="bg-indigo-600 hover:bg-indigo-700 text-white py-2 px-4 rounded-lg md:rounded-l-none md:rounded-r-lg transition-colors duration-300 md:min-w-[80px] text-center touch-manipulation min-h-[44px]">
                                                {{ copied ? '已复制' : '复制' }}
                                            </button>
                                        </div>
                                    </div>

                                    <!-- 原始链接 -->
                                    <div>
                                        <div class="mb-1">
                                            <span class="text-xs font-medium text-gray-500">原始链接</span>
                                        </div>
                                        <div
                                            class="bg-gray-50 rounded-lg border border-gray-200 p-3 text-gray-600 text-sm break-all">
                                            {{ shortLinkResult.originalUrl }}
                                        </div>
                                    </div>

                                    <!-- 链接信息 -->
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-3 md:gap-4">
                                        <div>
                                            <span class="text-xs font-medium text-gray-500 block mb-1">创建时间</span>
                                            <div
                                                class="bg-gray-50 rounded-lg border border-gray-200 p-2 md:p-3 text-gray-600 text-xs md:text-sm">
                                                {{ formatDateTime(shortLinkResult.createTime) }}
                                            </div>
                                        </div>
                                        <div>
                                            <span class="text-xs font-medium text-gray-500 block mb-1">过期时间</span>
                                            <div
                                                class="bg-gray-50 rounded-lg border border-gray-200 p-2 md:p-3 text-gray-600 text-xs md:text-sm">
                                                {{ shortLinkResult.expired ? formatDateTime(shortLinkResult.expired) :
                                                    '永不过期' }}
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 二维码 -->
                                    <div v-if="shortLinkResult.qrCode" class="text-center">
                                        <span class="text-xs font-medium text-gray-500 block mb-2">链接二维码</span>
                                        <div class="inline-block bg-white p-2 border border-gray-200 rounded-lg">
                                            <img :src="shortLinkResult.qrCode" alt="QR Code"
                                                class="h-24 w-24 md:h-32 md:w-32">
                                        </div>
                                    </div>
                                </div>

                                <!-- 操作按钮 -->
                                <div class="mt-5 md:mt-6 flex flex-col md:flex-row md:justify-end gap-3">
                                    <button @click="resetForm"
                                        class="order-2 md:order-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors duration-300 text-sm md:text-base touch-manipulation min-h-[44px]">
                                        创建新链接
                                    </button>
                                    <button @click="shareLink"
                                        class="order-1 md:order-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-300 flex items-center justify-center gap-1 text-sm md:text-base touch-manipulation min-h-[44px]">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none"
                                            viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z" />
                                        </svg>
                                        分享链接
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </GsapAnimation>

                <!-- 错误提示框 -->
                <div v-if="errorMessage"
                    class="mt-4 bg-red-50 border border-red-200 rounded-lg p-4 text-red-700 text-sm flex items-start gap-3">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 flex-shrink-0 text-red-500" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <div>{{ errorMessage }}</div>
                </div>

                <!-- 使用说明卡片 -->
                <GsapAnimation v-if="!shortLinkResult" animation="fadeInUp" :delay="0.5" :duration="0.7">
                    <div class="mt-12">
                        <div class="bg-white rounded-xl border border-gray-100 p-6 shadow-md">
                            <h3 class="text-lg font-semibold text-gray-900 mb-4">如何使用短链接</h3>

                            <div class="space-y-4">
                                <div class="flex items-start">
                                    <div
                                        class="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-indigo-100 text-indigo-800">
                                        <span class="text-xs font-bold">1</span>
                                    </div>
                                    <p class="ml-3 text-sm text-gray-600">输入您想要缩短的完整链接（必须以 http:// 或 https:// 开头）</p>
                                </div>
                                <div class="flex items-start">
                                    <div
                                        class="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-indigo-100 text-indigo-800">
                                        <span class="text-xs font-bold">2</span>
                                    </div>
                                    <p class="ml-3 text-sm text-gray-600">选填链接标题，方便您后续管理识别</p>
                                </div>
                                <div class="flex items-start">
                                    <div
                                        class="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-indigo-100 text-indigo-800">
                                        <span class="text-xs font-bold">3</span>
                                    </div>
                                    <p class="ml-3 text-sm text-gray-600">可以设置链接的过期时间，过期后链接将不可访问</p>
                                </div>
                                <div class="flex items-start">
                                    <div
                                        class="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-indigo-100 text-indigo-800">
                                        <span class="text-xs font-bold">4</span>
                                    </div>
                                    <p class="ml-3 text-sm text-gray-600">点击生成按钮后，复制生成的短链接即可使用</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </GsapAnimation>
            </div>
        </GsapAnimation>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { GroupData } from './config'; // 引入GroupData
import PageLayout from '@/components/PageLayout.vue'; // 导入PageLayout组件
import GsapAnimation from '@/components/GsapAnimation.vue'; // 导入GsapAnimation组件
import { useShortLinkForm } from '@/composables/useShortLinkForm'; // 导入组合式函数

// 引入分组数据
const groupData = ref(GroupData);

// 获取默认的分组ID
const defaultGroupId = groupData.value.length > 0 ? groupData.value[0].id : 0;

// 使用组合式函数
const {
    linkForm,
    isSubmitting,
    shortLinkResult,
    urlError,
    errorMessage,
    presetExpiry,
    expiryDate,
    expiryTime,
    minDateTime,
    minDate,
    isFormValid,
    formattedExpiryDate,
    copied,
    validateUrl,
    setPresetExpiry,
    createShortLink,
    copyToClipboard,
    shareLink,
    resetForm,
    formatDateTime
} = useShortLinkForm(defaultGroupId);

// 一些页面特有的逻辑可以放在这里

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