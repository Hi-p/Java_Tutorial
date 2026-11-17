# Part 30. 연습문제

> 파트별 종합 연습문제. 직접 풀어보고 해설과 비교하자.

문제 1~10은 기존 번호를 유지하고, 11~20은 Part 10·16·24 보강과 Part 31~36을 연결한다. 먼저 [Part 29 요약](part29_요약.md)과 [기존 실행 예제 안내](examples/README.md)를 참고한다.

코드 조각은 **JDK 17 이상** 기준이다. import는 파일 맨 위, 실행문은 `main()` 안, 일반 클래스·메소드는 해당 선언 위치에 둔다. 필요한 경우 `main()`에 `throws Exception`을 선언하는 학습용 실행 방식을 사용한다. JUnit 해설은 `examples/src/test/java`에 별도 테스트 클래스로 작성할 수 있다. 기존 예제의 출력과 **이번 문제로 확장한 출력**은 구분한다.

---

## 🟢 초급

### 문제 1. 계산기
두 정수와 연산자(`+`, `-`, `*`, `/`)를 입력받아 결과를 출력하세요.

```
입력: 10 + 3
출력: 13
```

### 문제 2. 별 찍기
정수 n을 입력받아 직각삼각형 모양으로 별을 출력하세요.

```
입력: 4
출력:
*
**
***
****
```

### 문제 3. 최대공약수
두 정수의 최대공약수(GCD)를 구하세요. (유클리드 호제법)

### 문제 4. 회문 검사
문자열을 입력받아 회문(앞뒤 같은 단어)인지 판별하세요.

```
입력: "level"
출력: true
```

### 문제 5. 배열 정렬
정수 배열을 입력받아 오름차순으로 정렬하여 출력하세요. (직접 구현)

---

## 🔵 심화

### 문제 6. 학생 관리
`Student` 클래스를 만들고, `ArrayList<Student>`에 학생을 추가/삭제/조회하는 프로그램을 작성하세요.
- 필드: 이름, 학번, 점수
- 메소드: 전체 출력, 평균 계산, 최고 점수 학생 찾기

### 문제 7. 도형 넓이 (다형성)
`Shape` 추상클래스를 상속하는 `Circle`, `Rectangle`, `Triangle`을 만들고, 배열에 넣어 반복문으로 각 넓이를 출력하세요.

반지름 5인 원, 가로 3·세로 4인 직사각형, 밑변 3·높이 4인 삼각형을 사용하고 소수 둘째 자리까지 출력한다.

```text
78.54
12.00
6.00
```

### 문제 8. 단어 빈도수
문장을 입력받아 각 단어의 등장 횟수를 `HashMap`으로 세어 출력하세요.

공백·탭으로 단어를 구분하고 앞뒤 공백과 빈 입력은 무시한다. 대소문자·구두점은 그대로 구별한다. `HashMap`은 출력 순서를 보장하지 않으므로, 아래처럼 **키 오름차순**으로 출력하려면 집계 후 `TreeMap`으로 옮긴다.

```
입력: "the cat sat on the mat the cat"
출력: {cat=2, mat=1, on=1, sat=1, the=3}
빈 입력 또는 공백뿐인 입력: {}
```

### 문제 9. 스트림 활용
정수 리스트에서 짝수만 골라 제곱한 뒤 내림차순 정렬하여 출력하세요. (Stream API)

### 문제 10. 파일 단어 수 세기
텍스트 파일을 읽어서 총 단어 수, 줄 수, 문자 수를 출력하세요.

- UTF-8 파일을 읽고 공백·탭으로 단어를 구분한다. 빈 줄과 공백뿐인 줄의 단어 수는 0이다.
- 빈 줄도 줄 수에는 포함한다. 파일 끝의 줄바꿈 자체는 추가 빈 줄로 세지 않는다.
- 문자 수는 각 줄의 `String.length()` 합으로 정한다. 공백은 포함하고 줄바꿈은 제외한다. 이는 UTF-16 코드 단위 수이며 이모지 등의 사용자 인식 문자 수와 다를 수 있다.

예를 들어 파일 내용이 Java 문자열로 `"hello world\n\n   \njava\n"`라면 다음과 같다. 세 번째 줄은 공백 3개다.

```text
줄: 4, 단어: 3, 문자: 18
```

빈 파일의 예상 결과는 `줄: 0, 단어: 0, 문자: 0`이다.

---

## 🧩 보강·확장 종합 문제

### 문제 11. 컬렉션 선택과 주문 우선순위

**요구사항**

1. `(긴급도, 접수 번호, 상품 ID)`가 `(2, 1, P003)`, `(1, 3, P002)`, `(1, 2, P001)`인 요청을 저장한다.
2. 긴급도 숫자가 작을수록 먼저, 같으면 접수 번호가 작은 요청을 먼저 꺼낸다. `PriorityQueue`와 `Comparator`를 사용한다.
3. 별도 주문 목록 `P002, P001, P002`를 `HashMap.merge()`로 집계하고 상품 ID순으로 출력한다.
4. FIFO라면 어떤 컬렉션을 쓸지, 우선순위 큐의 for-each가 왜 정답 출력 방법이 아닌지 설명한다.

**예상 결과**

```text
처리 순서: [P001, P002, P003]
상품별 횟수: {P001=1, P002=2}
```

**관련 예제:** [Part 10](part10_컬렉션.md), [CollectionsExample.java](examples/src/main/java/tutorial/part10/CollectionsExample.java). 기존 예제의 큐·정렬·맵 선택을 조합한 확장이다.

