<template>
    <!-- 页脚 -->
    <footer class="bg-gray-800 py-8 text-white">
        <div class="container mx-auto px-4 text-center sm:px-6 lg:px-8">
            <!-- 页脚顶部带波浪动画 -->
            <div class="relative mb-8 h-10 w-full overflow-hidden">
                <div class="wave-animation absolute inset-x-0 top-0">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" class="h-20 w-full">
                        <path fill="rgba(99, 102, 241, 0.2)" fill-opacity="0.5"
                            d="M0,128L48,122.7C96,117,192,107,288,133.3C384,160,480,224,576,245.3C672,267,768,245,864,213.3C960,181,1056,139,1152,133.3C1248,128,1344,160,1392,176L1440,192L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z">
                        </path>
                    </svg>
                </div>
            </div>

            <div class="animate-fadeIn mb-6 flex flex-wrap items-center justify-center gap-6">
                <div class="flex items-center transform transition-transform duration-300 hover:scale-105">
                    <div class="mr-3 flex h-10 w-10 items-center justify-center rounded-full bg-indigo-500 text-white
                                shadow-lg hover:shadow-indigo-500/50 transition-all duration-300">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z"
                                clip-rule="evenodd" />
                        </svg>
                    </div>
                    <span class="font-semibold text-gradient">Timu短链平台</span>
                </div>

                <div class="hidden md:flex md:space-x-4">
                    <a href="#" class="social-icon-hover">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                            <path
                                d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z" />
                        </svg>
                    </a>
    
                    <a href="#" class="social-icon-hover">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                            <path
                                d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
                        </svg>
                    </a>
                </div>
            </div>

            <div class="animate-bounce-slow mb-6 flex justify-center">
                <div class="h-px w-16 bg-gradient-to-r from-transparent via-indigo-300 to-transparent"></div>
            </div>

            <p class="animate-fadeIn animate-delay-100 text-sm text-gray-400 transition-all hover:text-gray-300">
                {{ copyright }} {{ currentYear }} {{ appName }} {{ rights }}
            </p>

            <div v-if="showLinks" class="animate-fadeIn animate-delay-200 mt-4 flex flex-wrap justify-center gap-4">
                <a v-for="(link, index) in links" :key="index" :href="link.url" class="footer-link"
                    :style="{ animationDelay: `${0.1 + index * 0.1}s` }">
                    {{ link.text }}
                </a>
            </div>

            <div class="animate-fadeIn animate-delay-300 mt-6 border-t border-gray-700 pt-6">
                <p class="text-xs text-gray-500 hover:text-gray-400 transition-colors">
                    本站点由 <span class="typing-animation-container"><span class="typing-animation">Tailwind
                            CSS</span></span> 强力驱动
                </p>
                <div class="mt-3 flex items-center justify-center space-x-2">
                    <span class="pulse-dot bg-green-500"></span>
                    <span class="text-xs text-gray-400">服务正常运行中</span>
                </div>
            </div>
        </div>
    </footer>
</template>

<script setup lang="ts">
import { computed } from 'vue';

// 组件属性设置
interface Props {
    appName?: string;
    copyright?: string;
    rights?: string;
    showLinks?: boolean;
    links?: Array<{ text: string; url: string }>;
}

withDefaults(defineProps<Props>(), {
    appName: 'Timu短链平台',
    copyright: 'Copyright ©',
    rights: 'All Rights Reserved',
    showLinks: false,
    links: () => []
});

// 计算当前年份
const currentYear = computed(() => new Date().getFullYear());
</script>

<style scoped>
/* 波浪动画 */
@keyframes wave {
    0% {
        transform: translateX(0) translateZ(0) scaleY(1);
    }

    50% {
        transform: translateX(-25%) translateZ(0) scaleY(0.8);
    }

    100% {
        transform: translateX(-50%) translateZ(0) scaleY(1);
    }
}

