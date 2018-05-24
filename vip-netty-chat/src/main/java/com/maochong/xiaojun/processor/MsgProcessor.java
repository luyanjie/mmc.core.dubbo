package com.maochong.xiaojun.processor;

import com.alibaba.fastjson.JSONObject;
import com.maochong.xiaojun.protocol.IMDecoder;
import com.maochong.xiaojun.protocol.IMEncoder;
import com.maochong.xiaojun.protocol.IMMessage;
import com.maochong.xiaojun.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 主要用于自定义协议内容的逻辑处理
 * @author jokin
 * */
public class MsgProcessor {
    /**
     * 记录当前用户数量(他的原型是Set)
     * */
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 记录当前用户数量ConcurrentHashMap 来存放，暂时是为了测试点对点发送消息
     * 这里使用昵称来当做key
     * */
    private static Map<String,Channel> currentHashMap = new ConcurrentHashMap<>();

    /**
     * 定义扩展属性
     * NICK_NAME：昵称
     * IP_ADDR：IP地址
     * ATTS:扩展属性
     * */
    private final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");

    /**
     * 传输信息的编码与解码
     * */
    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();


    /**
     * 获取用户昵称
     * @param client 通道
     * @return 昵称
     */
    public String getNickName(Channel client)
    {
        return client.attr(NICK_NAME).get();
    }
    /**
     * 获取用户远程IP地址
     * @param client 通道
     * @return IP地址
     */
    public String getAddress(Channel client)
    {
        return client.remoteAddress().toString().replaceFirst("/","");
    }

