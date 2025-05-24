<template>
  <Teleport to="body">
    <Transition enter-active-class="duration-150 ease-out" enter-from-class="opacity-0" enter-to-class="opacity-100"
      leave-active-class="duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
      <div v-if="modelValue" class="fixed inset-0 z-50 overflow-y-auto" :id="id" ref="modalContainerRef">
        <!-- 背景遮罩 -->
        <div class="fixed inset-0 bg-black/40 transition-opacity" aria-hidden="true" ref="backdropRef">
          <!-- 模糊效果背景使用GsapAnimation -->
          <GsapAnimation animation="fadeIn" :from="{ backdropFilter: 'blur(0px)', opacity: 0 }"
            :to="{ backdropFilter: 'blur(4px)', opacity: 1 }" :duration="0.2" ease="power1.out"
            class="absolute inset-0 backdrop-blur-effect" />
        </div>

        <div class="flex min-h-full items-center justify-center p-4 text-center sm:items-center sm:p-0">
          <!-- 模态框内容使用GsapAnimation - 添加focusable属性 -->
          <GsapAnimation :from="{ y: 10, opacity: 0, scale: 0.98 }" :to="{ y: 0, opacity: 1, scale: 1 }"
            :duration="0.18" ease="power2.out"
            class="modal-content relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl sm:my-8 sm:w-full sm:max-w-lg"
            :class="[contentPadding]" ref="modalContentRef">
            <!-- 添加可聚焦元素，并使用tabindex使其可接收焦点 -->
            <div tabindex="-1" ref="focusableElementRef" class="outline-none">
              <!-- 标题与关闭按钮 -->
              <div v-if="title" class="flex items-center justify-between pb-3 sm:px-6 sm:py-4">
                <h3 class="text-lg font-medium leading-6 text-gray-900">{{ title }}</h3>
                <button type="button"
                  class="modal-close-btn rounded-md text-gray-400 hover:text-gray-500 focus:outline-none"
                  @click="emit('update:modelValue', false)">
                  <span class="sr-only">关闭</span>
                  <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              <!-- 模态框主内容 -->
              <div>
                <slot></slot>
              </div>

              <!-- 分离的页脚，用于按钮等操作 -->
              <div v-if="$slots.separateFooter" class="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
                <slot name="separateFooter"></slot>
              </div>
            </div>
          </GsapAnimation>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';

// 组件属性
const props = defineProps({
  // 控制模态框显示/隐藏
  modelValue: {
    type: Boolean,
    required: true,
    default: false
  },
  // 模态框ID 
  id: {
    type: String,
    default: 'modal'
  },
  // 模态框标题
  title: {
    type: String,
    default: ''
  },
  // 内容区域的 padding 类
  contentPadding: {
    type: String,
    default: 'p-6'
  }
});

const emit = defineEmits(['update:modelValue']);

// 引用DOM元素 - 添加一个可聚焦元素的引用
const modalContentRef = ref<InstanceType<typeof GsapAnimation> | null>(null);
const modalContainerRef = ref<HTMLElement | null>(null);
const focusableElementRef = ref<HTMLElement | null>(null);

// 键盘事件处理 - ESC键关闭模态框
const handleKeyDown = (e: KeyboardEvent) => {
  if (props.modelValue && e.key === 'Escape') {
    emit('update:modelValue', false);
  }
};

// 焦点管理 - 修复focus方法
const setupFocusTrap = () => {
  // 使用focusableElementRef而不是modalContentRef
  if (focusableElementRef.value) {
    focusableElementRef.value.focus();
  }
};

// 监听模态框显示来初始化焦点处理
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    // 短暂延迟以确保DOM已更新
    setTimeout(() => {
      setupFocusTrap();
    }, 20);
  }
});

// 生命周期钩子
onMounted(() => {
  document.addEventListener('keydown', handleKeyDown);

  // 防止模态框打开时文档滚动
  watch(() => props.modelValue, (value) => {
    if (value) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  }, { immediate: true });
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown);
  document.body.style.overflow = ''; // 确保移除时恢复滚动
});
</script>

<style scoped>
.modal-content {
  will-change: transform, opacity;
}

/* 分离背景模糊效果，解决闪烁问题 */
.backdrop-blur-effect {
  backdrop-filter: blur(0px);
  /* 初始值由GsapAnimation控制 */
  -webkit-backdrop-filter: blur(0px);
}

@media (prefers-reduced-motion: reduce) {
  .modal-content {
    transition: none !important;
  }

  .backdrop-blur-effect {
    backdrop-filter: none !important;
    -webkit-backdrop-filter: none !important;
    opacity: 1 !important;
  }
}

/* 为关闭按钮添加一些交互效果 */
.modal-close-btn {
  transition: transform 0.1s ease, color 0.1s ease;
}

.modal-close-btn:hover {
  transform: rotate(90deg);
  color: rgba(239, 68, 68, 0.9);
}
</style>
