package com.maochong.xiaojun.io.bio.fun02;

import java.io.IOException;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        // 启动服务端
        Thread thread = new Thread(() -> {
            try {
                BIOServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        // 等待线程执行完成,这里不适合用，因为服务端会一直等待
        // thread.join();

        Thread.sleep(2 * 1000);

        System.out.println("开始执行发送.....");
        final char[] op = {'+', '-', '*', '/'};
        final Random random = new Random(System.currentTimeMillis());
        new Thread(() -> {
            while (true) {
                String expression = random.nextInt(10) + "" + op[random.nextInt(4)] + (random.nextInt(10) + 1);
                BIOClient.send(expression);

                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
