# Part 34. 날짜·시간과 금액 처리

> 주문 시각·취소 기한·할인 금액을 예로 들어 **시간대와 소수 계산 규칙**을 명확히 표현한다.

선행 학습: [Part 7 객체지향](part7_객체지향기초.md), [Part 33 불변 객체](part33_불변객체와객체복사.md)

---

## 34.1 java.time 타입 선택

| 타입 | 표현하는 것 | 사용 예 |
|---|---|---|
| `LocalDate` | 날짜 | 배송 예정일, 생일 |
| `LocalTime` | 시각 | 매일 영업 시작 시각 |
| `LocalDateTime` | 시간대 없는 날짜와 시각 | 특정 지역의 벽시계 시각 표현 |
| `Instant` | 시간선상의 한 시점 | 주문·로그의 발생 시점 |
| `ZonedDateTime` | 지역 시간대 규칙이 포함된 날짜와 시각 | 서울·뉴욕 등 지역 기준 표시 |
| `Duration` | 초·나노초 기반 시간 차이 | 주문 후 30분 |
| `Period` | 연·월·일 단위 기간 | 한 달 뒤, 3일 뒤 |

`LocalDateTime`에는 시간대가 없다. `2026-11-17 12:00`만으로는 서울의 정오인지 다른 지역의 정오인지 알 수 없다. 저장·통신 정책과 사용자 표시 정책을 구분한다.

```java
Instant orderedAt = Instant.parse("2026-11-17T03:00:00Z");
ZonedDateTime inSeoul = orderedAt.atZone(ZoneId.of("Asia/Seoul"));
```

`java.time`의 주요 날짜·시간 객체는 불변이다. `date.plusDays(3)`의 반환값을 받아야 계산 결과를 사용할 수 있다.

---

## 34.2 날짜 계산·포맷·시간대

```java
LocalDate delivery = LocalDate.of(2026, 11, 17).plusDays(3);
DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
String display = orderedAt.atZone(ZoneId.of("Asia/Seoul")).format(format);
```

- `MM`은 월, `mm`은 분이다.
- `HH`는 24시간 형식이다.
- `uuuu`는 연도, `YYYY`는 주 기준 연도이므로 연말·연초에 다른 값이 나올 수 있다.
- 사용자 입력은 날짜·시각 형식과 허용 범위를 검증하고, 파싱 실패를 처리한다.

“다음 날 같은 시각”과 “정확히 24시간 뒤”는 일광절약시간제를 사용하는 지역에서 다를 수 있다. 달력 기준 계산과 실제 경과 시간 계산을 구분한다. 월말에 한 달을 더할 때도 일수를 단순히 30으로 치환하지 않는다.

---

## 34.3 Clock으로 현재 시각 주입

현재 시각을 코드 안에서 바로 읽으면 테스트 날짜·실행 타이밍에 따라 결과가 달라질 수 있다. `Clock`을 받으면 테스트에서 시간을 고정할 수 있다.

```java
public static boolean canCancel(Instant orderedAt, Clock clock) {
    Duration elapsed = Duration.between(orderedAt, clock.instant());
    return !elapsed.isNegative()
            && elapsed.compareTo(Duration.ofMinutes(30)) <= 0;
}
```

이 예제는 주문 시각부터 **정확히 30분까지 포함**해 취소를 허용한다. 주문 전 시각은 거부한다. `toMinutes() <= 30`처럼 잘린 분 단위로 비교하면 30분 59초도 허용하는 오류가 생길 수 있다.

```java
Clock fixed = Clock.fixed(orderedAt.plusSeconds(1800), ZoneOffset.UTC);
```

운영 시각에는 `Clock.systemUTC()` 등을, 테스트에는 `Clock.fixed()`를 전달할 수 있다. 시간대가 다르더라도 `Instant`가 같은 시점을 가리키는지 확인한다.

---

## 34.4 BigDecimal과 금액 표현

`double`은 이진 부동소수점이므로 모든 십진 소수를 정확히 표현하지 못한다. 금액을 다룰 때는 통화·최소 단위·반올림 정책을 정하고 적절한 타입을 사용한다. 원 단위 정수만 필요하면 정수 타입도 선택지다.

```java
BigDecimal amount = new BigDecimal("19.99"); // 문자열에서 정확한 십진 값
BigDecimal rate = new BigDecimal("0.10");
BigDecimal total = amount.multiply(BigDecimal.valueOf(3))
        .multiply(BigDecimal.ONE.subtract(rate))
        .setScale(2, RoundingMode.HALF_UP);     // 53.97
```

