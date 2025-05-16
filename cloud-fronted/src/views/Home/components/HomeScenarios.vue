<template>
    <section class="relative overflow-hidden py-24">
        <!-- 背景装饰元素 -->
        <div class="absolute inset-0 z-0">
            <div class="grid-pattern absolute inset-0 opacity-5"></div>
            <div class="noise-bg absolute inset-0 bg-gradient-to-br from-white to-indigo-50/30 opacity-90"></div>
            <div class="absolute -top-32 right-1/4 h-96 w-96 rounded-full bg-blue-100 opacity-60 blur-3xl"></div>
            <div class="absolute -bottom-32 -left-32 h-96 w-96 rounded-full bg-indigo-100/50 opacity-60 blur-3xl"></div>
            <div class="absolute top-1/3 right-1/4 h-64 w-64 rounded-full bg-purple-100/30 opacity-60 blur-3xl"></div>
        </div>

        <!-- 漂浮元素装饰 -->
        <div class="absolute inset-0 z-0 overflow-hidden">
            <div
                class="floating-element absolute top-[20%] right-[15%] h-12 w-12 rounded-lg border border-blue-200/50 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-slow absolute top-[25%] left-[10%] h-10 w-10 rotate-12 rounded-full border border-indigo-200/50 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-delay absolute top-[45%] left-[25%] h-16 w-16 -rotate-12 rounded-md border border-purple-200/50 bg-white/50 shadow-lg backdrop-blur-sm">
            </div>
            <div
                class="floating-element-delay-more absolute right-[20%] bottom-[25%] h-8 w-8 rotate-45 rounded-full border border-blue-200/50 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 标题区域 -->
            <div class="mb-20 text-center">
                <span class="animated-gradient-badge mb-4 inline-block rounded-full px-4 py-1.5 text-sm font-medium">
                    探索更多应用可能性
                </span>
                <h2
                    class="reveal-text mb-6 bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 bg-clip-text text-4xl font-extrabold tracking-tight text-transparent md:text-5xl">
                    丰富的应用场景
                </h2>
                <p class="mx-auto max-w-2xl text-lg leading-relaxed text-gray-600 md:text-xl">
                    无论您是电商营销、社群运营还是数据分析，我们的短链接解决方案都能满足您的专业需求
                </p>
                <div class="mt-8 flex justify-center">
                    <div class="h-1 w-24 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600">
                    </div>
                </div>
            </div>

            <!-- 替换主要场景展示部分的实现 -->
            <div class="mx-auto mb-16">
                <!-- 场景指示器和切换按钮 -->
                <div class="mb-8 flex items-center justify-center">
                    <button @click="prevScene"
                        class="mr-6 flex h-11 w-11 items-center justify-center rounded-full border border-gray-100 bg-white text-gray-500 shadow-sm transition-all duration-300 hover:bg-indigo-600 hover:text-white hover:shadow-md disabled:cursor-not-allowed disabled:opacity-50"
                        :disabled="isChangingScene">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                    </button>

                    <div class="flex space-x-3">
                        <button v-for="(_, index) in scenarioCards" :key="index" @click="setActiveScene(index)"
                            class="h-3 w-3 rounded-full transition-all duration-300 hover:scale-110 hover:border-2 hover:border-indigo-200"
                            :class="activeSceneIndex === index
                                ? 'scale-110 bg-indigo-600'
                                : 'bg-gray-300'
                                "></button>
                    </div>

                    <button @click="nextScene"
                        class="ml-6 flex h-11 w-11 items-center justify-center rounded-full border border-gray-100 bg-white text-gray-500 shadow-sm transition-all duration-300 hover:bg-indigo-600 hover:text-white hover:shadow-md disabled:cursor-not-allowed disabled:opacity-50"
                        :disabled="isChangingScene">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                        </svg>
                    </button>
                </div>

                <!-- 固定高度的卡片显示容器 -->
                <div class="relative mx-auto" :style="{ minHeight: scenarioMinHeight + 'px' }">
                    <!-- 预加载所有卡片，通过v-show控制显示 -->
                    <div v-for="(scene, index) in scenarioCards" :key="scene.title"
                        class="invisible absolute top-0 left-0 w-full translate-x-8 transform opacity-0 transition-all duration-500"
                        :class="{
                            'visible translate-x-0 opacity-100': activeSceneIndex === index,
                        }" v-show="activeSceneIndex === index ||
                            (isChangingScene &&
                                (index === previousIndex || index === activeSceneIndex))
                            ">
                        <div class="transform rounded-2xl bg-white p-8 shadow-xl transition-all duration-500">
                            <div class="flex flex-col md:flex-row md:items-start md:gap-8">
                                <div class="relative mb-6 flex-shrink-0 md:mb-0">
                                    <div class="flex h-20 w-20 items-center justify-center rounded-xl text-white shadow-lg transition-transform duration-300 hover:scale-110 hover:rotate-[-5deg]"
                                        :class="`bg-gradient-to-br ${scene.gradient}`">
                                        <component :is="scene.icon" class="h-8 w-8" />
                                    </div>
                                    <div class="absolute top-1/2 left-1/2 -z-10 h-[180px] w-[180px] -translate-x-1/2 -translate-y-1/2 opacity-50 blur-[40px] transition-all duration-300 hover:h-[200px] hover:w-[200px] hover:opacity-80"
                                        :style="{
                                            background: `radial-gradient(circle at center, ${scene.color}20, transparent 70%)`,
                                        }"></div>
                                </div>

                                <div class="flex-grow">
                                    <h3 class="relative mb-4 inline-block text-2xl font-bold text-gray-900">
                                        {{ scene.title }}
                                        <span
                                            class="absolute bottom-[-4px] left-0 h-[3px] w-10 rounded-md bg-indigo-600 transition-all duration-300 group-hover:w-[60px]"></span>
                                    </h3>
                                    <p class="mb-6 text-lg leading-relaxed text-gray-600">
                                        {{ scene.description }}
                                    </p>

                                    <div class="sm:grid-cols-auto-fit mb-6 grid grid-cols-1 gap-4 sm:gap-4">
                                        <div v-for="(feature, fIndex) in scene.features" :key="fIndex"
                                            class="flex items-start gap-3 rounded-lg bg-gray-50 p-3 transition-all duration-300 hover:-translate-y-0.5 hover:bg-gray-100 hover:shadow-sm">
                                            <div class="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full text-xs font-semibold text-white"
                                                :style="{ background: scene.color }">
                                                <span>{{ fIndex + 1 }}</span>
                                            </div>
                                            <span class="text-gray-700">{{ feature }}</span>
                                        </div>
                                    </div>

                                    <div class="flex flex-wrap gap-2">
                                        <span v-for="(tag, tIndex) in scene.tags" :key="tIndex"
                                            class="rounded-full px-3.5 py-1.5 text-xs font-semibold transition-all duration-300 hover:-translate-y-0.5 hover:bg-indigo-600 hover:text-white hover:shadow"
                                            :style="{
                                                color: scene.color,
                                                backgroundColor: `${scene.color}10`,
                                            }">
                                            {{ tag }}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 应用场景收益展示 -->
            <div class="mt-32">
                <div class="mb-16 text-center">
                    <h3 class="mb-4 text-2xl font-bold text-gray-900">
                        短链接为您带来的价值
                    </h3>
                    <p class="mx-auto max-w-2xl text-gray-600">
                        专业短链接服务为您的业务提供全方位助力
                    </p>
                </div>

                <div class="perspective-1000 grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
                    <div v-for="(benefit, index) in benefits" :key="index"
                        class="translate-y-10 transform rounded-xl border border-gray-50 bg-white p-8 opacity-0 shadow-md transition-all duration-600 hover:-translate-y-2.5 hover:border-indigo-100 hover:shadow-lg"
                        :class="{ visible: true }">
                        <div class="mb-6 flex h-16 w-16 items-center justify-center rounded-xl"
                            :class="`bg-${benefit.color}-50`">
                            <component :is="benefit.icon" class="h-8 w-8" :class="`text-${benefit.color}-500`" />
                        </div>
                        <h4 class="mb-3 text-xl font-bold text-gray-900">
                            {{ benefit.title }}
                        </h4>
                        <p class="mb-6 leading-relaxed text-gray-600">
                            {{ benefit.description }}
                        </p>
                        <div class="border-t border-gray-100 pt-4">
                            <div class="flex items-baseline">
                                <span class="text-3xl font-bold" :class="`text-${benefit.color}-600`">{{ benefit.stat
                                }}</span>
                                <span class="ml-1 text-sm text-gray-500">{{
                                    benefit.unit
                                }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 适用业务场景 - 重新设计为流线型布局 -->
            <div class="mt-32">
                <div class="mb-12 text-center">
                    <h3 class="mb-2 text-2xl font-bold text-gray-900">
                        适用多种业务场景
                    </h3>
                    <p class="mx-auto mb-8 max-w-2xl text-gray-600">
                        为各行各业提供专业的短链接解决方案
                    </p>
                </div>

                <div class="overflow-hidden py-4">
                    <div class="flex flex-wrap justify-center gap-5">
                        <div v-for="(scenario, index) in businessScenarios" :key="index"
                            class="business-scenario-card relative flex w-[260px] transform flex-col overflow-hidden rounded-xl border border-gray-50 bg-white p-7 shadow-md transition-all duration-800 hover:-translate-y-2 hover:border-indigo-100 hover:shadow-lg"
                            :style="{
                                '--delay': `${index * 0.1}s`,
                                '--color': scenario.color,
                            }">
                            <div
                                class="absolute top-0 right-0 left-0 h-1 -translate-y-1 bg-gradient-to-r from-transparent via-indigo-600 to-transparent opacity-0 transition-all duration-300">
                            </div>
                            <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl text-white shadow-md transition-transform duration-300 hover:scale-110"
                                :class="`bg-gradient-to-br ${scenario.gradient}`">
                                <component :is="scenario.icon" class="h-5 w-5" />
                            </div>
                            <div class="flex-1">
                                <span class="mb-2 block text-center text-lg font-bold text-gray-900">{{ scenario.name
                                }}</span>
                                <p class="px-2 text-center text-sm text-gray-600">
                                    {{ scenario.description }}
                                </p>
                            </div>
                            <div
                                class="mx-auto mt-4 h-0.5 w-10 rounded-full bg-gradient-to-r from-transparent via-indigo-500 to-transparent">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 客户案例轮播 -->
            <div class="mt-32 mb-8">
                <div class="mb-12 text-center">
                    <h3 class="mb-2 text-2xl font-bold text-gray-900">客户成功案例</h3>
                    <p class="mx-auto max-w-2xl text-gray-600">
                        看看其他企业如何通过我们的短链接服务提升业务效果
                    </p>
                </div>

                <div class="relative mx-auto max-w-4xl overflow-hidden py-4">
                    <div class="flex transition-transform duration-600" :style="{
                        transform: `translateX(-${activeTestimonialIndex * 100}%)`,
                    }">
                        <div v-for="(testimonial, index) in testimonials" :key="index"
                            class="w-full flex-shrink-0 px-4">
                            <div class="testimonial-card relative rounded-xl border border-gray-50 bg-white p-6 shadow-md transition-all duration-600 md:p-8"
                                :class="{ 'visible': true }">
                                <div class="mb-6 flex items-center justify-between">
                                    <img :src="testimonial.image" :alt="testimonial.company"
                                        class="h-12 w-12 rounded-lg object-contain" />
                                    <div class="flex">
                                        <svg v-for="i in 5" :key="i" class="h-5 w-5" :class="i <= testimonial.rating
                                            ? 'text-yellow-400'
                                            : 'text-gray-300'
                                            " xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path
                                                d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                        </svg>
                                    </div>
                                </div>
                                <p class="mb-6 text-lg leading-relaxed text-gray-600 italic">
                                    {{ testimonial.quote }}
                                </p>
                                <div class="mb-6 flex flex-col">
                                    <span class="text-lg font-bold text-gray-900">{{
                                        testimonial.name
                                    }}</span>
                                    <span class="text-sm text-gray-500">{{
                                        testimonial.title
                                    }}</span>
                                </div>
                                <div class="flex gap-6 border-t border-gray-100 pt-5">
                                    <div class="flex flex-col">
                                        <span class="text-2xl font-bold text-indigo-600">{{ testimonial.clicksIncrease
                                        }}%</span>
                                        <span class="text-sm text-gray-500">点击率提升</span>
                                    </div>
                                    <div class="flex flex-col">
                                        <span class="text-2xl font-bold text-indigo-600">{{
                                            testimonial.conversionIncrease }}%</span>
                                        <span class="text-sm text-gray-500">转化率增长</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 轮播指示器 -->
                    <div class="mt-6 flex justify-center space-x-2">
                        <button v-for="(_, index) in testimonials" :key="index" @click="setActiveTestimonial(index)"
                            class="h-2 w-2 rounded-full bg-gray-300 transition-all duration-300 hover:bg-indigo-400"
                            :class="{
                                'scale-125 bg-indigo-600': activeTestimonialIndex === index,
                            }"></button>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from "vue";
