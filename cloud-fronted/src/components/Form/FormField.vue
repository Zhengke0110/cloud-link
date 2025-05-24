<template>
    <div>
        <label :for="id" class="mb-1 block text-sm font-medium text-gray-700">
            {{ label }}
            <span v-if="required" class="text-red-500">*</span>
        </label>
        <div class="relative">
            <!-- 输入框或选择框 -->
            <template v-if="type === 'text' || type === 'number' || type === 'email' || type === 'password'">
                <input :id="id" :type="type" :value="modelValue" @input="handleInput" @blur="handleBlur"
                    class="form-input w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500 sm:text-sm md:text-base"
                    :class="[{ 'touch-input': isTouchDevice }]" :placeholder="placeholder" :required="required"
                    :disabled="disabled" :aria-describedby="`${id}-help`" />
            </template>

            <!-- 选择框 -->
            <template v-else-if="type === 'select'">
                <select :id="id" :value="modelValue" @input="handleInput" @blur="handleBlur"
                    class="form-select w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500 sm:text-sm md:text-base"
                    :class="[{ 'touch-input': isTouchDevice }]" :required="required" :disabled="disabled"
                    :aria-describedby="`${id}-help`">
                    <slot></slot>
                </select>
            </template>

            <!-- 文本域 -->
            <template v-else-if="type === 'textarea'">
                <textarea :id="id" :value="modelValue" @input="handleInput" @blur="handleBlur"
                    class="form-textarea w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500 sm:text-sm md:text-base"
                    :class="[{ 'touch-input': isTouchDevice }]" :placeholder="placeholder" :required="required"
                    :disabled="disabled" :rows="rows" :aria-describedby="`${id}-help`"></textarea>
            </template>

            <!-- 右侧按钮 -->
            <div class="absolute inset-y-0 right-0 flex items-center pr-3">
                <slot name="suffix"></slot>
            </div>
        </div>

        <!-- 帮助文本区域 -->
        <div :id="`${id}-help`" class="mt-1 flex items-center text-xs text-gray-500 sm:text-xs">
            <slot name="help">
                <template v-if="helpText">{{ helpText }}</template>
            </slot>

            <!-- 辅助信息 -->
            <div class="flex-grow"></div>
            <slot name="extra"></slot>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
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
    },
    // 添加debounce属性
    debounce: {
        type: Number,
        default: 0 // 默认不启用debounce
    },
    // 添加防抖触发选项
    debounceOptions: {
        type: Object,
        default: () => ({
            leading: false, // 是否在延迟开始前调用
            trailing: true  // 是否在延迟结束后调用
        })
    },
    // 添加懒加载模式 - 只在blur时更新
    lazy: {
        type: Boolean,
        default: false
    },
    // 添加缓存属性 - 避免不必要的重新渲染
    memoize: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['update:modelValue', 'blur']);

// 缓存上一次的值，用于避免重复触发
let lastValue: string | number = props.modelValue;

// 防抖处理
let debounceTimer: ReturnType<typeof setTimeout> | null = null;

// 增强的输入处理
const handleInput = (event: Event) => {
    const target = event.target as HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement;
    const value = target.value;

    // 如果启用了懒加载模式，则不在输入时更新
    if (props.lazy) {
        return;
    }

    // 如果启用了缓存且值没变，则不触发更新
    if (props.memoize && value === lastValue) {
        return;
    }

    // 更新缓存的值
    lastValue = value;

    // 如果不需要防抖，直接发出事件
    if (props.debounce <= 0) {
        emit('update:modelValue', value);
        return;
    }

    // 如果配置了leading选项（立即触发一次）
    if (props.debounceOptions.leading && !debounceTimer) {
        emit('update:modelValue', value);
    }

    // 清除现有的计时器
    if (debounceTimer) {
        clearTimeout(debounceTimer);
        debounceTimer = null;
    }

    // 设置新的计时器
    debounceTimer = setTimeout(() => {
        // 如果配置了trailing选项（延迟结束后触发）
        if (props.debounceOptions.trailing) {
            emit('update:modelValue', value);
        }
        debounceTimer = null;
    }, props.debounce);
};

// 失去焦点处理
const handleBlur = (event: FocusEvent) => {
    const target = event.target as HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement;
    const value = target.value;

    // 对于lazy模式，在失去焦点时更新值
    if (props.lazy && value !== lastValue) {
        lastValue = value;
        emit('update:modelValue', value);
    }

    // 触发blur事件
    emit('blur', event);
};

// 确保组件销毁时清理任何未完成的计时器
onBeforeUnmount(() => {
    if (debounceTimer) {
        clearTimeout(debounceTimer);
        debounceTimer = null;
    }
});

// 使用requestIdleCallback在浏览器空闲时预加载资源
onMounted(() => {
    // 使用requestIdleCallback在浏览器空闲时执行
    const idleCallback = window.requestIdleCallback || ((cb) => setTimeout(cb, 1));

    idleCallback(() => {
        // 在这里预加载任何可能需要的资源
        // 例如加载自定义验证脚本等
    });
});

// 判断是否为触摸设备
const isTouchDevice = ref(false);

// 计算属性: 检测当前设备是否是移动设备
onMounted(() => {
    // 检测是否为触摸设备
    isTouchDevice.value =
        ('ontouchstart' in window) ||
        (navigator.maxTouchPoints > 0);

    // 如果是触摸设备，自动调整某些属性以优化体验
    if (isTouchDevice.value) {
        // 例如，可以自动增加防抖时间
    }
});
</script>

<style scoped>
/* 使用 CSS 变量来减少重绘和重排的影响 */
.form-input,
.form-select,
.form-textarea {
    will-change: transform, border-color, box-shadow;
    transform: translateZ(0);
    /* 启用GPU加速 */
    backface-visibility: hidden;
    /* 减少某些浏览器中的闪烁 */
    -webkit-font-smoothing: subpixel-antialiased;
    /* 提高字体渲染 */
}

/* 提高响应性能 */
@media (pointer: fine) {

    /* 只为具有精确指针设备（如鼠标）的设备添加悬停效果 */
    .form-input:hover,
    .form-select:hover,
    .form-textarea:hover {
        border-color: #a5b4fc;
        /* indigo-300 */
    }
}

/* 移动设备优化 */
.touch-input {
    font-size: 16px !important;
    /* 避免iOS缩放 */
    min-height: 44px;
    /* 提供足够的点击区域 */
    touch-action: manipulation;
    /* 优化触摸操作 */
}

/* 移动端响应式设计 */
@media (max-width: 640px) {

    .form-input,
    .form-select,
    .form-textarea {
        padding-top: 0.625rem;
        padding-bottom: 0.625rem;
        font-size: 16px;
        /* 避免iOS自动缩放 */
        height: auto;
        /* 确保高度自适应 */
    }

    label {
        font-size: 0.875rem;
        margin-bottom: 0.25rem;
    }
}

/* 确保输入框在小屏幕上显示正确 */
@media (max-width: 480px) {

    .form-input,
    .form-select,
    .form-textarea {
        padding-left: 0.75rem;
        padding-right: 0.75rem;
    }
}

/* 提高在黑暗环境下的可读性 */
@media (prefers-color-scheme: dark) {

    .form-input::placeholder,
    .form-textarea::placeholder {
        color: rgba(156, 163, 175, 0.8);
        /* gray-400 with opacity */
    }
}
</style>
