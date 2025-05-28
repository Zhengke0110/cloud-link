import { ref, computed } from 'vue';
import { useDebounceFn } from "@vueuse/core";
import { useModal } from "@/composables/useModal";
import { useToast } from "@/composables/useToast";
import { GroupingGetListsApi, LinksGetListsApi, LinkDeleteApi, LinkUpdateApi, LinksCheckApi } from '@/services/links';
import { OpenAPI } from '@/generated';

export function useLinksManagement() {
    // 数据状态
    const groupData = ref<any[]>([]);
    const linkData = ref<any[]>([]);
    const totalPages = ref(0);
    const totalCount = ref(0);
    const selectedGroupId = ref<string | null>(null);
    const currentPage = ref(1);
    const pageSize = ref(20);

    // 加载状态
    const isLoadingGroups = ref(false);
    const isLoadingLinks = ref(false);
    const checkingValidityMap = ref<Record<string, boolean>>({});

    // Toast实例
    const toast = useToast();

    // 模态框状态
    const editLinkModal = useModal({
        id: "",
        groupId: "",
        code: "",
        title: "",
        domainId: 0,
        domainType: "OFFICIAL"
    });

    const deleteLinkModal = useModal({
        id: "",
        code: "",
        title: "",
        domain: "",
        groupId: ""
    });

    // 计算属性
    const filteredLinks = computed(() => linkData.value);

    // 加载分组数据
    const fetchGroups = async () => {
        if (isLoadingGroups.value) return;
        isLoadingGroups.value = true;

        try {
            groupData.value = await GroupingGetListsApi();
            if (groupData.value.length > 0 && selectedGroupId.value === null) {
                selectedGroupId.value = groupData.value[0].id;
            }
        } catch (error) {
            console.error("加载分组数据失败:", error);
            toast.error("加载分组数据失败，请刷新页面重试", { title: "加载失败" });
        } finally {
            isLoadingGroups.value = false;
        }
    };

    // 加载链接数据
    const fetchLinks = async () => {
        if (isLoadingLinks.value) return;
        isLoadingLinks.value = true;

        const loadingToastId = toast.info("正在加载链接数据...", {
            duration: 0,
            title: "加载中"
        });

        try {
            const params = {
                groupId: selectedGroupId.value !== null ? String(selectedGroupId.value) : '',
                page: currentPage.value,
                size: pageSize.value,
            };

            const response = await LinksGetListsApi(params);
            totalCount.value = response.total_record || 0;
            totalPages.value = response.total_page || 0;
            linkData.value = response.current_data || [];

            toast.removeToast(loadingToastId);
            toast.success("链接数据加载成功", {
                title: "数据已更新",
                duration: 2000
            });
        } catch (error) {
            console.error("加载链接数据失败:", error);
            toast.removeToast(loadingToastId);
            toast.error("加载链接数据失败，请重试", { title: "加载失败" });
        } finally {
            isLoadingLinks.value = false;
        }
    };

    // 选择分组
    const selectGroup = (groupId: string | null) => {
        selectedGroupId.value = groupId;
        currentPage.value = 1;
        fetchLinks();
    };

    // 分页处理
    const handlePageChange = (page: number) => {
        currentPage.value = page;
        fetchLinks();
    };

    const handlePageSizeChange = (size: number) => {
        pageSize.value = size;
        currentPage.value = 1;
        fetchLinks();
    };

    // 打开编辑模态框
    const openEditLinkModal = (link: any) => {
        const domainType = link.domain.includes("timu.fun") ? "OFFICIAL" : "CUSTOM";
        const modalData = {
            id: String(link.id),
            groupId: String(link.groupId),
            code: link.code,
            title: link.title,
            domainId: 1,
            domainType
        };
        editLinkModal.open(modalData);
    };

    // 更新链接
    const updateLink = async () => {
        if (!editLinkModal.formData.title) return;

        editLinkModal.startLoading();

        try {
            const params = {
                groupId: String(editLinkModal.formData.groupId),
                code: String(editLinkModal.formData.code),
                title: String(editLinkModal.formData.title),
                domainId: String(editLinkModal.formData.domainId),
                domainType: String(editLinkModal.formData.domainType),
            };

            await LinkUpdateApi(params);

            setTimeout(async () => {
                editLinkModal.close();
                toast.success("链接更新成功", { title: "操作成功" });
                await fetchLinks();
            }, 1000);
        } catch (error) {
            console.error("更新链接失败:", error);
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
            const params = {
                groupId: String(deleteLinkModal.formData.groupId),
                code: String(deleteLinkModal.formData.code),
            };
            await LinkDeleteApi(params);

            setTimeout(async () => {
                deleteLinkModal.close();
                toast.success("链接删除成功", { title: "操作成功" });
                await fetchLinks();
            }, 500);
        } catch (error) {
            console.error("删除链接失败:", error);
            toast.error("删除链接失败，请重试", { title: "操作失败" });
        } finally {
            deleteLinkModal.endLoading();
        }
    };

    // 检查链接有效性
    const checkLinkValidity = async (link: any) => {
        const shortLinkCode = link.code;

        if (checkingValidityMap.value[shortLinkCode]) {
            return;
        }

        checkingValidityMap.value[shortLinkCode] = true;
        const checkingToastId = toast.info(`正在检查短链接 ${shortLinkCode} 的有效性...`, {
            duration: 0,
            title: "有效性检查"
        });

        try {
            const response = await LinksCheckApi(shortLinkCode);
            toast.removeToast(checkingToastId);

            if (response) {
                toast.success(`短链接 ${shortLinkCode} 有效且可正常访问`, {
                    title: "检查通过",
                    duration: 3000
                });
            }
        } catch (error) {
            toast.removeToast(checkingToastId);
            toast.error(`检查短链接 ${shortLinkCode} 失败,${error}`, {
                title: "检查异常"
            });
        } finally {
            checkingValidityMap.value[shortLinkCode] = false;
        }
    };

    // 打开链接
    const openLink = (code: string) => {
        const url = `${OpenAPI.BASE}/${code}`;
        window.open(url, "_blank");
    };

    // 初始化数据
    const initializeData = async () => {
        await fetchGroups();
        if (selectedGroupId.value !== null) {
            await fetchLinks();
        }
    };

    // 防抖函数
    const debouncedSelectGroup = useDebounceFn(selectGroup, 300);
    const debouncedHandlePageChange = useDebounceFn(handlePageChange, 300);
    const debouncedHandlePageSizeChange = useDebounceFn(handlePageSizeChange, 300);

    return {
        // 数据
        groupData,
        linkData,
        filteredLinks,
        totalPages,
        totalCount,
        selectedGroupId,
        currentPage,
        pageSize,
        checkingValidityMap,

        // 加载状态
        isLoadingGroups,
        isLoadingLinks,

        // 模态框
        editLinkModal,
        deleteLinkModal,

        // 方法
        initializeData,
        debouncedSelectGroup,
        debouncedHandlePageChange,
        debouncedHandlePageSizeChange,
        openEditLinkModal,
        updateLink,
        openDeleteConfirmModal,
        deleteLink,
        checkLinkValidity,
        openLink,
    };
}
