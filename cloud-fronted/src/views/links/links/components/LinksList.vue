<template>
    <div class="space-y-4">
        <LinkCard v-for="(link, index) in links" :key="link.id" :title="link.title"
            :colorIndex="getLinkColorIndex(link, index)">
            <!-- 顶部操作按钮 -->
            <template #header-actions>
                <span class="text-sm text-white/80">操作</span>
                <div class="flex space-x-1">
                    <IconActionButton icon="edit" @click="handleEdit(link)" title="编辑链接" />
                    <IconActionButton icon="delete" type="danger" @click="handleDelete(link)" title="删除链接" />
                    <IconActionButton icon="share" @click="handleShare(link)" title="分享链接" />
                </div>
            </template>

            <!-- 链接内容 -->
            <div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
                <InfoField label="原始链接" :value="getOriginalUrl(link.originalUrl)" breakAll />
                <InfoField label="短链接" valueClass="text-indigo-600" borderClass="border-indigo-100"
                    bgClass="bg-indigo-50">
                    <div class="flex items-center justify-between w-full">
                        <span>{{ `${link.domain}/${link.code}` }}</span>
                        <IconActionButton icon="copy" variant="light" size="sm" title="复制链接"
                            customClass="text-indigo-600 hover:bg-indigo-100" @click="handleCopy(link)" />
                    </div>
                </InfoField>
            </div>

            <div class="mb-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
                <InfoField label="分组">
                    <div class="flex items-center gap-2 w-full">
                        <span class="h-2 w-2 rounded-full" :class="getColorDot(getGroupIndex(link.groupId))"></span>
                        {{ getGroupTitle(link.groupId) }}
                    </div>
                </InfoField>
                <InfoField label="过期时间" :value="formatDate(link.expired)" icon="clock"
                    :valueClass="isExpired(link.expired) ? 'text-red-600' : 'text-gray-800'" />
                <InfoField label="状态" :value="getStatusText(link.state, isExpired(link.expired))"
                    :borderClass="getStatusBorderClass(link.state, isExpired(link.expired))"
                    :bgClass="getStatusBgClass(link.state, isExpired(link.expired))"
                    :valueClass="getStatusValueClass(link.state, isExpired(link.expired))">
                    <template #icon>
                        <span class="h-2 w-2 rounded-full"
                            :class="getStatusDot(link.state, isExpired(link.expired))"></span>
                    </template>
                </InfoField>
            </div>

            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <InfoField label="创建时间" :value="formatDate(link.gmtCreate)" icon="clock"
                    :iconClass="getIconColor(getLinkColorIndex(link, index), 0)" />
                <InfoField label="修改时间" :value="formatDate(link.gmtModified)" icon="update"
                    :iconClass="getIconColor(getLinkColorIndex(link, index), 1)" />
            </div>

            <!-- 底部操作按钮 -->
            <template #footer-actions>
                <IconActionButton icon="view" variant="light" customClass="border border-gray-300" title="有效性检查"
                    :disabled="checkingValidityMap[link.code]" @click="handleValidityCheck(link)" />
                <button
                    class="flex items-center gap-1.5 rounded-lg px-4 py-2 text-sm text-white transition-colors duration-300"
                    :class="getActionButtonBg(getLinkColorIndex(link, index))" @click="handleOpen(link.code)">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
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
</template>

<script setup lang="ts">
import LinkCard from '../../components/LinkCard.vue';
import InfoField from '@/components/Form/InfoField.vue';
import IconActionButton from '../../components/IconActionButton.vue';
import {
    getIconColor,
    getActionButtonBg,
    getColorDot,
    getLinkColorIndex
} from "@/utils/ColorSchemeProvider";
import { formatDate, isDateExpired } from '@/utils/DateUtils';

interface Link {
    id: string;
    title: string;
    originalUrl: string;
    domain: string;
    code: string;
    groupId: string;
    expired: string;
    state: string;
    gmtCreate: string;
    gmtModified: string;
}

interface Group {
    id: string;
    title: string;
}

interface Props {
    links: Link[];
    groups: Group[];
    checkingValidityMap: Record<string, boolean>;
}

interface Emits {
    (e: 'edit', link: Link): void;
    (e: 'delete', link: Link): void;
    (e: 'share', link: Link): void;
    (e: 'copy', link: Link): void;
    (e: 'validity-check', link: Link): void;
    (e: 'open', code: string): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const handleEdit = (link: Link) => emit('edit', link);
const handleDelete = (link: Link) => emit('delete', link);
const handleShare = (link: Link) => emit('share', link);
const handleCopy = (link: Link) => emit('copy', link);
const handleValidityCheck = (link: Link) => emit('validity-check', link);
const handleOpen = (code: string) => emit('open', code);

const getGroupTitle = (groupId: string) => {
    const group = props.groups.find((g) => g.id === groupId);
    return group ? group.title : "未分组";
};

const getGroupIndex = (groupId: string) => {
    const index = props.groups.findIndex((g) => g.id === groupId);
    return index >= 0 ? index : 0;
};

const getOriginalUrl = (url: string) => {
    if (url.includes("&")) {
        return url.split("&")[1] || url;
    }
    return url;
};

const isExpired = (dateStr: string) => {
    return isDateExpired(dateStr);
};

const getStatusText = (state: string, expired: boolean) => {
    if (expired) return "已过期";
    if (state === "ACTIVE") return "活跃";
    return state;
};

const getStatusDot = (state: string, expired: boolean) => {
    if (expired) return "bg-red-500";
    if (state === "ACTIVE") return "bg-emerald-500";
    return "bg-yellow-500";
};

const getStatusBorderClass = (state: string, expired: boolean) => {
    if (expired) return 'border-red-100';
    if (state === 'ACTIVE') return 'border-emerald-100';
    return 'border-yellow-100';
};

const getStatusBgClass = (state: string, expired: boolean) => {
    if (expired) return 'bg-red-50';
    if (state === 'ACTIVE') return 'bg-emerald-50';
    return 'bg-yellow-50';
};

const getStatusValueClass = (state: string, expired: boolean) => {
    if (expired) return 'text-red-600';
    if (state === 'ACTIVE') return 'text-emerald-600';
    return 'text-yellow-600';
};
</script>
