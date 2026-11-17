package tutorial.part32;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void 같은_ID의_상품은_이름과_무관하게_동등하다() {
        Product a = new Product("P001", "노트");
        Product b = new Product("P001", "새 노트");
        Product c = new Product("P001", "세 번째 이름");
        assertAll(() -> assertNotSame(a, b), () -> assertEquals(a, a),
                () -> assertEquals(a, b), () -> assertEquals(b, a),
                () -> assertEquals(b, c), () -> assertEquals(a, c),
                () -> assertEquals(a.hashCode(), b.hashCode()));
    }
    @Test
    void 다른_ID나_다른_타입은_동등하지_않다() {
        Product a = new Product("P001", "노트");
        assertNotEquals(a, new Product("P002", "노트"));
        assertNotEquals(a, null);
        assertNotEquals(a, "P001");
    }
    @Test
    void 해시_컬렉션에서_같은_키로_동작한다() {
        Product a = new Product("P001", "노트"), b = new Product("P001", "수정 이름");
        assertEquals(1, new HashSet<>(List.of(a, b)).size());
        Map<Product, Integer> stocks = new HashMap<>();
        stocks.put(a, 7);
        assertEquals(7, stocks.get(b));
    }
}
