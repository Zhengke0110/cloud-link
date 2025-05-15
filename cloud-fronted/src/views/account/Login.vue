<template>
    <div class="animate-slideUp mx-auto w-full max-w-md">
        <div class="mb-8 text-center">
            <h1 class="text-3xl font-bold tracking-tight text-indigo-600" :class="{ 'text-2xl': deviceType.isMobile }">
                欢迎回来
            </h1>
            <p class="mt-2 text-gray-600" :class="{ 'text-sm': deviceType.isMobile }">
                请输入您的账号信息进行登录
            </p>
        </div>

        <!-- 通用错误信息 -->
        <div v-if="errors.general" class="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
            <p class="text-sm text-red-600">{{ errors.general }}</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-6">
            <!-- 手机号输入框 -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700">手机号码</label>
                <div class="mt-1">
                    <input type="text" id="phone" v-model="loginForm.phone" autocomplete="tel"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 py-2 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none sm:text-sm"
                        :class="{ 'border-red-500': errors.phone }" placeholder="请输入手机号码" />
                    <p v-if="errors.phone" class="mt-1 text-sm text-red-600">
                        {{ errors.phone }}
                    </p>
                </div>
            </div>

            <!-- 密码输入框 -->
            <div>
                <label for="password" class="block text-sm font-medium text-gray-700">密码</label>
                <div class="relative mt-1">
                    <input :type="showPassword ? 'text' : 'password'" id="password" v-model="loginForm.pwd"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 py-2 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none sm:text-sm"
                        :class="{ 'border-red-500': errors.pwd }" placeholder="请输入密码" />
                    <button type="button" @click="showPassword = !showPassword"
                        class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-400 hover:text-gray-500">
                        <i :class="[
                            showPassword
                                ? 'icon-[material-symbols--visibility-off-outline]'
                                : 'icon-[material-symbols--visibility-outline]',
                        ]" class="size-5"></i>
                    </button>
                    <p v-if="errors.pwd" class="mt-1 text-sm text-red-600">
                        {{ errors.pwd }}
                    </p>
                </div>
            </div>

            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <input id="remember-me" type="checkbox" v-model="loginForm.remember"
                        class="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500" />
                    <label for="remember-me" class="ml-2 block text-sm text-gray-700">记住我</label>
                </div>

                <div class="text-sm">
                    <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">忘记密码?</a>
                </div>
            </div>

            <div>
                <button type="submit"
                    class="flex w-full justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
                    :disabled="loading">
                    <span v-if="loading" class="inline-flex items-center">
                        <i class="icon-[eos-icons--loading] mr-2 animate-spin"></i>
                        登录中...
                    </span>
                    <span v-else>登录</span>
                </button>
            </div>
        </form>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { deviceType } from "@/utils/flexible";
import { AccountLogin } from "@/services/account";

const router = useRouter();
const loading = ref(false);
const showPassword = ref(false);

const loginForm = reactive({
    phone: "",
    pwd: "",
    remember: false,
});

const errors = reactive({
    phone: "",
    pwd: "",
    general: "",
});

// 页面加载时检查是否有保存的登录信息
onMounted(() => {
    const savedUser = localStorage.getItem("rememberedUser");
    if (savedUser) {
        try {
            const userInfo = JSON.parse(savedUser);
            loginForm.phone = userInfo.phone || "";
            loginForm.pwd = userInfo.pwd || "";
            loginForm.remember = true;
        } catch (e) {
            console.error("解析保存的用户信息失败", e);
            // 如果解析失败，清除可能损坏的数据
            localStorage.removeItem("rememberedUser");
        }
    }
});

// 表单验证
const validateForm = () => {
    let isValid = true;

    // 重置通用错误
    errors.general = "";

    // 验证手机号
    if (!loginForm.phone) {
        errors.phone = "请输入手机号码";
        isValid = false;
    } else if (!/^1[3-9]\d{9}$/.test(loginForm.phone)) {
        errors.phone = "请输入有效的手机号码";
        isValid = false;
    } else {
        errors.phone = "";
    }

    // 验证密码
    if (!loginForm.pwd) {
        errors.pwd = "请输入密码";
        isValid = false;
    } else if (loginForm.pwd.length < 6) {
        errors.pwd = "密码长度不能少于6个字符";
        isValid = false;
    } else {
        errors.pwd = "";
    }

    return isValid;
};

// 处理登录
const handleLogin = async () => {
    if (!validateForm()) return;

    // 清除所有错误
    errors.phone = "";
    errors.pwd = "";
    errors.general = "";

    try {
        loading.value = true;
        const response = await AccountLogin({
            phone: loginForm.phone,
            pwd: loginForm.pwd,
        });
        if (response === true) {
            // 如果用户勾选了"记住我"，保存登录信息
            if (loginForm.remember) {
                localStorage.setItem(
                    "rememberedUser",
                    JSON.stringify({
                        phone: loginForm.phone,
                        pwd: loginForm.pwd,
                    }),
                );
            } else {
                localStorage.removeItem("rememberedUser"); // 如果未勾选，清除之前可能保存的信息
            }

            router.push("/home");
        } else {
            // 显示通用错误信息
            errors.general = "登录失败，请检查您的账号信息";
        }
    } catch (error) {
        errors.general = "登录失败，请稍后重试";
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-slideUp {
    animation: slideUp 0.8s ease-out forwards;
}
</style>