### 문제 12. 동시 주문 횟수와 비동기 금액 계산

**요구사항**

1. 스레드 풀 4개에 작업 4개를 제출한다. 각 작업은 `P001` 주문 횟수와 전체 완료 횟수를 각각 1,000번 증가시킨다.
2. `ConcurrentHashMap.merge()`와 `AtomicInteger`를 사용하고 모든 `Future`의 완료 후 출력한다.
3. `Future`로 `1200 * 3`을 계산하고, `CompletableFuture.thenApply()`로 600을 할인한다.
4. `sleep()`에 의존하지 않고 대기에 시간 제한을 둔다. 성공·실패와 무관하게 풀을 종료한다.

**예상 결과**

```text
완료 작업: 4000
P001 집계: 4000
Future 합계: 3600
비동기 할인 합계: 3000
```

**관련 예제:** [Part 16](part16_멀티스레드기초.md), [ConcurrencyExample.java](examples/src/main/java/tutorial/part16/ConcurrencyExample.java). 기존 예제는 총 1,000회이며 이 문제는 4,000회로 확장한다.

### 문제 13. 시간 초과와 협력적 작업 취소

**요구사항**

1. `CountDownLatch`에서 대기하는 작업을 제출하고 별도 신호로 작업 시작을 확인한다.
2. `get(50, MILLISECONDS)`의 `TimeoutException`을 확인한다. 아직 취소하지 않은 작업은 계속 대기해야 한다.
3. `cancel(true)`를 요청하고 작업은 인터럽트에 반응해 종료한다. `finally`에서 종료 신호를 보낸다.
4. 취소 상태와 작업 본문의 종료 신호를 각각 확인하고 풀을 정리한다. `CompletableFuture.cancel(true)`에도 같은 인터럽트 동작을 기대할 수 있는지 설명한다.

**예상 결과**

```text
대기 시간 초과
취소 전 작업 미완료: true
취소 요청 수락: true
작업 종료 확인: true
Future 취소 상태: true
```

**관련 예제:** [CancellationExample.java](examples/src/main/java/tutorial/part16/CancellationExample.java). 기존 취소 예제에 결과 대기 시간 초과를 추가한다.

### 문제 14. 재고의 경계값과 동시 주문 테스트

**요구사항**

1. 기존 `StockService`를 대상으로 JUnit 5 테스트를 작성한다. 각 입력은 새 재고 10개에서 시작한다.
2. 수량 -1·0·1·10·11과 초기 재고 0을 검사하고 반환값 또는 예외뿐 아니라 남은 재고도 확인한다.
3. 공유 재고 10개에 1개씩 주문하는 작업 100개를 제출한다. 시작을 조율하고 모든 작업 완료 후 성공 횟수와 재고를 검증한다.
4. 테스트 시간 제한·풀 종료를 포함하고, 한 번의 통과가 모든 경쟁 상태의 부재를 증명하지 않는 이유를 설명한다.

**예상 결과**

| 초기 재고 | 주문 수량·방식 | 결과 | 남은 재고 |
|---:|---|---|---:|
| 10 | -1 또는 0 | `IllegalArgumentException` | 10 |
| 10 | 1 | true | 9 |
| 10 | 10 | true | 0 |
| 10 | 11 | false | 10 |
| 0 | 1 | false | 0 |
| 10 | 1개씩 100회 동시 주문 | 성공 10회, 실패 90회 | 0 |

**관련 예제:** [Part 24](part24_테스트.md), [StockService.java](examples/src/main/java/tutorial/part24/StockService.java), [StockServiceTest.java](examples/src/test/java/tutorial/part24/StockServiceTest.java).

### 문제 15. TCP 에코 프로토콜에 PING 추가

**요구사항**

1. Part 31.4의 `TcpEchoServer.java`와 `TcpEchoClient.java`를 별도 실습 폴더에 저장한다. 기본 주소 `127.0.0.1:5000`을 사용한다.
2. 기존 UTF-8·한 줄 한 메시지 규칙을 유지하며 `PING`에는 `PONG`, `QUIT`에는 `BYE`, 그 외에는 `ECHO: 원문`을 응답한다. 클라이언트는 응답 후 다음 요청을 보낸다.
3. 연결·읽기 시간 제한, EOF 처리, 자원 정리를 유지한다.
4. 줄바꿈과 flush의 역할, TCP와 UDP의 전달·메시지 경계 차이를 설명한다.

**예상 결과** — 요청과 응답의 대응이며 콘솔 안내 문구는 생략했다.

| 요청 | 응답 | 이후 상태 |
|---|---|---|
| `PING` | `PONG` | 다음 요청 대기 |
| `안녕` | `ECHO: 안녕` | 다음 요청 대기 |
| `QUIT` | `BYE` | 연결과 기본 서버 종료 |

**관련 예제:** [Part 31의 TCP 서버·클라이언트](part31_소켓프로그래밍과통신.md). Part 31은 문서 안에 전체 코드가 있고 `examples/run-core.sh 31` 항목은 없다.

### 문제 16. 같은 상품과 같은 객체 구분

**요구사항**

1. Part 32의 정책대로 상품 ID만으로 동등성을 정한다. ID는 null·빈 문자열을 허용하지 않고 변경 불가여야 한다.
2. ID가 P001인 서로 다른 이름의 상품 두 개와 첫 상품의 별칭 참조를 만든다.
3. `==`, `equals()`, `hashCode()` 관계, `HashSet` 중복 제거와 `HashMap`의 동등한 키 조회를 확인한다.
4. 같은 이름이지만 ID가 P002인 상품을 추가하고 별개 상품으로 남는지 확인한다.

