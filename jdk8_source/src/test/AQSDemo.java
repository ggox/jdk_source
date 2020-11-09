package test;

import java.util.concurrent.CountDownLatch;

/**
 * @author: xuhui
 * @description: AQS 测试
 * @date: 2020/11/9 2:20 下午
 */
public class AQSDemo {

    public static void main(String[] args) {
        CountDownLatch cd = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cd.countDown();
        });
        thread.start();
        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
