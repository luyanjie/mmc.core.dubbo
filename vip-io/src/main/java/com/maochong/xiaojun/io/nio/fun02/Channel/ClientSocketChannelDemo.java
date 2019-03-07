package com.maochong.xiaojun.io.nio.fun02.Channel;

import com.maochong.xiaojun.io.nio.fun02.buffer.Buffers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * 客户端：客户端每个1-2秒自动向服务器发送数据，接收服务器数据并显示
 */
public class ClientSocketChannelDemo {

    public static class TCPEchoClient implements Runnable {

        private String name;
        private Random random = new Random(System.currentTimeMillis());

        //
        private InetSocketAddress remoteAddress;

        public TCPEchoClient(String name, InetSocketAddress remoteAddress) {
            this.name = name;
            this.remoteAddress = remoteAddress;
        }

        @Override
        public void run() {
            //创建解码器
            Charset charset = Charset.forName("UTF-8");
            Selector selector;
            try {
                // 创建TCP链接
                SocketChannel sc = SocketChannel.open();
                // 设置通道为非阻塞
                sc.configureBlocking(false);
                // 创建选择器
                selector = Selector.open();
                // 注册感兴趣事件
                int ops = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                // 向选择器注册通道
                sc.register(selector, ops, new Buffers(256, 256));
                // 向服务器发起链接，一个通道代表一个tcp链接
                sc.connect(remoteAddress);
                // 等待三次握手完成
                while (!sc.finishConnect()) {
                    ;
                }

                System.out.println(name + " " + "finished connection");
            } catch (IOException e) {
                System.out.println("client connect failed");
                return;
            }

            // 与服务器断开或者线程被中断则结束线程
            try {
                int i = 1;
                while (!Thread.currentThread().isInterrupted()) {
                    // 阻塞等待
                    selector.select();
                    // Set中的每个key代表一个通道
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();

                    // 遍历每个已就绪的通道，处理这个通道已就绪的事件
                    while (it.hasNext()) {

                        SelectionKey key = it.next();
                        //防止下次select方法返回已处理过的通道
                        it.remove();

                        //通过SelectionKey获取对应的通道
                        Buffers buffers = (Buffers) key.attachment();
                        ByteBuffer readBuffer = buffers.getReadBuffer();
                        ByteBuffer writeBuffer = buffers.getWriteBuffer();

                        //通过SelectionKey获取通道对应的缓冲区
                        SocketChannel sc = (SocketChannel) key.channel();

                        // 表示底层socket的读缓冲区有数据可读
                        if (key.isReadable()) {
                            /*从socket的读缓冲区读取到程序定义的缓冲区中*/
                            sc.read(readBuffer);
                            readBuffer.flip();
                            /*字节到utf8解码*/
                            CharBuffer cb = charset.decode(readBuffer);
                            /*显示接收到由服务器发送的信息*/
                            System.out.println(cb.array());
                            readBuffer.clear();
                        }

                        /*socket的写缓冲区可写*/
                        if (key.isWritable()) {
                            writeBuffer.put((name + "  " + i).getBytes("UTF-8"));
                            writeBuffer.flip();
                            /*将程序定义的缓冲区中的内容写入到socket的写缓冲区中*/
                            sc.write(writeBuffer);
                            writeBuffer.clear();
                            i++;
                        }
                    }

                    Thread.sleep(1000 + random.nextInt(1000));
                }
            } catch (InterruptedException e) {
                System.out.println(name + " is interrupted");
            } catch (IOException e) {
                System.out.println(name + " encounter a connect error");
            } finally {
                try {
                    selector.close();
                } catch (IOException e1) {
                    System.out.println(name + " close selector failed");
                } finally {
                    System.out.println(name + "  closed");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        InetSocketAddress remoteAddress = new InetSocketAddress("127.0.0.1", 8080);

        Thread ta = new Thread(new TCPEchoClient("thread a", remoteAddress));
        Thread tb = new Thread(new TCPEchoClient("thread b", remoteAddress));
        Thread tc = new Thread(new TCPEchoClient("thread c", remoteAddress));
        Thread td = new Thread(new TCPEchoClient("thread d", remoteAddress));

        ta.start();
        tb.start();
        tc.start();

        Thread.sleep(5000);

        /*结束客户端a*/
        ta.interrupt();

        /*开始客户端d*/
        td.start();
    }
}
