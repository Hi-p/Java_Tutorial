# Part 24. 테스트 (JUnit 5)

> 정상 동작뿐 아니라 **실패 상황·경계값·상태 변화**를 자동으로 확인한다.

이 파트는 [StockService.java](examples/src/main/java/tutorial/part24/StockService.java)와 [StockServiceTest.java](examples/src/test/java/tutorial/part24/StockServiceTest.java)를 직접 실행하며 공부한다. JDK 17 이상, JUnit Jupiter 5.10.1을 사용한다. 버전은 예제 재현을 위해 고정했으며 최신 버전이라는 의미는 아니다.

---

## 24.1 무엇을 테스트할까?

예제의 재고 규칙은 다음과 같다.

| 상황 | 결과 | 재고 변화 |
|---|---|---|
| 초기 재고가 음수 | `IllegalArgumentException` | 객체 생성 실패 |
| 주문 수량이 0 이하 | `IllegalArgumentException` | 변경 없음 |
| 주문 수량이 재고 이하 | `true` | 수량만큼 차감 |
| 주문 수량이 재고 초과 | `false` | 변경 없음 |

“코드를 그대로 따라 쓴 테스트”보다 **업무 규칙을 독립적으로 확인하는 테스트**를 작성한다. 정상 주문 한 번만 확인하면 경계의 오류를 놓치기 쉽다.

---

## 24.2 JUnit 기본과 Given–When–Then

```java
@Test
void 재고보다_큰_주문은_실패한다() {
    StockService service = new StockService(10); // Given: 준비
    boolean success = service.order(11);        // When: 실행
    assertFalse(success);                       // Then: 결과 확인
    assertEquals(10, service.getStock());        // 실패 시 상태도 유지
}
```

전체 클래스에서는 `org.junit.jupiter.api.Test`와 `org.junit.jupiter.api.Assertions.*`를 import한다. 테스트 클래스·메소드는 JUnit 5에서 반드시 `public`일 필요가 없다.

**테스트 하나는 하나의 행동·규칙에 집중한다.** 그 규칙을 확인하기 위해 여러 `assert`를 쓰는 것은 괜찮다. “한 테스트에는 assert 하나만”이라는 제한으로 해석하지 않는다.

| Assertion | 용도 |
|---|---|
| `assertEquals(expected, actual)` | 기대 값과 실제 값 비교 |
| `assertSame(expected, actual)` | 같은 객체 참조인지 확인 |
| `assertTrue()` / `assertFalse()` | 조건 확인 |
| `assertNull()` / `assertNotNull()` | null 여부 확인 |
| `assertThrows(Type.class, action)` | 예상한 예외 또는 하위 타입이 발생하는지 확인 |
| `assertAll(...)` | 관련 검증을 묶어 여러 실패를 함께 확인 |

`assertEquals()`와 `assertSame()`의 차이는 [Part 32](part32_Object와객체비교.md)와 연결된다. `BigDecimal.equals()`는 scale도 비교하므로 금액의 수치만 비교할지 표현 자리수까지 비교할지도 정한다.

---

## 24.3 경계값과 매개변수화 테스트

재고가 10이면 최소한 수량 **-1, 0, 1, 10, 11**을 생각해 본다.

```java
@ParameterizedTest
@ValueSource(ints = {0, -1, Integer.MIN_VALUE})
void 잘못된_수량은_재고를_바꾸지_않는다(int quantity) {
    StockService service = new StockService(10);
    assertThrows(IllegalArgumentException.class, () -> service.order(quantity));
    assertEquals(10, service.getStock());
}
```

```java
@ParameterizedTest
@CsvSource({"1, 9", "10, 0"})
void 재고_범위의_주문은_차감된다(int quantity, int remaining) {
    StockService service = new StockService(10);
    assertTrue(service.order(quantity));
    assertEquals(remaining, service.getStock());
}
```

`@ValueSource`는 단순한 값 목록, `@CsvSource`는 입력·예상 결과 조합에 편하다. 객체나 복잡한 데이터는 `@MethodSource`를 고려한다. 이 예제의 `junit-jupiter` 의존성에는 매개변수화 테스트 모듈도 포함된다.

---

## 24.4 생명주기와 테스트 독립성

| 어노테이션 | 실행 시점 |
|---|---|
| `@BeforeEach` | 각 테스트 실행 전 |
| `@AfterEach` | 각 테스트 실행 후 |
| `@BeforeAll` | 클래스의 모든 테스트 전 |
| `@AfterAll` | 클래스의 모든 테스트 후 |

기본 인스턴스 생명주기에서는 `@BeforeAll`·`@AfterAll` 메소드를 `static`으로 작성한다. `@TestInstance(PER_CLASS)`를 쓰면 조건이 달라진다.

각 테스트는 새 상태에서 시작하게 만든다. 다른 테스트가 먼저 재고를 초기화하거나 주문해 줄 것이라고 가정하지 않는다. DB·파일·서버·스레드를 생성했다면 성공·실패와 무관하게 정리한다.

---

## 24.5 단위 테스트, 통합 테스트, 테스트 대역

| 종류 | 이 프로젝트의 예 | 확인 대상 |
|---|---|---|
| 단위 테스트 | `StockServiceTest`, `OrderServiceTest` | 제한된 단위의 업무 규칙 |
| 구성 요소 통합 테스트 | `OrderIntegrationTest` | 실제 메모리 저장소 + 서비스 + 할인 정책의 협력 |
| HTTP 통합 테스트 | `HttpJsonIntegrationTest` | 로컬 서버 + HTTP 요청 + JSON 해석 |

