package com.maochong.xiaojun.io.nio.fun02.Channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChannelDemo {

    public static void main(String[] args) {
        try {
            // 如果文件不存在，则创建文件，文件后缀是不是文本文件不重要
            File file = new File("f:/noi_utf8.data");
            if(!file.exists()){
                file.createNewFile();
            }
            // 根据文件输出流创建与这个文件相关的通道
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel fc = fos.getChannel();
            // 创建ByteBuffer对象，position = 0 limit=64
            ByteBuffer bb = ByteBuffer.allocate(64);
            // 向ByteBuffer中放入字符串UTF-8 的字节，position = 17，limit = 64
            bb.put("Hello,World 123 \n".getBytes("UTF-8"));
            System.out.println(bb.position());
            System.out.println(bb.limit());
            // flip方法 锁定 position = 0 limit=17
            bb.flip();
            // write方法使得ByteBuffer的position到limit中的元素写入通道中
            fc.write(bb);
            // clear 方法使得position=0 limit=64
            bb.clear();
            /*下面的代码同理*/
            bb.put("你好，世界 456".getBytes("UTF-8"));
            bb.flip();

            fc.write(bb);
            bb.clear();

            fos.close();
            fc.close();
        }catch (IOException e){
            System.out.println(e);
        }

        /*从刚才的文件中读取字符序列*/
        try {

            /*通过Path对象创建文件通道*/
            Path path = Paths.get("f:/noi_utf8.data");
            FileChannel fc = FileChannel.open(path);
            ByteBuffer bb = ByteBuffer.allocate((int) fc.size()+1);
            Charset utf8 = Charset.forName("UTF-8");
            /*阻塞模式，读取完成才能返回*/
            fc.read(bb);
            bb.flip();
            CharBuffer cb = utf8.decode(bb);
            System.out.print(cb.toString());
            bb.clear();
            fc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
