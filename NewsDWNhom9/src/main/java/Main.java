import Flows.Crawler;
import Flows.Extract;
import Flows.Transform;


import lombok.Data;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Data
public class Main {

    public static void main(String[] args)   {
        // Số lượng luồng trong thread pool
        int threadPoolSize = 1;

        // Tạo một ScheduledExecutorService với số lượng luồng được xác định
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadPoolSize);

        try {
            // Crawler
            CompletableFuture<Void> crawlerFuture = CompletableFuture.runAsync(() -> new Crawler().excute());

            // Extract
            CompletableFuture<Void> extractFuture = crawlerFuture.thenCompose(result ->
                    CompletableFuture.runAsync(() -> new Extract().excute())
            );

            // Transform
            CompletableFuture<Void> transformFuture = extractFuture.thenRun(() -> {
                scheduledExecutorService.schedule(() -> new Transform().excute(), 1, TimeUnit.SECONDS);
            });

            // Đợi cho tất cả các công việc hoàn thành
            CompletableFuture.allOf(crawlerFuture, extractFuture, transformFuture).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng scheduled executor service
            scheduledExecutorService.shutdown();
        }


    }

}