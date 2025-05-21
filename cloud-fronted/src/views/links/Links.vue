<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 to-white py-16 md:py-24">
        <!-- 确保背景装饰元素正确放置在顶部 -->
        <DecorativeBackground />

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 使用PageHeader组件替换原有的标题区域 -->
            <PageHeader tag="短链接管理" title="管理您的短链接" description="查看、编辑和管理您的所有短链接" />

            <!-- 分组选择器 -->
            <div class="reveal-element mx-auto mb-8 max-w-6xl delay-300">
                <div class="rounded-xl bg-white p-4 shadow-md">
                    <div class="mb-4">
                        <h3 class="text-lg font-semibold text-gray-800">选择分组</h3>
                    </div>

                    <div class="flex flex-wrap gap-3">
                        <button @click="selectGroup(null)"
                            class="rounded-lg px-4 py-2 text-sm transition-all duration-300" :class="selectedGroupId === null
                                ? 'bg-indigo-600 text-white shadow-md'
                                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                ">
                            全部分组
                        </button>

                        <button v-for="group in groupData" :key="group.id" @click="selectGroup(group.id)"
                            class="flex items-center gap-2 rounded-lg px-4 py-2 text-sm transition-all duration-300"
                            :class="selectedGroupId === group.id
                                ? getSelectedButtonStyle(group.id)
                                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                ">
                            <span class="h-2 w-2 rounded-full" :class="getColorDot(getGroupIndex(group.id))"></span>
                            {{ group.title }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- 链接列表 -->
            <div class="reveal-element mx-auto max-w-6xl delay-300">
                <!-- 创建链接按钮 -->
                <div class="mb-6 flex justify-end">
                    <button
                        class="flex items-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-5 py-2.5 font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg hover:shadow-indigo-200">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
                                clip-rule="evenodd" />
                        </svg>
                        创建新链接
                    </button>
                </div>

                <!-- 链接卡片 -->
                <div class="space-y-4">
                    <div v-for="(link, index) in filteredLinks" :key="link.id"
                        class="link-card group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl">
                        <!-- 调试信息 - 可以在确认修复后删除 -->
                        <!-- <div class="absolute top-0 right-0 bg-black/70 text-white text-xs p-1 z-50">
                            Color Index: {{ getLinkColorIndex(link, index) }}
                        </div> -->

                        <!-- 确保这里使用 getLinkColorIndex 而不是 getGroupIndex -->
                        <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
                            :class="getBorderGradient(getLinkColorIndex(link, index))"></div>

                        <!-- 卡片内容 -->
                        <div class="relative overflow-hidden rounded-xl bg-white">
                            <!-- 顶部标题栏 - 确保这里使用 getLinkColorIndex -->
                            <div class="relative overflow-hidden p-4"
                                :class="getHeaderGradient(getLinkColorIndex(link, index))">
                                <!-- 装饰圆圈 -->
                                <div class="absolute -top-10 -right-10 h-24 w-24 rounded-full bg-white/10"></div>
                                <div class="absolute -bottom-16 -left-8 h-32 w-32 rounded-full bg-white/5"></div>

                                <div class="relative z-10 flex items-center justify-between">
                                    <h3 class="text-lg font-bold text-white">{{ link.title }}</h3>
                                    <div class="flex items-center gap-2">
                                        <span class="text-sm text-white/80">操作</span>
                                        <div class="flex space-x-1">
                                            <button @click="openEditLinkModal(link)"
                                                class="rounded-full bg-white/10 p-1.5 text-white transition-colors hover:bg-white/20">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path
                                                        d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                                                </svg>
                                            </button>
                                            <button @click="openDeleteConfirmModal(link)"
                                                class="rounded-full bg-white/10 p-1.5 text-white transition-colors hover:bg-red-400">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path fill-rule="evenodd"
                                                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                        clip-rule="evenodd" />
                                                </svg>
                                            </button>
                                            <button
                                                class="rounded-full bg-white/10 p-1.5 text-white transition-colors hover:bg-white/20">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path d="M8 2a1 1 0 000 2h2a1 1 0 100-2H8z" />
                                                    <path
                                                        d="M3 5a2 2 0 002-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v6h-4.586l1.293-1.293a1 1 0 00-1.414-1.414l-3 3a1 1 0 000 1.414l3 3a1 1 0 001.414-1.414L10.414 13H15v3a2 2 0 01-2 2H5a2 2 0 01-2-2V5zM15 11h2a1 1 0 110 2h-2v-2z" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 链接内容 -->
                            <div class="p-5">
                                <div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">原始链接</div>
                                        <div
                                            class="rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium break-all text-gray-800">
                                            {{ getOriginalUrl(link.originalUrl) }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">短链接</div>
                                        <div
                                            class="flex items-center justify-between rounded-lg border border-indigo-100 bg-indigo-50 p-2 text-sm font-medium text-indigo-600">
                                            <span>{{ `${link.domain}/${link.code}` }}</span>
                                            <button class="rounded p-1 transition-colors hover:bg-indigo-100">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-indigo-600"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path d="M8 3a1 1 0 011-1h2a1 1 0 110 2H9a1 1 0 01-1-1z" />
                                                    <path
                                                        d="M6 3a2 2 0 00-2 2v11a2 2 0 002 2h8a2 2 0 002-2V5a2 2 0 00-2-2 3 3 0 01-3 3H9a3 3 0 01-3-3z" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="mb-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">分组</div>
                                        <div
                                            class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            <span class="h-2 w-2 rounded-full"
                                                :class="getColorDot(getGroupIndex(link.groupId))"></span>
                                            {{ getGroupTitle(link.groupId) }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">过期时间</div>
                                        <div class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium"
                                            :class="isExpired(link.expired)
                                                ? 'text-red-600'
                                                : 'text-gray-800'
                                                ">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                                fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            {{ formatDate(link.expired) }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">状态</div>
                                        <div class="flex items-center gap-2 rounded-lg border p-2 text-sm font-medium"
                                            :class="getStatusClass(link.state, isExpired(link.expired))
                                                ">
                                            <span class="h-2 w-2 rounded-full" :class="getStatusDot(link.state, isExpired(link.expired))
                                                "></span>
                                            {{ getStatusText(link.state, isExpired(link.expired)) }}
                                        </div>
                                    </div>
                                </div>

                                <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">创建时间</div>
                                        <div
                                            class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                                fill="currentColor"
                                                :class="getIconColor(getLinkColorIndex(link, index), 0)">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            {{ formatDate(link.gmtCreate) }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">修改时间</div>
                                        <div
                                            class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                                fill="currentColor"
                                                :class="getIconColor(getLinkColorIndex(link, index), 1)">
                                                <path fill-rule="evenodd"
                                                    d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            {{ formatDate(link.gmtModified) }}
                                        </div>
                                    </div>
                                </div>

                                <!-- 链接操作按钮 - 确保这里使用 getLinkColorIndex -->
                                <div class="mt-5 flex justify-end space-x-3">
                                    <button
                                        class="flex items-center gap-1.5 rounded-lg border border-gray-300 px-4 py-2 text-sm text-gray-700 transition-colors duration-300 hover:bg-gray-50">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                                            <path fill-rule="evenodd"
                                                d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                                                clip-rule="evenodd" />
                                        </svg>
                                        查看统计
                                    </button>
                                    <button
                                        class="flex items-center gap-1.5 rounded-lg px-4 py-2 text-sm text-white transition-colors duration-300"
                                        :class="getActionButtonBg(getLinkColorIndex(link, index))">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path
                                                d="M11 3a1 1 0 100 2h2.586l-6.293 6.293a1 1 0 101.414 1.414L15 6.414V9a1 1 0 102 0V4a1 1 0 00-1-1h-5z" />
                                            <path
                                                d="M5 5a2 2 0 00-2 2v8a2 2 0 002 2h8a2 2 0 002-2v-3a1 1 0 10-2 0v3H5V7h3a1 1 0 000-2H5z" />
                                        </svg>
                                        访问链接
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 无链接数据提示 -->
                <div v-if="!filteredLinks.length"
                    class="rounded-xl border border-gray-100 bg-white p-10 text-center shadow-sm">
                    <div class="mb-4 inline-flex h-16 w-16 items-center justify-center rounded-full bg-indigo-100">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-600" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M12.586 4.586a2 2 0 112.828 2.828l-7 7a2 2 0 01-2.828 0l-7-7a2 2 0 111.414-1.414L9 14.586l3.586-3.586a2 2 0 013.536 0l3.586 3.586a1 1 0 010 1.414l-3.586 3.586a1 1 0 01-1.414 0l-7-7a1 1 0 010-1.414l7-7a1 1 0 011.414 0zM5 5a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2zM5 19a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2z" />
                        </svg>
                    </div>
                    <h3 class="mb-1 text-lg font-medium text-gray-900">暂无链接数据</h3>
                    <p class="mb-4 text-gray-500">
                        该分组下没有链接数据，或者您还没有创建任何链接
                    </p>
                </div>

                <!-- 分页区域 -->
                <div class="mt-6 flex justify-center">
                    <nav class="flex items-center space-x-1">
                        <button @click="prevPage" :disabled="currentPage <= 1" :class="[
                            'rounded-md border p-2',
                            currentPage <= 1
                                ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-400'
                                : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50',
                        ]">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                                    clip-rule="evenodd" />
                            </svg>
                        </button>

                        <template v-if="totalPages <= 7">
                            <button v-for="page in totalPages" :key="page" @click="goToPage(page)"
                                class="rounded-md border px-3 py-2" :class="page === currentPage
                                    ? 'border-indigo-500 bg-indigo-500 text-white'
                                    : 'border-gray-300 bg-white text-gray-700 hover:bg-gray-50'
                                    ">
                                {{ page }}
                            </button>
                        </template>

                        <template v-else>
                            <!-- 显示首页 -->
                            <button @click="goToPage(1)" class="rounded-md border px-3 py-2" :class="currentPage === 1
                                ? 'border-indigo-500 bg-indigo-500 text-white'
                                : 'border-gray-300 bg-white text-gray-700 hover:bg-gray-50'
                                ">
                                1
                            </button>

                            <!-- 左省略号 -->
                            <span v-if="showLeftEllipsis" class="px-2 text-gray-500">...</span>

                            <!-- 中间页码 -->
                            <button v-for="page in middlePages" :key="page" @click="goToPage(page)"
                                class="rounded-md border px-3 py-2" :class="page === currentPage
                                    ? 'border-indigo-500 bg-indigo-500 text-white'
                                    : 'border-gray-300 bg-white text-gray-700 hover:bg-gray-50'
                                    ">
                                {{ page }}
                            </button>

                            <!-- 右省略号 -->
                            <span v-if="showRightEllipsis" class="px-2 text-gray-500">...</span>

                            <!-- 显示尾页 -->
                            <button @click="goToPage(totalPages)" class="rounded-md border px-3 py-2" :class="currentPage === totalPages
                                ? 'border-indigo-500 bg-indigo-500 text-white'
                                : 'border-gray-300 bg-white text-gray-700 hover:bg-gray-50'
                                ">
                                {{ totalPages }}
                            </button>
                        </template>

                        <button @click="nextPage" :disabled="currentPage >= totalPages" :class="[
                            'rounded-md border p-2',
                            currentPage >= totalPages
                                ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-400'
                                : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50',
                        ]">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                                    clip-rule="evenodd" />
                            </svg>
                        </button>
                    </nav>
                </div>

                <!-- 页面大小选择器 -->
                <div class="mt-4 flex items-center justify-center gap-2 text-sm text-gray-600">
                    <span>每页显示</span>
                    <select v-model="pageSize" @change="handlePageSizeChange"
                        class="rounded-md border border-gray-300 bg-white p-1">
                        <option :value="10">10</option>
                        <option :value="20">20</option>
                        <option :value="50">50</option>
                        <option :value="100">100</option>
                    </select>
                    <span>条，总计 {{ totalCount }} 条数据</span>
                </div>
            </div>
        </div>

        <!-- 使用 BaseModal 组件重构编辑链接模态框 -->
        <BaseModal v-model="showEditLinkModal" title="更新链接信息" id="edit-link-modal">
            <!-- 表单内容 -->
            <form @submit.prevent="updateLink" class="space-y-4">
                <div>
                    <label for="edit-link-title" class="mb-1 block text-sm font-medium text-gray-700">链接标题</label>
                    <div class="relative">
                        <input type="text" id="edit-link-title" v-model="editingLink.title"
                            class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                            placeholder="请输入链接标题" required />
                    </div>
                    <p class="mt-1 text-xs text-gray-500">
                        <span>为您的链接添加一个易于识别的名称</span>
                    </p>
                </div>

                <div>
                    <label for="edit-link-group" class="mb-1 block text-sm font-medium text-gray-700">选择分组</label>
                    <select id="edit-link-group" v-model="editingLink.groupId"
                        class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500">
                        <option v-for="group in groupData" :key="group.id" :value="group.id">
                            {{ group.title }}
                        </option>
                    </select>
                    <p class="mt-1 text-xs text-gray-500">选择该链接所属的分组</p>
                </div>

                <div>
                    <label for="edit-link-domain-type" class="mb-1 block text-sm font-medium text-gray-700">域名类型</label>
                    <select id="edit-link-domain-type" v-model="editingLink.domainType"
                        class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500">
                        <option value="OFFICIAL">官方域名</option>
                        <option value="CUSTOM">自定义域名</option>
                    </select>
                    <p class="mt-1 text-xs text-gray-500">选择域名类型</p>
                </div>

                <div>
                    <label for="edit-link-domain-id" class="mb-1 block text-sm font-medium text-gray-700">域名</label>
                    <select id="edit-link-domain-id" v-model="editingLink.domainId"
                        class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500">
                        <option :value="1">timu.fun</option>
                        <!-- 这里可以根据实际域名列表动态生成选项 -->
                    </select>
                    <p class="mt-1 text-xs text-gray-500">选择短链接使用的域名</p>
                </div>

                <!-- 提交按钮容器 -->
                <div class="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
                    <button type="button" @click="closeEditLinkModal"
                        class="mt-2 w-full rounded-lg border border-gray-300 px-4 py-3 text-center text-base font-medium text-gray-700 transition-colors duration-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">
                        取消
                    </button>
                    <button type="submit" :disabled="isUpdatingLink || !editingLink.title"
                        class="flex w-full items-center justify-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-3 text-base font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg disabled:cursor-not-allowed disabled:opacity-70 sm:w-auto">
                        <svg v-if="isUpdatingLink" class="h-5 w-5 animate-spin text-white"
                            xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                            </circle>
                            <path class="opacity-75" fill="currentColor"
                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                            </path>
                        </svg>
                        {{ isUpdatingLink ? "更新中..." : "保存修改" }}
                    </button>
                </div>
            </form>
        </BaseModal>

        <!-- 使用 BaseModal 组件重构删除确认模态框 -->
        <BaseModal v-model="showDeleteConfirmModal" title="确认删除短链接" id="delete-confirm-modal"
            content-padding="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div class="sm:flex sm:items-start">
                <div
                    class="mx-auto flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                </div>
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                    <div class="mt-2">
                        <p class="text-sm text-gray-500">
                            您确定要删除这个短链接吗？此操作无法撤销，删除后该链接将无法访问。
                        </p>
                        <div class="mt-3 rounded-md bg-gray-50 p-3">
                            <p class="text-sm font-medium text-gray-700">链接详情：</p>
                            <p class="mt-1 text-sm text-gray-500">标题: {{ deletingLink.title }}</p>
                            <p class="mt-1 text-sm text-gray-500">短链接: {{ deletingLink.domain }}/{{ deletingLink.code }}
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 使用分离的页脚插槽 -->
            <template #separateFooter>
                <button type="button" @click="deleteLink" :disabled="isDeleting"
                    class="inline-flex w-full justify-center rounded-md border border-transparent bg-red-600 px-4 py-2 text-base font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-70 sm:ml-3 sm:w-auto sm:text-sm">
                    <svg v-if="isDeleting" class="mr-2 h-4 w-4 animate-spin text-white"
                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                        </circle>
                        <path class="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
                    {{ isDeleting ? "正在删除..." : "确认删除" }}
                </button>
                <button type="button" @click="closeDeleteConfirmModal"
                    class="mt-3 inline-flex w-full justify-center rounded-md border border-gray-300 bg-white px-4 py-2 text-base font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                    取消
                </button>
            </template>
        </BaseModal>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, reactive } from "vue";
import { GroupData, Data } from "./config";
import PageHeader from "@/components/PageHeader";
import DecorativeBackground from "@/components/DecorativeBackground.vue";
import BaseModal from "@/components/BaseModal.vue";
// 导入颜色方案工具
import {
    getHeaderGradient,
    getBorderGradient,
    getIconColor,
    getActionButtonBg,
    getColorDot,
    getLinkColorIndex,
    getSelectedButtonStyle as getSchemeButtonStyle // 重命名为 getSchemeButtonStyle 以避免命名冲突
} from "@/utils/ColorSchemeProvider";

// 分组数据
const groupData = ref(GroupData);

// 链接数据
const linkData = ref(Data.current_data);

// 分页信息 - 从API获取
const totalPages = ref(Data.total_page);
const totalCount = ref(Data.total_record);

// 当前选中的分组ID
const selectedGroupId = ref<number | null>(null);

// 分页相关参数
const currentPage = ref(1);
const pageSize = ref(20);

// 分页显示逻辑
const showLeftEllipsis = computed(() => currentPage.value > 3);
const showRightEllipsis = computed(
    () => currentPage.value < totalPages.value - 2,
);
const middlePages = computed(() => {
    const result = [];
    let start, end;

    if (totalPages.value <= 7) {
        // 总页数少于7，直接显示所有页码
        return [];
    } else if (currentPage.value <= 3) {
        // 当前页靠近开始
        start = 2;
        end = 5;
    } else if (currentPage.value >= totalPages.value - 2) {
        // 当前页靠近结束
        start = totalPages.value - 4;
        end = totalPages.value - 1;
    } else {
        // 当前页在中间
        start = currentPage.value - 1;
        end = currentPage.value + 1;
    }

    for (let i = start; i <= end; i++) {
        if (i > 1 && i < totalPages.value) {
            result.push(i);
        }
    }
    return result;
});

// 直接使用过滤后的链接数据，不需要再做本地分页
const filteredLinks = computed(() => {
    return linkData.value;
});

// 选择分组
const selectGroup = (groupId: number | null) => {
    selectedGroupId.value = groupId;
    // 切换分组时重置页码
    currentPage.value = 1;
    // 调用获取数据的方法
    fetchLinks();
};

// 切换页码
const goToPage = (page: number) => {
    currentPage.value = page;
    fetchLinks();
};

// 下一页
const nextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchLinks();
    }
};

