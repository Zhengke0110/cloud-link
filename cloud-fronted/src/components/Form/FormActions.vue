<template>
    <div class="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end" :class="wrapperClass">
        <!-- 左侧额外内容插槽 -->
        <div v-if="$slots.extra" class="flex-grow text-left order-3 sm:order-1">
            <slot name="extra"></slot>
        </div>

        <!-- 取消按钮 - 进一步改进移动端体验 -->
        <button v-if="showCancel" type="button" @click="$emit('cancel')"
            class="order-2 mt-2 min-h-[44px] w-full rounded-lg border border-gray-300 bg-white px-4 py-2.5 sm:py-3 text-center text-base font-medium text-gray-700 transition-all duration-300 hover:bg-gray-50 hover:shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-opacity-50 active:bg-gray-100 sm:mt-0 sm:w-auto touch-manipulation"
            :disabled="disabled" :class="cancelButtonClass">
            <!-- 取消按钮图标插槽 -->
            <span class="inline-flex items-center">
                <slot name="cancel-icon"></slot>
                <span :class="{ 'ml-2': $slots['cancel-icon'] }">{{ cancelText }}</span>
            </span>
        </button>

        <!-- 提交按钮 - 进一步优化 -->
        <button :type="submitType" :disabled="isLoading || disabled" :aria-busy="isLoading" @click="$emit('submit')"
            class="order-1 sm:order-3 relative overflow-hidden flex min-h-[44px] w-full items-center justify-center gap-2 rounded-lg px-4 py-2.5 sm:py-3 text-base font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-opacity-50 active:translate-y-px disabled:cursor-not-allowed disabled:opacity-70 sm:w-auto touch-manipulation"
            :class="[getSubmitButtonClasses, submitButtonClass]" ref="submitButtonRef">

            <!-- 提交按钮背景活力效果 -->
            <div v-if="showRipple" class="absolute inset-0 ripple-effect" ref="rippleEffectRef"></div>

            <!-- 动态加载图标 -->
            <transition name="spin-fade" mode="out-in">
                <svg v-if="isLoading" key="loading-icon" class="animate-spin h-5 w-5 text-white" aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                </svg>
                <slot v-else name="submit-icon" key="custom-icon"></slot>
            </transition>

            <span :class="{ 'opacity-0': isLoading && hideTextWhenLoading }">
                {{ isLoading ? loadingText : submitText }}
            </span>

            <!-- 焦点效果装饰 -->
            <div v-if="showFocusRing" class="absolute -inset-0.5 rounded-lg opacity-0 focus-ring"
                :class="`bg-${colorType}-400/20`"></div>
        </button>
    </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, nextTick } from 'vue';

type ButtonType = 'submit' | 'button' | 'reset';
type ColorType = 'primary' | 'success' | 'warning' | 'danger' | 'info';

