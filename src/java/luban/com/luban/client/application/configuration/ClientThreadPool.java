package com.luban.client.application.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 模拟多个客户端获取配置
        executorService.submit(new ZkClientDemo());
        executorService.submit(new ZkClientDemo());
        executorService.submit(new ZkClientDemo());

    }
}