// 上一页
const prevPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--;
        fetchLinks();
    }
};

// 处理每页大小变化
const handlePageSizeChange = () => {
    currentPage.value = 1; // 重置到第一页
    fetchLinks();
};

// 获取链接数据
const fetchLinks = () => {
    // 构建API请求参数
    const params = {
        groupId: selectedGroupId.value,
        page: currentPage.value,
        size: pageSize.value,
    };

    console.log("请求参数:", params);

    // 实际应用中这里应该是API请求
    // axios.post('/api/link/page', params)
    //   .then(response => {
    //     const result = response.data.data;
    //     linkData.value = result.current_data;
    //     totalPages.value = result.total_page;
    //     totalCount.value = result.total_record;
    //   })
    //   .catch(error => {
    //     console.error('获取链接数据失败:', error);
    //   });

    // 模拟API调用 - 仅用于演示
    setTimeout(() => {
        // 假设这是从API获取的数据
        linkData.value = Data.current_data;
        totalPages.value = Data.total_page;
        totalCount.value = Data.total_record;
    }, 300);
};

// 监听分组变化，重新获取数据
watch([selectedGroupId, currentPage, pageSize], () => {
    // 依赖变化时不需要重复调用，因为各个处理函数中已经调用了fetchLinks
});

// 获取分组标题
const getGroupTitle = (groupId: number) => {
    const group = groupData.value.find((g) => g.id === groupId);
    return group ? group.title : "未分组";
};

