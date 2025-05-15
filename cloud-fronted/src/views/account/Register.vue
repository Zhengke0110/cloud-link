<template>
    <div class="animate-slideUp mx-auto w-full rounded-2xl bg-white shadow-lg" :class="{
        'max-w-xs p-5': deviceType.isMobile,
        'max-w-md p-6': deviceType.isTablet,
        'max-w-lg p-8': deviceType.isDesktop,
    }">
        <div class="mb-6 text-center">
            <h1 class="font-bold tracking-tight text-indigo-600" :class="{
                'text-xl': deviceType.isMobile,
                'text-2xl': deviceType.isTablet,
                'text-3xl': deviceType.isDesktop,
            }">
                创建账号
            </h1>
            <p class="mt-2 text-gray-600" :class="{
                'text-xs': deviceType.isMobile,
                'text-sm': deviceType.isTablet,
                'text-base': deviceType.isDesktop,
            }">
                请填写以下信息完成注册
            </p>
        </div>

        <!-- 通用错误信息 -->
        <div v-if="errors.general" class="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
            <p class="text-sm text-red-600" :class="{ 'text-xs': deviceType.isMobile }">
                {{ errors.general }}
            </p>
        </div>

        <form @submit.prevent="handleRegister" class="space-y-5" :class="{
            'space-y-3': deviceType.isMobile,
            'space-y-4': deviceType.isTablet,
            'space-y-5': deviceType.isDesktop,
        }">
            <!-- 用户名输入框 -->
            <div>
                <label for="username" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">用户名</label>
                <div class="mt-1">
                    <input type="text" id="username" v-model="registerForm.username" autocomplete="name"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.username,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请输入您的用户名" />
                    <p v-if="errors.username" class="mt-1 text-red-600" :class="{
                        'text-xs': deviceType.isMobile,
                        'text-sm': !deviceType.isMobile,
                    }">
                        {{ errors.username }}
                    </p>
                </div>
            </div>

            <!-- 手机号输入框 -->
            <div>
                <label for="phone" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">手机号码</label>
                <div class="mt-1">
                    <input type="text" id="phone" v-model="registerForm.phone" autocomplete="tel"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.phone,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请输入手机号码" />
                    <p v-if="errors.phone" class="mt-1 text-red-600" :class="{
                        'text-xs': deviceType.isMobile,
                        'text-sm': !deviceType.isMobile,
                    }">
                        {{ errors.phone }}
                    </p>
                </div>
            </div>

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
            <div>
                <label for="email" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">电子邮箱</label>
                <div class="mt-1">
                    <input type="email" id="email" v-model="registerForm.mail" autocomplete="email"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.mail,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请输入电子邮箱" />
                    <p v-if="errors.mail" class="mt-1 text-red-600" :class="{
                        'text-xs': deviceType.isMobile,
                        'text-sm': !deviceType.isMobile,
                    }">
                        {{ errors.mail }}
                    </p>
                </div>
            </div>

            <!-- 密码输入框 -->
            <div>
                <label for="password" class="block font-medium text-gray-700" :class="{
                    'text-xs': deviceType.isMobile,
                    'text-sm': !deviceType.isMobile,
                }">密码</label>
                <div class="relative mt-1">
                    <input :type="showPassword ? 'text' : 'password'" id="password" v-model="registerForm.pwd"
                        class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                        :class="{
                            'border-red-500': errors.pwd,
                            'py-1.5 text-xs': deviceType.isMobile,
                            'py-2 text-sm': !deviceType.isMobile,
                        }" placeholder="请设置您的密码" />
                    <button type="button" @click="showPassword = !showPassword"
                        class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-400 hover:text-gray-500">
                        <i :class="[
                            showPassword
                                ? 'icon-[material-symbols--visibility-off-outline]'
                                : 'icon-[material-symbols--visibility-outline]',
                            deviceType.isMobile ? 'size-4' : 'size-5',
                        ]"></i>
                    </button>
                    <p v-if="errors.pwd" class="mt-1 text-red-600" :class="{
                        'text-xs': deviceType.isMobile,
                        'text-sm': !deviceType.isMobile,
                    }">
                        {{ errors.pwd }}
                    </p>
                </div>
            </div>

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
                <button type="submit"
                    class="flex w-full justify-center rounded-md border border-transparent px-4 font-medium text-white shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    :class="{
                        'py-1.5 text-xs': deviceType.isMobile,
                        'py-2 text-sm': !deviceType.isMobile,
                        'bg-indigo-600 hover:bg-indigo-700': registerForm.agreeTerms,
                        'bg-indigo-400 cursor-not-allowed': !registerForm.agreeTerms,
                    }" :disabled="loading || !registerForm.agreeTerms">
                    <span v-if="loading" class="inline-flex items-center">
                        <i class="icon-[eos-icons--loading] mr-2 animate-spin" :class="{
                            'size-3': deviceType.isMobile,
                            'size-4': !deviceType.isMobile,
                        }"></i>
                        注册中...
                    </span>
                    <span v-else>立即注册</span>
                </button>
            </div>
        </form>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { deviceType } from "@/utils/flexible";
import {
    AccountRegister,
    GetCaptcha,
    SendCodeByPhone,
} from "@/services/account";

const router = useRouter();
const loading = ref(false);
const showPassword = ref(false);
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
    if (!/^1[3-9]\d{9}$/.test(registerForm.phone)) {
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
    } else if (!/^1[3-9]\d{9}$/.test(registerForm.phone)) {
        errors.phone = "请输入有效的手机号码";
        isValid = false;
    }

    // 验证邮箱
    if (!registerForm.mail) {
        errors.mail = "请输入电子邮箱";
        isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(registerForm.mail)) {
        errors.mail = "请输入有效的电子邮箱";
        isValid = false;
    }

    // 验证密码
    if (!registerForm.pwd) {
        errors.pwd = "请设置密码";
        isValid = false;
    } else if (registerForm.pwd.length < 6) {
        errors.pwd = "密码长度不能少于6个字符";
        isValid = false;
    }

    // 验证手机验证码
    if (!registerForm.code) {
        errors.code = "请输入手机验证码";
        isValid = false;
    } else if (
        registerForm.code.length !== 6 ||
        !/^\d+$/.test(registerForm.code)
    ) {
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
