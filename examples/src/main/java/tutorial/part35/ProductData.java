package tutorial.part35;

import java.math.BigDecimal;
import java.util.Objects;

public record ProductData(String id, String name, BigDecimal price) {
    public ProductData {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
        if (id.isBlank() || name.isBlank() || price.signum() < 0) {
            throw new IllegalArgumentException("잘못된 상품 데이터");
        }
    }
}
