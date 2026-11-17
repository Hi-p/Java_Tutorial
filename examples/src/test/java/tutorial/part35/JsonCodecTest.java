package tutorial.part35;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class JsonCodecTest {
    @Test
    void 한글과_따옴표를_포함한_상품을_왕복변환한다() throws Exception {
        ProductData input = new ProductData("P001", "노트 \"특별판\"", new BigDecimal("1200.00"));
        assertEquals(input, JsonCodec.readProduct(JsonCodec.writeProduct(input)));
    }
    @Test
    void 잘못된_JSON을_거부한다() {
        assertThrows(JsonProcessingException.class, () -> JsonCodec.readProduct("{invalid}"));
    }
    @Test
    void 필수값_누락을_거부한다() {
        assertThrows(JsonProcessingException.class, () -> JsonCodec.readProduct("{\"id\":\"P001\"}"));
    }
    @Test
    void 음수_가격을_거부한다() {
        assertThrows(JsonProcessingException.class, () -> JsonCodec.readProduct(
                "{\"id\":\"P001\",\"name\":\"노트\",\"price\":-1}"));
    }
    @Test
    void null_본문을_거부한다() {
        assertThrows(IllegalArgumentException.class, () -> JsonCodec.readProduct("null"));
    }
    @Test
    void 정의하지_않은_필드를_거부한다() {
        assertThrows(JsonProcessingException.class, () -> JsonCodec.readProduct(
                "{\"id\":\"P001\",\"name\":\"노트\",\"price\":1,\"extra\":true}"));
    }
}
