<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 to-white md:py-24 py-16">
        <!-- 背景装饰元素 -->
        <div class="absolute inset-0 z-0">
            <div class="absolute -top-24 -right-24 h-64 w-64 rounded-full bg-indigo-100/40 blur-3xl"></div>
            <div class="absolute -bottom-32 -left-32 h-80 w-80 rounded-full bg-blue-100/30 blur-3xl"></div>
            <div class="absolute top-1/3 left-1/4 h-48 w-48 rounded-full bg-purple-100/20 blur-3xl"></div>
            <div class="noise-pattern absolute inset-0 opacity-[0.03]"></div>
        </div>

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 标题区域 -->
            <div class="mb-12 md:mb-16 text-center reveal-element">
                <span
                    class="mb-3 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm">
                    会员方案选择
                </span>
                <h2
                    class="mb-6 bg-gradient-to-r from-indigo-600 to-blue-600 bg-clip-text text-2xl md:text-3xl lg:text-5xl font-bold text-transparent">
                    选择适合您的会员方案
                </h2>
                <p class="mx-auto max-w-2xl text-gray-600 text-sm md:text-base lg:text-lg">
                    升级会员享受更多短链接生成权限和高级功能
                </p>
                <div
                    class="mx-auto mt-6 h-1 w-24 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600">
                </div>
            </div>

            <!-- 会员方案卡片 - 移动端优化 -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
                <div v-for="plan in membershipPlans" :key="plan.id"
                    class="membership-card group relative overflow-hidden rounded-2xl border border-gray-100 bg-white p-0.5 shadow-lg transition-all duration-500 hover:-translate-y-2 hover:shadow-xl"
                    :class="[getPlanSpecialClass(plan), 'mb-6 md:mb-0']">

                    <!-- 渐变边框 - 更明显的区分 -->
                    <div class="absolute inset-0 rounded-2xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
                        :class="getBorderGradient(plan)">
                    </div>

                    <!-- 卡片内容 -->
                    <div class="relative bg-white rounded-2xl overflow-hidden">
                        <!-- 顶部标题栏 - 移动端优化 -->
                        <div class="p-4 md:p-6 pb-3 md:pb-4 text-center relative overflow-hidden"
                            :class="getHeaderBgClass(plan)">
                            <!-- 装饰圆圈 -->
                            <div class="absolute -top-10 -right-10 w-24 h-24 rounded-full bg-white/10"></div>
                            <div class="absolute -bottom-16 -left-8 w-32 h-32 rounded-full bg-white/5"></div>

                            <!-- 会员等级徽章 - 调整移动端大小 -->
                            <div class="mb-1">
                                <span class="inline-block px-2 md:px-3 py-0.5 md:py-1 rounded-full text-xs font-medium"
                                    :class="getLevelBadgeClass(plan)">
                                    {{ getLevelName(plan) }}
                                </span>
                            </div>
                            <h3 class="text-lg md:text-2xl font-bold text-white relative z-10">{{ plan.title }}</h3>
                            <div class="mt-3 md:mt-4 flex items-center justify-center">
                                <span class="line-through text-white/70 text-sm md:text-lg mr-1 md:mr-2">¥{{
                                    plan.oldAmount }}</span>
                                <span class="text-2xl md:text-4xl font-bold text-white">¥{{ plan.amount }}</span>
                                <span class="text-white/90 ml-1">/{{ getValidityPeriod(plan) }}</span>
                            </div>

                            <!-- 推荐标记 -->
                            <div v-if="plan.level === 'SECOND'" class="absolute top-2 right-2">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-amber-300"
                                    viewBox="0 0 20 20" fill="currentColor">
                                    <path
                                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                                </svg>
                            </div>
                        </div>

                        <!-- 特性列表 - 移动端紧凑显示 -->
                        <div class="p-4 md:p-6">
                            <ul class="space-y-2 md:space-y-4">
                                <li v-for="(feature, index) in getPlanFeatures(plan)" :key="index"
                                    class="flex items-start transition-all duration-300 hover:translate-x-1">
                                    <div class="flex h-5 md:h-6 w-5 md:w-6 flex-shrink-0 items-center justify-center rounded-full"
                                        :class="getFeatureIconBgClass(plan)">
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
                            </ul>
                        </div>

                        <!-- 特色标签 - 移动端优化 -->
                        <div class="px-4 md:px-6 pb-2">
                            <div class="flex flex-wrap gap-1 md:gap-2">
                                <span v-if="hasDetailFeature(plan, '自定义域名')"
                                    class="inline-flex items-center rounded-full border border-indigo-100 bg-indigo-50 px-2 py-0.5 text-xs font-medium text-indigo-800">
                                    <span class="mr-1 h-1 w-1 md:h-1.5 md:w-1.5 rounded-full bg-indigo-500"></span>
                                    自定义域名
                                </span>
                                <span v-if="hasDetailFeature(plan, '不限制')"
                                    class="inline-flex items-center rounded-full border border-blue-100 bg-blue-50 px-2 py-0.5 text-xs font-medium text-blue-800">
                                    <span class="mr-1 h-1 w-1 md:h-1.5 md:w-1.5 rounded-full bg-blue-500"></span>
                                    不限跳转
                                </span>
                                <span v-if="plan.amount === 0"
                                    class="inline-flex items-center rounded-full border border-amber-100 bg-amber-50 px-2 py-0.5 text-xs font-medium text-amber-800">
                                    <span class="mr-1 h-1 w-1 md:h-1.5 md:w-1.5 rounded-full bg-amber-500"></span>
                                    免费方案
                                </span>
                            </div>
                        </div>

                        <!-- 按钮区域 - 移动端优化 -->
                        <div class="p-4 md:p-6 pt-2 md:pt-4">
                            <button :class="getButtonClass(plan)"
                                class="w-full rounded-lg py-2 md:py-3 text-sm md:text-base font-medium transition-all duration-300">
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
                        </div>

                        <!-- 最受欢迎标签 - 调整移动端尺寸 -->
                        <div v-if="isPopularPlan(plan)"
                            class="absolute -right-11 md:-right-12 top-6 md:top-8 w-36 md:w-40 transform rotate-45 bg-gradient-to-r from-yellow-500 to-amber-600 py-0.5 md:py-1 text-center text-xs font-bold uppercase tracking-wider text-white shadow-md">
                            最受欢迎
                        </div>

                        <!-- 顶级方案标签 - 移动端适配 -->
                        <div v-if="plan.level === 'THIRD'"
                            class="absolute -left-3 top-8 md:top-10 px-3 md:px-4 py-0.5 md:py-1 bg-gradient-to-r from-indigo-600 to-blue-600 text-white text-xs font-bold rounded-r-lg shadow-lg transform -rotate-90 -translate-x-7 md:-translate-x-8">
                            顶级方案
                        </div>
                    </div>
                </div>
            </div>

            <!-- 会员优势说明 - 移动端优化 -->
            <div class="mt-12 md:mt-24 max-w-4xl mx-auto reveal-element delay-500">
                <div class="mb-6 md:mb-8 text-center">
                    <h3 class="text-lg md:text-2xl font-bold text-gray-900 mb-1 md:mb-2">会员专属优势</h3>
                    <p class="text-sm md:text-base text-gray-600">升级会员后您将获得以下优势</p>
                </div>

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 md:gap-6">
                    <!-- 优势卡片 - 移动端高度和间距调整 -->
                    <div v-if="hasDayTimesFeature"
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl">
                        <div
                            class="absolute inset-0 rounded-xl bg-gradient-to-br from-indigo-500 via-purple-500 to-blue-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                        </div>

                        <div
                            class="relative flex rounded-xl bg-white p-4 md:p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-indigo-50">
                            <div
                                class="feature-icon relative mr-3 md:mr-5 flex h-10 md:h-14 w-10 md:w-14 flex-shrink-0 items-center justify-center rounded-xl bg-indigo-100 text-indigo-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-indigo-600 group-hover:text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M13 10V3L4 14h7v7l9-11h-7z" />
                                </svg>
                            </div>

                            <div class="flex-1">
                                <h4
                                    class="text-base md:text-lg font-semibold text-gray-900 transition-all duration-500 group-hover:text-indigo-700">
                                    更高生成配额
                                </h4>
                                <p
                                    class="mt-1 md:mt-2 text-xs md:text-sm text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                                    会员每日可生成更多短链接，满足您的批量运营需求。黑金会员每日可生成{{ getMaxDayTimes() }}次，远超普通用户。
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- 其他优势卡片 - 同样适配移动端 -->
                    <div v-if="hasUnlimitedFeature"
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
                        <div
                            class="absolute inset-0 rounded-xl bg-gradient-to-br from-blue-500 via-cyan-500 to-teal-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                        </div>

                        <div
                            class="relative flex rounded-xl bg-white p-4 md:p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-blue-50">
                            <div
                                class="feature-icon relative mr-3 md:mr-5 flex h-10 md:h-14 w-10 md:w-14 flex-shrink-0 items-center justify-center rounded-xl bg-blue-100 text-blue-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-blue-600 group-hover:text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                            </div>

                            <div class="flex-1">
                                <h4
                                    class="text-base md:text-lg font-semibold text-gray-900 transition-all duration-500 group-hover:text-blue-700">
                                    无限制跳转
                                </h4>
                                <p
                                    class="mt-1 md:mt-2 text-xs md:text-sm text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                                    普通用户有跳转限制，会员用户享受无限制跳转次数，适合大型活动和持续性营销活动。
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- 其他优势卡片继续适配... -->
                    <div v-if="hasDataViewFeature"
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-purple-200 hover:shadow-xl">
                        <div
                            class="absolute inset-0 rounded-xl bg-gradient-to-br from-purple-500 via-indigo-500 to-blue-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                        </div>

                        <div
                            class="relative flex rounded-xl bg-white p-4 md:p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-purple-50">
                            <div
                                class="feature-icon relative mr-3 md:mr-5 flex h-10 md:h-14 w-10 md:w-14 flex-shrink-0 items-center justify-center rounded-xl bg-purple-100 text-purple-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-purple-600 group-hover:text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                </svg>
                            </div>

                            <div class="flex-1">
                                <h4
                                    class="text-base md:text-lg font-semibold text-gray-900 transition-all duration-500 group-hover:text-purple-700">
                                    详细数据分析
                                </h4>
                                <p
                                    class="mt-1 md:mt-2 text-xs md:text-sm text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                                    所有方案均支持数据查看功能，帮助您了解短链接的使用情况和效果。
                                </p>
                            </div>
                        </div>
                    </div>

                    <div v-if="hasCustomDomainFeature"
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-green-200 hover:shadow-xl">
                        <div
                            class="absolute inset-0 rounded-xl bg-gradient-to-br from-green-500 via-teal-500 to-cyan-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                        </div>

                        <div
                            class="relative flex rounded-xl bg-white p-4 md:p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-green-50">
                            <div
                                class="feature-icon relative mr-3 md:mr-5 flex h-10 md:h-14 w-10 md:w-14 flex-shrink-0 items-center justify-center rounded-xl bg-green-100 text-green-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-green-600 group-hover:text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 md:h-6 w-5 md:w-6" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c-1.657 0-3-4.03-3-9s1.343-9 3-9m0 18c1.657 0 3-4.03 3-9s-1.343-9-3-9" />
                                </svg>
                            </div>

                            <div class="flex-1">
                                <h4
                                    class="text-base md:text-lg font-semibold text-gray-900 transition-all duration-500 group-hover:text-green-700">
                                    自定义域名
                                </h4>
                                <p
                                    class="mt-1 md:mt-2 text-xs md:text-sm text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                                    黑金会员专享功能，可使用自定义域名生成短链接，提高品牌辨识度和专业形象。
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 常见问题 - 移动端优化 -->
            <div class="mt-12 md:mt-24 max-w-3xl mx-auto reveal-element delay-700">
                <div class="mb-5 md:mb-8 text-center">
                    <h3 class="text-lg md:text-2xl font-bold text-gray-900 mb-1 md:mb-2">常见问题</h3>
                    <p class="text-sm md:text-base text-gray-600">关于会员方案的常见问题解答</p>
                </div>

                <div class="space-y-3 md:space-y-4">
                    <div
                        class="rounded-xl border border-gray-100 bg-white p-4 md:p-6 shadow-sm hover:shadow-md transition-shadow duration-300">
                        <h4 class="text-base md:text-lg font-semibold text-gray-900 mb-1 md:mb-2">如何升级会员？</h4>
                        <p class="text-xs md:text-sm text-gray-600">选择您心仪的会员方案，点击"立即购买"按钮，按照系统提示完成支付即可立即升级。</p>
                    </div>

                    <div
                        class="rounded-xl border border-gray-100 bg-white p-4 md:p-6 shadow-sm hover:shadow-md transition-shadow duration-300">
                        <h4 class="text-base md:text-lg font-semibold text-gray-900 mb-1 md:mb-2">会员到期后会怎样？</h4>
                        <p class="text-xs md:text-sm text-gray-600">会员到期后，您的账户将自动降级为免费用户，已创建的短链接仍然有效，但会受到免费用户的使用限制。</p>
                    </div>

                    <div
                        class="rounded-xl border border-gray-100 bg-white p-4 md:p-6 shadow-sm hover:shadow-md transition-shadow duration-300">
                        <h4 class="text-base md:text-lg font-semibold text-gray-900 mb-1 md:mb-2">会员有效期是多久？</h4>
                        <p class="text-xs md:text-sm text-gray-600">我们提供多种会员时长选择，目前黄金会员和黑金会员的有效期为{{ getMaxValidDay()
                            }}天，具体以您选择的方案为准。</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { Data } from './config';

