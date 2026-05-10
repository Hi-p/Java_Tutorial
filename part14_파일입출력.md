# Part 14. 파일 입출력

> 프로그램 종료 후에도 데이터를 **파일에 저장**하고 **다시 불러오는** 방법.

---

## 14.1 파일 쓰기 — PrintWriter

```java
import java.io.PrintWriter;
import java.io.FileWriter;

try (PrintWriter pw = new PrintWriter(new FileWriter("output.txt"))) {
    pw.println("첫 번째 줄");
    pw.println("두 번째 줄");
    pw.printf("점수: %d%n", 95);
} catch (Exception e) {
    e.printStackTrace();
}
```

> `try-with-resources`: `try (자원)` 형태로 쓰면 **자동으로 닫힘** (close 호출 불필요).

---

## 14.2 파일 읽기 — BufferedReader

```java
import java.io.BufferedReader;
import java.io.FileReader;

try (BufferedReader br = new BufferedReader(new FileReader("output.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## 14.3 간편한 방법 — Files (Java 7+)

```java
import java.nio.file.Files;
import java.nio.file.Path;

// 전체 읽기
String content = Files.readString(Path.of("output.txt"));

// 줄 단위 읽기
List<String> lines = Files.readAllLines(Path.of("output.txt"));

// 쓰기
Files.writeString(Path.of("hello.txt"), "안녕하세요!");
```

> 짧은 파일에는 `Files` 클래스가 가장 편하다.

---

## 14.4 Scanner로 파일 읽기

```java
Scanner sc = new Scanner(new File("data.txt"));
while (sc.hasNextLine()) {
    System.out.println(sc.nextLine());
}
sc.close();
```

---

## 14.5 자주 빠지는 함정 ⚠️

1. **파일 경로** — 상대 경로는 프로젝트 루트 기준. 절대 경로가 더 안전
2. **인코딩** — 한글 깨지면 `new FileReader("file.txt", StandardCharsets.UTF_8)` 사용
3. **파일 닫기** — `try-with-resources` 쓰면 자동 처리. 안 쓰면 반드시 `close()` 호출
4. **FileNotFoundException** — 없는 파일 읽으려 하면 예외. 존재 여부 먼저 확인
