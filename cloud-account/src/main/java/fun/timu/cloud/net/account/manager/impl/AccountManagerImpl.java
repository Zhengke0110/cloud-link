package fun.timu.cloud.net.account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.account.manager.AccountManager;
import fun.timu.cloud.net.account.mapper.AccountMapper;
import fun.timu.cloud.net.account.model.DO.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountManagerImpl implements AccountManager {
    private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

    private final AccountMapper accountMapper;

    public AccountManagerImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public int insert(Account accountDO) {
        return accountMapper.insert(accountDO);
    }


    @Override
    public List<Account> findByPhone(String phone) {

        List<Account> accountDOList = accountMapper.selectList(new QueryWrapper<Account>().eq("phone", phone));
        return accountDOList;
    }

    @Override
    public Account detail(long accountNo) {
        Account accountDO = accountMapper.selectOne(new QueryWrapper<Account>().eq("account_no", accountNo));

        return accountDO;
    }
}
