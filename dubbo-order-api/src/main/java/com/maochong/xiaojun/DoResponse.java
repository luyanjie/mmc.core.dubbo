package com.maochong.xiaojun;

import java.io.Serializable;

/**
 * @author jokin
 */
public class DoResponse implements Serializable {

    private static final long serialVersionUID = -6581245952395898730L;

    /**
     * 执行Id
     * */
    private int code;

    /**
     * 返回说明
     * */
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DoResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
