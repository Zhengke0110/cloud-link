/**
 * 表单验证工具函数库
 */

/**
 * 验证手机号格式
 * @param phone 手机号
 * @returns 是否为有效的手机号
 */
export const isValidPhone = (phone: string): boolean => {
  return /^1[3-9]\d{9}$/.test(phone);
};

/**
 * 验证邮箱格式
 * @param email 邮箱地址
 * @returns 是否为有效的邮箱
 */
export const isValidEmail = (email: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

/**
 * 验证密码强度
 * @param password 密码
 * @param minLength 最小长度
 * @returns 是否为有效的密码
 */
export const isValidPassword = (password: string, minLength = 6): boolean => {
  return password.length >= minLength;
};

/**
 * 验证是否为空
 * @param value 值
 * @returns 是否为空
 */
export const isEmpty = (value: string | null | undefined): boolean => {
  return value === null || value === undefined || value.trim() === '';
};

/**
 * 验证验证码格式（6位数字）
 * @param code 验证码
 * @returns 是否为有效的验证码
 */
export const isValidVerificationCode = (code: string): boolean => {
  return /^\d{6}$/.test(code);
};

/**
 * 创建表单错误对象
 * @returns 错误对象
 */
export const createErrorObject = <T extends Record<string, any>>(fields: Array<keyof T>) => {
  return fields.reduce((acc, field) => {
    acc[field] = '';
    return acc;
  }, {} as Record<keyof T, string>);
};
