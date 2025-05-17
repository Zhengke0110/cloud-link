<template>
    <section class="relative overflow-hidden py-16 md:py-24">
        <!-- 背景装饰元素 -->
        <div class="absolute inset-0 z-0">
            <div class="grid-pattern absolute inset-0 opacity-5"></div>
            <div class="noise-bg absolute inset-0 bg-gradient-to-br from-white to-indigo-50/30 opacity-90"></div>
            <div class="absolute -top-32 right-1/4 h-96 w-96 rounded-full bg-blue-100 opacity-60 blur-3xl"></div>
            <div class="absolute -bottom-32 -left-32 h-96 w-96 rounded-full bg-indigo-100/50 opacity-60 blur-3xl"></div>
            <div class="absolute top-1/3 right-1/4 h-64 w-64 rounded-full bg-purple-100/30 opacity-60 blur-3xl"></div>
        </div>

        <!-- 漂浮元素装饰 - 在移动端隐藏部分元素 -->
        <div class="absolute inset-0 z-0 overflow-hidden">
            <div class="floating-element absolute top-[20%] right-[15%] h-12 w-12 rounded-lg border border-blue-200/50 bg-white/80 shadow-lg backdrop-blur-sm"
                :class="{ hidden: deviceType.isMobile }"></div>
            <div class="floating-element-slow absolute top-[25%] left-[10%] h-10 w-10 rotate-12 rounded-full border border-indigo-200/50 bg-white/80 shadow-lg backdrop-blur-sm"
                :class="{ hidden: deviceType.isMobile }"></div>
            <div class="floating-element-delay absolute top-[45%] left-[25%] h-16 w-16 -rotate-12 rounded-md border border-purple-200/50 bg-white/50 shadow-lg backdrop-blur-sm"
                :class="{ hidden: deviceType.isMobile }"></div>
            <div
                class="floating-element-delay-more absolute right-[20%] bottom-[25%] h-8 w-8 rotate-45 rounded-full border border-blue-200/50 bg-white/80 shadow-lg backdrop-blur-sm">
            </div>
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 标题区域 - 调整移动端字体大小 -->
            <div class="mb-12 text-center md:mb-20">
                <span
                    class="animated-gradient-badge mb-3 inline-block rounded-full px-3 py-1 text-xs font-medium md:mb-4 md:px-4 md:py-1.5 md:text-sm">
                    探索更多应用可能性
                </span>
                <h2
                    class="reveal-text mb-4 bg-gradient-to-r from-blue-600 via-indigo-600 to-violet-600 bg-clip-text text-2xl font-extrabold tracking-tight text-transparent md:mb-6 md:text-4xl lg:text-5xl">
                    丰富的应用场景
                </h2>
                <p class="mx-auto max-w-2xl text-sm leading-relaxed text-gray-600 md:text-xl">
                    无论您是电商营销、社群运营还是数据分析，我们的短链接解决方案都能满足您的专业需求
                </p>
                <div class="mt-6 flex justify-center md:mt-8">
                    <div
                        class="h-1 w-16 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600 md:w-24">
                    </div>
                </div>
            </div>

            <!-- 替换主要场景展示部分的实现 -->
            <div class="mx-auto mb-12 md:mb-16">
                <!-- 场景指示器和切换按钮 -->
                <div class="mb-6 flex items-center justify-center md:mb-8">
                    <button @click="prevScene"
                        class="mr-3 flex h-9 w-9 items-center justify-center rounded-full border border-gray-100 bg-white text-gray-500 shadow-sm transition-all duration-300 hover:bg-indigo-600 hover:text-white hover:shadow-md disabled:cursor-not-allowed disabled:opacity-50 md:mr-6 md:h-11 md:w-11"
                        :disabled="isChangingScene">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                    </button>

                    <div class="flex space-x-2 md:space-x-3">
                        <button v-for="(_, index) in scenarioCards" :key="index" @click="setActiveScene(index)"
                            class="h-2 w-2 rounded-full transition-all duration-300 hover:scale-110 hover:border-2 hover:border-indigo-200 md:h-3 md:w-3"
                            :class="activeSceneIndex === index
                                ? 'scale-110 bg-indigo-600'
                                : 'bg-gray-300'
                                "></button>
                    </div>

                    <button @click="nextScene"
                        class="ml-3 flex h-9 w-9 items-center justify-center rounded-full border border-gray-100 bg-white text-gray-500 shadow-sm transition-all duration-300 hover:bg-indigo-600 hover:text-white hover:shadow-md disabled:cursor-not-allowed disabled:opacity-50 md:ml-6 md:h-11 md:w-11"
                        :disabled="isChangingScene">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                        </svg>
                    </button>
                </div>

                <!-- 固定高度的卡片显示容器 - 添加触摸事件支持 -->
                <div class="relative mx-auto touch-pan-y" ref="scenarioContainer"
                    :style="{ minHeight: scenarioMinHeight + 'px' }">
                    <!-- 预加载所有卡片，通过v-show控制显示 -->
                    <div v-for="(scene, index) in scenarioCards" :key="scene.title"
                        class="scenario-card invisible absolute top-0 left-0 w-full translate-x-8 transform opacity-0 transition-all duration-500"
                        :class="{
                            'visible translate-x-0 opacity-100': activeSceneIndex === index,
                        }" v-show="activeSceneIndex === index ||
                            (isChangingScene &&
                                (index === previousIndex || index === activeSceneIndex))
                            ">
                        <!-- 优化卡片样式，移动端更紧凑 -->
                        <div class="transform rounded-2xl bg-white p-5 shadow-xl transition-all duration-500 md:p-8">
                            <!-- 移动端垂直布局，桌面端水平布局 -->
                            <div class="flex flex-col md:flex-row md:items-start md:gap-8">
                                <!-- 图标居中显示(移动端)，左对齐显示(桌面端) -->
                                <div class="relative mx-auto mb-5 flex-shrink-0 order-1 md:mx-0 md:mb-0">
                                    <div class="flex h-16 w-16 items-center justify-center rounded-xl text-white shadow-lg transition-transform duration-300 hover:scale-110 hover:rotate-[-5deg] md:h-20 md:w-20"
                                        :class="`bg-gradient-to-br ${scene.gradient}`">
                                        <component :is="scene.icon" class="h-7 w-7 md:h-8 md:w-8" />
                                    </div>
                                    <div class="absolute top-1/2 left-1/2 -z-10 h-[120px] w-[120px] -translate-x-1/2 -translate-y-1/2 opacity-40 blur-[30px] transition-all duration-300 md:h-[180px] md:w-[180px] md:opacity-50 md:blur-[40px]"
                                        :style="{
                                            background: `radial-gradient(circle at center, ${scene.color}30, transparent 70%)`,
                                        }"></div>
                                </div>

                                <!-- 内容区域，移动端居中标题，桌面端左对齐 -->
                                <div class="flex-grow order-2">
                                    <div class="flex flex-col items-center md:items-start">
                                        <h3
                                            class="relative mb-3 text-center text-xl font-bold text-gray-900 md:mb-4 md:text-left md:text-2xl">
                                            {{ scene.title }}
                                            <span
                                                class="absolute bottom-[-4px] left-1/2 h-[3px] w-10 -translate-x-1/2 transform rounded-md bg-indigo-600 transition-all duration-300 md:left-0 md:translate-x-0"></span>
                                        </h3>
                                        <p
                                            class="mb-5 text-center text-sm leading-relaxed text-gray-600 md:mb-6 md:text-left md:text-lg">
                                            {{ scene.description }}
                                        </p>
                                    </div>

                                    <!-- 特性列表，移动端更紧凑 -->
                                    <div class="mb-5 grid grid-cols-1 gap-3 md:mb-6 md:gap-4">
                                        <!-- 移动端垂直卡片，更易于阅读 -->
                                        <div v-for="(feature, fIndex) in scene.features" :key="fIndex"
                                            class="feature-item flex items-start gap-2 rounded-lg bg-gray-50 p-2.5 transition-all duration-300 hover:-translate-y-0.5 hover:bg-gray-100 hover:shadow-sm md:gap-3 md:p-3">
                                            <div class="flex h-5 w-5 flex-shrink-0 items-center justify-center rounded-full text-xs font-semibold text-white md:h-6 md:w-6"
                                                :style="{ background: scene.color }">
                                                <span>{{ fIndex + 1 }}</span>
                                            </div>
                                            <span class="text-sm text-gray-700 md:text-base">{{ feature }}</span>
                                        </div>
                                    </div>

                                    <!-- 标签区域，移动端居中 -->
                                    <div class="flex flex-wrap justify-center gap-2 md:justify-start">
                                        <span v-for="(tag, tIndex) in scene.tags" :key="tIndex"
                                            class="mb-1 rounded-full px-2.5 py-1 text-xs font-semibold transition-all duration-300 hover:-translate-y-0.5 hover:bg-indigo-600 hover:text-white hover:shadow md:px-3.5 md:py-1.5"
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

                <!-- 移动端滑动指示器 - 使用视觉提示替代文字 -->
                <div v-if="deviceType.isMobile" class="mt-5 flex items-center justify-center">
                    <div class="swipe-indicator flex items-center space-x-2">
                        <div class="h-1 w-5 rounded-full bg-gray-300 animate-pulse"></div>
                        <div class="h-1.5 w-16 rounded-full bg-indigo-500"></div>
                        <div class="h-1 w-5 rounded-full bg-gray-300 animate-pulse"></div>
                    </div>
                </div>
            </div>

            <!-- 应用场景收益展示 -->
            <div class="mt-20 md:mt-32">
                <div class="mb-10 text-center md:mb-16">
                    <h3 class="mb-3 text-xl font-bold text-gray-900 md:mb-4 md:text-2xl">
                        短链接为您带来的价值
                    </h3>
                    <p class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base">
                        专业短链接服务为您的业务提供全方位助力
                    </p>
                </div>

                <div class="perspective-1000 grid grid-cols-1 gap-6 md:grid-cols-2 md:gap-8 lg:grid-cols-3">
                    <div v-for="(benefit, index) in benefits" :key="index"
                        class="translate-y-10 transform rounded-xl border border-gray-50 bg-white p-6 opacity-0 shadow-md transition-all duration-600 hover:-translate-y-2.5 hover:border-indigo-100 hover:shadow-lg md:p-8"
                        :class="{ visible: true }">
                        <div class="mb-5 flex h-14 w-14 items-center justify-center rounded-xl md:mb-6 md:h-16 md:w-16"
                            :class="`bg-${benefit.color}-50`">
                            <component :is="benefit.icon" class="h-7 w-7 md:h-8 md:w-8"
                                :class="`text-${benefit.color}-500`" />
                        </div>
                        <h4 class="mb-2 text-lg font-bold text-gray-900 md:mb-3 md:text-xl">
                            {{ benefit.title }}
                        </h4>
                        <p class="mb-5 text-sm leading-relaxed text-gray-600 md:mb-6 md:text-base">
                            {{ benefit.description }}
                        </p>
                        <div class="border-t border-gray-100 pt-4">
                            <div class="flex items-baseline">
                                <span class="text-2xl font-bold md:text-3xl" :class="`text-${benefit.color}-600`">{{
                                    benefit.stat }}</span>
                                <span class="ml-1 text-xs text-gray-500 md:text-sm">{{
                                    benefit.unit
                                    }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 适用业务场景 - 重新设计为流线型布局 -->
            <div class="mt-20 md:mt-32">
                <div class="mb-8 text-center md:mb-12">
                    <h3 class="mb-2 text-xl font-bold text-gray-900 md:text-2xl">
                        适用多种业务场景
                    </h3>
                    <p class="mx-auto mb-6 max-w-2xl text-sm text-gray-600 md:mb-8 md:text-base">
                        为各行各业提供专业的短链接解决方案
                    </p>
                </div>

                <div class="overflow-hidden py-4">
                    <div class="flex flex-wrap justify-center gap-4 md:gap-5">
                        <div v-for="(scenario, index) in businessScenarios" :key="index"
                            class="business-scenario-card relative flex w-[140px] transform flex-col overflow-hidden rounded-xl border border-gray-50 bg-white p-4 shadow-md transition-all duration-800 hover:-translate-y-2 hover:border-indigo-100 hover:shadow-lg sm:w-[200px] md:w-[260px] md:p-7"
                            :style="{
                                '--delay': `${index * 0.1}s`,
                                '--color': scenario.color,
                            }">
                            <div
                                class="absolute top-0 right-0 left-0 h-1 -translate-y-1 bg-gradient-to-r from-transparent via-indigo-600 to-transparent opacity-0 transition-all duration-300">
                            </div>
                            <div class="mx-auto mb-3 flex h-10 w-10 items-center justify-center rounded-xl text-white shadow-md transition-transform duration-300 hover:scale-110 md:mb-4 md:h-14 md:w-14"
                                :class="`bg-gradient-to-br ${scenario.gradient}`">
                                <component :is="scenario.icon" class="h-4 w-4 md:h-5 md:w-5" />
                            </div>
                            <div class="flex-1">
                                <span
                                    class="mb-1 block text-center text-base font-bold text-gray-900 md:mb-2 md:text-lg">{{
                                        scenario.name }}</span>
                                <p class="px-1 text-center text-xs text-gray-600 md:px-2 md:text-sm">
                                    {{ scenario.description }}
                                </p>
                            </div>
                            <div
                                class="mx-auto mt-3 h-0.5 w-8 rounded-full bg-gradient-to-r from-transparent via-indigo-500 to-transparent md:mt-4 md:w-10">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 客户案例轮播 -->
            <div class="mt-20 mb-6 md:mt-32 md:mb-8">
                <div class="mb-8 text-center md:mb-12">
                    <h3 class="mb-2 text-xl font-bold text-gray-900 md:text-2xl">
                        客户成功案例
                    </h3>
                    <p class="mx-auto max-w-2xl text-sm text-gray-600 md:text-base">
                        看看其他企业如何通过我们的短链接服务提升业务效果
                    </p>
                </div>

                <div class="relative mx-auto max-w-4xl overflow-hidden py-4">
                    <div class="flex transition-transform duration-600" :style="{
                        transform: `translateX(-${activeTestimonialIndex * 100}%)`,
                    }">
                        <div v-for="(testimonial, index) in testimonials" :key="index"
                            class="w-full flex-shrink-0 px-3 md:px-4">
                            <div class="testimonial-card relative rounded-xl border border-gray-50 bg-white p-4 shadow-md transition-all duration-600 md:p-6 lg:p-8"
                                :class="{ visible: true }">
                                <div class="mb-5 flex items-center justify-between md:mb-6">
                                    <img :src="testimonial.image" :alt="testimonial.company"
                                        class="h-10 w-10 rounded-lg object-contain md:h-12 md:w-12" />
                                    <div class="flex">
                                        <svg v-for="i in 5" :key="i" class="h-4 w-4 md:h-5 md:w-5" :class="i <= testimonial.rating
                                            ? 'text-yellow-400'
                                            : 'text-gray-300'
                                            " xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path
                                                d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                        </svg>
                                    </div>
                                </div>
                                <p
                                    class="mb-5 line-clamp-4 text-base leading-relaxed text-gray-600 italic md:mb-6 md:line-clamp-none md:text-lg">
                                    {{ testimonial.quote }}
                                </p>
                                <div class="mb-5 flex flex-col md:mb-6">
                                    <span class="text-base font-bold text-gray-900 md:text-lg">{{
                                        testimonial.name
                                        }}</span>
                                    <span class="text-xs text-gray-500 md:text-sm">{{
                                        testimonial.title
                                        }}</span>
                                </div>
                                <div class="flex gap-4 border-t border-gray-100 pt-4 md:gap-6 md:pt-5">
                                    <div class="flex flex-col">
                                        <span class="text-xl font-bold text-indigo-600 md:text-2xl">{{
                                            testimonial.clicksIncrease }}%</span>
                                        <span class="text-xs text-gray-500 md:text-sm">点击率提升</span>
                                    </div>
                                    <div class="flex flex-col">
                                        <span class="text-xl font-bold text-indigo-600 md:text-2xl">{{
                                            testimonial.conversionIncrease }}%</span>
                                        <span class="text-xs text-gray-500 md:text-sm">转化率增长</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 轮播指示器 -->
                    <div class="mt-4 flex justify-center space-x-2 md:mt-6">
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
import { deviceType } from "@/utils/flexible";
import {
    scenarioCards,
    testimonials,
    benefits,
    businessScenarios,
} from "../config";

