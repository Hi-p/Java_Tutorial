package tutorial.part24;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class StockServiceTest {
    @Test
    void 음수_초기재고를_거부한다() {
        assertThrows(IllegalArgumentException.class, () -> new StockService(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void 잘못된_수량은_재고를_바꾸지_않는다(int quantity) {
        StockService service = new StockService(10); // Given
        assertThrows(IllegalArgumentException.class, () -> service.order(quantity)); // When
        assertEquals(10, service.getStock()); // Then
    }

    @ParameterizedTest
    @CsvSource({"1, 9", "10, 0"})
    void 재고_범위의_주문은_차감된다(int quantity, int remaining) {
        StockService service = new StockService(10);
        assertTrue(service.order(quantity));
        assertEquals(remaining, service.getStock());
    }

    @Test
    void 재고보다_큰_주문은_실패한다() {
        StockService service = new StockService(10);
        assertFalse(service.order(11));
        assertEquals(10, service.getStock());
    }

    @Test
    void 품절이면_주문할_수_없다() {
        assertFalse(new StockService(0).order(1));
    }

    @Test
    @Timeout(10)
    void 동시_주문도_재고를_초과하지_않는다() throws Exception {
        StockService service = new StockService(10);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> results = new ArrayList<>();
        try {
            for (int i = 0; i < 100; i++) {
                results.add(pool.submit(() -> { start.await(); return service.order(1); }));
            }
            start.countDown();
            int successes = 0;
            for (Future<Boolean> result : results) {
                if (result.get(5, TimeUnit.SECONDS)) successes++;
            }
            assertEquals(10, successes);
            assertEquals(0, service.getStock());
        } finally {
            pool.shutdownNow();
            assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS));
        }
    }
}
