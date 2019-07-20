package com.kay.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 和事务管理相关的工具类，它包括，开启事务，提交事务，回滚事务和释放事务
 */
@Component("txManager")
@Aspect//配置切面
public class TransactionManager {

    //当前线程上的connection的获取
    @Autowired
    private ConnectionUtils connectionUtils;

    @Pointcut("execution(* com.kay.service.impl.*.*(..))")
    private void pt1(){}
/*
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }
*/
    /**
     * 开启事务
     */
    //@Before("pt1()")
    public void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    /**
     * 提交事务
     */
   //@AfterReturning("pt1()")
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 回滚事务
     */
    //@AfterThrowing("pt1()")
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 释放连接
     */
    //@After("pt1()")
    public void release(){
        try {
            connectionUtils.getThreadConnection().close();//把线程还回线程池中
            connectionUtils.removeConnection();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //由于是spring的事务处理的顺序有问题导致上边的每个事务通过注解的方式执行因spring的顺序问题会发生异常
    //因此再这里运用了环绕注解来手动添加通知的顺序
    @Around("pt1()")
    public Object arroundAdvice(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try{
            //1.获取参数
            Object[] args = pjp.getArgs();
            //2.开启事务
            this.beginTransaction();
            //3.执行方法
            rtValue = pjp.proceed(args);
            //4.提交事务
            this.commit();

            //返回结果
            return rtValue;
        }catch (Throwable t){
            this.rollback();
            throw new RuntimeException(t);
        }finally {
            //6.释放资源
            this.release();
        }
    }

}