const props = defineProps({
    // 提交按钮文本
    submitText: {
        type: String,
        default: '确认'
    },
    // 取消按钮文本
    cancelText: {
        type: String,
        default: '取消'
    },
    // 加载中文本
    loadingText: {
        type: String,
        default: '处理中...'
    },
    // 是否处于加载状态
    isLoading: {
        type: Boolean,
        default: false
    },
    // 按钮是否禁用
    disabled: {
        type: Boolean,
        default: false
    },
    // 提交按钮类型
    submitType: {
        type: String as () => ButtonType,
        default: 'submit',
        validator: (value: string) => ['submit', 'button', 'reset'].includes(value)
    },
    // 组件包装器类名
    wrapperClass: {
        type: String,
        default: ''
    },
    // 取消按钮类名
    cancelButtonClass: {
        type: String,
        default: ''
    },
    // 提交按钮类名
    submitButtonClass: {
        type: String,
        default: ''
    },
    // 按钮颜色类型
    colorType: {
        type: String as () => ColorType,
        default: 'primary',
        validator: (value: string) => ['primary', 'success', 'warning', 'danger', 'info'].includes(value)
    },
    // 是否显示取消按钮
    showCancel: {
        type: Boolean,
        default: true
    },
    // 是否显示波纹效果
    showRipple: {
        type: Boolean,
        default: true
    },
    // 是否显示焦点环效果
    showFocusRing: {
        type: Boolean,
        default: true
    },
    // 加载时是否隐藏文字(只显示spinner)
    hideTextWhenLoading: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['cancel', 'submit']);

// 根据颜色类型计算提交按钮的类名
const getSubmitButtonClasses = computed(() => {
    const colorMap: Record<ColorType, string> = {
        primary: 'bg-gradient-to-r from-indigo-600 to-blue-600 hover:from-indigo-700 hover:to-blue-700 focus:ring-indigo-500',
        success: 'bg-gradient-to-r from-emerald-600 to-green-600 hover:from-emerald-700 hover:to-green-700 focus:ring-emerald-500',
        warning: 'bg-gradient-to-r from-amber-500 to-orange-500 hover:from-amber-600 hover:to-orange-600 focus:ring-amber-500',
        danger: 'bg-gradient-to-r from-red-600 to-rose-600 hover:from-red-700 hover:to-rose-700 focus:ring-red-500',
        info: 'bg-gradient-to-r from-sky-500 to-cyan-500 hover:from-sky-600 hover:to-cyan-600 focus:ring-sky-500'
    };

    return colorMap[props.colorType] || colorMap.primary;
});

// 按钮引用
const submitButtonRef = ref<HTMLButtonElement | null>(null);
const rippleEffectRef = ref<HTMLDivElement | null>(null);

// 波纹效果函数
const createRippleEffect = (event: MouseEvent) => {
    if (!props.showRipple || !submitButtonRef.value || !rippleEffectRef.value) return;

    const button = submitButtonRef.value;
    const rippleEffect = rippleEffectRef.value;

    // 计算波纹位置
    const rect = button.getBoundingClientRect();
    const size = Math.max(rect.width, rect.height);
    const x = event.clientX - rect.left - size / 2;
    const y = event.clientY - rect.top - size / 2;

    // 清除旧的波纹效果
    rippleEffect.style.cssText = '';

    // 创建新的波纹效果
    rippleEffect.style.width = rippleEffect.style.height = `${size}px`;
    rippleEffect.style.left = `${x}px`;
    rippleEffect.style.top = `${y}px`;
    rippleEffect.classList.remove('rippling');

    // 触发重绘
    void rippleEffect.offsetWidth;

    // 添加动画类
    rippleEffect.classList.add('rippling');
};

// 挂载后处理
onMounted(() => {
    // 添加点击事件监听器
    if (props.showRipple && submitButtonRef.value) {
        submitButtonRef.value.addEventListener('mousedown', createRippleEffect);
    }

    // 应用轻微的进入动画
    nextTick(() => {
        if (submitButtonRef.value && !window.matchMedia?.('(prefers-reduced-motion: reduce)').matches) {
            submitButtonRef.value.style.transform = 'translateY(0)';
        }
    });
});
</script>

<style scoped>
/* 波纹效果 */
.ripple-effect {
    position: absolute;
    border-radius: 50%;
    transform: scale(0);
    opacity: 0;
    pointer-events: none;
    background-color: rgba(255, 255, 255, 0.3);
}

.ripple-effect.rippling {
    transform: scale(1);
    opacity: 0;
    transition: transform 0.5s ease-out, opacity 0.5s ease-out;
}

/* 过渡动画 */
.spin-fade-enter-active,
.spin-fade-leave-active {
    transition: all 0.2s ease;
}

.spin-fade-enter-from,
.spin-fade-leave-to {
    opacity: 0;
    transform: scale(0.8);
}

/* 焦点效果 */
button:focus .focus-ring {
    opacity: 1;
}

/* 移动端优化 */
@media (max-width: 640px) {
    button {
        padding-top: 0.625rem;
        padding-bottom: 0.625rem;
    }
}

/* 无障碍性优化 */
@media (prefers-reduced-motion: reduce) {

    .ripple-effect.rippling,
    .spin-fade-enter-active,
    .spin-fade-leave-active,
    button {
        transition: none !important;
        animation: none !important;
    }
}
</style>
