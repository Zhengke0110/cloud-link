<template>
    <PageLayout tag="链接分组管理" title="管理您的链接分组" description="组织和管理您的短链接，提高工作效率">
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
                    创建分组
                </button>
            </div>

            <!-- 分组卡片 - 使用LinkCard组件替换 -->
            <div class="space-y-4 md:space-y-6">
                <LinkCard v-for="(group, index) in groupData" :key="group.id" :title="group.title" :colorIndex="index">
                    <!-- 顶部操作按钮 -->
                    <template #header-actions>
                        <span class="text-sm text-white/80">操作</span>
                        <div class="flex space-x-1">
                            <!-- 替换为IconActionButton组件 -->
                            <IconActionButton icon="edit" @click="openEditModal(group)" title="编辑分组" />

                            <!-- 替换为IconActionButton组件，并设置为危险类型 -->
                            <IconActionButton icon="delete" type="danger" @click="openDeleteConfirmModal(group)"
                                title="删除分组" />
                        </div>
                    </template>

                    <!-- 分组内容 -->
                    <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
                        <!-- 使用 InfoField 替换原有的分组ID字段 -->
                        <InfoField label="分组ID" :value="group.id" breakAll />

                        <!-- 使用 InfoField 替换原有的账号字段 -->
                        <InfoField label="账号" :value="group.accountNo" />
                    </div>

                    <div class="mt-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                        <!-- 使用 InfoField 替换原有的创建时间字段 -->
                        <InfoField label="创建时间" :value="formatDate(group.gmtCreate)" icon="clock"
                            :iconClass="getIconColor(index, 0)" />

                        <!-- 使用 InfoField 替换原有的修改时间字段 -->
                        <InfoField label="修改时间" :value="formatDate(group.gmtModified)" icon="update"
                            :iconClass="getIconColor(index, 1)" />
                    </div>

                    <!-- 分组操作按钮 -->
                    <template #footer-actions>
                        <!-- 替换为IconActionButton组件，使用light变体 -->
                        <IconActionButton icon="share" variant="light" customClass="border border-gray-300"
                            title="分享分组" />

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
                    </template>
                </LinkCard>
            </div>

            <!-- 替换现有的无分组数据提示 -->
            <EmptyState v-if="!groupData.length" title="暂无分组数据" description="您还没有创建任何分组，点击上方按钮创建新分组">
                <!-- 使用默认文件夹图标 -->
            </EmptyState>
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
    </PageLayout>

    <!-- 模态框部分保持不变，移动到PageLayout外部 -->
    <!-- 创建分组模态框 -->
    <!-- 使用 BaseModal 组件重构创建分组模态框 -->
    <BaseModal v-model="showCreateModal" title="创建新分组" id="create-group-modal">
        <!-- 表单内容 -->
        <form @submit.prevent="createGroup" class="space-y-4">
            <!-- 替换分组名称字段为FormField组件 -->
            <FormField id="group-title" label="分组名称" v-model="newGroup.title" placeholder="请输入分组名称"
                helpText="为您的分组添加一个易于识别的名称" required>
                <!-- 随机生成按钮作为后缀 -->
                <template #suffix>
                    <button type="button" v-if="newGroup.title" @click="generateRandomGroupName"
                        class="absolute top-1/2 right-3 -translate-y-1/2 rounded-full p-1 text-gray-400 hover:bg-indigo-50 hover:text-indigo-600"
                        title="生成随机名称">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                clip-rule="evenodd" />
                        </svg>
                    </button>
                </template>
            </FormField>

            <!-- 替换提交按钮容器为FormActions组件 -->
            <FormActions submitText="创建分组" loadingText="创建中..." :isLoading="isCreating" :disabled="!newGroup.title"
                @cancel="closeCreateModal" />
        </form>
    </BaseModal>

    <!-- 使用 BaseModal 组件重构编辑分组模态框 -->
    <BaseModal v-model="showEditModal" title="编辑分组信息" id="edit-group-modal">
        <!-- 表单内容 -->
        <form @submit.prevent="updateGroup" class="space-y-4">
            <!-- 替换分组名称字段为FormField组件 -->
            <FormField id="edit-group-title" label="分组名称" v-model="editingGroup.title" placeholder="请输入分组名称" required>
                <!-- 随机生成按钮作为后缀 -->
                <template #suffix>
                    <button type="button" @click="generateRandomGroupNameForEdit"
                        class="absolute top-1/2 right-3 -translate-y-1/2 rounded-full p-1 text-gray-400 hover:bg-indigo-50 hover:text-indigo-600"
                        title="生成随机名称">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                                clip-rule="evenodd" />
                        </svg>
                    </button>
                </template>

                <!-- 使用额外信息插槽显示ID -->
                <template #extra>
                    <span class="ml-1 text-gray-400">ID: {{ editingGroup.id }}</span>
                </template>

                <!-- 帮助文本 -->
                <template #help>
                    <span>为您的分组添加一个易于识别的名称</span>
                </template>
            </FormField>

            <!-- 替换提交按钮容器为FormActions组件 -->
            <FormActions submitText="保存修改" loadingText="更新中..." :isLoading="isUpdating" :disabled="!editingGroup.title"
                @cancel="closeEditModal" />
        </form>
    </BaseModal>

    <!-- 替换为ConfirmDeleteModal组件 -->
    <ConfirmDeleteModal v-model="showDeleteConfirmModal" title="确认删除分组" id="delete-group-modal"
        message="您确定要删除这个分组吗？此操作无法撤销，删除后该分组中的所有链接将移至默认分组。" detailsTitle="分组详情" :isLoading="isDeleting"
        loadingText="正在删除..." @confirm="deleteGroup">
        <template #details>
            <p class="mt-1 text-sm text-gray-500">名称: {{ deletingGroup.title }}</p>
            <p class="mt-1 text-sm text-gray-500">ID: {{ deletingGroup.id }}</p>
        </template>
    </ConfirmDeleteModal>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Data } from "./config";
