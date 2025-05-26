<template>
    <PageLayout tag="个人中心" :title="`欢迎回来，${userData.username}`" description="在这里查看和管理您的个人账户信息">
        <!-- 背景装饰放到background插槽 -->
        <template #background>
            <BackgroundDecorations />
        </template>

        <!-- 主内容区域 -->
        <div class="mx-auto max-w-4xl">
            <!-- 使用ProfileCard组件 -->
            <ProfileCard class="delay-300" :userData="userData" :accountStats="accountStats"
                @update-profile="showUpdateModal = true" @change-password="handleChangePassword" />

            <!-- 使用TrafficPackages组件 -->
            <TrafficPackages v-if="trafficData" class="mt-10  delay-500" :trafficData="trafficData"
                @view-details="handleViewTrafficDetails" />
        </div>

        <!-- 更新个人信息模态窗口 -->
        <UpdateProfileModal v-model="showUpdateModal" :userData="userData" @submit="handleProfileUpdate"
            @upload-image="handleImageUpload" />

        <!-- 流量包详情模态窗口 -->
        <TrafficDetailModal v-model="showTrafficDetailModal" :trafficData="selectedTrafficData" />
    </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Data, AccountStats } from './config';
import UpdateProfileModal from './components/UpdateProfileModal.vue';
import BackgroundDecorations from './components/BackgroundDecorations.vue';
import ProfileCard from './components/ProfileCard.vue';
import TrafficPackages from './components/TrafficPackages.vue';
import TrafficDetailModal from './components/TrafficDetailModal.vue';
import PageLayout from '@/components/PageLayout.vue';
import { GetAccountDetailApi, TrafficDataListApi, AccountUpdateApi, AccountUploadImageApi, TrafficDetailByIDApi } from '@/services/account'
import { useToast } from '@/composables/useToast'
import type { UserData, AccountStat, TrafficDataType } from './config'
const toast = useToast();

// 从config.ts获取用户数据和账户统计
const userData = ref<UserData>(Data);
const accountStats = ref<AccountStat[]>(AccountStats);
const trafficData = ref<TrafficDataType>();
const GetUserData = async () => {
    try {
        // 发送请求获取用户信息
        const userResponse = await GetAccountDetailApi();
        // 更新用户数据
        if (!userResponse.data) throw new Error('用户数据为空!');
        userData.value = userResponse.data;
    } catch (error) {
        let errorMessage = '未知错误';
        if (error instanceof Error) {
            errorMessage = error.message;
        } else if (typeof error === 'string') {
            errorMessage = error;
        }
        throw new Error(errorMessage);
    }
}
const GetTrafficData = async () => {
    try {
        // 发送请求获取流量包数据
        const trafficResponse = await TrafficDataListApi();
        console.log('获取到的流量包数据:', trafficResponse);
        trafficData.value = trafficResponse;
    } catch (error) {
        let errorMessage = '未知错误';
        if (error instanceof Error) {
            errorMessage = error.message;
        } else if (typeof error === 'string') {
            errorMessage = error;
        }
        throw new Error(errorMessage);
    }
}

// 在组件挂载时从API获取用户数据、账户统计信息和流量包数据
onMounted(async () => {
    try {
        GetUserData()

        // TODO: 发送请求获取账户统计数据
        // const statsResponse = await api.getAccountStats();
        // accountStats.value = statsResponse.data;

        GetTrafficData()
    } catch (error) {
        console.error('加载数据失败:', error);
        // 处理错误情况，例如显示错误通知
        toast.error('加载数据失败，请稍后重试');
    }
});

// 模态窗口状态
const showUpdateModal = ref(false);
const showTrafficDetailModal = ref(false);
const selectedTrafficData = ref<any>(null);

// 图片上传处理函数
const handleImageUpload = async (file: File) => {
    try {
        // 验证文件类型
        if (!file.type.match('image/jpeg') && !file.type.match('image/png')) {
            throw new Error('请上传 JPG 或 PNG 格式的图片');
        }

        // 验证文件大小 (最大 2MB)
        if (file.size > 2 * 1024 * 1024) {
            throw new Error('图片大小不能超过 2MB');
        }

        // 调用第三方上传接口
        const { data, code } = await AccountUploadImageApi(file);
        if (code !== 0 || data === null) {
            throw new Error('图片上传失败，请稍后重试');
        }
        // 更新用户头像
        userData.value.headImg = data
        console.log('图片上传成功:', userData.value);
    } catch (error) {
        console.error('图片上传失败:', error);
        toast.error(error instanceof Error ? error.message : '图片上传失败');
        throw error;
    }
};

// 处理个人信息更新
const handleProfileUpdate = async (updatedData: Partial<UserData>) => {
    try {
        // 调用更新用户信息API
        await AccountUpdateApi(updatedData);
        // 显示成功通知
        toast.success('个人信息已更新');
    } catch (error) {
        console.error('更新个人信息失败:', error);
        toast.error('更新个人信息失败，请稍后重试');
    }
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
const handleViewTrafficDetails = async (id: string | number) => {
    console.log('查看流量包详情:', id);

    // 模拟获取流量包详情数据
    const mockTrafficDetail = {
        "id": 1922322216953921537,
        "dayLimit": 5,
        "dayUsed": 0,
        "totalLimit": null,
        "accountNo": 1126256272715284480,
        "outTradeNo": "YI22QYVMSdDsHql3GM9FzYvhwYXtQUiT",
        "level": "SECOND",
        "expiredDate": "2025-06-13T00:00:00.000+00:00",
        "pluginType": "SHORT_LINK",
        "productId": 2
    };

    // TODO: 在实际应用中，这里应该调用API来获取流量包详情
    try {
        const detailResponse = await TrafficDetailByIDApi(String(id));
        selectedTrafficData.value = detailResponse.data;
        showTrafficDetailModal.value = true;
    } catch (error) {
        console.error('获取流量包详情失败:', error);
        toast.error('获取流量包详情失败，请稍后重试');
    }

    selectedTrafficData.value = mockTrafficDetail;
    showTrafficDetailModal.value = true;
};
</script>