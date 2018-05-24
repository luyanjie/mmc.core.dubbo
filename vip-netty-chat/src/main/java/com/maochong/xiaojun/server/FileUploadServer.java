package com.maochong.xiaojun.server;

import com.maochong.xiaojun.server.handler.FileUploadServerHandler;
import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;

/**
 * @author jokin
 * @date 2018/5/24 14:35
 * 文件上传
 */
public class FileUploadServer {

    private static Logger LOG = Logger.getLogger(FileUploadServer.class);

    private int port = 8012;

    public void start()
    {
        EventLoopGroup bossGrop = new NioEventLoopGroup();
        EventLoopGroup workGrop = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGrop,workGrop)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            // 最大长度
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
                            pipeline.addLast(new FileUploadServerHandler());
                        }
                    });
           ChannelFuture f = b.bind(this.port).sync();
            LOG.info("服务已启动,监听端口" + this.port);
           f.channel().closeFuture().sync();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new FileUploadServer().start();
    }
}
