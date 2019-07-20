package com.kay.dao;

import com.kay.domain.Account;

import java.util.List;

/**
 * 持久层的接口
 */
public interface IAccountDao {
    /**
     * 查询所有
     * @return
     */
    List<Account> findAllAccount();

    /**
     * 查询一个
     * @return
     */
    Account findAccountById(Integer accountId);

    /**
     * 保存操作
     * @param account
     */
    void saveAccount(Account account);

    /**
     * 更新操作
     * @param account
     */
    void updateAccount(Account account);

    /**
     * 删除
     * @param account
     */
    void deleteAccount(Integer account);

    /**
     * 根据名称查询账户
     * @param accountName
     * @return  如果由唯一的一个结果就返回，没有结果就返回null
     *          如果结果集超过一个就抛异常
     */
    Account findAccountByName(String accountName);
}
