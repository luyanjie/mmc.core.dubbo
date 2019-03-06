package com.maochong.xiaojun.orderapi;

/**
 * @author jokin
 * @date 2018/6/21 17:22
 */
public class OrderMock implements IOrderServices {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        response.setCode(0);
        response.setMessage("系统繁忙，稍后再试！");
        return response;
    }
}