// 获取分组在数组中的索引，用于颜色分配
const getGroupIndex = (groupId: number) => {
    const index = groupData.value.findIndex((g) => g.id === groupId);
    return index >= 0 ? index : 0;
};

// 获取分组选中状态按钮样式 - 重命名并修复了递归调用问题
const getSelectedButtonStyle = (groupId: number) => {
    const index = getGroupIndex(groupId);
    return getSchemeButtonStyle(index);
};

// 处理原始URL显示
const getOriginalUrl = (url: string) => {
    // 假设URL格式可能需要处理
    if (url.includes("&")) {
        return url.split("&")[1] || url;
    }
    return url;
};

// 检查链接是否过期
const isExpired = (dateStr: string) => {
    try {
        const expireDate = new Date(dateStr);
        return expireDate < new Date();
    } catch (e) {
        return false;
    }
};

// 获取链接状态文本
const getStatusText = (state: string, expired: boolean) => {
    if (expired) return "已过期";
    if (state === "ACTIVE") return "活跃";
    return state;
};

// 获取状态点颜色
const getStatusDot = (state: string, expired: boolean) => {
    if (expired) return "bg-red-500";
    if (state === "ACTIVE") return "bg-emerald-500";
    return "bg-yellow-500";
};

// 获取状态样式类
const getStatusClass = (state: string, expired: boolean) => {
    if (expired) return "text-red-600 bg-red-50 border-red-100";
    if (state === "ACTIVE")
        return "text-emerald-600 bg-emerald-50 border-emerald-100";
    return "text-yellow-600 bg-yellow-50 border-yellow-100";
};



