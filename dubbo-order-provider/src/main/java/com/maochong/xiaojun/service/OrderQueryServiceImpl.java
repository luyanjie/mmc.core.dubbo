package com.maochong.xiaojun.service;

import com.maochong.xiaojun.beans.UserResponse;
import com.maochong.xiaojun.dao.WangHongDaoImpl;
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
 * 多版本中的2.0.0版本
 * @author jokin
 * @date 2018-05-04
 * */
@Service("orderQueryService")
public class OrderQueryServiceImpl implements IOrderServices {

    private Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    @Autowired
    WangHongDaoImpl wangHongDao;

    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {
        DoOrderResponse response = new DoOrderResponse();
        if(request!=null){
            logger.info("版本2.0.0"+request.toString());

            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("id",request.getId());

            // 测试多数据源
             UserResponse user = wangHongDao.getUser(paramMap);

            logger.info(user.toString());

            response.setCode(200);
            response.setMessage("success2.0.0");
            //response.setOrderId(request.getOrderId());
            response.setOrderId(user.getName());
            return response;
        }
        response.setOrderId("");
        response.setMessage("fail2.0.0");
        response.setCode(404);
        return response;
    }
}
