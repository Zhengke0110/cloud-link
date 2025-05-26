<template>
    <header>
        <nav ref="navRef" :class="[
            'fixed w-full z-50 bg-gray-800/95 backdrop-blur-sm text-white transition-all duration-300',
            { 'shadow-lg shadow-gray-900/10': !isAtTop || isMobileMenuOpen },
            { 'translate-y-0': !isScrollingDown || isAtTop || isMobileMenuOpen },
            { '-translate-y-full': isScrollingDown && !isAtTop && !isMobileMenuOpen }
        ]">
            <div class="container mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex h-16 items-center justify-between">
                    <!-- Logo和品牌名称 -->
                    <div class="flex items-center">
                        <router-link to="/" class="flex items-center gap-2 group">
                            <div class="flex h-9 w-9 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-blue-600 
                         text-white shadow-lg shadow-indigo-600/20 transition-all duration-300 
                         group-hover:shadow-indigo-500/40">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z" />
                                </svg>
                            </div>
                            <span
                                class="text-xl font-bold tracking-tight text-white transition-all duration-300 group-hover:text-indigo-200">
                                Timu<span class="text-indigo-300">短链</span>
                            </span>
                        </router-link>

                        <!-- 桌面端导航菜单 -->
                        <div class="hidden md:ml-10 md:block">
                            <div class="flex space-x-2">
                                <router-link v-for="(item, index) in navItems" :key="index" :to="item.path" custom
                                    v-slot="{ href, navigate, isActive }">
                                    <a :href="href" @click="navigate" :class="[
                                        'px-3 py-2 text-sm font-medium rounded-md transition-all duration-200',
                                        isActive
                                            ? 'bg-gray-700 text-white'
                                            : 'text-gray-200 hover:bg-gray-700/70 hover:text-white'
                                    ]">
                                        <span class="flex items-center gap-1.5">
                                            <component :is="item.icon" class="h-4 w-4" />
                                            {{ item.name }}
                                        </span>
                                    </a>
                                </router-link>
                            </div>
                        </div>
                    </div>

                    <!-- 右侧用户操作区 -->
                    <div class="flex items-center gap-2">
                        <!-- 登录/注册按钮 -->
                        <button v-if="!isLoggedIn" @click="goToLogin" class="hidden rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-2 text-sm font-semibold 
                           text-white shadow-md transition-all duration-200 hover:from-indigo-500 hover:to-blue-500 
                           hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 
                           focus:ring-offset-2 md:block">
                            登录 / 注册
                        </button>

                        <!-- 用户头像下拉菜单 -->
                        <div v-else class="relative hidden md:block">
                            <button @click="isUserMenuOpen = !isUserMenuOpen"
                                class="flex items-center gap-2 rounded-full bg-gray-700 px-3 py-2 text-sm text-white transition-all duration-200 hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-800">
                                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" alt="用户头像"
                                    class="h-6 w-6 rounded-full bg-white" />
                                <span class="font-medium text-white">用户中心</span>
                                <svg xmlns="http://www.w3.org/2000/svg"
                                    class="h-4 w-4 transition-transform duration-200 text-gray-300"
                                    :class="{ 'rotate-180': isUserMenuOpen }" viewBox="0 0 20 20" fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                        clip-rule="evenodd" />
                                </svg>
                            </button>

                            <!-- 下拉菜单 -->
                            <gsap-animation 
                                v-if="isUserMenuOpen" 
                                animation="fadeInDown" 
                                :duration="0.2"
                                class="absolute right-0 mt-2 w-48 origin-top-right">
                                <div class="rounded-lg bg-white py-2 shadow-xl ring-1 ring-black ring-opacity-5 backdrop-blur-sm border border-gray-200">
                                    <router-link to="/center" custom v-slot="{ href, navigate }">
                                        <a :href="href" @click="navigate"
                                            class="flex items-center gap-3 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 hover:text-gray-900 transition-colors duration-150">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-500" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            <span class=" text-black">个人中心</span>
                                        </a>
                                    </router-link>
                                    
                                    <!-- 分割线 -->
                                    <div class="my-1 border-t border-gray-100"></div>
                                    
                                    <button @click="logout"
                                        class="flex items-center gap-3 w-full px-4 py-2 text-left text-sm font-medium text-red-600 hover:bg-red-50 hover:text-red-700 transition-colors duration-150">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-red-500" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path fill-rule="evenodd"
                                                d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z"
                                                clip-rule="evenodd" />
                                        </svg>
                                        <span>退出登录</span>
                                    </button>
                                </div>
                            </gsap-animation>
                        </div>

                        <!-- 移动端菜单按钮 -->
                        <button @click="isMobileMenuOpen = !isMobileMenuOpen" class="inline-flex items-center justify-center rounded-md p-2 text-gray-400 transition-colors 
                           hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset 
                           focus:ring-white md:hidden">
                            <span class="sr-only">{{ isMobileMenuOpen ? '关闭菜单' : '打开菜单' }}</span>
                            <svg v-if="!isMobileMenuOpen" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 6h16M4 12h16M4 18h16" />
                            </svg>
                            <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>

                <!-- 移动端菜单面板 -->
                <Transition enter-active-class="transition duration-200 ease-out"
                    enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100"
                    leave-active-class="transition duration-100 ease-in"
                    leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
                    <div v-if="isMobileMenuOpen" class="md:hidden">
                        <div class="space-y-1 px-2 pt-2 pb-3">
                            <router-link v-for="item in navItems" :key="item.name" :to="item.path" custom
                                v-slot="{ href, navigate, isActive }">
                                <a :href="href" @click="navigate" :class="[
                                    'block rounded-md px-3 py-2 text-base font-medium',
                                    isActive
                                        ? 'bg-gray-700 text-white'
                                        : 'text-gray-300 hover:bg-gray-700 hover:text-white'
                                ]">
                                    <span class="flex items-center gap-2">
                                        <component :is="item.icon" class="h-5 w-5" />
                                        {{ item.name }}
                                    </span>
                                </a>
                            </router-link>
                        </div>

                        <!-- 移动端的用户信息和操作 -->
                        <div class="border-t border-gray-700 pt-4 pb-3">
                            <div v-if="isLoggedIn" class="flex items-center px-5">
                                <div class="flex-shrink-0">
                                    <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" alt="用户头像"
                                        class="h-10 w-10 rounded-full" />
                                </div>
                                <div class="ml-3">
                                    <div class="text-base font-medium text-white">用户名</div>
                                    <div class="text-sm font-medium text-gray-400">user@example.com</div>
                                </div>
                                <button v-if="hasNotifications"
                                    class="relative ml-auto flex-shrink-0 rounded-full p-1 text-gray-400 hover:text-white">
                                    <span class="sr-only">查看通知</span>
                                    <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                                    </svg>
                                    <span class="absolute -top-1 -right-1 h-2.5 w-2.5 rounded-full bg-red-500"></span>
                                </button>
                            </div>

                            <div class="mt-3 space-y-1 px-2">
                                <div v-if="isLoggedIn">
                                    <router-link to="/center" custom v-slot="{ href, navigate }">
                                        <a :href="href" @click="navigate"
                                            class="block rounded-md px-3 py-2 text-base font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition-colors duration-200">
                                            个人中心
                                        </a>
                                    </router-link>
                                    <button @click="logout"
                                        class="mt-1 block w-full rounded-md px-3 py-2 text-left text-base font-medium text-red-400 hover:bg-gray-700 hover:text-red-300 transition-colors duration-200">
                                        退出登录
                                    </button>
                                </div>
                                <div v-else class="px-1">
                                    <button @click="goToLogin" class="w-full rounded-md bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-2 text-white shadow-md
                                 hover:from-indigo-500 hover:to-blue-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 
                                 focus:ring-offset-2 transition-all duration-200">
                                        登录 / 注册
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </Transition>
            </div>
        </nav>

        <!-- 占位元素，确保内容不会被固定导航栏遮挡 -->
        <div class="h-16 w-full"></div>
    </header>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted, computed } from 'vue';
