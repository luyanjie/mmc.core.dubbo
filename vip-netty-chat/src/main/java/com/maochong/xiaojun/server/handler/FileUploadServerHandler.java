package com.maochong.xiaojun.server.handler;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.maochong.xiaojun.protocol.FileUploadFile;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * @author jokin
 * @date 2018/5/24 14:25
 * 文件上传
 */
public class FileUploadServerHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    /**
     * 指定目录
     * */
    private String fileDir = "F:";
    private Logger log= Logger.getLogger(FileUploadServerHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelActive(ctx);
        log.info("服务端：channelActive()");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
        log.info("服务端：channelInactive()");
        ctx.flush();
        ctx.close();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileUploadFile) {
            FileUploadFile ef = (FileUploadFile)msg;

            byte[] bytes = ef.getBytes();
            byteRead = ef.getEndPos();
            // 配置文件名
            String md5 = ef.getFileMd5();
            String path = fileDir + File.separator + md5;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);

            start = start + byteRead;
            System.out.println("path:"+path+","+byteRead+","+start);
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
                randomAccessFile.close();
                if(byteRead!=1024 * 10){
                    Thread.sleep(1000);
                    channelInactive(ctx);
                }
            } else {
                System.out.println("文件接收完成");
                ctx.flush();
                ctx.close();
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        log.info("FileUploadServerHandler--exceptionCaught()");
    }
}
