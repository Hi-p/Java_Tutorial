# Part 15. Enum과 어노테이션

> **정해진 값의 집합**과 **코드에 메타정보를 붙이는** 도구.

---

## 15.1 Enum — 열거형

```java
public enum Season {
    SPRING, SUMMER, FALL, WINTER
}
```

```java
Season s = Season.SUMMER;
System.out.println(s);           // SUMMER
System.out.println(s.name());    // SUMMER
System.out.println(s.ordinal()); // 1 (순서, 0부터)

// switch와 찰떡궁합
switch (s) {
    case SPRING -> System.out.println("봄");
    case SUMMER -> System.out.println("여름");
    case FALL   -> System.out.println("가을");
    case WINTER -> System.out.println("겨울");
}
```

**필드와 메소드가 있는 Enum**

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    EARTH(5.976e+24, 6.37814e6);

    private final double mass;
    private final double radius;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    double surfaceGravity() {
        return 6.67300E-11 * mass / (radius * radius);
    }
}
```

> Enum은 단순 상수 집합을 넘어 **메소드와 필드를 가진 클래스**처럼 쓸 수 있다.

---

## 15.2 왜 상수 대신 Enum?

```java
// 나쁜 예 — 매직 넘버
int status = 1;  // 1이 뭐지...?

// 좋은 예 — Enum
enum Status { ACTIVE, INACTIVE, SUSPENDED }
Status status = Status.ACTIVE;  // 의미가 명확
```

| | 상수 (`int`) | Enum |
|---|---|---|
| 타입 안전 | ✗ (아무 숫자 가능) | ✓ (정해진 값만) |
| 가독성 | 나쁨 | 좋음 |
| IDE 지원 | 없음 | 자동완성 가능 |

---

## 15.3 어노테이션 — `@`

코드에 **메타정보**를 붙이는 장치.

| 어노테이션 | 용도 |
|---|---|
| `@Override` | 메소드 오버라이딩 명시 |
| `@Deprecated` | 더 이상 사용하지 말 것 |
| `@SuppressWarnings` | 경고 무시 |
| `@FunctionalInterface` | 함수형 인터페이스 명시 |

```java
@Override
public String toString() {
    return "재정의된 toString";
}

@Deprecated
public void oldMethod() {
    // 이 메소드는 사용하지 마세요
}
```

> 실무에서는 Spring의 `@Controller`, `@Service`, `@Autowired` 등을 수없이 만나게 된다.

---

## 15.4 자주 빠지는 함정 ⚠️

1. **Enum에 `new` 불가** — `new Season()` (✗). 정의된 값만 사용
2. **`==`로 비교 가능** — Enum은 `equals()` 대신 `==` 사용 OK
3. **`@Override` 습관화** — 오타 실수를 컴파일 타임에 잡아줌
