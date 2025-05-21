<template>
    <div>
        <label :for="id" class="mb-1 block text-sm font-medium text-gray-700">{{ label }}</label>
        <div class="relative">
            <!-- 输入框或选择框 -->
            <template v-if="type === 'text' || type === 'number' || type === 'email' || type === 'password'">
                <input :id="id" :type="type" :value="modelValue"
                    @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                    class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                    :placeholder="placeholder" :required="required" :disabled="disabled" />
            </template>

            <!-- 选择框 -->
            <template v-else-if="type === 'select'">
                <select :id="id" :value="modelValue"
                    @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                    class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                    :required="required" :disabled="disabled">
                    <slot></slot>
                </select>
            </template>

            <!-- 文本域 -->
            <template v-else-if="type === 'textarea'">
                <textarea :id="id" :value="modelValue"
                    @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                    class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                    :placeholder="placeholder" :required="required" :disabled="disabled" :rows="rows"></textarea>
            </template>

            <!-- 右侧按钮 -->
            <slot name="suffix"></slot>
        </div>

        <!-- 帮助文本区域 -->
        <div class="mt-1 flex items-center text-xs text-gray-500">
            <slot name="help">
                <template v-if="helpText">{{ helpText }}</template>
            </slot>

            <!-- 辅助信息 -->
            <slot name="extra"></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
defineProps({
    // 输入框ID，用于label的for属性
    id: {
        type: String,
        required: true
    },
    // 标签文本
    label: {
        type: String,
        required: true
    },
    // 输入框类型
    type: {
        type: String,
        default: 'text',
        validator: (value: string) => {
            return ['text', 'number', 'email', 'password', 'select', 'textarea'].includes(value);
        }
    },
    // v-model绑定值
    modelValue: {
        type: [String, Number],
        default: ''
    },
    // 占位符文本
    placeholder: {
        type: String,
        default: ''
    },
    // 帮助文本
    helpText: {
        type: String,
        default: ''
    },
    // 是否必填
    required: {
        type: Boolean,
        default: false
    },
    // 是否禁用
    disabled: {
        type: Boolean,
        default: false
    },
    // 文本域行数
    rows: {
        type: Number,
        default: 3
    }
});

defineEmits(['update:modelValue']);
</script>
