package tutorial.part34;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateMoneyExample {
    // 예제 정책: 주문 시각부터 정확히 30분까지(경계 포함) 취소 가능.
    public static boolean canCancel(Instant orderedAt, Clock clock) {
        Duration elapsed = Duration.between(orderedAt, clock.instant());
        return !elapsed.isNegative() && elapsed.compareTo(Duration.ofMinutes(30)) <= 0;
    }

    public static BigDecimal discountedTotal(BigDecimal unitPrice, int quantity, BigDecimal rate) {
        if (unitPrice.signum() < 0 || quantity <= 0
                || rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("금액·수량·할인율 오류");
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.ONE.subtract(rate)).setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        Instant orderedAt = Instant.parse("2026-11-17T03:00:00Z");
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
        System.out.println("주문 시각(서울): " + orderedAt.atZone(seoul).format(format));
        LocalDate deliveryDate = orderedAt.atZone(seoul).toLocalDate().plusDays(3);
        System.out.println("배송 예정일: " + deliveryDate);
        Clock at30 = Clock.fixed(orderedAt.plusSeconds(1800), ZoneOffset.UTC);
        Clock at31 = Clock.fixed(orderedAt.plusSeconds(1860), ZoneOffset.UTC);
        System.out.println("30분 후 취소: " + canCancel(orderedAt, at30));
        System.out.println("31분 후 취소: " + canCancel(orderedAt, at31));
        System.out.println("할인 후 금액: " + discountedTotal(new BigDecimal("19.99"), 3, new BigDecimal("0.10")));
        BigDecimal a = new BigDecimal("2.0"), b = new BigDecimal("2.00");
        System.out.println("2.0 equals 2.00: " + a.equals(b));
        System.out.println("2.0 compareTo 2.00: " + a.compareTo(b));
        System.out.println("1 / 3 (소수 2자리): " + BigDecimal.ONE.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP));
    }
}
