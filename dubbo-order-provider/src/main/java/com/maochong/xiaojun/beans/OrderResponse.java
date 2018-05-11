package com.maochong.xiaojun.beans;

import java.io.Serializable;

/**
 * @author jokin
 */
public class OrderResponse implements Serializable {
    private static final long serialVersionUID = -175237200585362942L;

    private int id;
    private String date;
    private String orderId;
    private int uid;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", orderId='" + orderId + '\'' +
                ", uid=" + uid +
                ", message='" + message + '\'' +
                '}';
    }
}
