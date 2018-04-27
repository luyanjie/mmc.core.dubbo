package com.maochong.xiaojun.userapi;

import com.maochong.xiaojun.DoResponse;

import java.io.Serializable;

/**
 * @author jokin
 */
public class DoUserResponse extends DoResponse implements Serializable {
    private static final long serialVersionUID = -1333205860481097963L;

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DoUserResponse{" +
                "userId=" + userId +
                '}';
    }
}
