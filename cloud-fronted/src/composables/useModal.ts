import { ref, reactive } from 'vue';

/**
 * 模态框状态管理Hook
 * 提供模态框的显示、隐藏和加载状态管理
 */
export function useModal<T extends Record<string, any>>(initialData: T = {} as T) {
    // 模态框显示状态
    const isVisible = ref(false);
    // 加载状态
    const isLoading = ref(false);
    // 表单数据
    const formData = reactive({ ...initialData });

    // 打开模态框
    const open = (data?: Partial<T>) => {
        console.log('open', data);
        // 如果提供了数据，则合并到表单数据中
        if (data) {
            Object.assign(formData, data);
        } else {
            // 否则重置为初始数据
            Object.assign(formData, initialData);
        }
        isVisible.value = true;
    };

    // 关闭模态框
    const close = () => {
        isVisible.value = false;
    };

    // 开始加载
    const startLoading = () => {
        isLoading.value = true;
    };

    // 结束加载
    const endLoading = () => {
        isLoading.value = false;
    };

    // 重置表单数据
    const resetForm = () => {
        Object.assign(formData, initialData);
    };

    return {
        isVisible,
        isLoading,
        formData,
        open,
        close,
        startLoading,
        endLoading,
        resetForm
    };
}
