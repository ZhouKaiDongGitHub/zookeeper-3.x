package com.luban.client.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Client2 implements Runnable{

    @Override
    public void run() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 50000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(Event.KeeperState.SyncConnected.equals(event.getState())){
                        //链接完毕之后才能使用
                        countDownLatch.countDown();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //线程阻塞等着
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //添加一个用户
        zooKeeper.addAuthInfo("digest","kzhou:11111".getBytes());
        List<ACL> aclList = new ArrayList<>();
        aclList.add(new ACL(1,new Id("auth","kzhou:11111:awrcd")));
        try {
            zooKeeper.setACL("/node",aclList,-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //创建一个节点并且监听
        Stat stat = null;
        try {
            stat = zooKeeper.setData("/node","nod@@@@@@@".getBytes(),-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
