package com.luban.client.application.configuration;

import java.util.concurrent.TimeUnit;

public class ConfTest {
    public static void main(String[] args) throws InterruptedException {
        ConnectionConfig config = new ConnectionConfig();
        config.initConnect(null);
        config.syncConnectionToZookoopeer();
        TimeUnit.SECONDS.sleep(10);
        // 修改值
        config.update(new ConnectionDao("jdbc:mysql://127.0.0.1:3306/test_db?useUnicode=true&characterEncoding=utf-8",
                "root", "12345", "com.mysql.jdbc.Driver"));
    }
}
