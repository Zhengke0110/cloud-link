<template>
  <Teleport to="body">
    <div 
      v-if="modelValue" 
      :id="id"
      class="fixed inset-0 z-50 overflow-y-auto" 
      :aria-labelledby="ariaLabelledby" 
      role="dialog" 
      aria-modal="true"
    >
      <div class="flex min-h-screen items-center justify-center px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <!-- 背景遮罩 -->
        <div class="bg-opacity-75 fixed inset-0 bg-gray-500 transition-opacity" aria-hidden="true" @click="closeOnMaskClick && close()"></div>

        <!-- 使模态框居中的技巧 -->
        <span class="hidden sm:inline-block sm:h-screen sm:align-middle" aria-hidden="true">&#8203;</span>

        <!-- 模态框内容 -->
        <div 
          class="relative inline-block w-full transform overflow-hidden rounded-lg bg-white text-left align-bottom shadow-xl transition-all sm:my-8 sm:align-middle"
          :class="[width, 'sm:w-full']"
          @click.stop
        >
          <!-- 手机端顶部拖动条示意 -->
          <div class="mx-auto my-2 h-1 w-12 rounded-full bg-gray-300 sm:hidden"></div>

          <div :class="contentPadding">
            <!-- 标题和关闭按钮 -->
            <div v-if="title || showClose" class="mb-5 flex items-center justify-between">
              <h3 v-if="title" class="text-lg leading-6 font-medium text-gray-900" :id="titleId">
                {{ title }}
              </h3>
              <div v-else class="flex-grow"></div>
              <button 
                v-if="showClose" 
                type="button" 
                @click="close"
                class="rounded-md p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-500 focus:outline-none"
                aria-label="关闭"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <!-- 插槽内容 -->
            <div class="modal-body">
              <slot></slot>
            </div>
            
            <!-- 底部按钮 -->
            <div v-if="$slots.footer" :class="[footerClass, {'mt-5': !footerClass}]">
              <slot name="footer"></slot>
            </div>
          </div>

          <!-- 独立的底部区域 (当需要不同背景色时) -->
          <div v-if="$slots.separateFooter" :class="separateFooterClass">
            <slot name="separateFooter"></slot>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from "vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  // 明确添加 id 作为 prop
  id: {
    type: String,
    default: () => `modal-${Math.random().toString(36).substr(2, 9)}`
  },
  showClose: {
    type: Boolean,
    default: true
  },
  closeOnMaskClick: {
    type: Boolean,
    default: true
  },
  width: {
    type: String,
    default: 'sm:max-w-lg'
  },
  contentPadding: {
    type: String,
    default: 'px-4 pt-5 pb-4 sm:p-6 sm:pb-4'
  },
  footerClass: {
    type: String,
    default: ''
  },
  separateFooterClass: {
    type: String,
    default: 'bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6'
  }
});

const emit = defineEmits(['update:modelValue', 'close']);

// 为标题元素生成ID (用于无障碍访问)
const titleId = computed(() => `${props.id}-title`);

// 为 aria-labelledby 属性提供值
const ariaLabelledby = computed(() => props.title ? titleId.value : undefined);

// 关闭模态框
const close = () => {
  emit('update:modelValue', false);
  emit('close');
};

// 处理ESC键关闭
const handleEscKey = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && props.modelValue) {
    close();
  }
};

// 挂载和卸载事件监听器
onMounted(() => {
  document.addEventListener('keydown', handleEscKey);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleEscKey);
});

// 当模态框打开时，禁止背景滚动
const originalOverflow = ref('');
const lockScroll = () => {
  originalOverflow.value = document.body.style.overflow;
  document.body.style.overflow = 'hidden';
};

const unlockScroll = () => {
  document.body.style.overflow = originalOverflow.value;
};

// 监听模态框开关状态
const isOpen = computed(() => props.modelValue);

// 监听模态框状态变化，控制滚动锁定
import { watch } from 'vue';

watch(isOpen, (val) => {
  if (val) {
    lockScroll();
  } else {
    unlockScroll();
  }
}, { immediate: true });
</script>

<style scoped>
/* 模态框动画 */
.fixed {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 模态框内容动画 */
.transform {
  animation: slideIn 0.3s ease-in-out;
}

@keyframes slideIn {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 模态框移动端优化 */
@media (max-width: 640px) {
  /* 确保模态框内容在移动设备上更易于点击 */
  input,
  select,
  button {
    font-size: 16px; /* 防止iOS自动缩放 */
    min-height: 44px; /* 确保触摸友好 */
  }
}
</style>
