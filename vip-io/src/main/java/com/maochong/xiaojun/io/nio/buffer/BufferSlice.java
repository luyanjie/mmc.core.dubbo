package com.maochong.xiaojun.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 缓冲区片（子缓冲区）
 * */
public class BufferSlice {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 缓冲区中的数据0-9
        for (int i=0; i<buffer.capacity(); ++i) {
            buffer.put( (byte)i );
        }

        // 创建子缓冲区
        buffer.position(3);
        buffer.limit(7);

        ByteBuffer slice = buffer.slice();

        // 改变子缓冲区的内容
        for(int i=0;i<slice.capacity();i++)
        {
            int b = (int)slice.get(i);
            b *=10;
            slice.put(i,(byte) b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.remaining()>0){
            System.out.println(buffer.get());
        }
    }
}
