package com.maochong.xiaojun.processor;

import com.alibaba.fastjson.JSONObject;
import com.maochong.xiaojun.protocol.IMDecoder;
import com.maochong.xiaojun.protocol.IMEncoder;
import com.maochong.xiaojun.protocol.IMMessage;
import com.maochong.xiaojun.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 主要用于自定义协议内容的逻辑处理
 * */
public class MsgProcessor {
    /**
     * 记录当前用户数量
     * */
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 定义扩展属性
     * NICK_NAME：昵称
     * IP_ADDR：IP地址
     * ATTS:扩展属性
     * */
    private final static AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private final static AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private final static AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");

    /**
     * 获取昵称
     * */
    public static String getNickName(Channel client){
        return client.attr(NICK_NAME).get();
    }

    /**
     * 获取远程地址IP
     * */
    public static String getAddress(Channel client){
        return client.remoteAddress().toString().replaceFirst("/","");
    }

    /**
     * 获取扩展属性
     * */
    public static JSONObject getAttrs(Channel client)
    {
        try{
            return client.attr(ATTRS).get();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 设置扩展属性
     * @param client
     * @return
     */
    private static void setAttrs(Channel client,String key,Object value){
        try{
            JSONObject json = getAttrs(client);
            if(json==null) {return;}
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }catch(Exception e){
            JSONObject json = new JSONObject();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }
    }

    /**
     * 登出通知
     * @param client
     */
    public static void logout(Channel client){
        //如果nickName为null，没有遵从聊天协议的连接，表示未非法登录
        if(getNickName(client) == null){ return; }
        for (Channel channel : onlineUsers) {
            IMMessage request = new IMMessage(IMP.SYSTEM.getName(), System.currentTimeMillis(), onlineUsers.size(), getNickName(client) + "离开");
            String content = IMEncoder.encode(request);
            channel.writeAndFlush(new TextWebSocketFrame(content));
        }
        onlineUsers.remove(client);
    }

    /**
     * 发送消息
     * */

    public static void sendMessage(Channel client,IMMessage message){
        sendMessage(client,IMEncoder.encode(message));
    }

    public static void sendMessage(Channel client,String message)
    {
        IMMessage request = IMDecoder.decode(message);
        if(null == request) {return;}
        // 获取IP地址
        String addr = getAddress(client);

        // 判断是否为登陆
        if(request.getCmd().equals(IMP.LOGIN.getName())){
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(addr);
            // 登陆，添加一个在线人数
            onlineUsers.add(client);
            // 分发消息
            for (Channel channel : onlineUsers){
                request = new IMMessage(IMP.SYSTEM.getName(), System.currentTimeMillis(), onlineUsers.size(),
                        client != channel
                                ?getNickName(client) + "加入"
                                :"已与服务器建立连接！");
                String content = IMEncoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
        // 判断为聊天
        else if (request.getCmd().equals(IMP.CHAT.getName()))
        {
            for (Channel channel:onlineUsers)
            {
                request.setSender(channel == client?"you":getNickName(client));
                request.setTime(System.currentTimeMillis());
                String content = IMEncoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
        // 判断为送花
        else if(request.getCmd().equals(IMP.FLOWER.getName()))
        {
            JSONObject attrs = getAttrs(client);
            long currTime = System.currentTimeMillis();
            if(null != attrs){
                long lastTime = attrs.getLongValue("lastFlowerTime");
                //60秒之内不允许重复刷鲜花
                int secends = 10;
                long sub = currTime - lastTime;
                if(sub < 1000 * secends){
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setContent("您送鲜花太频繁," + (secends - Math.round(sub / 1000)) + "秒后再试");
                    String content = IMEncoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }

            //正常送花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("你给大家送了一波鲜花雨");
                    setAttrs(client, "lastFlowerTime", currTime);
                }else{
                    request.setSender(getNickName(client));
                    request.setContent(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(System.currentTimeMillis());

                String content = IMEncoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }
}