// 交互状态管理
const activeSceneIndex = ref(0);
const activeTestimonialIndex = ref(0);

// 保存前一个场景索引和触摸相关状态
const previousIndex = ref(0);
const isChangingScene = ref(false);
const scenarioContainer = ref<HTMLElement | null>(null); // 添加容器引用
const touchStartX = ref(0);
const touchEndX = ref(0);

// 移动端和桌面端采用不同的默认高度
const scenarioMinHeight = ref(deviceType.value.isMobile ? 600 : 400);

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

// 添加触摸事件处理函数
const handleTouchStart = (event: any) => {
    touchStartX.value = event.touches[0].clientX;
};

const handleTouchEnd = (event: any) => {
    touchEndX.value = event.changedTouches[0].clientX;
    const touchDiff = touchEndX.value - touchStartX.value;

    // 最小滑动距离阈值
    const minSwipeDistance = 50;

    if (touchDiff > minSwipeDistance && !isChangingScene.value) {
        // 向右滑动，显示上一个场景
        prevScene();
    } else if (touchDiff < -minSwipeDistance && !isChangingScene.value) {
        // 向左滑动，显示下一个场景
        nextScene();
    }
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

// 自动轮播间隔时间设置，移动端可以设置长一些
const autoRotationInterval = deviceType.value.isMobile ? 8000 : 5000;

// 自动轮播
const { pause: pauseSceneRotation, resume: resumeSceneRotation } =
    useIntervalFn(() => {
        nextScene();
    }, autoRotationInterval);

const { pause: pauseTestimonialRotation, resume: resumeTestimonialRotation } =
    useIntervalFn(() => {
        activeTestimonialIndex.value =
            (activeTestimonialIndex.value + 1) % testimonials.length;
    }, autoRotationInterval + 1000); // 错开轮播时间，避免同时切换

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
                { threshold: 0.1 },
            );

            // 找到所有业务场景卡片
            const scenarioCards = document.querySelectorAll(
                ".business-scenario-card",
            );
            scenarioCards.forEach((card) => observer.observe(card));

            // 观察所有好处卡片
            const benefitCards = document.querySelectorAll(".perspective-1000 > div");
            benefitCards.forEach((card) => observer.observe(card));
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

        // 添加触摸事件监听器
        if (scenarioContainer.value && deviceType.value.isMobile) {
            scenarioContainer.value.addEventListener('touchstart', handleTouchStart, { passive: true });
            scenarioContainer.value.addEventListener('touchend', handleTouchEnd, { passive: true });
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

    // 移除触摸事件监听器
    if (scenarioContainer.value && deviceType.value.isMobile) {
        scenarioContainer.value.removeEventListener('touchstart', handleTouchStart);
        scenarioContainer.value.removeEventListener('touchend', handleTouchEnd);
    }
});

