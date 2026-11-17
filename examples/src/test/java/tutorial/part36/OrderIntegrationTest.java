package tutorial.part36;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class OrderIntegrationTest {
    @Test
    void 실제_저장소와_서비스와_정책을_조합한다() {
        Product product = new Product("P001", "노트", new BigDecimal("1200.00"));
        Map<String, Product> source = new HashMap<>(Map.of("P001", product));
        ProductRepository repository = new InMemoryProductRepository(source);
        source.clear();
        OrderService service = new OrderService(repository, DiscountPolicy.percentage(new BigDecimal("0.10")));
        assertEquals(new BigDecimal("3240.00"), service.quote("P001", 3).total());
        assertTrue(repository.findById("missing").isEmpty());
    }
}
