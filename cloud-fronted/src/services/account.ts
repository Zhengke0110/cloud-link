import { DefaultService, OpenAPI } from '@/generated'


/**
 * 账户登录异步函数
 * 
 * 此函数用于处理账户登录请求，需要传入包含手机号和密码的信息对象
 * 如果手机号或密码为空，则抛出错误；否则，构造请求体并调用服务接口进行登录
 * 
 * @param info 账户登录请求对象，必须包含手机号和密码
 * @returns Promise<AccountLoginResponse> 登录成功后，返回账户登录响应对象的Promise
 * @throws Error 如果手机号或密码为空，则抛出错误
 */
export const AccountLoginApi = async (info: { phone: string, pwd: string }): Promise<boolean> => {
    if (!info.phone || !info.pwd) {
        throw new Error('手机号与密码不能为空!')
    }

    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(info.phone)) throw new Error('手机号格式不正确')

    try {
        const { data, msg } = await DefaultService.postAccountServerApiAccountV1Login(info)
        if (data == null && msg != null) throw new Error(msg)

        if (typeof data !== 'string') throw new Error('登录失败：无效的token返回')

        // 保存token到本地存储
        localStorage.setItem('userToken', data)
        
        // 设置请求头
        OpenAPI.HEADERS = {
            'token': `${data}`,
        }
        return true
    } catch (error: any) {
        const errorMessage = error?.message ? error.message : error || '登录失败，请稍后再试'
        throw new Error(errorMessage)
    }
}


/**
 * 获取验证码接口
 * 
 * 此函数返回一个URL字符串，用于获取验证码服务
 * 验证码通常用于用户注册、登录或找回密码等场景，以确保操作的安全性
 * 
 * @returns {string} 验证码服务的URL地址
 */
export const GetCaptchaApi = () => {
    return OpenAPI.BASE + '/account-server/api/notify/v1/captcha'
}

/**
 *  通过手机号发送验证码
 * 
 * 此函数用于通过手机号码发送验证码，需要传入一个包含必要信息的请求对象
 * 如果请求对象中缺少图片验证码或手机号，将抛出错误
 * 
 * @param info 发送验证码所需的请求信息，包括图片验证码和目标手机号
 * @throws 如果图片验证码或手机号为空，则抛出错误
 * @returns 返回发送验证码的异步操作结果
 */
export const SendCodeByPhoneApi = async (info: { captcha: string; to: string; }) => {
    // 检查请求信息中是否包含图片验证码和目标手机号，如果任一为空，则抛出错误
    if (!info.captcha || !info.to) {
        throw new Error('图片验证码与手机号不能为空!')
    }
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(info.to)) {
        throw new Error('手机号格式不正确!')
    }
    const captchaRegex = /^\d{4}$/;
    if (!captchaRegex.test(info.captcha)) {
        throw new Error('图片验证码格式不正确!')
    }
    try {
        return await DefaultService.postAccountServerApiNotifyV1SendCode(info)
    } catch (error) {
        // 包装网络错误或其他异常，便于统一处理
        throw new Error(`发送验证码失败: ${(error as Error).message}`)
    }
}

/**
 * 注册账户服务
 * 
 * 此函数通过调用服务API进行账户注册，使用提供的账户信息作为参数
 * 它是账户注册流程中的关键步骤，负责与服务器通信，提交用户信息
 * 
 * @param info 账户注册所需的信息，包括但不限于用户名、密码等
 * @returns 返回服务器的响应结果，通常包含注册成功或失败的信息
 */
export const AccountRegisterApi = async (info: {
    headImg: string;
    phone: string;
    pwd: string;
    mail: string;
    username: string;
    code: string;
}) => {
    try {
        // 基本输入验证
        if (!info.mail || !/^\S+@\S+\.\S+$/.test(info.mail)) {
            throw new Error('邮箱地址不正确');
        }

        if (!info.pwd || info.pwd.length < 8) {
            throw new Error('密码长度必须至少为8个字符');
        }

        if (!info.phone) {
            throw new Error('手机号不能为空');
        }

        const phoneRegex = /^1[3-9]\d{9}$/;
        if (!phoneRegex.test(info.phone)) {
            throw new Error('手机号格式不正确');
        }

        if (!info.code || info.code.length !== 6) {
            throw new Error('验证码必须为6位数字');
        }

        const { code, msg } = await DefaultService.postAccountServerApiAccountV1Register(info);
        if (code != 0) {
            throw new Error(msg || '注册失败');
        }
        return true;
    }
    catch (error: any) {
        const errorMessage = error?.message ? error.message : error || '注册失败'
        throw new Error(errorMessage)
    }
}

/**
 * 获取账户详情
 * 
 * 此函数通过调用DefaultService的getAccountServerApiAccountV1Detail方法来获取账户的详细信息
 * 由于该功能涉及到具体的业务逻辑和API调用，因此需要在函数级别进行注释说明
 * 
 * @returns Promise<AccountDetail> 返回一个Promise，解析为账户详情对象
 */
export const GetAccountDetailApi = async () => {
    return await DefaultService.getAccountServerApiAccountV1Detail()
}

