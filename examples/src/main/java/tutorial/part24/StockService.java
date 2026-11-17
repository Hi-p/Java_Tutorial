package tutorial.part24;

public final class StockService {
    private int stock;

    public StockService(int initialStock) {
        if (initialStock < 0) throw new IllegalArgumentException("초기 재고는 0 이상이어야 합니다.");
        stock = initialStock;
    }

    public synchronized boolean order(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("주문 수량은 양수여야 합니다.");
        if (quantity > stock) return false;
        stock -= quantity;
        return true;
    }

    public synchronized int getStock() { return stock; }
}
