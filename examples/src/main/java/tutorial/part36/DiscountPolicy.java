package tutorial.part36;

import java.math.BigDecimal;
import java.util.Objects;

@FunctionalInterface
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal);

    static DiscountPolicy none() { return subtotal -> subtotal; }

    static DiscountPolicy percentage(BigDecimal rate) {
        Objects.requireNonNull(rate);
        if (rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("할인율은 0~1이어야 합니다.");
        }
        return subtotal -> subtotal.multiply(BigDecimal.ONE.subtract(rate));
    }
}
