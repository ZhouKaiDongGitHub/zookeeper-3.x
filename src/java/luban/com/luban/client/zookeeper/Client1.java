package com.luban.client.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Client1 implements Runnable{

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
        //创建一个节点并且监听
        String path = "/node";
        String data = "node";
        //String result = zooKeeper.create("/node","node".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // System.out.println("create:"+"["+path+"-->"+data+"],result:"+result);
        Stat stat = new Stat();
        byte[] info = new byte[0];
        try {
            info = zooKeeper.getData("/node", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getType().equals(Event.EventType.NodeDataChanged)){
                        System.out.println(" 数据发生改变。");
                    }
                }
            },stat);
            System.out.println("新的数据为："+new String(info));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
