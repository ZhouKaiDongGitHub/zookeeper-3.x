package com.luban.client.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperClientTest {
    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper  = new ZooKeeper("192.168.124.130:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("链接的时候:"+event);
            }
        });
        System.in.read();
    }
}