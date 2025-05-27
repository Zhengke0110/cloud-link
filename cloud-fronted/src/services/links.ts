import { DefaultService } from '@/generated'

/**
 * 获取域名列表的异步函数
 * 该函数通过调用服务端接口来获取域名列表，并对结果进行处理
 * 如果获取失败，会抛出错误
 * 
 * @throws {Error} 当获取域名列表失败时抛出错误，包含失败的原因
 * @returns {Promise<any>} 返回一个Promise，解析为域名列表数据
 */
export const DomainGetListsApi = async () => {
    try {
        // 调用服务端接口获取域名列表
        const { data, code, msg } = await DefaultService.getLinkServerApiDomainV1List();

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取域名列表失败');
        // 返回域名列表数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取域名列表失败: ${(error as Error).message}`);
    }
}


/**
 * 获取分组列表的异步函数
 * 该函数尝试调用服务端接口以获取分组列表，如果获取失败，则抛出错误
 * 
 * @throws {Error} 当分组列表获取失败时抛出错误
 * @returns {Promise<any>} 返回一个Promise，解析为分组列表数据
 */
export const GroupingGetListsApi = async () => {
    try {
        // 调用服务端接口获取分组列表
        const { data, code, msg } = await DefaultService.getLinkServerApiGroupV1List();

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取分组列表失败');
        // 返回分组列表数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取分组列表失败: ${(error as Error).message}`);
    }
}

/**
 * 根据ID获取分组详情的异步函数
 * 该函数调用服务端接口以获取特定ID的分组详细信息，并对结果进行处理
 * @param id 分组的唯一标识符
 * @returns 返回Promise，解析为分组详情数据
 * @throws 当获取分组信息失败时，抛出错误
 */
export const GroupingGetDetailByIDApi = async (id: string) => {
    try {
        // 调用服务端接口获取指定ID的分组信息
        const { data, code, msg } = await DefaultService.getLinkServerApiGroupV1Detail(id);

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取分组信息失败');
        // 返回分组信息数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取分组信息失败: ${(error as Error).message}`);
    }
}

/**
 * 根据ID删除分组的异步函数
 * 调用服务端接口以删除指定ID的分组，并处理删除过程中的错误
 * @param id 分组的唯一标识符
 * @throws 如果删除失败，抛出包含错误信息的错误对象
 */
