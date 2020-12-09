package test;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author: xuhui
 * @description: Demo For CompletableFuture
 * @date: 2020/12/3 1:53 下午
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            System.out.println("supply:" + Thread.currentThread().getId());
            throw new RuntimeException("参数不合法");
            // return s;
        });
        System.out.println(Thread.currentThread().getId());

        future.acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "456";
        }), s -> {
            System.out.println("result:" + s);
        });

        TimeUnit.HOURS.sleep(1);
    }

}
