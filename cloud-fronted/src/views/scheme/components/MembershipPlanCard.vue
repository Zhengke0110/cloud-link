<template>
    <div ref="cardRef"
        class="membership-card group relative overflow-hidden rounded-2xl border border-gray-100 bg-white p-0.5 shadow-lg mb-6 md:mb-0"
        :class="specialClass">
        <!-- 渐变边框 -->
        <div ref="borderRef" class="absolute inset-0 rounded-2xl opacity-0" :class="borderGradient"></div>

        <!-- 卡片内容 -->
        <GsapAnimation :animation="'fadeInUp'" :duration="0.7" :delay="0.1 * transitionDelay">
            <div class="relative bg-white rounded-2xl overflow-hidden">
                <!-- 顶部标题栏 -->
                <div class="p-4 md:p-6 pb-3 md:pb-4 text-center relative overflow-hidden" :class="headerBgClass">
                    <!-- 装饰圆圈 -->
                    <div class="absolute -top-10 -right-10 w-24 h-24 rounded-full bg-white/10"></div>
                    <div class="absolute -bottom-16 -left-8 w-32 h-32 rounded-full bg-white/5"></div>

                    <!-- 会员等级徽章 -->
                    <GsapAnimation animation="scale" :duration="0.5" :delay="0.2 + 0.1 * transitionDelay">
                        <div class="mb-2">
                            <span
                                class="inline-block px-3 md:px-4 py-1 md:py-1.5 rounded-full text-xs md:text-sm font-semibold shadow-sm"
                                :class="levelBadgeClass">
                                {{ levelName }}
                            </span>
                        </div>
                    </GsapAnimation>

                    <GsapAnimation animation="fadeIn" :duration="0.6" :delay="0.3 + 0.1 * transitionDelay">
                        <h3 class="text-xl md:text-3xl font-bold text-white relative z-10 mb-1">{{ plan.title }}</h3>
                    </GsapAnimation>

                    <GsapAnimation animation="fadeInUp" :duration="0.6" :delay="0.4 + 0.1 * transitionDelay">
                        <div class="mt-3 md:mt-4 flex items-center justify-center">
                            <span class="line-through text-white/70 text-sm md:text-lg mr-1 md:mr-2">¥{{ plan.oldAmount
                            }}</span>
                            <span class="text-2xl md:text-4xl font-bold text-white">¥{{ plan.amount }}</span>
                            <span class="text-white/90 ml-1">/{{ validityPeriod }}</span>
                        </div>
                    </GsapAnimation>

                    <!-- 推荐标记 -->
                    <GsapAnimation v-if="isPopular" animation="scale" :duration="0.8"
                        :delay="0.6 + 0.1 * transitionDelay">
                        <div class="absolute top-2 right-2">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-yellow-300" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path
                                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                            </svg>
                        </div>
                    </GsapAnimation>
                </div>

                <!-- 特性列表 -->
                <div class="p-4 md:p-6 pt-5 md:pt-7">
                    <ul class="space-y-3 md:space-y-4">
                        <GsapAnimation v-for="(feature, index) in features" :key="index" animation="fadeInLeft"
                            :duration="0.4" :delay="0.5 + (index * 0.1) + (0.1 * transitionDelay)">
                            <li class="flex items-start" ref="featureItems">
                                <div class="flex h-5 md:h-6 w-5 md:w-6 flex-shrink-0 items-center justify-center rounded-full shadow-inner"
                                    :class="featureIconBgClass">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-3 md:h-4 w-3 md:w-4 text-white"
                                        viewBox="0 0 20 20" fill="currentColor">
                                        <path fill-rule="evenodd"
                                            d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                            clip-rule="evenodd" />
                                    </svg>
                                </div>
                                <span class="ml-2 md:ml-3 text-sm md:text-base text-gray-700"
                                    :class="plan.level === 'THIRD' ? 'font-medium' : ''">{{ feature }}</span>
                            </li>
                        </GsapAnimation>
                    </ul>
                </div>

                <!-- 特色标签 -->
                <div class="px-4 md:px-6 pb-2">
                    <div class="flex flex-wrap gap-1 md:gap-2">
                        <GsapAnimation v-if="hasCustomDomain" animation="fadeIn" :duration="0.5"
                            :delay="0.8 + 0.1 * transitionDelay">
                            <span
                                class="inline-flex items-center rounded-full border border-indigo-200 bg-indigo-50 px-2.5 py-0.5 text-xs font-medium text-indigo-800 shadow-sm">
                                <span class="mr-1 h-1.5 w-1.5 md:h-2 md:w-2 rounded-full bg-indigo-500"></span>
                                自定义域名
                            </span>
                        </GsapAnimation>

                        <GsapAnimation v-if="hasUnlimitedJumps" animation="fadeIn" :duration="0.5"
                            :delay="0.9 + 0.1 * transitionDelay">
                            <span
                                class="inline-flex items-center rounded-full border border-blue-200 bg-blue-50 px-2.5 py-0.5 text-xs font-medium text-blue-800 shadow-sm">
                                <span class="mr-1 h-1.5 w-1.5 md:h-2 md:w-2 rounded-full bg-blue-500"></span>
                                不限跳转
                            </span>
                        </GsapAnimation>

                        <GsapAnimation v-if="isFree" animation="fadeIn" :duration="0.5"
                            :delay="1.0 + 0.1 * transitionDelay">
                            <span
                                class="inline-flex items-center rounded-full border border-amber-200 bg-amber-50 px-2.5 py-0.5 text-xs font-medium text-amber-800 shadow-sm">
                                <span class="mr-1 h-1.5 w-1.5 md:h-2 md:w-2 rounded-full bg-amber-500"></span>
                                免费方案
                            </span>
                        </GsapAnimation>
                    </div>
                </div>

                <!-- 按钮区域 -->
                <div class="p-4 md:p-6 pt-3 md:pt-5">
                    <GsapAnimation animation="fadeInUp" :duration="0.6" :delay="1.1 + 0.1 * transitionDelay">
                        <button ref="buttonRef" :class="buttonClass"
                            class="w-full rounded-lg py-2.5 md:py-3.5 text-sm md:text-base font-medium"
                            @click="$emit('select', plan)">
                            <span class="flex items-center justify-center gap-1 md:gap-2">
                                <svg v-if="plan.level === 'FIRST'" xmlns="http://www.w3.org/2000/svg"
                                    class="h-4 md:h-5 w-4 md:w-5" viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                        clip-rule="evenodd" />
                                </svg>
                                <svg v-if="plan.level === 'SECOND'" xmlns="http://www.w3.org/2000/svg"
                                    class="h-4 md:h-5 w-4 md:w-5" viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M5 2a1 1 0 011 1v1h1a1 1 0 010 2H6v1a1 1 0 01-2 0V6H3a1 1 0 010-2h1V3a1 1 0 011-1zm0 10a1 1 0 011 1v1h1a1 1 0 110 2H6v1a1 1 0 11-2 0v-1H3a1 1 0 110-2h1v-1a1 1 0 011-1zM12 2a1 1 0 01.967.744L14.146 7.2 17.5 9.134a1 1 0 010 1.732l-3.354 1.935-1.18 4.455a1 1 0 01-1.933 0L9.854 12.8 6.5 10.866a1 1 0 010-1.732l3.354-1.935 1.18-4.455A1 1 0 0112 2z"
                                        clip-rule="evenodd" />
                                </svg>
                                <svg v-if="plan.level === 'THIRD'" xmlns="http://www.w3.org/2000/svg"
                                    class="h-4 md:h-5 w-4 md:w-5" viewBox="0 0 20 20" fill="currentColor">
                                    <path
                                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                </svg>
                                {{ plan.amount === 0 ? '免费使用' : '立即购买' }}
                            </span>
                        </button>
                    </GsapAnimation>
                </div>

                <!-- 最受欢迎标签 -->
                <GsapAnimation v-if="isPopular" animation="fadeInRight" :duration="0.8"
                    :delay="0.7 + 0.1 * transitionDelay">
                    <div
                        class="absolute -right-12 md:-right-14 top-6 md:top-8 w-40 md:w-44 transform rotate-45 bg-gradient-to-r from-yellow-400 to-amber-500 py-1 md:py-1.5 text-center text-xs font-bold uppercase tracking-wider text-white shadow-md">
                        最受欢迎
                    </div>
                </GsapAnimation>

                <!-- 顶级方案标签 -->
                <GsapAnimation v-if="plan.level === 'THIRD'" animation="fadeInLeft" :duration="0.8"
                    :delay="0.7 + 0.1 * transitionDelay">
                    <div
                        class="absolute -left-3 top-8 md:top-10 px-4 md:px-5 py-1 md:py-1.5 bg-gradient-to-r from-black via-gray-900 to-yellow-600 text-white text-xs font-bold rounded-r-lg shadow-lg transform -rotate-90 -translate-x-7 md:-translate-x-8">
                        顶级方案
                    </div>
                </GsapAnimation>
            </div>
        </GsapAnimation>
    </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import GsapAnimation from '@/components/GsapAnimation.vue';
