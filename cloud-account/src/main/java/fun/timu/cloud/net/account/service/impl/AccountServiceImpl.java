package fun.timu.cloud.net.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import fun.timu.cloud.net.account.config.RabbitMQConfig;
import fun.timu.cloud.net.account.controller.request.AccountLoginRequest;
import fun.timu.cloud.net.account.controller.request.AccountRegisterRequest;
import fun.timu.cloud.net.account.manager.AccountManager;
import fun.timu.cloud.net.account.mapper.AccountMapper;
import fun.timu.cloud.net.account.model.DO.Account;
import fun.timu.cloud.net.account.model.VO.AccountVO;
import fun.timu.cloud.net.account.service.AccountService;
import fun.timu.cloud.net.account.service.NotifyService;
import fun.timu.cloud.net.common.enums.AuthTypeEnum;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.enums.SendCodeEnum;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.model.LoginUser;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.IDUtil;
import fun.timu.cloud.net.common.util.JWTUtil;
import fun.timu.cloud.net.common.util.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2025-04-12 09:08:16
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final Long FREE_TRAFFIC_PRODUCT_ID = 1L;
    private final NotifyService notifyService;

    private final AccountManager accountManager;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;


    public AccountServiceImpl(NotifyService notifyService, AccountManager accountManager, RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.notifyService = notifyService;
        this.accountManager = accountManager;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    /**
     * 用户注册功能
     *
     * @param registerRequest 注册请求对象，包含用户输入的注册信息
     * @return 返回注册结果的JSON数据
     */
    @Override
    public JsonData register(AccountRegisterRequest registerRequest) {
        // 初始化验证码验证结果为false
        boolean checkCode = false;
        //判断验证码
        if (StringUtils.isNotBlank(registerRequest.getPhone())) {
            // 调用通知服务检查验证码是否正确
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getPhone(), registerRequest.getCode());
        }
        //验证码错误
        if (!checkCode) {
            // 如果验证码错误，返回错误信息
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }

        // 创建Account对象以存储用户账户信息
        Account accountDO = new Account();

        // 将注册请求中的属性复制到Account对象中
        BeanUtils.copyProperties(registerRequest, accountDO);
        // 设置默认的认证级别
        accountDO.setAuth(AuthTypeEnum.DEFAULT.name());

        // 生成唯一的账号
        accountDO.setAccountNo(Long.valueOf(IDUtil.geneSnowFlakeID().toString()));

        // 设置密码加密所需的秘钥和盐
        accountDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        // 对用户输入的密码进行加密处理
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), accountDO.getSecret());
        accountDO.setPwd(cryptPwd);

        // 调用账户管理器插入新的账户信息
        int rows = accountManager.insert(accountDO);
        // 记录日志，显示插入的行数和账户信息
        logger.info("rows:{},注册成功:{}", rows, accountDO);

        // 用户注册成功后，执行初始化任务，如发放福利等
        userRegisterInitTask(accountDO);

        // 返回注册成功的JSON数据
        return JsonData.buildSuccess();
    }

    /**
     * 用户登录方法
     * <p>
     * 该方法处理用户登录请求，根据手机号查询用户账户信息，并验证密码
     * 如果用户存在且密码正确，生成并返回JWT令牌，否则返回相应的错误信息
     *
     * @param request 用户登录请求对象，包含手机号和密码
     * @return 返回一个JsonData对象，包含登录结果或错误信息
     */
    @Override
    public JsonData login(AccountLoginRequest request) {
        // 根据手机号查询账户信息
        List<Account> accountDOList = accountManager.findByPhone(request.getPhone());

        // 检查查询结果，确保只有一个账户与手机号关联
        if (accountDOList != null && accountDOList.size() == 1) {

            Account accountDO = accountDOList.get(0);

            // 使用MD5加密用户输入的密码，并与数据库中的密码进行比较
            String md5Crypt = Md5Crypt.md5Crypt(request.getPwd().getBytes(), accountDO.getSecret());
            if (md5Crypt.equalsIgnoreCase(accountDO.getPwd())) {

                // 创建LoginUser对象，并从账户信息中复制属性
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(accountDO, loginUser);

                // 生成JWT令牌并返回
                String token = JWTUtil.geneJsonWebTokne(loginUser);
                return JsonData.buildSuccess(token);

            } else {
                // 密码错误，返回错误信息
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }

        } else {
            // 用户未注册，返回错误信息
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
    }

    /**
     * 获取用户账户详细信息
     * <p>
     * 此方法用于获取当前登录用户的账户详细信息它首先从线程局部变量中获取登录用户信息，
     * 然后通过账户管理器获取账户详情，并将账户信息转换后返回
     *
     * @return 返回包含用户账户详细信息的JsonData对象
     */
    @Override
    public JsonData detail() {

        // 从线程局部变量中获取当前登录用户信息
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        // 通过账户管理器根据账户编号获取账户详细信息
        Account accountDO = accountManager.detail(loginUser.getAccountNo());

        // 创建一个账户信息传输对象
        AccountVO accountVO = new AccountVO();

        // 将账户详细信息从账户数据对象复制到账户信息传输对象
        BeanUtils.copyProperties(accountDO, accountVO);

        // 构建并返回包含账户信息传输对象的成功响应Json数据
        return JsonData.buildSuccess(accountVO);
    }


    /**
     * 用户初始化，发放福利：流量包
     *
     * @param accountDO
     */
    private void userRegisterInitTask(Account accountDO) {

        EventMessage eventMessage = EventMessage.builder().messageId(IDUtil.geneSnowFlakeID().toString()).accountNo(accountDO.getAccountNo()).eventMessageType(EventMessageType.TRAFFIC_FREE_INIT.name()).bizId(FREE_TRAFFIC_PRODUCT_ID.toString()).build();

        //发送发放流量包消息
        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(), rabbitMQConfig.getTrafficFreeInitRoutingKey(), eventMessage);

    }
}




