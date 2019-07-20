package com.kay.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 链接的工具类，它用于从数据源中获取一个链接并且实现和线程的绑定
 */
@Component("connectionUtils")
public class ConnectionUtils {

    /**
     * 需要使用ThreadLocal对象把Connection和当前线程绑定，从而使一个线程中只有一个控制事务的对象
     *
     */
    private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

    @Autowired
    private DataSource dataSource;
/*
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
*/
    /**
     * 获取当前线程上的链接
     * @return
     */
    public Connection getThreadConnection(){

        try{
            //1.先从ThreadLoacl上获取
            Connection conn = tl.get();
            //2.判断当前线程上是否有链接
            if(conn == null) {
                //3.从数据源中获取一个链接并且和线程绑定（也就是存入Threadlocal中）
                conn = dataSource.getConnection();
                //conn.setAutoCommit(false);
                tl.set(conn);
            }
            //4.返回当前线程上的链接
            return conn;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 把连接和线程解绑
     */
    public void removeConnection() {
         tl.remove();
    }
}
