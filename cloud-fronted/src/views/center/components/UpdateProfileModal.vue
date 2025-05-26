<template>
    <div class="fixed inset-0 z-50 overflow-auto bg-gray-900 bg-opacity-50 backdrop-blur-sm flex items-center justify-center"
        v-if="modelValue" @click.self="closeModal">
        <div class="relative bg-white rounded-xl shadow-2xl w-full max-w-md mx-4 md:mx-auto transition-all duration-300 transform"
            :class="[modelValue ? 'opacity-100 scale-100' : 'opacity-0 scale-95']">
            <!-- 头部 -->
            <div class="relative p-5 border-b border-gray-100">
                <h3 class="text-lg font-semibold text-gray-800">更新个人信息</h3>
                <button type="button" @click="closeModal"
                    class="absolute top-4 right-4 text-gray-400 hover:text-gray-600 transition-colors duration-200">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>

            <!-- 表单内容 -->
            <form @submit.prevent="submitForm" class="p-5">
                <!-- 头像上传 -->
                <div class="mb-5">
                    <label class="block text-sm font-medium text-gray-700 mb-1">头像</label>
                    <div class="flex items-center">
                        <div class="relative mr-4">
                            <div class="h-20 w-20 rounded-full overflow-hidden bg-gray-100 border border-gray-200">
                                <img :src="displayAvatar" alt="用户头像" class="h-full w-full object-cover" />
                            </div>
                            <div v-if="!isUploading"
                                class="absolute inset-0 bg-black bg-opacity-40 flex items-center justify-center rounded-full opacity-0 hover:opacity-100 transition-opacity duration-300 cursor-pointer">
                                <span class="text-white text-xs">更换头像</span>
                            </div>
                            <div v-else
                                class="absolute inset-0 bg-black bg-opacity-60 flex items-center justify-center rounded-full">
                                <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg"
                                    fill="none" viewBox="0 0 24 24">
                                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                        stroke-width="4"></circle>
                                    <path class="opacity-75" fill="currentColor"
                                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                                    </path>
                                </svg>
                            </div>
                            <input type="file" accept="image/*" class="absolute inset-0 opacity-0 cursor-pointer"
                                @change="handleImageUpload" :disabled="isUploading" />
                        </div>
                        <div class="text-sm text-gray-500">
                            <p>点击头像更换</p>
                            <p>支持 JPG, PNG 格式</p>
                            <p>建议尺寸 200x200 像素</p>
                        </div>
                    </div>
                    <p v-if="errors.headImg" class="mt-1 text-xs text-red-500">{{ errors.headImg }}</p>
                </div>

                <!-- 用户名 -->
                <div class="mb-5">
                    <label for="username" class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                    <div class="relative">
                        <input type="text" id="username" v-model="formData.username"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                            placeholder="请输入用户名" />
                        <div v-if="formData.username" @click="formData.username = ''"
                            class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </div>
                    </div>
                    <p v-if="errors.username" class="mt-1 text-xs text-red-500">{{ errors.username }}</p>
                </div>

                <!-- 邮箱 -->
                <div class="mb-5">
                    <label for="mail" class="block text-sm font-medium text-gray-700 mb-1">电子邮箱</label>
                    <div class="relative">
                        <input type="email" id="mail" v-model="formData.mail"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                            placeholder="请输入电子邮箱" />
                        <div v-if="formData.mail" @click="formData.mail = ''"
                            class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </div>
                    </div>
                    <p v-if="errors.mail" class="mt-1 text-xs text-red-500">{{ errors.mail }}</p>
                </div>

                <!-- 按钮区域 -->
                <div class="flex justify-end space-x-3 mt-7">
                    <button type="button" @click="closeModal"
                        class="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        取消
                    </button>
                    <button type="submit" :disabled="isSubmitting"
                        class="px-4 py-2 bg-gradient-to-r from-indigo-600 to-blue-600 text-white rounded-lg hover:shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200 disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center min-w-[80px]">
                        <svg v-if="isSubmitting" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                            xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                            </circle>
                            <path class="opacity-75" fill="currentColor"
                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                            </path>
                        </svg>
                        {{ isSubmitting ? '提交中...' : '保存' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue';

// 定义 props 和 emits
const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    userData: {
        type: Object,
        required: true
    }
});

const emit = defineEmits(['update:modelValue', 'submit', 'upload-image']);

// 表单数据
const formData = reactive({
    username: '',
    mail: '',
    headImg: ''
});

// 表单验证错误
const errors = reactive({
    username: '',
    mail: '',
    headImg: ''
});

// 提交和上传状态
const isSubmitting = ref(false);
const isUploading = ref(false);

// 计算显示的头像
const displayAvatar = computed(() => {
    return formData.headImg || props.userData.headImg;
});

// 当模态窗口打开时，初始化表单数据
watch(() => props.modelValue, (val) => {
    if (val) {
        formData.username = props.userData.username || '';
        formData.mail = props.userData.mail || '';
        formData.headImg = '';

        // 清除错误信息
        clearErrors();
    }
});

// 关闭模态窗口
const closeModal = () => {
    emit('update:modelValue', false);
};

// 处理图片上传
const handleImageUpload = async (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
        const file = input.files[0];

        try {
            isUploading.value = true;
            errors.headImg = '';

            // 通过事件让父组件处理上传，不期望返回值
            emit('upload-image', file);

            // 注意：这里需要父组件通过其他方式（如props）来更新头像URL
            // 或者通过一个回调函数来设置URL

        } catch (error) {
            errors.headImg = error instanceof Error ? error.message : '图片上传失败';
        } finally {
            isUploading.value = false;
            // 清空input值，允许重新选择同一个文件
            input.value = '';
        }
    }
};

// 清除所有错误信息
const clearErrors = () => {
    errors.username = '';
    errors.mail = '';
    errors.headImg = '';
};

// 验证表单
const validateForm = () => {
    let isValid = true;

    // 验证用户名
    if (!formData.username.trim()) {
        errors.username = '请输入用户名';
        isValid = false;
    } else if (formData.username.length > 20) {
        errors.username = '用户名不能超过20个字符';
        isValid = false;
    } else {
        errors.username = '';
    }

    // 验证邮箱
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.mail.trim()) {
        errors.mail = '请输入电子邮箱';
        isValid = false;
    } else if (!emailRegex.test(formData.mail)) {
        errors.mail = '请输入有效的电子邮箱地址';
        isValid = false;
    } else {
        errors.mail = '';
    }

    return isValid;
};

// 提交表单
const submitForm = async () => {
    if (!validateForm() || isUploading.value) return;

    try {
        isSubmitting.value = true;

        // 创建更新数据对象
        const updateData = {
            username: formData.username,
            mail: formData.mail,
            headImg: formData.headImg || props.userData.headImg
        };

        // 发送更新事件
        emit('submit', updateData);

        // 关闭模态窗口
        setTimeout(() => {
            isSubmitting.value = false;
            closeModal();
        }, 500);
    } catch (error) {
        console.error('更新个人信息失败:', error);
        isSubmitting.value = false;
    }
};
</script>

<style scoped>
/* 添加动画效果 */
.scale-100 {
    transform: scale(1);
}

.scale-95 {
    transform: scale(0.95);
}

/* 防止滚动条跳动 */
.fixed {
    width: 100vw;
    height: 100vh;
}

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
