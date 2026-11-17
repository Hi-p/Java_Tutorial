# 실행 예제와 예상 결과

Part 10·16·24 보강과 Part 32~36의 개념을 직접 실행하는 학습 프로젝트다. 모든 Java 소스는 **JDK 17 API 기준**이다. 문서 속 짧은 코드는 개념 설명용 조각일 수 있으며, `src`에는 import·클래스·실행 진입점 또는 테스트를 포함한 완성 파일을 제공한다.

파트별 패키지가 달라 같은 `Product` 이름을 사용해도 충돌하지 않는다. 각 예제는 같은 상품·주문 소재로 서로 다른 개념을 설명하며, 하나의 완성된 주문 서비스 전체를 의미하지 않는다.

## 1. JDK만으로 시작하기

터미널을 저장소 루트에서 열고 실행한다.

```bash
java -version
javac -version
bash examples/run-core.sh 32
```

다른 번호는 `10`, `16`, `24`, `33`, `34`, `36`, 취소 예제는 `cancellation`을 지정한다. `all`은 외부 라이브러리가 필요 없는 예제 8개를 순서대로 실행한다. 컴파일 결과는 `examples/build/core/classes`에 생성된다.

```bash
bash examples/run-core.sh all
```

macOS·Linux의 Bash용 스크립트다. Windows에서는 Git Bash를 사용하거나 아래 Gradle 작업을 사용한다. 파일을 UTF-8로 저장하고 터미널 인코딩도 UTF-8로 맞춘다.

직접 컴파일하는 예:

```bash
cd examples
javac -encoding UTF-8 --release 17 -d build/manual src/main/java/tutorial/part32/*.java
java -cp build/manual tutorial.part32.EqualityExample
```

## 2. Gradle로 JSON 예제·JUnit까지 실행하기

