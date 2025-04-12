package fun.timu.cloud.net.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import fun.timu.cloud.net.account.mapper.AccountMapper;
import fun.timu.cloud.net.account.model.DO.Account;
import fun.timu.cloud.net.account.service.AccountService;
import org.springframework.stereotype.Service;

/**
* @author zhengke
* @description 针对表【account】的数据库操作Service实现
* @createDate 2025-04-12 09:08:16
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService {

}




