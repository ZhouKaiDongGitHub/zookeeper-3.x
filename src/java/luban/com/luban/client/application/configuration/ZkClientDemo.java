package com.luban.client.application.configuration;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.concurrent.TimeUnit;

public class ZkClientDemo implements Runnable {

    private String nodeName = "/connectionNode";
    private ConnectionDao connectionDao;

    @Override
    public void run() {
        ZkClient zkClient = new ZkClient(new ZkConnection("192.168.124.130:2181"),5000);
        while (!zkClient.exists(nodeName)){
            System.out.println("配置节点不存在！");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        connectionDao = (ConnectionDao) zkClient.readData(nodeName);
        System.out.println(connectionDao.toString());

        zkClient.subscribeDataChanges(nodeName, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                if(s.equals(nodeName)){
                    System.out.println("节点"+nodeName+"数据更新了。");
                    connectionDao = (ConnectionDao) o;
                }
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                if(s.equals(nodeName)){
                    System.out.println("节点"+nodeName+"被删除了。");
                }
            }
        });

    }
}
