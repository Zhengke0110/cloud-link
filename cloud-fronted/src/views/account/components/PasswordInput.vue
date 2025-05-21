<template>
    <div>
        <label :for="id" class="block font-medium text-gray-700"
            :class="{ 'text-xs': deviceType.isMobile, 'text-sm': !deviceType.isMobile }">
            {{ label }}
        </label>
        <div class="relative mt-1">
            <input :type="showPassword ? 'text' : 'password'" :id="id" :value="modelValue"
                @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                :class="{
                    'border-red-500': error,
                    'py-1.5 text-xs': deviceType.isMobile,
                    'py-2 text-sm': !deviceType.isMobile,
                }" :placeholder="placeholder" />
            <button type="button" @click="showPassword = !showPassword"
                class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-400 hover:text-gray-500">
                <i :class="[
                    showPassword
                        ? 'icon-[material-symbols--visibility-off-outline]'
                        : 'icon-[material-symbols--visibility-outline]',
                    deviceType.isMobile ? 'size-4' : 'size-5',
                ]"></i>
            </button>
            <p v-if="error" class="mt-1 text-red-600"
                :class="{ 'text-xs': deviceType.isMobile, 'text-sm': !deviceType.isMobile }">
                {{ error }}
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { deviceType } from "@/utils/flexible";

// 定义组件属性
defineProps({
    id: {
        type: String,
        required: true
    },
    label: {
        type: String,
        required: true
    },
    modelValue: {
        type: String,
        default: ""
    },
    placeholder: {
        type: String,
        default: "请输入密码"
    },
    error: {
        type: String,
        default: ""
    }
});

// 定义事件
defineEmits(['update:modelValue']);

// 密码可见状态
const showPassword = ref(false);
</script>
