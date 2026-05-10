# Part 27. 디버깅 테크닉

> 버그를 **체계적으로 찾고 고치는** 방법. 초보와 고수의 가장 큰 차이.

---

## 27.1 에러 메시지 읽기

```
Exception in thread "main" java.lang.NullPointerException
    at com.example.UserService.getUser(UserService.java:15)
    at com.example.Main.main(Main.java:8)
```

**읽는 순서:**
1. **맨 첫 줄** — 에러 종류: `NullPointerException`
2. **at 라인** — 어디서 터졌는지: `UserService.java:15`
3. **아래로** — 호출 흐름 (역순)

> 스택 트레이스를 **아래서 위로** 읽으면 흐름이 보인다.

---

## 27.2 자주 만나는 에러

| 에러 | 원인 | 해결 |
|---|---|---|
| `NullPointerException` | null에 접근 | null 체크, Optional 사용 |
| `ArrayIndexOutOfBoundsException` | 배열 범위 초과 | 인덱스 확인 |
| `ClassCastException` | 잘못된 형변환 | instanceof 체크 |
| `StackOverflowError` | 무한 재귀 | 재귀 종료 조건 확인 |
| `ConcurrentModificationException` | 반복 중 수정 | Iterator 사용 |
| `OutOfMemoryError` | 메모리 부족 | 메모리 누수 확인 |

---

## 27.3 IDE 디버거 사용법 (IntelliJ)

### 브레이크포인트 설정
- 라인 번호 옆 **클릭** → 빨간 점 (브레이크포인트)
- **Debug 모드로 실행** (벌레 아이콘 🪲)

### 디버그 모드 컨트롤

| 버튼 | 단축키 (Mac) | 설명 |
|---|---|---|
| Step Over | `F8` | 다음 줄로 (메소드 안으로 안 들어감) |
| Step Into | `F7` | 메소드 안으로 진입 |
| Step Out | `Shift+F8` | 현재 메소드 빠져나가기 |
| Resume | `F9` | 다음 브레이크포인트까지 실행 |

### 변수 확인
- **Variables 탭** — 현재 스코프의 모든 변수값 확인
- **Evaluate Expression** (`⌥+F8`) — 원하는 표현식 실시간 평가
- **Watch** — 특정 변수/표현식을 지속 감시

---

## 27.4 System.out.println 디버깅

IDE 디버거가 최선이지만, 간단한 경우:

```java
System.out.println("=== DEBUG: x = " + x);
System.out.println("=== DEBUG: 여기까지 옴");
```

> 다만 디버깅 끝나면 **반드시 제거**. 실무에서는 Logger 사용.

---

## 27.5 디버깅 체크리스트

1. ☐ **에러 메시지를 끝까지 읽었는가?**
2. ☐ **어느 라인에서 터졌는지 확인했는가?**
3. ☐ **변수 값이 예상대로인지 확인했는가?**
4. ☐ **입력값이 올바른지 확인했는가?**
5. ☐ **최근에 바꾼 코드가 문제인지 확인했는가?** (git diff)
6. ☐ **같은 에러를 구글링해봤는가?**

---

## 27.6 실무 팁

- **재현 가능해야 고칠 수 있다** — 버그가 발생하는 정확한 조건을 찾자
- **이분 탐색** — 코드 반을 주석 처리해서 어느 쪽 문제인지 좁히기
- **고무 오리 디버깅** — 누군가(또는 오리 인형)에게 코드를 설명하다 보면 답이 보임
