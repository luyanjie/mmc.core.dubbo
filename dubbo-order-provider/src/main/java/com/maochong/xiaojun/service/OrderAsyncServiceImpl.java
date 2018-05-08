package com.maochong.xiaojun.service;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.springframework.stereotype.Service;

/**
 * 区分服务消费者异步调用的返回内容，单独添加一个实现类
 * @author jokin
 * @date 2018-05-04
 * */
@Service("orderAsyncService")
public class OrderAsyncServiceImpl implements IOrderServices {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            System.out.println("异步调用版本2.2.0"+request.toString());
            response.setCode(200);
            response.setMessage("2.2.0 success");
            response.setOrderId(request.getOrderId());
            return response;
        }
        response.setOrderId("");
        response.setMessage("2.2.0 fail");
        response.setCode(404);
        return response;
    }
}
