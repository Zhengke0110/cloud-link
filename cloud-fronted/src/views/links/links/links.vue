<template>
    <PageLayout tag="短链接管理" title="管理您的短链接" description="查看、编辑和管理您的所有短链接">
        <!-- 分组选择器 -->
        <GroupSelector :groups="groupData" :selectedGroupId="selectedGroupId" @group-select="debouncedSelectGroup" />

        <!-- 链接列表 -->
        <div class="mx-auto max-w-6xl delay-300">
            <!-- 工具栏 -->
            <LinksToolbar @create-click="router.push({ name: 'create' })" />

            <!-- 链接列表 -->
            <LinksList v-if="filteredLinks.length" :links="filteredLinks" :groups="groupData"
                :checkingValidityMap="checkingValidityMap" @edit="openEditLinkModal" @delete="openDeleteConfirmModal"
                @share="handleShare" @copy="handleCopy" @validity-check="checkLinkValidity" @open="openLink" />

            <!-- 无链接数据提示 -->
            <EmptyState v-else title="暂无链接数据" description="该分组下没有链接数据，或者您还没有创建任何链接" iconType="blue">
                <template #icon>
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" viewBox="0 0 20 20"
                        fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M12.586 4.586a2 2 0 112.828 2.828l-7 7a2 2 0 01-2.828 0l-7-7a2 2 0 111.414-1.414L9 14.586l3.586-3.586a2 2 0 013.536 0l3.586 3.586a1 1 0 010 1.414l-3.586 3.586a1 1 0 01-1.414 0l-7-7a1 1 0 010-1.414l7-7a1 1 0 011.414 0zM5 5a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2zM5 19a1 1 0 100-2 1 1 0 000 2zm14 0a1 1 0 100-2 1 1 0 000 2z"
                            clip-rule="evenodd" />
                    </svg>
                </template>
            </EmptyState>

            <!-- 分页组件 -->
            <Pagination v-model:currentPage="currentPage" v-model:pageSize="pageSize" :totalPages="totalPages"
                :totalCount="totalCount" @pageChange="debouncedHandlePageChange"
                @pageSizeChange="debouncedHandlePageSizeChange" class="mt-8" />
        </div>
    </PageLayout>

    <!-- 编辑链接模态框 -->
    <BaseModal v-model="showEditLinkModal" title="更新链接信息" id="edit-link-modal">
        <form @submit.prevent="updateLink" class="space-y-4">
            <FormField id="edit-link-title" label="链接标题" v-model="editingLink.title" placeholder="请输入链接标题"
                helpText="为您的链接添加一个易于识别的名称" required />
            <FormField id="edit-link-group" label="选择分组" type="select" v-model="editingLink.groupId"
                helpText="选择该链接所属的分组">
                <option v-for="group in groupData" :key="group.id" :value="group.id">
                    {{ group.title }}
                </option>
            </FormField>
            <FormField id="edit-link-domain-type" label="域名类型" type="select" v-model="editingLink.domainType"
                helpText="选择域名类型">
                <option value="OFFICIAL">官方域名</option>
                <option value="CUSTOM">自定义域名</option>
            </FormField>
            <FormField id="edit-link-domain-id" label="域名" type="select" v-model="editingLink.domainId"
                helpText="选择短链接使用的域名">
                <option :value="1">timu.fun</option>
            </FormField>
            <FormActions submitText="保存修改" loadingText="更新中..." :isLoading="isUpdatingLink"
                :disabled="!editingLink.title" @cancel="closeEditLinkModal" />
        </form>
    </BaseModal>

    <!-- 删除确认模态框 -->
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
import { ref, computed, onMounted } from "vue";
import { useDebounceFn } from "@vueuse/core";
import { useRouter } from 'vue-router';
import { OpenAPI } from '@/generated';

// 组件导入
import BaseModal from "@/components/BaseModal.vue";
import ConfirmDeleteModal from '../components/ConfirmDeleteModal.vue';
import EmptyState from '@/components/EmptyState.vue';
import FormField from '@/components/Form/FormField.vue';
import FormActions from '@/components/Form/FormActions.vue';
import PageLayout from '@/components/PageLayout.vue';
import Pagination from '@/components/Pagination.vue';
import GroupSelector from './components/GroupSelector.vue';
import LinksToolbar from './components/LinksToolbar.vue';
import LinksList from './components/LinksList.vue';

