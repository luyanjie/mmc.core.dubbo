package com.maochong.xiaojun.io.nio.buffer;

import java.nio.ByteBuffer;

public class BufferWrap {
    public static void main(String[] args) {
        // 分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 包装一个现有的数据
        byte[] array = new byte[10];
        for (int i=0;i<10;i++){
            array[i] = (byte)i;
        }
        ByteBuffer buffer1  = ByteBuffer.wrap(array);

        while (true){
            if(buffer1.remaining()>0){
                System.out.println((int) buffer1.get());
            }
            else {
                break;
            }
        }
    }
}