/**
 * 账户登出
 * 
 * 此函数用于处理账户登出请求，调用DefaultService的getAccountServerApiAccountV1Logout方法
 * 该方法通常会清除用户的会话信息，并返回登出结果
 * 
 * @returns Promise<void> 返回一个Promise，解析为void，表示登出操作完成
 */
export const AccountLogoutApi = async () => { 
    try {
        const result = await DefaultService.getAccountServerApiAccountV1Logout()
        // 清除本地存储的token
        localStorage.removeItem('userToken')
        localStorage.removeItem('rememberedUser')
        // 清除请求头
        OpenAPI.HEADERS = {}
        return result
    } catch (error) {
        // 即使请求失败也要清除本地token
        localStorage.removeItem('userToken')
        localStorage.removeItem('rememberedUser')
        OpenAPI.HEADERS = {}
        throw error
    }
}

/**
 * 更新账户信息的异步函数
 * 
 * 该函数通过向服务器发送POST请求来更新用户的账户信息，包括头像、邮箱和用户名
 * 它体现了函数式的编程风格，通过参数传递需要更新的信息
 * 
 * @param info 包含要更新的用户信息的对象
 * @param info.headImg 可选字段，用户的新头像URL
 * @param info.mail 可选字段，用户的新邮箱地址
 * @param info.username 可选字段，用户的新用户名
 * 
 * 注意：该函数会抛出错误，当服务器返回非0代码或请求失败时，表示更新账户失败
 */
export const AccountUpdateApi = async (info: { headImg?: string; mail?: string; username?: string; }) => {
    try {
        // 发送更新请求到服务器
        const { code } = await DefaultService.postAccountServerApiAccountV1Update(info)
        // 如果服务器返回代码非0，表示更新失败，抛出错误
        if (code != 0) throw new Error('更新账户失败，请稍后再试');
    } catch (error) {
        // 捕获到任何错误时，均重新抛出一个通用的更新失败错误
        throw new Error(`更新账户失败`);
    }
}

/**
 * 上传账户图片到服务器
 * 
 * 本函数通过调用后端API，将用户选择的文件上传到服务器，并返回上传结果
 * 主要涉及表单数据的准备、API请求的发送以及响应结果的处理
 * 
 * @param file 用户选择的文件，类型为File
 * @returns 返回一个Promise，解析为上传结果的对象
 * @throws 如果上传失败或服务器返回错误代码，则抛出错误
 */
export const AccountUploadImageApi = async (file: File): Promise<any> => {
    // 创建表单数据对象，用于包装文件数据
    const formData = new FormData();
    // 将文件添加到表单数据中，键名为'file'
    formData.append('file', file);

    // 构造上传API的URL
    const url = OpenAPI.BASE + '/account-server/api/account/v1/upload';

    // 发送POST请求到上传API，请求体为表单数据
    const response = await fetch(url, {
        method: 'POST',
        body: formData,
    });

    // 解析响应的JSON数据
    const result = await response.json();

    // 检查响应状态和结果代码，如果不符合预期，则抛出错误
    if (!response.ok || result.code !== 0) {
        throw new Error(result.msg || '文件上传失败');
    }

    // 返回上传结果
    return result;
};


/**
 * 获取流量数据列表的API函数
 * 
 * 此函数通过向服务器发送POST请求，获取分页后的流量数据列表
 * 能够捕获请求过程中的错误，确保在数据获取失败时抛出易于理解的错误信息
 * 
 * @param page 页码，表示请求的流量数据所在页数
 * @param size 每页的流量数据条数
 * @returns 返回一个Promise，解析为流量数据列表对象
 * @throws 当流量数据获取失败时，抛出错误
 */
export const TrafficDataListApi = async (page: number = 1, size: number = 20) => {
    try {
        // 发送POST请求获取流量数据列表
        const { data, code, msg } = await DefaultService.postAccountServerApiTrafficV1Page({
            page: page,
            size: size
        });

        // 检查响应状态码，如果不为0则表示获取数据失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取流量数据失败');

        // 返回获取到的流量数据列表
        return data;
    } catch (error) {
        // 捕获请求过程中的错误，并抛出带有错误信息的新错误
        throw new Error(`获取流量数据失败: ${(error as Error).message}`);
    }
}


export const TrafficDetailByIDApi = async (id: string) => {
    try {
        // 发送GET请求获取指定ID的流量数据详情
        const { data, code, msg } = await DefaultService.getAccountServerApiTrafficV1Detail(id);

        // 检查响应状态码，如果不为0则表示获取数据失败，抛出错误
        if (code !== 0) throw new Error(msg || '获取流量数据详情失败');
        // 返回获取到的流量数据详情
        return data;
    } catch (error) {
        // 捕获请求过程中的错误，并抛出带有错误信息的新错误
        throw new Error(`获取流量数据详情失败: ${(error as Error).message}`);
    }
}

/**
 * 初始化认证状态
 * 从本地存储中恢复token并设置到请求头中
 * 应在应用启动时调用
 */
export const initAuthFromStorage = () => {
    const token = localStorage.getItem('userToken')
    if (token) {
        OpenAPI.HEADERS = {
            'token': token,
        }
        return true
    }
    return false
}