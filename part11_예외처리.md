# Part 11. 예외 처리

> 프로그램이 터지지 않게 **에러를 잡아서 처리**하는 방법.

---

## 11.1 예외란?

```java
int[] arr = {1, 2, 3};
System.out.println(arr[5]);  // ArrayIndexOutOfBoundsException 💥
```

> 예외가 발생하면 프로그램이 **즉시 종료**된다. 이걸 막으려면 `try-catch`.

---

## 11.2 try-catch

```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("0으로 나눌 수 없습니다");
    System.out.println("에러 메시지: " + e.getMessage());
}
```

```
0으로 나눌 수 없습니다
에러 메시지: / by zero
```

**여러 예외 잡기**

```java
try {
    // 위험한 코드
} catch (NumberFormatException e) {
    System.out.println("숫자 변환 실패");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("배열 범위 초과");
} catch (Exception e) {
    System.out.println("기타 에러: " + e.getMessage());
}
```

---

## 11.3 finally — 무조건 실행

```java
try {
    int result = 10 / 0;
} catch (Exception e) {
    System.out.println("에러 발생");
} finally {
    System.out.println("이건 무조건 실행됨");
}
```

> `finally`는 에러 여부와 상관없이 **항상 실행**. 파일 닫기, DB 연결 해제 등에 사용.

---

## 11.4 throws — 예외 떠넘기기

```java
public static int divide(int a, int b) throws ArithmeticException {
    return a / b;  // 예외가 발생하면 호출한 쪽에서 처리
}

public static void main(String[] args) {
    try {
        System.out.println(divide(10, 0));
    } catch (ArithmeticException e) {
        System.out.println("나누기 실패");
    }
}
```

---

## 11.5 커스텀 예외

```java
public class AgeException extends Exception {
    AgeException(String message) {
        super(message);
    }
}

public static void checkAge(int age) throws AgeException {
    if (age < 0) throw new AgeException("나이는 음수가 될 수 없습니다");
}
```

---

## 11.6 주요 예외 종류

| 예외 | 원인 |
|---|---|
| `NullPointerException` | null 객체에 접근 |
| `ArrayIndexOutOfBoundsException` | 배열 범위 초과 |
| `ArithmeticException` | 0으로 나누기 |
| `NumberFormatException` | 문자열→숫자 변환 실패 |
| `ClassCastException` | 잘못된 타입 캐스팅 |
| `FileNotFoundException` | 파일 없음 |

### Checked vs Unchecked

| | Checked | Unchecked |
|---|---|---|
| 상속 | `Exception` | `RuntimeException` |
| 컴파일 시 | 반드시 처리 강제 | 처리 안 해도 컴파일 됨 |
| 예시 | `IOException` | `NullPointerException` |

---

## 11.7 자주 빠지는 함정 ⚠️

1. **빈 catch** — `catch (Exception e) { }` ← 에러를 삼켜버림. 최소한 로그는 남기자
2. **Exception으로 다 잡기** — 구체적 예외를 먼저 catch하고, `Exception`은 마지막에
3. **NullPointerException** — 자바 버그 1위. null 체크를 습관화 (Part 18에서 상세히)
