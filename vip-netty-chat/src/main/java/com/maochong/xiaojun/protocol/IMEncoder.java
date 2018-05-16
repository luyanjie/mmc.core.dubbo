package com.maochong.xiaojun.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

import javax.core.common.config.CustomConfig;

/**
 * 自定义编码器
 * @author jokin
 * */
public class IMEncoder extends MessageToByteEncoder<IMMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }

    /**
     * 自定义encode方式
     * */
    public static String encode(IMMessage message){
        if(null == message) {return "";}
        // 拼接输出头
        String prex = "[" + message.getCmd() + "]" + "[" + message.getTime() + "]";
        if(IMP.LOGIN.getName().equals(message.getCmd()) ||
                IMP.CHAT.getName().equals(message.getCmd()) ||
                IMP.FLOWER.getName().equals(message.getCmd())){
            prex += ("[" + message.getSender() + "]");
        }else if(IMP.SYSTEM.getName().equals(message.getCmd())){
            prex += ("[" + message.getOnline() + "]");
        }
        if(!(null == message.getContent() || "".equals(message.getContent()))){
            prex += (" - " + message.getContent());
        }
        return prex;
    }
}