// API 导入
import { GroupingGetListsApi, LinksGetListsApi, LinkDeleteApi, LinkUpdateApi, LinksCheckApi } from '@/services/links';

// 工具函数导入
import { useModal } from "@/composables/useModal";
import { useToast } from "@/composables/useToast";

const router = useRouter();
const toast = useToast();

// 数据状态管理 - 改为响应式状态，初始值为空
const groupData = ref<any[]>([]);
const linkData = ref<any[]>([]);
const totalPages = ref(0);
const totalCount = ref(0);

// 加载状态管理
const isLoadingGroups = ref(false);
const isLoadingLinks = ref(false);

// 当前选中的分组ID
const selectedGroupId = ref<string | null>(null);

// 分页相关参数
const currentPage = ref(1);
const pageSize = ref(20);

// 直接使用过滤后的链接数据，不需要再做本地分页
const filteredLinks = computed(() => {
    return linkData.value;
});

// 加载分组数据
const fetchGroups = async () => {
    if (isLoadingGroups.value) return;

    isLoadingGroups.value = true;

    try {
        groupData.value = await GroupingGetListsApi()
        // 默认选择第一个分组
        if (groupData.value.length > 0 && selectedGroupId.value === null) {
            selectedGroupId.value = groupData.value[0].id;
        }
    } catch (error) {
        console.error("加载分组数据失败:", error);
        toast.error("加载分组数据失败，请刷新页面重试", {
            title: "加载失败"
        });
    } finally {
        isLoadingGroups.value = false;
    }
};

// 加载链接数据
const fetchLinks = async () => {
    if (isLoadingLinks.value) return;
    isLoadingLinks.value = true;

    // 显示加载中通知
    const loadingToastId = toast.info("正在加载链接数据...", {
        duration: 0, // 不自动关闭
        title: "加载中"
    });

    try {
        // 构建API请求参数
        const params = {
            groupId: selectedGroupId.value !== null ? String(selectedGroupId.value) : '',
            page: currentPage.value,
            size: pageSize.value,
        };

        const response = await LinksGetListsApi(params);
        totalCount.value = response.total_record || 0;
        totalPages.value = response.total_page || 0;
        linkData.value = response.current_data || [];
        console.log("加载链接数据成功:", {
            totalCount: totalCount.value,
            totalPages: totalPages.value,
            currentData: linkData.value
        });
        // 移除加载中通知
        toast.removeToast(loadingToastId);

        // 显示成功通知
        toast.success("链接数据加载成功", {
            title: "数据已更新",
            duration: 2000
        });
    } catch (error) {
        console.error("加载链接数据失败:", error);

        // 移除加载中通知
        toast.removeToast(loadingToastId);

        // 显示错误通知
        toast.error("加载链接数据失败，请重试", {
            title: "加载失败"
        });
    } finally {
        isLoadingLinks.value = false;
    }
};

// 初始化数据加载
const initializeData = async () => {
    // 先加载分组数据
    await fetchGroups();

    // 再加载链接数据
    if (selectedGroupId.value !== null) {
        await fetchLinks();
    }
};

// 选择分组
const selectGroup = (groupId: string | null) => {
    selectedGroupId.value = groupId;
    // 切换分组时重置页码
    currentPage.value = 1;
    // 调用获取数据的方法
    fetchLinks();
};

// 使用防抖优化分组选择，防止用户快速点击时重复请求
const debouncedSelectGroup = useDebounceFn(selectGroup, 300);

// 简化分页处理函数
const handlePageChange = (page: number) => {
    currentPage.value = page;
    fetchLinks();
};

const handlePageSizeChange = (size: number) => {
    pageSize.value = size;
    currentPage.value = 1; // 重置到第一页
    fetchLinks();
};

// 使用防抖优化分页操作，防止用户快速点击时重复请求
const debouncedHandlePageChange = useDebounceFn(handlePageChange, 300);
const debouncedHandlePageSizeChange = useDebounceFn(handlePageSizeChange, 300);


// 添加有效性检查状态管理
const checkingValidityMap = ref<Record<string, boolean>>({});

onMounted(() => initializeData());

// 使用模态框状态管理Hook - 编辑链接模态框
const editLinkModal = useModal({
    id: "",  // 改为 string 类型
    groupId: "",
    code: "",
    title: "",
    domainId: 0,
    domainType: "OFFICIAL"
});