**예상 결과**

```text
서로 다른 객체: true
별칭은 같은 객체: true
같은 ID의 동등성: true
동등한 객체의 해시 일치: true
같은 ID 추가 후 크기: 1
다른 ID 추가 후 크기: 2
동등한 키로 조회: 10
```

**관련 예제:** [Part 32](part32_Object와객체비교.md), [Product.java](examples/src/main/java/tutorial/part32/Product.java), [EqualityExample.java](examples/src/main/java/tutorial/part32/EqualityExample.java), [ProductTest.java](examples/src/test/java/tutorial/part32/ProductTest.java).

### 문제 17. 변경되는 상품과 주문 스냅샷

**요구사항**

1. 가격 1000의 가변 상품으로 원본 목록, 얕은 복사, 요소까지 복사한 목록을 만든다.
2. 주문 시점의 상품 ID·단가·수량 2를 불변 `OrderLine`에 저장하고 `OrderSnapshot`을 만든다. 생성자는 목록을 방어적으로 복사한다.
3. 상품 가격을 2000으로 바꾸고 원본 상품 목록과 입력 주문 목록을 비운다.
4. 얕은 복사·깊은 복사·주문 스냅샷의 결과를 비교한다. 반환된 주문 목록을 직접 비우는 시도도 확인한다.

**예상 결과**

```text
원본 리스트 크기: 0
얕은 복사 가격: 2000
깊은 복사 가격: 1000
주문 당시 가격: 1000
주문 항목 수: 1
주문 목록 직접 수정: 차단
```

**관련 예제:** [Part 33](part33_불변객체와객체복사.md), [ImmutabilityExample.java](examples/src/main/java/tutorial/part33/ImmutabilityExample.java), [ImmutabilityTest.java](examples/src/test/java/tutorial/part33/ImmutabilityTest.java). 기존 실행 결과를 먼저 예상한 뒤 재현한다.

### 문제 18. 취소 기한과 할인 금액의 경계

**요구사항**

1. 주문 시점을 `2026-11-17T03:00:00Z`로 고정하고 서울 시간으로 출력한다.
2. `Clock.fixed()`로 주문 1초 전, 정확히 30분 후, 30분 1초 후를 만든다. 취소는 주문부터 30분까지 포함한다.
3. 단가 19.99인 상품 3개에 10% 할인을 적용하고 합산·할인 후 마지막에 소수 두 자리 `HALF_UP` 반올림한다.
4. `2.0`과 `2.00`의 `equals()`와 `compareTo()` 결과를 비교한다.

**예상 결과**

```text
주문 시각(서울): 2026-11-17 12:00
주문 1초 전 취소: false
30분 후 취소: true
30분 1초 후 취소: false
할인 후 금액: 53.97
2.0 equals 2.00: false
2.0 compareTo 2.00: 0
```

**관련 예제:** [Part 34](part34_날짜시간과금액처리.md), [DateMoneyExample.java](examples/src/main/java/tutorial/part34/DateMoneyExample.java), [DateMoneyTest.java](examples/src/test/java/tutorial/part34/DateMoneyTest.java). 기존의 31분 후 검사보다 경계에 가까운 입력으로 확장한다.

### 문제 19. JSON 왕복 변환과 HTTP 실패 검증

**요구사항**

1. `ProductData`·`JsonCodec`을 사용해 이름이 `노트 "개정판"`인 상품을 JSON으로 저장했다가 복원한다. 따옴표 이스케이프를 직접 조립하지 않는다.
2. 로컬 `HttpFixtureServer`의 상품 조회와 404 경로를 확인한다.
3. 별도 테스트 서버에서 HTTP 200 + `application/json`이지만 본문이 잘못된 JSON인 경우도 거부하는지 검증한다.
4. HTTP 상태·본문 형식·JSON 구문·필수 값 검증을 구분하고 테스트 서버를 반드시 닫는다.

**예상 결과**

| 상황 | 결과 |
|---|---|
| 정상 상품의 JSON 왕복 변환 | 필드 값이 같음, 따옴표 포함 이름 유지 |
| 기존 서버의 `/products/P001` | ID P001, 이름 노트, 가격 1200.00 |
| 기존 서버의 `/missing` | `IOException`, 메시지 `HTTP 오류: 404` |
| HTTP 200 + 잘못된 JSON | JSON 변환 예외, 성공 상품을 반환하지 않음 |

**관련 예제:** [Part 35](part35_HTTP클라이언트와JSON.md), [JsonCodec.java](examples/src/main/java/tutorial/part35/JsonCodec.java), [HttpJsonExample.java](examples/src/main/java/tutorial/part35/HttpJsonExample.java), [JsonCodecTest.java](examples/src/test/java/tutorial/part35/JsonCodecTest.java), [HttpJsonIntegrationTest.java](examples/src/test/java/tutorial/part35/HttpJsonIntegrationTest.java).

### 문제 20. 할인 정책 확장과 설계 검증

**요구사항**

1. Part 36의 `OrderService`에 생성자로 새 `DiscountPolicy`를 주입한다. **소계가 5000.00 이상이면 500.00 할인**, 미만이면 원래 금액을 반환한다.
2. 서비스의 견적 계산 흐름을 수정하지 않고 정책을 교체한다. 이 인터페이스는 할인액이 아니라 **할인 후 금액**을 반환한다.
3. 소계 4999.99·5000.00·6000.00의 경계를 테스트한다. 수량 0, 없는 상품, 소계보다 큰 정책 반환값도 검사한다.
4. 단위 테스트는 stub을, 구성 요소 통합 테스트는 실제 `InMemoryProductRepository`를 사용한다. 어느 동작이 리팩토링이고 어느 동작이 기능 추가인지 설명한다.

