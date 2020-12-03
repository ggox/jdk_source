package test;

import javax.xml.transform.SourceLocator;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author: xuhui
 * @description: Demo For CompletableFuture
 * @date: 2020/12/3 1:53 下午
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws IOException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "123";
        });
        future.whenCompleteAsync((s, e) -> {
            System.out.println(s);
        });
        System.in.read();
    }

}