// 移除所有已经抽取到ColorSchemeProvider的函数：
// - getColorScheme
// - getHeaderGradient
// - getBorderGradient
// - getIconColor
// - getActionButtonBg
// - getColorDot
// - getLinkColorIndex (已在ColorSchemeProvider中重新实现)

// 格式化日期
const formatDate = (dateStr: string) => {
    try {
        const date = new Date(dateStr);
        return date.toLocaleString("zh-CN", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        });
    } catch (e) {
        return dateStr;
    }
};

// 添加页面动画效果
onMounted(() => {
    // 默认选择第一个分组
    if (groupData.value.length > 0) {
        selectedGroupId.value = groupData.value[0].id;
    }

    // 初始获取数据
    fetchLinks();

    // 如果浏览器支持，添加页面加载动画
    const revealElements = document.querySelectorAll(".reveal-element");

    if ("IntersectionObserver" in window) {
        const observer = new IntersectionObserver(
            (entries) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add("revealed");
                        observer.unobserve(entry.target);
                    }
                });
            },
            {
                threshold: 0.1,
            },
        );

        revealElements.forEach((el) => {
            observer.observe(el);
        });
    } else {
        // 对于不支持 IntersectionObserver 的浏览器，直接显示元素
        revealElements.forEach((el) => {
            el.classList.add("revealed");
        });
    }
});