import gsap from 'gsap';

// 定义Props
const props = defineProps({
    plan: {
        type: Object,
        required: true
    },
    colorIndex: {
        type: Number,
        default: 0
    },
    transitionDelay: {
        type: Number,
        default: 0
    },
    isPopular: {
        type: Boolean,
        default: false
    }
});

// 定义事件
defineEmits(['select']);

// 计算特性
const features = computed(() => {
    return props.plan.detail.split('||').map((item: string) => {
        return item.replace(/\{\{dayTimes\}\}/, props.plan.dayTimes);
    });
});

// 计算会员等级名称
const levelName = computed(() => {
    const levelMap = {
        'FIRST': '青铜会员',
        'SECOND': '黄金会员',
        'THIRD': '黑金会员'
    };
    return levelMap[props.plan.level as keyof typeof levelMap];
});

// 获取会员有效期
const validityPeriod = computed(() => {
    return props.plan.validDay === 1 ? '天' : '月';
});

// 特色标签判断
const hasCustomDomain = computed(() => {
    return props.plan.detail.includes('自定义域名');
});

const hasUnlimitedJumps = computed(() => {
    return props.plan.detail.includes('不限制');
});

const isFree = computed(() => {
    return props.plan.amount === 0;
});

// 样式类计算
const specialClass = computed(() => {
    switch (props.plan.level) {
        case 'SECOND':
            return 'md:scale-105 md:-translate-y-2 z-10';
        case 'THIRD':
            return 'md:bg-gradient-to-b md:from-white md:to-indigo-50/30 scale-100';
        default:
            return '';
    }
});

