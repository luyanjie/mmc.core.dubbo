package com.maochong.xiaojun.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author jokin
 * @date 2018/5/25 14:08
 * Netty 内存池的使用 例子
 */
public class AppPoolByteBuf {

    public static void main(String[] args) {
        poolByteBuf();
        noPoolByteBuf();
    }

    /**
     * 使用内存池分配器创建直接内存缓冲区
     */
    private static void poolByteBuf() {
        final byte[] CONTENT = new byte[1024];
        long startTime = System.currentTimeMillis();
        int loop = 1800000;
        ByteBuf poolByteBuf;
        for (int i = 0; i < loop; i++) {
            poolByteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            poolByteBuf.writeBytes(CONTENT);
            poolByteBuf.release();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("内存池分配缓冲区耗时" + (endTime - startTime));
    }

    /**
     * 使用非堆内内存分配器创建的直接内存缓冲区
     */
    private static void noPoolByteBuf() {
        final byte[] CONTENT = new byte[1024];
        long startTime = System.currentTimeMillis();
        int loop = 1800000;

        ByteBuf byteBuf;
        for (int i = 0; i < loop; i++) {
            byteBuf = Unpooled.directBuffer(1024);
            byteBuf.writeBytes(CONTENT);
            byteBuf.release();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("非内存池分配缓冲区耗时：" + (endTime - startTime));
    }
}
