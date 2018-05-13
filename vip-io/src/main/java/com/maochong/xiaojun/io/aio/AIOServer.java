package com.maochong.xiaojun.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIOServer {

    private final int port;

    private AIOServer(int port)
    {
        this.port = port;
    }

    private void listen() {
        try {
            // 线程缓冲池，为了体现异步
            ExecutorService executorService = Executors.newCachedThreadPool();
            // 给线程一个初始化一个线程
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            // Asynchronous异步
            // NIO ServerSocketChannel
            // BIO ServerSocket

            // 同样的，也是先把通道打开
            final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
            // 可以让客户端连进来了（打开高速公路的关卡）
            server.bind(new InetSocketAddress(port));
            System.out.println("服务已启动，监听端口" + port);

            final Map<String,Integer> count = new ConcurrentHashMap<String, Integer>();
            count.put("count", 0);
            //开始等待客户端连接
            //实现一个CompletionHandler 的接口，匿名的实现类
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                //实现IO操作完成的方法
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    count.put("count", count.get("count") + 1);

                    System.out.println(count.get("count"));
                    //只要拿数据，捡现成的,我们都是懒人，IO操作都不用关心了
                    //System.out.println("IO操作成功，开始获取数据");
                    try {
                        buffer.clear();
                        result.read(buffer).get();
                        buffer.flip();
                        result.write(buffer);
                        buffer.flip();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    } finally {
                        try {
                            result.close();
                            server.accept(null, this);
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }

                    //System.out.println("操作完成");
                }

                //实现IO操作失败的方法
                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("IO操作是失败: " + exc);
                }
            });


            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AIOServer(8100).listen();
    }
}
