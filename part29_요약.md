# Part 29. 요약 — Java 치트시트

> 전체 내용을 한 눈에 정리.

---

## 🟢 기본 문법

```java
// 출력
System.out.println("hello");
System.out.printf("%s은 %d살%n", name, age);

// 입력
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
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
class Puppy extends Dog { }

// 인터페이스
interface Flyable { void fly(); }
class Bird implements Flyable { public void fly() { } }

// 추상 클래스
abstract class Shape { abstract double area(); }
```

---

## 🔵 컬렉션

```java
ArrayList<String> list = new ArrayList<>();
list.add("a"); list.get(0); list.size(); list.remove(0);

HashMap<String, Integer> map = new HashMap<>();
map.put("key", 1); map.get("key"); map.containsKey("key");

HashSet<String> set = new HashSet<>();
set.add("a"); set.contains("a"); set.size();
```

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
