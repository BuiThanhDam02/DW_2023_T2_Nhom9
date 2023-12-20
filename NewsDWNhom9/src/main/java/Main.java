import DAO.ControlDAO;
import Flows.*;


import Models.Status;
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
            ControlDAO controlDAO = new ControlDAO();
//            if (controlDAO.checkConfigStatus(Status.LOADED.name())) controlDAO.setConfigStatus(Status.PREPARE.name());
            // Crawler
            CompletableFuture<Void> crawlerFuture = CompletableFuture.runAsync(() -> new Crawler(controlDAO).excute());

            // Extract
            CompletableFuture<Void> extractFuture = crawlerFuture.thenCompose(result ->
                    CompletableFuture.runAsync(() -> new Extract(controlDAO).excute())
            );

            // Transform
            CompletableFuture<Void> transformFuture = extractFuture.thenRun(() -> {
                scheduledExecutorService.schedule(() -> new Transform(controlDAO).excute(), 1, TimeUnit.SECONDS);
            });
            // Aggregate
            CompletableFuture<Void> aggregateFuture = transformFuture.thenRun(() -> {

                scheduledExecutorService.schedule(() ->  new Aggregate(controlDAO).excute(), 1, TimeUnit.SECONDS);
            });

            // Mart
            CompletableFuture<Void> martFuture = aggregateFuture.thenRun(() -> {

                scheduledExecutorService.schedule(() ->  new Load(controlDAO).excute(), 1, TimeUnit.SECONDS);
            });
//             Đợi cho tất cả các công việc hoàn thành
            CompletableFuture.allOf(crawlerFuture, extractFuture, transformFuture,aggregateFuture,martFuture).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng scheduled executor service
            scheduledExecutorService.shutdown();
        }


    }

}