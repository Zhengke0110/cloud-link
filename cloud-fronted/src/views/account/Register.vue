<template>
    <auth-container title="创建账号" subtitle="请填写以下信息完成注册" :generalError="errors.general">
        <form @submit.prevent="handleRegister" class="space-y-5" :class="{
            'space-y-3': deviceType.isMobile,
            'space-y-4': deviceType.isTablet,
            'space-y-5': deviceType.isDesktop,
        }">
            <!-- 用户名输入框 -->
            <form-input id="username" label="用户名" v-model="registerForm.username" placeholder="请输入您的用户名"
                autocomplete="name" :error="errors.username" />

            <!-- 手机号输入框 -->
            <form-input id="phone" label="手机号码" v-model="registerForm.phone" placeholder="请输入手机号码" autocomplete="tel"
                :error="errors.phone" />

            <!-- 图片验证码 -->
            <div>
                <label for="imageCaptcha" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">图片验证码</label>
                <div class="mt-1 flex space-x-2">
                    <input type="text" id="imageCaptcha" v-model="imageCaptcha"
                        class="block flex-1 appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.imageCaptcha,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请输入图片验证码" />
                    <div class="overflow-hidden rounded-md" :class="{
                        'h-8 w-24': deviceType.isMobile,
                        'h-10 w-28': !deviceType.isMobile,
                    }">
                        <img :src="captchaUrl" @click="refreshCaptcha" class="h-full w-full cursor-pointer object-cover"
                            alt="验证码" title="点击刷新验证码" />
                    </div>
                </div>
                <p v-if="errors.imageCaptcha" class="mt-1 text-red-600" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">
                    {{ errors.imageCaptcha }}
                </p>
            </div>

            <!-- 手机验证码输入框 -->
            <div>
                <label for="code" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">手机验证码</label>
                <div class="mt-1 flex space-x-2">
                    <input type="text" id="code" v-model="registerForm.code"
                        class="block flex-1 appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.code,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请输入手机验证码" />
                    <button type="button" @click="sendVerificationCode"
                        class="rounded-md border border-indigo-300 bg-white font-medium text-indigo-600 hover:bg-indigo-50 focus:outline-none"
                        :class="{
                            'px-2 py-1 text-xs': deviceType.isMobile,
                            'px-3 py-2 text-sm': !deviceType.isMobile,
                        }" :disabled="cooldown > 0 || !registerForm.phone || !imageCaptcha">
                        {{ cooldown > 0 ? `${cooldown}秒后重试` : "获取验证码" }}
                    </button>
                </div>
                <p v-if="errors.code" class="mt-1 text-red-600" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">
                    {{ errors.code }}
                </p>
            </div>

            <!-- 邮箱输入框 -->
            <form-input id="email" label="电子邮箱" v-model="registerForm.mail" placeholder="请输入电子邮箱" type="email"
                autocomplete="email" :error="errors.mail" />

            <!-- 密码输入框 -->
            <password-input id="password" label="密码" v-model="registerForm.pwd" placeholder="请设置您的密码"
                :error="errors.pwd" />

            <div class="flex items-center">
                <input id="agree-terms" type="checkbox" v-model="registerForm.agreeTerms"
                    class="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500" :class="{
                        'h-3 w-3': deviceType.isMobile,
                        'h-4 w-4': !deviceType.isMobile,
                    }" />
                <label for="agree-terms" class="ml-2 block text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">
                    我已阅读并同意
                    <a class="font-medium text-indigo-600 hover:text-indigo-500" href="#">用户协议</a>
                    和
                    <a class="font-medium text-indigo-600 hover:text-indigo-500" href="#">隐私政策</a>
                </label>
            </div>

            <div>
                <submit-button text="立即注册" loadingText="注册中..." :loading="loading"
                    :disabled="!registerForm.agreeTerms" />
            </div>
        </form>
    </auth-container>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { deviceType } from "@/utils/flexible";
import { isValidPhone, isValidEmail, isValidPassword, isValidVerificationCode } from "@/utils/formValidation";
import {
    AccountRegister,
    GetCaptcha,
    SendCodeByPhone,
} from "@/services/account";
import AuthContainer from "./components/AuthContainer.vue";
import FormInput from "./components/FormInput.vue";
import PasswordInput from "./components/PasswordInput.vue";
import SubmitButton from "./components/SubmitButton.vue";

const router = useRouter();
const loading = ref(false);
const cooldown = ref(0);

