<template>
    <div class="rounded-lg border border-gray-100 bg-white shadow-sm transition-all duration-200 hover:shadow-sm hover:border-gray-200"
        :class="customClass">
        <div class="p-4 md:p-5" @click="toggleExpanded" :class="{ 'cursor-pointer': collapsible }">
            <div class="flex items-center justify-between">
                <h4 class="text-base md:text-lg font-medium text-gray-800 md:mb-0.5" :class="titleClass">
                    {{ question }}
                </h4>
                <button v-if="collapsible" type="button"
                    class="ml-2 flex-shrink-0 rounded-full p-1 text-gray-400 hover:bg-gray-50 hover:text-gray-600"
                    @click.stop="toggleExpanded" aria-label="展开或收起">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 transition-transform duration-200"
                        :class="isExpanded ? 'rotate-180' : ''" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                            clip-rule="evenodd" />
                    </svg>
                </button>
            </div>

            <div v-if="collapsible ? isExpanded : true" ref="contentRef" class="overflow-hidden">
                <GsapAnimation :animation="'fadeInDown'" :duration="0.4" :disabled="!isAnimationEnabled"
                    :key="animationKey">
                    <div class="mt-2">
                        <p class="text-sm md:text-base text-gray-600 leading-relaxed">{{ answer }}</p>
                        <slot></slot>
                    </div>
                </GsapAnimation>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';

const props = defineProps({
    // FAQ问题
    question: {
        type: String,
        required: true,
    },
    // FAQ答案
    answer: {
        type: String,
        required: true,
    },
    // 是否默认展开
    defaultOpen: {
        type: Boolean,
        default: true,
    },
    // 是否可折叠
    collapsible: {
        type: Boolean,
        default: false,
    },
    // 自定义类名
    customClass: {
        type: String,
        default: '',
    },
    // 标题类名
    titleClass: {
        type: String,
        default: '',
    },
});

// 展开/折叠状态
const isExpanded = ref(props.defaultOpen);
const contentRef = ref<HTMLElement | null>(null);
const animationKey = ref(0);

// 控制是否启用动画（首次渲染不需要动画）
const isAnimationEnabled = ref(false);

// 切换展开/折叠状态
const toggleExpanded = () => {
    if (props.collapsible) {
        isExpanded.value = !isExpanded.value;
        // 触发GSAP重新动画
        animationKey.value++;
        // 确保展开/折叠时启用动画
        isAnimationEnabled.value = true;
    }
};

// 监听defaultOpen属性变化
watch(() => props.defaultOpen, (newValue) => {
    isExpanded.value = newValue;
}, { immediate: false });

// 组件挂载后启用动画，但首次渲染不需要动画
setTimeout(() => {
    isAnimationEnabled.value = true;
}, 100);
</script>
