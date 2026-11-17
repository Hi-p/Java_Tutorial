# Part 26. 최신 Java 문법

> Java 10~21에서 추가된 **모던 자바 기능**들. 코드가 훨씬 깔끔해진다.

---

## 26.1 var — 지역 변수 타입 추론 (Java 10)

```java
// 기존
ArrayList<String> list = new ArrayList<String>();
HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// var 사용
var list = new ArrayList<String>();
var map = new HashMap<String, List<Integer>>();
```

> 컴파일러가 **타입을 추론**. 지역 변수에만 사용 가능. 필드, 매개변수에는 불가.

---

## 26.2 텍스트 블록 (Java 15 정식, Java 13 미리보기)

```java
// 기존
String json = "{\n" +
    "  \"name\": \"민수\",\n" +
    "  \"age\": 17\n" +
    "}";

// 텍스트 블록
String json = """
    {
        "name": "민수",
        "age": 17
    }
    """;
```

> 여러 줄 문자열을 **그대로** 쓸 수 있음. JSON, HTML, SQL에 유용.

---

## 26.3 switch 표현식 (Java 14)

```java
// 기존 (statement)
String result;
switch (day) {
    case 1: result = "월"; break;
    case 2: result = "화"; break;
    default: result = "기타"; break;
}

// 새 문법 (expression)
String result = switch (day) {
    case 1 -> "월";
    case 2 -> "화";
    default -> "기타";
};
```

> `break` 불필요, 값을 **직접 반환** 가능.

---

## 26.4 record — 얕게 불변인 데이터 클래스 (Java 16)

```java
// 기존: 30줄짜리 DTO
public class User {
    private final String name;
    private final int age;
    // 생성자, getter, equals, hashCode, toString... 😫
}

// record: 한 줄!
public record User(String name, int age) {}
```

```java
User u = new User("민수", 17);
System.out.println(u.name());   // 민수
System.out.println(u.age());    // 17
System.out.println(u);          // User[name=민수, age=17]
```

> 별도로 선언하지 않으면 `record`는 표준 생성자, 컴포넌트 접근자, equals, hashCode, toString을 자동 제공한다. 접근자는 `getName()`이 아니라 `name()` 형태다. 가변 리스트 등 내부 객체까지 불변으로 만드는 것은 아니므로 [Part 33의 방어적 복사](part33_불변객체와객체복사.md)를 함께 참고한다.

---

## 26.5 sealed class — 상속 제한 (Java 17)

```java
public sealed class Shape permits Circle, Rectangle {}

public final class Circle extends Shape {}
public final class Rectangle extends Shape {}
// public class Triangle extends Shape {}  // 에러! permits에 없음
```

> 상속할 수 있는 클래스를 **명시적으로 제한**.

---

## 26.6 패턴 매칭 (Java 16+)

```java
// 기존
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// 패턴 매칭
if (obj instanceof String s) {
    System.out.println(s.length());  // 캐스팅 자동!
}
```

---

## 26.7 Java 버전별 주요 기능 요약

| 버전 | 기능 |
|---|---|
| Java 8 | 람다, 스트림, Optional |
| Java 10 | `var` |
| Java 11 | `String.isBlank()`, `Files.readString()` |
| Java 13 | 텍스트 블록 미리보기 |
| Java 15 | 텍스트 블록 정식 |
| Java 14 | switch 표현식 |
| Java 16 | record, 패턴 매칭 |
| Java 17 ⭐ | sealed class **(LTS)** |
| Java 21 ⭐ | 가상 스레드, 패턴 매칭 강화 **(LTS)** |

> **LTS(Long Term Support)** 버전을 사용하자: Java 17 또는 21 추천.
