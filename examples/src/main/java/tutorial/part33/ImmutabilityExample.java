package tutorial.part33;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImmutabilityExample {
    public static final class MutableProduct {
        private final String id;
        private int price;
        public MutableProduct(String id, int price) { this.id = id; this.price = price; }
        public String getId() { return id; }
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
    }

    public record OrderLine(String productId, int unitPrice, int quantity) {
        public OrderLine {
            Objects.requireNonNull(productId);
            if (unitPrice < 0 || quantity <= 0) throw new IllegalArgumentException("금액·수량 오류");
        }
    }

    public record OrderSnapshot(List<OrderLine> lines) {
        public OrderSnapshot { lines = List.copyOf(lines); }
    }

    public static void main(String[] args) {
        MutableProduct product = new MutableProduct("P001", 1000);
        List<MutableProduct> original = new ArrayList<>(List.of(product));
        List<MutableProduct> shallow = new ArrayList<>(original);
        // 이 예제의 요소는 String과 int만 가지므로 이 복사로 가변 상태가 분리된다.
        List<MutableProduct> deep = original.stream()
                .map(p -> new MutableProduct(p.getId(), p.getPrice())).toList();
        List<OrderLine> lines = new ArrayList<>(List.of(
                new OrderLine(product.getId(), product.getPrice(), 2)));
        OrderSnapshot order = new OrderSnapshot(lines);

        product.setPrice(2000);
        original.clear();
        lines.clear();
        System.out.println("원본 리스트 크기: " + original.size());
        System.out.println("얕은 복사 가격: " + shallow.get(0).getPrice());
        System.out.println("깊은 복사 가격: " + deep.get(0).getPrice());
        System.out.println("주문 당시 가격: " + order.lines().get(0).unitPrice());
        System.out.println("주문 항목 수: " + order.lines().size());
        try {
            order.lines().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("주문 목록 직접 수정: 차단");
        }
    }
}
