# Part 3. 조건문

> 프로그램에 **판단력**을 준다. "만약 ~라면 이렇게, 아니면 저렇게."

---

## 3.1 if문

```java
int score = 85;

if (score >= 60) {
    System.out.println("합격");
}
```

```
합격
```

> 조건이 `true`면 `{}` 안의 코드 실행, `false`면 건너뜀.

---

## 3.2 if-else

```java
int age = 15;

if (age >= 18) {
    System.out.println("성인");
} else {
    System.out.println("미성년자");
}
```

```
미성년자
```

---

## 3.3 if-else if-else

```java
int score = 85;

if (score >= 90) {
    System.out.println("A");
} else if (score >= 80) {
    System.out.println("B");
} else if (score >= 70) {
    System.out.println("C");
} else {
    System.out.println("F");
}
```

```
B
```

> 위에서부터 순서대로 검사. **하나라도 `true`면 나머지는 건너뜀.**

---

## 3.4 중첩 if

```java
int age = 20;
boolean hasTicket = true;

if (age >= 18) {
    if (hasTicket) {
        System.out.println("입장 가능");
    } else {
        System.out.println("티켓 필요");
    }
} else {
    System.out.println("나이 미달");
}
```

```
입장 가능
```

---

## 3.5 switch문

같은 변수를 여러 값과 비교할 때 `if-else if`보다 깔끔.

```java
int day = 3;

switch (day) {
    case 1:
        System.out.println("월요일");
        break;
    case 2:
        System.out.println("화요일");
        break;
    case 3:
        System.out.println("수요일");
        break;
    default:
        System.out.println("기타");
        break;
}
```

```
수요일
```

> ⚠️ **`break` 빠뜨리면** 아래 case까지 전부 실행된다 (fall-through).

### switch에 사용 가능한 타입

| 타입 | 사용 가능 |
|---|---|
| `int`, `byte`, `short`, `char` | ✓ |
| `String` (Java 7+) | ✓ |
| `double`, `float` | ✗ |
| `boolean` | ✗ |

---

## 3.6 자주 빠지는 함정 ⚠️

1. **조건에 괄호 필수** — `if score >= 60` (✗) → `if (score >= 60)` (✓)
2. **`=` vs `==`** — `if (x = 5)` 는 대입! `if (x == 5)` 가 비교
3. **중괄호 생략 주의** — 한 줄이면 생략 가능하지만, 버그 원인이 됨. **항상 쓰는 습관**
4. **switch의 break** — 빠뜨리면 다음 case까지 실행됨
5. **문자열 비교** — switch 안에서는 `String` 비교 가능하지만, if에서는 `.equals()` 사용

---

## 3.7 연습문제

**1)** 정수를 입력받아 양수/음수/0 판별

**2)** 점수(0~100)를 입력받아 등급 출력 (A/B/C/D/F)

**3)** 월(1~12)을 입력받아 해당 계절 출력 (switch 사용)

**4)** 세 정수를 입력받아 가장 큰 수 출력

---

## 해설

**1)**
```java
Scanner sc = new Scanner(System.in);
int n = sc.nextInt();
if (n > 0) System.out.println("양수");
else if (n < 0) System.out.println("음수");
else System.out.println("0");
```

**2)**
```java
int score = new Scanner(System.in).nextInt();
if (score >= 90) System.out.println("A");
else if (score >= 80) System.out.println("B");
else if (score >= 70) System.out.println("C");
else if (score >= 60) System.out.println("D");
else System.out.println("F");
```

**3)**
```java
int month = new Scanner(System.in).nextInt();
switch (month) {
    case 3: case 4: case 5:
        System.out.println("봄"); break;
    case 6: case 7: case 8:
        System.out.println("여름"); break;
    case 9: case 10: case 11:
        System.out.println("가을"); break;
    case 12: case 1: case 2:
        System.out.println("겨울"); break;
}
```

**4)**
```java
Scanner sc = new Scanner(System.in);
int a = sc.nextInt(), b = sc.nextInt(), c = sc.nextInt();
int max = a;
if (b > max) max = b;
if (c > max) max = c;
System.out.println(max);
```