// 编辑链接模态框状态
const showEditLinkModal = ref(false);
const isUpdatingLink = ref(false);
const editingLink = reactive({
    id: 0,
    code: "",
    title: "",
    groupId: 0,
    domainId: 0,
    domainType: "OFFICIAL",
});

// 打开编辑链接模态框
const openEditLinkModal = (link: any) => {
    editingLink.id = link.id;
    editingLink.code = link.code;
    editingLink.title = link.title;
    editingLink.groupId = link.groupId;

    // 从域名中提取domainId (示例实现，实际应根据域名结构调整)
    // 假设link.domain是域名字符串，这里简单地设置成1作为示例
    editingLink.domainId = 1;

    // 设置域名类型 (示例实现，实际应根据业务逻辑调整)
    editingLink.domainType = link.domain.includes("timu.fun")
        ? "OFFICIAL"
        : "CUSTOM";

    showEditLinkModal.value = true;
};

// 关闭编辑链接模态框
const closeEditLinkModal = () => {
    showEditLinkModal.value = false;
};

// 更新链接
const updateLink = async () => {
    if (!editingLink.title) return;

    isUpdatingLink.value = true;

    try {
        // 构建请求参数
        const params = {
            groupId: editingLink.groupId,
            code: editingLink.code,
            title: editingLink.title,
            domainId: editingLink.domainId,
            domainType: editingLink.domainType,
        };

        console.log("更新链接参数:", params);

        // 模拟API请求
        await new Promise((resolve) => setTimeout(resolve, 800));

        // 更新本地数据
        const index = linkData.value.findIndex(
            (link) => link.id === editingLink.id,
        );

        if (index !== -1) {
            // 创建更新后的链接对象
            const updatedLink = {
                ...linkData.value[index],
                title: editingLink.title,
                groupId: editingLink.groupId,
                // 如果有其他需要更新的字段，也在这里添加
                gmtModified: new Date().toISOString(),
            };

            // 更新数据
            linkData.value[index] = updatedLink;
            console.log("链接更新成功:", updatedLink);
        }

        // 关闭模态框
        closeEditLinkModal();

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("链接更新成功");
    } catch (error) {
        console.error("更新链接失败:", error);
    } finally {
        isUpdatingLink.value = false;
    }
};

