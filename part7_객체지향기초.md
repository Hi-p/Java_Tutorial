# Part 7. 객체지향 기초

> 자바의 **핵심 철학**. 모든 것이 클래스와 객체로 돌아간다.

---

## 7.1 클래스와 객체

클래스 = **설계도**, 객체 = **설계도로 만든 실체**

```java
public class Dog {
    String name;
    int age;

    void bark() {
        System.out.println(name + "이(가) 짖습니다!");
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Dog d1 = new Dog();
        d1.name = "멍멍이";
        d1.age = 3;
        d1.bark();  // 멍멍이이(가) 짖습니다!

        Dog d2 = new Dog();
        d2.name = "초코";
        d2.bark();  // 초코이(가) 짖습니다!
    }
}
```

> 클래스 하나로 객체를 여러 개 만들 수 있다. 붕어빵 틀(클래스) → 붕어빵(객체).

---

## 7.2 생성자 (Constructor)

객체를 만들 때 **자동 호출**되는 특수 메소드.

```java
public class Dog {
    String name;
    int age;

    // 생성자
    Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void info() {
        System.out.println(name + ", " + age + "살");
    }
}
```

```java
Dog d = new Dog("멍멍이", 3);
d.info();  // 멍멍이, 3살
```

> `this` = "이 객체 자신". 매개변수 이름과 필드 이름이 같을 때 구분용.

---

## 7.3 접근 제어자

| 제어자 | 같은 클래스 | 같은 패키지 | 자식 클래스 | 외부 |
|---|---|---|---|---|
| `public` | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| (없음) default | ✓ | ✓ | ✗ | ✗ |
| `private` | ✓ | ✗ | ✗ | ✗ |

**캡슐화 — `private` + getter/setter**

```java
public class Student {
    private String name;
    private int score;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getScore() { return score; }
    public void setScore(int score) {
        if (score >= 0 && score <= 100) {
            this.score = score;
        }
    }
}
```

> 필드는 `private`, 접근은 `getter/setter`로. 이게 **캡슐화**의 기본.

---

## 7.4 static — 클래스 소속

```java
public class Counter {
    static int count = 0;  // 모든 객체가 공유

    Counter() { count++; }
}

Counter c1 = new Counter();
Counter c2 = new Counter();
System.out.println(Counter.count);  // 2
```

| | `static` | non-static |
|---|---|---|
| 소속 | 클래스 | 객체 |
| 호출 | `클래스명.메소드()` | `객체.메소드()` |
| 공유 | 모든 객체가 공유 | 객체마다 별도 |

---

## 7.5 자주 빠지는 함정 ⚠️

1. **`new` 빼먹기** — `Dog d;` 만으로는 객체 생성 안 됨. `new Dog()` 필수
2. **파일명 = public 클래스명** — `Dog.java` 안에 `public class Dog`
3. **`this` 안 쓰면** — 매개변수가 필드를 가림 (`name = name`은 의미 없음)
4. **`static`에서 non-static 접근 불가** — `main`에서 인스턴스 메소드 직접 호출 불가

---

## 7.6 연습문제

**1)** `Rectangle` 클래스: 가로, 세로를 받아 넓이와 둘레를 반환하는 메소드

**2)** `BankAccount` 클래스: 잔액 필드 + 입금/출금/잔액조회 메소드

---

## 해설

**1)**
```java
public class Rectangle {
    private int width, height;

    Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int area() { return width * height; }
    int perimeter() { return 2 * (width + height); }
}
```

**2)**
```java
public class BankAccount {
    private int balance = 0;

    void deposit(int amount) { balance += amount; }
    void withdraw(int amount) {
        if (amount <= balance) balance -= amount;
        else System.out.println("잔액 부족");
    }
    int getBalance() { return balance; }
}
```
