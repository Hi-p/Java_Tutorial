# Part 20. 빌드 도구 (Maven / Gradle)

> 라이브러리 관리, 빌드, 테스트를 **자동화**하는 도구. 현업 프로젝트의 기본.

---

## 20.1 왜 빌드 도구?

| 빌드 도구 없이 | 빌드 도구 사용 |
|---|---|
| jar 파일 직접 다운로드 | 한 줄로 의존성 추가 |
| 컴파일 명령어 직접 입력 | `mvn build` / `gradle build` 한 방 |
| 버전 충돌 직접 해결 | 자동 의존성 관리 |

---

## 20.2 Maven

**`pom.xml`** — 프로젝트 설정 파일

```xml
<project>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>
</project>
```

**주요 명령어**

| 명령어 | 설명 |
|---|---|
| `mvn compile` | 컴파일 |
| `mvn test` | 테스트 실행 |
| `mvn package` | jar 파일 생성 |
| `mvn clean` | 빌드 결과물 삭제 |

---

## 20.3 Gradle (최근 트렌드)

**`build.gradle`** — 더 간결한 설정

```groovy
plugins {
    id 'java'
}

dependencies {
    implementation 'com.google.code.gson:gson:2.10.1'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}
```

**주요 명령어**

| 명령어 | 설명 |
|---|---|
| `gradle build` | 빌드 |
| `gradle test` | 테스트 |
| `gradle run` | 실행 |
| `gradle clean` | 정리 |

---

## 20.4 Maven vs Gradle

| | Maven | Gradle |
|---|---|---|
| 설정 파일 | XML (`pom.xml`) | Groovy/Kotlin (`build.gradle`) |
| 속도 | 느림 | 빠름 (캐싱, 증분 빌드) |
| 유연성 | 제한적 | 스크립트 자유도 높음 |
| 트렌드 | 레거시 프로젝트 | 신규 프로젝트 (Spring Boot 기본) |

> **신규 프로젝트라면 Gradle 추천.** 기존 프로젝트는 Maven인 경우도 많음.

---

## 20.5 의존성 관리

**Maven Central** ([https://search.maven.org](https://search.maven.org))에서 필요한 라이브러리를 검색하고, 복사-붙여넣기하면 끝.

```
원하는 라이브러리 검색 → Gradle/Maven 탭 → 코드 복사 → build.gradle에 붙여넣기
```
