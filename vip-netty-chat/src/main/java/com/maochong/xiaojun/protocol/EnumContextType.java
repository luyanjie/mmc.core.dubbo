package com.maochong.xiaojun.protocol;

/**
 * @author jokin
 * @date 2018/5/24 14:14
 */
public enum  EnumContextType {

    HTML("text/html;"),CSS("text/css;"),JAVASCRIPT("text/javascript;"),IMAGE("image/");

    private String name;

    EnumContextType(String name){this.name = name;}

    public String getNmae() {return this.name;}
}
