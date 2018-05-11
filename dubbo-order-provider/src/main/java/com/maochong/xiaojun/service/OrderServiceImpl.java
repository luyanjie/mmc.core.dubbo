package com.maochong.xiaojun.service;

import com.maochong.xiaojun.beans.OrderResponse;
import com.maochong.xiaojun.dao.OrderDaoImpl;
import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 多版本中的1.0.0版本
 * @author jokin
 * @date 2018-04-15
 * */
@Service("orderService")
public class OrderServiceImpl implements IOrderServices {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDaoImpl orderDao;

    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {

        logger.info("版本1.0.0"+ (request==null?"": request.toString()));

        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("id",request.getId());

            OrderResponse order = orderDao.getUser(paramMap);

            logger.info(order.toString());
            response.setCode(200);
            response.setMessage("success");
            response.setOrderId(order.getOrderId());
            return response;
        }
        response.setOrderId("");
        response.setMessage("fail");
        response.setCode(404);
        return response;
    }
}
