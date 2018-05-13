package com.maochong.xiaojun.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class BIOClientOne {

    public static void main(String[] args) {
        try {
            // 开一条路，开一条小路
            Socket client = new Socket("localhost",8100);
            // 输出流通道打开
            OutputStream os = client.getOutputStream();
            // 产生一个随机的字符串
            String uuid = UUID.randomUUID().toString()+"\n"+UUID.randomUUID().toString();
            os.write(uuid.getBytes());
            os.close();
            client.close();
        }
        catch (Exception ex)
        {

        }
    }
}
