# Part 1. 입출력과 변수

> `System.out.println`, `Scanner`, 변수, 자료형. 이 한 묶음이 안 잡히면 그 다음이 다 무너진다.

---

## 1.1 출력 — `System.out.println`

```java
System.out.println("안녕하세요");
System.out.println(10);
System.out.println(3 + 5);
```

```
안녕하세요
10
8
```

| 메소드 | 동작 |
|---|---|
| `System.out.println()` | 출력 후 **줄 바꿈** |
| `System.out.print()` | 출력 후 줄 바꿈 **안 함** |
| `System.out.printf()` | **서식 지정** 출력 |

**printf — 서식 지정 출력**

```java
String name = "민수";
int age = 17;
System.out.printf("%s님은 %d살입니다%n", name, age);
System.out.printf("원주율: %.2f%n", 3.14159);
```

```
민수님은 17살입니다
원주율: 3.14
```

| 서식 | 의미 | 예시 |
|---|---|---|
| `%d` | 정수 | `17` |
| `%f` | 실수 | `3.141590` |
| `%.2f` | 소수점 2자리 | `3.14` |
| `%s` | 문자열 | `"민수"` |
| `%n` | 줄 바꿈 | |

---

## 1.2 변수 — 데이터에 이름 붙이기

```java
String name = "민수";
int age = 17;
double pi = 3.14;
boolean isStudent = true;

System.out.println(name + " " + age);
System.out.println(pi + " " + isStudent);
```

```
민수 17
3.14 true
```

> 자바는 **변수를 선언할 때 자료형을 반드시 적어야 한다.** 파이썬과 가장 큰 차이.

**규칙**
- 영문/숫자/언더스코어(`_`)만 사용
- 숫자로 시작 불가 (`1score` ✗, `score1` ✓)
- 대소문자 구별 (`Age`와 `age`는 다른 변수)
- **카멜 케이스** 관례: `myFirstName`, `totalScore`

**상수 — `final`**

```java
final double PI = 3.14159;
// PI = 3.0;  // 에러! final은 값 변경 불가
```

---

## 1.3 자료형

### 기본형 (Primitive Type)

| 자료형 | 크기 | 예시 | 설명 |
|---|---|---|---|
| `int` | 4바이트 | `100` | 정수 (가장 많이 씀) |
| `long` | 8바이트 | `100L` | 큰 정수. 뒤에 `L` |
| `double` | 8바이트 | `3.14` | 실수 (기본) |
| `float` | 4바이트 | `3.14f` | 실수. 뒤에 `f` |
| `char` | 2바이트 | `'A'` | 문자 **하나** (작은따옴표) |
| `boolean` | 1비트 | `true` | 참/거짓 |

### 참조형

| 자료형 | 예시 | 설명 |
|---|---|---|
| `String` | `"안녕"` | 문자열 (큰따옴표) |

> **핵심**: 정수 `int`, 실수 `double`, 문자열 `String`이 기본.

---

## 1.4 입력 — `Scanner`

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름: ");
        String name = sc.nextLine();
        System.out.println("안녕, " + name);
        sc.close();
    }
}
```

| 메소드 | 반환 타입 | 설명 |
|---|---|---|
| `sc.nextInt()` | `int` | 정수 입력 |
| `sc.nextDouble()` | `double` | 실수 입력 |
| `sc.next()` | `String` | 한 단어 |
| `sc.nextLine()` | `String` | 한 줄 전체 |

---

## 1.5 형변환

```java
// 자동 (작은 → 큰)
int a = 10;
double b = a;      // 10.0

// 강제 (큰 → 작은)
double c = 3.99;
int d = (int) c;   // 3 (소수점 버림)

// 문자열 ↔ 숫자
int n = Integer.parseInt("100");
String s = String.valueOf(42);
```

---

## 1.6 자주 빠지는 함정 ⚠️

1. **자료형 선언 필수** — `x = 10;` (✗) → `int x = 10;` (✓)
2. **`String`은 대문자 S** — `string` (✗)
3. **문자 vs 문자열** — `'A'`(char) ≠ `"A"`(String)
4. **`Scanner` import 빼먹기** — `import java.util.Scanner;` 필수
5. **`nextInt()` 뒤 `nextLine()` 문제** — 엔터가 버퍼에 남음
6. **세미콜론 빼먹기** — 모든 문장 끝에 `;`

**5번 함정 해결법**

```java
int age = sc.nextInt();
sc.nextLine();              // ← 버퍼에 남은 엔터 소비
String name = sc.nextLine();
```

---

## 1.7 연습문제

**1)** 두 정수를 입력받아 합·차·곱·몫·나머지 출력

**2)** 반지름 입력받아 원의 넓이 출력 (π = 3.14)

**3)** 4자리 정수의 각 자릿수 합 출력 (1234 → 10)

**4)** 섭씨 → 화씨 변환 (F = C × 9/5 + 32)

**5)** 이름과 나이를 입력받아 `"○○님은 ○○살입니다"` 출력

---

## 해설

**1)**
```java
Scanner sc = new Scanner(System.in);
int a = sc.nextInt(), b = sc.nextInt();
System.out.println(a + b);
System.out.println(a - b);
System.out.println(a * b);
System.out.println(a / b);
System.out.println(a % b);
```

**2)**
```java
double r = new Scanner(System.in).nextDouble();
System.out.println(3.14 * r * r);
```

**3)**
```java
int n = new Scanner(System.in).nextInt();
System.out.println(n/1000 + n/100%10 + n/10%10 + n%10);
```

**4)**
```java
double c = new Scanner(System.in).nextDouble();
System.out.println(c * 9.0 / 5 + 32);
```

**5)**
```java
Scanner sc = new Scanner(System.in);
String name = sc.nextLine();
int age = sc.nextInt();
System.out.println(name + "님은 " + age + "살입니다");
```
