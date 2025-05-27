<template>
    <ChartCard
        title="地域分布"
        chart-id="regionChart"
        chart-class="h-96 w-full"
        icon-class="text-indigo-600"
        icon-bg-class="bg-indigo-100"
        icon-path="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z M15 11a3 3 0 11-6 0 3 3 0 016 0z"
    />
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import ChartCard from '../ChartCard.vue';

interface Props {
    data: Array<{
        province: string;
        [key: string]: any;
    }>;
}

const props = defineProps<Props>();

let chart: echarts.ECharts | null = null;

onMounted(async () => {
    await loadChinaMap();
    initChart();
});

onUnmounted(() => {
    if (chart) {
        chart.dispose();
        chart = null;
    }
});

async function loadChinaMap() {
    try {
        const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json');
        const chinaJson = await response.json();
        echarts.registerMap('china', chinaJson);
    } catch (error) {
        console.error('加载地图数据失败:', error);
    }
}

function convertToMapName(province: string): string {
    const provinceToMapName: Record<string, string> = {
        '北京市': '北京市', '天津市': '天津市', '上海市': '上海市', '重庆市': '重庆市',
        '河北省': '河北省', '山西省': '山西省', '内蒙古自治区': '内蒙古自治区',
        '辽宁省': '辽宁省', '吉林省': '吉林省', '黑龙江省': '黑龙江省',
        '江苏省': '江苏省', '浙江省': '浙江省', '安徽省': '安徽省', '福建省': '福建省',
        '江西省': '江西省', '山东省': '山东省', '河南省': '河南省', '湖北省': '湖北省',
        '湖南省': '湖南省', '广东省': '广东省', '广西壮族自治区': '广西壮族自治区',
        '海南省': '海南省', '四川省': '四川省', '贵州省': '贵州省', '云南省': '云南省',
        '西藏自治区': '西藏自治区', '陕西省': '陕西省', '甘肃省': '甘肃省',
        '青海省': '青海省', '宁夏回族自治区': '宁夏回族自治区',
        '新疆维吾尔自治区': '新疆维吾尔自治区', '台湾省': '台湾省',
        '香港特别行政区': '香港特别行政区', '澳门特别行政区': '澳门特别行政区'
    };

    if (provinceToMapName[province]) {
        return provinceToMapName[province];
    }

    const simpleToFullName: Record<string, string> = {
        '北京': '北京市', '天津': '天津市', '上海': '上海市', '重庆': '重庆市',
        '河北': '河北省', '山西': '山西省', '内蒙古': '内蒙古自治区',
        '辽宁': '辽宁省', '吉林': '吉林省', '黑龙江': '黑龙江省',
        '江苏': '江苏省', '浙江': '浙江省', '安徽': '安徽省', '福建': '福建省',
        '江西': '江西省', '山东': '山东省', '河南': '河南省', '湖北': '湖北省',
        '湖南': '湖南省', '广东': '广东省', '广西': '广西壮族自治区',
        '海南': '海南省', '四川': '四川省', '贵州': '贵州省', '云南': '云南省',
        '西藏': '西藏自治区', '陕西': '陕西省', '甘肃': '甘肃省',
        '青海': '青海省', '宁夏': '宁夏回族自治区', '新疆': '新疆维吾尔自治区',
        '台湾': '台湾省', '香港': '香港特别行政区', '澳门': '澳门特别行政区'
    };

    if (simpleToFullName[province]) {
        return simpleToFullName[province];
    }

    return 'Unknown';
}

function initChart() {
    const chartDom = document.getElementById('regionChart');
    if (!chartDom) return;

    chart = echarts.init(chartDom);

    // 统计省份数据
    const provinceCount: Record<string, number> = {};
    props.data.forEach(item => {
        let province = item.province || 'Unknown';
        province = convertToMapName(province);

        if (province !== 'Unknown') {
            provinceCount[province] = (provinceCount[province] || 0) + 1;
        }
    });

    const mapData = Object.entries(provinceCount).map(([name, value]) => ({
        name,
        value
    }));

    const maxValue = Math.max(...Object.values(provinceCount), 1);

    const option = {
        tooltip: {
            trigger: 'item',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            borderColor: '#e5e7eb',
            borderWidth: 1,
            textStyle: {
                color: '#374151'
            },
            formatter: function (params: any) {
                if (params.data && params.data.value !== undefined) {
                    return `${params.name}: ${params.data.value} 次访问`;
                }
                return `${params.name}: 0 次访问`;
            }
        },
        visualMap: {
            min: 0,
            max: maxValue,
            left: 'left',
            top: 'bottom',
            text: ['高', '低'],
            calculable: true,
            inRange: {
                color: ['#ddd6fe', '#6366f1']
            }
        },
        series: [
            {
                name: '访问量',
                type: 'map',
                map: 'china',
                roam: false,
                zoom: 1.5,
                center: [104, 35],
                layoutCenter: ['50%', '50%'],
                layoutSize: '90%',
                itemStyle: {
                    areaColor: '#f3f4f6',
                    borderColor: '#fff',
                    borderWidth: 1
                },
                emphasis: {
                    itemStyle: {
                        areaColor: '#fbbf24',
                        shadowOffsetX: 0,
                        shadowOffsetY: 0,
                        shadowBlur: 20,
                        borderWidth: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.3)'
                    }
                },
                label: {
                    show: false,
                    fontSize: 10
                },
                data: mapData
            }
        ]
    };

    chart.setOption(option);
}

defineExpose({
    resize: () => chart?.resize()
});
</script>