**예상 결과**

| 입력·상황 | 결과 |
|---|---|
| 소계 4999.99 | 4999.99 |
| 소계 5000.00 | 4500.00 |
| 소계 6000.00 | 5500.00 |
| 수량 0 | `IllegalArgumentException` |
| 없는 상품 | `NoSuchElementException` |
| 정책이 소계 + 1을 반환 | `IllegalStateException` |

**관련 예제:** [Part 36](part36_객체지향설계와리팩토링.md), [DiscountPolicy.java](examples/src/main/java/tutorial/part36/DiscountPolicy.java), [OrderService.java](examples/src/main/java/tutorial/part36/OrderService.java), [OrderServiceTest.java](examples/src/test/java/tutorial/part36/OrderServiceTest.java), [OrderIntegrationTest.java](examples/src/test/java/tutorial/part36/OrderIntegrationTest.java).

---

## 해설

### 1. 계산기
```java
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
String op = sc.next();
int b = sc.nextInt();

int result = switch (op) {
    case "+" -> a + b;
    case "-" -> a - b;
    case "*" -> a * b;
    case "/" -> a / b;
    default -> throw new IllegalArgumentException("알 수 없는 연산자");
};
System.out.println(result);
```

### 2. 별 찍기
```java
int n = new Scanner(System.in).nextInt();
for (int i = 1; i <= n; i++) {
    System.out.println("*".repeat(i));
}
```

### 3. 최대공약수
```java
public static int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}
```

### 4. 회문 검사
```java
String s = new Scanner(System.in).nextLine();
String reversed = new StringBuilder(s).reverse().toString();
System.out.println(s.equals(reversed));
```

### 5. 배열 정렬 (버블 정렬)
```java
int[] arr = {5, 3, 8, 1, 9};
for (int i = 0; i < arr.length - 1; i++) {
    for (int j = 0; j < arr.length - 1 - i; j++) {
        if (arr[j] > arr[j + 1]) {
            int temp = arr[j];
            arr[j] = arr[j + 1];
            arr[j + 1] = temp;
        }
    }
}
```

### 6. 학생 관리
```java
public class Student {
    private String name;
    private int id;
    private int score;

    Student(String name, int id, int score) {
        this.name = name; this.id = id; this.score = score;
    }

    // getter, toString 생략
}

ArrayList<Student> students = new ArrayList<>();
students.add(new Student("민수", 1, 90));
students.add(new Student("영희", 2, 85));

// 평균
double avg = students.stream()
    .mapToInt(Student::getScore).average().orElse(0);

// 최고 점수
Student top = students.stream()
    .max(Comparator.comparingInt(Student::getScore))
    .orElseThrow();
```

### 7. 도형 넓이
```java
abstract class Shape {
    abstract double area();
}
class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    double area() { return Math.PI * r * r; }
}
class Rectangle extends Shape {
    double w, h;
    Rectangle(double w, double h) { this.w = w; this.h = h; }
    double area() { return w * h; }
}
class Triangle extends Shape {
    double base, height;
    Triangle(double base, double height) { this.base = base; this.height = height; }
    @Override
    double area() { return base * height / 2.0; }
}

Shape[] shapes = { new Circle(5), new Rectangle(3, 4), new Triangle(3, 4) };
for (Shape s : shapes) {
    System.out.printf(java.util.Locale.ROOT, "%.2f%n", s.area());
}
```

클래스 선언과 실행문은 각각 선언 위치와 `main()`에 둔다. 배열의 참조 타입은 `Shape`지만 실제 객체의 `area()`가 호출된다. 삼각형 넓이는 밑변 × 높이 ÷ 2이며 `Locale.ROOT`로 소수점 표기를 일정하게 한다.

### 8. 단어 빈도수
```java
String input = "the cat sat on the mat the cat";
HashMap<String, Integer> freq = new HashMap<>();
String normalized = input.strip();
if (!normalized.isEmpty()) {
    for (String word : normalized.split("\\s+")) {
        freq.merge(word, 1, Integer::sum);
    }
}
System.out.println(new TreeMap<>(freq));
```

`HashMap` 자체의 출력 순서는 채점 기준으로 삼지 않는다. 정렬 출력이 요구되므로 `TreeMap`을 사용했다. 처음 등장한 순서가 요구된다면 `LinkedHashMap`을 선택한다. 빈 입력을 먼저 제외하므로 빈 문자열을 단어로 세지 않는다.

### 9. 스트림 활용
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> result = nums.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
System.out.println(result);  // [100, 64, 36, 16, 4]
```

### 10. 파일 단어 수 세기
```java
Path path = Path.of("sample.txt");
List<String> lines = Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
long lineCount = lines.size();
long wordCount = lines.stream()
    .map(String::strip)
    .filter(line -> !line.isEmpty())
    .flatMap(line -> Arrays.stream(line.split("\\s+")))
    .count();
long charCount = lines.stream()
    .mapToLong(String::length).sum();

