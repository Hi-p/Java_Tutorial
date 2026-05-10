# Part 19. 디자인 패턴 기초

> 반복되는 설계 문제에 대한 **검증된 해결책**. 면접에서도 단골 질문.

---

## 19.1 Singleton — 하나만 존재

```java
public class Database {
    private static Database instance;

    private Database() {}  // 외부에서 new 불가

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}

Database db1 = Database.getInstance();
Database db2 = Database.getInstance();
System.out.println(db1 == db2);  // true (같은 객체)
```

> 설정, DB 연결, 로거 등 **하나만 있어야 하는 객체**에 사용.

---

## 19.2 Factory — 객체 생성을 위임

```java
public interface Shape { void draw(); }

public class Circle implements Shape {
    public void draw() { System.out.println("원"); }
}
public class Rectangle implements Shape {
    public void draw() { System.out.println("사각형"); }
}

public class ShapeFactory {
    public static Shape create(String type) {
        return switch (type) {
            case "circle"    -> new Circle();
            case "rectangle" -> new Rectangle();
            default -> throw new IllegalArgumentException("알 수 없는 타입");
        };
    }
}

Shape s = ShapeFactory.create("circle");
s.draw();  // 원
```

> 객체 생성 로직을 **한 곳에 모아서** 관리. Spring의 핵심 원리.

---

## 19.3 Observer — 이벤트 알림

```java
public interface Observer {
    void update(String message);
}

public class EventSystem {
    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer o) { observers.add(o); }
    public void notify(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}
```

```java
EventSystem events = new EventSystem();
events.subscribe(msg -> System.out.println("A 수신: " + msg));
events.subscribe(msg -> System.out.println("B 수신: " + msg));
events.notify("새 메시지!");
```

```
A 수신: 새 메시지!
B 수신: 새 메시지!
```

> 하나의 이벤트를 **여러 곳에 전파**. GUI, 메시징 시스템에서 많이 사용.

---

## 19.4 Strategy — 알고리즘 교체

```java
public interface SortStrategy {
    void sort(int[] arr);
}

public class BubbleSort implements SortStrategy {
    public void sort(int[] arr) { /* 버블 정렬 */ }
}
public class QuickSort implements SortStrategy {
    public void sort(int[] arr) { /* 퀵 정렬 */ }
}

public class Sorter {
    private SortStrategy strategy;
    
    public Sorter(SortStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void execute(int[] arr) { strategy.sort(arr); }
}

// 사용: 전략 교체 가능
Sorter sorter = new Sorter(new QuickSort());
sorter.execute(data);
```

> 같은 인터페이스, **다른 구현을 끼워넣는** 패턴.

---

## 19.5 Builder — 복잡한 객체 생성

```java
public class User {
    private String name;
    private int age;
    private String email;

    private User() {}

    public static class Builder {
        private User user = new User();
        
        public Builder name(String name) { user.name = name; return this; }
        public Builder age(int age) { user.age = age; return this; }
        public Builder email(String email) { user.email = email; return this; }
        public User build() { return user; }
    }
}

User user = new User.Builder()
    .name("민수")
    .age(17)
    .email("minsu@example.com")
    .build();
```

> 매개변수가 많은 생성자 대신 **체이닝** 방식으로 가독성 향상.

---

## 19.6 패턴 정리

| 패턴 | 목적 | 현업 사용 예 |
|---|---|---|
| Singleton | 인스턴스 1개 보장 | 설정, DB 연결 풀 |
| Factory | 객체 생성 위임 | Spring Bean 생성 |
| Observer | 이벤트 구독/알림 | 이벤트 리스너, MQ |
| Strategy | 알고리즘 교체 | 결제 방식, 정렬 방식 |
| Builder | 복잡한 객체 생성 | Lombok `@Builder` |
