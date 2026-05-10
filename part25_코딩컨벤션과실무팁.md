# Part 25. 코딩 컨벤션과 실무 팁

> 혼자 코딩할 때와 **팀으로 개발할 때**는 규칙이 다르다.

---

## 25.1 네이밍 규칙

| 대상 | 규칙 | 예시 |
|---|---|---|
| 클래스 | PascalCase | `UserService`, `OrderItem` |
| 메소드/변수 | camelCase | `getUserName()`, `totalPrice` |
| 상수 | UPPER_SNAKE | `MAX_SIZE`, `DEFAULT_VALUE` |
| 패키지 | 소문자 | `com.example.service` |
| Boolean 변수 | is/has/can 접두사 | `isValid`, `hasPermission` |

---

## 25.2 패키지 구조

```
com.example.myapp/
├── controller/     ← 요청 처리 (API 엔드포인트)
├── service/        ← 비즈니스 로직
├── repository/     ← DB 접근
├── entity/         ← DB 테이블 매핑
├── dto/            ← 데이터 전송 객체
├── config/         ← 설정
├── exception/      ← 커스텀 예외
└── util/           ← 유틸리티
```

---

## 25.3 로깅

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void createUser(String name) {
        log.info("사용자 생성: {}", name);       // 정보
        log.warn("중복 이메일 감지");             // 경고
        log.error("사용자 생성 실패", exception);  // 에러
    }
}
```

> `System.out.println` 대신 **Logger** 사용. 레벨별 필터링, 파일 저장 가능.

---

## 25.4 Git 활용

**커밋 메시지 규칙**

```
feat: 사용자 등록 API 추가
fix: 로그인 비밀번호 검증 오류 수정
refactor: UserService 코드 정리
docs: README 업데이트
test: UserController 테스트 추가
```

**브랜치 전략**

```
main        ← 배포용 (안정)
  └── develop   ← 개발용
       ├── feature/login     ← 기능 개발
       ├── feature/signup
       └── hotfix/bug-123    ← 긴급 수정
```

---

## 25.5 실무 꿀팁

1. **DTO를 쓰자** — Entity를 API 응답으로 직접 내보내지 말 것. 보안/유연성 문제.
2. **예외는 GlobalExceptionHandler로** — `@ControllerAdvice`로 한 곳에서 처리
3. **환경 변수로 설정 관리** — 비밀번호, API 키를 코드에 직접 쓰지 말 것
4. **코드 리뷰 받기** — PR(Pull Request) 통해 다른 사람의 눈으로 확인
5. **IDE 단축키 익히기** — IntelliJ 단축키만 잘 써도 생산성 2배

---

## 25.6 IntelliJ 필수 단축키 (Mac / Windows)

| 기능 | Mac | Windows |
|---|---|---|
| 자동 완성 | `Ctrl+Space` | `Ctrl+Space` |
| 빠른 수정 | `⌥+Enter` | `Alt+Enter` |
| 코드 포맷 | `⌥+⌘+L` | `Ctrl+Alt+L` |
| 이름 변경 | `Shift+F6` | `Shift+F6` |
| 파일 검색 | `⌘+Shift+O` | `Ctrl+Shift+N` |
| 전체 검색 | `⌘+Shift+F` | `Ctrl+Shift+F` |
| 실행 | `Ctrl+R` | `Shift+F10` |
