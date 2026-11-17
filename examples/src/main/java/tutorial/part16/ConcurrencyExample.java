package tutorial.part16;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyExample {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        AtomicInteger completed = new AtomicInteger();
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        try {
            List<Future<?>> jobs = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                jobs.add(pool.submit(() -> {
                    completed.incrementAndGet();
                    counts.merge("P001", 1, Integer::sum);
                }));
            }
            // sleep으로 완료를 추측하지 않고 실제 작업 완료를 기다린다.
            for (Future<?> job : jobs) job.get(5, TimeUnit.SECONDS);
            System.out.println("완료 작업: " + completed.get());
            System.out.println("P001 집계: " + counts.get("P001"));

            Future<Integer> total = pool.submit(() -> 1200 * 3);
            System.out.println("Future 합계: " + total.get(5, TimeUnit.SECONDS));

            CompletableFuture<Integer> discounted = CompletableFuture
                    .supplyAsync(() -> 1200 * 3, pool)
                    .thenApply(price -> price - 600);
            System.out.println("비동기 할인 합계: " + discounted.get(5, TimeUnit.SECONDS));
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) pool.shutdownNow();
        }
    }
}
