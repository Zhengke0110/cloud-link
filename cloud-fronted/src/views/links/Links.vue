<template>
    <PageLayout tag="短链接管理" title="管理您的短链接" description="查看、编辑和管理您的所有短链接">
        <!-- 分组选择器 -->
        <div class="reveal-element mx-auto mb-8 max-w-6xl delay-300">
            <div class="rounded-xl bg-white p-4 shadow-md">
                <div class="mb-4">
                    <h3 class="text-lg font-semibold text-gray-800">选择分组</h3>
                </div>

                <div class="flex flex-wrap gap-3">
                    <button @click="selectGroup(null)" class="rounded-lg px-4 py-2 text-sm transition-all duration-300"
                        :class="selectedGroupId === null
                            ? 'bg-indigo-600 text-white shadow-md'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                            ">
                        全部分组
                    </button>

                    <button v-for="group in groupData" :key="group.id" @click="selectGroup(group.id)"
                        class="flex items-center gap-2 rounded-lg px-4 py-2 text-sm transition-all duration-300" :class="selectedGroupId === group.id
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
                <LinkCard v-for="(link, index) in filteredLinks" :key="link.id" :title="link.title"
                    :colorIndex="getLinkColorIndex(link, index)">
                    <!-- 顶部操作按钮 -->
                    <template #header-actions>
                        <span class="text-sm text-white/80">操作</span>
                        <div class="flex space-x-1">
                            <!-- 替换为IconActionButton组件 -->
                            <IconActionButton icon="edit" @click="openEditLinkModal(link)" title="编辑链接" />

                            <!-- 替换为IconActionButton组件，并设置为危险类型 -->
                            <IconActionButton icon="delete" type="danger" @click="openDeleteConfirmModal(link)"
                                title="删除链接" />

                            <!-- 替换为IconActionButton组件 -->
                            <IconActionButton icon="share" title="分享链接" />
                        </div>
                    </template>

                    <!-- 链接内容 -->
                    <div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                        <!-- 使用InfoField替换原始链接字段 -->
                        <InfoField label="原始链接" :value="getOriginalUrl(link.originalUrl)" breakAll />

                        <!-- 使用InfoField替换短链接字段 -->
                        <InfoField label="短链接" valueClass="text-indigo-600" borderClass="border-indigo-100"
                            bgClass="bg-indigo-50">
                            <div class="flex items-center justify-between w-full">
                                <span>{{ `${link.domain}/${link.code}` }}</span>
                                <!-- 替换为IconActionButton组件，使用light变体 -->
                                <IconActionButton icon="copy" variant="light" size="sm" title="复制链接"
                                    customClass="text-indigo-600 hover:bg-indigo-100" />
                            </div>
                        </InfoField>
                    </div>

                    <div class="mb-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
                        <!-- 使用InfoField替换分组字段 -->
                        <InfoField label="分组">
                            <div class="flex items-center gap-2 w-full">
                                <span class="h-2 w-2 rounded-full"
                                    :class="getColorDot(getGroupIndex(link.groupId))"></span>
                                {{ getGroupTitle(link.groupId) }}
                            </div>
                        </InfoField>

                        <!-- 使用InfoField替换过期时间字段 -->
                        <InfoField label="过期时间" :value="formatDate(link.expired)" icon="clock"
                            :valueClass="isExpired(link.expired) ? 'text-red-600' : 'text-gray-800'" />

                        <!-- 使用InfoField替换状态字段 -->
                        <InfoField label="状态" :value="getStatusText(link.state, isExpired(link.expired))" :borderClass="isExpired(link.expired) ? 'border-red-100' :
                            link.state === 'ACTIVE' ? 'border-emerald-100' : 'border-yellow-100'" :bgClass="isExpired(link.expired) ? 'bg-red-50' :
                                link.state === 'ACTIVE' ? 'bg-emerald-50' : 'bg-yellow-50'" :valueClass="isExpired(link.expired) ? 'text-red-600' :
                                    link.state === 'ACTIVE' ? 'text-emerald-600' : 'text-yellow-600'">
                            <template #icon>
                                <span class="h-2 w-2 rounded-full"
                                    :class="getStatusDot(link.state, isExpired(link.expired))"></span>
                            </template>
                        </InfoField>
                    </div>

                    <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                        <!-- 使用InfoField替换创建时间字段 -->
                        <InfoField label="创建时间" :value="formatDate(link.gmtCreate)" icon="clock"
                            :iconClass="getIconColor(getLinkColorIndex(link, index), 0)" />

                        <!-- 使用InfoField替换修改时间字段 -->
                        <InfoField label="修改时间" :value="formatDate(link.gmtModified)" icon="update"
                            :iconClass="getIconColor(getLinkColorIndex(link, index), 1)" />
                    </div>

                    <!-- 底部操作按钮 -->
                    <template #footer-actions>
                        <!-- 替换为IconActionButton组件，使用light变体 -->
                        <IconActionButton icon="view" variant="light" customClass="border border-gray-300"
                            title="查看详情" />

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
                    </template>
                </LinkCard>
            </div>

            <!-- 无链接数据提示 - 替换为EmptyState组件 -->
            <EmptyState v-if="!filteredLinks.length" title="暂无链接数据" description="该分组下没有链接数据，或者您还没有创建任何链接"
                iconType="blue">
                <template #icon>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M12.586 4.586a2 2 0 112.828 2.828l-7 7a2 2 0 01-2.828 0l-7-7a2 2 0 111.414-1.414L9 14.586l3.586-3.586a2 2 0 013.536 0l3.586 3.586a1 1 0 010 1.414l-3.586 3.586a1 1 0 01-1.414 0l-7-7a1 1 0 010-1.414l7-7a1 1 0 011.414 0zM5 5a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2zM5 19a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2z"
                            clip-rule="evenodd" />
                    </svg>
                </template>
            </EmptyState>

            <!-- 分页区域 -->
            <div class="mt-6 flex justify-center">
                <nav class="flex items-center space-x-1">
                    <button @click="prevPage" :disabled="currentPage <= 1" :class="[
                        'rounded-md border p-2',
                        currentPage <= 1
                            ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-400'
                            : 'border-gray-300 bg-white text-gray-500 hover:bg-gray-50',
                    ]">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
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
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
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
    </PageLayout>

    <!-- 模态框保持不变 -->
    <!-- 使用 BaseModal 组件重构编辑链接模态框 -->
    <BaseModal v-model="showEditLinkModal" title="更新链接信息" id="edit-link-modal">
        <!-- 表单内容 -->
        <form @submit.prevent="updateLink" class="space-y-4">
            <!-- 替换链接标题字段 -->
            <FormField id="edit-link-title" label="链接标题" v-model="editingLink.title" placeholder="请输入链接标题"
                helpText="为您的链接添加一个易于识别的名称" required />

            <!-- 替换选择分组字段 -->
            <FormField id="edit-link-group" label="选择分组" type="select" v-model="editingLink.groupId"
                helpText="选择该链接所属的分组">
                <option v-for="group in groupData" :key="group.id" :value="group.id">
                    {{ group.title }}
                </option>
            </FormField>

            <!-- 替换域名类型字段 -->
            <FormField id="edit-link-domain-type" label="域名类型" type="select" v-model="editingLink.domainType"
                helpText="选择域名类型">
                <option value="OFFICIAL">官方域名</option>
                <option value="CUSTOM">自定义域名</option>
            </FormField>

            <!-- 替换域名字段 -->
            <FormField id="edit-link-domain-id" label="域名" type="select" v-model="editingLink.domainId"
                helpText="选择短链接使用的域名">
                <option :value="1">timu.fun</option>
                <!-- 这里可以根据实际域名列表动态生成选项 -->
            </FormField>

            <!-- 使用FormActions组件替换原有的按钮容器 -->
            <FormActions submitText="保存修改" loadingText="更新中..." :isLoading="isUpdatingLink"
                :disabled="!editingLink.title" @cancel="closeEditLinkModal" />
        </form>
    </BaseModal>

    <!-- 替换原有的删除确认模态框为ConfirmDeleteModal组件 -->
    <ConfirmDeleteModal v-model="showDeleteConfirmModal" title="确认删除短链接" id="delete-confirm-modal"
        message="您确定要删除这个短链接吗？此操作无法撤销，删除后该链接将无法访问。" detailsTitle="链接详情" :isLoading="isDeleting" loadingText="正在删除..."
        @confirm="deleteLink">
        <template #details>
            <p class="mt-1 text-sm text-gray-500">标题: {{ deletingLink.title }}</p>
            <p class="mt-1 text-sm text-gray-500">短链接: {{ deletingLink.domain }}/{{ deletingLink.code }}</p>
        </template>
    </ConfirmDeleteModal>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, reactive } from "vue";