export const GroupingDeleteByIDApi = async (id: string) => {
    try {
        // 调用服务端接口删除指定ID的分组
        const { code, msg } = await DefaultService.deleteLinkServerApiGroupV1Del(id);

        // 检查响应码，如果不为0则表示删除失败，抛出错误
        if (code !== 0) throw new Error(msg || '删除分组失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`删除分组失败: ${(error as Error).message}`);
    }
}

/**
 * 调用服务端接口创建新的分组
 * 
 * 此函数通过向服务端发送POST请求来创建一个新的分组如果创建失败，会抛出错误
 * 
 * @param data 包含分组标题的对象
 * @throws {Error} 如果服务端返回的code不为0，表示创建分组失败，抛出错误
 */
export const GroupingCreateApi = async (data: { title: string; }) => {
    try {
        // 调用服务端接口创建新的分组
        const { code, msg } = await DefaultService.postLinkServerApiGroupV1Add(data);

        // 检查响应码，如果不为0则表示创建失败，抛出错误
        if (code !== 0) throw new Error(msg || '创建分组失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`创建分组失败: ${(error as Error).message}`);
    }
}

/**
 * 更新分组信息的API接口函数
 * 
 * 该函数通过调用服务端接口来更新指定ID的分组信息
 * 当更新失败时，会抛出错误
 * 
 * @param data 包含分组ID和新标题的对象
 * @throws {Error} 当更新失败时抛出的错误
 */
export const GroupingUpdateApi = async (data: { id: string, title: string }) => {
    try {
        // 调用服务端接口更新指定ID的分组信息
        const { code, msg } = await DefaultService.putLinkServerApiGroupV1Update(data);

        // 检查响应码，如果不为0则表示更新失败，抛出错误
        if (code !== 0) throw new Error(msg || '更新分组失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`更新分组失败: ${(error as Error).message}`);
    }
}


/**
 * 异步函数：检查指定URL的有效性
 * 该函数通过调用服务端API来检查给定的URL是否有效它不返回任何值，但会抛出错误，如果URL检查失败
 * 
 * @param url {string} 需要检查的URL地址
 * @throws {Error} 如果URL检查失败或服务端返回错误，抛出包含错误信息的错误对象
 */
export const LinksCheckApi = async (url: string) => {
    try {
        // 调用服务端接口检查链接
        const { code, msg } = await DefaultService.getLinkServerApiLinkV1Check(url, 'timu.fun');
        // 检查响应码，如果不为0则表示检查失败，抛出错误
        if (code !== 0) throw new Error(msg || '链接检查失败');
        return true
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`链接检查失败: ${(error as Error).message}`);
    }
}

/**
 * 创建链接的API函数
 * 
 * 此函数通过异步请求向服务端提交数据，以创建一个新的短链接
 * 它捕获在提交过程中可能发生的错误，并在创建失败时抛出异常
 * 
 * @param data 包含创建链接所需信息的对象
 * @param data.groupId 组ID，用于分类链接
 * @param data.title 链接的标题，用于标识链接
 * @param data.originalUrl 原始URL，即需要缩短的长链接
 * @param data.domainId 域名ID，指定链接的域名
 * @param data.domainType 域名类型，进一步指定域名的类型
 * @param data.expired 链接的过期时间，超过该时间链接将无法访问
 * @throws {Error} 如果创建链接失败，抛出一个错误
 */
export const LinksCreateApi = async (data: {
    groupId: string;
    title: string;
    originalUrl: string;
    domainId: string;
    domainType: string;
    expired: string;
}) => {
    try {
        // 调用服务端接口创建新的链接
        const { code, msg } = await DefaultService.postLinkServerApiLinkV1Add(data);

        // 检查响应码，如果不为0则表示创建失败，抛出错误
        if (code !== 0) throw new Error(msg || '创建链接失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`创建链接失败: ${(error as Error).message}`);
    }
}

/**
 * 获取链接列表的异步函数
 * 
 * 该函数向服务端发送请求，以获取指定条件下的链接列表信息
 * 它处理了错误情况，并在获取数据失败时抛出错误
 * 
 * @param form 包含分页信息和组ID的对象
 * @param form.page 页码，表示请求的链接列表是第几页
 * @param form.pageSize 每页数量，表示每页包含的链接数量
 * @param form.groupId 组ID，用于指定需要获取链接的组
 * @throws {Error} 如果服务端返回的code不为0，表示获取链接列表失败，抛出错误
 * @returns 返回包含链接列表数据的Promise对象
 */
export const LinksGetListsApi = async (form: {
    groupId: string, page: number, size: number,
}) => {
    try {
        // 调用服务端接口获取链接列表
        const { data, code, msg } = await DefaultService.postLinkServerApiLinkV1Page(form);
        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取链接列表失败');
        // 返回链接列表数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取链接列表失败: ${(error as Error).message}`);
    }
}

/**
 * 更新链接的API接口
 * 
 * 此函数通过调用服务端接口来更新特定链接的信息
 * 它允许更新链接的组、短链码、标题、域名ID和域名类型
 * 
 * @param form 包含更新信息的对象
 * @param form.groupId 组ID，可选参数
 * @param form.code 短链码，可选参数
 * @param form.title 链接标题，可选参数
 * @param form.domainId 域名ID，可选参数
 * @param form.domainType 域名类型，可选参数
 * @throws {Error} 如果服务端返回的code不为0，表示更新失败，将抛出错误
 */
export const LinkUpdateApi = async (form: {
    /**
     * 短链码
     */
    code?: string;
    /**
     * 标题
     */
    title?: string;
    /**
     * 域名id
     */
    domainId?: string;
    /**
     * 域名类型
     */
    domainType?: string;
}) => {
    try {
        console.log('LinkUpdateApi', form);
        // 调用服务端接口更新链接信息
        const { code, msg } = await DefaultService.postLinkServerApiLinkV1Update(form);

        // 检查响应码，如果不为0则表示更新失败，抛出错误
        if (code !== 0) throw new Error(msg || '更新链接失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`更新链接失败: ${(error as Error).message}`);
    }
}


/**
 * 异步函数 LinkDeleteApi 用于调用服务端接口删除指定的短链码链接
 * 它接受一个对象参数 form，该对象包含 groupId（组ID）和 code（短链码）
 * 如果删除失败，函数会抛出一个错误
 * 
 * @param {Object} form - 包含 groupId 和 code 的对象，用于指定要删除的短链码
 * @param {number} form.groupId - 组ID，用于区分不同的组
 * @param {string} form.code - 短链码，用于标识要删除的链接
 * @throws {Error} 如果删除链接失败，会抛出一个包含错误信息的错误对象
 */
export const LinkDeleteApi = async (form: { groupId: string, code: string }) => {
    try {
        // 调用服务端接口删除指定短链码的链接
        const { code: responseCode, msg } = await DefaultService.postLinkServerApiLinkV1Del(form);
        // 检查响应码，如果不为0则表示删除失败，抛出错误
        if (responseCode !== 0) throw new Error(msg || '删除链接失败');
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`删除链接失败: ${(error as Error).message}`);
    }
}