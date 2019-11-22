package com.luban.client.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *      打开ServerSocketChannel，监听客户端连接
 *     绑定监听端口，设置连接为非阻塞模式
 *     创建Reactor线程，创建多路复用器并启动线程
 *     将ServerSocketChannel注册到Reactor线程中的Selector上，监听ACCEPT事件
 *     Selector轮询准备就绪的key
 *     Selector监听到新的客户端接入，处理新的接入请求，完成TCP三次握手，简历物理链路
 *     设置客户端链路为非阻塞模式
 *     将新接入的客户端连接注册到Reactor线程的Selector上，监听读操作，读取客户端发送的网络消息
 *     异步读取客户端消息到缓冲区
 *     对Buffer编解码，处理半包消息，将解码成功的消息封装成Task
 *     将应答消息编码为Buffer，调用SocketChannel的write将消息异步发送给客户端
 */
public class MyServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(12345),1024);

            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        // handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
