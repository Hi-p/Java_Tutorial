package tutorial.part16;

import java.util.concurrent.*;

public class CancellationExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(1);
        try {
            Future<?> job = pool.submit(() -> {
                started.countDown();
                try {
                    new CountDownLatch(1).await(); // 취소 전까지 대기하는 작업
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태 복구
                } finally {
                    finished.countDown();
                }
            });
            if (!started.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("시작 시간 초과");
            System.out.println("취소 요청 수락: " + job.cancel(true));
            if (!finished.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("종료 시간 초과");
            System.out.println("작업 종료 확인: true");
            System.out.println("Future 취소 상태: " + job.isCancelled());
        } finally {
            pool.shutdownNow();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
