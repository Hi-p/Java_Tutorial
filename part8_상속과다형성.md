# Part 8. 상속과 다형성

> 기존 클래스를 **확장**하고, 같은 메소드가 **다르게 동작**하게 만드는 기법.

---

## 8.1 상속 — `extends`

```java
public class Animal {
    String name;

    void eat() {
        System.out.println(name + "이(가) 먹습니다");
    }
}

public class Dog extends Animal {
    void bark() {
        System.out.println(name + "이(가) 짖습니다");
    }
}
```

```java
Dog d = new Dog();
d.name = "멍멍이";
d.eat();   // Animal의 메소드 사용 가능
d.bark();  // Dog만의 메소드
```

> `Dog`는 `Animal`의 모든 것을 물려받고 + 자기만의 기능을 추가.

---

## 8.2 메소드 오버라이딩

부모의 메소드를 자식이 **재정의**.

```java
public class Animal {
    void sound() {
        System.out.println("...");
    }
}

public class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("멍멍!");
    }
}

public class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("야옹!");
    }
}
```

```java
Dog d = new Dog();
Cat c = new Cat();
d.sound();  // 멍멍!
c.sound();  // 야옹!
```

> `@Override` 어노테이션은 선택이지만, **붙이는 습관**이 실수를 줄인다.

---

## 8.3 super — 부모 호출

```java
public class Animal {
    String name;

    Animal(String name) {
        this.name = name;
    }

    void info() {
        System.out.println("이름: " + name);
    }
}

public class Dog extends Animal {
    String breed;

    Dog(String name, String breed) {
        super(name);  // 부모 생성자 호출 (첫 줄이어야 함!)
        this.breed = breed;
    }

    @Override
    void info() {
        super.info();  // 부모 메소드 호출
        System.out.println("품종: " + breed);
    }
}
```

---

## 8.4 다형성 — 업캐스팅

부모 타입 변수에 자식 객체를 담을 수 있다.

```java
Animal a1 = new Dog("멍멍이", "진돗개");
Animal a2 = new Cat();

a1.sound();  // 멍멍! (실제 객체의 메소드 실행)
a2.sound();  // 야옹!
```

**배열로 다형성 활용**

```java
Animal[] animals = { new Dog(), new Cat(), new Dog() };
for (Animal a : animals) {
    a.sound();  // 각 객체에 맞는 메소드 실행
}
```

```
멍멍!
야옹!
멍멍!
```

> 이것이 다형성의 핵심: **같은 타입, 다른 동작**.

---

## 8.5 instanceof — 타입 확인

```java
Animal a = new Dog();
System.out.println(a instanceof Dog);     // true
System.out.println(a instanceof Animal);  // true
System.out.println(a instanceof Cat);     // false
```

---

## 8.6 자주 빠지는 함정 ⚠️

1. **자바는 단일 상속** — `extends`는 하나만 가능 (다중 상속은 인터페이스로)
2. **super() 는 생성자 첫 줄** — 다른 코드보다 먼저 와야 함
3. **업캐스팅 시 자식 고유 메소드 호출 불가** — `Animal a = new Dog();` → `a.bark()` 불가
4. **`@Override` 오타** — 메소드 시그니처가 다르면 오버라이딩이 아니라 새 메소드

---

## 8.7 연습문제

**1)** `Shape` 부모 클래스 + `Circle`, `Rectangle` 자식 클래스 만들어 각각 넓이 계산

**2)** `Shape[] shapes` 배열에 여러 도형을 넣고 반복문으로 넓이 출력 (다형성)
