import { ref, readonly } from 'vue';

export type ToastType = 'success' | 'error' | 'warning' | 'info' | 'default';
export type ToastPosition = 'top-right' | 'top-left' | 'bottom-right' | 'bottom-left';

export interface ToastOptions {
  title?: string;
  duration?: number;
  position?: ToastPosition;
  onClose?: () => void;
}

export interface Toast {
  id: number;
  type: ToastType;
  message: string;
  title?: string;
  duration: number;
  position: ToastPosition;
  onClose?: () => void;
}

// 创建一个单例状态
const toasts = ref<Toast[]>([]);
let toastId = 0;

// 默认配置
const defaultOptions: Required<Omit<ToastOptions, 'title' | 'onClose'>> = {
  duration: 3000,
  position: 'top-right',
};

/**
 * 创建一个Toast通知系统
 */
export function useToast() {
  // 添加toast
  const addToast = (
    message: string,
    type: ToastType = 'default',
    options: ToastOptions = {}
  ) => {
    // 合并选项
    const { title, duration = defaultOptions.duration, position = defaultOptions.position, onClose } = options;
    
    // 创建toast
    const toast: Toast = {
      id: toastId++,
      type,
      message,
      title,
      duration,
      position,
      onClose
    };
    
    // 添加到列表，限制最大显示数量为5个
    if (toasts.value.length >= 5) {
      // 移除最早的toast
      const oldestToast = toasts.value.shift();
      // 如果有关闭回调则执行
      oldestToast?.onClose?.();
    }
    
    toasts.value.push(toast);
    
    // 如果duration > 0，设置自动移除
    if (duration > 0) {
      setTimeout(() => {
        removeToast(toast.id);
      }, duration);
    }
    
    return toast.id;
  };
  
  // 移除toast
  const removeToast = (id: number) => {
    const index = toasts.value.findIndex(t => t.id === id);
    if (index !== -1) {
      const toast = toasts.value[index];
      toasts.value.splice(index, 1);
      // 执行关闭回调
      toast.onClose?.();
    }
  };
  
  // 移除所有toast
  const clearToasts = () => {
    // 执行所有toast的关闭回调
    toasts.value.forEach(toast => toast.onClose?.());
    toasts.value = [];
  };
  
  // 便捷方法：成功提示
  const success = (message: string, options?: ToastOptions) => {
    return addToast(message, 'success', options);
  };
  
  // 便捷方法：错误提示
  const error = (message: string, options?: ToastOptions) => {
    return addToast(message, 'error', options);
  };
  
  // 便捷方法：警告提示
  const warning = (message: string, options?: ToastOptions) => {
    return addToast(message, 'warning', options);
  };
  
  // 便捷方法：信息提示
  const info = (message: string, options?: ToastOptions) => {
    return addToast(message, 'info', options);
  };
  
  return {
    // 状态
    toasts: readonly(toasts),
    
    // 方法
    addToast,
    removeToast,
    clearToasts,
    
    // 便捷方法
    success,
    error,
    warning,
    info,
  };
}
