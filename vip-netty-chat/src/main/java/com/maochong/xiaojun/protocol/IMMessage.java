package com.maochong.xiaojun.protocol;

import lombok.Data;
import org.msgpack.annotation.Message;



/**
 * 自定义消息体
 * @author jokin
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

    public IMMessage(String cmd,long time,int online,String content){
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }
    public IMMessage(String cmd,long time,String sender){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
    }
    public IMMessage(String cmd,long time,String sender,String content){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.content = content;
    }
}
