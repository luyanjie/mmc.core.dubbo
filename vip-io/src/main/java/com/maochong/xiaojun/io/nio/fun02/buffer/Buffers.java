package com.maochong.xiaojun.io.nio.fun02.buffer;

import java.nio.ByteBuffer;

/**
 * 自定义buffer类中包含读缓冲区和写缓冲区，用于注册通道时的附加对象
 */
public class Buffers {
    private ByteBuffer readBuffer = null;
    private ByteBuffer writeBuffer = null;

    public Buffers(int readCapacity,int writeCapacity){
        readBuffer = ByteBuffer.allocate(readCapacity);
        writeBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadBuffer(){
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer(){
        return writeBuffer;
    }
}
