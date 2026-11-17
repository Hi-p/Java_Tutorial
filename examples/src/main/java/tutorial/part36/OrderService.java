package tutorial.part36;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Objects;

/** 견적 계산 예제. 주문 저장·재고 차감·결제는 수행하지 않는다. */
public final class OrderService {
    private final ProductRepository repository;
    private final DiscountPolicy discount;

    public OrderService(ProductRepository repository, DiscountPolicy discount) {
        this.repository = Objects.requireNonNull(repository);
        this.discount = Objects.requireNonNull(discount);
    }

    public OrderReceipt quote(String productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("주문 수량은 양수여야 합니다.");
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("상품 없음: " + productId));
        BigDecimal subtotal = product.price().multiply(BigDecimal.valueOf(quantity));
        BigDecimal total = Objects.requireNonNull(discount.apply(subtotal));
        if (total.signum() < 0 || total.compareTo(subtotal) > 0) {
            throw new IllegalStateException("할인 결과가 허용 범위를 벗어났습니다.");
        }
        return new OrderReceipt(productId, quantity, total.setScale(2, RoundingMode.HALF_UP));
    }
}
