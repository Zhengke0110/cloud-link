<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
        <!-- 页面头部 -->
        <div class="max-w-7xl mx-auto">
            <div class="text-center mb-8 lg:mb-12">
                <!-- 标签 -->
                <div v-if="tag" class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-indigo-100 text-indigo-800 mb-4">
                    {{ tag }}
                </div>
                
                <!-- 标题 -->
                <h1 class="text-3xl md:text-4xl lg:text-5xl font-bold text-gray-900 mb-4">
                    {{ title }}
                </h1>
                
                <!-- 描述 -->
                <p v-if="description" class="text-lg md:text-xl text-gray-600 max-w-3xl mx-auto">
                    {{ description }}
                </p>
            </div>
            
            <!-- 页面内容 -->
            <div class="space-y-8 lg:space-y-12">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
interface Props {
    tag?: string;
    title: string;
    description?: string;
}

defineProps<Props>();
</script>