// 会员方案数据
const membershipPlans = ref(Data);

// 根据数据计算的特性存在标志
const hasDayTimesFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.dayTimes > 0);
});

const hasUnlimitedFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('不限制'));
});

const hasDataViewFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('数据查看'));
});

const hasCustomDomainFeature = computed(() => {
    return membershipPlans.value.some(plan => plan.detail.includes('自定义域名'));
});

// 获取会员等级名称
const getLevelName = (plan: any) => {
    const levelMap = {
        'FIRST': '青铜会员',
        'SECOND': '黄金会员',
        'THIRD': '黑金会员'
    };
    return levelMap[plan.level as keyof typeof levelMap];
};

// 获取会员有效期
const getValidityPeriod = (plan: any) => {
    return plan.validDay === 1 ? '天' : '月';
};

// 解析会员特性
const getPlanFeatures = (plan: any) => {
    let details = plan.detail.split('||');

    // 替换占位符
    details = details.map((item: string) => {
        return item.replace(/\{\{dayTimes\}\}/, plan.dayTimes);
    });

    return details;
};

// 检查特定功能是否存在
const hasDetailFeature = (plan: any, featureKeyword: string) => {
    return plan.detail.includes(featureKeyword);
};

// 获取会员方案中最大的每日生成次数
const getMaxDayTimes = () => {
    return Math.max(...membershipPlans.value.map(plan => plan.dayTimes || 0));
};

