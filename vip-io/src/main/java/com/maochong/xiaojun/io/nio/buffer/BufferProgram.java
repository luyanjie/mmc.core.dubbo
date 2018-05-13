package com.maochong.xiaojun.io.nio.buffer;

import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.channels.FileChannel;

/**
 * Buffer 演示
 * 八大基本数据类型中，只用boolean没有，别的都有
 * ByteBuffer、IntBuffer、LongBuffer、ShortBuffer、StringCharBuffer、FloatBuffer、DoubleBuffer
 * */
public class BufferProgram {
    public static void main(String[] args) throws Exception {
        // 这用的是文件IO处理
        FileInputStream fin = new FileInputStream("F:\\file\\read.txt");
        // 创建文件的操作管道
        FileChannel fc = fin.getChannel();
        while (true)
        {
            // 分配一个10个大小的缓冲区，就是分配一个10个大小的byte数组
            // 0<=position<=limit<=capacity 第一个分配的时候 position=0 ，limit=capacity=初始值
            ByteBuffer buffer = ByteBuffer.allocate(10);
            output("init",buffer);
            //先读一下
            int len = fc.read(buffer);
            output("调用read()", buffer);
            if(len<=0){
                break;
            }
            // 准备操作之前，先锁定操作范围
            // 赋值 limit=position,position=0
            buffer.flip();
            output("调用flip()",buffer);
            // 判断有没有可以读的数据
            while (buffer.remaining()>0){
                byte b = buffer.get();
                System.out.println((char)b);
            }
            output("调用get()", buffer);

            // 可以理解为解锁 clear掉
            buffer.clear();
            output("调用clear()",buffer);

        }
        fin.close();
    }

    //把这个缓冲里面实时状态给答应出来
    public static void output(String step, Buffer buffer) {
        System.out.println(step + " : ");
        //容量，数组大小
        System.out.print("capacity: " + buffer.capacity() + ", ");
        //当前操作数据所在的位置，也可以叫做游标
        System.out.print("position: " + buffer.position() + ", ");
        //锁定值，flip，数据操作范围索引只能在position - limit 之间
        System.out.println("limit: " + buffer.limit());
        System.out.println();
    }
}
