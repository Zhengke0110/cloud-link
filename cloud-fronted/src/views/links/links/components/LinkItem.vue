<template>
    <LinkCard :title="link.title" :colorIndex="colorIndex">
        <template #header-actions>
            <span class="text-sm text-white/80">操作</span>
            <div class="flex space-x-1">
                <IconActionButton icon="edit" @click="$emit('edit', link)" title="编辑链接" />
                <IconActionButton icon="delete" type="danger" @click="$emit('delete', link)" title="删除链接" />
                <IconActionButton icon="share" title="分享链接" />
            </div>
        </template>

        <!-- 链接内容 -->
        <div class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2">
            <InfoField label="原始链接" :value="getOriginalUrl(link.originalUrl)" breakAll />
            <InfoField label="短链接" valueClass="text-indigo-600" borderClass="border-indigo-100" bgClass="bg-indigo-50">
                <div class="flex items-center justify-between w-full">
                    <span>{{ `${link.domain}/${link.code}` }}</span>
                    <IconActionButton icon="copy" variant="light" size="sm" title="复制链接"
                        customClass="text-indigo-600 hover:bg-indigo-100" />
                </div>
            </InfoField>
        </div>

        <div class="mb-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
            <InfoField label="分组">
                <div class="flex items-center gap-2 w-full">
                    <span class="h-2 w-2 rounded-full" :class="getColorDot(groupIndex)"></span>
                    {{ groupTitle }}
                </div>
            </InfoField>

            <InfoField label="过期时间" :value="formatDate(link.expired)" icon="clock"
                :valueClass="isExpired(link.expired) ? 'text-red-600' : 'text-gray-800'" />

            <InfoField label="状态" :value="getStatusText(link.state, isExpired(link.expired))"
                :borderClass="isExpired(link.expired) ? 'border-red-100' : link.state === 'ACTIVE' ? 'border-emerald-100' : 'border-yellow-100'"
                :bgClass="isExpired(link.expired) ? 'bg-red-50' : link.state === 'ACTIVE' ? 'bg-emerald-50' : 'bg-yellow-50'"
                :valueClass="isExpired(link.expired) ? 'text-red-600' : link.state === 'ACTIVE' ? 'text-emerald-600' : 'text-yellow-600'">
                <template #icon>
                    <span class="h-2 w-2 rounded-full"
                        :class="getStatusDot(link.state, isExpired(link.expired))"></span>
                </template>
            </InfoField>
        </div>

        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <InfoField label="创建时间" :value="formatDate(link.gmtCreate)" icon="clock"
                :iconClass="getIconColor(colorIndex, 0)" />
            <InfoField label="修改时间" :value="formatDate(link.gmtModified)" icon="update"
                :iconClass="getIconColor(colorIndex, 1)" />
        </div>

        <template #footer-actions>
            <IconActionButton icon="view" variant="light" customClass="border border-gray-300" title="有效性检查"
                :disabled="isCheckingValidity" @click="$emit('checkValidity', link)" />

            <button
                class="flex items-center gap-1.5 rounded-lg px-4 py-2 text-sm text-white transition-colors duration-300"
                :class="getActionButtonBg(colorIndex)" @click="$emit('openLink', link.code)">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                    <path
                        d="M11 3a1 1 0 100 2h2.586l-6.293 6.293a1 1 0 101.414 1.414L15 6.414V9a1 1 0 102 0V4a1 1 0 00-1-1h-5z" />
                    <path d="M5 5a2 2 0 00-2 2v8a2 2 0 002 2h8a2 2 0 002-2v-3a1 1 0 10-2 0v3H5V7h3a1 1 0 000-2H5z" />
                </svg>
                访问链接
            </button>
        </template>
    </LinkCard>
</template>

<script setup lang="ts">
import LinkCard from '../../components/LinkCard.vue';
import IconActionButton from '../../components/IconActionButton.vue';
import InfoField from '@/components/Form/InfoField.vue';
import { getIconColor, getActionButtonBg, getColorDot } from "@/utils/ColorSchemeProvider";
import { formatDate, isDateExpired } from '@/utils/DateUtils';

interface Link {
    id: string;
    title: string;
    originalUrl: string;
    domain: string;
    code: string;
    groupId: number;
    expired: string;
    state: string;
    gmtCreate: string;
    gmtModified: string;
}

interface Props {
    link: Link;
    colorIndex: number;
    groupTitle: string;
    groupIndex: number;
    isCheckingValidity?: boolean;
}

interface Emits {
    (e: 'edit', link: Link): void;
    (e: 'delete', link: Link): void;
    (e: 'checkValidity', link: Link): void;
    (e: 'openLink', code: string): void;
}
defineProps<Props>();
defineEmits<Emits>();

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
</script>
