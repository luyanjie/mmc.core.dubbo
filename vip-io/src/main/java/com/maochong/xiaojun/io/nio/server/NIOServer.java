package com.maochong.xiaojun.io.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO(同步非阻塞)服务端
 * @author jokin
 * @data 2018-05-13
 * */
public class NIOServer {
    private Charset charset = Charset.forName("UTF-8");
    // 用来记录在线人数，以及昵称（类似注册中心，应该可以用zookeeper来做）
    private static HashSet<String> users = new HashSet<>();

    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILT = "#@#";

    private Selector selector = null;


    private NIOServer(int port) throws IOException {

        //要想富，先修路
        //先把通道打开
        ServerSocketChannel server = ServerSocketChannel.open();

        //设置高速公路的关卡
        server.bind(new InetSocketAddress(port));
        // 设置为非阻塞模式
        server.configureBlocking(false);

        //开门迎客，排队叫号大厅开始工作
        selector = Selector.open();

        //告诉服务叫号大厅的工作人员，你可以接待了（注册链接事件）
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务已启动，监听端口是：" + port);
    }

    /**
     * 监听
     * */
    private void listen() throws IOException{

        //死循环，这里不会阻塞
        //CPU工作频率可控了，是可控的固定值
        while(true) {
            //在轮询，我们服务大厅中，到底有多少个人正在排队
            int wait = selector.select();
            if(wait == 0) {continue;} //如果没有人排队，进入下一次轮询

            //取号，默认给他分配个号码（排队号码）
            //可以通过这个方法，知道可用通道的集合
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //处理一个，号码就要被消除，打发他走人（别在服务大厅占着茅坑不拉屎了）
                //过号不候
                iterator.remove();
                //处理逻辑
                process(key);
            }
        }
    }

    private void process(SelectionKey key) throws IOException{
        //判断客户端确定已经进入服务大厅并且已经可以实现交互了
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel client = server.accept();
            //非阻塞模式（为了向下兼容：兼容BIO，所以默认是用了阻塞IO）
            client.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个SocketChannel处理
            client.register(selector, SelectionKey.OP_READ);

            //将此对应的channel设置为准备接受其他客户端请求
            key.interestOps(SelectionKey.OP_ACCEPT);
//            System.out.println("有客户端连接，IP地址为 :" + sc.getRemoteAddress());
            client.write(charset.encode("请输入你的昵称"));
        }
        //处理来自客户端的数据读取请求
        if(key.isReadable()){
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel client = (SocketChannel)key.channel();

            //往缓冲区读数据
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try{
                while(client.read(buff) > 0)
                {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
//                System.out.println("从IP地址为：" + sc.getRemoteAddress() + "的获取到消息: " + content);
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            }catch (IOException io){
                key.cancel();
                if(key.channel() != null)
                {
                    key.channel().close();
                }
            }
            if(content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILT);
                //注册用户
                if(arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if(users.contains(nickName)) {
                        client.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        broadCast(null, message);
                    }
                }
                //注册完了，发送消息
                else if(arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILT.length());
                    message = nickName + " 说 " + message;
                    if(users.contains(nickName)) {
                        //不回发给发送此内容的客户端
                        broadCast(client, message);
                    }
                }
            }
        }
    }

    //TODO 要是能检测下线，就不用这么统计了（检查下线暂时想着 是否可以用zookeeper来做注册中心调度，注册子节点的删除事件）
    private int onlineCount() {
        int res = 0;
        for(SelectionKey key : selector.keys()){
            Channel target = key.channel();

            if(target instanceof SocketChannel){
                res++;
            }
        }
        return res;
    }

    private void broadCast(SocketChannel client, String content) throws IOException {
        //广播数据到所有的SocketChannel中
        for(SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            //如果client不为空，不回发给发送此内容的客户端
            if(targetChannel instanceof SocketChannel && targetChannel != client) {
                SocketChannel target = (SocketChannel)targetChannel;
                target.write(charset.encode(content));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new NIOServer(8100).listen();
    }
}