import BaseModal from "@/components/BaseModal.vue";
import LinkCard from "@/components/LinkCard.vue";
import ConfirmDeleteModal from "../components/ConfirmDeleteModal.vue";
import EmptyState from "@/components/EmptyState.vue";
import FormField from "@/components/FormField.vue";
import InfoField from "@/components/InfoField.vue";
import FormActions from "@/components/FormActions.vue";
import PageLayout from "@/components/PageLayout.vue";
import IconActionButton from '@/components/IconActionButton.vue';
// 导入日期工具函数
import { formatDate } from "@/utils/DateUtils";
// 导入颜色方案工具
import { getIconColor, getActionButtonBg } from "@/utils/ColorSchemeProvider";
// 导入模态框状态管理Hook
import { useModal } from "@/composables/useModal";
// 导入Toast通知系统
import { useToast } from "@/composables/useToast";

// 分组数据
const groupData = ref(Data);

// 初始化Toast
const toast = useToast();

// 使用模态框状态管理Hook - 创建分组模态框
const createGroupModal = useModal({
    title: ""
});

// 使用模态框状态管理Hook - 编辑分组模态框
const editGroupModal = useModal({
    id: 0,
    title: ""
});

// 使用模态框状态管理Hook - 删除确认模态框
const deleteGroupModal = useModal({
    id: 0,
    title: ""
});

// 重命名以保持API兼容性
const showCreateModal = createGroupModal.isVisible;
const isCreating = createGroupModal.isLoading;
const newGroup = createGroupModal.formData;

const showEditModal = editGroupModal.isVisible;
const isUpdating = editGroupModal.isLoading;
const editingGroup = editGroupModal.formData;

const showDeleteConfirmModal = deleteGroupModal.isVisible;
const isDeleting = deleteGroupModal.isLoading;
const deletingGroup = deleteGroupModal.formData;

// 打开创建模态框
const openCreateModal = () => {
    // 设置初始随机名称并打开模态框
    const randomName = `我是测试分组-${generateRandomNumber(1, 100)}`;
    createGroupModal.open({ title: randomName });
};

// 关闭创建模态框
const closeCreateModal = () => {
    createGroupModal.close();
};

// 打开编辑模态框
const openEditModal = (group: any) => {
    editGroupModal.open({
        id: group.id,
        title: group.title
    });
};

// 关闭编辑模态框
const closeEditModal = () => {
    editGroupModal.close();
};

// 打开删除确认模态框
const openDeleteConfirmModal = (group: any) => {
    deleteGroupModal.open({
        id: group.id,
        title: group.title
    });
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

    createGroupModal.startLoading();

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
        createGroupModal.resetForm();

        // 使用Toast通知替换console.log
        toast.success("分组创建成功", { title: "操作成功" });
    } catch (error) {
        console.error("创建分组失败:", error);
        // 显示错误通知
        toast.error("创建分组失败，请重试", { title: "操作失败" });
    } finally {
        createGroupModal.endLoading();
    }
};

// 更新分组
const updateGroup = async () => {
    if (!editingGroup.title) return;

    editGroupModal.startLoading();

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

        // 使用Toast通知替换console.log
        toast.success("分组更新成功", { title: "操作成功" });
    } catch (error) {
        console.error("更新分组失败:", error);
        // 显示错误通知
        toast.error("更新分组失败，请重试", { title: "操作失败" });
    } finally {
        editGroupModal.endLoading();
    }
};

// 删除分组
const deleteGroup = async () => {
    deleteGroupModal.startLoading();

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
        deleteGroupModal.close();

        // 使用Toast通知替换console.log
        toast.success("分组删除成功", { title: "操作成功" });
    } catch (error) {
        console.error("删除分组失败:", error);
        // 显示错误通知
        toast.error("删除分组失败，请重试", { title: "操作失败" });
    } finally {
        deleteGroupModal.endLoading();
    }
};

// 添加页面动画效果
onMounted(() => {
    // 只保留业务逻辑相关的初始化操作，移除动画初始化
    // 原有的动画初始化已经被PageLayout组件接管
});
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

/* 改善移动端按钮点击区域 */
@media (max-width: 640px) {
    button {
        min-height: 44px;
    }
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
