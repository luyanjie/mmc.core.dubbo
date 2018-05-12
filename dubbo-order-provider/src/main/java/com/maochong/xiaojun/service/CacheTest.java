package com.maochong.xiaojun.service;

import com.maochong.xiaojun.beans.OrderResponse;
import com.maochong.xiaojun.dao.OrderDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableCaching
public class CacheTest {

    private Logger logger = LoggerFactory.getLogger(CacheTest.class);

    @Autowired
    private OrderDaoImpl orderDao;

    @Cacheable(value="lesson", key="'get'+#id")
    public String cache(int id)
    {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        OrderResponse order = orderDao.getUser(paramMap);
        logger.info(order.toString());
        return order.getOrderId();
    }
}