// 获取最长有效期
const getMaxValidDay = () => {
    return Math.max(...membershipPlans.value.map(plan => plan.validDay || 0));
};

// 判断是否是最受欢迎的方案
const isPopularPlan = (plan: any) => {
    // 假设第二个方案(黄金会员)是最受欢迎的
    return plan.level === 'SECOND';
};

// 获取特殊卡片类名 - 根据级别
const getPlanSpecialClass = (plan: any) => {
    switch (plan.level) {
        case 'SECOND':
            return 'md:scale-105 md:-translate-y-2 z-10';
        case 'THIRD':
            return 'md:bg-gradient-to-b md:from-white md:to-indigo-50/20';
        default:
            return '';
    }
};

// 获取卡片边框渐变色
const getBorderGradient = (plan: any) => {
    switch (plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-br from-amber-400 via-yellow-400 to-amber-600';
        case 'SECOND':
            return 'bg-gradient-to-br from-yellow-400 via-amber-500 to-orange-500';
        case 'THIRD':
            return 'bg-gradient-to-br from-blue-600 via-indigo-600 to-purple-600';
        default:
            return '';
    }
};

// 获取卡片标题栏背景色
const getHeaderBgClass = (plan: any) => {
    switch (plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-r from-amber-400 to-amber-600';
        case 'SECOND':
            return 'bg-gradient-to-r from-yellow-500 to-amber-600';
        case 'THIRD':
            return 'bg-gradient-to-r from-indigo-600 via-blue-700 to-blue-600';
        default:
            return '';
    }
};

