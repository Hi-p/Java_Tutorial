# Part 36. 객체지향 설계와 리팩토링

> 상품 조회·할인·견적 계산을 예로 들어 **변경 이유에 따라 책임을 나누고, 테스트로 동작을 확인하며 구조를 개선**한다.

선행 학습: [Part 9 인터페이스](part9_인터페이스와추상클래스.md), [Part 19 패턴](part19_디자인패턴기초.md), [Part 24 테스트](part24_테스트.md), [Part 34 금액](part34_날짜시간과금액처리.md)

이 파트는 주문 **견적 계산** 예제다. 재고 차감·결제·DB 저장까지 구현한 주문 시스템은 아니다.

---

## 36.1 언제 구조를 나눌까?

하나의 메소드가 입력 받기, DB 조회, 할인 계산, 출력까지 모두 처리하면 서로 다른 이유로 수정하게 된다.

```java
// 구조를 설명하기 위한 개념 코드
void order() {
    // 1. 키보드 입력
    // 2. DB 연결과 상품 조회
    // 3. 할인 조건 분기
    // 4. 금액 계산
    // 5. 콘솔 출력
}
```

처음부터 모든 클래스를 잘게 쪼갤 필요는 없다. **다른 방식으로 바꿔야 하는 부분, 독립적으로 검증할 부분**이 드러날 때 경계를 잡는다.

| 책임 | 예제의 클래스·인터페이스 | 변경 이유 |
|---|---|---|
| 상품 값 표현 | `Product` | 상품 데이터 규칙 |
| 상품 조회 계약 | `ProductRepository` | 조회 기능의 계약 |
| 메모리 저장소 | `InMemoryProductRepository` | 저장·조회 구현 |
| 할인 규칙 | `DiscountPolicy` | 할인 정책 |
| 견적 계산 흐름 | `OrderService` | 수량 검증·조회·할인·반올림 순서 |
| 계산 결과 | `OrderReceipt` | 반환할 데이터 |
| 객체 조립과 출력 | `RefactoringExample` | 실행 환경과 표시 방식 |

전체 소스: [part36 폴더](examples/src/main/java/tutorial/part36)

---

## 36.2 캡슐화는 getter/setter 생성으로 끝나지 않는다

상태를 private으로 두어도 아무 setter로 잘못된 값을 넣을 수 있다면 규칙을 보호하기 어렵다. 생성 시 값 검증, 목적이 명확한 메소드, 변경 가능한 경로 제한을 함께 고려한다.

`OrderService.quote()`는 수량이 양수인지 먼저 검사한다. 없는 상품은 명확한 예외로 처리하고, 할인 결과가 음수이거나 원래 소계를 초과하면 거부한다. 규칙이 객체 안에 모이면 호출자가 매번 같은 검사를 반복할 필요가 줄어든다.

---

## 36.3 상속과 합성

- **상속:** 기존 타입의 하위 타입으로 동작한다. 부모 타입이 기대하는 계약을 지켜야 한다.
- **합성:** 다른 객체를 필드로 가지고 필요한 일을 위임한다.

할인 방식마다 `OrderService`를 상속한 클래스를 만드는 대신, 할인 정책 객체를 받아 교체할 수 있다.

```java
@FunctionalInterface
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal);
}
```

```java
OrderService regular = new OrderService(repository, DiscountPolicy.none());
OrderService sale = new OrderService(repository,
        DiscountPolicy.percentage(new BigDecimal("0.10")));
```

합성은 정책을 바꾸기 쉽지만 계약과 객체 조립 코드가 추가된다. 변경 가능성이 없는 작은 코드까지 무조건 인터페이스로 감싸지는 않는다.

---

## 36.4 생성자 주입과 의존성 역전

```java
public OrderService(ProductRepository repository, DiscountPolicy discount) {
    this.repository = Objects.requireNonNull(repository);
    this.discount = Objects.requireNonNull(discount);
}
```

서비스 내부에서 저장소 구현을 직접 생성하지 않고 필요한 객체를 외부에서 전달한다. 이것이 **생성자 기반 의존성 주입(DI)**이다. Spring 없이도 구현할 수 있다.

서비스는 특정 메모리 저장소 대신 `ProductRepository` 계약에 의존한다. 실행 프로그램은 실제 구현을 연결하고, 단위 테스트는 정해진 응답을 주는 stub을 연결한다.

```java
ProductRepository stub = id -> Optional.of(product);
OrderService service = new OrderService(stub, DiscountPolicy.none());
```

