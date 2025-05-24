<template>
    <div class="absolute inset-0 z-0">
        <div class="absolute -top-24 -right-24 h-64 w-64 rounded-full bg-indigo-100/40 blur-3xl"></div>
        <div class="absolute -bottom-32 -left-32 h-80 w-80 rounded-full bg-blue-100/30 blur-3xl"></div>
        <div class="absolute top-1/3 left-1/4 h-48 w-48 rounded-full bg-purple-100/20 blur-3xl"></div>
        <div class="absolute top-2/3 right-1/3 h-36 w-36 rounded-full bg-cyan-100/20 blur-2xl"></div>
        <div class="noise-pattern absolute inset-0 opacity-[0.03]"></div>

        <!-- 装饰性浮动元素 -->
        <div
            class="floating-element-slow absolute top-[15%] left-[15%] h-8 w-8 rounded-full border border-indigo-200/50 bg-white/80 shadow-sm backdrop-blur-sm hidden md:block">
        </div>
        <div
            class="floating-element-reverse absolute bottom-[20%] right-[10%] h-10 w-10 rotate-45 rounded-lg border border-blue-200/50 bg-white/60 shadow-sm backdrop-blur-sm hidden md:block">
        </div>
        <div
            class="floating-element absolute top-[40%] right-[20%] h-6 w-6 rounded-lg border border-purple-200/50 bg-white/80 shadow-sm backdrop-blur-sm hidden md:block">
        </div>
    </div>
</template>

<script setup lang="ts">
// 无需额外逻辑
</script>

<style scoped>
/* 背景噪点图案 */
.noise-pattern {
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='1'/%3E%3C/svg%3E");
}

/* 浮动元素动画 */
@keyframes floating {
    0%, 100% {
        transform: translateY(0) rotate(0deg);
    }

    50% {
        transform: translateY(-15px) rotate(5deg);
    }
}

@keyframes floating-reverse {
    0%, 100% {
        transform: translateY(0) rotate(45deg);
    }

    50% {
        transform: translateY(-15px) rotate(40deg);
    }
}

.floating-element {
    animation: floating 6s ease-in-out infinite;
}

.floating-element-slow {
    animation: floating 8s ease-in-out infinite;
}

.floating-element-reverse {
    animation: floating-reverse 7s ease-in-out infinite;
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {
    .floating-element,
    .floating-element-slow,
    .floating-element-reverse {
        animation: none !important;
        transition: none !important;
    }
}
</style>