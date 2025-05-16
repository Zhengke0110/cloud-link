import { defineComponent, h } from "vue";

// 场景卡片数据
export const scenarioCards = [
    {
        title: "智能定向",
        description: "根据访问者设备、地区和时间，智能识别并将流量精准导向不同目标页面，实现千人千面的个性化体验。",
        features: [
            "移动端与桌面端智能适配不同页面",
            "实时地域识别，区域化投放精准营销",
            "自定义时间规则，控制链接生效周期"
        ],
        tags: ["智能路由", "多维定向", "规则引擎"],
        gradient: "from-blue-500 to-blue-600",
        color: "#2563eb",
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-7 w-7',
                    fill: 'none',
                    viewBox: '0 0 24 24',
                    stroke: 'currentColor'
                }, [
                    h('path', {
                        'stroke-linecap': 'round',
                        'stroke-linejoin': 'round',
                        'stroke-width': '2',
                        'd': 'M11 3.055A9.001 9.001 0 1020.945 13H11V3.055z'
                    }),
                    h('path', {
                        'stroke-linecap': 'round',
                        'stroke-linejoin': 'round',
                        'stroke-width': '2',
                        'd': 'M20.488 9H15V3.512A9.025 9.025 0 0120.488 9z'
                    })
                ]);
            }
        })
    },
    {
        title: "可视化数据分析",
        description: "多维度数据分析与直观可视化展示，助您洞悉用户行为、优化转化路径、提升营销效果。",
        features: [
            "全面的访问来源、地域和设备分析",
            "动态热力图展示用户点击行为偏好",
            "转化漏斗可视化，定位流失环节"
        ],
        tags: ["数据面板", "趋势图表", "转化分析"],
        gradient: "from-indigo-500 to-indigo-600",
        color: "#4f46e5",
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-7 w-7',
                    fill: 'none',
                    viewBox: '0 0 24 24',
                    stroke: 'currentColor'
                }, [
                    h('path', {
                        'stroke-linecap': 'round',
                        'stroke-linejoin': 'round',
                        'stroke-width': '2',
                        'd': 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z'
                    })
                ]);
            }
        })
    },
    {
        title: "高级防刷保护",
        description: "智能识别并过滤恶意访问、机器模拟点击等虚假流量，确保数据真实可靠，营销投放更具价值。",
        features: [
            "多层次IP频次限制与异常检测系统",
            "先进设备指纹识别，过滤模拟器访问",
            "实时行为监控与自动预警拦截机制"
        ],
        tags: ["访问验证", "安全防护", "数据净化"],
        gradient: "from-purple-500 to-purple-600",
        color: "#9333ea",
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-7 w-7',
                    fill: 'none',
                    viewBox: '0 0 24 24',
                    stroke: 'currentColor'
                }, [
                    h('path', {
                        'stroke-linecap': 'round',
                        'stroke-linejoin': 'round',
                        'stroke-width': '2',
                        'd': 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z'
                    })
                ]);
            }
        })
    }
];

