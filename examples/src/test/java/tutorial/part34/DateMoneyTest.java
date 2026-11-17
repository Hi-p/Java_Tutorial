package tutorial.part34;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class DateMoneyTest {
    @ParameterizedTest
    @CsvSource({"-1, false", "0, true", "1800, true", "1801, false"})
    void 취소_시간의_경계를_확인한다(long seconds, boolean expected) {
        Instant ordered = Instant.parse("2026-11-17T03:00:00Z");
        Clock clock = Clock.fixed(ordered.plusSeconds(seconds), ZoneOffset.UTC);
        assertEquals(expected, DateMoneyExample.canCancel(ordered, clock));
    }
    @Test
    void 할인_계산_마지막에_소수_둘째자리로_반올림한다() {
        assertEquals(new BigDecimal("53.97"), DateMoneyExample.discountedTotal(
                new BigDecimal("19.99"), 3, new BigDecimal("0.10")));
    }
    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "1.01"})
    void 범위밖_할인율을_거부한다(String rate) {
        assertThrows(IllegalArgumentException.class, () -> DateMoneyExample.discountedTotal(
                BigDecimal.TEN, 1, new BigDecimal(rate)));
    }
}
