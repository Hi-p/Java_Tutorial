# Part 6. 메소드

> 코드를 **묶어서 이름 붙이기**. 반복을 줄이고 구조를 잡는 핵심 도구.

---

## 6.1 메소드란?

```java
public static void greet() {
    System.out.println("안녕하세요!");
}

public static void main(String[] args) {
    greet();   // 호출
    greet();   // 재사용
}
```

```
안녕하세요!
안녕하세요!
```

---

## 6.2 매개변수와 반환값

```java
public static int add(int a, int b) {
    return a + b;
}

public static void main(String[] args) {
    int result = add(3, 5);
    System.out.println(result);  // 8
}
```

| 요소 | 설명 |
|---|---|
| `int` (반환 타입) | 결과의 자료형 |
| `add` | 메소드 이름 |
| `int a, int b` | 매개변수 (입력) |
| `return a + b` | 결과 돌려주기 |

**void — 반환값 없음**

```java
public static void printLine() {
    System.out.println("----------");
    // return 없음
}
```

---

## 6.3 오버로딩 — 같은 이름, 다른 매개변수

```java
public static int add(int a, int b) {
    return a + b;
}

public static double add(double a, double b) {
    return a + b;
}

public static int add(int a, int b, int c) {
    return a + b + c;
}
```

> 이름은 같지만 **매개변수의 타입/개수**가 다르면 별도 메소드로 인식.

---

## 6.4 메소드 설계 원칙

1. **하나의 메소드 = 하나의 역할** — `calculateAndPrintAndSave()` (✗)
2. **이름은 동사** — `getScore()`, `isValid()`, `printResult()`
3. **짧게** — 20줄 넘어가면 쪼갤 수 있는지 고민

---

## 6.5 자주 빠지는 함정 ⚠️

1. **`static` 빼먹기** — `main`에서 직접 호출하려면 `static` 필요 (객체지향 이전)
2. **return 빠뜨리기** — 반환 타입이 `void`가 아니면 반드시 `return` 필요
3. **매개변수 타입 생략** — `add(a, b)` (✗) → `add(int a, int b)` (✓)

---

## 6.6 연습문제

**1)** 두 정수 중 큰 값을 반환하는 메소드 `max(int a, int b)` 작성

**2)** 정수 배열을 받아 합계를 반환하는 메소드 작성

**3)** 문자열을 받아 모음 개수를 반환하는 메소드 작성

---

## 해설

**1)**
```java
public static int max(int a, int b) {
    return (a > b) ? a : b;
}
```

**2)**
```java
public static int sum(int[] arr) {
    int total = 0;
    for (int n : arr) total += n;
    return total;
}
```

**3)**
```java
public static int countVowels(String s) {
    int count = 0;
    for (char c : s.toLowerCase().toCharArray()) {
        if ("aeiou".indexOf(c) >= 0) count++;
    }
    return count;
}
```
