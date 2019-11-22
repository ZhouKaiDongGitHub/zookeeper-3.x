package com.luban.client.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MyClient {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();

            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress("127.0.0.1",12345));
            client.register(selector,SelectionKey.OP_CONNECT);

            while (true){
                try{
                    //无论是否有读写事件发生，selector每隔1s被唤醒一次
                    selector.select(1000);
                    //阻塞,只有当至少一个注册的事件发生的时候才会继续.
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    SelectionKey key = null;
                    while(it.hasNext()){
                        key = it.next();
                        it.remove();
                        try{
                            //handleInput(key);
                        }catch(Exception e){
                            if(key != null){
                                key.cancel();
                                if(key.channel() != null){
                                    key.channel().close();
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
