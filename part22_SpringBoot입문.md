# Part 22. Spring Boot 입문

> 자바 백엔드 개발의 **사실상 표준**. 웹 서비스를 빠르게 만들 수 있는 프레임워크.

---

## 22.1 Spring Boot란?

- **Spring** = 자바 엔터프라이즈 프레임워크 (설정이 복잡)
- **Spring Boot** = Spring을 **쉽게** 쓸 수 있게 포장한 것

---

## 22.2 프로젝트 생성

[https://start.spring.io](https://start.spring.io) 접속 → 설정 선택 → Generate

| 설정 | 추천 값 |
|---|---|
| Project | Gradle - Groovy |
| Language | Java |
| Spring Boot | 최신 안정 버전 |
| Packaging | Jar |
| Java | 17 |

**Dependencies** 추가: `Spring Web`, `Spring Data JPA`, `H2 Database`

---

## 22.3 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java     ← 시작점
│   │   ├── controller/              ← 요청 처리
│   │   ├── service/                 ← 비즈니스 로직
│   │   ├── repository/              ← DB 접근
│   │   └── entity/                  ← 데이터 모델
│   └── resources/
│       └── application.properties   ← 설정
└── test/
```

---

## 22.4 Hello World API

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
```

실행 후 `http://localhost:8080/hello` 접속 → `Hello, Spring Boot!`

---

## 22.5 핵심 개념: DI (의존성 주입)

```java
@Service
public class UserService {
    public String getUser() { return "민수"; }
}

@RestController
public class UserController {
    private final UserService userService;

    // 생성자 주입 (추천 방식)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String user() {
        return userService.getUser();
    }
}
```

> `@Service`, `@Controller` 등의 어노테이션을 붙이면 Spring이 **알아서 객체를 만들고 연결**해준다.

---

## 22.6 주요 어노테이션

| 어노테이션 | 역할 |
|---|---|
| `@SpringBootApplication` | 시작점 |
| `@RestController` | REST API 컨트롤러 |
| `@Service` | 비즈니스 로직 |
| `@Repository` | DB 접근 |
| `@GetMapping` | GET 요청 처리 |
| `@PostMapping` | POST 요청 처리 |
| `@Autowired` | 의존성 자동 주입 |

---

## 22.7 실행 방법

```bash
# Gradle
./gradlew bootRun

# Maven
./mvnw spring-boot:run
```

> 내장 톰캣 서버가 포함되어 있어서 별도 서버 설치 불필요.
