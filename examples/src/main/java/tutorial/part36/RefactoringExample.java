package tutorial.part36;

import java.math.BigDecimal;
import java.util.Map;

public class RefactoringExample {
    public static void main(String[] args) {
        Product notebook = new Product("P001", "노트", new BigDecimal("1200.00"));
        ProductRepository repository = new InMemoryProductRepository(Map.of(notebook.id(), notebook));
        OrderService regular = new OrderService(repository, DiscountPolicy.none());
        OrderService sale = new OrderService(repository, DiscountPolicy.percentage(new BigDecimal("0.10")));
        System.out.println("정상 견적: " + regular.quote("P001", 3).total());
        System.out.println("10% 할인 견적: " + sale.quote("P001", 3).total());
        System.out.println("상품 조회: " + repository.findById("P001").orElseThrow().name());
    }
}
