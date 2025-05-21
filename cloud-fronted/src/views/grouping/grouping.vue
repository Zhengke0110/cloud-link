<template>
    <div class="relative overflow-hidden bg-gradient-to-b from-gray-50 to-white py-16 md:py-24">
        <!-- 确保背景装饰元素正确放置在顶部 -->
        <DecorativeBackground />

        <div class="relative z-10 container mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 使用PageHeader组件替换原有的标题区域 -->
            <PageHeader tag="链接分组管理" title="管理您的链接分组" description="组织和管理您的短链接，提高工作效率" />

            <!-- 分组数据展示 -->
            <div class="reveal-element mx-auto max-w-3xl delay-300">
                <!-- 创建分组按钮 -->
                <div class="mb-8 flex justify-center">
                    <button @click="openCreateModal"
                        class="flex items-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-6 py-3 font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg hover:shadow-indigo-200">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
                                clip-rule="evenodd" />
                        </svg>
                        创建新分组
                    </button>
                </div>

                <!-- 分组卡片 -->
                <div class="space-y-4 md:space-y-6">
                    <div v-for="(group, index) in groupData" :key="group.id"
                        class="group-card group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-0.5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:border-indigo-200 hover:shadow-xl">
                        <div class="absolute inset-0 rounded-xl opacity-0 transition-opacity duration-500 group-hover:opacity-100"
                            :class="getBorderGradient(index)"></div>

                        <!-- 卡片内容 -->
                        <div class="relative overflow-hidden rounded-xl bg-white">
                            <!-- 顶部标题栏 -->
                            <div class="relative overflow-hidden p-5" :class="getHeaderGradient(index)">
                                <!-- 装饰圆圈 -->
                                <div class="absolute -top-10 -right-10 h-24 w-24 rounded-full bg-white/10"></div>
                                <div class="absolute -bottom-16 -left-8 h-32 w-32 rounded-full bg-white/5"></div>

                                <div class="relative z-10 flex items-center justify-between">
                                    <h3 class="text-xl font-bold text-white">
                                        {{ group.title }}
                                    </h3>
                                    <div class="flex items-center gap-2">
                                        <span class="text-sm text-white/80">操作</span>
                                        <div class="flex space-x-1">
                                            <button @click="openEditModal(group)"
                                                class="rounded-full bg-white/10 p-1.5 text-white transition-colors hover:bg-white/20">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path
                                                        d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                                                </svg>
                                            </button>
                                            <button @click="openDeleteConfirmModal(group)"
                                                class="rounded-full bg-white/10 p-1.5 text-white transition-colors hover:bg-red-400">
                                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                    viewBox="0 0 20 20" fill="currentColor">
                                                    <path fill-rule="evenodd"
                                                        d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                        clip-rule="evenodd" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- 分组内容 -->
                            <div class="p-6">
                                <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">分组ID</div>
                                        <div
                                            class="rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium break-all text-gray-800">
                                            {{ group.id }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">账号</div>
                                        <div
                                            class="rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            {{ group.accountNo }}
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">创建时间</div>
                                        <div
                                            class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                                fill="currentColor" :class="getIconColor(index, 0)">
                                                <path fill-rule="evenodd"
                                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            {{ formatDate(group.gmtCreate) }}
                                        </div>
                                    </div>
                                    <div>
                                        <div class="mb-1 text-xs text-gray-500">修改时间</div>
                                        <div
                                            class="flex items-center gap-2 rounded-lg border border-gray-100 bg-gray-50 p-2 text-sm font-medium text-gray-800">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                                fill="currentColor" :class="getIconColor(index, 1)">
                                                <path fill-rule="evenodd"
                                                    d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                            {{ formatDate(group.gmtModified) }}
                                        </div>
                                    </div>
                                </div>

                                <!-- 分组操作按钮 -->
                                <div class="mt-5 flex justify-end space-x-3">
                                    <button
                                        class="flex items-center gap-1.5 rounded-lg border border-gray-300 px-4 py-2 text-sm text-gray-700 transition-colors duration-300 hover:bg-gray-50">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path
                                                d="M11 3a1 1 0 100 2h2.586l-6.293 6.293a1 1 0 101.414 1.414L15 6.414V9a1 1 0 102 0V4a1 1 0 00-1-1h-5z" />
                                            <path
                                                d="M5 5a2 2 0 00-2 2v8a2 2 0 002 2h8a2 2 0 002-2v-3a1 1 0 10-2 0v3H5V7h3a1 1 0 000-2H5z" />
                                        </svg>
                                    </button>
                                    <button
                                        class="flex items-center gap-1.5 rounded-lg px-4 py-2 text-sm text-white transition-colors duration-300"
                                        :class="getActionButtonBg(index)">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                            fill="currentColor">
                                            <path
                                                d="M5 4a1 1 0 00-2 0v7.268a2 2 0 000 3.464V16a1 1 0 102 0v-1.268a2 2 0 000-3.464V4zM11 4a1 1 0 10-2 0v1.268a2 2 0 000 3.464V16a1 1 0 102 0V8.732a2 2 0 000-3.464V4zM16 3a1 1 0 011 1v7.268a2 2 0 010 3.464V16a1 1 0 11-2 0v-1.268a2 2 0 010-3.464V4a1 1 0 011-1z" />
                                        </svg>
                                        管理分组
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 无分组数据提示 -->
                <div v-if="!groupData.length"
                    class="rounded-xl border border-gray-100 bg-white p-10 text-center shadow-sm">
                    <div class="mb-4 inline-flex h-16 w-16 items-center justify-center rounded-full bg-indigo-100">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-600" viewBox="0 0 20 20"
                            fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M2 6a2 2 0 012-2h4l2 2h4a2 2 0 012 2v1H8a3 3 0 00-3 3v1.5a1.5 1.5 0 01-3 0V6z"
                                clip-rule="evenodd" />
                            <path d="M6 12a2 2 0 012-2h8a2 2 0 012 2v2a2 2 0 01-2 2H2h2a2 2 0 002-2v-2z" />
                        </svg>
                    </div>
                    <h3 class="mb-1 text-lg font-medium text-gray-900">暂无分组数据</h3>
                    <p class="mb-4 text-gray-500">
                        您还没有创建任何分组，点击上方按钮创建新分组
                    </p>
                </div>
            </div>

            <!-- 功能介绍卡片 -->
            <div class="reveal-element mx-auto mt-16 max-w-4xl delay-500">
                <div class="mb-6 text-center md:mb-8">
                    <h3 class="mb-1 text-lg font-bold text-gray-900 md:mb-2 md:text-2xl">
                        分组管理功能
                    </h3>
                    <p class="text-sm text-gray-600 md:text-base">
                        更好地组织和管理您的短链接
                    </p>
                </div>

                <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3">
                    <!-- 功能卡片 -->
                    <div
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">
                        <div class="mb-4 flex">
                            <div
                                class="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-lg bg-indigo-100 text-indigo-600">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path
                                        d="M7 3a1 1 0 000 2h6a1 1 0 100-2H7zM4 7a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1zM2 11a2 2 0 002-2h12a2 2 0 012 2v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z" />
                                </svg>
                            </div>
                        </div>
                        <h4 class="mb-2 text-lg font-semibold text-gray-900">分类整理</h4>
                        <p class="text-sm text-gray-600">
                            将相似的短链接分组整理，便于后续查找和管理
                        </p>
                    </div>

                    <div
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">
                        <div class="mb-4 flex">
                            <div
                                class="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-lg bg-blue-100 text-blue-600">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path fill-rule="evenodd"
                                        d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                                        clip-rule="evenodd" />
                                </svg>
                            </div>
                        </div>
                        <h4 class="mb-2 text-lg font-semibold text-gray-900">批量操作</h4>
                        <p class="text-sm text-gray-600">
                            对分组内的链接进行批量管理，提高工作效率
                        </p>
                    </div>

                    <div
                        class="feature-card-3d group relative overflow-hidden rounded-xl border border-gray-100 bg-white p-5 shadow-md transition-all duration-500 hover:-translate-y-1 hover:shadow-xl">
                        <div class="mb-4 flex">
                            <div
                                class="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-lg bg-purple-100 text-purple-600">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path d="M2 10a8 8 0 018-8v8h8a8 8 0 11-16 0z" />
                                    <path d="M12 2.252A8.014 8.014 0 0117.748 8H12V2.252z" />
                                </svg>
                            </div>
                        </div>
                        <h4 class="mb-2 text-lg font-semibold text-gray-900">数据分析</h4>
                        <p class="text-sm text-gray-600">
                            查看分组内链接的总体数据分析，了解使用情况
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- 创建分组模态框 -->
        <!-- 使用 BaseModal 组件重构创建分组模态框 -->
        <BaseModal v-model="showCreateModal" title="创建新分组" id="create-group-modal">
            <!-- 表单内容 -->
            <form @submit.prevent="createGroup" class="space-y-4">
                <div>
                    <label for="group-title" class="mb-1 block text-sm font-medium text-gray-700">分组名称</label>
                    <div class="relative">
                        <input type="text" id="group-title" v-model="newGroup.title"
                            class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                            placeholder="请输入分组名称" required />
                        <button type="button" v-if="newGroup.title" @click="generateRandomGroupName"
                            class="absolute top-1/2 right-3 -translate-y-1/2 rounded-full p-1 text-gray-400 hover:bg-indigo-50 hover:text-indigo-600"
                            title="生成随机名称">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                    clip-rule="evenodd" />
                            </svg>
                        </button>
                    </div>
                    <p class="mt-1 text-xs text-gray-500">为您的分组添加一个易于识别的名称</p>
                </div>

                <!-- 提交按钮容器 -->
                <div class="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
                    <button type="button" @click="closeCreateModal"
                        class="mt-2 w-full rounded-lg border border-gray-300 px-4 py-3 text-center text-base font-medium text-gray-700 transition-colors duration-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">
                        取消
                    </button>
                    <button type="submit" :disabled="isCreating || !newGroup.title"
                        class="flex w-full items-center justify-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-3 text-base font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg disabled:cursor-not-allowed disabled:opacity-70 sm:w-auto">
                        <LoadingSpinner v-if="isCreating" class="text-white" /> {{ isCreating ? "创建中..." : "创建分组" }}
                    </button>
                </div>
            </form>
        </BaseModal>

        <!-- 使用 BaseModal 组件重构编辑分组模态框 -->
        <BaseModal v-model="showEditModal" title="编辑分组信息" id="edit-group-modal">
            <!-- 表单内容 -->
            <form @submit.prevent="updateGroup" class="space-y-4">
                <div>
                    <label for="edit-group-title" class="mb-1 block text-sm font-medium text-gray-700">分组名称</label>
                    <div class="relative">
                        <input type="text" id="edit-group-title" v-model="editingGroup.title"
                            class="w-full rounded-lg border border-gray-300 px-4 py-3 text-base transition-all duration-200 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-500"
                            placeholder="请输入分组名称" required />
                        <button type="button" @click="generateRandomGroupNameForEdit"
                            class="absolute top-1/2 right-3 -translate-y-1/2 rounded-full p-1 text-gray-400 hover:bg-indigo-50 hover:text-indigo-600"
                            title="生成随机名称">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path fill-rule="evenodd"
                                    d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                    clip-rule="evenodd" />
                            </svg>
                        </button>
                    </div>
                    <p class="mt-1 text-xs text-gray-500">
                        <span>为您的分组添加一个易于识别的名称</span>
                        <span class="ml-1 text-gray-400">ID: {{ editingGroup.id }}</span>
                    </p>
                </div>

                <!-- 提交按钮容器 -->
                <div class="mt-5 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
                    <button type="button" @click="closeEditModal"
                        class="mt-2 w-full rounded-lg border border-gray-300 px-4 py-3 text-center text-base font-medium text-gray-700 transition-colors duration-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">
                        取消
                    </button>
                    <button type="submit" :disabled="isUpdating || !editingGroup.title"
                        class="flex w-full items-center justify-center gap-2 rounded-lg bg-gradient-to-r from-indigo-600 to-blue-600 px-4 py-3 text-base font-medium text-white shadow-md transition-all duration-300 hover:shadow-lg disabled:cursor-not-allowed disabled:opacity-70 sm:w-auto">
                        <LoadingSpinner v-if="isUpdating" class="h-5 w-5 animate-spin text-white" />
                        {{ isUpdating ? "更新中..." : "保存修改" }}
                    </button>
                </div>
            </form>
        </BaseModal>

        <!-- 使用 BaseModal 组件重构删除确认模态框 -->
        <BaseModal v-model="showDeleteConfirmModal" title="确认删除分组" id="delete-group-modal"
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
                            您确定要删除这个分组吗？此操作无法撤销，删除后该分组中的所有链接将移至默认分组。
                        </p>
                        <div class="mt-3 rounded-md bg-gray-50 p-3">
                            <p class="text-sm font-medium text-gray-700">分组详情：</p>
                            <p class="mt-1 text-sm text-gray-500">名称: {{ deletingGroup.title }}</p>
                            <p class="mt-1 text-sm text-gray-500">ID: {{ deletingGroup.id }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 使用分离的页脚插槽 -->
            <template #separateFooter>
                <button type="button" @click="deleteGroup" :disabled="isDeleting"
                    class="inline-flex w-full justify-center rounded-md border border-transparent bg-red-600 px-4 py-2 text-base font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-70 sm:ml-3 sm:w-auto sm:text-sm">
                    <LoadingSpinner v-if="isDeleting" size="mr-2 h-4 w-4" class="text-white" />
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
import { ref, onMounted, reactive } from "vue";
import { Data } from "./config";
import PageHeader from "@/components/PageHeader";
import DecorativeBackground from "@/components/DecorativeBackground.vue";
import BaseModal from "@/components/BaseModal.vue";
import LoadingSpinner from '@/components/LoadingSpinner.vue';
// 导入日期工具函数
import { formatDate } from "@/utils/DateUtils";
// 导入颜色方案工具
import {
    getHeaderGradient,
    getBorderGradient,
    getIconColor,
    getActionButtonBg
} from "@/utils/ColorSchemeProvider";
import { initPageAnimations } from '@/utils/AnimationUtils'

// 分组数据
const groupData = ref(Data);

// 创建分组模态框状态
const showCreateModal = ref(false);
const isCreating = ref(false);
const newGroup = reactive({
    title: "",
});

// 编辑分组模态框状态
const showEditModal = ref(false);
const isUpdating = ref(false);
const editingGroup = reactive({
    id: 0,
    title: "",
});

// 删除分组相关状态
const showDeleteConfirmModal = ref(false);
const isDeleting = ref(false);
const deletingGroup = reactive({
    id: 0,
    title: "",
});

// 打开创建模态框
const openCreateModal = () => {
    newGroup.title = `我是测试分组-${generateRandomNumber(1, 100)}`;
    showCreateModal.value = true;
};

// 关闭创建模态框
const closeCreateModal = () => {
    showCreateModal.value = false;
};

// 打开编辑模态框
const openEditModal = (group: any) => {
    editingGroup.id = group.id;
    editingGroup.title = group.title;
    showEditModal.value = true;
};

// 关闭编辑模态框
const closeEditModal = () => {
    showEditModal.value = false;
};

// 打开删除确认模态框
const openDeleteConfirmModal = (group: any) => {
    deletingGroup.id = group.id;
    deletingGroup.title = group.title;
    showDeleteConfirmModal.value = true;
};

// 关闭删除确认模态框
const closeDeleteConfirmModal = () => {
    showDeleteConfirmModal.value = false;
};

// 生成随机分组名称 (创建)
const generateRandomGroupName = () => {
    newGroup.title = `我是测试分组-${generateRandomNumber(1, 100)}`;
};

// 生成随机分组名称 (编辑)
const generateRandomGroupNameForEdit = () => {
    editingGroup.title = `我是测试分组-${generateRandomNumber(1, 100)}`;
};

// 生成随机数字
const generateRandomNumber = (min: number, max: number) => {
    return Math.floor(Math.random() * (max - min + 1) + min);
};

// 创建分组
const createGroup = async () => {
    if (!newGroup.title) return;

    isCreating.value = true;

    try {
        // 模拟API请求
        await new Promise((resolve) => setTimeout(resolve, 800));

        // 创建新分组对象
        const newGroupData = {
            id: Date.now(),
            title: newGroup.title,
            accountNo: 1126256272715284480,
            gmtCreate: new Date().toISOString(),
            gmtModified: new Date().toISOString(),
        };

        // 将新分组添加到数据中
        groupData.value = [newGroupData, ...groupData.value];
        console.log("分组数据:", groupData.value);
        // 关闭模态框
        closeCreateModal();

        // 重置表单
        newGroup.title = "";

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("分组创建成功");
    } catch (error) {
        console.error("创建分组失败:", error);
    } finally {
        isCreating.value = false;
    }
};

// 更新分组
const updateGroup = async () => {
    if (!editingGroup.title) return;

    isUpdating.value = true;

    try {
        // 模拟API请求
        await new Promise((resolve) => setTimeout(resolve, 800));

        // 更新分组数据
        const index = groupData.value.findIndex(
            (group) => group.id === editingGroup.id,
        );

        if (index !== -1) {
            // 创建更新后的分组对象
            const updatedGroup = {
                ...groupData.value[index],
                title: editingGroup.title,
                gmtModified: new Date().toISOString(),
            };

            // 更新数据
            groupData.value[index] = updatedGroup;
            console.log("分组数据更新:", updatedGroup);
        }

        // 关闭模态框
        closeEditModal();

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("分组更新成功");
    } catch (error) {
        console.error("更新分组失败:", error);
    } finally {
        isUpdating.value = false;
    }
};

// 删除分组
const deleteGroup = async () => {
    isDeleting.value = true;

    try {
        // 构建请求参数 - 这里可能需要根据API要求调整
        const params = {
            id: deletingGroup.id,
        };

        console.log("删除分组参数:", params);

        // 模拟API请求
        await new Promise((resolve) => setTimeout(resolve, 800));

        // 从本地数据中移除该分组
        const index = groupData.value.findIndex(
            (group) => group.id === deletingGroup.id,
        );

        if (index !== -1) {
            groupData.value.splice(index, 1);
            console.log("分组删除成功:", deletingGroup);
        }

        // 关闭模态框
        closeDeleteConfirmModal();

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("分组删除成功");
    } catch (error) {
        console.error("删除分组失败:", error);
    } finally {
        isDeleting.value = false;
    }
};

// 添加页面动画效果
onMounted(() => initPageAnimations());
</script>

<style scoped>

/* 卡片效果 */
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
</style>
