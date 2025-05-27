<template>
    <PageLayout tag="数据分析" title="设备信息分析" description="分析访问用户的设备类型、操作系统和浏览器分布情况">

        <!-- 图表容器 -->
        <div class="grid grid-cols-1 gap-8 lg:grid-cols-3 mb-8">
            <!-- 操作系统分布 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-lg font-bold text-gray-900">操作系统</h3>
                    <div class="flex items-center space-x-1 text-sm text-gray-500">
                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                                clip-rule="evenodd" />
                        </svg>
                        <span>{{ osTotalPV }}次访问</span>
                    </div>
                </div>
                <div ref="osChartRef" class="w-full h-64"></div>
            </div>

            <!-- 设备类型分布 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-lg font-bold text-gray-900">设备类型</h3>
                    <div class="flex items-center space-x-1 text-sm text-gray-500">
                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M3 5a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2h-2.22l.123.489.804.804A1 1 0 0113 18H7a1 1 0 01-.707-1.707l.804-.804L7.22 15H5a2 2 0 01-2-2V5zm5.771 7H5V5h10v7H8.771z"
                                clip-rule="evenodd" />
                        </svg>
                        <span>{{ deviceTotalPV }}次访问</span>
                    </div>
                </div>
                <div ref="deviceChartRef" class="w-full h-64"></div>
            </div>

            <!-- 浏览器分布 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-lg font-bold text-gray-900">浏览器</h3>
                    <div class="flex items-center space-x-1 text-sm text-gray-500">
                        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M4.083 9h1.946c.089-1.546.383-2.97.837-4.118A6.004 6.004 0 004.083 9zM10 2a8 8 0 100 16 8 8 0 000-16zm0 2c-.076 0-.232.032-.465.262-.238.234-.497.623-.737 1.182-.389.907-.673 2.142-.766 3.556h3.936c-.093-1.414-.377-2.649-.766-3.556-.24-.559-.499-.948-.737-1.182C10.232 4.032 10.076 4 10 4zm3.971 5c-.089-1.546-.383-2.97-.837-4.118A6.004 6.004 0 0115.917 9h-1.946zm-2.003 2H8.032c.093 1.414.377 2.649.766 3.556.24.559.499.948.737 1.182.233.23.389.262.465.262.076 0 .232-.032.465-.262.238-.234.497-.623.737-1.182.389-.907.673-2.142.766-3.556zm1.166 4.118c.454-1.148.748-2.572.837-4.118h1.946a6.004 6.004 0 01-2.783 4.118zm-6.268 0C6.412 13.97 6.118 12.546 6.03 11H4.083a6.004 6.004 0 002.783 4.118z"
                                clip-rule="evenodd" />
                        </svg>
                        <span>{{ browserTotalPV }}次访问</span>
                    </div>
                </div>
                <div ref="browserChartRef" class="w-full h-64"></div>
            </div>
        </div>

        <!-- 详细统计表格 -->
        <div class="grid grid-cols-1 gap-8 lg:grid-cols-3">
            <!-- 操作系统统计 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <h4 class="text-lg font-bold text-gray-900 mb-4">操作系统详情</h4>
                <div class="space-y-3">
                    <div v-for="(item, index) in sortedOsData" :key="index"
                        class="flex items-center justify-between p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                        <div class="flex items-center space-x-3">
                            <div class="w-3 h-3 rounded-full" :style="{ backgroundColor: getOsColor(item.os) }"></div>
                            <span class="font-medium text-gray-900">{{ formatOsName(item.os) }}</span>
                        </div>
                        <div class="text-right">
                            <div class="text-sm font-semibold text-gray-900">{{ item.pvCount }}</div>
                            <div class="text-xs text-gray-500">{{ ((item.pvCount / osTotalPV) * 100).toFixed(1) }}%
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 设备类型统计 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <h4 class="text-lg font-bold text-gray-900 mb-4">设备类型详情</h4>
                <div class="space-y-3">
                    <div v-for="(item, index) in sortedDeviceData" :key="index"
                        class="flex items-center justify-between p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                        <div class="flex items-center space-x-3">
                            <div class="w-3 h-3 rounded-full"
                                :style="{ backgroundColor: getDeviceColor(item.deviceType) }"></div>
                            <span class="font-medium text-gray-900">{{ formatDeviceName(item.deviceType) }}</span>
                        </div>
                        <div class="text-right">
                            <div class="text-sm font-semibold text-gray-900">{{ item.pvCount }}</div>
                            <div class="text-xs text-gray-500">{{ ((item.pvCount / deviceTotalPV) * 100).toFixed(1) }}%
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 浏览器统计 -->
            <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6">
                <h4 class="text-lg font-bold text-gray-900 mb-4">浏览器详情</h4>
                <div class="space-y-3">
                    <div v-for="(item, index) in sortedBrowserData" :key="index"
                        class="flex items-center justify-between p-3 rounded-lg bg-gray-50 hover:bg-gray-100 transition-colors">
                        <div class="flex items-center space-x-3">
                            <div class="w-3 h-3 rounded-full"
                                :style="{ backgroundColor: getBrowserColor(item.browserName) }"></div>
                            <span class="font-medium text-gray-900">{{ formatBrowserName(item.browserName) }}</span>
                        </div>
                        <div class="text-right">
                            <div class="text-sm font-semibold text-gray-900">{{ item.pvCount }}</div>
                            <div class="text-xs text-gray-500">{{ ((item.pvCount / browserTotalPV) * 100).toFixed(1) }}%
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue';
import * as echarts from 'echarts';
import { deviceInfoData } from './config copy';
import PageLayout from '@/components/PageLayout.vue';

