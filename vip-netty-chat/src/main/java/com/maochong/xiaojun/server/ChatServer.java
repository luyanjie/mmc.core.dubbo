package com.maochong.xiaojun.server;

import com.maochong.xiaojun.protocol.IMDecoder;
import com.maochong.xiaojun.protocol.IMEncoder;
import com.maochong.xiaojun.server.handler.HttpHandler;
import com.maochong.xiaojun.server.handler.SocketHandler;
import com.maochong.xiaojun.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 聊天工具服务端
 * @author jokin
 * */
public class ChatServer {
    /**
     * 配置log文件
     * */
    private static Logger LOG = Logger.getLogger(ChatServer.class);
    /**
     * 配置端口为80
     * */
    private static int port = 80;

    /**
     * 入口
     * */
    public void start(){
        // 配置boss线程（主线程）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 配置工作线程（子线程）
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 解析自定义协议
                            pipeline.addLast(new IMDecoder());
                            pipeline.addLast(new IMEncoder());
                            pipeline.addLast(new SocketHandler());

                            // 配置HTTP请求
                            pipeline.addLast(new HttpServerCodec());
                            //主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
                            pipeline.addLast(new HttpObjectAggregator(64*1024));
                            //主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加自定义的HttpHandler处理
                            pipeline.addLast(new HttpHandler());

                            // 解析webSocket请求
                            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                            pipeline.addLast(new WebSocketHandler());
                        }
                    });

            ChannelFuture future = b.bind(port).sync();
            LOG.info("服务已启动,监听端口" + port);
            future.channel().closeFuture().sync();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            // 关闭线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 启动服务
     * */
    public static void main(String[] args) throws IOException {
        new ChatServer().start();
    }
}
