package com.maochong.xiaojun.protocol;

import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import javax.core.common.config.CustomConfig;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义解码器
 * @author jokin
 * */
public class IMDecoder extends ByteToMessageDecoder {

    /**
     * 解析IM写一下请求内容的正则
     * */
    private static Pattern pattern = Pattern.compile("^\\[(.*)](\\s-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext cxt, ByteBuf in, List<Object> out) throws Exception {
        try {
            // 获取可读字节数
            final int length = in.readableBytes();
            final byte[] array = new byte[length];
            String content = new String(array,in.readerIndex(),length);

            // 判断字符串content
            if(CustomConfig.isEmpty(content) || IMP.isIMP(content))
            {
                // 此条内容不符合规则，移除此条信息
                cxt.channel().pipeline().remove(this);
                return;
            }
            in.getBytes(in.readerIndex(), array, 0, length);
            out.add(new MessagePack().read(array,IMMessage.class));
            in.clear();
        }
        catch (MessageTypeException e){
            // 解码失败，移除此条信息
            cxt.channel().pipeline().remove(this);
        }
    }

    /**
     * 字符串解析成自定义即时通信协议
     * */
    public static IMMessage decode(String message){
        if(CustomConfig.isEmpty(message)) {return null;}
        try {
            Matcher m = pattern.matcher(message);
            String header = "";
            String content = "";
            if(m.matches()){
                header = m.group(0);
                content = m.group(3);
            }
            String [] heards = header.split("\\]\\[");
            long time = 0;
            try{ time = Long.parseLong(heards[1]); } catch(Exception e){}
            String nickName = heards[2];
            //昵称最多十个字
            nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);

            String  login = "["+IMP.LOGIN.getName()+"]";
            String chat = "[" + IMP.CHAT.getName() + "]";
            String flower = "[" + IMP.FLOWER.getName() + "]";
            // LOGIN 开头- >登陆
            if(message.startsWith(login)){
                return new IMMessage(heards[0],time,nickName);
            }
            // CHAR 开头 - > 聊天消息
            else if(message.startsWith(chat)){
                return new IMMessage(heards[0],time,nickName,content);
            }
            // FLOWER 开头-> 送鲜花
            else if(message.startsWith(flower)){
                return new IMMessage(heards[0],time,nickName);
            }else{
                return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
