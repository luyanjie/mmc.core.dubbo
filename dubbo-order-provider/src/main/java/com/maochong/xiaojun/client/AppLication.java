package com.maochong.xiaojun.client;

import com.maochong.xiaojun.userapi.DoUserRequest;
import com.maochong.xiaojun.userapi.DoUserResponse;
import com.maochong.xiaojun.userapi.IUserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppLication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("user-consumer.xml");

        // 用户信息
        IUserService services = (IUserService)context.getBean("userServices");
        DoUserRequest request = new DoUserRequest();
        request.setUserId(88);
        request.setUserName("luyanjie");
        DoUserResponse response = services.doUser(request);

        if(response!=null){
            System.out.println(response.getCode()+"===="+response.getMessage()+"====="+response.getUserId());
        }
        else {
            System.out.println("请求返回null");
        }
    }
}
