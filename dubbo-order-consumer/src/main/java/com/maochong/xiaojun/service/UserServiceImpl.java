package com.maochong.xiaojun.service;

import com.maochong.xiaojun.userapi.DoUserRequest;
import com.maochong.xiaojun.userapi.DoUserResponse;
import com.maochong.xiaojun.userapi.IUserService;

public class UserServiceImpl implements IUserService {
    @Override
    public DoUserResponse doUser(DoUserRequest request) {
        System.out.println(request.toString());
        DoUserResponse response = new DoUserResponse();
        if(request!=null){
            response.setUserId(request.getUserId());
            response.setCode(200);
            response.setMessage("success");
            return response;
        }
        response.setUserId(0);
        response.setCode(404);
        response.setMessage("fail");
        return  response;
    }
}
