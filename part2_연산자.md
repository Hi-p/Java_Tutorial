# Part 2. 연산자

> 산술·비교·논리 세 가지. 조건문·반복문 안에서 매번 등장하는 도구.

---

## 2.1 산술 연산자

| 연산자 | 의미 | 예시 | 결과 |
|---|---|---|---|
| `+` | 더하기 | `7 + 3` | `10` |
| `-` | 빼기 | `7 - 3` | `4` |
| `*` | 곱하기 | `7 * 3` | `21` |
| `/` | 나누기 | `7 / 3` | `2` |
| `%` | 나머지 | `7 % 3` | `1` |

> ⚠️ **정수끼리 나누면 정수**가 나온다. `7 / 3 = 2` (소수점 버림)
> 실수 결과를 원하면: `7.0 / 3` 또는 `(double) 7 / 3`

**복합 대입 연산자**

```java
int x = 10;
x += 3;   // x = x + 3 → 13
x -= 2;   // x = x - 2 → 11
x *= 4;   // x = x * 4 → 44
x /= 2;   // x = x / 2 → 22
x %= 5;   // x = x % 5 → 2
```

**증감 연산자**

```java
int a = 5;
a++;       // a = 6 (1 증가)
a--;       // a = 5 (1 감소)
System.out.println(a++);  // 5 출력 후 증가 → a는 6
System.out.println(++a);  // 증가 후 출력 → 7
```

---

## 2.2 비교 연산자 → 결과는 항상 `true` 또는 `false`

| 연산자 | 의미 |
|---|---|
| `==` | 같다 |
| `!=` | 다르다 |
| `>`, `<` | 크다, 작다 |
| `>=`, `<=` | 크거나 같다, 작거나 같다 |

```java
System.out.println(5 > 3);    // true
System.out.println(5 == 5);   // true
System.out.println(5 != 3);   // true
```

### ⚠️ 문자열 비교는 `equals()`

```java
String a = "hello";
String b = "hello";
System.out.println(a == b);        // true일 수도 있지만 보장 안 됨!
System.out.println(a.equals(b));   // true (항상 정확)
```

> **문자열 비교는 반드시 `.equals()`**. `==`는 주소를 비교하므로 위험하다.

---

## 2.3 논리 연산자

| 연산자 | 의미 |
|---|---|
| `&&` | 둘 다 참일 때 참 (AND) |
| `\|\|` | 하나라도 참이면 참 (OR) |
| `!` | 반대로 (NOT) |

```java
int age = 17;
System.out.println(age >= 13 && age <= 19);  // true
System.out.println(age < 13 || age > 19);    // false
System.out.println(!true);                    // false
```

### 진리표

| A | B | A && B | A \|\| B | !A |
|---|---|---|---|---|
| T | T | T | T | F |
| T | F | F | T | F |
| F | T | F | T | T |
| F | F | F | F | T |

---

## 2.4 삼항 연산자

```java
int score = 85;
String result = (score >= 60) ? "합격" : "불합격";
System.out.println(result);  // 합격
```

> `조건 ? 참일 때 값 : 거짓일 때 값` — if-else를 한 줄로 쓸 수 있다.

---

## 2.5 연산자 우선순위

1. `()` 괄호
2. `++`, `--` 증감
3. `*`, `/`, `%` 곱셈·나눗셈
4. `+`, `-` 덧셈·뺄셈
5. `<`, `>`, `<=`, `>=` 비교
6. `==`, `!=` 동등
7. `&&` 논리 AND
8. `||` 논리 OR

> 헷갈릴 땐 **괄호**로 묶자.

---

## 2.6 자주 빠지는 함정 ⚠️

1. **정수 나누기** — `7 / 3 = 2` (소수점 날아감)
2. **`=`는 대입, `==`는 비교**
3. **문자열 비교** — `==` 쓰지 말고 `.equals()` 사용
4. **`&&`와 `||`** — 파이썬의 `and`, `or`와 같은 역할
5. **`++a` vs `a++`** — 전위/후위 증감 차이 주의

---

## 2.7 연습문제

**1)** 정수를 입력받아 짝수면 `true`, 홀수면 `false` 출력

**2)** 두 정수를 입력받아 `/`와 정수 나누기 결과 각각 출력

**3)** 정수를 입력받아 1 이상 100 이하인지 `true`/`false` 출력

**4)** 3자리 정수의 각 자릿수 합 출력 (357 → 15)

---

## 해설

**1)**
```java
int n = new Scanner(System.in).nextInt();
System.out.println(n % 2 == 0);
```

**2)**
```java
Scanner sc = new Scanner(System.in);
int a = sc.nextInt(), b = sc.nextInt();
System.out.println((double) a / b);
System.out.println(a / b);
```

**3)**
```java
int n = new Scanner(System.in).nextInt();
System.out.println(n >= 1 && n <= 100);
```

**4)**
```java
int n = new Scanner(System.in).nextInt();
System.out.println(n/100 + n/10%10 + n%10);
```
