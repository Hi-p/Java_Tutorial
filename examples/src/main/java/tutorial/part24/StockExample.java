package tutorial.part24;

public class StockExample {
    public static void main(String[] args) {
        StockService service = new StockService(10);
        System.out.println("3개 주문: " + service.order(3));
        System.out.println("8개 주문: " + service.order(8));
        System.out.println("남은 재고: " + service.getStock());
        try {
            service.order(0);
        } catch (IllegalArgumentException e) {
            System.out.println("실패 사유: " + e.getMessage());
        }
    }
}
