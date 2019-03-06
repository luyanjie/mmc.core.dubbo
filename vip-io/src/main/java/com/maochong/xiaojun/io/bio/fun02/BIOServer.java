package com.maochong.xiaojun.io.bio.fun02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO 服务端源码
 * 写法二
 */
public class BIOServer {
    /**
     * 设置默认端口
     */
    private static int DEFAULT_PORT = 6789;
    /**
     * 单例的ServerSocket
     */
    private static ServerSocket serverSocket;

    /**
     * 根据传入的参数设置监听端口，如果没有参数调用以下方法并使用默认值
     */
    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    /**
     * 这个方法不会被大量并发访问，不塌需要考虑效率，直接使用方法同步就行了
     * @param port port
     * @throws IOException 异常
     */
    public synchronized static void start(int port) throws IOException {
        if(serverSocket!=null) return;
        try {
            // 通过构造函数创建ServerSocket
            // 如果端口合法并空闲，服务端就监听成功
            serverSocket = new ServerSocket(port);
            System.out.println("服务端已经启动，端口号为："+port);
            // 通过无限循环监听客户端连接，如果没有客户端接入，将会阻塞在accept方法上
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        }
        finally {
            if(serverSocket!=null){
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
