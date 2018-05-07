package com.maochong.xiaojun.client;

import com.alibaba.dubbo.rpc.RpcContext;
import com.maochong.xiaojun.orderapi.DoOrderRequest;
import com.maochong.xiaojun.orderapi.DoOrderResponse;
import com.maochong.xiaojun.orderapi.IOrderServices;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AppLication {
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("order-consumer.xml");
        // 用户下单
        DoOrderRequest request = new DoOrderRequest();
        request.setDate("2018-04-27");
        request.setId(1);
        request.setOrderId("JJ09324234");

        /**
         * 版本1.0.0
         * */
        v1(context,request);

        /**
         * 版本2.0.0
         * */
        v2(context,request);

        /**
         * 版本2.2.0 异步调用测试
         * */
        async(context,request);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 版本1.0.0
     * */
    static void  v1(ClassPathXmlApplicationContext context,DoOrderRequest request)
    {
        System.out.println("======版本1.0.0===========");
        IOrderServices services = (IOrderServices)context.getBean("orderServices");
        DoOrderResponse response = services.doOrder(request);

        if(response!=null){
            System.out.println("版本1.0.0"+response.toString()+"message:"+ response.getMessage()+"code : " + response.getCode());
        }
        else {
            System.out.println("版本1.0.0 请求返回null");
        }
    }

    /**
     * 版本2.0.0
     * */
    static void v2(ClassPathXmlApplicationContext context,DoOrderRequest request){
        System.out.println("======版本2.0.0===========");
        IOrderServices services = (IOrderServices)context.getBean("orderQueryServices");

        try {
            System.out.println("开始等待..........");
            // 这个时候如果注册中心挂了，如果有把注册服务写到本地，依然可以使用
            //
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DoOrderResponse response = services.doOrder(request);

        if(response!=null){
            System.out.println("版本2.0.0"+response.toString() +"message:"+ response.getMessage()+"code : " + response.getCode());
        }
        else {
            System.out.println("版本2.0.0 请求返回null");
        }
    }

    /**
     * 异步调用
     * */
    static void async(ClassPathXmlApplicationContext context,DoOrderRequest request)
    {
        System.out.println("==========异步调用==========");
        IOrderServices servicesAsync = (IOrderServices)context.getBean("orderAsyncServices");
        // 此调用会立即返回null
        DoOrderResponse responseAsync = servicesAsync.doOrder(request);
        if(responseAsync!=null){
            System.out.println("版本2.2.0 async "+responseAsync.toString() +"message:"+ responseAsync.getMessage()+"code : " + responseAsync.getCode());
        }
        else {
            System.out.println("版本2.0.0 async 请求返回null");
        }
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        Future<DoOrderResponse> fooFuture = RpcContext.getContext().getFuture();

        try {
            // 如果foo已返回，直接拿到返回值，否则线程wait住，等待foo返回后，线程会被notify唤醒
            responseAsync = fooFuture.get();
            if(responseAsync!=null){
                System.out.println("版本2.2.0 async "+responseAsync.toString() +"message:"+ responseAsync.getMessage()+"code : " + responseAsync.getCode());
            }
            else {
                System.out.println("版本2.0.0 async 请求返回null");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
