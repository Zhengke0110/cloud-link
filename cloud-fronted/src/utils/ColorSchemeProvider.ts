/**
 * 颜色方案提供器
 * 为整个应用程序提供一致的颜色方案
 */

// 颜色方案接口定义
export interface ColorScheme {
  header: string;        // 标题栏渐变背景
  border: string;        // 卡片边框渐变
  icon1: string;         // 第一种图标颜色
  icon2: string;         // 第二种图标颜色
  button: string;        // 按钮背景和悬浮效果
  dot?: string;          // 颜色点
}

// 应用中使用的所有颜色方案
export const colorSchemes: ColorScheme[] = [
  {
    header: "bg-gradient-to-r from-indigo-600 via-blue-700 to-indigo-600",
    border: "bg-gradient-to-br from-indigo-500 via-purple-500 to-blue-500",
    icon1: "text-indigo-500",
    icon2: "text-blue-500",
    button: "bg-indigo-600 hover:bg-indigo-700",
    dot: "bg-indigo-500",
  },
  {
    header: "bg-gradient-to-r from-rose-500 via-pink-600 to-rose-500",
    border: "bg-gradient-to-br from-rose-500 via-pink-600 to-fuchsia-500",
    icon1: "text-rose-500",
    icon2: "text-pink-500",
    button: "bg-rose-600 hover:bg-rose-700",
    dot: "bg-rose-500",
  },
  {
    header: "bg-gradient-to-r from-amber-500 via-orange-600 to-amber-500",
    border: "bg-gradient-to-br from-amber-500 via-orange-600 to-yellow-500",
    icon1: "text-amber-500",
    icon2: "text-orange-500",
    button: "bg-amber-600 hover:bg-amber-700",
    dot: "bg-amber-500",
  },
  {
    header: "bg-gradient-to-r from-emerald-500 via-teal-600 to-emerald-500",
    border: "bg-gradient-to-br from-emerald-500 via-teal-600 to-green-500",
    icon1: "text-emerald-500",
    icon2: "text-teal-500",
    button: "bg-emerald-600 hover:bg-emerald-700",
    dot: "bg-emerald-500",
  },
  {
    header: "bg-gradient-to-r from-purple-500 via-violet-600 to-purple-500",
    border: "bg-gradient-to-br from-purple-500 via-violet-600 to-indigo-500",
    icon1: "text-purple-500",
    icon2: "text-violet-500",
    button: "bg-purple-600 hover:bg-purple-700",
    dot: "bg-purple-500",
  },
  {
    header: "bg-gradient-to-r from-cyan-500 via-sky-600 to-cyan-500",
    border: "bg-gradient-to-br from-cyan-500 via-sky-600 to-blue-500",
    icon1: "text-cyan-500",
    icon2: "text-sky-500",
    button: "bg-cyan-600 hover:bg-cyan-700",
    dot: "bg-cyan-500",
  },
  // 增加更多颜色方案
  {
    header: "bg-gradient-to-r from-pink-500 via-red-600 to-pink-500",
    border: "bg-gradient-to-br from-pink-500 via-red-500 to-rose-500",
    icon1: "text-pink-500",
    icon2: "text-red-500",
    button: "bg-pink-600 hover:bg-pink-700",
    dot: "bg-pink-500",
  },
  {
    header: "bg-gradient-to-r from-blue-500 via-indigo-600 to-blue-500",
    border: "bg-gradient-to-br from-blue-500 via-indigo-500 to-purple-500",
    icon1: "text-blue-500",
    icon2: "text-indigo-500",
    button: "bg-blue-600 hover:bg-blue-700",
    dot: "bg-blue-500",
  },
  {
    header: "bg-gradient-to-r from-green-500 via-emerald-600 to-green-500",
    border: "bg-gradient-to-br from-green-500 via-emerald-500 to-teal-500",
    icon1: "text-green-500",
    icon2: "text-emerald-500",
    button: "bg-green-600 hover:bg-green-700",
    dot: "bg-green-500",
  },
  {
    header: "bg-gradient-to-r from-yellow-500 via-amber-600 to-yellow-500",
    border: "bg-gradient-to-br from-yellow-500 via-amber-500 to-orange-500",
    icon1: "text-yellow-600",
    icon2: "text-amber-500",
    button: "bg-yellow-600 hover:bg-yellow-700",
    dot: "bg-yellow-500",
  },
  {
    header: "bg-gradient-to-r from-fuchsia-600 via-pink-700 to-fuchsia-600",
    border: "bg-gradient-to-br from-fuchsia-500 via-pink-600 to-purple-600",
    icon1: "text-fuchsia-500",
    icon2: "text-pink-500",
    button: "bg-fuchsia-600 hover:bg-fuchsia-700",
    dot: "bg-fuchsia-500",
  },
  {
    header: "bg-gradient-to-r from-lime-500 via-green-600 to-lime-500",
    border: "bg-gradient-to-br from-lime-500 via-green-500 to-emerald-500",
    icon1: "text-lime-600",
    icon2: "text-green-500",
    button: "bg-lime-600 hover:bg-lime-700",
    dot: "bg-lime-500",
  }
];

/**
 * 根据索引获取颜色方案
 * @param index 颜色索引
 * @returns 颜色方案对象
 */
export const getColorScheme = (index: number): ColorScheme => {
  return colorSchemes[index % colorSchemes.length];
};

/**
 * 获取标题栏渐变背景样式类
 * @param index 颜色索引
 */
export const getHeaderGradient = (index: number): string => {
  return getColorScheme(index).header;
};

/**
 * 获取卡片边框渐变样式类
 * @param index 颜色索引
 */
export const getBorderGradient = (index: number): string => {
  return getColorScheme(index).border;
};

/**
 * 获取图标颜色样式类
 * @param index 颜色索引
 * @param iconIndex 图标类型索引（0或1）
 */
export const getIconColor = (index: number, iconIndex: number): string => {
  const scheme = getColorScheme(index);
  return iconIndex === 0 ? scheme.icon1 : scheme.icon2;
};

/**
 * 获取操作按钮背景样式类
 * @param index 颜色索引
 */
export const getActionButtonBg = (index: number): string => {
  return getColorScheme(index).button;
};

/**
 * 获取分组颜色点样式类
 * @param index 颜色索引
 */
export const getColorDot = (index: number): string => {
  return getColorScheme(index).dot || "bg-gray-500"; // 提供默认值
};

/**
 * 获取选中状态按钮样式
 * @param index 颜色索引
 */
export const getSelectedButtonStyle = (index: number): string => {
  const bgColor = getColorScheme(index).button;
  return `${bgColor} text-white shadow-md`;
};

/**
 * 根据链接ID生成唯一的颜色索引
 * 确保相同的链接总是获得相同的颜色
 * @param link 链接对象，需要包含ID
 * @param fallbackIndex 作为备选的索引值
 * @returns 颜色索引
 */
export const getLinkColorIndex = (link: any, fallbackIndex: number = 0): number => {
  // 如果链接有ID
  if (link && link.id) {
    // 处理可能是字符串的ID
    const idStr = String(link.id);
    // 使用简单的字符串哈希算法，确保结果更随机
    let hash = 0;
    for (let i = 0; i < idStr.length; i++) {
      hash = ((hash << 5) - hash) + idStr.charCodeAt(i);
      hash = hash & hash; // 转换为32位整数
    }
    // 取绝对值并对颜色数组长度取模
    return Math.abs(hash) % colorSchemes.length;
  }
  // 回退到索引，如果没有ID
  return fallbackIndex % colorSchemes.length;
};
