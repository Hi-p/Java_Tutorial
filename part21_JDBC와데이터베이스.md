# Part 21. JDBC와 데이터베이스

> 자바에서 **데이터베이스에 접속**하고 SQL을 실행하는 표준 API.

---

## 21.1 JDBC 흐름

```
1. 드라이버 로드 → 2. 연결(Connection) → 3. SQL 실행 → 4. 결과 처리 → 5. 자원 해제
```

---

## 21.2 기본 사용법

```java
import java.sql.*;

String url = "jdbc:mysql://localhost:3306/mydb";
String user = "root";
String password = "1234";

try (Connection conn = DriverManager.getConnection(url, user, password);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

    while (rs.next()) {
        System.out.println(rs.getInt("id") + " " + rs.getString("name"));
    }

} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 21.3 PreparedStatement — SQL 인젝션 방지

```java
// 나쁜 예 (SQL 인젝션 취약)
String sql = "SELECT * FROM users WHERE name = '" + name + "'";

// 좋은 예 (PreparedStatement)
String sql = "SELECT * FROM users WHERE name = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, name);  // ?에 값 바인딩
    ResultSet rs = pstmt.executeQuery();
}
```

> ⚠️ **절대 문자열 연결로 SQL을 만들지 말 것.** `PreparedStatement` 필수.

---

## 21.4 CRUD

```java
// INSERT
String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "민수");
pstmt.setInt(2, 17);
pstmt.executeUpdate();

// UPDATE
String sql = "UPDATE users SET age = ? WHERE name = ?";

// DELETE
String sql = "DELETE FROM users WHERE id = ?";
```

---

## 21.5 트랜잭션

```java
conn.setAutoCommit(false);  // 자동 커밋 끄기
try {
    // 여러 SQL 실행
    stmt.executeUpdate("UPDATE accounts SET balance = balance - 1000 WHERE id = 1");
    stmt.executeUpdate("UPDATE accounts SET balance = balance + 1000 WHERE id = 2");
    conn.commit();  // 전부 성공하면 확정
} catch (Exception e) {
    conn.rollback();  // 하나라도 실패하면 원복
}
```

---

## 21.6 의존성 추가 (Gradle)

```groovy
dependencies {
    implementation 'mysql:mysql-connector-java:8.0.33'
    // 또는 PostgreSQL
    // implementation 'org.postgresql:postgresql:42.6.0'
}
```

---

## 21.7 자주 빠지는 함정 ⚠️

1. **자원 닫기** — Connection, Statement, ResultSet 반드시 close. `try-with-resources` 사용
2. **SQL 인젝션** — 문자열 연결 절대 금지. `PreparedStatement` 필수
3. **커넥션 풀** — 실무에서는 매번 new Connection이 아닌 HikariCP 같은 풀 사용
