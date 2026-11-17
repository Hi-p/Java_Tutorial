package tutorial.part33;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import tutorial.part33.ImmutabilityExample.*;

class ImmutabilityTest {
    @Test
    void 주문은_입력_리스트의_후속_변경과_분리된다() {
        List<OrderLine> input = new ArrayList<>(List.of(new OrderLine("P001", 1000, 2)));
        OrderSnapshot order = new OrderSnapshot(input);
        input.clear();
        assertEquals(1, order.lines().size());
        assertEquals(1000, order.lines().get(0).unitPrice());
        assertThrows(UnsupportedOperationException.class, () -> order.lines().clear());
    }
    @Test
    void 수정불가_리스트도_가변_요소는_보호하지_못한다() {
        MutableProduct product = new MutableProduct("P001", 1000);
        List<MutableProduct> list = List.copyOf(List.of(product));
        product.setPrice(2000);
        assertEquals(2000, list.get(0).getPrice());
    }
    @Test
    void 주문항목의_잘못된_값을_거부한다() {
        assertThrows(IllegalArgumentException.class, () -> new OrderLine("P001", -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new OrderLine("P001", 1000, 0));
    }
}
