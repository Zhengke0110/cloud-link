<template>
  <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 to-white py-24">
    <!-- 背景装饰元素 - 增强效果 -->
    <div class="absolute inset-0 z-0">
      <div class="absolute -top-24 -right-24 h-64 w-64 rounded-full bg-indigo-100/40 blur-3xl"></div>
      <div class="absolute -bottom-32 -left-32 h-80 w-80 rounded-full bg-blue-100/30 blur-3xl"></div>
      <div class="absolute top-1/3 left-1/4 h-48 w-48 rounded-full bg-purple-100/20 blur-3xl"></div>
      <div class="noise-pattern absolute inset-0 opacity-[0.03]"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 标题区域 - 改进设计 -->
      <div class="mb-16 text-center">
        <span
          class="mb-3 inline-block rounded-full bg-indigo-50 px-4 py-1.5 text-sm font-medium text-indigo-800 shadow-sm">
          电商应用专区
        </span>
        <h2
          class="mb-6 bg-gradient-to-r from-indigo-600 to-blue-600 bg-clip-text text-3xl font-bold text-transparent md:text-4xl lg:text-5xl">
          提升电商分享转化率
        </h2>
        <p class="mx-auto max-w-2xl text-gray-600 md:text-lg">
          短链接如何为电商平台提供高效的用户体验和更好的营销效果
        </p>
        <div class="mx-auto mt-6 h-1 w-24 rounded-full bg-gradient-to-r from-indigo-300 via-blue-500 to-indigo-600">
        </div>
      </div>

      <div class="lg:grid lg:grid-cols-12 lg:items-center lg:gap-10">
        <!-- 左侧内容区域 - 改进设计 -->
        <div class="col-span-5 mb-16 lg:mb-0">
          <!-- 主要内容卡片 - 优化设计 -->
          <div class="mb-10 transform-gpu transition-all duration-500 hover:scale-[1.01]">
            <!-- 卡片标题 -->
            <div class="mb-5 flex items-center">
              <div
                class="mr-4 flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-indigo-600 to-blue-600 text-white shadow-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd"
                    d="M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.38z"
                    clip-rule="evenodd" />
                </svg>
              </div>
              <h3 class="text-2xl font-bold tracking-tight text-gray-900">
                为电商赋能的短链接方案
              </h3>
            </div>

            <!-- 特点列表 - 优化设计 -->
            <div class="space-y-6">
              <!-- 特点 1: 精准触达 -->
              <div
                class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl">
                <!-- 渐变边框 -->
                <div
                  class="absolute inset-0 rounded-xl bg-gradient-to-br from-indigo-500 via-purple-500 to-blue-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                </div>

                <!-- 内容区 -->
                <div
                  class="relative flex rounded-xl bg-white p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-indigo-50">
                  <!-- 左侧图标 -->
                  <div
                    class="feature-icon relative mr-5 flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-xl bg-indigo-100 text-indigo-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-indigo-600 group-hover:text-white">
                    <div
                      class="icon-pulse absolute inset-0 rounded-xl bg-indigo-600 opacity-0 transition-all duration-700 group-hover:animate-ping group-hover:opacity-30">
                    </div>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                      stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 15l-2 5L9 9l11 4-5 2zm0 0l5 5M7.188 2.239l.777 2.897M5.136 7.965l-2.898-.777M13.95 4.05l-2.122 2.122m-5.657 5.656l-2.12 2.122" />
                    </svg>
                  </div>

                  <!-- 右侧内容 -->
                  <div class="flex-1">
                    <div class="mb-2 flex items-center">
                      <h4
                        class="text-xl font-semibold text-gray-900 transition-all duration-500 group-hover:text-indigo-700">
                        精准触达
                      </h4>
                      <div
                        class="ml-3 flex items-center rounded-full bg-indigo-100 px-2.5 py-1 text-xs font-medium text-indigo-800 transition-all duration-500 group-hover:bg-indigo-700 group-hover:text-white">
                        核心优势
                      </div>
                    </div>
                    <p class="text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                      使用短链接引导精准触碰导购，便于跨平台分享传播，大幅提升转化率和用户参与度，让每次点击都更有价值。
                    </p>
                    <div
                      class="mt-3 flex items-center text-sm text-indigo-600 opacity-0 transition-all duration-500 group-hover:opacity-100">
                      <span class="font-medium">查看案例</span>
                      <svg xmlns="http://www.w3.org/2000/svg" class="ml-1.5 h-3.5 w-3.5" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                          d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z"
                          clip-rule="evenodd" />
                      </svg>
                    </div>
                  </div>

                  <!-- 右侧指示器 -->
                  <div class="ml-2 flex items-center self-center">
                    <div
                      class="h-6 w-1.5 rounded-full bg-indigo-100 transition-all duration-500 group-hover:h-14 group-hover:bg-indigo-600">
                    </div>
                  </div>
                </div>
              </div>

              <!-- 特点 2: 精准统计 -->
              <div
                class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-blue-200 hover:shadow-xl">
                <div
                  class="absolute inset-0 rounded-xl bg-gradient-to-br from-blue-500 via-cyan-500 to-teal-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                </div>

                <div
                  class="relative flex rounded-xl bg-white p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-blue-50">
                  <div
                    class="feature-icon relative mr-5 flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-xl bg-blue-100 text-blue-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-blue-600 group-hover:text-white">
                    <div
                      class="icon-pulse absolute inset-0 rounded-xl bg-blue-600 opacity-0 transition-all duration-700 group-hover:animate-ping group-hover:opacity-30">
                    </div>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                      stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                    </svg>
                  </div>

                  <div class="flex-1">
                    <div class="mb-2 flex items-center">
                      <h4
                        class="text-xl font-semibold text-gray-900 transition-all duration-500 group-hover:text-blue-700">
                        精准统计
                      </h4>
                      <div
                        class="ml-3 flex items-center rounded-full bg-blue-100 px-2.5 py-1 text-xs font-medium text-blue-800 transition-all duration-500 group-hover:bg-blue-700 group-hover:text-white">
                        数据驱动
                      </div>
                    </div>
                    <p class="text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                      分享后台精准识别渠道和引流来源，清晰统计转化路径，支持精细化数据分析，让营销决策更有依据。
                    </p>
                    <div
                      class="mt-3 flex items-center text-sm text-blue-600 opacity-0 transition-all duration-500 group-hover:opacity-100">
                      <span class="font-medium">查看报表</span>
                      <svg xmlns="http://www.w3.org/2000/svg" class="ml-1.5 h-3.5 w-3.5" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                          d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z"
                          clip-rule="evenodd" />
                      </svg>
                    </div>
                  </div>

                  <div class="ml-2 flex items-center self-center">
                    <div
                      class="h-6 w-1.5 rounded-full bg-blue-100 transition-all duration-500 group-hover:h-14 group-hover:bg-blue-600">
                    </div>
                  </div>
                </div>
              </div>

              <!-- 特点 3: 优化传播 -->
              <div
                class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-purple-200 hover:shadow-xl">
                <div
                  class="absolute inset-0 rounded-xl bg-gradient-to-br from-purple-500 via-indigo-500 to-blue-500 opacity-0 transition-opacity duration-500 group-hover:opacity-100">
                </div>

                <div
                  class="relative flex rounded-xl bg-white p-5 transition-all duration-500 group-hover:bg-gradient-to-br group-hover:from-white group-hover:to-purple-50">
                  <div
                    class="feature-icon relative mr-5 flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-xl bg-purple-100 text-purple-600 transition-all duration-500 group-hover:scale-110 group-hover:bg-purple-600 group-hover:text-white">
                    <div
                      class="icon-pulse absolute inset-0 rounded-xl bg-purple-600 opacity-0 transition-all duration-700 group-hover:animate-ping group-hover:opacity-30">
                    </div>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                      stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z" />
                    </svg>
                  </div>

                  <div class="flex-1">
                    <div class="mb-2 flex items-center">
                      <h4
                        class="text-xl font-semibold text-gray-900 transition-all duration-500 group-hover:text-purple-700">
                        优化传播
                      </h4>
                      <div
                        class="ml-3 flex items-center rounded-full bg-purple-100 px-2.5 py-1 text-xs font-medium text-purple-800 transition-all duration-500 group-hover:bg-purple-700 group-hover:text-white">
                        品牌增强
                      </div>
                    </div>
                    <p class="text-gray-600 transition-all duration-500 group-hover:text-gray-700">
                      短链接美观简洁，在有限字符内完整传达信息，提升品牌形象和用户体验，让每次分享都更加专业。
                    </p>
                    <div
                      class="mt-3 flex items-center text-sm text-purple-600 opacity-0 transition-all duration-500 group-hover:opacity-100">
                      <span class="font-medium">查看示例</span>
                      <svg xmlns="http://www.w3.org/2000/svg" class="ml-1.5 h-3.5 w-3.5" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                          d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z"
                          clip-rule="evenodd" />
                      </svg>
                    </div>
                  </div>

                  <div class="ml-2 flex items-center self-center">
                    <div
                      class="h-6 w-1.5 rounded-full bg-purple-100 transition-all duration-500 group-hover:h-14 group-hover:bg-purple-600">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 应用场景标签 - 优化设计 -->
          <div>
            <h4 class="mb-4 text-base font-medium text-gray-700">
              适用电商场景
            </h4>
            <div class="flex flex-wrap gap-3">
              <div
                class="scene-tag group flex cursor-pointer items-center rounded-full border border-indigo-100 bg-indigo-50 px-4 py-2 transition-all duration-300 hover:-translate-y-1 hover:border-indigo-200 hover:bg-indigo-100 hover:shadow-md">
                <div
                  class="mr-2 h-2.5 w-2.5 rounded-full bg-indigo-500 transition-all duration-300 group-hover:scale-125 group-hover:shadow-sm">
                </div>
                <span class="text-sm font-medium text-indigo-700">产品推广</span>
              </div>
              <div
                class="scene-tag group flex cursor-pointer items-center rounded-full border border-blue-100 bg-blue-50 px-4 py-2 transition-all duration-300 hover:-translate-y-1 hover:border-blue-200 hover:bg-blue-100 hover:shadow-md">
                <div
                  class="mr-2 h-2.5 w-2.5 rounded-full bg-blue-500 transition-all duration-300 group-hover:scale-125 group-hover:shadow-sm">
                </div>
                <span class="text-sm font-medium text-blue-700">社交营销</span>
              </div>
              <div
                class="scene-tag group flex cursor-pointer items-center rounded-full border border-green-100 bg-green-50 px-4 py-2 transition-all duration-300 hover:-translate-y-1 hover:border-green-200 hover:bg-green-100 hover:shadow-md">
                <div
                  class="mr-2 h-2.5 w-2.5 rounded-full bg-green-500 transition-all duration-300 group-hover:scale-125 group-hover:shadow-sm">
                </div>
                <span class="text-sm font-medium text-green-700">促销活动</span>
              </div>
              <div
                class="scene-tag group flex cursor-pointer items-center rounded-full border border-purple-100 bg-purple-50 px-4 py-2 transition-all duration-300 hover:-translate-y-1 hover:border-purple-200 hover:bg-purple-100 hover:shadow-md">
                <div
                  class="mr-2 h-2.5 w-2.5 rounded-full bg-purple-500 transition-all duration-300 group-hover:scale-125 group-hover:shadow-sm">
                </div>
                <span class="text-sm font-medium text-purple-700">会员引流</span>
              </div>
              <div
                class="scene-tag group flex cursor-pointer items-center rounded-full border border-pink-100 bg-pink-50 px-4 py-2 transition-all duration-300 hover:-translate-y-1 hover:border-pink-200 hover:bg-pink-100 hover:shadow-md">
                <div
                  class="mr-2 h-2.5 w-2.5 rounded-full bg-pink-500 transition-all duration-300 group-hover:scale-125 group-hover:shadow-sm">
                </div>
                <span class="text-sm font-medium text-pink-700">直播带货</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧可视化区域 - 优化设计 -->
        <div class="relative col-span-7">
          <div class="reveal-element relative mx-auto max-w-lg">
            <!-- 3D倾斜的手机模型 - 增强视觉效果 -->
            <div
              class="phone-container perspective-1200 rotate-x-10 rotate-y-10 transform transition-all duration-700 hover:rotate-y-16"
              ref="phoneContainer">
              <!-- 增加光晕效果 -->
              <div
                class="phone-glow absolute -inset-2 rounded-[50px] bg-gradient-to-br from-indigo-200/20 via-blue-200/10 to-transparent opacity-0 blur-2xl transition-opacity duration-700">
              </div>

              <!-- 手机框架 -->
              <div
                class="phone-frame relative h-[580px] w-[280px] overflow-hidden rounded-[36px] bg-gray-800 shadow-2xl">
                <!-- 模拟手机反光 -->
                <div
                  class="pointer-events-none absolute inset-0 z-20 bg-gradient-to-br from-gray-700/80 via-transparent to-gray-900/60">
                </div>

                <!-- 手机屏幕 -->
                <div class="phone-screen relative h-full w-full bg-white p-2">
                  <!-- 顶部状态栏 -->
                  <div class="mb-4 flex h-6 w-full items-center justify-between rounded-t-3xl bg-gray-100 px-4">
                    <div class="text-xs font-medium text-gray-500">12:30</div>
                    <div class="flex space-x-1">
                      <div class="h-2 w-2 rounded-full bg-gray-400"></div>
                      <div class="h-2 w-2 rounded-full bg-gray-400"></div>
                      <div class="h-2 w-2 rounded-full bg-gray-400"></div>
                    </div>
                  </div>

                  <!-- 模拟电商平台内容 -->
                  <div class="p-2">
                    <div
                      class="mb-4 flex h-8 w-full items-center justify-center rounded-lg bg-indigo-600 text-sm font-medium text-white">
                      商城首页
                    </div>

                    <!-- 产品卡片 -->
                    <div
                      class="product-card mb-3 overflow-hidden rounded-lg bg-white shadow-md transition-all duration-300 hover:shadow-lg">
                      <div
                        class="animate-shimmer relative aspect-video w-full overflow-hidden bg-gray-200 before:absolute before:inset-0 before:-translate-x-full before:animate-[shimmer_1.5s_infinite] before:bg-gradient-to-r before:from-transparent before:via-white/30 before:to-transparent">
                      </div>
                      <div class="p-3">
                        <div class="mb-2 h-4 w-3/4 rounded-full bg-gray-200"></div>
                        <div class="mb-3 h-3 w-1/2 rounded-full bg-gray-200"></div>
                        <div class="flex items-center justify-between">
                          <div class="h-5 w-16 rounded-full bg-red-500"></div>
                          <div class="flex h-6 w-6 items-center justify-center rounded-full bg-indigo-100">
                            <div class="h-3 w-3 rounded-full bg-indigo-600"></div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- 分享界面弹窗 -->
                    <div
                      class="share-popup absolute top-auto right-8 bottom-24 left-8 transform rounded-xl bg-white p-4 shadow-2xl transition-all duration-500 hover:scale-105"
                      ref="sharePopup">
                      <div class="mb-3 text-center text-sm font-medium">
                        分享给好友
                      </div>
                      <div class="grid grid-cols-4 gap-2">
                        <div class="share-option flex flex-col items-center">
                          <div
                            class="mb-1 flex h-10 w-10 items-center justify-center rounded-full bg-green-500 text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                              fill="currentColor">
                              <path d="M2 5a2 2 0 012-2h7a2 2 0 012 2v4a2 2 0 01-2 2H9l-3 3v-3H4a2 2 0 01-2-2V5z" />
                              <path
                                d="M15 7v2a4 4 0 01-4 4H9.828l-1.766 1.767c.28.149.599.233.938.233h2l3 3v-3h2a2 2 0 002-2V9a2 2 0 00-2-2h-1z" />
                            </svg>
                          </div>
                          <div class="text-xs">微信</div>
                        </div>
                        <div class="share-option flex flex-col items-center">
                          <div
                            class="mb-1 flex h-10 w-10 items-center justify-center rounded-full bg-blue-500 text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                              fill="currentColor">
                              <path fill-rule="evenodd"
                                d="M10 18a8 8 0 100-16 8 8 0 000 16zM4.332 8.027a6.012 6.012 0 011.912-2.706C6.512 5.73 6.974 6 7.5 6A1.5 1.5 0 019 7.5V8a2 2 0 004 0 2 2 0 011.523-1.943A5.977 5.977 0 0116 10c0 .34-.028.675-.083 1H15a2 2 0 00-2 2v2.197A5.973 5.973 0 0110 16v-2a2 2 0 00-2-2 2 2 0 01-2-2 2 2 0 00-1.668-1.973z"
                                clip-rule="evenodd" />
                            </svg>
                          </div>
                          <div class="text-xs">朋友圈</div>
                        </div>
                        <div class="share-option flex flex-col items-center">
                          <div
                            class="mb-1 flex h-10 w-10 items-center justify-center rounded-full bg-red-500 text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                              fill="currentColor">
                              <path
                                d="M15 8a3 3 0 10-2.977-2.63l-4.94 2.47a3 3 0 100 4.319l4.94 2.47a3 3 0 10.895-1.789l-4.94-2.47a3.027 3.027 0 000-.74l4.94-2.47C13.456 7.68 14.19 8 15 8z" />
                            </svg>
                          </div>
                          <div class="text-xs">链接</div>
                        </div>
                        <div class="share-option flex flex-col items-center">
                          <div
                            class="mb-1 flex h-10 w-10 items-center justify-center rounded-full bg-purple-500 text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                              fill="currentColor">
                              <path fill-rule="evenodd"
                                d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z"
                                clip-rule="evenodd" />
                            </svg>
                          </div>
                          <div class="text-xs">图片</div>
                        </div>
                      </div>

                      <!-- 短链接展示 -->
                      <div class="mt-4 flex items-center rounded-lg bg-indigo-50 p-2">
                        <div class="flex-1 overflow-hidden text-sm text-ellipsis whitespace-nowrap text-indigo-700">
                          timu.link/k8d2f
                        </div>
                        <div class="ml-2 rounded bg-indigo-600 px-2 py-1 text-xs text-white">
                          复制
                        </div>
                      </div>
                    </div>

                    <!-- 底部导航栏 -->
                    <div
                      class="absolute right-0 bottom-0 left-0 flex justify-around border-t border-gray-200 bg-white py-2">
                      <div class="flex flex-col items-center">
                        <div class="h-5 w-5 rounded bg-indigo-600"></div>
                        <div class="mt-1 h-2 w-8 rounded bg-gray-300"></div>
                      </div>
                      <div class="flex flex-col items-center">
                        <div class="h-5 w-5 rounded bg-gray-300"></div>
                        <div class="mt-1 h-2 w-8 rounded bg-gray-300"></div>
                      </div>
                      <div class="flex flex-col items-center">
                        <div class="h-5 w-5 rounded bg-gray-300"></div>
                        <div class="mt-1 h-2 w-8 rounded bg-gray-300"></div>
                      </div>
                      <div class="flex flex-col items-center">
                        <div class="h-5 w-5 rounded bg-gray-300"></div>
                        <div class="mt-1 h-2 w-8 rounded bg-gray-300"></div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 手机顶部凹槽 -->
                <div class="absolute top-0 left-1/2 z-30 h-8 w-32 -translate-x-1/2 rounded-b-2xl bg-gray-800">
                  <!-- 摄像头和扬声器 -->
                  <div class="absolute top-1.5 left-1/2 flex -translate-x-1/2 items-center space-x-4">
                    <div class="h-1.5 w-1.5 rounded-full bg-gray-600"></div>
                    <div class="h-1.5 w-8 rounded-full bg-gray-600"></div>
                    <div class="h-1.5 w-1.5 rounded-full bg-gray-600"></div>
                  </div>
                </div>

                <!-- 手机侧边按钮 -->
                <div class="absolute top-20 -right-1 h-14 w-1.5 rounded-l-lg bg-gray-700 shadow-inner"></div>
                <div class="absolute top-40 -right-1 h-14 w-1.5 rounded-l-lg bg-gray-700 shadow-inner"></div>
                <div class="absolute top-24 -left-1 h-18 w-1.5 rounded-r-lg bg-gray-700 shadow-inner"></div>
              </div>
            </div>

            <!-- 数据弹窗 -->
            <div
              class="data-popup absolute -top-6 -left-6 w-40 transform rounded-xl bg-white p-3 shadow-xl transition-all duration-500 hover:-translate-y-1"
              ref="dataPopup">
              <div class="flex items-center justify-between">
                <div class="text-xs font-medium text-gray-700">渠道分析</div>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z" />
                </svg>
              </div>
              <div class="mt-2 space-y-2">
                <div class="flex items-center">
                  <div class="mr-2 h-2 w-2 rounded-full bg-indigo-500"></div>
                  <div class="text-xs text-gray-600">微信 45%</div>
                </div>
                <div class="flex items-center">
                  <div class="mr-2 h-2 w-2 rounded-full bg-blue-500"></div>
                  <div class="text-xs text-gray-600">朋友圈 32%</div>
                </div>
                <div class="flex items-center">
                  <div class="mr-2 h-2 w-2 rounded-full bg-green-500"></div>
                  <div class="text-xs text-gray-600">其他 23%</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useIntersectionObserver } from "@vueuse/core";

