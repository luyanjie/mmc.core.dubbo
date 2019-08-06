package com.maochong.xiaojun.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 只读缓冲区
 *
 * @author jokin
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 缓冲区中的数据0-9
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        // 创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();

        System.out.println("创建后的只读缓冲区：这时候是没有值的，等于复制了一个结构");
        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }

        // 改变原缓冲区的内容
        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readonly.position(0);
        readonly.limit(buffer.capacity());

        // 只读缓冲区的内容也随之改变
        System.out.println("变更原缓冲区内容后的只读缓冲区：验证了原缓冲区改变，只读缓冲区也会改变");
        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }
    }
}
