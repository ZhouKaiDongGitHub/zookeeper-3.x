package com.luban.client.application.configuration;

import java.io.Serializable;

public class ConnectionDao implements Serializable {

    // 数据库连接配置
    private String dbUrl;
    private String username;
    private String password;
    private String driverClass;
    public ConnectionDao(){}
    public ConnectionDao(String dbUrl, String username, String password, String driverClass){
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String toString() {
        return "ConnectionDao{" +
                "dbUrl='" + dbUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driverClass='" + driverClass + '\'' +
                '}';
    }
}
