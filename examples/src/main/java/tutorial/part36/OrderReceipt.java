package tutorial.part36;

import java.math.BigDecimal;

public record OrderReceipt(String productId, int quantity, BigDecimal total) { }
