<template>
    <div
        class="card-container group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl">
        <!-- 卡片边框渐变 -->
        <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
            :class="getBorderGradient(colorIndex)"></div>

        <!-- 卡片内容 -->
        <div class="relative overflow-hidden rounded-xl bg-white">
            <!-- 顶部标题栏 -->
            <div class="relative overflow-hidden p-5" :class="getHeaderGradient(colorIndex)">
                <!-- 装饰圆圈 -->
                <div class="absolute -top-10 -right-10 h-24 w-24 rounded-full bg-white/10"></div>
                <div class="absolute -bottom-16 -left-8 h-32 w-32 rounded-full bg-white/5"></div>

                <div class="relative z-10 flex items-center justify-between">
                    <!-- 标题区域 -->
                    <h3 class="text-xl font-bold text-white">
                        <slot name="title">{{ title }}</slot>
                    </h3>

                    <!-- 操作按钮区域 -->
                    <div class="flex items-center gap-2">
                        <slot name="header-actions">
                            <span class="text-sm text-white/80">操作</span>
                        </slot>
                    </div>
                </div>
            </div>

            <!-- 卡片主体内容 -->
            <div class="p-6">
                <slot></slot>

                <!-- 底部操作按钮 -->
                <div class="mt-5 flex justify-end space-x-3">
                    <slot name="footer-actions">
                        <!-- 默认底部操作按钮 -->
                    </slot>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {
    getHeaderGradient,
    getBorderGradient,
    getIconColor,
    getActionButtonBg
} from "@/utils/ColorSchemeProvider";

// 组件属性定义
defineProps({
    // 卡片标题
    title: {
        type: String,
        default: ''
    },
    // 用于颜色方案的索引
    colorIndex: {
        type: Number,
        default: 0
    },
    // 是否启用悬浮效果
    enableHover: {
        type: Boolean,
        default: true
    }
});

// 暴露颜色工具方法供模板使用
defineExpose({
    getIconColor,
    getActionButtonBg
});
</script>

<style scoped>
/* 卡片基础样式 */
.card-container {
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.05),
        0 2px 4px -1px rgba(0, 0, 0, 0.03);
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    position: relative;
    z-index: 1;
}

.card-container:hover {
    transform: translateY(-0.5rem);
    z-index: 2;
    box-shadow:
        0 12px 20px -3px rgba(0, 0, 0, 0.07),
        0 6px 8px -2px rgba(0, 0, 0, 0.04);
}

/* 移动端优化 */
@media (max-width: 640px) {

    /* 改善移动端按钮点击区域 */
    button {
        min-height: 44px;
    }
}

/* 修复一些动画问题 */
@media (prefers-reduced-motion: reduce) {
    .card-container {
        transform: none !important;
        transition: none !important;
    }
}
</style>
