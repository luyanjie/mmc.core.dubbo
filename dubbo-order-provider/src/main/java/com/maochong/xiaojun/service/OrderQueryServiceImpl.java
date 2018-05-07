package com.maochong.xiaojun.service;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;

/**
 * 多版本中的2.0.0版本
 * @author jokin
 * @date 2018-05-04
 * */
public class OrderQueryServiceImpl implements IOrderServices {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            System.out.println("版本2.0.0"+request.toString());
            response.setCode(200);
            response.setMessage("success2.0.0");
            response.setOrderId(request.getOrderId());
            return response;
        }
        response.setOrderId("");
        response.setMessage("fail2.0.0");
        response.setCode(404);
        return response;
    }
}
