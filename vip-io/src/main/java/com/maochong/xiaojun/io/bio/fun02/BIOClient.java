package com.maochong.xiaojun.io.bio.fun02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 阻塞式IO 创建客户端
 */
public class BIOClient {
    /**
     * 默认的端口号
     */
    private static int DEFAULT_SERVER_PORT = 6789;

    /**
     * 默认IP
     */
    private static String DEFAULT_SERVER_IP = "127.0.0.1";

    /**
     * 使用默认ip:port 发送消息
     *
     * @param expression 消息内容
     */
    public static void send(String expression) {
        send(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT, expression);
    }

    public static void send(String ip, int port, String expression) {
        System.out.println("算术表达式为：" + expression);
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(ip,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(expression);
            System.out.println(("结果为：" + in.readLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