System.out.printf("줄: %d, 단어: %d, 문자: %d%n", lineCount, wordCount, charCount);
```

`Path`·`Files`는 `java.nio.file`, `List`·`Arrays`는 `java.util`에서 import한다. 파일 읽기의 `IOException`은 처리하거나 호출자에게 전달한다. 단어 계산에만 `strip()`과 빈 줄 필터를 적용하므로 원본 줄 수와 공백을 포함한 문자 수는 보존한다. `readAllLines()`는 파일 전체를 메모리에 읽는 학습용 방식이며 큰 파일은 스트림·버퍼 방식으로 처리한다.

### 11. 컬렉션 선택과 주문 우선순위

아래는 `java.util.*`를 import한 실행문이다. `Request`는 `main()` 내부의 지역 record로 선언할 수 있다.

```java
record Request(int urgency, long sequence, String productId) { }
Comparator<Request> byPriority = Comparator.comparingInt(Request::urgency)
        .thenComparingLong(Request::sequence);
Queue<Request> requests = new PriorityQueue<>(byPriority);
requests.offer(new Request(2, 1, "P003"));
requests.offer(new Request(1, 3, "P002"));
requests.offer(new Request(1, 2, "P001"));
List<String> processed = new ArrayList<>();
while (!requests.isEmpty()) processed.add(requests.poll().productId());
System.out.println("처리 순서: " + processed);

Map<String, Integer> counts = new HashMap<>();
for (String id : List.of("P002", "P001", "P002")) {
    counts.merge(id, 1, Integer::sum);
}
System.out.println("상품별 횟수: " + new TreeMap<>(counts));
```

긴급도 동률은 접수 번호로 해결한다. 이 입력에서는 접수 번호가 고유하므로 처리 순서가 결정된다. 비교 기준까지 모두 같은 요청 간 순서는 보장되지 않는다. FIFO는 `ArrayDeque`의 `offer()`·`poll()`로 구현하고, 우선순위 큐는 `poll()`을 반복한다. 맵의 **집계 방식**과 **출력 순서**는 별도로 선택한다.

### 12. 동시 주문 횟수와 비동기 금액 계산

`java.util.*`, `java.util.concurrent.*`, `java.util.concurrent.atomic.AtomicInteger`를 import한다. 실행문은 `main() throws Exception`에 둔다.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
AtomicInteger completed = new AtomicInteger();
ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
try {
    List<Future<?>> jobs = new ArrayList<>();
    for (int worker = 0; worker < 4; worker++) {
        jobs.add(pool.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                counts.merge("P001", 1, Integer::sum);
                completed.incrementAndGet();
            }
        }));
    }
    for (Future<?> job : jobs) job.get(5, TimeUnit.SECONDS);
    System.out.println("완료 작업: " + completed.get());
    System.out.println("P001 집계: " + counts.get("P001"));
    Future<Integer> total = pool.submit(() -> 1200 * 3);
    System.out.println("Future 합계: " + total.get(5, TimeUnit.SECONDS));
    CompletableFuture<Integer> discounted = CompletableFuture
            .supplyAsync(() -> 1200 * 3, pool)
            .thenApply(value -> value - 600);
    System.out.println("비동기 할인 합계: " + discounted.get(5, TimeUnit.SECONDS));
} finally {
    pool.shutdown();
    try {
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("풀 종료 시간 초과");
            }
        }
    } catch (InterruptedException e) {
        pool.shutdownNow();
        Thread.currentThread().interrupt();
        throw e;
    }
}
```

`volatile int`의 `++`는 읽기·계산·쓰기가 나뉘므로 갱신을 잃을 수 있다. `merge()`는 한 키의 갱신을, `incrementAndGet()`은 증가를 원자적으로 수행한다. 둘을 합친 하나의 트랜잭션은 아니지만 **모든 작업 완료 후** 두 값이 4000임을 확인할 수 있다. 작업 도중 두 값을 따로 읽으면 서로 다른 순간을 관찰할 수 있다.

### 13. 시간 초과와 협력적 작업 취소

`java.util.concurrent.*`를 import하고 실행문은 `main() throws Exception`에 둔다.

```java
ExecutorService pool = Executors.newSingleThreadExecutor();
CountDownLatch started = new CountDownLatch(1);
CountDownLatch finished = new CountDownLatch(1);
try {
    Future<?> job = pool.submit(() -> {
        started.countDown();
        try {
            new CountDownLatch(1).await(); // 완료 신호 없이 인터럽트를 기다림
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            finished.countDown();
        }
    });
    if (!started.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("시작 시간 초과");
    try {
        job.get(50, TimeUnit.MILLISECONDS);
        throw new IllegalStateException("작업이 예상보다 먼저 끝남");
    } catch (TimeoutException expected) {
        System.out.println("대기 시간 초과");
    }
    System.out.println("취소 전 작업 미완료: " + !job.isDone());
    System.out.println("취소 요청 수락: " + job.cancel(true));
    if (!finished.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("종료 시간 초과");
    System.out.println("작업 종료 확인: true");
    System.out.println("Future 취소 상태: " + job.isCancelled());
} finally {
    pool.shutdownNow();
    if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
        throw new IllegalStateException("풀 종료 시간 초과");
    }
}
```

작업은 신호가 오지 않는 latch에서 대기하므로 결과 대기 시간만 끝난다고 완료되지 않는다. `cancel(true)`가 인터럽트를 요청하고 작업이 이에 반응한다. `isCancelled()`는 Future 상태이며 종료 신호를 대신하지 않는다. 마지막 풀 종료 대기까지 확인한다. `CompletableFuture.cancel(true)`는 이 방식의 작업 스레드 인터럽트를 보장하지 않는다.

### 14. 재고의 경계값과 동시 주문 테스트

