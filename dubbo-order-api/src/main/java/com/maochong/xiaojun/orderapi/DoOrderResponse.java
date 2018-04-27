package com.maochong.xiaojun.orderapi;

import com.maochong.xiaojun.DoResponse;

import java.io.Serializable;

/**
 * @author jokin
 */
public class DoOrderResponse extends DoResponse implements Serializable {
    private static final long serialVersionUID = 2344815507495913964L;

    /**
     * orderId
     * */
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "DoOrderResponse{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