// 获取会员等级徽章样式
const getLevelBadgeClass = (plan: any) => {
    switch (plan.level) {
        case 'FIRST':
            return 'bg-white/20 text-white';
        case 'SECOND':
            return 'bg-yellow-300 text-amber-800';
        case 'THIRD':
            return 'bg-gradient-to-r from-blue-400 to-indigo-400 text-white shadow-md';
        default:
            return '';
    }
};

// 获取特性图标背景色
const getFeatureIconBgClass = (plan: any) => {
    switch (plan.level) {
        case 'FIRST':
            return 'bg-amber-500';
        case 'SECOND':
            return 'bg-yellow-500';
        case 'THIRD':
            return 'bg-indigo-600';
        default:
            return 'bg-blue-500';
    }
};

// 获取按钮样式
const getButtonClass = (plan: any) => {
    switch (plan.level) {
        case 'FIRST':
            return 'bg-gradient-to-r from-amber-400 to-amber-600 text-white hover:shadow-lg hover:shadow-amber-200/50 active:scale-95';
        case 'SECOND':
            return 'bg-gradient-to-r from-yellow-500 to-amber-600 text-white shadow-lg hover:shadow-amber-300/50 hover:-translate-y-0.5 active:scale-95';
        case 'THIRD':
            return 'bg-gradient-to-r from-indigo-600 to-blue-600 text-white shadow-lg hover:shadow-indigo-300/50 hover:-translate-y-0.5 active:scale-95';
        default:
            return 'bg-gray-200 text-gray-600';
    }
};