DI와 의존성 역전은 관련되지만 같은 개념은 아니다. DI는 객체를 전달·조립하는 방식이고, 의존성 역전은 상위 정책이 하위 구현 세부사항에 묶이지 않도록 의존 방향을 설계하는 원칙이다.

---

## 36.5 SOLID를 판단 질문으로 사용하기

| 원칙 | 이 예제에서 던질 질문 |
|---|---|
| 단일 책임(SRP) | 할인 변경 때문에 저장소 코드도 수정하는가? |
| 개방·폐쇄(OCP) | 새 할인 정책을 기존 계산 흐름을 덜 건드리고 추가할 수 있는가? |
| 리스코프 치환(LSP) | 저장소 구현을 바꿔도 호출자가 기대한 조회 계약을 지키는가? |
| 인터페이스 분리(ISP) | 조회만 필요한 코드가 불필요한 저장·삭제 기능에도 의존하는가? |
| 의존성 역전(DIP) | 업무 흐름이 구체적인 저장 방식에 직접 묶여 있는가? |

약어를 외우는 것보다 실제 변경 하나를 적용해 보고 수정 범위와 테스트 난이도를 확인한다. 코드 줄 수가 늘었다고 설계가 좋아진 것은 아니다.

---

## 36.6 리팩토링 순서

1. 현재 기대하는 입력·출력·실패 동작을 테스트로 고정한다.
2. 긴 메소드에서 의미 있는 계산이나 검증을 추출한다.
3. 저장소·정책처럼 바꾸거나 대체할 경계를 분리한다.
4. 생성자로 의존성을 전달한다.
5. 단계마다 테스트를 실행한다.

리팩토링은 외부에서 관찰하는 동작을 유지하면서 내부 구조를 바꾸는 작업이다. 동작도 바꾸려면 변경할 요구사항과 테스트를 별도로 명시한다.

이 문서의 첫 `order()`는 문제 상황을 설명하는 개념 코드다. 실행 소스는 리팩토링 후 구조를 보여 주며, 완성된 기존 프로그램을 자동 변환한 결과라는 뜻은 아니다.

---

## 36.7 실행과 예상 결과

실행 진입점: [RefactoringExample.java](examples/src/main/java/tutorial/part36/RefactoringExample.java)

```bash
bash examples/run-core.sh 36
```

```text
정상 견적: 3600.00
10% 할인 견적: 3240.00
상품 조회: 노트
```

같은 서비스 코드를 사용하고 정책만 바꿔 견적이 달라진다. 상품 조회를 DB로 바꿀 경우 새 저장소 구현을 연결할 수 있지만, DB 연결·트랜잭션·실패 정책은 추가로 설계해야 한다.

```bash
cd examples
gradle test --tests 'tutorial.part36.*'
```

[단위 테스트](examples/src/test/java/tutorial/part36/OrderServiceTest.java)는 stub으로 업무 규칙을 확인한다. [통합 테스트](examples/src/test/java/tutorial/part36/OrderIntegrationTest.java)는 실제 메모리 저장소와 정책을 조합한다.

---

## 36.8 연습문제

1. 5,000 이상 주문이면 500을 할인하는 정책을 추가하자.
2. 없는 상품 조회 시 호출자가 처리할 예외 정책을 설명하자.
3. 화면 출력 형식을 바꿀 때 어느 클래스를 수정해야 할까?
4. 실제 주문 저장·재고 차감까지 추가한다면 무엇을 하나의 원자적 처리로 묶어야 할까?

<details>
<summary>풀이 방향</summary>

1. `DiscountPolicy`를 구현하거나 람다를 전달한다. 4,999.99와 5,000.00 경계를 테스트한다.
2. 예제는 `NoSuchElementException`을 사용한다. 서비스 규격에 맞춰 전용 예외·응답 변환 등을 정할 수 있다.
3. 이 예제에서는 `RefactoringExample`의 출력 부분이다. 업무 서비스에 콘솔 출력 의존성을 넣지 않는다.
4. 재고 확인·차감과 주문 저장의 정합성, 실패 시 복구, 중복 요청 정책을 함께 설계한다. 단순 인터페이스 분리만으로 트랜잭션이 생기는 것은 아니다.

</details>

연결 학습: [Part 19 디자인 패턴](part19_디자인패턴기초.md)의 Strategy와 [Part 22 Spring Boot](part22_SpringBoot입문.md)의 DI를 이번 예제와 비교해 보자.

[목차로 돌아가기](README.md)
