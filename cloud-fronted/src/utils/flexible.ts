import { computed, reactive } from "vue";
import { useWindowSize } from "@vueuse/core";

interface DeviceConfig {
  readonly PC_DEVICE_WIDTH: number;
  readonly TABLET_DEVICE_WIDTH: number;
}

const deviceConfig: DeviceConfig = reactive({
  PC_DEVICE_WIDTH: 1280,
  TABLET_DEVICE_WIDTH: 768,
});

/**
 * @description 判断当前设备类型
 * @returns {Object} 包含设备类型判断的对象
 * isMobile: 是否为移动设备 (<768px)
 * isTablet: 是否为平板设备 (>=768px && <1280px)
 * isDesktop: 是否为桌面设备 (>=1280px)
 */
const { width } = useWindowSize();
export const deviceType = computed(() => ({
  isMobile: width.value < deviceConfig.TABLET_DEVICE_WIDTH,
  isTablet:
    width.value >= deviceConfig.TABLET_DEVICE_WIDTH &&
    width.value < deviceConfig.PC_DEVICE_WIDTH,
  isDesktop: width.value >= deviceConfig.PC_DEVICE_WIDTH,
}));

// 向后兼容的导出
export const isMobileTerminal = computed(() => !deviceType.value.isDesktop);

