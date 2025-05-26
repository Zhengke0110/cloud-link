/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AccountLoginRequest } from '../models/AccountLoginRequest';
import type { AccountRegisterRequest } from '../models/AccountRegisterRequest';
import type { AccountUpdateRequest } from '../models/AccountUpdateRequest';
import type { ConfirmOrderRequest } from '../models/ConfirmOrderRequest';
import type { FrequentSourceRequset } from '../models/FrequentSourceRequset';
import type { JsonData } from '../models/JsonData';
import type { LinkGroupAddRequest } from '../models/LinkGroupAddRequest';
import type { LinkGroupUpdateRequest } from '../models/LinkGroupUpdateRequest';
import type { PayCallbackRequest } from '../models/PayCallbackRequest';
import type { ProductOrderPageRequest } from '../models/ProductOrderPageRequest';
import type { QueryDeviceRequest } from '../models/QueryDeviceRequest';
import type { RegionQueryRequest } from '../models/RegionQueryRequest';
import type { SendCodeRequest } from '../models/SendCodeRequest';
import type { ShortLinkAddRequest } from '../models/ShortLinkAddRequest';
import type { ShortLinkDelRequest } from '../models/ShortLinkDelRequest';
import type { ShortLinkPageRequest } from '../models/ShortLinkPageRequest';
import type { ShortLinkUpdateRequest } from '../models/ShortLinkUpdateRequest';
import type { TrafficPageRequest } from '../models/TrafficPageRequest';
import type { UseTrafficRequest } from '../models/UseTrafficRequest';
import type { VisitRecordPageRequest } from '../models/VisitRecordPageRequest';
import type { VisitTrendQueryRequest } from '../models/VisitTrendQueryRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class DefaultService {
    /**
     * 使用流量包接口
     * 该接口接收一个UseTrafficRequest对象作为请求体，处理用户使用流量包的请求
     * 主要用于减少用户账户中的流量余额
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiTrafficV1Reduce(
        requestBody?: UseTrafficRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/traffic/v1/reduce',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 分页查询流量包列表，查看可用的流量包
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiTrafficV1Page(
        requestBody?: TrafficPageRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/traffic/v1/page',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 查找某个流量包详情
     * @param trafficId 流量包的唯一标识符
     * @returns JsonData
     * @throws ApiError
     */
    public static getAccountServerApiTrafficV1Detail(
        trafficId: string,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/account-server/api/traffic/v1/detail/{trafficId}',
            path: {
                'trafficId': trafficId,
            },
        });
    }
    /**
     * 文件上传接口，最大默认1M
     * 该方法负责处理用户图像的上传请求，包括对文件的格式、扩展名等进行判断
     * 使用@PostMapping注解限定HTTP的POST请求，路径为"upload"
     * @param token
     * @param formData
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiAccountV1Upload(
        token?: string,
        formData?: {
            /**
             * 用户上传的文件，通过@RequestPart注解指定文件参数名为"file"
             */
            file: Blob;
        },
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/account/v1/upload',
            headers: {
                'token': token,
            },
            formData: formData,
            mediaType: 'multipart/form-data',
        });
    }
    /**
     * 用户注册
     * <p>
     * 此方法处理用户注册请求，接收注册信息并返回注册结果
     * 使用PostMapping注解限定HTTP请求方法为POST，路径为"register"
     * 使用RequestBody注解将HTTP请求的JSON格式请求体转换为AccountRegisterRequest对象
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiAccountV1Register(
        requestBody?: AccountRegisterRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/account/v1/register',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 用户登录
     * <p>
     * 此方法处理用户的登录请求它期望接收一个AccountLoginRequest对象作为参数，
     * 该对象中包含了登录所需的信息，例如用户名和密码这些信息用于验证用户身份
     * 登录成功后，将返回一个包含成功信息的JsonData对象；如果登录失败，则返回一个包含错误信息的JsonData对象
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiAccountV1Login(
        requestBody?: AccountLoginRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/account/v1/login',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 获取账户详情信息
     * <p>
     * 通过GET请求调用此方法，以获取账户的详细信息该方法不接受任何参数，
     * 并返回一个包含账户详情的JsonData对象主要用于前端展示账户详细信息的页面
     * @returns JsonData
     * @throws ApiError
     */
    public static getAccountServerApiAccountV1Detail(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/account-server/api/account/v1/detail',
        });
    }
    /**
     * 更新用户信息
     * <p>
     * 此方法处理用户信息更新请求，接收更新信息并返回更新结果
     * 使用PostMapping注解限定HTTP请求方法为POST，路径为"update"
     * 使用RequestBody注解将HTTP请求体转换为AccountUpdateRequest对象
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiAccountV1Update(
        requestBody?: AccountUpdateRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/account/v1/update',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 用户登出
     * <p>
     * 此方法处理用户登出请求，使当前token失效
     * @returns JsonData
     * @throws ApiError
     */
    public static getAccountServerApiAccountV1Logout(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/account-server/api/account/v1/logout',
        });
    }
    /**
     * 生成验证码
     * @returns any
     * @throws ApiError
     */
    public static getAccountServerApiNotifyV1Captcha(): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/account-server/api/notify/v1/captcha',
        });
    }
    /**
     * 处理发送验证码请求
     * 该方法用于验证用户提交的验证码，并在验证成功后发送新的验证码
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postAccountServerApiNotifyV1SendCode(
        requestBody?: SendCodeRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/account-server/api/notify/v1/send_code',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 查看商品列表接口
     * <p>
     * 此方法响应GET请求，提供商品列表数据
     * 它没有输入参数，因为GET请求的性质不需要用户发送数据到服务器
     * 返回的是一个JsonData对象，其中包含了从productService获取的商品列表
     * 使用List<ProductVO>类型来存储商品数据，ProductVO代表商品的视图对象，用于数据传输
     * 使用JsonData.buildSuccess方法构建返回对象，确保客户端能够接收到成功状态和商品列表数据
     * @returns JsonData
     * @throws ApiError
     */
    public static getShopServerApiProductV1List(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/shop-server/api/product/v1/list',
        });
    }
    /**
     * 查看商品详情
     * <p>
     * 此方法通过商品ID获取并返回商品的详细信息它使用GET请求来访问资源，
     * 并以JSON格式返回商品信息
     * @param productId 商品ID，用于标识特定的商品
     * @returns JsonData
     * @throws ApiError
     */
    public static getShopServerApiProductV1Detail(
        productId: number,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/shop-server/api/product/v1/detail/{productId}',
            path: {
                'productId': productId,
            },
        });
    }
    /**
     * WechatPayCallback
     * @param requestBody
     * @returns any
     * @throws ApiError
     */
    public static postShopServerApiCallbackOrderV1Wechat(
        requestBody?: PayCallbackRequest,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/shop-server/api/callback/order/v1/wechat',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 获取下单令牌
     * <p>
     * 本方法用于生成和返回一个下单所需的令牌(token)，以确保用户在下单时的操作合法性
     * 它首先从ThreadLocal中获取当前用户的账户编号，然后生成一个随机令牌，并将该令牌与账户编号组合，
     * 作为Redis中的键，用于存储和验证令牌的有效性
     * @returns JsonData
     * @throws ApiError
     */
    public static getShopServerApiOrderV1Token(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/shop-server/api/order/v1/token',
        });
    }
    /**
     * 处理产品订单的分页查询请求
     * <p>
     * 该方法接收一个ProductOrderPageRequest对象作为参数，其中包含了分页查询的相关信息，
     * 如页码、每页记录数以及可能的筛选条件等它通过HTTP POST方法接收请求，
     * 请求体中包含分页查询的具体参数，并返回一个JsonData对象，其中包含了查询结果
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postShopServerApiOrderV1Page(
        requestBody?: ProductOrderPageRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/shop-server/api/order/v1/page',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 查询订单状态
     * <p>
     * 此方法通过订单号查询订单的状态，并以Json格式返回结果
     * 如果订单号不存在或状态为空，则返回特定的错误码
     * 否则，返回订单的当前状态
     * @param outTradeNo 订单号，用于查询订单状态
     * @returns JsonData
     * @throws ApiError
     */
    public static getShopServerApiOrderV1QueryState(
        outTradeNo: string,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/shop-server/api/order/v1/queryState',
            query: {
                'outTradeNo': outTradeNo,
            },
        });
    }
    /**
     * 确认订单接口
     * 根据订单请求信息，确认订单并根据支付类型和客户端类型进行相应处理
     * @param requestBody
     * @returns any
     * @throws ApiError
     */
    public static postShopServerApiOrderV1Confirm(
        requestBody?: ConfirmOrderRequest,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/shop-server/api/order/v1/confirm',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 列举全部可用域名列表
     * <p>
     * 该方法通过GET请求获取系统中全部可用的域名列表，并以JSON格式返回
     * 主要用于前端展示或系统间数据交互
     * @returns JsonData
     * @throws ApiError
     */
    public static getLinkServerApiDomainV1List(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/link-server/api/domain/v1/list',
        });
    }
    /**
     * 处理测试请求，主要用于验证短链码和账户号的关联
     * 此方法通过执行Lua脚本在Redis中检查和设置短链码与账户号的映射关系
     * @param code 短链码，用于识别特定的资源或信息
     * @param accountNo 账户号，代表用户账户的唯一标识
     * @returns JsonData
     * @throws ApiError
     */
    public static getLinkServerApiDomainV1Test(
        code: string,
        accountNo: number,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/link-server/api/domain/v1/test',
            query: {
                'code': code,
                'accountNo': accountNo,
            },
        });
    }
    /**
     * 转发请求
     * 解析 301还是302，这边是返回http code是302
     * <p>
     * 知识点一，为什么要用 301 跳转而不是 302 呐？
     * <p>
     * 301 是永久重定向，302 是临时重定向。
     * <p>
     * 短地址一经生成就不会变化，所以用 301 是同时对服务器压力也会有一定减少
     * <p>
     * 但是如果使用了 301，无法统计到短地址被点击的次数。
     * <p>
     * 所以选择302虽然会增加服务器压力，但是有很多数据可以获取进行分析
     * @param shortLinkCode 短链接代码
     * @returns any
     * @throws ApiError
     */
    public static get(
        shortLinkCode: string,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/{shortLinkCode}',
            path: {
                'shortLinkCode': shortLinkCode,
            },
        });
    }
    /**
     * 处理添加链接组的请求
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postLinkServerApiGroupV1Add(
        requestBody?: LinkGroupAddRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/link-server/api/group/v1/add',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 处理删除链接分组的请求
     * @param groupId 需要删除的分组的ID
     * @returns JsonData
     * @throws ApiError
     */
    public static deleteApiGroupV1Del(
        groupId: number,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/group/v1/del/{groupId}',
            path: {
                'groupId': groupId,
            },
        });
    }
    /**
     * 获取指定ID的友链分组详细信息
     * @param groupId 友链分组的唯一标识符
     * @returns JsonData
     * @throws ApiError
     */
    public static getLinkServerApiGroupV1Detail(
        groupId: number,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/link-server/api/group/v1/detail/{groupId}',
            path: {
                'groupId': groupId,
            },
        });
    }
    /**
     * 获取所有链接分组信息
     * <p>
     * 该方法通过GET请求处理[list]路径，返回所有链接分组的列表
     * 它调用了LinkGroupService中的listAllGroup方法来获取数据，并将结果封装到JsonData中返回
     * @returns JsonData
     * @throws ApiError
     */
    public static getLinkServerApiGroupV1List(): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/link-server/api/group/v1/list',
        });
    }
    /**
     * 处理更新链接分组的请求
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static putLinkServerApiGroupV1Update(
        requestBody?: LinkGroupUpdateRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/link-server/api/group/v1/update',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 检查短链的有效性
     * @param shortLinkCode 短链代码，用于解析短链信息
     * @param rpcToken
     * @returns JsonData
     * @throws ApiError
     */
    public static getLinkServerApiLinkV1Check(
        shortLinkCode: string,
        rpcToken: string,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/link-server/api/link/v1/check',
            headers: {
                'rpc-token': rpcToken,
            },
            query: {
                'shortLinkCode': shortLinkCode,
            },
        });
    }
    /**
     * 创建短链接的控制器方法
     * 该方法接收一个HTTP POST请求，用于添加新的短链接信息
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postLinkServerApiLinkV1Add(
        requestBody?: ShortLinkAddRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/link-server/api/link/v1/add',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 根据组ID分页查询短链接信息
     * 此方法使用@RequestMapping注解来映射HTTP请求到此方法
     * 请求体应包含ShortLinkPageRequest对象，其中包含分页查询所需的信息
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postLinkServerApiLinkV1Page(
        requestBody?: ShortLinkPageRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/link-server/api/link/v1/page',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 删除短链
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postLinkServerApiLinkV1Del(
        requestBody?: ShortLinkDelRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/link-server/api/link/v1/del',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 更新短链
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postLinkServerApiLinkV1Update(
        requestBody?: ShortLinkUpdateRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/link-server/api/link/v1/update',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 处理页面访问记录的请求
     * <p>
     * 此方法用于处理分页查询访问记录的请求它首先检查请求的总条数是否超过限制，
     * 如果超过，则返回错误信息；否则，调用服务层方法获取分页数据并返回
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postDataServerApiVisitStatsV1PageRecord(
        requestBody?: VisitRecordPageRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/data-server/api/visitStats/v1/pageRecord',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 查询时间范围内的，地区访问分布
     * <p>
     * 该方法通过接收一个请求对象来查询特定时间范围内不同地区的访问分布情况
     * 它使用了@RequestMapping注解来映射HTTP请求到此方法，并使用@RequestBody注解来将HTTP请求的正文转换为RegionQueryRequest对象
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postDataServerApiVisitStatsV1RegionDay(
        requestBody?: RegionQueryRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/data-server/api/visitStats/v1/regionDay',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 访问趋势图
     * <p>
     * 该方法用于处理查询访问趋势的请求它接收一个VisitTrendQueryRequest对象作为参数，
     * 该对象包含了查询访问趋势所需的参数信息方法通过调用statsService的queryVisitTrend方法
     * 获取访问趋势数据，并将结果封装在JsonData对象中返回
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postDataServerApiVisitStatsV1Trend(
        requestBody?: VisitTrendQueryRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/data-server/api/visitStats/v1/trend',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 高频来源统计接口
     * 该接口用于获取系统中频繁访问的来源信息，通过分析访问数据来确定主要的流量来源
     * 对于参数和返回值的详细说明，采用JSON格式进行数据交互，以适应前后端分离的架构设计
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postDataServerApiVisitStatsV1FrequentSource(
        requestBody?: FrequentSourceRequset,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/data-server/api/visitStats/v1/frequentSource',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * 查询设备访问分布情况
     * <p>
     * 该方法通过接收一个QueryDeviceRequest对象作为参数，来查询设备的访问分布情况
     * 它使用了@RequestMapping注解来映射HTTP请求到此方法，参数通过@RequestBody注解指定，意味着请求体中的内容将被转换为方法参数
     * @param requestBody
     * @returns JsonData
     * @throws ApiError
     */
    public static postDataServerApiVisitStatsV1DeviceInfo(
        requestBody?: QueryDeviceRequest,
    ): CancelablePromise<JsonData> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/data-server/api/visitStats/v1/deviceInfo',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
