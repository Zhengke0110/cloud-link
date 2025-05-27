import { DefaultService } from '@/generated'

/**
 * 异步函数DataPageRecordApi用于获取特定页面的访问记录
 * 该函数通过调用服务端接口来获取数据，并对获取的结果进行处理
 * 
 * @param form 包含请求参数的对象
 * @param form.page 页码，表示请求的页面序号
 * @param form.pageSize 每页大小，表示每页包含的记录数
 * @param form.groupId 组ID，用于筛选特定组的访问记录
 * @throws {Error} 如果获取访问记录失败，抛出错误
 * @returns 返回访问记录的数据
 */
export const DataPageRecordApi = async (form: {
    code: string;
    size: number;
    page: number;
}) => {
    try {
        // 调用服务端接口获取访问记录
        const { code, data, msg } = await DefaultService.postDataServerApiVisitStatsV1PageRecord(form);

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取访问记录失败');

        // 返回访问记录数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取访问记录失败: ${(error as Error).message}`);
    }
}

/**
 * 根据地区和时间获取访问记录
 * 
 * 此函数通过POST请求调用服务端API，以获取特定地区在指定时间范围内的访问记录
 * 它主要用于统计和分析访问数据，以便更好地了解用户行为和优化服务
 * 
 * @param form 包含查询参数的对象
 * @param form.code 地区代码，用于指定需要查询的地区
 * @param form.startTime 查询的开始时间，格式为字符串
 * @param form.endTime 查询的结束时间，格式为字符串
 * @throws {Error} 如果请求失败或服务端返回错误码，则抛出错误
 * @returns 返回访问记录数据的Promise对象
 */
export const DataRegionDayApi = async (form: {
    code: string;
    startTime: string;
    endTime: string;
}) => {
    try {
        // 发起POST请求到服务端API，获取访问记录数据
        const { code, data, msg } = await DefaultService.postDataServerApiVisitStatsV1RegionDay(form);

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取访问记录失败');

        // 返回访问记录数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取访问记录失败: ${(error as Error).message}`);
    }
}

/**
 * 获取数据趋势的API
 * 
 * 此函数通过POST请求向服务器发送参数，以获取特定条件下的访问记录趋势
 * 它使用了异步处理和try-catch块来处理请求和响应过程中可能发生的错误
 * 
 * @param form 包含请求参数的对象
 * @param form.code 数据标识代码
 * @param form.type 数据类型
 * @param form.startTime 查询的开始时间
 * @param form.endTime 查询的结束时间
 * @throws {Error} 如果请求失败或服务器返回非0的响应码，抛出错误
 * @returns {Promise<any>} 返回一个Promise，解析为访问记录的数据
 */
export const DataTrendApi = async (form: {
    code: string;
    type: string;
    startTime: string;
    endTime: string;
}) => {
    try {
        // 发送POST请求到服务器，获取访问记录的趋势数据
        const { code, data, msg } = await DefaultService.postDataServerApiVisitStatsV1Trend(form);
        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取访问记录失败');

        // 返回访问记录数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取访问记录失败: ${(error as Error).message}`);
    }
}

/**
 * 获取指定时间段内的数据访问频繁情况
 * 
 * 此函数通过调用服务端API，获取给定时间范围内指定资源的访问记录
 * 它主要用于分析和监控数据访问的频率和模式
 * 
 * @param form 包含查询条件的对象
 * @param form.code 资源代码，标识要查询的资源
 * @param form.startTime 查询的开始时间
 * @param form.endTime 查询的结束时间
 * @throws {Error} 如果服务端返回的code不为0，表示获取失败，将抛出错误
 * @returns 返回访问记录的数据
 */
export const DataFrequentApi = async (form: {
    code: string;
    startTime: string;
    endTime: string;
}) => {
    try {
        // 发起POST请求到服务端，获取访问统计信息
        const { code, data, msg } = await DefaultService.postDataServerApiVisitStatsV1FrequentSource(form);
        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取访问记录失败');

        // 返回访问记录数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取访问记录失败: ${(error as Error).message}`);
    }
}

/**
 * 异步函数 DataDeviceApi 用于获取设备访问记录
 * 该函数通过调用服务端接口来获取指定条件下的设备访问数据
 * 
 * @param form 包含查询条件的对象
 * @param form.code 设备代码，用于标识特定设备
 * @param form.startTime 查询的开始时间，用于限定时间范围
 * @param form.endTime 查询的结束时间，用于限定时间范围
 * @throws {Error} 如果获取访问记录失败或服务端返回非0码，则抛出错误
 * @returns 返回设备访问记录的数据
 */
export const DataDeviceApi = async (form: {
    code: string;
    startTime: string;
    endTime: string;
}) => {
    try {
        // 调用服务端接口获取访问记录
        const { data, code, msg } = await DefaultService.postDataServerApiVisitStatsV1DeviceInfo(form);

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取访问记录失败');

        // 返回访问记录数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取访问记录失败: ${(error as Error).message}`);
    }
}