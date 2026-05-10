# Part 24. 테스트 (JUnit 5)

> 코드가 **의도대로 동작하는지** 자동으로 검증. 현업에서 테스트 없으면 배포 불가.

---

## 24.1 왜 테스트?

- 코드 수정 후 **기존 기능이 깨지지 않았는지** 확인
- 버그를 **미리** 발견
- 리팩토링할 때 **안전망**

---

## 24.2 JUnit 5 기본

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void 더하기() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void 나누기_0으로() {
        Calculator calc = new Calculator();
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }
}
```

---

## 24.3 주요 Assertion

| 메소드 | 설명 |
|---|---|
| `assertEquals(expected, actual)` | 값 비교 |
| `assertNotEquals(a, b)` | 다른지 확인 |
| `assertTrue(조건)` | true인지 |
| `assertFalse(조건)` | false인지 |
| `assertNull(객체)` | null인지 |
| `assertNotNull(객체)` | null이 아닌지 |
| `assertThrows(예외, 실행)` | 예외 발생하는지 |

---

## 24.4 테스트 생명주기

```java
@BeforeAll   static void 전체_시작_전() { }
@BeforeEach  void 각_테스트_전() { }
@Test        void 테스트() { }
@AfterEach   void 각_테스트_후() { }
@AfterAll    static void 전체_종료_후() { }
```

---

## 24.5 Mock — 가짜 객체

```java
import static org.mockito.Mockito.*;

@Test
void 사용자_조회_테스트() {
    // 가짜 Repository 생성
    UserRepository mockRepo = mock(UserRepository.class);
    when(mockRepo.findById(1L)).thenReturn(Optional.of(new User("민수")));

    UserService service = new UserService(mockRepo);
    User user = service.getUser(1L);

    assertEquals("민수", user.getName());
    verify(mockRepo).findById(1L);  // 호출 여부 확인
}
```

> DB, 외부 API 등 **외부 의존성을 가짜로** 대체하여 순수한 로직만 테스트.

---

## 24.6 테스트 실행

```bash
# Gradle
./gradlew test

# Maven
./mvnw test
```

---

## 24.7 좋은 테스트의 원칙

1. **테스트 이름은 한국어도 OK** — `void 잔액부족이면_출금실패()`
2. **하나의 테스트 = 하나의 검증** — 여러 assert를 몰아넣지 말 것
3. **Given-When-Then 패턴** — 준비 → 실행 → 검증
4. **독립적** — 테스트 순서에 의존하면 안 됨
