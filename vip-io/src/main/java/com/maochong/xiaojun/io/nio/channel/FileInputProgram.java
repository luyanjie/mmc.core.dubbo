package com.maochong.xiaojun.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读取文件数据到缓冲区，并打印
 * */
public class FileInputProgram {
    public static void main(String[] args) throws IOException {

        FileInputStream fin = new FileInputStream("F:\\file\\read.txt");

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据到缓冲区中
        fc.read(buffer);

        // 固定缓冲区
        buffer.flip();

        // 操作读取缓冲区的内容
        while (buffer.remaining()>0){
            byte b = buffer.get();
            System.out.println((char)b);
        }
        fc.close();
        fin.close();
    }
}
