package com.maochong.xiaojun.client;

import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class AppLication {
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("order-consumer.xml");

        // 用户下单

        IOrderServices services = (IOrderServices)context.getBean("orderServices");
        DoOrderRequest request = new DoOrderRequest();
        request.setDate("2018-04-27");
        request.setId(1);
        request.setOrderId("JJ09324234");
        DoOrderResponse response = services.doOrder(request);

        if(response!=null){
            System.out.println(response.getCode()+"===="+response.getMessage()+"====="+response.getOrderId());
        }
        else {
            System.out.println("请求返回null");
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