const osChartRef = ref<HTMLElement>();
const deviceChartRef = ref<HTMLElement>();
const browserChartRef = ref<HTMLElement>();
let osChart: echarts.ECharts | null = null;
let deviceChart: echarts.ECharts | null = null;
let browserChart: echarts.ECharts | null = null;

// 排序后的数据
const sortedOsData = computed(() => {
    return [...deviceInfoData.os].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

const sortedDeviceData = computed(() => {
    return [...deviceInfoData.device].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

const sortedBrowserData = computed(() => {
    return [...deviceInfoData.browser].sort((a, b) => (b.pvCount || 0) - (a.pvCount || 0));
});

// 总访问量
const osTotalPV = computed(() => {
    return deviceInfoData.os.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

const deviceTotalPV = computed(() => {
    return deviceInfoData.device.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

const browserTotalPV = computed(() => {
    return deviceInfoData.browser.reduce((acc, item) => acc + (item.pvCount || 0), 0);
});

// 格式化名称
function formatOsName(os: string | null): string {
    if (!os || os === 'Unknown') return '未知系统';
    return os;
}

function formatDeviceName(deviceType: string | null): string {
    if (!deviceType || deviceType === 'UNKNOWN') return '未知设备';
    if (deviceType === 'COMPUTER') return '电脑';
    if (deviceType === 'MOBILE') return '手机';
    if (deviceType === 'TABLET') return '平板';
    return deviceType;
}

function formatBrowserName(browserName: string | null): string {
    if (!browserName || browserName === 'Unknown') return '未知浏览器';
    return browserName;
}

// 获取颜色
function getOsColor(os: string | null): string {
    const colors = {
        'Windows': '#0078d4',
        'Mac OS X': '#000000',
        'Unknown': '#6b7280'
    };
    return colors[os as keyof typeof colors] || '#3b82f6';
}

function getDeviceColor(deviceType: string | null): string {
    const colors = {
        'COMPUTER': '#10b981',
        'MOBILE': '#f59e0b',
        'TABLET': '#8b5cf6',
        'UNKNOWN': '#6b7280'
    };
    return colors[deviceType as keyof typeof colors] || '#3b82f6';
}

function getBrowserColor(browserName: string | null): string {
    const colors = {
        'Chrome': '#4285f4',
        'Safari': '#ff6b00',
        'Firefox': '#ff7139',
        'Edge': '#0078d7',
        'Unknown': '#6b7280'
    };
    return colors[browserName as keyof typeof colors] || '#3b82f6';
}

// 初始化操作系统图表
function initOsChart() {
    if (!osChartRef.value) return;

    osChart = echarts.init(osChartRef.value);

    const pieData = sortedOsData.value.map(item => ({
        name: formatOsName(item.os),
        value: item.pvCount || 0,
        itemStyle: {
            color: getOsColor(item.os)
        }
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: { color: '#374151' },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        series: [{
            name: '操作系统',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: pieData,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
            label: {
                show: false
            },
            labelLine: {
                show: false
            }
        }]
    };

    osChart.setOption(option);
}

// 初始化设备类型图表
function initDeviceChart() {
    if (!deviceChartRef.value) return;

    deviceChart = echarts.init(deviceChartRef.value);

    const pieData = sortedDeviceData.value.map(item => ({
        name: formatDeviceName(item.deviceType),
        value: item.pvCount || 0,
        itemStyle: {
            color: getDeviceColor(item.deviceType)
        }
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: { color: '#374151' },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        series: [{
            name: '设备类型',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: pieData,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
            label: {
                show: false
            },
            labelLine: {
                show: false
            }
        }]
    };

    deviceChart.setOption(option);
}

// 初始化浏览器图表
function initBrowserChart() {
    if (!browserChartRef.value) return;

    browserChart = echarts.init(browserChartRef.value);

    const pieData = sortedBrowserData.value.map(item => ({
        name: formatBrowserName(item.browserName),
        value: item.pvCount || 0,
        itemStyle: {
            color: getBrowserColor(item.browserName)
        }
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: { color: '#374151' },
            extraCssText: 'box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); border-radius: 8px;'
        },
        series: [{
            name: '浏览器',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: pieData,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
            label: {
                show: false
            },
            labelLine: {
                show: false
            }
        }]
    };

    browserChart.setOption(option);
}

// 调整图表大小
function handleResize() {
    osChart?.resize();
    deviceChart?.resize();
    browserChart?.resize();
}

onMounted(() => {
    initOsChart();
    initDeviceChart();
    initBrowserChart();
    window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
    osChart?.dispose();
    deviceChart?.dispose();
    browserChart?.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>

<style scoped></style>