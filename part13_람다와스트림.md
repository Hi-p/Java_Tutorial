# Part 13. 람다와 스트림

> Java 8의 **혁명적 변화**. 코드를 짧고 선언적으로 쓸 수 있게 해준다.

---

## 13.1 람다식 — 익명 함수

```java
// 기존 방식
Comparator<String> comp1 = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
};

// 람다식
Comparator<String> comp2 = (a, b) -> a.length() - b.length();
```

**문법**: `(매개변수) -> { 본문 }` / 한 줄이면 `{}` 생략 가능

```java
// 매개변수 없음
Runnable r = () -> System.out.println("Hello");

// 매개변수 1개 (괄호 생략 가능)
Consumer<String> c = s -> System.out.println(s);

// 매개변수 2개
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
```

---

## 13.2 함수형 인터페이스

| 인터페이스 | 메소드 | 용도 |
|---|---|---|
| `Predicate<T>` | `boolean test(T)` | 조건 검사 |
| `Function<T, R>` | `R apply(T)` | 변환 |
| `Consumer<T>` | `void accept(T)` | 소비 (반환 없음) |
| `Supplier<T>` | `T get()` | 생성 (입력 없음) |

```java
Predicate<Integer> isEven = n -> n % 2 == 0;
System.out.println(isEven.test(4));   // true

Function<String, Integer> strLen = s -> s.length();
System.out.println(strLen.apply("hello"));  // 5
```

---

## 13.3 Stream API

컬렉션 데이터를 **파이프라인**으로 처리.

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 짝수만 골라서 제곱한 뒤 합계
int result = nums.stream()
    .filter(n -> n % 2 == 0)     // 짝수만
    .map(n -> n * n)             // 제곱
    .reduce(0, Integer::sum);    // 합계

System.out.println(result);  // 220
```

**주요 메소드**

| 메소드 | 역할 | 예시 |
|---|---|---|
| `.filter()` | 조건에 맞는 것만 | `.filter(n -> n > 0)` |
| `.map()` | 변환 | `.map(s -> s.toUpperCase())` |
| `.sorted()` | 정렬 | `.sorted()` |
| `.distinct()` | 중복 제거 | `.distinct()` |
| `.limit(n)` | 앞에서 n개 | `.limit(5)` |
| `.forEach()` | 각 요소에 작업 | `.forEach(System.out::println)` |
| `.collect()` | 결과 모으기 | `.collect(Collectors.toList())` |
| `.count()` | 개수 | `.count()` |
| `.reduce()` | 하나로 합치기 | `.reduce(0, Integer::sum)` |

---

## 13.4 메소드 참조 — `::`

```java
// 람다식
list.forEach(s -> System.out.println(s));

// 메소드 참조 (같은 동작)
list.forEach(System.out::println);
```

| 종류 | 예시 |
|---|---|
| 정적 메소드 | `Integer::parseInt` |
| 인스턴스 메소드 | `String::toUpperCase` |
| 특정 객체 메소드 | `System.out::println` |

---

## 13.5 실전 예제

**문자열 리스트에서 길이 3 이상인 것만 대문자로 변환**

```java
List<String> words = List.of("hi", "hello", "bye", "java", "ok");

List<String> result = words.stream()
    .filter(w -> w.length() >= 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

System.out.println(result);  // [HELLO, BYE, JAVA]
```

---

## 13.6 자주 빠지는 함정 ⚠️

1. **스트림은 일회용** — 한 번 사용하면 재사용 불가. 새로 만들어야 함
2. **원본 변경 안 함** — 스트림은 **새로운 결과**를 만들 뿐 원본을 바꾸지 않음
3. **지연 평가** — `filter`, `map` 등은 **최종 연산**(forEach, collect)이 호출되어야 실행됨
