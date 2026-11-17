package tutorial.part36;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    private final Product product = new Product("P001", "노트", new BigDecimal("1200.00"));

    @ParameterizedTest
    @CsvSource({"0, 3600.00", "0.10, 3240.00", "1, 0.00"})
    void 할인정책에_따른_견적을_계산한다(String rate, String expected) {
        ProductRepository stub = id -> Optional.of(product);
        OrderService service = new OrderService(stub, DiscountPolicy.percentage(new BigDecimal(rate)));
        OrderReceipt receipt = service.quote("P001", 3);
        assertAll(() -> assertEquals("P001", receipt.productId()),
                () -> assertEquals(3, receipt.quantity()),
                () -> assertEquals(new BigDecimal(expected), receipt.total()));
    }
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 잘못된_수량이면_조회하지_않는다(int quantity) {
        ProductRepository shouldNotRun = id -> { throw new AssertionError("불필요한 조회"); };
        OrderService service = new OrderService(shouldNotRun, DiscountPolicy.none());
        assertThrows(IllegalArgumentException.class, () -> service.quote("P001", quantity));
    }
    @Test
    void 없는_상품을_거부한다() {
        OrderService service = new OrderService(id -> Optional.empty(), DiscountPolicy.none());
        assertThrows(NoSuchElementException.class, () -> service.quote("missing", 1));
    }
    @Test
    void 잘못된_할인정책_결과를_거부한다() {
        OrderService service = new OrderService(id -> Optional.of(product), total -> total.negate());
        assertThrows(IllegalStateException.class, () -> service.quote("P001", 1));
    }
}
