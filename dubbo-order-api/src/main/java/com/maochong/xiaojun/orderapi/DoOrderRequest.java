package com.maochong.xiaojun.orderapi;

import java.io.Serializable;

/**
 * @author jokin
 */
public class DoOrderRequest implements Serializable {
    private static final long serialVersionUID = 8024148078744647953L;

    /**
     * 编号
     * */
    private int id;

    /**
     * 时间
     * */
    private String date;

    /**
     * orderId
     * */
    private String orderId;
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

    @Override
    public String toString() {
        return "DoOrderRequest{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
