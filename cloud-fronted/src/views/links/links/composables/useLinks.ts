import { ref, computed } from 'vue'
import { useDebounceFn } from '@vueuse/core'
import { LinksGetListsApi, LinkDeleteApi, LinkUpdateApi, LinksCheckApi } from '@/services/links'
import { useToast } from '@/composables/useToast'
import { useModal } from '@/composables/useModal'
import { OpenAPI } from '@/generated'

export const useLinks = () => {
    const toast = useToast()
    
    // 数据状态
    const linkData = ref<any[]>([])
    const totalPages = ref(0)
    const totalCount = ref(0)
    const isLoadingLinks = ref(false)
    const checkingValidityMap = ref<Record<string, boolean>>({})
    
    // 分页参数
    const currentPage = ref(1)
    const pageSize = ref(20)
    
    // 过滤后的链接
    const filteredLinks = computed(() => linkData.value)
    
    // 加载链接数据
    const fetchLinks = async (selectedGroupId: string | null) => {
        if (isLoadingLinks.value) return
        isLoadingLinks.value = true

        const loadingToastId = toast.info("正在加载链接数据...", {
            duration: 0,
            title: "加载中"
        })

        try {
            const params = {
                groupId: selectedGroupId !== null ? String(selectedGroupId) : '',
                page: currentPage.value,
                size: pageSize.value,
            }

            const response = await LinksGetListsApi(params)
            totalCount.value = response.total_record || 0
            totalPages.value = response.total_page || 0
            linkData.value = response.current_data || []

            toast.removeToast(loadingToastId)
            toast.success("链接数据加载成功", {
                title: "数据已更新",
                duration: 2000
            })
        } catch (error) {
            console.error("加载链接数据失败:", error)
            toast.removeToast(loadingToastId)
            toast.error("加载链接数据失败，请重试", {
                title: "加载失败"
            })
        } finally {
            isLoadingLinks.value = false
        }
    }
    
    // 分页处理
    const handlePageChange = (page: number, selectedGroupId: string | null) => {
        currentPage.value = page
        fetchLinks(selectedGroupId)
    }

    const handlePageSizeChange = (size: number, selectedGroupId: string | null) => {
        pageSize.value = size
        currentPage.value = 1
        fetchLinks(selectedGroupId)
    }

    const debouncedHandlePageChange = useDebounceFn(handlePageChange, 300)
    const debouncedHandlePageSizeChange = useDebounceFn(handlePageSizeChange, 300)
    
    // 编辑链接模态框
    const editLinkModal = useModal({
        id: "",
        groupId: "",
        code: "",
        title: "",
        domainId: 0,
        domainType: "OFFICIAL"
    })
    
    // 删除链接模态框
    const deleteLinkModal = useModal({
        id: "",
        code: "",
        title: "",
        domain: "",
        groupId: ""
    })
    
    // 更新链接
    const updateLink = async (selectedGroupId: string | null) => {
        if (!editLinkModal.formData.title) return

        editLinkModal.startLoading()

        try {
            const params = {
                groupId: String(editLinkModal.formData.groupId),
                code: String(editLinkModal.formData.code),
                title: String(editLinkModal.formData.title),
                domainId: String(editLinkModal.formData.domainId),
                domainType: String(editLinkModal.formData.domainType),
            }
            
            await LinkUpdateApi(params)

            setTimeout(async () => {
                editLinkModal.close()
                toast.success("链接更新成功", { title: "操作成功" })
                await fetchLinks(selectedGroupId)
            }, 1000)
        } catch (error) {
            console.error("更新链接失败:", error)
            toast.error("更新链接失败，请重试", { title: "操作失败" })
        } finally {
            editLinkModal.endLoading()
        }
    }
    
    // 删除链接
    const deleteLink = async (selectedGroupId: string | null) => {
        deleteLinkModal.startLoading()

        try {
            const params = {
                groupId: String(deleteLinkModal.formData.groupId),
                code: String(deleteLinkModal.formData.code),
            }
            await LinkDeleteApi(params)

            setTimeout(async () => {
                deleteLinkModal.close()
                toast.success("链接删除成功", { title: "操作成功" })
                await fetchLinks(selectedGroupId)
            }, 500)
        } catch (error) {
            console.error("删除链接失败:", error)
            toast.error("删除链接失败，请重试", { title: "操作失败" })
        } finally {
            deleteLinkModal.endLoading()
        }
    }
    
    // 检查链接有效性
    const checkLinkValidity = async (link: any) => {
        const shortLinkCode = link.code

        if (checkingValidityMap.value[shortLinkCode]) return

        checkingValidityMap.value[shortLinkCode] = true

        const checkingToastId = toast.info(`正在检查短链接 ${shortLinkCode} 的有效性...`, {
            duration: 0,
            title: "有效性检查"
        })

        try {
            const response = await LinksCheckApi(shortLinkCode)
            toast.removeToast(checkingToastId)

            if (response) {
                toast.success(`短链接 ${shortLinkCode} 有效且可正常访问`, {
                    title: "检查通过",
                    duration: 3000
                })
            }
        } catch (error) {
            toast.removeToast(checkingToastId)
            toast.error(`检查短链接 ${shortLinkCode} 失败,${error}`, {
                title: "检查异常"
            })
        } finally {
            checkingValidityMap.value[shortLinkCode] = false
        }
    }
    
    // 打开链接
    const openLink = (code: string) => {
        const url = `${OpenAPI.BASE}/${code}`
        window.open(url, "_blank")
    }
    
    // 打开编辑模态框
    const openEditLinkModal = (link: any) => {
        const domainType = link.domain.includes("timu.fun") ? "OFFICIAL" : "CUSTOM"
        const modalData = {
            id: String(link.id),
            groupId: String(link.groupId),
            code: link.code,
            title: link.title,
            domainId: 1,
            domainType
        }
        editLinkModal.open(modalData)
    }
    
    // 打开删除模态框
    const openDeleteConfirmModal = (link: any) => {
        const modalData = {
            id: String(link.id),
            code: link.code,
            title: link.title,
            domain: link.domain,
            groupId: String(link.groupId)
        }
        deleteLinkModal.open(modalData)
    }

    return {
        // 数据
        linkData,
        filteredLinks,
        totalPages,
        totalCount,
        currentPage,
        pageSize,
        isLoadingLinks,
        checkingValidityMap,
        
        // 模态框
        editLinkModal,
        deleteLinkModal,
        
        // 方法
        fetchLinks,
        debouncedHandlePageChange,
        debouncedHandlePageSizeChange,
        updateLink,
        deleteLink,
        checkLinkValidity,
        openLink,
        openEditLinkModal,
        openDeleteConfirmModal
    }
}
