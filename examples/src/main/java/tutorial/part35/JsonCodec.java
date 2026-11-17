package tutorial.part35;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonCodec {
    // 첫 사용 이후 설정을 변경하지 않는다.
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private JsonCodec() { }

    public static ProductData readProduct(String json) throws JsonProcessingException {
        ProductData product = MAPPER.readValue(json, ProductData.class);
        if (product == null) throw new IllegalArgumentException("상품은 null일 수 없습니다.");
        return product;
    }

    public static String writeProduct(ProductData product) throws JsonProcessingException {
        return MAPPER.writeValueAsString(product);
    }
}
