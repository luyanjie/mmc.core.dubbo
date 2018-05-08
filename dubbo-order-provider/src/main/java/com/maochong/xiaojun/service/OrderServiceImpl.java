package com.maochong.xiaojun.service;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.springframework.stereotype.Service;

/**
 * 多版本中的1.0.0版本
 * @author jokin
 * @date 2018-04-15
 * */
@Service("orderService")
public class OrderServiceImpl implements IOrderServices {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            System.out.println("版本1.0.0"+request.toString());
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
