<template>
    <div>
        <label :for="id" class="block font-medium text-gray-700"
            :class="{ 'text-xs': deviceType.isMobile, 'text-sm': !deviceType.isMobile }">
            {{ label }}
        </label>
        <div class="mt-1">
            <input :type="type" :id="id" :value="modelValue"
                @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                :autocomplete="autocomplete" :placeholder="placeholder"
                class="block w-full appearance-none rounded-md border border-gray-300 px-3 placeholder-gray-400 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 focus:outline-none"
                :class="{
                    'border-red-500': error,
                    'py-1.5 text-xs': deviceType.isMobile,
                    'py-2 text-sm': !deviceType.isMobile,
                }" />
            <p v-if="error" class="mt-1 text-red-600"
                :class="{ 'text-xs': deviceType.isMobile, 'text-sm': !deviceType.isMobile }">
                {{ error }}
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
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
    type: {
        type: String,
        default: "text"
    },
    placeholder: {
        type: String,
        default: ""
    },
    error: {
        type: String,
        default: ""
    },
    autocomplete: {
        type: String,
        default: ""
    }
});

// 定义事件
defineEmits(['update:modelValue']);
</script>
