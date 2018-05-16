package com.maochong.xiaojun.io.nio.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接缓冲区（实现零拷贝）
 * @author jokin
 * */
public class DirectBuffer {
    public static void main(String[] args) throws IOException {
        // 首先我们从磁盘上读取刚才我们写好的文件内容
        String infile = "F:\\file\\read.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fc = fin.getChannel();

        // 把刚刚读取的内容写到一个新的文件中
        String outfile = "F:\\file\\write.txt";
        File file = new File(outfile);
        // 文件存在先删除
        if(file.exists()){
            file.delete();
        }

        String direct = "F:\\file";
        File fileDirect = new File(direct);
        if(fileDirect.exists() && fileDirect.isDirectory()){
            System.out.println(String.format("%s 文件夹已经存在",direct));
        }

        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();

        // 使用allocateDirect，而不是allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true){
            buffer.clear();
            int r = fc.read(buffer);
            if(r>0){
                buffer.flip();
                fcout.write(buffer);
            }
            else {
                break;
            }
        }
    }
}