import { GroupData, Data } from "./config";
import BaseModal from "@/components/BaseModal.vue";
import LinkCard from '@/components/LinkCard.vue';
import ConfirmDeleteModal from '@/components/ConfirmDeleteModal.vue';
import EmptyState from '@/components/EmptyState.vue';
import FormField from '@/components/FormField.vue';
import InfoField from '@/components/InfoField.vue';
import FormActions from '@/components/FormActions.vue';
import PageLayout from '@/components/PageLayout.vue';
import IconActionButton from '@/components/IconActionButton.vue';

// 导入颜色方案工具
import {
    getIconColor,
    getActionButtonBg,
    getColorDot,
    getLinkColorIndex,
    getSelectedButtonStyle as getSchemeButtonStyle
} from "@/utils/ColorSchemeProvider";
import { formatDate, isDateExpired } from '@/utils/DateUtils';

// 导入模态框状态管理Hook
import { useModal } from "@/composables/useModal";

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
    return isDateExpired(dateStr);
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


// 添加页面动画效果
onMounted(() => {
    // 默认选择第一个分组
    if (groupData.value.length > 0) {
        selectedGroupId.value = groupData.value[0].id;
    }

    // 初始获取数据
    fetchLinks();
});

// 使用模态框状态管理Hook - 编辑链接模态框
const editLinkModal = useModal({
    id: 0,
    code: "",
    title: "",
    groupId: 0,
    domainId: 0,
    domainType: "OFFICIAL"
});

