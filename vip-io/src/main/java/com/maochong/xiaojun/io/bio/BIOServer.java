package com.maochong.xiaojun.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

/**
 * BIO（同步阻塞）服务端
 * @author jokin
 * @date 2018-05-12
 * */
public class BIOServer {
    ServerSocket server;

    // BIO 服务端
    public BIOServer(int port){
        try {
            // 把socket服务启动
            server = new ServerSocket(port);
            System.out.println(String.format("服务启动，端口：%d",port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始监听，并处理逻辑
     * */
    public void listener() throws Exception{
        // 死循环监听
        while (true) {
            //虽然写了一个死循环，如果一直没有客户端连接的话，这里一直不会往下执行
            //等待客户端连接，阻塞方法
            Socket client = server.accept();
            // 拿到输入流，IO阻塞的，相当于一条小路，只能过一辆车
            InputStream is = client.getInputStream();

            while (true) {
                // 缓冲区，数据而已
                byte[] buff = new byte[1024];
                int len = is.read(buff);
                if (len > 0) {
                    AtomicReference<String> content = new AtomicReference<String>(new String(buff));
                    System.out.println(content.get());
                }
                else {
                    break;
                }
            }
        }
    }


    public static void main(String[] args) {
        BIOServer server = new BIOServer(8100);
        try {
            server.listener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
