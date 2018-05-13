package com.maochong.xiaojun.io.bio;

import java.io.*;

/**
 * 读取一个文件的数据
 * */
public class FileBIO {
    public static void main(String[] args) throws IOException {
        FileInputStream input = new FileInputStream("F:\\file\\read.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true){
            // 按行读取
            String content = reader.readLine();
            if(null == content){
                break;
            }
            System.out.println(content);
        }
    }
}