const borderGradient = computed(() => {
    switch (props.plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-br from-amber-500 via-amber-600 to-amber-700';
        case 'SECOND':
            return 'bg-gradient-to-br from-yellow-300 via-yellow-500 to-amber-500';
        case 'THIRD':
            return 'bg-gradient-to-br from-black via-gray-800 to-yellow-500';
        default:
            return '';
    }
});

const headerBgClass = computed(() => {
    switch (props.plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-r from-amber-600 to-amber-800';
        case 'SECOND':
            return 'bg-gradient-to-r from-yellow-400 to-amber-500';
        case 'THIRD':
            return 'bg-gradient-to-r from-black via-gray-900 to-yellow-600';
        default:
            return '';
    }
});

const levelBadgeClass = computed(() => {
    switch (props.plan.level) {
        case 'FIRST':
            return 'bg-white/20 text-white backdrop-blur-sm';
        case 'SECOND':
            return 'bg-yellow-300 text-amber-800 ring-2 ring-yellow-200';
        case 'THIRD':
            return 'bg-gradient-to-r from-black to-yellow-500 text-white shadow-md ring-2 ring-yellow-200/50';
        default:
            return '';
    }
});

const featureIconBgClass = computed(() => {
    switch (props.plan.level) {
        case 'FIRST':
            return 'bg-amber-600';
        case 'SECOND':
            return 'bg-yellow-500';
        case 'THIRD':
            return 'bg-black ring-2 ring-yellow-300/30';
        default:
            return 'bg-blue-500';
    }
});

const buttonClass = computed(() => {
    switch (props.plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-r from-amber-500 to-amber-700 text-white hover:shadow-lg hover:shadow-amber-200/50 active:scale-95';
        case 'SECOND':
            return 'bg-gradient-to-r from-yellow-400 to-amber-500 text-white shadow-lg hover:shadow-amber-300/50 hover:-translate-y-0.5 active:scale-95 ring-2 ring-yellow-200/50';
        case 'THIRD':
            return 'bg-gradient-to-r from-black to-yellow-600 text-white shadow-lg hover:shadow-yellow-500/30 hover:-translate-y-0.5 active:scale-95 ring-2 ring-yellow-400/30';
        default:
            return 'bg-gray-200 text-gray-600';
    }
});



// 添加DOM引用
const cardRef = ref<HTMLElement | null>(null);
const borderRef = ref<HTMLElement | null>(null);
const buttonRef = ref<HTMLElement | null>(null);
const featureItems = ref<HTMLElement[]>([]);

// 使用GSAP设置卡片悬停动画
onMounted(() => {
    if (!cardRef.value) return;

    const ctx = gsap.context(() => {
        // 卡片悬停效果
        cardRef.value?.addEventListener('mouseenter', () => {
            gsap.to(cardRef.value, {
                y: -8,
                boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
                duration: 0.4,
                ease: 'power2.out'
            });

            // 边框动画
            gsap.to(borderRef.value, {
                opacity: 1,
                duration: 0.3,
                ease: 'power1.out'
            });

            // 按钮悬停效果
            if (buttonRef.value) {
                gsap.to(buttonRef.value, {
                    scale: 1.03,
                    duration: 0.3,
                    ease: 'back.out(1.5)'
                });
            }
        });

        cardRef.value?.addEventListener('mouseleave', () => {
            gsap.to(cardRef.value, {
                y: 0,
                boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
                duration: 0.4,
                ease: 'power2.out'
            });

            // 边框动画
            gsap.to(borderRef.value, {
                opacity: 0,
                duration: 0.3,
                ease: 'power1.out'
            });

            // 按钮悬停效果
            if (buttonRef.value) {
                gsap.to(buttonRef.value, {
                    scale: 1,
                    duration: 0.3,
                    ease: 'back.out(1.5)'
                });
            }
        });
    }, cardRef);

    // 组件卸载时清理
    return () => ctx.revert();
});
</script>