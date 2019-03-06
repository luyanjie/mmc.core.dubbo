package com.maochong.xiaojun.protocol;

import lombok.Data;
import org.msgpack.annotation.Message;



/**
 * 自定义消息体
 * @author jokin
 * 消息体模式如下：
 * 服务端命令：
 * SYSTEM：[命令][命令发送时间][接受人]  例子：[SYSTEM][12331232133][JOKIN]
 * 客服端命令：
 * LOGIN：[命令][命令发送时间][命令发送者] 例子：[SYSTEM][12331232133][JOKIN]
 * LOGOUT：[命令][命令发送时间][命令发送者] 例子：[SYSTEM][12331232133][JOKIN]
 * CHAT：[命令][命令发送时间][命令发送者][命令接收人] - 聊天内容  例子：[SYSTEM][12331232133][JOKIN][ALL] - 你好，我是jokin
 * FLOWER：[命令][命令发送时间][命令发送者][名人接收人]  例子：[SYSTEM][12331232133][JOKIN][ALL]
 * */
@Message
@Data
public class IMMessage {
    /**
     * ip地址及端口
     * */
    private String addr;
    /**
     * 命令类型[LOGIN]...
     * */
    private String cmd;
    /**
     * 命令发送时间
     * */
    private long time;
    /**
     * 在线人数
     * */
    private int online;
    /**
     * 消息发送者 这里存nickname
     * */
    private String sender;
    /**
     * 消息接收者
     * */
    private String receiver;
    /**
     * 消息内容
     * */
    private String content;

    public IMMessage(){}

    public IMMessage(String cmd,long time,int online,String receiver, String content){
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.receiver = receiver;
        this.content = content;

    }
    public IMMessage(String cmd,long time,String sender,String receiver){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
    }
    public IMMessage(String cmd,long time,String sender,String receiver,String content){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