// 添加页面动画效果
onMounted(() => {
    // 如果浏览器支持，添加页面加载动画
    const revealElements = document.querySelectorAll('.reveal-element');

    if ('IntersectionObserver' in window) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('revealed');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.1
        });

        revealElements.forEach(el => {
            observer.observe(el);
        });
    } else {
        // 对于不支持 IntersectionObserver 的浏览器，直接显示元素
        revealElements.forEach(el => {
            el.classList.add('revealed');
        });
    }
});
</script>

<style scoped>
/* 背景噪点图案 */
.noise-pattern {
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='1'/%3E%3C/svg%3E");
}

/* 卡片效果 */
.feature-card-3d {
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.feature-card-3d:hover {
    box-shadow: 0 12px 20px -3px rgba(0, 0, 0, 0.07), 0 6px 8px -2px rgba(0, 0, 0, 0.04);
}

/* 元素显示动画 */
.reveal-element {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-element.delay-500 {
    transition-delay: 0.5s;
}

.reveal-element.delay-700 {
    transition-delay: 0.7s;
}

.reveal-element.revealed {
    opacity: 1;
    transform: translateY(0);
}

/* 交错动画效果 */
.feature-card-3d:nth-child(1) {
    transition-delay: 0.1s;
}

.feature-card-3d:nth-child(2) {
    transition-delay: 0.25s;
}

.feature-card-3d:nth-child(3) {
    transition-delay: 0.4s;
}

.feature-card-3d:nth-child(4) {
    transition-delay: 0.55s;
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {

    .feature-card-3d,
    .reveal-element {
        animation: none !important;
        transition: none !important;
        transform: none !important;
        opacity: 1 !important;
    }
}

/* 响应式调整 */
@media (max-width: 768px) {
    .membership-card {
        max-width: 100%;
        margin-left: auto;
        margin-right: auto;
    }

    /* 在移动端为会员卡片添加底部间距 */
    .membership-card:not(:last-child) {
        margin-bottom: 2rem;
    }

    /* 移动端卡片阴影优化 */
    .membership-card {
        box-shadow: 0 4px 10px -2px rgba(0, 0, 0, 0.1);
    }

    /* 移动端特色标签布局优化 */
    .feature-card-3d {
        padding: 0.375rem;
    }

    /* 移动端文字尺寸调整 */
    h2 {
        font-size: 1.75rem;
        line-height: 1.3;
    }

    h3 {
        font-size: 1.25rem;
    }

    /* 移动端触摸交互优化 */
    .feature-item:active,
    .btn-primary:active,
    .btn-secondary:active {
        transform: scale(0.98);
    }
}

/* 超小屏幕下的额外优化 */
@media (max-width: 360px) {
    .membership-card {
        padding: 0.25rem;
    }

    /* 调整按钮和间距 */
    .stats-card,
    .info-card {
        padding: 1rem;
    }
}

/* 改进悬浮状态在移动触摸设备上的表现 */
@media (hover: none) {
    .hover:-translate-y-2 {
        transform: none;
    }

    .group-hover\:opacity-100 {
        opacity: 1;
    }

    .membership-card:active {
        transform: translateY(-4px);
    }
}
</style>