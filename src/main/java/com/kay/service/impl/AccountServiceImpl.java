package com.kay.service.impl;

import com.kay.dao.IAccountDao;
import com.kay.domain.Account;
import com.kay.service.IAccountService;
import com.kay.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户的业务层实现类
 *
 * 事务的控制都应该在业务层，而不应该在持久层
 */
@Service("accountService")
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountDao accountDao;

    private TransactionManager txManager;

    public void setAccountDao(IAccountDao accoutDao) {
        this.accountDao = accoutDao;
    }

    public void setTxManager(TransactionManager txManager) {
        this.txManager = txManager;
    }

    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }

    public void saveAccount(Account account) {
            accountDao.saveAccount(account);
    }

    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    public void deleteAccount(Integer account) {
        accountDao.deleteAccount(account);
    }

    public void transfer(String sourceName,String targetName,Float money){

        System.out.println("transfer........");
        //1.根据名称查询转出账户
        Account source = accountDao.findAccountByName(sourceName);
        //2.根据名称查询转入账户
        Account target = accountDao.findAccountByName(targetName);
        //3.转出账户减钱
        source.setMoney(source.getMoney() - money);
        //4.转入账户加钱
        target.setMoney(target.getMoney() + money);
        //5.更新转出账户
        accountDao.updateAccount(source);
       // int i = 5/0;
        //6.更新转出账户
        accountDao.updateAccount(target);
    }

    public void test() {

    }
}
