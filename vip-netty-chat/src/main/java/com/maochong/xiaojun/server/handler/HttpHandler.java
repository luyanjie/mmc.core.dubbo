package com.maochong.xiaojun.server.handler;

import com.maochong.xiaojun.protocol.EnumContextType;
import com.maochong.xiaojun.protocol.EnumUrlType;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * http 请求模式
 * @author jokin
 * */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static Logger LOG = Logger.getLogger(HttpHandler.class);

    /**
     * 获取类的文件路径
     * */
    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    /**
     * 文件存放目录路径
     * */
    private final String WebRoot = "webroot";

    private File getResource(String fileName) throws Exception{
        // 拼装文件路径
        String path = baseURL.toURI()+"/"+WebRoot + "/" + fileName;
        path = !path.contains("file:") ? path : path.substring(5).replaceAll("//", "/");
        return new File(path);

        /**
         * 获取路径的方式
         * 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
         *      File f = new File(this.getClass().getResource("/").getPath());
         *       获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
         *      File f2 = new File(this.getClass().getResource("").getPath());
         * 第二种：获取项目路径    D:\git\daotie\daotie
         *      File directory = new File("");// 参数为空
         *      String courseFile = directory.getCanonicalPath();
         * 第三种：  file:/D:/git/daotie/daotie/target/classes/
         *      URL xmlpath = this.getClass().getClassLoader().getResource("");
         * 第四种： D:\git\daotie\daotie
         *      System.out.println(System.getProperty("user.dir"));
         * 第五种：  获取所有的类路径 包括jar包的路径
         *      System.out.println(System.getProperty("java.class.path"));
         * */
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        RandomAccessFile file;
        try{
            String page = "/".equals(uri) ? "chat.html" : uri;
            // mode: r->可读  w-> 可写  rw-> 可读写
            file =	new RandomAccessFile(getResource(page), "r");

        }catch(Exception e){
            ctx.fireChannelRead(request.retain());
            return;
        }
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        //"text/html;";
        String contextType = EnumContextType.HTML.getNmae();
        if(uri.endsWith(EnumUrlType.CSS.getName())){
            contextType = EnumContextType.CSS.getNmae();
        }else if(uri.endsWith(EnumUrlType.JS.getName())){
            contextType = EnumContextType.JAVASCRIPT.getNmae();
        }else if(uri.toLowerCase().matches(EnumUrlType.IMAGE.getName())){
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = EnumContextType.IMAGE.getNmae() + ext;
        }
        response.headers().set("Content-Type", contextType + "charset=utf-8;");

        // 判断是都为长连接
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        if (keepAlive) {
            response.headers().set("Content-Length", file.length());
            response.headers().set("Connection", "keep-alive");
        }
        ctx.write(response);

        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();
        LOG.info("Client:"+client.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}