// 删除链接相关状态
const showDeleteConfirmModal = ref(false);
const isDeleting = ref(false);
const deletingLink = reactive({
    id: 0,
    code: "",
    title: "",
    domain: "",
    groupId: 0,
});

// 打开删除确认模态框
const openDeleteConfirmModal = (link: any) => {
    deletingLink.id = link.id;
    deletingLink.code = link.code;
    deletingLink.title = link.title;
    deletingLink.domain = link.domain;
    deletingLink.groupId = link.groupId;
    showDeleteConfirmModal.value = true;
};

// 关闭删除确认模态框
const closeDeleteConfirmModal = () => {
    showDeleteConfirmModal.value = false;
};

// 删除链接
const deleteLink = async () => {
    isDeleting.value = true;

    try {
        // 构建请求参数
        const params = {
            groupId: deletingLink.groupId,
            code: deletingLink.code,
        };

        console.log("删除链接参数:", params);

        // 模拟API请求
        await new Promise((resolve) => setTimeout(resolve, 800));

        // 从本地数据中移除该链接
        const index = linkData.value.findIndex(
            (link) => link.id === deletingLink.id,
        );

        if (index !== -1) {
            linkData.value.splice(index, 1);
            console.log("链接删除成功:", deletingLink);

            // 更新总数
            totalCount.value -= 1;

            // 如果当前页没有数据且不是第一页，则回退到上一页
            if (filteredLinks.value.length === 0 && currentPage.value > 1) {
                currentPage.value -= 1;
                fetchLinks();
            }
        }

        // 关闭模态框
        closeDeleteConfirmModal();

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("链接删除成功");
    } catch (error) {
        console.error("删除链接失败:", error);
    } finally {
        isDeleting.value = false;
    }
};
</script>

