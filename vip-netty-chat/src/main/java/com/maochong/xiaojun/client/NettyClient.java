package com.maochong.xiaojun.client;

import com.maochong.xiaojun.client.handler.ChatClientHandler;
import com.maochong.xiaojun.protocol.IMDecoder;
import com.maochong.xiaojun.protocol.IMEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class NettyClient {
    private ChatClientHandler clientHandler;
    private String host;
    private int port;

    public NettyClient(String nickName){
        this.clientHandler = new ChatClientHandler(nickName);
    }

    /**
     * 连接
     * */
    public void connect(String host,int port){
        this.host = host;
        this.port = port;
        // 1、EventLoopGroup：不论是服务器端还是客户端，都必须制定EventLoopGroup，这里制定NioEventLoopGroup，表示是一个NIO的EventLoopGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            // 指定Channel的类型，因为是客户端，所以是NioSocketChannel
            b.channel(NioSocketChannel.class);
            // SO_KEEPALIVE，该参数用于设置TCP连接
            b.option(ChannelOption.SO_KEEPALIVE, true);
            // 设置数据的处理器
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IMDecoder());
                    ch.pipeline().addLast(new IMEncoder());
                    ch.pipeline().addLast(clientHandler);
                }
            });
            // 发起同步链接操作
            ChannelFuture f = b.connect(this.host, this.port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭、释放线程资源
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws IOException {
        new NettyClient("Sam").connect("127.0.0.1",8011);
    }
}