JDK 17 이상과 **Gradle 9.x**를 준비한다. 이 프로젝트에는 Gradle Wrapper를 포함하지 않았으므로 명령은 `./gradlew`가 아니라 `gradle`이다. 처음 실행할 때 Gradle이 Maven Central에서 의존성을 다운로드하므로 인터넷이 필요하다. `gradle` 명령이 없으면 [공식 설치 안내](https://docs.gradle.org/current/userguide/installation.html)를 따른다.

```bash
cd examples
gradle --version
gradle -q runPart35
gradle test
```

의존성은 [build.gradle](build.gradle)에 고정되어 있다.

| 용도 | 의존성 | 버전 |
|---|---|---|
| JSON 변환 | Jackson Databind | 2.19.1 |
| 테스트 API·매개변수화·실행 엔진 | JUnit Jupiter | 5.10.1 |
| 테스트 실행 지원 | JUnit Platform Launcher | 1.10.1 |

최신 버전 소개가 아니라 이 예제의 재현 기준이다. 다른 메이저 버전의 import·설정을 혼용하지 않는다. JDK 버전은 `sourceCompatibility`와 `--release 17`로 제한하며, 빌드를 실행하는 JVM은 17 이상이어야 한다.

## 3. 파트별 실행·예상 결과

아래 Gradle 명령은 모두 `examples` 폴더 기준이다. `-q`는 Gradle 안내 출력을 줄인다. 표의 예상 결과 파일은 프로그램 자체의 표준 출력이며 빌드 로그·실행 시간은 포함하지 않는다.

| 내용 | 실행 파일 | Gradle 작업 | 예상 출력 |
|---|---|---|---|
| 컬렉션·정렬 | [CollectionsExample.java](src/main/java/tutorial/part10/CollectionsExample.java) | `gradle -q runPart10` | [part10.txt](expected/part10.txt) |
| 동시 집계·Future | [ConcurrencyExample.java](src/main/java/tutorial/part16/ConcurrencyExample.java) | `gradle -q runPart16` | [part16.txt](expected/part16.txt) |
| 협력적 작업 취소 | [CancellationExample.java](src/main/java/tutorial/part16/CancellationExample.java) | `gradle -q runCancellation` | [cancellation.txt](expected/cancellation.txt) |
| 재고 업무 규칙 | [StockExample.java](src/main/java/tutorial/part24/StockExample.java) | `gradle -q runPart24` | [part24.txt](expected/part24.txt) |
| 객체 비교·해시 | [EqualityExample.java](src/main/java/tutorial/part32/EqualityExample.java) | `gradle -q runPart32` | [part32.txt](expected/part32.txt) |
| 복사·불변 스냅샷 | [ImmutabilityExample.java](src/main/java/tutorial/part33/ImmutabilityExample.java) | `gradle -q runPart33` | [part33.txt](expected/part33.txt) |
| 시간·할인 계산 | [DateMoneyExample.java](src/main/java/tutorial/part34/DateMoneyExample.java) | `gradle -q runPart34` | [part34.txt](expected/part34.txt) |
| JSON 왕복 변환 | [JsonExample.java](src/main/java/tutorial/part35/JsonExample.java) | `gradle -q runPart35` | [part35.txt](expected/part35.txt) |
| 책임 분리·DI | [RefactoringExample.java](src/main/java/tutorial/part36/RefactoringExample.java) | `gradle -q runPart36` | [part36.txt](expected/part36.txt) |

날짜·시간 예제는 고정 시각을 사용하고, 동시성 예제는 작업 완료 후 집계하므로 출력 값을 비교할 수 있다. `HashMap`·`HashSet`의 임의 순서나 객체의 기본 해시값 같은 불안정한 출력은 예상 결과에 사용하지 않는다.

예상 결과와 자동 비교하려면 저장소 루트에서 실행한다.

```bash
bash examples/run-core.sh 32 > /tmp/java-tutorial-part32.txt
diff -u examples/expected/part32.txt /tmp/java-tutorial-part32.txt
```

차이가 없으면 `diff`는 아무 내용도 출력하지 않고 종료한다.

## 4. HTTP 통신은 터미널 두 개에서 실행

터미널 1과 2 모두 `examples` 폴더에서 실행한다.

```bash
# 터미널 1
gradle -q runHttpServer
```

```bash
# 터미널 2
gradle -q runHttpClient
```

클라이언트 예상 출력: [part35-http.txt](expected/part35-http.txt). 서버는 `127.0.0.1:18080`을 사용한다. 포트를 이미 사용 중이면 해당 서버를 확인하고 중복 실행을 종료한다. 서버 종료는 `Ctrl+C`다. Gradle은 실행 상황에 따라 별도 데몬을 사용할 수 있다.

```bash
# 실패 상황 실습: HTTP 오류 404를 포함한 예외와 Gradle 작업 실패가 예상됨
gradle -q runHttpClient -Purl=http://127.0.0.1:18080/missing
```

HTTP 예제는 기본적으로 자기 컴퓨터의 학습 서버에만 요청하며 API 키가 필요 없다.

## 5. JUnit 테스트

| 범위 | 실행되는 테스트 수 | 확인할 내용 |
|---|---:|---|
| 기본 `gradle test` | 36 | 경계값, 잘못된 입력, 객체 비교·복사, 시간·금액, JSON, 동시 주문, 서비스 조합 |
| `gradle test -PnetworkTests --rerun-tasks` | 38 | 위 36개 + HTTP 200·404 통합 테스트 2개 |

매개변수화 테스트의 각 입력도 하나의 테스트 실행으로 계산했다. `network` 태그의 테스트는 기본 명령에서 제외한다. HTTP 테스트는 빈 포트를 자동으로 배정받고 종료 시 서버를 닫는다.

```bash
gradle test --tests tutorial.part24.StockServiceTest
gradle test --tests 'tutorial.part36.*'
```

실행 뒤 `build/reports/tests/test/index.html`에서 보고서를 열 수 있다. 실행 순서와 시간은 달라질 수 있다. 성공한 테스트도 요구사항 전체의 정확성을 증명하는 것은 아니므로 테스트가 다루는 범위를 함께 살펴본다.

## 6. 작성 시 검증 범위

- JDK 25 런타임에서 `javac --release 17`로 Java 파일 29개를 컴파일했다.
- 실행 예제 9개와 실제 로컬 HTTP 조회 예제의 출력까지, 예상 출력 파일 10개와 모두 일치함을 확인했다.
- JUnit 5.10.1 엔진으로 기본 테스트 36개가 통과했고, HTTP 200·404 통합 테스트를 포함한 38개도 통과했다.
- Gradle 9.6.0으로 컴파일, `test -PnetworkTests`, `runPart35`를 실행해 성공했다. 의존성은 준비된 캐시를 이용해 오프라인으로 해석했다.
- JDK 17 API 호환 컴파일은 확인했지만, JDK 17 런타임 자체에서 별도로 실행한 것은 아니다. 실행 검증은 JDK 25에서 진행했다.

[문서 목차로 돌아가기](../README.md)
