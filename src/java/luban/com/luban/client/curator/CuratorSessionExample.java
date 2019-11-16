package com.luban.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

public class CuratorSessionExample {
    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.124.130:2181",
                new RetryNTimes(3,1000));
        client.start();

        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if(connectionState == ConnectionState.LOST){
                    try {
                        if(client.getZookeeperClient().blockUntilConnectedOrTimedOut()){
                            doTask();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        doTask();
    }

    public static void doTask(){
        //创建临时节点....
    }
}