import { useIntervalFn } from "@vueuse/core";
import {
    scenarioCards,
    testimonials,
    benefits,
    businessScenarios,
} from "../config";

// 交互状态管理
const activeSceneIndex = ref(0);
const activeTestimonialIndex = ref(0);

// 保存前一个场景索引
const previousIndex = ref(0);
// 添加切换状态标记
const isChangingScene = ref(false);
// 添加场景卡片最小高度计算，以防止高度跳动导致的白屏
const scenarioMinHeight = ref(400); // 默认最小高度

// 改进的场景卡片切换函数
const setActiveScene = (index: number) => {
    if (isChangingScene.value || index === activeSceneIndex.value) return;

    isChangingScene.value = true;
    previousIndex.value = activeSceneIndex.value;
    activeSceneIndex.value = index;

    // 在过渡完成后重置状态
    setTimeout(() => {
        isChangingScene.value = false;
    }, 500); // 与CSS过渡时间匹配
};

// 添加切换当前展示的客户案例的方法
const setActiveTestimonial = (index: number) => {
    activeTestimonialIndex.value = index;
};

const nextScene = () => {
    if (isChangingScene.value) return;
    setActiveScene((activeSceneIndex.value + 1) % scenarioCards.length);
};

const prevScene = () => {
    if (isChangingScene.value) return;
    setActiveScene(
        (activeSceneIndex.value - 1 + scenarioCards.length) % scenarioCards.length,
    );
};

