package com.maochong.xiaojun.protocol;

/**
 *  自定义IM协议，Instant Messaging Protocol即时通信协议
 *  @author jokin
 * */
public enum  IMP {
    /** 系统消息 */
    SYSTEM("SYSTEM"),
    /** 登录指令 */
    LOGIN("LOGIN"),
    /** 登出指令 */
    LOGOUT("LOGOUT"),
    /** 聊天消息 */
    CHAT("CHAT"),
    /** 送鲜花 */
    FLOWER("FLOWER");

    private String name;

    /**
     * 判断是都包含定义的几种类型消息体
     * */
    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT|FLOWER)\\]");
    }

    IMP(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