.wave-animation {
    animation: wave 15s cubic-bezier(0.36, 0.45, 0.63, 0.53) infinite;
    transform-origin: center bottom;
}

/* 文本渐变效果 */
.text-gradient {
    background: linear-gradient(90deg, #6366f1, #8b5cf6);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    animation: gradient-shift 8s ease infinite;
}

@keyframes gradient-shift {

    0%,
    100% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }
}

/* 慢速弹跳效果 */
@keyframes bounce-slow {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-6px);
    }
}

.animate-bounce-slow {
    animation: bounce-slow 3s ease-in-out infinite;
}

/* 社交图标悬停效果 */
.social-icon-hover {
    color: #9ca3af;
    /* text-gray-400 */
    position: relative;
    overflow: hidden;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    border-radius: 50%;
    transition: all 0.3s ease;
    /* duration-300 */
}

.social-icon-hover:hover {
    color: #ffffff;
    /* hover:text-white */
}

.social-icon-hover::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle, rgba(99, 102, 241, 0.3) 0%, rgba(99, 102, 241, 0) 70%);
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: -1;
    transform: scale(0.8);
}

.social-icon-hover:hover::before {
    opacity: 1;
    transform: scale(1.2);
}

.social-icon-hover:hover svg {
    transform: scale(1.15);
}

/* 打字机动画容器 */
.typing-animation-container {
    display: inline-block;
    max-width: fit-content;
    overflow: hidden;
}

/* 打字机动画 */
.typing-animation {
    color: #818cf8;
    /* text-indigo-400 */
    border-right: 2px solid rgba(99, 102, 241, 0.7);
    display: inline-block;
    white-space: nowrap;
    overflow: hidden;
    animation: typing 4s steps(20) infinite alternate, blink 0.7s step-end infinite;
}

@keyframes typing {
    from {
        width: 0;
    }

    to {
        width: 100%;
    }
}

@keyframes blink {
    50% {
        border-color: transparent;
    }
}

/* 脉冲点动画 */
.pulse-dot {
    display: inline-block;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    animation: pulse 2s infinite;
}

@keyframes pulse {
    0% {
        box-shadow: 0 0 0 0 rgba(16, 185, 129, 0.7);
    }

    70% {
        box-shadow: 0 0 0 6px rgba(16, 185, 129, 0);
    }

    100% {
        box-shadow: 0 0 0 0 rgba(16, 185, 129, 0);
    }
}

/* 页脚链接 */
.footer-link {
    color: #9ca3af;
    /* text-gray-400 */
    position: relative;
    overflow: hidden;
    animation: fadeInUp 0.6s ease-out forwards;
    opacity: 0;
    transition: all 0.3s;
    border-radius: 0.375rem;
    /* rounded-md */
    padding-left: 0.75rem;
    /* px-3 */
    padding-right: 0.75rem;
    padding-top: 0.25rem;
    /* py-1 */
    padding-bottom: 0.25rem;
}

.footer-link:hover {
    color: #a5b4fc;
    /* hover:text-indigo-300 */
    background-color: #374151;
    /* hover:bg-gray-700 */
}

.footer-link::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba(99, 102, 241, 0.6), transparent);
    transform: translateX(-100%);
    transition: transform 0.4s ease;
}

.footer-link:hover::after {
    transform: translateX(100%);
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(15px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 基本动画 */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-fadeIn {
    animation: fadeIn 0.6s ease-out forwards;
}

.animate-delay-100 {
    animation-delay: 0.1s;
}

.animate-delay-200 {
    animation-delay: 0.2s;
}

.animate-delay-300 {
    animation-delay: 0.3s;
}

/* 减少动画对有偏好的用户的影响 */
@media (prefers-reduced-motion: reduce) {

    .wave-animation,
    .animate-bounce-slow,
    .typing-animation,
    .pulse-dot,
    .footer-link::after,
    .footer-link {
        animation: none !important;
        transition: none !important;
    }
}
</style>