package com.maochong.xiaojun.client;

import com.maochong.xiaojun.client.handler.FileUploadClientHandler;
import com.maochong.xiaojun.protocol.FileUploadFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

/**
 * @author jokin
 * @date 2018/5/24 14:44
 */
public class FileUploadClient {
    public void connect(int port, String host,
                        final FileUploadFile fileUploadFile) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast(new FileUploadClientHandler(fileUploadFile));
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
            System.out.println("FileUploadClient connect()结束");
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8012;
        String host = "127.0.0.1";
        String path = "d:/12121.mp3"; //"d:/test.txt";
        if (args != null && args.length > 2) {
            try {
                host = String.valueOf(args[0]);
                port = Integer.valueOf(args[1]);
                path = String.valueOf(args[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try
        {
            FileUploadFile uploadFile = new FileUploadFile();
            File file = new File(path);
            // 文件名
            String fileMd5 = file.getName();
            uploadFile.setFile(file);
            uploadFile.setFilemd5(fileMd5);
            // 文件开始位置
            uploadFile.setStarPos(0);
            new FileUploadClient().connect(port, host, uploadFile);
        }
        catch (Exception ex)
        {

        }
    }
}
