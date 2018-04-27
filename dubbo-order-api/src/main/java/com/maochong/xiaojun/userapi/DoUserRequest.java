package com.maochong.xiaojun.userapi;

import java.io.Serializable;

/**
 * @author jokin
 */
public class DoUserRequest implements Serializable {

    private static final long serialVersionUID = -4693753582072054304L;

    /**
     * 用户Id
     * */
    private int userId;

    /**
     * 用户名称
     * */
    private  String  userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "DoUserRequest{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
