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
                                <!-- 原始链接输入 - 使用FormField替换 -->
                                <FormField id="originalUrl" label="完整链接地址" v-model="linkForm.originalUrl"
                                    placeholder="请输入您想要缩短的链接，如 https://example.com/page" :error="urlError"
                                    helpText="输入以http://或https://开头的有效网址" required :clearable="true"
                                    @input="validateUrl" />

                                <!-- 标题输入 - 使用FormField替换 -->
                                <FormField id="title" label="链接标题 (选填)" v-model="linkForm.title"
                                    placeholder="为您的链接添加一个易于识别的标题" maxlength="50" helpText="最多50个字符"
                                    :clearable="true" />

                                <!-- 分组选择 - 使用FormField替换 -->
                                <FormField id="groupId" label="选择分组" type="select" v-model="linkForm.groupId"
                                    helpText="选择要归属的分组">
                                    <option v-for="group in groupData" :key="group.id" :value="group.id">
                                        {{ group.title }}
                                    </option>
                                </FormField>

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
                                        <!-- 使用更简单的原生日期时间选择器布局 -->
                                        <div class="flex flex-col sm:flex-row gap-2">
                                            <!-- 日期选择器 -->
                                            <div class="flex-grow">
                                                <label for="expiryDate"
                                                    class="block text-sm font-medium text-gray-700 mb-1">选择日期</label>
                                                <input type="date" id="expiryDate" v-model="expiryDate" :min="minDate"
                                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 appearance-none pr-10 min-h-[48px] text-base" />
                                            </div>

                                            <!-- 时间选择器 -->
                                            <div class="flex-grow">
                                                <label for="expiryTime"
                                                    class="block text-sm font-medium text-gray-700 mb-1">选择时间</label>
                                                <input type="time" id="expiryTime" v-model="expiryTime"
                                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 appearance-none pr-10 min-h-[48px] text-base" />
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

                                <!-- 提交按钮 - 使用FormActions替换 -->
                                <FormActions submitText="生成短链接" loadingText="生成中..." :isLoading="isSubmitting"
                                    :disabled="!isFormValid" submitOnly />
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
                                    <!-- 短链接 - 使用InfoField替换 -->
                                    <InfoField label="短链接" valueClass="text-indigo-600" borderClass="border-indigo-100"
                                        bgClass="bg-indigo-50">
                                        <div class="flex items-center justify-between w-full">
                                            <span>{{ shortLinkResult.shortUrl }}</span>
                                            <button @click="copyToClipboard(shortLinkResult.shortUrl)"
                                                class="bg-indigo-600 hover:bg-indigo-700 text-white py-1 px-3 rounded-lg transition-colors duration-300 text-sm touch-manipulation min-h-[36px]">
                                                {{ copied ? '已复制' : '复制' }}
                                            </button>
                                        </div>
                                    </InfoField>

                                    <!-- 原始链接 - 使用InfoField替换 -->
                                    <InfoField label="原始链接" :value="shortLinkResult.originalUrl" breakAll />

                                    <!-- 链接信息 -->
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-3 md:gap-4">
                                        <!-- 创建时间 - 使用InfoField替换 -->
                                        <InfoField label="创建时间" :value="formatDateTime(shortLinkResult.createTime)"
                                            icon="clock" />

                                        <!-- 过期时间 - 使用InfoField替换 -->
                                        <InfoField label="过期时间"
                                            :value="shortLinkResult.expired ? formatDateTime(shortLinkResult.expired) : '永不过期'"
                                            icon="calendar" />
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

                                <!-- 操作按钮 - 使用FormActions替换 -->
                                <div class="mt-5 md:mt-6">
                                    <FormActions submitText="分享链接" cancelText="创建新链接" cancelVariant="secondary"
                                        submitVariant="primary" submitIcon="share" @submit="shareLink"
                                        @cancel="resetForm" reverseOrder />
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
import { ref } from 'vue';
import { GroupData } from './config'; // 引入GroupData
import PageLayout from '@/components/PageLayout.vue'; // 导入PageLayout组件
import GsapAnimation from '@/components/GsapAnimation.vue'; // 导入GsapAnimation组件
import FormField from '@/components/Form/FormField.vue'; // 导入FormField组件
import InfoField from '@/components/Form/InfoField.vue'; // 导入InfoField组件
import FormActions from '@/components/Form/FormActions.vue'; // 导入FormActions组件
import { useShortLinkForm } from '@/composables/useShortLinkForm'; // 导入组合式函数

// 引入分组数据
const groupData = ref(GroupData);

// 获取默认的分组ID
const defaultGroupId = groupData.value.length > 0 ? groupData.value[0].id : 0;

// TODO: 在这里添加API调用以获取分组数据，替代静态导入的GroupData
// 例如: 
// const fetchGroups = async () => {
//   try {
//     const response = await api.getGroups();
//     groupData.value = response.data;
//   } catch (error) {
//     console.error('获取分组数据失败:', error);
//   }
// };
// onMounted(fetchGroups);

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

// TODO: 在这里添加必要的生命周期钩子以获取其他初始数据
// 例如加载域名列表、用户配置等
// onMounted(async () => {
//   await Promise.all([
//     fetchDomains(), 
//     fetchUserPreferences()
//   ]);
// });
</script>

<style scoped>
/* 修复日期选择器在某些浏览器的显示问题 */
input[type="date"]::-webkit-calendar-picker-indicator,
input[type="time"]::-webkit-calendar-picker-indicator {
    opacity: 1;
    cursor: pointer;
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
}

input[type="date"],
input[type="time"] {
    position: relative;
}
</style>