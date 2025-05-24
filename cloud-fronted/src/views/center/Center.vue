<template>
    <PageLayout tag="个人中心" :title="`欢迎回来，${userData.username}`" description="在这里查看和管理您的个人账户信息">
        <!-- 背景装饰放到background插槽 -->
        <template #background>
            <BackgroundDecorations />
        </template>

        <!-- 主内容区域 -->
        <div class="mx-auto max-w-4xl">
            <!-- 使用ProfileCard组件 -->
            <ProfileCard class="reveal-element delay-300" :userData="userData" :accountStats="accountStats"
                @update-profile="showUpdateModal = true" @change-password="handleChangePassword" />

            <!-- 使用TrafficPackages组件 -->
            <TrafficPackages class="mt-10 reveal-element delay-500" :trafficData="trafficData"
                @view-details="handleViewTrafficDetails" />
        </div>

        <!-- 更新个人信息模态窗口 -->
        <UpdateProfileModal v-model="showUpdateModal" :userData="userData" @submit="handleProfileUpdate" />
    </PageLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Data, AccountStats, TrafficData } from './config';
import UpdateProfileModal from './components/UpdateProfileModal.vue';
import BackgroundDecorations from './components/BackgroundDecorations.vue';
import ProfileCard from './components/ProfileCard.vue';
import TrafficPackages from './components/TrafficPackages.vue';
import PageLayout from '@/components/PageLayout.vue';

// 定义数据接口
interface UserData {
    username: string;
    headImg: string;
    auth: string;
    create_time: string;
    accountNo: string | number;
    mail: string;
    phone: string;
    [key: string]: any; // 允许其他可能的字段
}

interface AccountStat {
    id: number | string;
    title: string;
    value: number | string;
    color: string;
    icon: string;
    unit?: string;
}

interface Traffic {
    id: string | number;
    level: string;
    productId: string | number;
    pluginType: string;
    dayUsed: number;
    dayLimit: number;
    expiredDate: string;
    outTradeNo: string;
}

interface TrafficDataType {
    total_record: number;
    current_data: Traffic[];
}

// 从config.ts获取用户数据和账户统计
const userData = ref<UserData>(Data);
const accountStats = ref<AccountStat[]>(AccountStats);
const trafficData = ref<TrafficDataType>(TrafficData);

// 模态窗口状态
const showUpdateModal = ref(false);

// 处理个人信息更新
const handleProfileUpdate = (updatedData: Partial<UserData>) => {
    // 在实际应用中，这里应该调用API来更新用户信息
    console.log('更新用户信息:', updatedData);

    // 更新本地数据
    userData.value = {
        ...userData.value,
        ...updatedData
    };

    // 显示成功通知
    showSuccessNotification('个人信息已更新');
};

// 处理修改密码点击事件
const handleChangePassword = () => {
    console.log('修改密码');
    alert('密码修改功能开发中...');
};

// 处理查看流量包详情事件
const handleViewTrafficDetails = (id: string | number) => {
    console.log('查看流量包详情:', id);
    alert(`查看流量包详情 ID: ${id}`);
};

// 显示成功通知（简单实现）
const showSuccessNotification = (message: string) => {
    alert(message); // 在实际应用中，应该使用更好看的通知组件
};
</script>