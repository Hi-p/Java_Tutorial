# Part 23. REST API 만들기

> 웹/앱 서비스의 **백엔드 API**를 만드는 방법. Spring Boot로 CRUD API 구현.

---

## 23.1 REST API란?

| HTTP 메소드 | 용도 | 예시 |
|---|---|---|
| `GET` | 조회 | `GET /users` → 사용자 목록 |
| `POST` | 생성 | `POST /users` → 새 사용자 등록 |
| `PUT` | 수정 | `PUT /users/1` → 1번 사용자 수정 |
| `DELETE` | 삭제 | `DELETE /users/1` → 1번 사용자 삭제 |

---

## 23.2 Entity (데이터 모델)

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    // 생성자, getter, setter
}
```

---

## 23.3 Repository (DB 접근)

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // 기본 CRUD 자동 제공!
}
```

---

## 23.4 Controller (API 엔드포인트)

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 전체 조회
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 생성
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 수정
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User updated) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        return userRepository.save(user);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## 23.5 JSON 요청/응답

**요청 (POST /api/users)**
```json
{
    "name": "민수",
    "email": "minsu@example.com"
}
```

**응답**
```json
{
    "id": 1,
    "name": "민수",
    "email": "minsu@example.com"
}
```

> Spring Boot는 Java 객체 ↔ JSON 변환을 **자동으로** 해준다 (Jackson 라이브러리).

---

## 23.6 테스트 방법

```bash
# GET
curl http://localhost:8080/api/users

# POST
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"민수","email":"minsu@example.com"}'
```

> 또는 **Postman**, **Insomnia** 같은 GUI 도구 사용.

---

## 23.7 주요 어노테이션 정리

| 어노테이션 | 설명 |
|---|---|
| `@RestController` | JSON 응답 컨트롤러 |
| `@RequestMapping` | 기본 URL 경로 |
| `@GetMapping` / `@PostMapping` | HTTP 메소드 매핑 |
| `@PathVariable` | URL 경로 변수 (`/users/{id}`) |
| `@RequestBody` | 요청 본문 (JSON) → Java 객체 |
| `@RequestParam` | 쿼리 파라미터 (`?name=민수`) |
