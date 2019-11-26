package com.luban.client.zookeeper;

public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Client1(),"thread1");
        thread1.start();
        Thread.sleep(5000);
        Thread thread2 = new Thread(new Client2(),"thread2");
        thread2.start();
    }
}