<style scoped>
/* 移除重复的噪点图案定义，让它来自组件 */
/* .noise-pattern {
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%' height='100%' filter='url(%23noiseFilter)' opacity='1'/%3E%3C/svg%3E");
} */

/* 卡片效果 */
.link-card {
    box-shadow:
        0 4px 6px -1px rgba(0, 0, 0, 0.05),
        0 2px 4px -1px rgba(0, 0, 0, 0.03);
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
}

.link-card:hover {
    box-shadow:
        0 12px 20px -3px rgba(0, 0, 0, 0.07),
        0 6px 8px -2px rgba(0, 0, 0, 0.04);
}

/* 元素显示动画 */
.reveal-element {
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1);
}

.reveal-element.delay-300 {
    transition-delay: 0.3s;
}

.reveal-element.delay-500 {
    transition-delay: 0.5s;
}

.reveal-element.revealed {
    opacity: 1;
    transform: translateY(0);
}

/* 组卡片悬浮效果 */
.group-card {
    transition: all 0.5s cubic-bezier(0.22, 1, 0.36, 1);
    position: relative;
    z-index: 1;
}

.group-card:hover {
    transform: translateY(-0.5rem);
    z-index: 2;
}

/* 移动端优化 */
@media (max-width: 640px) {
    .reveal-element {
        opacity: 0.1;
    }

    /* 改善移动端按钮点击区域 */
    button {
        min-height: 44px;
    }
}

/* 修复一些动画问题 */
@media (prefers-reduced-motion: reduce) {
    .reveal-element {
        opacity: 1 !important;
        transform: none !important;
        transition: none !important;
    }
}

/* 卡片颜色循环显示动画 */
@keyframes gradientShift {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

/* 让渐变背景有动画效果 */
[class*="bg-gradient-to-r"] {
    background-size: 200% 200%;
    animation: gradientShift 6s ease infinite;
}

/* 溢出文本处理 */
.break-all {
    word-break: break-all;
}

/* 模态框动画 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

/* 模态框内容动画 */
.transform {
    transition-property: transform, opacity;
    transition-duration: 0.3s;
}

/* 模态框移动端优化 */
@media (max-width: 640px) {

    /* 确保模态框内容在移动设备上更易于点击 */
    input,
    select,
    button {
        font-size: 16px;
        /* 防止iOS自动缩放 */
        min-height: 44px;
        /* 确保触摸友好 */
    }

    /* 模态框从底部滑入效果 */
    [role="dialog"] [role="dialog"] {
        bottom: 0;
        position: fixed;
        border-bottom-left-radius: 0;
        border-bottom-right-radius: 0;
    }
}

/* 删除按钮动画 */
.animate-spin {
    animation: spin 1s linear infinite;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}
</style>
