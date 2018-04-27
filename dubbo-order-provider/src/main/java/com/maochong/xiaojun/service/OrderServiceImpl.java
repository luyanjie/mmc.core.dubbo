package com.maochong.xiaojun.service;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;

public class OrderServiceImpl implements IOrderServices {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            System.out.println(request.getDate()+"=="+request.getOrderId()+"==="+request.getId());
            response.setCode(200);
            response.setMessage("success");
            response.setOrderId(request.getOrderId());
            return response;
        }
        response.setOrderId("");
        response.setMessage("fail");
        response.setCode(404);
        return response;
    }
}
