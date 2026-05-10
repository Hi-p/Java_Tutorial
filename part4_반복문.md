# Part 4. 반복문

> 같은 일을 여러 번 반복할 때. `for`, `while`, `do-while` 세 가지.

---

## 4.1 for문

```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);
}
```

```
0
1
2
3
4
```

**구조**: `for (초기화; 조건; 증감)`

| 부분 | 역할 |
|---|---|
| `int i = 0` | 시작 값 |
| `i < 5` | 조건 (false면 종료) |
| `i++` | 한 바퀴 돌고 나서 실행 |

**응용: 1~10 합계**

```java
int sum = 0;
for (int i = 1; i <= 10; i++) {
    sum += i;
}
System.out.println(sum);  // 55
```

---

## 4.2 while문

조건이 `true`인 동안 반복.

```java
int i = 0;
while (i < 5) {
    System.out.println(i);
    i++;
}
```

```
0
1
2
3
4
```

> `for`는 횟수가 정해져 있을 때, `while`은 조건 기반일 때 적합.

---

## 4.3 do-while문

**최소 1번은 실행**한 뒤 조건 검사.

```java
int i = 10;
do {
    System.out.println(i);
    i++;
} while (i < 5);
```

```
10
```

> `i = 10`이라 조건은 거짓이지만, **한 번은 실행**됨.

---

## 4.4 break와 continue

**break — 반복문 즉시 탈출**

```java
for (int i = 0; i < 10; i++) {
    if (i == 5) break;
    System.out.print(i + " ");
}
```

```
0 1 2 3 4
```

**continue — 이번 회차 건너뛰기**

```java
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) continue;
    System.out.print(i + " ");
}
```

```
1 3 5 7 9
```

---

## 4.5 중첩 반복문

```java
for (int i = 2; i <= 5; i++) {
    for (int j = 1; j <= 3; j++) {
        System.out.printf("%d × %d = %d%n", i, j, i * j);
    }
    System.out.println();
}
```

```
2 × 1 = 2
2 × 2 = 4
2 × 3 = 6

3 × 1 = 3
...
```

---

## 4.6 자주 빠지는 함정 ⚠️

1. **무한 루프** — 조건이 항상 `true`면 멈추지 않음. `while (true)` + `break`로 제어
2. **off-by-one** — `<`와 `<=` 혼동. `i < 5`는 0~4, `i <= 5`는 0~5
3. **변수 초기화** — `sum` 같은 누적 변수는 반드시 `0`으로 초기화
4. **세미콜론 실수** — `for (int i=0; i<5; i++);` ← 세미콜론 때문에 빈 반복

---

## 4.7 연습문제

**1)** 1~100 합계 출력

**2)** 구구단 전체(2~9단) 출력

**3)** 정수 n을 입력받아 1~n 중 짝수만 출력

**4)** 정수 n을 입력받아 팩토리얼(n!) 출력

---

## 해설

**1)**
```java
int sum = 0;
for (int i = 1; i <= 100; i++) sum += i;
System.out.println(sum);  // 5050
```

**2)**
```java
for (int i = 2; i <= 9; i++) {
    for (int j = 1; j <= 9; j++)
        System.out.printf("%d × %d = %d%n", i, j, i * j);
    System.out.println();
}
```

**3)**
```java
int n = new Scanner(System.in).nextInt();
for (int i = 2; i <= n; i += 2)
    System.out.print(i + " ");
```

**4)**
```java
int n = new Scanner(System.in).nextInt();
long fact = 1;
for (int i = 1; i <= n; i++) fact *= i;
System.out.println(fact);
```
