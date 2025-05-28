<template>
    <div class="mx-auto mb-8 max-w-6xl delay-300">
        <div class="rounded-xl bg-white p-6 shadow-md border border-gray-100">
            <div class="mb-4">
                <h3 class="text-lg font-semibold text-gray-800 flex items-center">
                    <svg class="w-5 h-5 text-blue-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path
                            d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zM3 10a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H4a1 1 0 01-1-1v-6zM14 9a1 1 0 00-1 1v6a1 1 0 001 1h2a1 1 0 001-1v-6a1 1 0 00-1-1h-2z" />
                    </svg>
                    选择分组
                </h3>
                <p class="text-sm text-gray-500 mt-1">点击分组标签查看对应的短链接</p>
            </div>

            <div class="flex flex-wrap gap-3">
                <button 
                    v-for="group in groups" 
                    :key="group.id" 
                    @click="handleGroupSelect(group.id)"
                    class="flex items-center gap-2 rounded-lg px-4 py-2.5 text-sm transition-all duration-300 shadow-sm hover:shadow-md"
                    :class="selectedGroupId === group.id
                        ? getSelectedButtonStyle(group.id)
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200 border border-gray-200'"
                >
                    <span class="h-2.5 w-2.5 rounded-full" :class="getColorDot(getGroupIndex(group.id))"></span>
                    {{ group.title }}
                    <span v-if="selectedGroupId === group.id" class="ml-1">
                        <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                clip-rule="evenodd" />
                        </svg>
                    </span>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { getColorDot, getSelectedButtonStyle as getSchemeButtonStyle } from "@/utils/ColorSchemeProvider";

interface Group {
    id: string;
    title: string;
}

interface Props {
    groups: Group[];
    selectedGroupId: string | null;
}

interface Emits {
    (e: 'group-select', groupId: string): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const handleGroupSelect = (groupId: string) => {
    emit('group-select', groupId);
};

const getGroupIndex = (groupId: string) => {
    const index = props.groups.findIndex((g) => g.id === groupId);
    return index >= 0 ? index : 0;
};

const getSelectedButtonStyle = (groupId: string) => {
    const index = getGroupIndex(groupId);
    return getSchemeButtonStyle(index);
};
</script>
