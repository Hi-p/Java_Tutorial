package tutorial.part36;

import java.math.BigDecimal;
import java.util.Objects;

public record Product(String id, String name, BigDecimal price) {
    public Product {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        if (id.isBlank() || price.signum() < 0) throw new IllegalArgumentException("잘못된 상품");
    }
}