import { useWindowScroll, useToggle, useEventListener } from '@vueuse/core';
import router from '@/router';
import { LayoutMenu } from '@/config';
import GsapAnimation from '@/components/GsapAnimation.vue';

// 图标组件定义
const HomeIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
             </svg>`
};

const LinkIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path fill-rule="evenodd" d="M12.586 4.586a2 2 0 112.828 2.828l-3 3a2 2 0 01-2.828 0 1 1 0 00-1.414 1.414 4 4 0 005.656 0l3-3a4 4 0 00-5.656-5.656l-1.5 1.5a1 1 0 101.414 1.414l1.5-1.5zm-5 5a2 2 0 012.828 0 1 1 0 101.414-1.414 4 4 0 00-5.656 0l-3 3a4 4 0 105.656 5.656l1.5-1.5a1 1 0 10-1.414-1.414l-1.5 1.5a2 2 0 11-2.828-2.828l3-3z" clip-rule="evenodd" />
             </svg>`
};

const ChartIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z" />
             </svg>`
};

const GearIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
             </svg>`
};

const UserIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
             </svg>`
};

const TemplateIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" />
             </svg>`
};

const FolderIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path d="M2 6a2 2 0 012-2h5l2 2h5a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z" />
             </svg>`
};

const GlobeIcon = {
    template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
               <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM4.332 8.027a6.012 6.012 0 011.912-2.706C6.512 5.73 6.974 6 7.5 6A1.5 1.5 0 019 7.5V8a2 2 0 004 0 2 2 0 011.523-1.943A5.977 5.977 0 0116 10c0 .34-.028.675-.083 1H15a2 2 0 00-2 2v2.197A5.973 5.973 0 0110 16v-2a2 2 0 00-2-2 2 2 0 01-2-2 2 2 0 00-1.668-1.973z" clip-rule="evenodd" />
             </svg>`
};

