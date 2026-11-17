# Part 29. 요약 — Java 치트시트



코드는 개념별 조각이며 필요한 import·실행 클래스는 생략할 수 있다. 실행 예제는 **JDK 17 이상** 기준이며, 아래의 [실행 예제 연결](#실행-예제-연결)에서 전체 소스와 예상 출력을 확인한다. 복습 후 [Part 30 연습문제](part30_연습문제.md)에 적용해 보자.

---

## 🟢 기본 문법

```java
// 출력
System.out.println("hello");
System.out.printf("%s은 %d살%n", name, age);

// 입력
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
sc.nextLine(); // 정수 뒤의 줄바꿈 소비
String s = sc.nextLine();

// 변수
int x = 10;
double pi = 3.14;
String name = "민수";
boolean flag = true;
final int MAX = 100;  // 상수

// 형변환
int n = Integer.parseInt("100");
String s = String.valueOf(42);
double d = (double) n;
```

---

## 🟢 제어문

```java
// 조건문
if (조건) { } else if (조건) { } else { }

// switch
switch (값) {
    case A -> 결과A;
    case B -> 결과B;
    default -> 기본값;
};

// 반복문
for (int i = 0; i < n; i++) { }
for (int x : arr) { }   // for-each
while (조건) { }
```

---

## 🟢 배열 & 문자열

```java
int[] arr = {1, 2, 3};
arr.length;                    // 배열 길이

String s = "hello";
s.length();                    // 문자열 길이
s.charAt(0);                   // 'h'
s.substring(0, 3);             // "hel"
s.equals("hello");             // 비교 (== 쓰지 말 것!)
s.split(",");                  // 쪼개기
```

---

## 🔵 객체지향

```java
// 클래스
public class Dog {
    private String name;
    Dog(String name) { this.name = name; }
    public String getName() { return name; }
}

// 상속
class Puppy extends Dog {
    Puppy(String name) { super(name); }
}

// 인터페이스
interface Flyable { void fly(); }
class Bird implements Flyable { public void fly() { } }

// 추상 클래스
abstract class Shape { abstract double area(); }
```

`Dog`에 매개변수 없는 생성자가 없으므로 `Puppy`는 `super(name)`으로 부모 생성자를 호출해야 한다. 상속과 다형성은 [Part 8](part8_상속과다형성.md), 도형 구현은 [Part 30 문제 7](part30_연습문제.md#문제-7-도형-넓이-다형성)과 연결된다.

---

## 🔵 컬렉션

```java
List<String> list = new ArrayList<>();
list.add("a"); list.get(0); list.size(); list.remove(0);

Map<String, Integer> map = new HashMap<>();
map.put("key", 1); map.get("key"); map.containsKey("key");

Set<String> set = new HashSet<>();
set.add("a"); set.contains("a"); set.size();
```

[Part 10](part10_컬렉션.md)의 선택 기준:

| 요구사항 | 먼저 고려할 구현 | 비용·주의점 |
|---|---|---|
| 인덱스로 읽는 목록 | `ArrayList` | 조회 O(1), 끝 추가 분할상환 O(1), 중간 삽입·삭제 O(n) |
| 키 조회 / 중복 제거 | `HashMap` / `HashSet` | 적절한 해시 분포에서 기대 O(1), 순서 보장 없음 |
| 삽입 순서 유지 | `LinkedHashMap` / `LinkedHashSet` | 키·요소 정렬과는 다름 |
| 정렬 상태 유지 | `TreeMap` / `TreeSet` | 조회·추가·삭제 O(log n), 비교 결과 0이면 같은 키·요소 |
| FIFO 큐 / LIFO 스택 | `ArrayDeque` | 양끝 연산 분할상환 O(1), null 불가 |
| 우선순위 처리 | `PriorityQueue` | `offer`·`poll` 보통 O(log n), `peek` O(1) |

`LinkedList`도 인덱스로 위치를 찾으면 O(n)이므로 중간 삽입이 무조건 빠르지 않다. `PriorityQueue`는 반복해서 `poll()`해야 우선순위 순서로 나온다. for-each 순회는 정렬을 보장하지 않는다.

```java
Queue<String> waiting = new ArrayDeque<>();
waiting.offer("order-1");
waiting.poll(); // order-1, 빈 큐라면 null

Deque<String> undo = new ArrayDeque<>();
undo.push("상품 추가");
undo.pop(); // 상품 추가, 빈 덱이라면 예외

List<Integer> values = new ArrayList<>(List.of(1, 2, 3));
values.remove(1);                  // 인덱스 1의 값 2 제거
values.remove(Integer.valueOf(1)); // 값 1 제거
values.removeIf(v -> v % 2 == 0);  // 안전하게 조건 삭제
```

- `Comparable`은 타입의 기본 순서, `Comparator`는 외부 정렬 기준이다. 동률이면 `thenComparing()`으로 기준을 추가한다. 정수 비교는 뺄셈 대신 `Integer.compare()` 등을 쓴다.
- `List.of()`는 수정 불가, `Arrays.asList()`는 크기 고정이다. 변경할 목록은 `new ArrayList<>(...)`로 만든다.
- `HashMap` 키·`HashSet` 요소는 `equals()`·`hashCode()`를 일관되게 구현하고 비교 필드를 저장 후 변경하지 않는다.
- 출력 순서가 필요하면 삽입 순서 또는 정렬 순서를 명시한다. [문제 8](part30_연습문제.md#문제-8-단어-빈도수)은 `HashMap` 집계 후 `TreeMap`으로 출력한다.

---

## 🔵 예외 처리

```java
try {
    // 위험한 코드
} catch (Exception e) {
    e.printStackTrace();
} finally {
    // 정리 코드
}
```

---

## 🔵 람다 & 스트림

```java
list.stream()
    .filter(x -> x > 0)
    .map(x -> x * 2)
    .sorted()
    .collect(Collectors.toList());
```

---

## 🔵 Optional

```java
Optional<String> opt = Optional.ofNullable(value);
opt.orElse("기본값");
opt.ifPresent(v -> System.out.println(v));
opt.map(String::toUpperCase);
```

---

## 🔵 동시성과 작업 종료 — Part 16

[Part 16](part16_멀티스레드기초.md)에서 동시성은 작업 진행을 겹쳐 관리하는 것, 병렬성은 실제로 동시에 계산하는 것이다. 실행 순서는 고정되지 않는다.

| 도구 | 용도 | 한계 |
|---|---|---|
| `volatile` | 변경 가시성과 관련 순서 보장 | `count++` 전체의 원자성은 보장하지 않음 |
| `synchronized` | 같은 잠금의 상호 배제·가시성 | 공유 상태의 읽기·수정에 같은 잠금이 필요 |
| `AtomicInteger` | 증가 등 개별 원자 연산 | 여러 상태를 묶은 업무 처리까지 보호하지 않음 |
| `ConcurrentHashMap` | 스레드 안전한 맵 연산 | `get()` 후 `put()` 두 호출은 하나의 원자 연산이 아님 |

```java
AtomicInteger completed = new AtomicInteger();
completed.incrementAndGet();
ConcurrentHashMap<String, Integer> orders = new ConcurrentHashMap<>();
orders.merge("P001", 1, Integer::sum); // 단일 키의 원자적 갱신
```

- `submit()`의 `Future.get()`으로 완료·실패를 확인한다. 제한 시간 초과는 **기다리기를 중단한 것**이며 작업 자동 취소가 아니다.
- `CompletableFuture`의 `thenApply()`는 값 변환, `thenCompose()`는 다음 비동기 작업 연결, `thenCombine()`은 두 결과 조합이다. `thenApply()`가 반드시 다른 스레드에서 실행되지는 않는다.
- `shutdown()` → `awaitTermination()` → 필요 시 `shutdownNow()`로 종료를 관리한다. `shutdownNow()`·`cancel(true)`도 강제 종료 보장은 아니다.
- `InterruptedException`은 전파하거나 인터럽트 상태를 복구하고 종료한다. `CompletableFuture.cancel(true)`는 작업 스레드 인터럽트를 보장하지 않는다.
- `sleep()`으로 완료를 추측하지 말고 `Future`, `CountDownLatch` 등으로 확인한다. 풀의 작업 대기열 크기·거절 정책도 고려한다.

재고 확인과 차감 전체는 하나의 임계 구역으로 묶어야 한다. [StockService.java](examples/src/main/java/tutorial/part24/StockService.java)는 같은 인스턴스의 `synchronized` 메소드로 이를 보호한다.

---

## 🟠 테스트 — Part 24

[Part 24](part24_테스트.md)의 핵심은 **정상 결과·실패 시 상태·경계값**을 독립적으로 검증하는 것이다.

```java
@Test
void 초과_주문은_재고를_유지한다() {
    StockService service = new StockService(10); // Given
    boolean success = service.order(11);        // When
    assertFalse(success);                       // Then
    assertEquals(10, service.getStock());
}
```

- `assertEquals()`는 값 비교, `assertSame()`은 참조 동일성, `assertThrows()`는 예외를 확인한다. 한 업무 규칙에 관련된 여러 assert는 괜찮다.
- `@ParameterizedTest` + `@ValueSource`·`@CsvSource`로 입력과 경계값을 바꾼다. 매 테스트는 새 상태에서 시작하고 순서에 의존하지 않는다.
- 단위 테스트는 제한된 규칙을, 통합 테스트는 실제로 연결한 구성 요소의 협력을 확인한다. Stub은 정해진 응답, Fake는 단순한 실제 동작, Mock은 기대 호출 검증에 사용한다.
- 시간은 `Clock.fixed()`로 고정하고 동시 작업은 시작 조율과 완료 대기를 분리한다. 시간 제한과 자원 정리도 필요하다.
- 동시성 테스트가 통과해도 모든 실행 순서에서 안전하다는 증명은 아니다. 잠금과 공유 상태 설계도 검토한다.

---

## 🌐 소켓 프로그래밍 — Part 31

[Part 31](part31_소켓프로그래밍과통신.md)은 TCP/UDP 통신과 메시지 규칙을 다룬다.

| 항목 | 핵심 |
|---|---|
| TCP | 순서 있는 바이트 스트림. 메시지 경계는 직접 정해야 함 |
| UDP | 데이터그램 단위. 전달·순서를 보장하지 않으며 수신 버퍼보다 크면 잘릴 수 있음 |
| `ServerSocket` | 연결 대기와 수락, `accept()`로 통신용 `Socket` 반환 |
| `Socket` | 특정 상대와 입력·출력 스트림으로 통신 |

- 에코 예제의 규칙은 **UTF-8 + 한 줄 한 메시지**다. `write()` → `newLine()` → `flush()`로 보내고 `readLine()`으로 받는다. `QUIT`에는 `BYE`를 응답한다.
- `readLine()`이 `null`이면 더 읽을 데이터 없이 EOF를 만난 것이다. 줄바꿈 없이 연결을 유지하면 계속 기다릴 수 있다. `flush()`는 상대의 업무 처리 완료 확인이 아니다.
- 연결 시간 제한과 `setSoTimeout()`의 읽기 시간 제한은 다르다. 읽기 제한은 쓰기나 메시지 전체 처리 기한이 아니다. `try-with-resources`로 자원을 닫는다.
- 멀티클라이언트 서버는 연결별 소켓과 공유 업무 상태를 구분한다. 동시 처리 수·대기열·메시지 길이를 제한하고 종료 시 활성 연결도 정리한다.
- UDP는 인코딩한 `byte[]`의 길이로 보내고 `packet.getLength()`만큼 해석한다. 패킷을 재사용하면 수신 전 길이를 복원한다.
- TCP 전송 성공이 주문 완료를 보장하지 않는다. 변경 요청 재시도에는 요청 ID와 중복 처리 정책이 필요하다. 이 에코 예제는 HTTP·WebSocket 서버가 아니다.

---

## 🧩 Object와 객체 비교 — Part 32

[Part 32](part32_Object와객체비교.md)의 상품은 **ID가 같으면 동등하다**는 정책을 사용한다.

| 비교 | 의미 |
|---|---|
| 참조형 `==` | 같은 객체를 가리키는가? |
| `equals()` | 정한 기준으로 값이 같은가? 기본 `Object.equals()`는 동일성 비교 |
| `Arrays.equals()` | 배열 내용 비교. 중첩 배열은 `deepEquals()`도 검토 |

`equals(Object)`는 반사성·대칭성·추이성·일관성을 지키고 null 비교는 false여야 한다. 동등한 객체는 같은 `hashCode()`를 반환해야 하지만, 같은 해시값이라고 동등한 것은 아니다. 해시값은 고유 ID가 아니다.

`@Override`로 메소드 서명을 확인하고 비교 필드는 변경 불가로 둔다. `Objects.equals()`는 null을 고려한 비교에 유용하다. `toString()`은 디버깅 표현이며 저장·통신 규격을 대신하지 않는다.

---

## 🧩 불변 객체와 복사 — Part 33

[Part 33](part33_불변객체와객체복사.md)은 상품 가격 변경과 주문 당시 스냅샷을 구분한다.

| 방식 | 복사·공유 범위 |
|---|---|
| `b = a` | 같은 객체를 참조 |
| `new ArrayList<>(source)` | 바깥 리스트만 복사, 요소 참조 공유 |
| `Collections.unmodifiableList(source)` | 원본을 보는 수정 불가 뷰. 원본 변경은 보임 |
| `List.copyOf(source)` | 목록 구조의 수정 불가 스냅샷. 가변 요소는 공유, null 불가 |
| 요소까지 필요한 만큼 복사 | 복사한 내부 가변 상태도 분리 |

`final`은 참조 재대입을 막을 뿐 내부 상태를 고정하지 않는다. record도 가변 리스트를 가지면 전체가 불변은 아니다. 생성자에서 방어적으로 복사하고 반환 경로에서도 가변 참조를 노출하지 않는다. 주문 스냅샷은 **수정 불가 목록 + 불변 OrderLine 요소**를 함께 사용한다.

---

## 🧩 날짜·시간과 금액 — Part 34

[Part 34](part34_날짜시간과금액처리.md)의 타입 선택과 경계 정책:

| 목적 | 타입·규칙 |
|---|---|
| 날짜 / 시각 / 시간대 없는 날짜·시각 | `LocalDate` / `LocalTime` / `LocalDateTime` |
| 발생 시점 / 지역 표시 | `Instant` / `ZonedDateTime` + `ZoneId` |
| 경과 시간 / 달력 기간 | `Duration` / `Period` |
| 테스트에서 현재 시각 고정 | `Clock.fixed()` 주입 |
| 십진 금액 | `BigDecimal`, 통화·scale·반올림 위치를 명시 |

```java
Instant orderedAt = Instant.parse("2026-11-17T03:00:00Z");
String display = orderedAt.atZone(ZoneId.of("Asia/Seoul"))
        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")); // 2026-11-17 12:00
BigDecimal total = new BigDecimal("19.99").multiply(BigDecimal.valueOf(3))
        .multiply(new BigDecimal("0.90"))
        .setScale(2, RoundingMode.HALF_UP); // 53.97
```

- 취소 기한은 주문 시점부터 정확히 30분까지 포함한다. `Duration`을 직접 비교하고 음수 경과 시간은 거부한다. `toMinutes()`로 잘라 비교하면 30분 59초도 허용할 수 있다.
- `MM`은 월, `mm`은 분이다. 연도 `uuuu`와 주 기준 연도 `YYYY`를 혼동하지 않는다. 달력의 하루와 24시간은 시간대 규칙에 따라 다를 수 있다.
- `new BigDecimal(0.1)` 대신 문자열로 십진 값을 만든다. `2.0`과 `2.00`은 `equals()`가 false, `compareTo()`는 0이다. 나눗셈에는 필요한 반올림 규칙을 정한다.
- 날짜·시간과 `BigDecimal`은 불변이므로 계산의 반환값을 사용한다. 소수 두 자리 정책은 이 예제의 선택이며 모든 통화의 규칙은 아니다.

---

## 🌐 HTTP 클라이언트와 JSON — Part 35

[Part 35](part35_HTTP클라이언트와JSON.md)는 JDK `HttpClient`와 외부 라이브러리 Jackson 2.19.1을 사용한다.

1. URI·메소드·헤더, 연결·요청 시간 제한을 설정한다.
2. 응답 상태를 검사한다. 예제는 2xx만 허용하며 404·500은 오류다.
3. `Content-Type`을 검사한다. 예제는 `application/json`을 요구한다.
4. JSON을 DTO로 변환하고 필수 필드·null·음수 가격 등을 검증한다.

```java
ProductData product = JsonCodec.readProduct(json);
String encoded = JsonCodec.writeProduct(product);
boolean same = product.equals(JsonCodec.readProduct(encoded));
```

JSON을 `split()`이나 문자열 덧셈으로 처리하지 않는다. 필드 순서 대신 변환한 값을 비교한다. 현재 예제는 알 수 없는 필드를 거부한다. HTTP 200이어도 잘못된 JSON은 실패해야 한다.

`Accept`는 받을 형식, `Content-Type`은 보내는 본문 형식이다. 비동기 `sendAsync()`에도 같은 검증이 필요하다. `InterruptedException`은 전파하거나 상태를 복구하고 종료한다. 요청 시간 초과가 서버의 미처리를 뜻하지 않으므로 변경 요청을 무조건 재시도하지 않는다.

---

## 🧩 객체지향 설계와 리팩토링 — Part 36

[Part 36](part36_객체지향설계와리팩토링.md)은 상품 조회와 할인 정책을 조합하는 **견적 계산** 예제다.

| 책임 | 기존 코드 |
|---|---|
| 조회 계약 / 구현 | `ProductRepository` / `InMemoryProductRepository` |
| 할인 정책 | `DiscountPolicy` — 반환값은 할인액이 아니라 할인 후 금액 |
| 검증·견적 흐름 | `OrderService.quote()` |
| 결과 / 객체 조립·출력 | `OrderReceipt` / `RefactoringExample` |

생성자로 저장소와 정책을 주입하고 상속 대신 합성으로 할인 규칙을 교체한다. DI는 객체 전달·조립 방식이고, 의존성 역전은 업무 정책이 구체 구현에 묶이지 않도록 하는 원칙이다.

SOLID는 변경 이유 분리(SRP), 정책 확장(OCP), 대체 가능한 계약(LSP), 필요한 기능만 의존(ISP), 추상 계약에 의존(DIP)을 점검하는 질문으로 사용한다. 작은 코드까지 무조건 인터페이스로 나누지는 않는다.

리팩토링은 관찰 가능한 동작을 유지하며 구조를 바꾼다. 현재 동작을 테스트로 고정 → 책임 추출 → 의존성 주입 → 단계마다 테스트 순으로 진행한다. 이 예제에는 재고 차감·결제·DB 저장이 없으며, 책임 분리만으로 트랜잭션이 생기지는 않는다.

---

## 실행 예제 연결

[실행 안내](examples/README.md)에 준비 방법과 검증 범위가 있다. JDK 예제는 **저장소 루트**에서 실행한다.

```bash
bash examples/run-core.sh 10
bash examples/run-core.sh all
```

| 범위 | 전체 실행 코드 | 예상 출력 |
|---|---|---|
| Part 10 | [CollectionsExample.java](examples/src/main/java/tutorial/part10/CollectionsExample.java) | [part10.txt](examples/expected/part10.txt) |
| Part 16 | [ConcurrencyExample.java](examples/src/main/java/tutorial/part16/ConcurrencyExample.java), [CancellationExample.java](examples/src/main/java/tutorial/part16/CancellationExample.java) | [part16.txt](examples/expected/part16.txt), [cancellation.txt](examples/expected/cancellation.txt) |
| Part 24 | [StockExample.java](examples/src/main/java/tutorial/part24/StockExample.java), [StockServiceTest.java](examples/src/test/java/tutorial/part24/StockServiceTest.java) | [part24.txt](examples/expected/part24.txt) |
| Part 31 | [본문 31.4·31.5·31.7의 전체 소스와 실행법](part31_소켓프로그래밍과통신.md) | 같은 문서의 TCP·UDP 실행 결과 |
| Part 32 | [EqualityExample.java](examples/src/main/java/tutorial/part32/EqualityExample.java) | [part32.txt](examples/expected/part32.txt) |
| Part 33 | [ImmutabilityExample.java](examples/src/main/java/tutorial/part33/ImmutabilityExample.java) | [part33.txt](examples/expected/part33.txt) |
| Part 34 | [DateMoneyExample.java](examples/src/main/java/tutorial/part34/DateMoneyExample.java) | [part34.txt](examples/expected/part34.txt) |
| Part 35 | [JsonExample.java](examples/src/main/java/tutorial/part35/JsonExample.java), [HttpJsonExample.java](examples/src/main/java/tutorial/part35/HttpJsonExample.java) | [part35.txt](examples/expected/part35.txt), [part35-http.txt](examples/expected/part35-http.txt) |
| Part 36 | [RefactoringExample.java](examples/src/main/java/tutorial/part36/RefactoringExample.java) | [part36.txt](examples/expected/part36.txt) |

Part 31은 문서의 코드를 별도 Java 파일에 저장해 실행한다. `run-core.sh`의 `31` 실행 항목은 없다. Part 35와 JUnit은 [Gradle 설정](examples/build.gradle)의 의존성이 필요하며 **Gradle 9.x**를 사용한다. 이 프로젝트에는 Wrapper가 없으므로 `gradle` 명령을 쓴다.

```bash
cd examples
gradle -q runPart35
gradle test
# 로컬 HTTP 테스트까지 포함
gradle test -PnetworkTests --rerun-tasks
```

HTTP 서버·클라이언트의 두 터미널 실행법은 [실행 안내의 HTTP 절](examples/README.md#4-http-통신은-터미널-두-개에서-실행)을 따른다. 기존 예제를 바꾸지 않은 기준으로 기본 테스트는 36개, HTTP 테스트 포함 시 38개다. Part 30에서 테스트를 추가하면 실행 수는 늘어난다.

---

## 🟠 자주 쓰는 어노테이션

| 어노테이션 | 용도 |
|---|---|
| `@Override` | 메소드 재정의 |
| `@Deprecated` | 사용 중단 표시 |
| `@RestController` | REST API 컨트롤러 |
| `@Service` | 서비스 계층 |
| `@Autowired` | 의존성 주입 |
| `@Test` | 테스트 메소드 |

---

## ⚠️ 절대 잊지 말 것

1. 세미콜론 `;` 필수
2. 문자열 비교는 `.equals()`
3. 배열 인덱스 0부터
4. `Scanner` → `import java.util.Scanner;`
5. `null` 체크 습관화
6. `System.out.println` 대신 Logger
7. SQL은 반드시 `PreparedStatement`

[목차](README.md) · [다음: 연습문제 1~20](part30_연습문제.md)
