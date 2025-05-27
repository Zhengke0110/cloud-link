import { ref, reactive, computed, watch } from 'vue';
import { useClipboard } from '@vueuse/core';
import { LinksCreateApi, ListResultTaskApi } from '@/services/links'
import dayjs from 'dayjs';

interface LinkForm {
  originalUrl: string;
  title: string;
  expired: string;
  groupId: string;
  domainId: string;
  domainType: string;
}

interface ShortLinkResult {
  status: string;
  groupId: number;
  title: string;
  originalUrl: string;
  domain: string;
  code: string;
  expired: string | null;
  accountNo: number;
  gmtCreate: string | null;
  gmtModified: string | null;
  del: number;
  state: string;
  linkType: string | null;
}

export function useShortLinkForm(initialGroupId: string = '') {
  // 使用 useClipboard
  const { copy, copied, isSupported: isClipboardSupported } = useClipboard();

  // 表单数据
  const linkForm = reactive<LinkForm>({
    originalUrl: '',
    title: '',
    expired: '',
    groupId: initialGroupId,
    domainId: '1',
    domainType: 'OFFICIAL'
  });

  // 状态管理
  const isSubmitting = ref(false);
  const shortLinkResult = ref<ShortLinkResult | null>(null);
  const urlError = ref('');
  const errorMessage = ref('');

  // 过期时间相关
  const presetExpiry = ref(7); // 默认设置为7天后
  const expiryDate = ref('');
  const expiryTime = ref('');

  // 最早可选日期时间（当前时间）
  const minDateTime = computed(() => {
    const now = new Date();
    return now.toISOString().slice(0, 16); // 格式: YYYY-MM-DDTHH:MM
  });

  // 计算最早可选日期（当前日期）
  const minDate = computed(() => {
    const now = new Date();
    return now.toISOString().split('T')[0]; // 格式: YYYY-MM-DD
  });

  // URL 验证
  const validateUrl = () => {
    if (!linkForm.originalUrl) {
      urlError.value = '';
      return;
    }

    try {
      const url = new URL(linkForm.originalUrl);
      if (!url.protocol.startsWith('http')) {
        urlError.value = '链接必须以 http:// 或 https:// 开头';
        return;
      }
      urlError.value = '';
    } catch (e) {
      urlError.value = '请输入有效的链接地址';
    }
  };

  // 表单验证
  const isFormValid = computed(() => {
    // 基础验证：URL必须存在且没有错误
    const hasValidUrl = linkForm.originalUrl.trim() && !urlError.value;
    // 可选：检查分组ID是否有效（如果需要的话）
    const hasValidGroup = linkForm.groupId.length > 0; // 允许0作为有效分组ID

    return hasValidUrl && hasValidGroup;
  });

  // 基于选择器值计算linkForm.expired
  watch([expiryDate, expiryTime], () => {
    if (expiryDate.value) {
      // 如果只选择了日期没选择时间，默认设为当天23:59:59
      const time = expiryTime.value || '23:59:59';
      const dateTimeString = `${expiryDate.value} ${time}`;
      // 使用 dayjs 格式化为 yyyy-MM-dd HH:mm:ss
      linkForm.expired = dayjs(dateTimeString).format('YYYY-MM-DD HH:mm:ss');
    } else {
      linkForm.expired = '';
    }
  }, { immediate: true });

  // 设置预设过期时间
  const setPresetExpiry = (days: number) => {
    presetExpiry.value = days;

    if (days === 0) {
      // 自定义，不做处理，由用户自行选择
      return;
    }

    const date = dayjs().add(days, 'day').hour(23).minute(59).second(59);

    // 更新日期和时间字段
    expiryDate.value = date.format('YYYY-MM-DD');
    expiryTime.value = date.format('HH:mm');

    // 直接设置linkForm.expired，确保格式正确
    linkForm.expired = date.format('YYYY-MM-DD HH:mm:ss');
  };

  // 格式化的过期日期用于显示
  const formattedExpiryDate = computed(() => {
    if (!linkForm.expired) return '';

    try {
      // 使用 dayjs 解析和格式化
      const date = dayjs(linkForm.expired);

      if (!date.isValid()) return '';

      return date.format('YYYY年MM月DD日 dddd HH:mm');
    } catch (e) {
      return '';
    }
  });

  // 格式化日期时间显示
  const formatDateTime = (dateStr: string) => {
    try {
      const date = dayjs(dateStr);
      if (!date.isValid()) return dateStr;
      return date.format('YYYY-MM-DD HH:mm:ss');
    } catch (e) {
      return dateStr;
    }
  };

  // 生成随机字符串
  const generateRandomString = (length: number) => {
    const chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let result = '';
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  };

  // 创建短链接
  const createShortLink = async () => {
    // 强制验证URL
    validateUrl();

    if (!isFormValid.value) {
      console.log('表单验证失败:', {
        originalUrl: linkForm.originalUrl,
        urlError: urlError.value,
        groupId: linkForm.groupId
      });
      return;
    }

    errorMessage.value = '';
    isSubmitting.value = true;

    try {
      // 构建请求数据
      const requestData = {
        groupId: linkForm.groupId,
        title: linkForm.title || `短链测试-${generateRandomString(3)}`,
        originalUrl: linkForm.originalUrl,
        domainId: linkForm.domainId,
        domainType: linkForm.domainType,
        expired: linkForm.expired || ""
      };

      // 控制台日志
      console.log('准备发送的数据:', requestData);

      const taskID = await LinksCreateApi(requestData);
      setTimeout(async () => {
        // 获取任务结果
        const res = await ListResultTaskApi(taskID)
        console.log('创建短链接结果:', res);

        // 直接使用API返回的数据，不添加额外字段
        shortLinkResult.value = res;
      }, 2000)
    } catch (error) {
      console.error('创建短链接失败:', error);
      errorMessage.value = '创建短链接失败，请稍后再试';
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // 复制到剪贴板
  const copyToClipboard = (text: string) => {
    copy(text);
    if (!isClipboardSupported) {
      errorMessage.value = '您的浏览器不支持自动复制，请手动选择文本并复制';
    }
    return copied;
  };

  // 分享链接
  const shareLink = () => {
    if (!shortLinkResult.value) return false;

    const shortUrl = `https://${shortLinkResult.value.domain}/${shortLinkResult.value.code}`;

    // 检查Web分享API是否可用
    if (navigator.share) {
      navigator.share({
        title: shortLinkResult.value.title || '分享短链接',
        text: '我创建了一个短链接，快来看看吧！',
        url: shortUrl
      })
        .catch((error) => {
          console.error('分享失败:', error);
        });
      return true;
    } else {
      // 不支持Web分享API时，回退到复制链接
      copyToClipboard(shortUrl);
      return false;
    }
  };

  // 重置表单
  const resetForm = () => {
    linkForm.originalUrl = '';
    linkForm.title = '';
    linkForm.expired = '';
    expiryDate.value = '';
    expiryTime.value = '';
    presetExpiry.value = 0;
    // 分组保持不变
    shortLinkResult.value = null;
    urlError.value = '';
    errorMessage.value = '';
  };

  return {
    linkForm,
    isSubmitting,
    shortLinkResult,
    urlError,
    errorMessage,
    presetExpiry,
    expiryDate,
    expiryTime,
    minDateTime,
    minDate,
    isFormValid,
    formattedExpiryDate,
    copied,
    validateUrl,
    setPresetExpiry,
    createShortLink,
    copyToClipboard,
    shareLink,
    resetForm,
    formatDateTime
  };
}