// 路由名称到图标的映射
const iconMap: Record<string, any> = {
    home: HomeIcon,
    scheme: TemplateIcon,
    create: LinkIcon,
    links: ChartIcon,
    grouping: FolderIcon,
    domain: GlobeIcon,
};

// 路由名称到中文名称的映射
const nameMap: Record<string, string> = {
    home: '首页',
    scheme: '方案模板',
    create: '创建短链',
    links: '链接管理',
    grouping: '分组管理',
    domain: '域名管理',
};

// 从router配置中获取导航项
const navItems = computed(() => {
    return router.getRoutes()
        .filter(route =>
            route.meta?.layout === LayoutMenu.BasicLayout &&
            route.name &&
            route.path !== '/' &&
            nameMap[route.name as string]
        )
        .map(route => ({
            name: nameMap[route.name as string] || route.name,
            path: route.path,
            icon: iconMap[route.name as string] || LinkIcon
        }));
});

// 用户菜单项
const userMenuItems = [
    { name: '个人中心', path: '/center' },
];

// 模拟登录状态和通知状态
const isLoggedIn = computed(() => {
    return !!localStorage.getItem('userToken');
});
const hasNotifications = ref(true);

// 移动端菜单状态
const isMobileMenuOpen = ref(false);
const isUserMenuOpen = ref(false);

// 滚动相关状态
const { y: scrollY } = useWindowScroll();
const navRef = ref(null);
const lastScrollTop = ref(0);
const isScrollingDown = ref(false);
const isAtTop = ref(true);

// 监听滚动变化
watch(scrollY, (newY) => {
    // 检测滚动方向
    isScrollingDown.value = newY > lastScrollTop.value && newY > 60;
    isAtTop.value = newY <= 10;
    lastScrollTop.value = newY <= 0 ? 0 : newY;
});

// 点击页面其他区域关闭用户下拉菜单
useEventListener(document, 'click', (event) => {
    const target = event.target as HTMLElement;
    if (isUserMenuOpen.value && !target.closest('.relative')) {
        isUserMenuOpen.value = false;
    }
});

// 按Escape键关闭移动端菜单
useEventListener(document, 'keydown', (e) => {
    if (e.key === 'Escape') {
        if (isMobileMenuOpen.value) isMobileMenuOpen.value = false;
        if (isUserMenuOpen.value) isUserMenuOpen.value = false;
    }
});

// 当窗口尺寸变化到桌面视图时自动关闭移动菜单
const handleResize = () => {
    if (window.innerWidth >= 768 && isMobileMenuOpen.value) {
        isMobileMenuOpen.value = false;
    }
};

// 模拟退出登录功能
const logout = async () => {
    try {
        // 调用退出登录API
        const { AccountLogoutApi } = await import('@/services/account');
        await AccountLogoutApi();
        isUserMenuOpen.value = false;
        // 跳转到首页
        router.push('/home');
    } catch (error) {
        console.error('退出登录失败:', error);
        // 即使API失败也要清除本地数据
        localStorage.removeItem('userToken');
        localStorage.removeItem('rememberedUser');
        isUserMenuOpen.value = false;
        router.push('/home');
    }
};

// 跳转到登录页面
const goToLogin = () => {
    router.push('/account/login');
};

// 组件生命周期处理
onMounted(() => {
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
/* 透明度过渡效果 */
.bg-gray-800\/95 {
    background-color: rgba(31, 41, 55, 0.95);
}

/* 动画效果 */
.transition-transform {
    transition-property: transform;
    transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
    transition-duration: 300ms;
}

/* 滚动隐藏效果 */
.-translate-y-full {
    transform: translateY(-100%);
}
</style>
