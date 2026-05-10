# Part 0. 자바 설치

> 코드를 실행하려면 컴퓨터에 **JDK(Java Development Kit)**가 깔려 있어야 한다.

---

# JDK란?

- **JDK** = 자바 개발 도구 모음 (컴파일러 + 실행환경)
- **JRE** = 실행만 가능 (개발 불가). 요즘은 JDK만 설치하면 JRE 포함됨.
- 버전은 **JDK 17** 이상 추천 (LTS = 장기 지원 버전)

---

# 윈도우 설치

## 다운로드

- [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
- 또는 무료 배포판: [https://adoptium.net/](https://adoptium.net/) (Eclipse Temurin)
- Windows → x64 Installer (.msi) 다운로드

## 설치

다운로드한 `.msi` 파일 실행 → Next → Next → Install.

## 환경 변수 설정

> **가장 중요한 단계.** 이거 안 하면 cmd에서 자바를 못 찾는다.

1. **시작 → "환경 변수" 검색 → 시스템 환경 변수 편집**
2. **시스템 변수 → 새로 만들기**
   - 변수 이름: `JAVA_HOME`
   - 변수 값: `C:\Program Files\Java\jdk-17` (설치 경로)
3. **시스템 변수 → Path → 편집 → 새로 만들기**
   - `%JAVA_HOME%\bin` 추가
4. 확인 → 확인 → 확인

## 설치 확인

```
java --version
javac --version
```

`java 17.x.x` 같이 버전이 뜨면 성공.

---

# 맥 설치

## Homebrew로 설치 (추천)

```
brew install openjdk@17
```

### 심볼릭 링크 설정
```
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

## 설치 확인

```
java --version
javac --version
```

`openjdk 17.x.x` 같이 뜨면 성공.

> Mac은 Homebrew가 없으면 먼저 설치: [https://brew.sh/](https://brew.sh/)

---

## 설치 안 될 때

| 증상 | 원인 / 해결 |
|---|---|
| `java은(는) 내부 또는 외부 명령이 아닙니다` (Windows) | 환경 변수 `PATH` 설정 안 됨 → JAVA_HOME, PATH 재설정 |
| `command not found: java` (Mac) | 심볼릭 링크 미설정 또는 JDK 미설치 |
| `javac`은 되는데 `java`가 안 됨 | JRE 경로 문제 → JAVA_HOME 확인 |
| 설치 중 권한 오류 | 관리자 권한으로 실행 / sudo 사용 |

---

# IDE (개발 도구) 설정

자바는 메모장으로도 코딩할 수 있지만, IDE를 쓰면 생산성이 10배는 올라간다.

## 추천 1: IntelliJ IDEA (가장 많이 쓰임)

- [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
- **Community 에디션** = 무료. 학습에 충분.
- 설치 후 → New Project → Java → JDK 17 선택 → Create

## 추천 2: VS Code

- [https://code.visualstudio.com/](https://code.visualstudio.com/)
- 확장 설치: **Extension Pack for Java** (Microsoft)
- 가벼움. 이미 쓰고 있다면 이걸로.

## 추천 3: Eclipse

- [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)
- 오래된 IDE. 대학교에서 많이 사용.

> 처음이라면 **IntelliJ Community**를 추천한다. 자동완성, 에러 표시, 리팩토링 전부 최고 수준.

---

# 첫 번째 프로그램: Hello World

## 1. 파일 만들기

파일명: `Main.java` (대소문자 정확히!)

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

## 2. 컴파일 & 실행

### IDE에서 (추천)
- IntelliJ: 초록색 ▶ 버튼 클릭
- VS Code: 우측 상단 ▶ 버튼 클릭

### 터미널/cmd에서
```
javac Main.java
java Main
```

```
Hello, World!
```

## 3. 구조 이해

```java
public class Main {                    // 클래스 선언 (파일명과 같아야 함)
    public static void main(String[] args) {  // 프로그램 시작점
        System.out.println("Hello, World!");   // 출력
    }
}
```

| 요소 | 의미 |
|---|---|
| `public class Main` | 클래스 이름. **파일명과 동일**해야 함 |
| `public static void main(String[] args)` | 프로그램의 **시작점** (엔트리 포인트) |
| `System.out.println()` | 화면에 출력하고 줄 바꿈 |
| `{ }` | 코드 블록의 시작과 끝 |
| `;` | 문장의 끝. **빠뜨리면 에러** |

---

## 주의사항

- **파일명 = 클래스명**: `Main.java` 안에는 반드시 `public class Main`
- **대소문자 구별**: `main`과 `Main`은 다르다
- **세미콜론 필수**: 모든 문장 끝에 `;` 붙이기. 파이썬과 가장 큰 차이 중 하나.
- **중괄호 `{}`**: 파이썬은 들여쓰기로 블록을 구분하지만, 자바는 `{}`로 구분한다

> 💡 **파이썬과의 차이**: 파이썬은 `print("hello")` 한 줄이면 되지만, 자바는 클래스와 main 메소드 안에 코드를 써야 한다. 처음엔 번거롭지만 프로젝트가 커지면 이 구조가 도움이 된다.