// 创建元素引用
const phoneContainer = ref<HTMLElement | null>(null);
const dataPopup = ref<HTMLElement | null>(null);
const sharePopup = ref<HTMLElement | null>(null);


// 初始化手机模型和弹窗动画
useIntersectionObserver(
  phoneContainer,
  ([{ isIntersecting }]) => {
    if (isIntersecting) {
      phoneContainer.value?.classList.add("visible");
      phoneContainer.value
        ?.querySelector(".phone-glow")
        ?.classList.add("opacity-80");

      setTimeout(() => {
        sharePopup.value?.classList.add("animate-popup");

        setTimeout(() => {
          dataPopup.value?.classList.add("animate-popup");
        }, 600);
      }, 400);
    }
  },
  { threshold: 0.2 },
);

// 观察特性卡片和场景标签
onMounted(() => {
  // 创建观察者实例
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          // 添加交错动画效果
          setTimeout(
            () => {
              entry.target.classList.add("visible");
            },
            Array.from(
              document.querySelectorAll(".feature-card-3d, .scene-tag"),
            ).indexOf(entry.target) * 100,
          );
        }
      });
    },
    { threshold: 0.1 },
  );

  // 观察需要添加动画的元素
  const animatedElements = document.querySelectorAll(
    ".feature-card-3d, .scene-tag",
  );

  animatedElements.forEach((el) => {
    observer.observe(el);
    // 初始设置为不可见
    el.classList.add("opacity-0", "translate-y-8");
  });

  // 监听鼠标移动添加3D效果
  if (phoneContainer.value) {
    phoneContainer.value.addEventListener("mousemove", handleMouseMove);
  }

  // 模拟产品卡片加载动画
  const productCardsInterval = setInterval(() => {
    const productCards = document.querySelectorAll(".product-card");
    productCards.forEach((card, index) => {
      setTimeout(() => {
        card.classList.add("shimmer-loaded");
      }, index * 200);
    });
  }, 2000);

  // 清理函数
  return () => {
    clearInterval(productCardsInterval);
    observer.disconnect();
    if (phoneContainer.value) {
      phoneContainer.value.removeEventListener("mousemove", handleMouseMove);
    }
  };
});

