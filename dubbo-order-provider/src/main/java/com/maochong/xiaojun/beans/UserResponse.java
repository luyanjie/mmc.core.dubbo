package com.maochong.xiaojun.beans;

import java.io.Serializable;

/**
 * @author jokin
 */
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 394181735217392082L;

    private int id;

    private String name;

    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
