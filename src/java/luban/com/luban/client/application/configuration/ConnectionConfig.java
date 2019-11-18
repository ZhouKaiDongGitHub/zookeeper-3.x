package com.luban.client.application.configuration;

import org.I0Itec.zkclient.ZkClient;

public class ConnectionConfig {
    private static String url = "jdbc:mysql://127.0.0.1:3306/test_db?useUnicode=true&characterEncoding=utf-8";
    private static String userName = "root";
    private static String passWord = "11111";
    private static String driveClass = "com.mysql.jdbc.Driver";

    private String nodeName = "/connectionNode";
    private ZkClient zkClient;
    private ConnectionDao connectionDao;

    public ConnectionDao initConnect(ConnectionDao connectionDao){
        if(connectionDao == null){
            this.connectionDao = new ConnectionDao(url,userName,passWord,driveClass);
        }else {
            this.connectionDao = connectionDao;
        }
        return this.connectionDao;
    }

    public ConnectionDao update(ConnectionDao connectionDao){
        if(connectionDao != null){
            this.connectionDao = connectionDao;
        }else {
            syncConnectionToZookoopeer();
        }
        return this.connectionDao;
    }

    public void syncConnectionToZookoopeer(){
        if(zkClient == null){
            zkClient = new ZkClient("192.168.124.130:2181");
        }
        if(!zkClient.exists(nodeName)){
            zkClient.createPersistent(nodeName);
        }
        zkClient.writeData(nodeName,connectionDao);
    }
}
