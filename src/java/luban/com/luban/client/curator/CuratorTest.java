package com.luban.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

/**
 * apache开发的curator客户端是为了弥补自己的 zookeeper原生客户端存在的问题
 * 这也是目前主流的zookeeper客户端
 */
public class CuratorTest {
    public static void main(String[] args) throws Exception {
        //链接   重试策略
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.124.130:2181",
                new RetryNTimes(3,1000));
        client.start();

        //创建节点
        client.create().withMode(CreateMode.PERSISTENT).forPath("/data","data".getBytes());

        //Watch机制
        NodeCache nodeCache = new NodeCache(client,"/data");
        //参数为true，表示从服务器立刻拿到缓存，所以下面的初始化不会打印；
        //参数为false，表示不从服务器立刻拿到缓存，所以初始化会立刻打印一次
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("====================================");
            }
        });
        System.in.read();
    }
}
