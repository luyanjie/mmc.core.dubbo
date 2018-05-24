package com.maochong.xiaojun.protocol;

/**
 * @author jokin
 * @date 2018/5/24 14:09
 */
public enum  EnumUrlType {
   CSS(".css"),JS(".js"),IMAGE("(jpg|png|gif)$");

    EnumUrlType(String name){
        this.name = name;
    }

    private String name;

    public String getName(){
        return this.name;
    }
}
