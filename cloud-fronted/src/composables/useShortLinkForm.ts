import { ref, reactive, computed, watch } from 'vue';
import { useClipboard } from '@vueuse/core';

interface LinkForm {
  originalUrl: string;
  title: string;
  expired: string;
  groupId: number;
  domainId: number;
  domainType: string;
}

interface ShortLinkResult {
  id: number;
  shortUrl: string;
  originalUrl: string;
  title: string;
  createTime: string;
  expired: string | null;
  qrCode: string;
}

export function useShortLinkForm(initialGroupId: number = 0) {
  // 使用 useClipboard
  const { copy, copied, isSupported: isClipboardSupported } = useClipboard();
  
  // 表单数据
  const linkForm = reactive<LinkForm>({
    originalUrl: '',
    title: '',
    expired: '',
    groupId: initialGroupId,
    domainId: 1,
    domainType: 'OFFICIAL'
  });

  // 状态管理
  const isSubmitting = ref(false);
  const shortLinkResult = ref<ShortLinkResult | null>(null);
  const urlError = ref('');
  const errorMessage = ref('');

  // 过期时间相关
  const presetExpiry = ref(0); // 0 表示自定义，其他数字表示天数
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
    return linkForm.originalUrl && !urlError.value;
  });

  // 基于选择器值计算linkForm.expired
  watch([expiryDate, expiryTime], () => {
    if (expiryDate.value) {
      // 如果只选择了日期没选择时间，默认设为当天23:59:59
      const time = expiryTime.value || '23:59:59';
      linkForm.expired = `${expiryDate.value}T${time}`;
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
    
    const date = new Date();
    date.setDate(date.getDate() + days);
    
    // 设置为当天23:59:59
    date.setHours(23, 59, 59);
    
    // 更新日期和时间字段
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    expiryDate.value = `${year}-${month}-${day}`;
    expiryTime.value = `${hours}:${minutes}`;
    
    // 直接设置linkForm.expired，确保格式正确
    linkForm.expired = formatDateTime(date.toISOString());
  };

  // 格式化的过期日期用于显示
  const formattedExpiryDate = computed(() => {
    if (!linkForm.expired) return '';
    
    try {
      // 支持多种输入格式（ISO或已格式化的字符串）
      let date;
      if (linkForm.expired.includes('T')) {
        // ISO格式
        date = new Date(linkForm.expired);
      } else {
        // 已格式化字符串，尝试解析
        const parts = linkForm.expired.split(/[- :]/);
        date = new Date(
          parseInt(parts[0]), 
          parseInt(parts[1])-1, 
          parseInt(parts[2]), 
          parseInt(parts[3] || "0"), 
          parseInt(parts[4] || "0"), 
          parseInt(parts[5] || "0")
        );
      }
      
      if (isNaN(date.getTime())) return '';
      
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        weekday: 'long'
      }).format(date);
    } catch (e) {
      return '';
    }
  });

  // 格式化日期时间显示
  const formatDateTime = (dateStr: string) => {
    try {
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
    if (!isFormValid.value) return;

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
        expired: linkForm.expired || null
      };

      // 控制台日志
      console.log('准备发送的数据:', requestData);

      // 模拟API请求延迟
      await new Promise(resolve => setTimeout(resolve, 1000));

      // 模拟返回结果
      shortLinkResult.value = {
        id: Date.now(),
        shortUrl: `timu.link/${generateRandomString(6)}`,
        originalUrl: linkForm.originalUrl,
        title: requestData.title,
        createTime: new Date().toISOString(),
        expired: linkForm.expired ? new Date(linkForm.expired).toISOString() : null,
        qrCode: `https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${encodeURIComponent(`timu.link/${generateRandomString(6)}`)}`
      };
      
      return shortLinkResult.value;
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

    // 检查Web分享API是否可用
    if (navigator.share) {
      navigator.share({
        title: shortLinkResult.value.title || '分享短链接',
        text: '我创建了一个短链接，快来看看吧！',
        url: shortLinkResult.value.shortUrl
      })
        .catch((error) => {
          console.error('分享失败:', error);
        });
        return true;
    } else {
      // 不支持Web分享API时，回退到复制链接
      copyToClipboard(shortLinkResult.value.shortUrl);
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