- `new BigDecimal(0.1)`은 이미 오차가 있는 double의 값을 받아 예상치 못한 긴 소수가 될 수 있다.
- 십진 리터럴은 문자열 생성자를 사용한다. 기존 double에서 변환할 때는 `valueOf()`의 의미를 이해하고 사용한다.
- `BigDecimal`도 불변이므로 `add()`·`multiply()`의 반환값을 사용한다.
- 위 예제는 **소수 두 자리 금액을 사용하는 가상의 정책**이다. 모든 통화에 소수 두 자리를 강제하는 규칙은 아니다.

반올림을 품목마다 할지, 합산 후 마지막에 할지에 따라 결과가 달라질 수 있다. 예제는 수량 곱셈 → 할인 → 마지막 반올림 순서를 따른다.

---

## 34.5 scale, equals, compareTo와 나눗셈

```java
BigDecimal a = new BigDecimal("2.0");
BigDecimal b = new BigDecimal("2.00");
System.out.println(a.equals(b));     // false: scale이 다름
System.out.println(a.compareTo(b));  // 0: 수치가 같음
```

`equals()`는 수치와 scale을 함께 비교한다. `HashSet`과 `TreeSet`에 넣었을 때 중복 판단이 다를 수 있으므로 금액 값 객체의 정규화·비교 정책을 정한다.

```java
BigDecimal third = BigDecimal.ONE.divide(
        new BigDecimal("3"), 2, RoundingMode.HALF_UP); // 0.33
```

정확히 끝나지 않는 소수에 대해 반올림 정책 없이 `divide()`하면 `ArithmeticException`이 발생할 수 있다. 0으로 나누는 경우도 별도로 처리한다. `scale`은 소수점 자리와 관련된 값이고 `MathContext`의 precision은 유효 숫자 수이므로 혼동하지 않는다.

---

## 34.6 실행과 예상 결과

전체 코드: [DateMoneyExample.java](examples/src/main/java/tutorial/part34/DateMoneyExample.java)

```bash
bash examples/run-core.sh 34
```

```text
주문 시각(서울): 2026-11-17 12:00
배송 예정일: 2026-11-20
30분 후 취소: true
31분 후 취소: false
할인 후 금액: 53.97
2.0 equals 2.00: false
2.0 compareTo 2.00: 0
1 / 3 (소수 2자리): 0.33
```

고정 입력을 사용하므로 실행 날짜와 컴퓨터의 기본 시간대가 달라도 같은 결과를 기대할 수 있다.

검증 코드: [DateMoneyTest.java](examples/src/test/java/tutorial/part34/DateMoneyTest.java)

---

## 34.7 연습문제

1. 주문 후 정확히 30분과 30분 1초의 취소 가능 여부를 확인하자.
2. 같은 주문 시각을 `Asia/Seoul`과 `America/New_York`로 각각 표시하자.
3. 가격 19.99인 상품 3개에 10% 할인을 적용하고 반올림 위치를 바꿔 비교하자.
4. `2.0`과 `2.00`을 금액으로 같게 취급하려면 비교·정규화 정책을 어떻게 정할까?

<details>
<summary>정답과 풀이 방향</summary>

1. 이 예제 정책에서는 true, false다. 초·나노초 단위의 `Duration`을 직접 비교한다.
2. 같은 `Instant`에 서로 다른 `ZoneId`를 적용한다. 시점은 그대로이고 표시가 달라진다.
3. 합산 후 할인·반올림은 53.97이다. 품목별 할인 후 소수 두 자리 반올림도 이 입력에서는 같은 값이지만, 다른 가격·할인율에서는 달라질 수 있다. 예를 들어 0.05짜리 3개에 10% 할인을 적용하면 품목별 반올림은 0.15, 합산 후 반올림은 0.14다.
4. 수치 비교에는 `compareTo() == 0`을 쓸 수 있다. 해시 키나 값 객체에는 정해진 scale로 정규화하는 등 equals·hashCode까지 일관된 정책이 필요하다.

</details>

공식 참고: [java.time](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/package-summary.html), [BigDecimal](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html)

[목차](README.md) · [다음: HTTP와 JSON](part35_HTTP클라이언트와JSON.md)
