package com.maochong.xiaojun.io.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileOutputProgram {
    static private final byte message[] = { 83, 111, 109, 101, 32,
            98, 121, 116, 101, 115, 46 };
    static private final  String str[] = { "我", "是","大","傻","B" };
    public static void main(String[] args) throws IOException {
        FileOutputStream fout = new FileOutputStream("F:\\file\\write.txt");

        FileChannel fc = fout.getChannel();

        // 写入第一个数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        for (byte aMessage : message) {
            buffer.put(aMessage);
        }
        buffer.flip();
        fc.write(buffer);
        // 写入第二个数据
        buffer.clear();
        buffer.put("\r\n".getBytes());
        for (String str : str) {
            buffer.put(str.getBytes());
        }
        buffer.flip();
        fc.write(buffer);

        // 关闭
        fout.close();
    }
}