별도 `Exercise14Test.java`로 작성할 수 있는 테스트다. 기존 구현·테스트를 덮어쓰지 않는다.

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tutorial.part24.StockService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class Exercise14Test {
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 잘못된_수량은_상태를_유지한다(int quantity) {
        StockService service = new StockService(10);
        assertThrows(IllegalArgumentException.class, () -> service.order(quantity));
        assertEquals(10, service.getStock());
    }

    @ParameterizedTest
    @CsvSource({"1, true, 9", "10, true, 0", "11, false, 10"})
    void 주문_경계를_검사한다(int quantity, boolean success, int remaining) {
        StockService service = new StockService(10);
        assertEquals(success, service.order(quantity));
        assertEquals(remaining, service.getStock());
    }

    @Test
    void 품절_상태는_유지된다() {
        StockService service = new StockService(0);
        assertFalse(service.order(1));
        assertEquals(0, service.getStock());
    }

    @Test
    @Timeout(10)
    void 동시_주문_성공은_재고만큼이다() throws Exception {
        StockService service = new StockService(10);
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> jobs = new ArrayList<>();
        try {
            for (int i = 0; i < 100; i++) {
                jobs.add(pool.submit(() -> { start.await(); return service.order(1); }));
            }
            start.countDown();
            int successes = 0;
            for (Future<Boolean> job : jobs) {
                if (job.get(5, TimeUnit.SECONDS)) successes++;
            }
            assertEquals(10, successes);
            assertEquals(90, jobs.size() - successes);
            assertEquals(0, service.getStock());
        } finally {
            pool.shutdownNow();
            assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS));
        }
    }
}
```

모든 작업은 같은 `StockService`를 공유한다. 재고 확인·차감과 조회가 같은 객체의 잠금으로 보호되어야 한다. latch는 시작을 조율하지만 100개 작업이 물리적으로 동시에 실행된다는 뜻은 아니다. 테스트를 통과한 실행 순서 외에도 가능한 순서가 있으므로 코드의 동기화 범위를 함께 검토한다.

### 15. TCP 에코 프로토콜에 PING 추가

Part 31.4 서버의 `handle()` 안에서 기존 `String message;`와 읽기 루프를 아래로 교체한다. 바깥의 UTF-8 스트림·읽기 시간 제한·try-with-resources·예외 처리는 유지한다.

```java
String message;
while ((message = in.readLine()) != null) {
    String response = switch (message) {
        case "PING" -> "PONG";
        case "QUIT" -> "BYE";
        default -> "ECHO: " + message;
    };
    out.write(response);
    out.newLine();
    out.flush();
    if ("QUIT".equals(message)) break;
}
```

실습 폴더에서 컴파일하고 두 터미널로 실행한다.

```bash
javac -encoding UTF-8 --release 17 TcpEchoServer.java TcpEchoClient.java
# 터미널 1
java TcpEchoServer
# 터미널 2
java TcpEchoClient
```

클라이언트에서 `PING`, `안녕`, `QUIT`을 차례로 입력한다. 기본 서버는 한 연결을 처리한 뒤 끝난다. `newLine()`은 메시지 경계를 만들고 `flush()`는 출력 버퍼를 밀어낸다. 연결을 유지하며 줄바꿈 없이 flush만 하면 `readLine()`은 계속 기다릴 수 있다. 읽기 시간 제한은 메시지 전체의 수신 기한이 아니며, 외부 입력용 서버라면 최대 길이도 별도로 제한한다.

TCP는 순서 있는 바이트 스트림이며 한 번의 write와 한 번의 read가 대응하지 않는다. UDP는 데이터그램 경계를 갖지만 전달·순서를 보장하지 않는다. UDP 길이는 문자열의 글자 수가 아니라 UTF-8 바이트 수로 정하며, 수신 버퍼가 작으면 잘릴 수 있다. 자세한 실행 예는 Part 31.7을 참고한다.

### 16. 같은 상품과 같은 객체 구분

`tutorial.part32.Product`와 `java.util.*`를 import한 실행문이다. `Product`의 `equals(Object)`와 `hashCode()`를 직접 작성해 본 뒤 기존 구현과 비교한다.

```java
Product first = new Product("P001", "노트");
Product second = new Product("P001", "노트 개정판");
Product alias = first;
System.out.println("서로 다른 객체: " + (first != second));
System.out.println("별칭은 같은 객체: " + (first == alias));
System.out.println("같은 ID의 동등성: " + first.equals(second));
System.out.println("동등한 객체의 해시 일치: " + (first.hashCode() == second.hashCode()));
Set<Product> products = new HashSet<>(List.of(first, second));
System.out.println("같은 ID 추가 후 크기: " + products.size());
products.add(new Product("P002", "노트"));
System.out.println("다른 ID 추가 후 크기: " + products.size());
Map<Product, Integer> stocks = new HashMap<>();
stocks.put(first, 10);
System.out.println("동등한 키로 조회: " + stocks.get(second));
```

기존 `Product`는 `final` 클래스와 변경 불가 ID를 사용하고 생성자에서 null·빈 ID를 검사한다. 동등성은 이름이 아닌 ID로 정한다. `equals()`가 true이면 해시값도 같아야 한다. 반대 방향은 성립하지 않으므로 **다른 ID의 해시값이 반드시 다르다는 테스트는 작성하지 않는다.** Set의 크기와 키 조회 결과를 검사하면 임의 순서에 의존하지 않는다.

### 17. 변경되는 상품과 주문 스냅샷

`java.util.*`와 `tutorial.part33.ImmutabilityExample`의 중첩 타입 `MutableProduct`, `OrderLine`, `OrderSnapshot`을 import한다. 예를 들어 `import tutorial.part33.ImmutabilityExample.OrderLine;`처럼 작성한다.

```java
MutableProduct product = new MutableProduct("P001", 1000);
List<MutableProduct> original = new ArrayList<>(List.of(product));
List<MutableProduct> shallow = new ArrayList<>(original);
List<MutableProduct> deep = original.stream()
        .map(p -> new MutableProduct(p.getId(), p.getPrice())).toList();