// 自动轮播
const { pause: pauseSceneRotation, resume: resumeSceneRotation } =
    useIntervalFn(() => {
        nextScene();
    }, 5000);

const { pause: pauseTestimonialRotation, resume: resumeTestimonialRotation } =
    useIntervalFn(() => {
        activeTestimonialIndex.value =
            (activeTestimonialIndex.value + 1) % testimonials.length;
    }, 6000);

onMounted(() => {
    try {
        // 检查浏览器是否支持 IntersectionObserver
        if ("IntersectionObserver" in window) {
            const observer = new IntersectionObserver(
                (entries) => {
                    entries.forEach((entry) => {
                        if (entry.isIntersecting) {
                            entry.target.classList.add("visible");
                        }
                    });
                },
                { threshold: 0.1 }
            );

            // 找到所有业务场景卡片
            const scenarioCards = document.querySelectorAll('.business-scenario-card');
            scenarioCards.forEach(card => observer.observe(card));
        } else {
            // 降级处理：直接将所有元素设为可见
            console.warn("浏览器不支持 IntersectionObserver，将直接显示所有元素");
            document
                .querySelectorAll(
                    ".reveal-text, .scene-card-3d, .benefit-card, .business-scenario-card, .testimonial-card",
                )
                .forEach((el) => {
                    el.classList.add("visible");
                });
        }

        // 启动自动轮播
        resumeSceneRotation();
        resumeTestimonialRotation();

        // 初始化卡片高度
        updateCardHeight();

        // 添加窗口大小改变监听
        window.addEventListener("resize", onResize);

        // 安全地预热组件渲染
        scenarioCards.forEach((scene) => {
            try {
                // 更健壮的类型检查
                if (
                    scene.icon &&
                    typeof scene.icon === "object" &&
                    "render" in scene.icon
                ) {
                    // 使用类型断言告诉TypeScript scene.icon有一个render方法
                    const iconComponent = scene.icon as { render: () => void };
                    if (typeof iconComponent.render === "function") {
                        // 强制渲染组件以确保它被缓存
                        iconComponent.render();
                    }
                }
            } catch (iconError) {
                console.error("渲染场景图标时出错:", iconError);
            }
        });
    } catch (error) {
        console.error("组件挂载时出错:", error);
        // 确保即使出错也能使组件显示
        document
            .querySelectorAll(
                ".reveal-text, .scene-card-3d, .benefit-card, .business-scenario-card, .testimonial-card",
            )
            .forEach((el) => {
                el.classList.add("visible");
            });
    }
});

