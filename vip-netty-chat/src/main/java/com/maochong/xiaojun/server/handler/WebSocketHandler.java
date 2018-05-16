package com.maochong.xiaojun.server.handler;

import com.maochong.xiaojun.processor.MsgProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;

/**
 * webSocketHandler
 * @author jokin
 * */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger LOG = Logger.getLogger(WebSocketHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        MsgProcessor.sendMessage(ctx.channel(),msg.text());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
        Channel client = ctx.channel();
        String addr = MsgProcessor.getAddress(client);
        LOG.info("WebSocket Client:" + addr + "加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        Channel client = ctx.channel();
        MsgProcessor.logout(client);
        LOG.info("WebSocket Client:" + MsgProcessor.getNickName(client) + "离开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel client = ctx.channel();
        String addr = MsgProcessor.getAddress(client);
        LOG.info("WebSocket Client:" + addr + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel client = ctx.channel();
        String addr = MsgProcessor.getAddress(client);
        LOG.info("WebSocket Client:" + addr + "掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();;
        String addr = MsgProcessor.getAddress(client);
        LOG.info("WebSocket Client:" + addr + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