List<OrderLine> lines = new ArrayList<>(List.of(
        new OrderLine(product.getId(), product.getPrice(), 2)));
OrderSnapshot snapshot = new OrderSnapshot(lines);
product.setPrice(2000);
original.clear();
lines.clear();
System.out.println("원본 리스트 크기: " + original.size());
System.out.println("얕은 복사 가격: " + shallow.get(0).getPrice());
System.out.println("깊은 복사 가격: " + deep.get(0).getPrice());
System.out.println("주문 당시 가격: " + snapshot.lines().get(0).unitPrice());
System.out.println("주문 항목 수: " + snapshot.lines().size());
try {
    snapshot.lines().clear();
} catch (UnsupportedOperationException expected) {
    System.out.println("주문 목록 직접 수정: 차단");
}
```

기존 `OrderSnapshot`의 생성자는 `lines = List.copyOf(lines)`를 수행한다. 목록 구조만 복사하므로 요소까지 안전하려면 `OrderLine`도 불변이어야 한다. 여기서는 `String`과 기본형만 보관한다. `Collections.unmodifiableList(lines)`만 사용하면 원본 `lines.clear()`가 뷰에 반영된다. 가변 요소를 넣은 `List.copyOf()`나 record 선언만으로 전체가 깊게 불변이 되지는 않는다.

### 18. 취소 기한과 할인 금액의 경계

`java.time.*`, `java.time.format.DateTimeFormatter`, `java.math.BigDecimal`, `tutorial.part34.DateMoneyExample`을 import한다.

```java
Instant orderedAt = Instant.parse("2026-11-17T03:00:00Z");
String display = orderedAt.atZone(ZoneId.of("Asia/Seoul"))
        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));
System.out.println("주문 시각(서울): " + display);
Clock before = Clock.fixed(orderedAt.minusSeconds(1), ZoneOffset.UTC);
Clock at30 = Clock.fixed(orderedAt.plusSeconds(1800), ZoneOffset.UTC);
Clock after30 = Clock.fixed(orderedAt.plusSeconds(1801), ZoneOffset.UTC);
System.out.println("주문 1초 전 취소: " + DateMoneyExample.canCancel(orderedAt, before));
System.out.println("30분 후 취소: " + DateMoneyExample.canCancel(orderedAt, at30));
System.out.println("30분 1초 후 취소: " + DateMoneyExample.canCancel(orderedAt, after30));
System.out.println("할인 후 금액: " + DateMoneyExample.discountedTotal(
        new BigDecimal("19.99"), 3, new BigDecimal("0.10")));
BigDecimal a = new BigDecimal("2.0"), b = new BigDecimal("2.00");
System.out.println("2.0 equals 2.00: " + a.equals(b));
System.out.println("2.0 compareTo 2.00: " + a.compareTo(b));
```

`canCancel()`은 `Duration`이 음수가 아니고 30분 이하인지 직접 비교한다. 분 단위로 잘라 비교하면 30분 1초를 놓친다. 금액은 문자열로 만들고 `19.99 × 3 × 0.90 = 53.973`을 마지막에 반올림해 53.97로 만든다. `BigDecimal.equals()`는 scale도 비교하므로 수치 비교와 구분한다. 이 문제는 실행 날짜·기본 시간대에 의존하지 않는다.

### 19. JSON 왕복 변환과 HTTP 실패 검증

별도 `Exercise19Test.java` 예시다. JSON 테스트는 네트워크 없이 실행하고 HTTP 테스트에만 `network` 태그를 붙인다. Jackson·JUnit 의존성은 기존 [build.gradle](examples/build.gradle)을 사용한다.

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tutorial.part35.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class Exercise19Test {
    @Test
    void 따옴표도_왕복_변환된다() throws Exception {
        ProductData original = new ProductData("P001", "노트 \"개정판\"",
                new BigDecimal("1200.00"));
        String json = JsonCodec.writeProduct(original);
        assertEquals(original, JsonCodec.readProduct(json));
    }

    @Test
    @Tag("network")
    void 정상_상품을_조회한다() throws Exception {
        HttpServer server = HttpFixtureServer.start(0); // 빈 포트 자동 배정
        try {
            ProductData actual = HttpJsonExample.fetchProduct(uri(server, "/products/P001"));
            assertEquals(new ProductData("P001", "노트", new BigDecimal("1200.00")), actual);
        } finally {
            server.stop(0);
        }
    }

    @Test
    @Tag("network")
    void HTTP_404를_거부한다() throws Exception {
        HttpServer server = HttpFixtureServer.start(0);
        try {
            IOException error = assertThrows(IOException.class,
                    () -> HttpJsonExample.fetchProduct(uri(server, "/missing")));
            assertEquals("HTTP 오류: 404", error.getMessage());
        } finally {
            server.stop(0);
        }
    }

    @Test
    @Tag("network")
    void HTTP_200이어도_깨진_JSON은_거부한다() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        try {
            server.createContext("/", exchange -> {
                try {
                    byte[] body = "{invalid".getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                } finally {
                    exchange.close();
                }
            });
            server.start();
            assertThrows(JsonProcessingException.class,
                    () -> HttpJsonExample.fetchProduct(uri(server, "/")));
        } finally {
            server.stop(0);
        }
    }

    private static URI uri(HttpServer server, String path) {
        return URI.create("http://127.0.0.1:" + server.getAddress().getPort() + path);
    }
}
```

