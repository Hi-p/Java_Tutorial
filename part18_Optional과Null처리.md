# Part 18. Optional과 Null 처리

> `NullPointerException`은 자바 버그 **1위**. Optional로 안전하게 다루자.

---

## 18.1 null의 문제

```java
String getName() { return null; }

String name = getName();
System.out.println(name.length());  // NullPointerException 💥
```

**전통적 해결법: null 체크**

```java
if (name != null) {
    System.out.println(name.length());
}
```

> 문제: null 체크가 코드 곳곳에 흩어지면 가독성이 떨어진다.

---

## 18.2 Optional이란?

**"값이 있을 수도, 없을 수도 있다"** 를 명시적으로 표현하는 컨테이너.

```java
import java.util.Optional;

Optional<String> opt1 = Optional.of("hello");
Optional<String> opt2 = Optional.empty();
Optional<String> opt3 = Optional.ofNullable(null);  // null이면 empty

System.out.println(opt1.isPresent());  // true
System.out.println(opt2.isPresent());  // false
```

---

## 18.3 Optional 사용법

```java
Optional<String> name = Optional.ofNullable(getName());

// 값이 있으면 출력
name.ifPresent(n -> System.out.println("이름: " + n));

// 값이 없으면 기본값
String result = name.orElse("이름 없음");

// 값이 없으면 예외
String result2 = name.orElseThrow(() -> new RuntimeException("이름 필수"));

// 변환
Optional<Integer> len = name.map(String::length);
```

| 메소드 | 설명 |
|---|---|
| `.isPresent()` | 값 있는지 확인 |
| `.ifPresent(동작)` | 값 있으면 동작 실행 |
| `.orElse(기본값)` | 없으면 기본값 |
| `.orElseGet(생성함수)` | 없으면 함수 호출해서 생성 |
| `.orElseThrow()` | 없으면 예외 |
| `.map(변환)` | 값 변환 |
| `.filter(조건)` | 조건 충족 시만 유지 |

---

## 18.4 실전 패턴

```java
// DB에서 사용자 조회
public Optional<User> findById(int id) {
    User user = db.query(id);
    return Optional.ofNullable(user);
}

// 사용
String email = findById(1)
    .map(User::getEmail)
    .orElse("no-reply@example.com");
```

---

## 18.5 자주 빠지는 함정 ⚠️

1. **Optional을 필드로 쓰지 말 것** — 반환 타입으로만 사용하는 것이 권장
2. **`get()` 직접 호출 위험** — 비어있으면 예외. `orElse()` 계열 사용
3. **`Optional.of(null)`** — NPE 발생! null 가능성 있으면 `ofNullable()` 사용
4. **과도한 사용** — 단순 null 체크가 더 명확할 때도 있음
