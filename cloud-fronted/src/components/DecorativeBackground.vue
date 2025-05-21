<template>
    <div class="absolute inset-0 z-0">
        <!-- 右上角模糊圆 -->
        <div class="absolute -top-24 -right-24 h-64 w-64 rounded-full bg-indigo-100/40 blur-3xl" :class="rightTopClass">
        </div>

        <!-- 左下角模糊圆 -->
        <div class="absolute -bottom-32 -left-32 h-80 w-80 rounded-full bg-blue-100/30 blur-3xl"
            :class="leftBottomClass"></div>

        <!-- 中间模糊圆 -->
        <div class="absolute top-1/3 left-1/4 h-48 w-48 rounded-full bg-purple-100/20 blur-3xl" :class="middleClass">
        </div>

        <!-- 增加一个额外的圆形，以增强视觉效果 -->
        <div class="absolute bottom-1/4 right-1/5 h-56 w-56 rounded-full bg-pink-100/20 blur-3xl"></div>

        <!-- 噪点图案覆盖层 -->
        <div class="noise-pattern absolute inset-0 opacity-[0.03]" :class="noiseClass"></div>
    </div>
</template>

<script setup lang="ts">
// 定义组件的属性，允许自定义各部分的样式
defineProps({
    rightTopClass: {
        type: String,
        default: ''
    },
    leftBottomClass: {
        type: String,
        default: ''
    },
    middleClass: {
        type: String,
        default: ''
    },
    noiseClass: {
        type: String,
        default: ''
    }
});
</script>

<style scoped>
/* 背景噪点图案 - 修正路径和格式 */
.noise-pattern {
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='1'/%3E%3C/svg%3E");
    pointer-events: none;
    /* 确保背景元素不影响交互 */
}

/* 提高性能，避免频繁重绘 */
.absolute {
    will-change: transform;
    pointer-events: none;
    /* 确保背景元素不影响交互 */
}

/* 确保模糊效果可见 */
.blur-3xl {
    filter: blur(64px);
}
</style>
