<template>
    <auth-container title="欢迎回来" subtitle="请输入您的账号信息进行登录" :generalError="errors.general">
        <form @submit.prevent="handleLogin" class="space-y-6" :class="{
            'space-y-4': deviceType.isMobile,
            'space-y-5': deviceType.isTablet,
            'space-y-6': deviceType.isDesktop,
        }">
            <!-- 手机号输入框 -->
            <form-input id="phone" label="手机号码" v-model="loginForm.phone" placeholder="请输入手机号码" autocomplete="tel"
                :error="errors.phone" />

            <!-- 密码输入框 -->
            <password-input id="password" label="密码" v-model="loginForm.pwd" placeholder="请输入密码" :error="errors.pwd" />

            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <input id="remember-me" type="checkbox" v-model="loginForm.remember"
                        class="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500" :class="{
                            'h-3 w-3': deviceType.isMobile,
                            'h-4 w-4': !deviceType.isMobile,
                        }" />
                    <label for="remember-me" class="ml-2 block text-gray-700" :class="{
                        'text-xs': deviceType.isMobile,
                        'text-sm': !deviceType.isMobile,
                    }">记住我</label>
                </div>

                <div :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">
                    <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">忘记密码?</a>
                </div>
            </div>

            <div>
                <submit-button text="登录" loadingText="登录中..." :loading="loading" />
            </div>
        </form>
    </auth-container>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { deviceType } from "@/utils/flexible";
import { AccountLoginApi } from "@/services/account";
import { isValidPhone, isValidPassword } from "@/utils/formValidation";
import AuthContainer from "./components/AuthContainer.vue";
import FormInput from "./components/FormInput.vue";
import PasswordInput from "./components/PasswordInput.vue";
import SubmitButton from "./components/SubmitButton.vue";

const router = useRouter();
const loading = ref(false);

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
    } else if (!isValidPhone(loginForm.phone)) {
        errors.phone = "请输入有效的手机号码";
        isValid = false;
    } else {
        errors.phone = "";
    }

    // 验证密码
    if (!loginForm.pwd) {
        errors.pwd = "请输入密码";
        isValid = false;
    } else if (!isValidPassword(loginForm.pwd)) {
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
        const response = await AccountLoginApi({
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
            // TODO 跳转到首页
            router.push("/home");
        } else {
            // 显示通用错误信息
            errors.general = "登录失败，请检查您的账号信息";
        }
    } catch (error: any) {
        // 显示具体的错误信息
        errors.general = error?.message || "登录失败，请稍后重试";
    } finally {
        loading.value = false;
    }
};
</script>