통합 테스트가 반드시 외부 DB를 써야 하는 것은 아니다. 무엇을 실제로 연결했는지 테스트 범위를 명시한다.

외부 의존성을 대체하는 도구를 **테스트 대역(test double)**이라고 한다.

| 대역 | 역할 |
|---|---|
| Stub | 정해진 응답을 반환 |
| Fake | 메모리 저장소처럼 단순하지만 동작하는 구현 |
| Mock | 기대한 호출·인자를 검증하도록 구성 |

```java
ProductRepository stub = id -> Optional.of(product);
OrderService service = new OrderService(stub, DiscountPolicy.none());
```

이 예제는 간단한 람다 stub을 사용하므로 Mockito가 필요 없다. Mockito를 쓰면 `when(...).thenReturn(...)`, `verify(...)` 등으로 대역을 구성할 수 있지만 별도 의존성이 필요하다. **모든 의존성을 무조건 Mock으로 바꾸는 것은 아니다.** 어떤 행동을 검증하는지 먼저 정한다.

[OrderServiceTest.java](examples/src/test/java/tutorial/part36/OrderServiceTest.java)와 [OrderIntegrationTest.java](examples/src/test/java/tutorial/part36/OrderIntegrationTest.java)를 비교해 보자.

---

## 24.6 시간과 동시성 테스트

현재 시각을 직접 사용하면 실행 날짜에 따라 결과가 바뀔 수 있다. [Part 34](part34_날짜시간과금액처리.md)의 `Clock` 주입으로 “주문 후 정확히 30분” 같은 상황을 만든다.

동시 주문 테스트는 다음 순서로 진행한다.

1. 공유 재고 10개와 작업 스레드를 준비한다.
2. `CountDownLatch`를 사용해 작업 시작을 조율한다.
3. 1개씩 주문하는 작업을 총 100개 제출한다.
4. 모든 `Future`의 완료를 확인한다.
5. 성공 10회·남은 재고 0개인지 확인하고 스레드 풀을 종료한다.

`sleep(1000)` 뒤에 값만 읽는 방식은 작업 완료를 보장하지 않는다. `@Timeout`과 시간 제한이 있는 대기를 사용하면 테스트가 끝없이 대기하는 상황을 줄일 수 있다.

> 동시성 테스트가 한 번 통과했다고 모든 실행 순서에서 안전하다는 증명이 되지는 않는다. 공유 상태와 잠금 설계도 함께 검토한다.

---

## 24.7 실행 방법과 예상 결과

[실행 안내](examples/README.md)의 Gradle 준비 방법을 따른 뒤 저장소 루트에서 실행한다.

```bash
cd examples
# 재고 테스트만 실행: 9개 테스트 실행이 성공해야 함
gradle test --tests tutorial.part24.StockServiceTest

# 전체 기본 테스트: 36개 테스트 실행이 성공해야 함
gradle test
```

Gradle의 전체 출력 형식·실행 시간은 환경에 따라 다르지만 결과는 `BUILD SUCCESSFUL`이어야 한다. 테스트가 이미 실행되어 변경이 없으면 `UP-TO-DATE`로 표시될 수 있다. 다시 실행하려면 `--rerun-tasks`를 추가한다.

기본 설정은 `network` 태그의 HTTP 테스트 2개를 제외한다. 로컬 포트 사용이 가능한 컴퓨터에서 다음 명령으로 포함한다.

```bash
gradle test -PnetworkTests --rerun-tasks
```

모두 통과하면 총 **38개** 테스트 실행이 성공한다. 보고서는 `examples/build/reports/tests/test/index.html`에 생성된다.

JUnit 없이 업무 로직의 출력만 보려면 저장소 루트에서 실행한다.

```bash
bash examples/run-core.sh 24
```

```text
3개 주문: true
8개 주문: false
남은 재고: 7
실패 사유: 주문 수량은 양수여야 합니다.
```

---

## 24.8 연습문제

1. 초기 재고 0일 때 1개 주문하는 테스트를 작성하자.
2. 주문 수량 `Integer.MAX_VALUE`를 전달해 재고가 음수가 되지 않는지 확인하자.
3. 할인 정책이 소계를 초과하는 금액을 반환하면 서비스가 거부하는지 테스트하자.
4. 한 테스트에서 주문 성공 여부와 남은 재고를 함께 검증해도 되는 이유는?

<details>
<summary>풀이 방향</summary>

1. `new StockService(0).order(1)`은 false이며 재고는 0이어야 한다.
2. 재고가 10이면 false이고 재고는 10이어야 한다. 입력 범위를 넘는 계산 전에 검사해야 한다.
3. `total -> total.add(BigDecimal.ONE)` 정책을 주입하고 `IllegalStateException`을 기대한다.
4. 둘은 “주문이 규칙대로 처리된다”는 하나의 행동을 확인하는 서로 관련된 검증이다.

</details>

공식 참고: [JUnit 5 사용자 가이드](https://docs.junit.org/5.13.4/user-guide/) — 링크의 가이드는 5.13.4이며 이 예제는 5.10.1에 존재하는 기능만 사용한다.

[목차로 돌아가기](README.md)
