/**
 * 格式化日期为本地化字符串
 * @param dateStr ISO日期字符串
 * @param options 可选的格式化选项
 * @returns 格式化后的日期字符串
 */
export const formatDate = (dateStr: string, options?: Intl.DateTimeFormatOptions) => {
  if (!dateStr) return '—';

  try {
    const date = new Date(dateStr);

    // 使用默认选项或传入的自定义选项
    const formatOptions = options || {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    };

    return date.toLocaleString("zh-CN", formatOptions);
  } catch (e) {
    return dateStr;
  }
};

/**
 * 检查日期是否已过期
 * @param dateStr ISO日期字符串
 * @returns 是否已过期的布尔值
 */
export const isDateExpired = (dateStr: string): boolean => {
  if (!dateStr) return false;

  try {
    const date = new Date(dateStr);
    return date.getTime() < Date.now();
  } catch (e) {
    return false;
  }
};

/**
 * 获取相对时间描述（如"3天前"，"刚刚"等）
 * @param dateStr ISO日期字符串
 * @returns 相对时间描述
 */
export const getRelativeTimeDesc = (dateStr: string): string => {
  if (!dateStr) return '—';

  try {
    const date = new Date(dateStr);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();

    // 将毫秒转换为更可读的单位
    const diffSeconds = Math.floor(diffMs / 1000);
    if (diffSeconds < 60) return '刚刚';

    const diffMinutes = Math.floor(diffSeconds / 60);
    if (diffMinutes < 60) return `${diffMinutes}分钟前`;

    const diffHours = Math.floor(diffMinutes / 60);
    if (diffHours < 24) return `${diffHours}小时前`;

    const diffDays = Math.floor(diffHours / 24);
    if (diffDays < 30) return `${diffDays}天前`;

    const diffMonths = Math.floor(diffDays / 30);
    if (diffMonths < 12) return `${diffMonths}个月前`;

    const diffYears = Math.floor(diffMonths / 12);
    return `${diffYears}年前`;
  } catch (e) {
    return dateStr;
  }
};

/**
 * 格式化日期为简短格式 (YYYY-MM-DD)
 * @param dateStr ISO日期字符串
 * @returns 格式化后的日期字符串
 */
export const formatDateShort = (dateStr: string): string => {
  return formatDate(dateStr, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit"
  });
};

/**
 * 创建一个日期格式化器，可用于格式化多个日期
 * @param options 格式化选项
 * @returns 格式化函数
 */
export const createDateFormatter = (options: Intl.DateTimeFormatOptions) => {
  return (dateStr: string) => formatDate(dateStr, options);
};
