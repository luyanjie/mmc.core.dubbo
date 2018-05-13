package com.maochong.xiaojun.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOClient {

    private final static CountDownLatch countDownLatch = new CountDownLatch(10);
    private final static int threadsCount =10;
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(threadsCount);
        for (int i=0;i<threadsCount;i++ )
        {
            service.execute(()->{
                String uuid = UUID.randomUUID().toString();
                try {
                    countDownLatch.await();
                    // 打开一个Socket服务，也就是开一条通道
                    Socket client = new Socket("localhost",8100);
                    // 输出流通道打开
                    OutputStream os = client.getOutputStream();
                    os.write(uuid.getBytes());
                    os.close();
                    client.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<threadsCount;i++ ){
            countDownLatch.countDown();
        }
    }

}
