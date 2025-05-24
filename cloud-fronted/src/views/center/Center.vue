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
import { ref, onMounted } from 'vue';
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

// TODO: 在组件挂载时从API获取用户数据、账户统计信息和流量包数据
onMounted(async () => {
    try {
        // TODO: 发送请求获取用户信息
        // const userResponse = await api.getUserInfo();
        // userData.value = userResponse.data;

        // TODO: 发送请求获取账户统计数据
        // const statsResponse = await api.getAccountStats();
        // accountStats.value = statsResponse.data;

        // TODO: 发送请求获取流量包数据
        // const trafficResponse = await api.getTrafficPackages();
        // trafficData.value = trafficResponse.data;
    } catch (error) {
        console.error('加载数据失败:', error);
        // TODO: 处理错误情况，例如显示错误通知
    }
});

// 模态窗口状态
const showUpdateModal = ref(false);

// 处理个人信息更新
const handleProfileUpdate = async (updatedData: Partial<UserData>) => {
    // TODO: 在实际应用中，这里应该调用API来更新用户信息
    // try {
    //   const response = await api.updateUserProfile(updatedData);
    //   if (response.status === 200) {
    //     userData.value = {
    //       ...userData.value,
    //       ...updatedData
    //     };
    //     showSuccessNotification('个人信息已更新');
    //   }
    // } catch (error) {
    //   console.error('更新个人信息失败:', error);
    //   showErrorNotification('更新个人信息失败，请稍后重试');
    // }

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
    // TODO: 实现密码修改功能，可能需要打开密码修改模态窗口
    // 可以创建一个新的模态组件 ChangePasswordModal 处理密码修改逻辑
    // showPasswordModal.value = true;
    
    alert('密码修改功能开发中...');
};

// 处理查看流量包详情事件
const handleViewTrafficDetails = (id: string | number) => {
    console.log('查看流量包详情:', id);
    // TODO: 根据ID获取流量包详情
    // try {
    //   const detailResponse = await api.getTrafficPackageDetail(id);
    //   // 显示详情模态窗口或导航到详情页面
    // } catch (error) {
    //   console.error('获取流量包详情失败:', error);
    // }
    
    alert(`查看流量包详情 ID: ${id}`);
};

// 显示成功通知（简单实现）
const showSuccessNotification = (message: string) => {
    // TODO: 使用项目中的通知系统，如 Toast 组件
    // toast.success(message, { title: '成功' });
    
    alert(message);
};

// TODO: 实现错误通知
// const showErrorNotification = (message: string) => {
//   toast.error(message, { title: '错误' });
// };
</script>