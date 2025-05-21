/**
 * 格式化日期为本地化字符串
 * @param dateStr ISO日期字符串
 * @returns 格式化后的日期字符串
 */
export const formatDate = (dateStr: string) => {
  try {
    const date = new Date(dateStr);
    return date.toLocaleString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch (e) {
    return dateStr;
  }
};
