import { DefaultService } from '@/generated'

/**
 * 获取产品列表的API函数
 * 此函数尝试调用服务端接口以获取产品列表，并对结果进行处理
 * 如果获取失败，会抛出一个错误
 * 
 * @throws {Error} 当获取商品列表失败时抛出错误，包含失败的原因
 * @returns {Promise<any>} 返回一个Promise，解析为商品列表数据
 */
export const ProductGetListApi = async () => {
    try {
        // 调用服务端接口获取商品列表
        const { data, code, msg } = await DefaultService.getShopServerApiProductV1List();

        // 检查响应码，如果不为0则表示获取失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取商品列表失败');
        // 返回商品列表数据
        return data;
    } catch (error) {
        // 捕获错误，并重新抛出一个包含错误信息的错误对象
        throw new Error(`获取商品列表失败: ${(error as Error).message}`);
    }
}

/**
 * 根据商品ID获取商品详细信息
 * 此函数调用服务端API以获取商品详情，如果请求失败或状态码不为0，则抛出错误
 * 
 * @param id 商品ID，用于指定需要获取详情的商品
 * @returns 返回Promise，解析为商品详情对象
 * @throws 当商品详情获取失败时，抛出错误
 */
export const ProductGetDetailByIDApi = async (id: string) => {
    try {
        // 调用服务端API获取商品详情
        const { data, code, msg } = await DefaultService.getShopServerApiProductV1Detail(id);

        // 检查返回状态码，如果不为0则抛出错误
        if (code !== 0) throw new Error(msg || '获取商品详情失败');
        // 返回商品详情数据
        return data;
    } catch (error) {
        // 捕获异常，并重新抛出更详细的错误信息
        throw new Error(`获取商品详情失败: ${(error as Error).message}`);
    }
}


/**
 * 获取订单令牌
 * 
 * 本函数通过调用服务端API来获取订单令牌，是获取订单信息流程中的关键一步
 * 它没有参数，但会返回从服务器获取的订单令牌数据，或者在失败时抛出错误
 * 
 * @returns {Promise<any>} 返回一个Promise，解析为订单令牌数据
 * @throws {Error} 如果获取订单令牌失败，会抛出错误
 */
export const ProductGetOrderTokenApi = async () => {
    try {
        // 调用服务端API获取订单令牌
        const { data, code, msg } = await DefaultService.getShopServerApiOrderV1Token();

        // 检查返回状态码，如果不为0则抛出错误
        if (code !== 0) throw new Error(msg || '获取订单令牌失败');
        // 返回订单令牌数据
        return data;
    } catch (error) {
        // 捕获异常，并重新抛出更详细的错误信息
        throw new Error(`获取订单令牌失败: ${(error as Error).message}`);
    }
}

/**
 * 异步获取订单状态
 * 
 * 本函数通过调用ShopServer的API来查询订单状态，根据订单的唯一交易号outTradeNo进行查询
 * 主要目的是为了获取订单的当前状态，以便进行后续的业务处理
 * 
 * @param outTradeNo 订单的唯一交易号，用于标识和查询特定的订单
 * @returns 返回订单的状态数据
 * @throws 如果API调用失败或返回的code不为0，则抛出错误
 */
export const ProductGetOrderStatusApi = async (outTradeNo: string) => {
    try {
        // 调用ShopServerAPI查询订单状态
        const { code, msg, data } = await DefaultService.getShopServerApiOrderV1QueryState(outTradeNo);
        // 如果返回的code不为0，则表示获取订单状态失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取订单状态失败');
        // 返回订单状态数据
        return data;
    } catch (error) {
        // 捕获异常，并重新抛出更详细的错误信息
        throw new Error(`获取订单状态失败: ${(error as Error).message}`);
    }
}

/**
 * 确认产品订单的API请求函数
 * 
 * 此函数负责向服务器发送产品订单的确认请求它需要一系列的订单信息作为参数，
 * 包括商品ID、购买数量、客户端类型等，并处理服务器的响应如果服务器返回的
 * 状态码不表示成功，则会抛出错误
 * 
 * @param form 包含订单信息的对象
 * @returns 如果订单确认成功，返回true；否则抛出错误
 */
