<template>
    <div class="rounded-xl bg-gradient-to-r from-blue-500 to-purple-600 p-6 text-white">
        <h3 class="mb-4 text-lg font-semibold">数据概览</h3>
        <div class="grid grid-cols-1 gap-4 md:grid-cols-4">
            <div class="text-center">
                <div class="text-2xl font-bold">{{ total }}</div>
                <div class="text-sm opacity-90">总访问量</div>
            </div>
            <div class="text-center">
                <div class="text-2xl font-bold">{{ uniqueDeviceCount }}</div>
                <div class="text-sm opacity-90">设备类型</div>
            </div>
            <div class="text-center">
                <div class="text-2xl font-bold">{{ uniqueProvinceCount }}</div>
                <div class="text-sm opacity-90">访问省份</div>
            </div>
            <div class="text-center">
                <div class="text-2xl font-bold">{{ uniqueBrowserCount }}</div>
                <div class="text-sm opacity-90">浏览器类型</div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
    total: number;
    data: Array<{
        deviceType: string;
        province: string;
        browserName: string;
        [key: string]: any;
    }>;
}

const props = defineProps<Props>();

const uniqueDeviceCount = computed(() => {
    const devices = new Set(props.data.map(item => item.deviceType));
    return devices.size;
});

const uniqueProvinceCount = computed(() => {
    const provinces = new Set(props.data.map(item => item.province));
    return provinces.size;
});

const uniqueBrowserCount = computed(() => {
    const browsers = new Set(props.data.map(item => item.browserName));
    return browsers.size;
});
</script>
