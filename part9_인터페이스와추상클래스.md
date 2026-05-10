# Part 9. 인터페이스와 추상클래스

> **"이 메소드는 반드시 구현하세요"** 라고 약속을 강제하는 도구.

---

## 9.1 추상클래스 — `abstract`

일부 메소드를 **미완성**으로 남겨두는 클래스. 직접 객체 생성 불가.

```java
public abstract class Shape {
    String color;

    abstract double area();  // 구현 없이 선언만

    void printColor() {      // 일반 메소드도 가능
        System.out.println("색상: " + color);
    }
}

public class Circle extends Shape {
    double radius;

    Circle(double radius) { this.radius = radius; }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}
```

```java
// Shape s = new Shape();  // 에러! 추상클래스는 new 불가
Shape s = new Circle(5);
System.out.println(s.area());  // 78.539...
```

---

## 9.2 인터페이스 — `interface`

메소드 **시그니처만** 정의. 구현은 클래스가 담당.

```java
public interface Flyable {
    void fly();  // 구현 없음 (자동으로 public abstract)
}

public interface Swimable {
    void swim();
}

public class Duck implements Flyable, Swimable {
    @Override
    public void fly() {
        System.out.println("오리가 날아갑니다");
    }

    @Override
    public void swim() {
        System.out.println("오리가 수영합니다");
    }
}
```

> **다중 구현 가능** — `implements A, B, C` (상속과 다르게!)

---

## 9.3 추상클래스 vs 인터페이스

| | 추상클래스 | 인터페이스 |
|---|---|---|
| 키워드 | `abstract class` | `interface` |
| 다중 | 단일 상속만 | 다중 구현 가능 |
| 필드 | 일반 필드 가능 | 상수만 (`public static final`) |
| 메소드 | 일반 + 추상 메소드 | 추상 메소드 (+ `default`, `static`) |
| 생성자 | 있음 | 없음 |
| 용도 | "~이다" (is-a) | "~할 수 있다" (can-do) |

---

## 9.4 default 메소드 (Java 8+)

인터페이스에도 **구현된 메소드**를 넣을 수 있다.

```java
public interface Greeting {
    void sayHello();

    default void sayBye() {
        System.out.println("안녕히 가세요!");
    }
}
```

> 기존 구현 클래스를 깨뜨리지 않고 인터페이스에 메소드를 추가할 때 유용.

---

## 9.5 자주 빠지는 함정 ⚠️

1. **추상 메소드 구현 안 하면** — 자식도 `abstract`로 선언해야 함
2. **인터페이스 메소드는 자동으로 `public`** — 구현 시 `public` 빼먹으면 에러
3. **추상클래스는 extends, 인터페이스는 implements** — 키워드 혼동 주의

---

## 9.6 연습문제

**1)** `Printable` 인터페이스를 만들고, `Book`과 `Magazine` 클래스로 구현

**2)** `Animal` 추상클래스 + `Flyable` 인터페이스를 동시에 사용하는 `Bird` 클래스 작성