onBeforeUnmount(() => {
    pauseSceneRotation();
    pauseTestimonialRotation();
    window.removeEventListener("resize", onResize);
});

// 计算和更新卡片容器高度
const updateCardHeight = () => {
    // 延迟计算以确保DOM已更新
    setTimeout(() => {
        const cards = document.querySelectorAll(".scenario-card");
        if (cards.length > 0) {
            let maxHeight = 0;
            cards.forEach((card) => {
                const height = card.scrollHeight;
                if (height > maxHeight) {
                    maxHeight = height;
                }
            });
            // 添加额外空间以确保有足够空间
            scenarioMinHeight.value = maxHeight + 20;
        }
    }, 100);
};

// 监听屏幕尺寸变化，重新计算高度
const onResize = () => {
    updateCardHeight();
};

// 监听活动场景变化，更新卡片高度
watch(activeSceneIndex, () => {
    updateCardHeight();
});
</script>

<style scoped>
/* 背景和基础样式 */
.grid-pattern {
    background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%236366F1' fill-opacity='0.1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.noise-bg {
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='0.1'/%3E%3C/svg%3E");
}

/* 浮动元素动画 */
@keyframes floating {
    0% {
        transform: translateY(0px) rotate(0deg);
    }

    50% {
        transform: translateY(-15px) rotate(2deg);
    }

    100% {
        transform: translateY(0px) rotate(0deg);
    }
}

.floating-element {
    animation: floating 6s ease-in-out infinite;
}

.floating-element-slow {
    animation: floating 10s ease-in-out infinite;
}

.floating-element-delay {
    animation: floating 8s ease-in-out infinite;
    animation-delay: 1s;
}

.floating-element-delay-more {
    animation: floating 9s ease-in-out infinite;
    animation-delay: 3s;
}

/* 标题区域动画 */
@keyframes gradient-title {

    0%,
    100% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }
}

