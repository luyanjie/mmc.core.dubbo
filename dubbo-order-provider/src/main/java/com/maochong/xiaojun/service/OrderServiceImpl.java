package com.maochong.xiaojun.service;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

/**
 * 多版本中的1.0.0版本
 * @author jokin
 * @date 2018-04-15
 * */
@Service("orderService")

public class OrderServiceImpl implements IOrderServices {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private CacheTest cacheTest;

    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {

        logger.info("版本1.0.0"+ (request==null?"": request.toString()));

        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            String orderId = cacheTest.cache(request.getId());
            response.setCode(200);
            response.setMessage("success");
            response.setOrderId(orderId);
            return response;
        }
        response.setOrderId("");
        response.setMessage("fail");
        response.setCode(404);
        return response;
    }


}