// 使用模态框状态管理Hook - 删除确认模态框
const deleteLinkModal = useModal({
    id: 0,
    code: "",
    title: "",
    domain: "",
    groupId: 0
});

// 重命名以保持API兼容性
const showEditLinkModal = editLinkModal.isVisible;
const isUpdatingLink = editLinkModal.isLoading;
const editingLink = editLinkModal.formData;

const showDeleteConfirmModal = deleteLinkModal.isVisible;
const isDeleting = deleteLinkModal.isLoading;
const deletingLink = deleteLinkModal.formData;

// 打开编辑链接模态框
const openEditLinkModal = (link: any) => {
    // 设置域名类型
    const domainType = link.domain.includes("timu.fun") ? "OFFICIAL" : "CUSTOM";

    // 打开模态框并传入数据
    editLinkModal.open({
        id: link.id,
        code: link.code,
        title: link.title,
        groupId: link.groupId,
        domainId: 1, // 从域名中提取domainId (示例)
        domainType
    });
};

// 关闭编辑链接模态框
const closeEditLinkModal = () => {
    editLinkModal.close();
};

// 更新链接
const updateLink = async () => {
    if (!editingLink.title) return;

    editLinkModal.startLoading();

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
        editLinkModal.endLoading();
    }
};

// 打开删除确认模态框
const openDeleteConfirmModal = (link: any) => {
    deleteLinkModal.open({
        id: link.id,
        code: link.code,
        title: link.title,
        domain: link.domain,
        groupId: link.groupId
    });
};

// 删除链接
const deleteLink = async () => {
    deleteLinkModal.startLoading();

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
        deleteLinkModal.close();

        // 显示成功提示（这里可以添加一个toast提示）
        console.log("链接删除成功");
    } catch (error) {
        console.error("删除链接失败:", error);
    } finally {
        deleteLinkModal.endLoading();
    }
};
</script>

<style scoped>
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

/* 改善移动端按钮点击区域 */
@media (max-width: 640px) {
    button {
        min-height: 44px;
    }
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
</style>