// 图片验证码相关
const captchaUrl = ref("");
const imageCaptcha = ref("");

const registerForm = reactive({
    headImg: "https://loremflickr.com/400/400?lock=4282480989208437",
    username: "",
    phone: "",
    pwd: "",
    mail: "",
    code: "",
    agreeTerms: false,
});

const errors = reactive({
    username: "",
    phone: "",
    pwd: "",
    mail: "",
    code: "",
    imageCaptcha: "",
    general: "",
});

// 刷新图片验证码
const refreshCaptcha = () => {
    captchaUrl.value = GetCaptcha() + "?t=" + new Date().getTime();
};

// 页面加载时获取验证码
onMounted(() => refreshCaptcha());

// 发送手机验证码
const sendVerificationCode = async () => {
    // 验证手机号
    if (!registerForm.phone) {
        errors.phone = "请先填写手机号";
        return;
    }
    if (!isValidPhone(registerForm.phone)) {
        errors.phone = "请输入有效的手机号码";
        return;
    }

    // 验证图片验证码
    if (!imageCaptcha.value) {
        errors.imageCaptcha = "请输入图片验证码";
        return;
    }

    try {
        // 调用发送验证码的API
        const res = await SendCodeByPhone({
            captcha: imageCaptcha.value,
            to: registerForm.phone,
        });

        console.log("验证码发送结果:", res);

        // 启动倒计时
        cooldown.value = 60;
        const timer = setInterval(() => {
            cooldown.value--;
            if (cooldown.value <= 0) {
                clearInterval(timer);
            }
        }, 1000);

        // 清除错误信息
        errors.phone = "";
        errors.imageCaptcha = "";
        errors.general = "";
    } catch (error: any) {
        console.error("发送验证码失败:", error);
        errors.general = error.message || "发送验证码失败，请稍后重试";

        // 刷新图片验证码
        refreshCaptcha();
    }
};

// 表单验证
const validateForm = () => {
    let isValid = true;

    // 重置所有错误
    Object.keys(errors).forEach((key) => {
        errors[key as keyof typeof errors] = "";
    });

    // 验证用户名
    if (!registerForm.username) {
        errors.username = "请输入用户名";
        isValid = false;
    } else if (registerForm.username.length < 2) {
        errors.username = "用户名长度不能少于2个字符";
        isValid = false;
    }

    // 验证手机号
    if (!registerForm.phone) {
        errors.phone = "请输入手机号码";
        isValid = false;
    } else if (!isValidPhone(registerForm.phone)) {
        errors.phone = "请输入有效的手机号码";
        isValid = false;
    }

    // 验证邮箱
    if (!registerForm.mail) {
        errors.mail = "请输入电子邮箱";
        isValid = false;
    } else if (!isValidEmail(registerForm.mail)) {
        errors.mail = "请输入有效的电子邮箱";
        isValid = false;
    }

    // 验证密码
    if (!registerForm.pwd) {
        errors.pwd = "请设置密码";
        isValid = false;
    } else if (!isValidPassword(registerForm.pwd)) {
        errors.pwd = "密码长度不能少于6个字符";
        isValid = false;
    }

    // 验证手机验证码
    if (!registerForm.code) {
        errors.code = "请输入手机验证码";
        isValid = false;
    } else if (!isValidVerificationCode(registerForm.code)) {
        errors.code = "请输入6位数字验证码";
        isValid = false;
    }

    // 验证图片验证码
    if (!imageCaptcha.value) {
        errors.imageCaptcha = "请输入图片验证码";
        isValid = false;
    }

    // 验证用户协议
    if (!registerForm.agreeTerms) {
        errors.general = "请阅读并同意用户协议与隐私政策";
        isValid = false;
    }

    return isValid;
};

// 处理注册
const handleRegister = async () => {
    if (!validateForm()) return;

    try {
        loading.value = true;
        const response = await AccountRegister({
            headImg: registerForm.headImg,
            username: registerForm.username,
            phone: registerForm.phone,
            mail: registerForm.mail,
            pwd: registerForm.pwd,
            code: registerForm.code,
        });

        if (response === true) {
            // TODO 注册成功，跳转到登录页
            router.push("/account/login");
        } else {
            // 显示错误信息
            errors.general = "注册失败，请检查您填写的信息";
        }
    } catch (error: any) {
        console.error("注册失败:", error);
        errors.general = error.message || "注册失败，请稍后重试";

        // 刷新验证码，可能验证码过期或错误
        refreshCaptcha();
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
