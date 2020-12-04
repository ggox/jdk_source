package test;

import javax.xml.transform.SourceLocator;
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
            return s;
        });
        System.out.println(Thread.currentThread().getId());
        TimeUnit.SECONDS.sleep(3);
        future.whenComplete((s, e) -> {
            System.out.println("whenComplete:" + Thread.currentThread().getId());
            System.out.println(s);
        });
        TimeUnit.HOURS.sleep(1);
    }

}
