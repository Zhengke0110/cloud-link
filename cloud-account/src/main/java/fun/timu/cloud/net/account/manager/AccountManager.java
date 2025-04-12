package fun.timu.cloud.net.account.manager;

import fun.timu.cloud.net.account.model.DO.Account;

import java.util.List;

public interface AccountManager {
    int insert(Account accountDO);

    List<Account> findByPhone(String phone);
}