.animated-gradient-badge {
    background-image: linear-gradient(-45deg, #6366f1, #3b82f6, #8b5cf6);
    background-size: 200% 200%;
    animation: gradient-title 4s ease infinite;
    color: white;
}

.reveal-text {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-text.visible {
    opacity: 1;
    transform: translateY(0);
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {

    .floating-element,
    .floating-element-slow,
    .floating-element-delay,
    .floating-element-delay-more,
    .animated-gradient-badge {
        animation: none !important;
        transition: none !important;
        transform: none !important;
    }

    .reveal-text {
        opacity: 1;
    }
}

/* 添加可见性类 */
.visible {
    opacity: 1 !important;
    transform: translateY(0) !important;
}

/* 为一些特定功能保留定制的动画类 */
@keyframes fadeInStagger {
    from {
        opacity: 0;
        transform: translateY(30px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 业务场景卡片动画 */
.business-scenario-card {
    opacity: 0;
    transform: translateY(30px);
    transition: all 0.6s cubic-bezier(0.22, 1, 0.36, 1);
    transition-delay: var(--delay, 0s);
}

.business-scenario-card.visible {
    opacity: 1;
    transform: translateY(0);
}

/* 业务场景卡片顶部渐变条动画 */
.business-scenario-card:hover .absolute {
    transform: translateY(0);
    opacity: 1;
}

/* 客户案例卡片动画 */
.testimonial-card {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}

.testimonial-card.visible {
    opacity: 1;
    transform: translateY(0);
}

/* 确保轮播容器中的卡片正确显示 */
.testimonial-card:not(.visible) {
    pointer-events: none;
}

/* 响应式调整 */
@media (max-width: 640px) {
    .business-scenario-card {
        width: 100%;
        max-width: 300px;
    }
}
</style>