export const ProductOrderConfirmApi = async (form: {
    /**
     * 商品id
     */
    productId?: number;
    /**
     * 购买数量
     */
    buyNum?: number;
    /**
     * 终端类型
     */
    clientType?: string;
    /**
     * 支付类型，微信-银行-支付宝
     */
    payType?: string;
    /**
     * 订单总金额
     */
    totalAmount?: number;
    /**
     * 订单实际支付价格
     */
    payAmount?: number;
    /**
     * 防重令牌
     */
    token?: string;
    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    billType?: string;
    /**
     * 发票抬头
     */
    billHeader?: string;
    /**
     * 发票内容
     */
    billContent?: string;
    /**
     * 发票收票人电话
     */
    billReceiverPhone?: string;
    /**
     * 发票收票人邮箱
     */
    billReceiverEmail?: string;
}) => {
    // 检查必要字段是否存在，如果不存在则抛出错误
    if (!form.productId || !form.buyNum || !form.clientType || !form.token) {
        throw new Error('缺少必要字段: productId, buyNum, clientType, token 必须提供');
    }
    try {
        // 发送确认订单的API请求
        const { code, msg, data } = await DefaultService.postShopServerApiOrderV1Confirm(form);
        // 如果服务器返回的状态码不为0，表示确认失败，抛出错误
        if (code !== 0) throw new Error(msg || '确认订单失败');
        // 确认成功，返回true
        return data;
    } catch (error) {
        // 捕获异常，并重新抛出错误
        throw new Error(`确认订单失败: ${(error as Error).message}`);
    }
}

/**
 * 获取产品订单列表的API函数
 * 
 * 此函数负责调用服务端接口以获取指定页码和大小的订单列表数据
 * 它通过POST请求向服务端发送分页参数，并处理返回的响应数据
 * 如果服务端返回的状态码不为0，表示请求失败，函数将抛出错误
 * 
 * @param page 页码，默认值为1，表示请求第1页数据
 * @param size 每页大小，默认值为20，表示每页请求20条数据
 * @returns 返回一个Promise，解析为订单列表数据
 * @throws 当获取订单列表失败时抛出错误
 */
export const ProductGetOrderListApi = async (page: number = 1, size: number = 20) => {
    try {
        // 调用服务端API获取订单列表
        const { data, code, msg } = await DefaultService.postShopServerApiOrderV1Page({ page, size });

        // 检查返回状态码，如果不为0则抛出错误
        if (code !== 0) throw new Error(msg || '获取订单列表失败');
        // 返回订单列表数据
        return data;
    } catch (error) {
        // 捕获异常，并重新抛出更详细的错误信息
        throw new Error(`获取订单列表失败: ${(error as Error).message}`);
    }
}

/**
 * 处理微信支付回调的异步函数
 * 该函数用于验证微信支付的回调通知参数，并调用相应的API进行订单状态更新
 * 
 * @param form 包含微信支付回调信息的对象
 * @param form.outTradeNo 商家订单号，用于标识一笔交易
 * @param form.tradeState 交易状态，表示交易的成功、失败或待支付等
 * @param form.accountNo 账户号，用于指定接收回调的账户
 * @returns 返回API调用的结果，表示订单状态更新的响应
 * @throws 当输入参数缺失时，抛出错误
 */
export const WeChatPayApi = async (form: {
    outTradeNo: string;
    tradeState: string;
    accountNo: string;
}) => {
    try {
        // 输入验证
        if (!form || !form.outTradeNo || !form.tradeState || !form.accountNo) {
            throw new Error('微信支付回调参数缺失');
        }

        // 调用API处理微信支付回调
        return await DefaultService.postShopServerApiCallbackOrderV1Wechat(form);
    } catch (error) {
        // 错误处理
        console.error('WeChatPayApi 请求失败:', error);
        throw error; // 抛出错误供上层处理
    }
};