// 使用模态框状态管理Hook - 删除确认模态框
const deleteLinkModal = useModal({
    id: "",  // 改为 string 类型
    code: "",
    title: "",
    domain: "",
    groupId: ""  // 确保为 string 类型
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

    const modalData = {
        id: String(link.id),
        groupId: String(link.groupId),
        code: link.code,
        title: link.title,
        domainId: 1, // 从域名中提取domainId (示例)
        domainType
    };
    // 打开模态框并传入数据，确保所有字段类型正确
    editLinkModal.open(modalData);
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
        // 构建请求参数 - 强制确保类型正确
        const params = {
            groupId: String(editingLink.groupId),
            code: String(editingLink.code),
            title: String(editingLink.title),
            domainId: String(editingLink.domainId),
            domainType: String(editingLink.domainType),
        };
        console.log("更新链接参数:", params);
        await LinkUpdateApi(params);

        setTimeout(async () => {
            // 关闭模态框
            closeEditLinkModal();

            // 使用Toast通知
            toast.success("链接更新成功", { title: "操作成功" });

            // 重新加载链接数据
            await fetchLinks();
        }, 1000);
    } catch (error) {
        console.error("更新链接失败:", error);
        // 显示错误通知
        toast.error("更新链接失败，请重试", { title: "操作失败" });
    } finally {
        editLinkModal.endLoading();
    }
};

// 打开删除确认模态框
const openDeleteConfirmModal = (link: any) => {
    const modalData = {
        id: String(link.id),
        code: link.code,
        title: link.title,
        domain: link.domain,
        groupId: String(link.groupId)
    };
    deleteLinkModal.open(modalData);
};

// 删除链接
const deleteLink = async () => {
    deleteLinkModal.startLoading();

    try {
        // 构建请求参数 - 再次确保类型正确
        const params = {
            groupId: String(deletingLink.groupId),
            code: String(deletingLink.code),
        };
        await LinkDeleteApi(params);


        // 重新加载链接数据
        setTimeout(async () => {
            // 关闭模态框
            deleteLinkModal.close();
            // 使用Toast通知
            toast.success("链接删除成功", { title: "操作成功" });
            await fetchLinks();
        }, 500);
    } catch (error) {
        console.error("删除链接失败:", error);
        // 显示错误通知
        toast.error("删除链接失败，请重试", { title: "操作失败" });
    } finally {
        deleteLinkModal.endLoading();
    }
};

// 检查短链接有效性
const checkLinkValidity = async (link: any) => {
    const shortLinkCode = link.code;

    // 防止重复检查
    if (checkingValidityMap.value[shortLinkCode]) {
        return;
    }

    // 设置检查状态
    checkingValidityMap.value[shortLinkCode] = true;

    // 显示检查中的通知
    const checkingToastId = toast.info(`正在检查短链接 ${shortLinkCode} 的有效性...`, {
        duration: 0, // 不自动关闭
        title: "有效性检查"
    });

    try {
        const response = await LinksCheckApi(shortLinkCode);
        console.log("短链接有效性检查结果:", {
            code: shortLinkCode,
            response
        });
        // 移除检查中通知
        toast.removeToast(checkingToastId);

        // // 根据返回结果显示不同的通知
        if (response) {
            // 有效
            toast.success(`短链接 ${shortLinkCode} 有效且可正常访问`, {
                title: "检查通过",
                duration: 3000
            });
        }

    } catch (error) {
        // 移除检查中通知
        toast.removeToast(checkingToastId);

        // 显示错误通知
        toast.error(`检查短链接 ${shortLinkCode} 失败,${error}`, {
            title: "检查异常"
        });
    } finally {
        // 清除检查状态
        checkingValidityMap.value[shortLinkCode] = false;
    }
};

// 打开短链接
const openLink = (code: string) => {
    const url = `${OpenAPI.BASE}/${code}`;
    window.open(url, "_blank");
};

// 新增的事件处理函数
const handleShare = (link: any) => {
    router.push({ name: 'analysis', query: { code: link.code } });
};

const handleCopy = (link: any) => {
    const shortUrl = `${link.domain}/${link.code}`;
    navigator.clipboard.writeText(shortUrl).then(() => {
        toast.success('链接已复制到剪贴板', { title: '复制成功' });
    }).catch(() => {
        toast.error('复制失败，请手动复制', { title: '复制失败' });
    });
};

onMounted(() => initializeData());
</script>