JSON은 파서가 이스케이프를 처리한다. 상태가 200이고 헤더가 맞아도 본문 검증은 별도로 필요하다. 기존 `JsonCodecTest`는 누락·null·음수 가격 등도 확인한다. JSON 필드 순서나 전체 문자열이 아니라 복원한 값을 비교한다. 실패를 정상 상품이나 임의의 기본값으로 바꾸면 오류를 숨길 수 있다.

### 20. 할인 정책 확장과 설계 검증

별도 `Exercise20Test.java` 예시다. `Product`는 Part 32가 아닌 **Part 36 패키지**를 사용한다.

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tutorial.part36.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class Exercise20Test {
    private static final DiscountPolicy THRESHOLD = subtotal ->
            subtotal.compareTo(new BigDecimal("5000.00")) >= 0
                    ? subtotal.subtract(new BigDecimal("500.00")) : subtotal;

    @ParameterizedTest
    @CsvSource({"4999.99, 4999.99", "5000.00, 4500.00", "6000.00, 5500.00"})
    void 경계별_견적을_계산한다(String subtotal, String expected) {
        Product product = new Product("P001", "노트", new BigDecimal(subtotal));
        ProductRepository stub = id -> Optional.of(product);
        OrderService service = new OrderService(stub, THRESHOLD);
        assertEquals(new BigDecimal(expected), service.quote("P001", 1).total());
    }

    @Test
    void 양수가_아닌_수량을_거부한다() {
        Product product = new Product("P001", "노트", new BigDecimal("1200.00"));
        ProductRepository stub = id -> Optional.of(product);
        OrderService service = new OrderService(stub, THRESHOLD);
        assertThrows(IllegalArgumentException.class, () -> service.quote("P001", 0));
    }

    @Test
    void 없는_상품을_거부한다() {
        OrderService missing = new OrderService(id -> Optional.empty(), THRESHOLD);
        assertThrows(NoSuchElementException.class, () -> missing.quote("missing", 1));
    }

    @Test
    void 소계보다_큰_정책_결과를_거부한다() {
        Product product = new Product("P001", "노트", new BigDecimal("1200.00"));
        ProductRepository stub = id -> Optional.of(product);
        OrderService invalid = new OrderService(stub, value -> value.add(BigDecimal.ONE));
        assertThrows(IllegalStateException.class, () -> invalid.quote("P001", 1));
    }

    @Test
    void 실제_메모리저장소와_정책을_조합한다() {
        Product product = new Product("P001", "노트", new BigDecimal("1200.00"));
        ProductRepository repository = new InMemoryProductRepository(Map.of("P001", product));
        OrderService service = new OrderService(repository, THRESHOLD);
        assertEquals(new BigDecimal("5500.00"), service.quote("P001", 5).total());
    }
}
```

정책은 소계에 따라 **최종 금액**을 반환하고, 서비스가 유효 범위 검사와 마지막 반올림을 담당한다. 생성자 주입으로 서비스 코드를 바꾸지 않고 정책을 교체한다. 단위 테스트의 stub은 정해진 상품을 반환하고, 통합 테스트는 실제 메모리 저장소의 조회까지 연결한다. 이는 DB 통합 테스트는 아니다.

기존 동작을 유지하며 책임을 나누는 것은 리팩토링이다. 이번 임계 금액 할인 규칙을 추가하는 일은 기능 추가이며 새 경계 테스트가 필요하다. 실제 주문 저장·재고 차감·결제를 추가하려면 정합성·트랜잭션·중복 요청 정책을 별도로 설계해야 한다.

---

## 실행·검증 안내

새 문제의 해설은 이 문서에 포함되어 있으며, 기존 `examples`의 구현·테스트에 자동으로 추가된 것은 아니다. 먼저 원래 예제를 실행해 기준 동작을 확인하고, 실습용 파일에서 확장한다.

```bash
# 저장소 루트: 기존 JDK 예제, 문제 11~13·16~18·20의 출발점
bash examples/run-core.sh all
```

JUnit 해설을 각각 `examples/src/test/java/Exercise14Test.java`, `Exercise19Test.java`, `Exercise20Test.java`로 저장했다면 다음 명령으로 실행한다. 이 프로젝트에는 Gradle Wrapper가 없으므로 설치된 **Gradle 9.x**의 `gradle` 명령을 사용한다. 첫 의존성 다운로드에는 인터넷이 필요하다.

```bash
cd examples
gradle test --tests Exercise14Test --tests Exercise20Test
gradle test --tests Exercise19Test
# 로컬 포트를 사용할 수 있는 환경에서 HTTP 테스트도 포함
gradle test -PnetworkTests --tests Exercise19Test --rerun-tasks
```

마지막 두 명령은 각각 문제 19의 JSON 테스트만, JSON과 HTTP 테스트 모두를 실행한다. 기존 전체 테스트는 `gradle test`, HTTP 포함은 `gradle test -PnetworkTests`로 실행한다. 추가 테스트를 저장하면 기존 기준인 36개·38개보다 실행 수가 늘어난다. 콘솔 안내·실행 시간 대신 프로그램 결과와 assertion 성공 여부를 확인한다.

[목차](README.md) · [요약으로 돌아가기](part29_요약.md)