    /**
     * 获取扩展属性
     * @param client 通道
     * @return 属性
     */
    public JSONObject getAttrs(Channel client){
        try{
            return client.attr(ATTRS).get();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 设置扩展属性
     * @param client 通道
     *               @param key key
     *               @param value value
     */
    private void setAttrs(Channel client,String key,Object value){
        try{
            JSONObject json = client.attr(ATTRS).get();
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
     * @param client 通道
     */
    public void logout(Channel client){
        // 本例子使用昵称来当做唯一用户标示 如果nickName为null，没有遵从聊天协议的连接，表示未非法登录
        if(getNickName(client) == null){ return; }
        // 给所有用户发送消息
        for (Channel channel : onlineUsers) {
            // 组装消息体
            IMMessage request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "ALL",getNickName(client) + "离开");
            // 编码消息后进行发送给各个通道（用户）
            String content = encoder.encode(request);
            channel.writeAndFlush(new TextWebSocketFrame(content));
        }
        // 最后从列表中删除
        onlineUsers.remove(client);
        currentHashMap.remove(getNickName(client));
    }

    /**
     * 发送消息
     * @param client 通道
     * @param message 消息类
     */
    public void sendMsg(Channel client,IMMessage message){
        sendMsg(client,encoder.encode(message));
    }

    /**
     * 发送消息
     * @param client 通道
     * @param msg 组装的消息
     */
    public void sendMsg(Channel client,String msg){
        // 解码消息后把消息内容转为实体类
        IMMessage request = decoder.decode(msg);
        if(null == request){ return; }

        // 获取IP地址
        String addr = getAddress(client);
        String receiver = request.getReceiver();
        // 判断是否发给单个人
        if(!StringUtil.isNullOrEmpty(receiver) && !"all".equals(receiver.toLowerCase())){
            sendMsg(client,receiver,msg);
            return;
        }

        // 消息类型是登陆 LOGIN
        if(IMP.LOGIN.getName().equals(request.getCmd())){
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(addr);
            onlineUsers.add(client);
            currentHashMap.put(request.getSender(),client);

            /*currentHashMap.forEach((k,v)->{
                String content = encoder.encode(new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(),receiver, v != client
                        ? getNickName(client) + "加入"
                        : "已与服务器建立连接！"));
                v.writeAndFlush(new TextWebSocketFrame(content));
            });

            for (Map.Entry<String,Channel> entry:currentHashMap.entrySet()) {
                Channel channel = entry.getValue();
            }
            */



            for (Channel channel : onlineUsers) {
                request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(),receiver, channel != client
                        ? getNickName(client) + "加入"
                        : "已与服务器建立连接！");
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
        // 消息类型是发送消息 CHAT
        else if(IMP.CHAT.getName().equals(request.getCmd())){
            for (Channel channel : onlineUsers) {
                // 判断是发送给自己的，还是发送给其他在线人
                request.setSender(channel != client ? getNickName(channel) : "you");
                request.setTime(sysTime());
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
        // 消息类型是送鲜花 FLOWER
        else if(IMP.FLOWER.getName().equals(request.getCmd())){
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            if(null != attrs){
                long lastTime = attrs.getLongValue("lastFlowerTime");
                // 10秒之内不允许重复刷鲜花
                int seconds = 10;
                long sub = currTime - lastTime;
                if(sub < 1000 * seconds){
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setContent("您送鲜花太频繁," + (seconds - Math.round(sub / 1000)) + "秒后再试");
                    String content = encoder.encode(request);
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
                request.setTime(sysTime());
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }

    /**
     * 点对点发送消息
     * @param client 消息发送者
     * @param receiveName 消息接受者
     * @param msg 消息内容
     * */
    public void sendMsg(Channel client, String receiveName, String msg)
    {
        System.out.println("sendMsg");
        System.out.println(msg);

        // 解码消息后把消息内容转为实体类
        IMMessage request = decoder.decode(msg);
        System.out.println(request.toString());
        if(null == request){ return; }
        // 获取IP地址
        // String addr = getAddress(client);

        // 判断要发送的通道是否存在
        Channel receive = currentHashMap.get(receiveName);
        if(receive == null){
            System.out.println("要发送的通道不存在");
            return;
        }

        // 消息类型是发送消息 CHAT
        if(IMP.CHAT.getName().equals(request.getCmd())){

            // 判断是发送给自己的
            send(client,request,"you","");
            // 发送给对方
            send(receive,request,getNickName(client),"");
//            request.setSender("you");
//            request.setTime(sysTime());
//            String content = encoder.encode(request);
//            client.writeAndFlush(new TextWebSocketFrame(content));
//
//            request.setSender(getNickName(client));
//            request.setTime(sysTime());
//            content = encoder.encode(request);
//            receive.writeAndFlush(new TextWebSocketFrame(content));
        }

        // 消息类型是送鲜花 FLOWER
        else if(IMP.FLOWER.getName().equals(request.getCmd())){
            long currTime = sysTime();
            if(noAllowFlower(client,request,currTime)){
                return;
            }

            // 给自己发送鲜花
            setAttrs(client, "lastFlowerTime", currTime);
            send(receive,request,"you","你给大家送了一波鲜花雨");

            // 给对方发送鲜花
            send(receive,request,getNickName(client),getNickName(client) + "送来一波鲜花雨");
        }
    }

    /**
     * 发送消息
     * @param client 接受者的通道
     * @param request request
     * @param sender 发送者
     * @param title 说明内容
     * */
    private void send(Channel client,IMMessage request,String sender,String title)
    {
        request.setSender(sender);
        if(!StringUtil.isNullOrEmpty(title))
        {request.setContent(title);}
        request.setTime(sysTime());
        String content = encoder.encode(request);
        client.writeAndFlush(new TextWebSocketFrame(content));
    }

    /**
     * 该客户端是否允许给对方发送鲜花
     * */
    private boolean noAllowFlower(Channel client,IMMessage request,long currTime)
    {
        JSONObject attrs = getAttrs(client);

        if(null != attrs){
            long lastTime = attrs.getLongValue("lastFlowerTime");
            // 10秒之内不允许重复刷鲜花
            int seconds = 10;
            long sub = currTime - lastTime;
            if(sub < 1000 * seconds){
                request.setSender("you");
                request.setCmd(IMP.SYSTEM.getName());
                request.setContent("您送鲜花太频繁," + (seconds - Math.round(sub / 1000)) + "秒后再试");
                String content = encoder.encode(request);
                client.writeAndFlush(new TextWebSocketFrame(content));
                return true;
            }
        }
        return false;
    }



    /**
     * 获取系统时间
     * @return 系统时间
     */
    private Long sysTime(){
        return System.currentTimeMillis();
    }

 }