// 处理鼠标移动以创建更自然的3D效果
const handleMouseMove = (e: MouseEvent) => {
  if (!phoneContainer.value) return;

  const rect = phoneContainer.value.getBoundingClientRect();
  const centerX = rect.left + rect.width / 2;
  const centerY = rect.top + rect.height / 2;
  const mouseX = e.clientX;
  const mouseY = e.clientY;

  // 计算从中心点的偏移
  const offsetX = (mouseX - centerX) / 15;
  const offsetY = (mouseY - centerY) / 15;

  // 应用转换
  phoneContainer.value.style.transform = `
    perspective(1200px) 
    rotateY(${10 + offsetX}deg) 
    rotateX(${10 - offsetY}deg)
    scale3d(1.01, 1.01, 1.01)
  `;
};
</script>

<style scoped>
@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

@keyframes pulse {

  0%,
  100% {
    opacity: 1;
  }

  50% {
    opacity: 0.7;
  }
}

@keyframes popUp {
  0% {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }

  70% {
    transform: scale(1.05) translateY(-5px);
  }

  100% {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.animate-shimmer {
  position: relative;
  overflow: hidden;
}

.animate-shimmer::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: -100%;
  background: linear-gradient(to right,
      transparent 0%,
      rgba(255, 255, 255, 0.3) 50%,
      transparent 100%);
  animation: shimmer 1.5s infinite;
}

.feature-card,
.metric-card,
.phone-container,
.reveal-element {
  transition: all 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}

.visible {
  opacity: 1 !important;
  transform: translateY(0) !important;
}

.animate-popup {
  animation: popUp 0.6s cubic-bezier(0.22, 1, 0.36, 1) forwards;
}

.shimmer-loaded .animate-shimmer::before {
  display: none;
}

.phone-container {
  transform-style: preserve-3d;
  transition: all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  will-change: transform;
}

/* 新增样式 */
.noise-pattern {
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='1'/%3E%3C/svg%3E");
}

.perspective-1200 {
  perspective: 1200px;
}

.feature-card-3d {
  box-shadow:
    0 4px 6px -1px rgba(0, 0, 0, 0.05),
    0 2px 4px -1px rgba(0, 0, 0, 0.03);
  transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.feature-card-3d:hover {
  box-shadow:
    0 12px 20px -3px rgba(0, 0, 0, 0.07),
    0 6px 8px -2px rgba(0, 0, 0, 0.04);
  transform: translateY(-6px);
}

/* 增强场景标签视觉效果 */
.scene-tag {
  transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.scene-tag:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.06);
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

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {

  .feature-card,
  .metric-card,
  .phone-container,
  .animate-shimmer::before,
  .animate-popup,
  .share-popup,
  .data-popup {
    animation: none !important;
    transition: none !important;
    transform: none !important;
    opacity: 1 !important;
  }
}
</style>
