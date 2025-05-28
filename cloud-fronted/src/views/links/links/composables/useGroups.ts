import { ref } from 'vue'
import { useDebounceFn } from '@vueuse/core'
import { GroupingGetListsApi } from '@/services/links'
import { useToast } from '@/composables/useToast'

export const useGroups = () => {
    const toast = useToast()
    
    // 数据状态
    const groupData = ref<any[]>([])
    const selectedGroupId = ref<string | null>(null)
    const isLoadingGroups = ref(false)
    
    // 加载分组数据
    const fetchGroups = async () => {
        if (isLoadingGroups.value) return

        isLoadingGroups.value = true

        try {
            groupData.value = await GroupingGetListsApi()
            // 默认选择第一个分组
            if (groupData.value.length > 0 && selectedGroupId.value === null) {
                selectedGroupId.value = groupData.value[0].id
            }
        } catch (error) {
            console.error("加载分组数据失败:", error)
            toast.error("加载分组数据失败，请刷新页面重试", {
                title: "加载失败"
            })
        } finally {
            isLoadingGroups.value = false
        }
    }
    
    // 选择分组
    const selectGroup = (groupId: string | null, onGroupChange?: (groupId: string | null) => void) => {
        selectedGroupId.value = groupId
        onGroupChange?.(groupId)
    }

    // 防抖优化的分组选择
    const debouncedSelectGroup = useDebounceFn(selectGroup, 300)

    return {
        // 数据
        groupData,
        selectedGroupId,
        isLoadingGroups,
        
        // 方法
        fetchGroups,
        selectGroup,
        debouncedSelectGroup
    }
}
