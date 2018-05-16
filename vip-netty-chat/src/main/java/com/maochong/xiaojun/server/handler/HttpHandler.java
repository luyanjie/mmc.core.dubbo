package com.maochong.xiaojun.server.handler;

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
     * 获取class路径
     * */
    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    /**
     * 设置文件根目录
     */
    private final String WWWROOT = "webroot";

    /**
     * 获取文件
     * */
    private File getResource(String fileName) throws Exception
    {
        // 拼装文件路径
        String path = baseURL.toURI() + WWWROOT + "/"+fileName;
        path = !path.startsWith("file:")?path:path.substring(5);
        path = path.replaceAll("//","/");
        return  new File(path);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri().toLowerCase();
        RandomAccessFile file = null;
        try {
            String page = uri.equals("/") ? "chat.html" : uri;
            file =	new RandomAccessFile(getResource(page), "r");
        }
        catch (Exception ex)
        {
            ctx.fireChannelRead(request.retain());
            return;
        }
        HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        String contextType = "text/html;";
        if(uri.endsWith(".css")){
            contextType = "text/css;";
        }
        else if(uri.endsWith(".js")){
            contextType = "text/javascript;";
        }else if(uri.toLowerCase().matches("(jpg|png|gif)$")){
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        response.headers().set("Content-Type", contextType + "charset=utf-8;");

        // 是否长连接
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if(keepAlive)
        {
            response.headers().set("Content-Length", file.length());
            response.headers().set("Connection", "keep-alive");
        }
        ctx.write(response);
        ctx.write(new DefaultFileRegion(file.getChannel(),0,file.length()));

        if(!keepAlive)
        {
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            future.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }

    /**
     * 重写异常
     * */
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