// 业务场景数据 - 更新并添加了描述
export const businessScenarios = [
    {
        name: '电商营销',
        description: '增强转化率和客户体验',
        color: '#e11d48',
        gradient: 'from-pink-500 to-rose-600',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-5 w-5',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        d: 'M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z'
                    })
                ]);
            }
        })
    },
    {
        name: '社群运营',
        description: '提升用户参与和品牌忠诚度',
        color: '#2563eb',
        gradient: 'from-blue-500 to-cyan-500',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-5 w-5',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        d: 'M2 5a2 2 0 012-2h7a2 2 0 012 2v4a2 2 0 01-2 2H9l-3 3v-3H4a2 2 0 01-2-2V5z'
                    }),
                    h('path', {
                        d: 'M15 7v2a4 4 0 01-4 4H9.828l-1.766 1.767c.28.149.599.233.938.233h2l3 3v-3h2a2 2 0 002-2V9a2 2 0 00-2-2h-1z'
                    })
                ]);
            }
        })
    },
    {
        name: '内容推广',
        description: '扩大品牌影响力和内容触达',
        color: '#f59e0b',
        gradient: 'from-amber-500 to-orange-600',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-5 w-5',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        'fill-rule': 'evenodd',
                        d: 'M6 2a2 2 0 00-2 2v12a2 2 0 002 2h8a2 2 0 002-2V7.414A2 2 0 0015.414 6L12 2.586A2 2 0 0010.586 2H6zm5 6a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V8z',
                        'clip-rule': 'evenodd'
                    })
                ]);
            }
        })
    },
    {
        name: '渠道分析',
        description: '优化投放渠道和策略',
        color: '#10b981',
        gradient: 'from-green-500 to-emerald-600',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-5 w-5',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        d: 'M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z'
                    }),
                    h('path', {
                        d: 'M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z'
                    })
                ]);
            }
        })
    },
    {
        name: '线下引流',
        description: '连接线上线下业务，提升客户到店率',
        color: '#2563eb',
        gradient: 'from-indigo-500 to-blue-600',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-5 w-5',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        'fill-rule': 'evenodd',
                        d: 'M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z',
                        'clip-rule': 'evenodd'
                    })
                ]);
            }
        })
    }
];

// 价值收益数据
export const benefits = [
    {
        title: '提升转化率',
        description: '优质短链接能够提高用户点击意愿，直接提升转化率和ROI表现',
        stat: '42',
        unit: '%',
        color: 'blue',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-8 w-8',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        'fill-rule': 'evenodd',
                        'd': 'M12 7a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0V8.414l-4.293 4.293a1 1 0 01-1.414 0L8 10.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0L11 10.586 14.586 7H12z',
                        'clip-rule': 'evenodd'
                    })
                ]);
            }
        })
    },
    {
        title: '数据洞察',
        description: '精确追踪每次点击的来源、设备和地理位置，获得宝贵的营销洞察',
        stat: '360',
        unit: '°',
        color: 'indigo',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-8 w-8',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        'd': 'M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z'
                    }),
                    h('path', {
                        'd': 'M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z'
                    })
                ]);
            }
        })
    },
    {
        title: '提高信任度',
        description: '专业品牌化短链接提升信任度和品牌认知，降低访客犹豫',
        stat: '89',
        unit: '%',
        color: 'green',
        icon: defineComponent({
            render() {
                return h('svg', {
                    xmlns: 'http://www.w3.org/2000/svg',
                    class: 'h-8 w-8',
                    viewBox: '0 0 20 20',
                    fill: 'currentColor'
                }, [
                    h('path', {
                        'd': 'M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z'
                    })
                ]);
            }
        })
    }
];

// 客户案例数据
export const testimonials = [
    {
        name: '张志强',
        title: '电商平台运营总监',
        company: '某知名电商平台',
        image: 'https://api.dicebear.com/7.x/shapes/svg?seed=e-commerce',
        quote: '我们使用Timu短链接服务后，促销链接的点击率提高了36%，转化率增长显著，同时也优化了社交媒体分享体验。',
        rating: 5,
        clicksIncrease: 36,
        conversionIncrease: 28
    },
    {
        name: '李晓华',
        title: '数字营销经理',
        company: '某知名服饰品牌',
        image: 'https://api.dicebear.com/7.x/shapes/svg?seed=fashion',
        quote: '短链接帮助我们精准追踪各渠道营销效果，数据分析功能让我们能够快速调整策略，提升ROI达42%。',
        rating: 5,
        clicksIncrease: 42,
        conversionIncrease: 35
    },
    {
        name: '王建国',
        title: '社交媒体主管',
        company: '某知名媒体公司',
        image: 'https://api.dicebear.com/7.x/shapes/svg?seed=media',
        quote: '通过使用Timu短链接的A/B测试功能，我们优化了内容策略，提高了用户互动率，社交分享转化提升了31%。',
        rating: 4,
        clicksIncrease: 31,
        conversionIncrease: 26
    }
];
