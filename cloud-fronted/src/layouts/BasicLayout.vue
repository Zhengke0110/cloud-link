<template>
    <div class="min-h-screen flex flex-col bg-gradient-to-br from-blue-50 to-indigo-100">
        <!-- 全局头部导航 -->
        <Header />

        <!-- 主要内容区域 -->
        <main class="flex-grow">
            <!-- 使用RouterView渲染匹配的路由组件 -->
            <RouterView />
        </main>

        <!-- 全局页脚 -->
        <Fooder :showLinks="true" :links="footerLinks" />
    </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router';
import Header from "@/components/Header/Header.vue";
import Fooder from "@/components/Fooder/Fooder.vue";

// 页脚链接配置
const footerLinks = [
    { text: '关于我们', url: '#about' },
    { text: '使用条款', url: '#terms' },
    { text: '隐私政策', url: '#privacy' },
    { text: '联系我们', url: '#contact' }
];
</script>

<style scoped>
/* 基本布局样式 */
.min-h-screen {
    min-height: 100vh;
}

.flex-grow {
    flex-grow: 1;
}
</style>