// 计算和更新卡片容器高度，考虑设备类型
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
            // 移动端需要更多空间，因为布局更垂直
            const extraSpace = deviceType.value.isMobile ? 40 : 20;
            scenarioMinHeight.value = maxHeight + extraSpace;
        }
    }, 100);
};

// 监听屏幕尺寸变化，重新计算高度
const onResize = () => {
    updateCardHeight();
};

// 监听活动场景变化和设备类型变化，更新卡片高度
watch(
    [activeSceneIndex, () => deviceType.value.isMobile],
    () => {
        updateCardHeight();
    },
    { immediate: true },
);
</script>

<style scoped>
/* 背景和基础样式 */
.grid-pattern {
    background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%236366F1' fill-opacity='0.1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zM36 0V4h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
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

/* 添加文本省略类 */
.line-clamp-4 {
    display: -webkit-box;
    -webkit-line-clamp: 4;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* 移动端特定响应式调整 */
@media (max-width: 640px) {
    .business-scenario-card {
        width: calc(50% - 1rem);
        min-width: 140px;
    }

    /* 加强移动端响应式样式 */
    .feature-item {
        padding: 0.5rem 0.75rem;
    }

    /* 优化移动端卡片视觉层次 */
    .scenario-card .rounded-2xl {
        border: 1px solid rgba(229, 231, 235, 0.5);
    }

    .business-scenario-card .mx-auto.mb-3 {
        margin-bottom: 0.5rem;
    }

    .business-scenario-card p {
        font-size: 0.75rem;
    }
}

@media (max-width: 480px) {

    /* 在超小屏幕上优化场景卡片布局 */
    .perspective-1000 {
        gap: 1rem;
    }

    /* 优化客户案例显示 */
    .testimonial-card {
        padding: 1rem;
    }

    .testimonial-card p {
        margin-bottom: 0.75rem;
        -webkit-line-clamp: 3;
    }
}

/* 添加触摸滑动相关样式 */
.touch-pan-y {
    touch-action: pan-y;
    /* 允许垂直滚动但捕获水平滑动 */
    -webkit-overflow-scrolling: touch;
    -webkit-tap-highlight-color: transparent;
}

/* 滑动指示器动画 */
@keyframes pulseIndication {

    0%,
    100% {
        opacity: 0.6;
    }

    50% {
        opacity: 1;
    }
}

.animate-pulse {
    animation: pulseIndication 1.5s ease-in-out infinite;
}

.swipe-indicator {
    position: relative;
    transition: all 0.3s ease;
}
</style>
