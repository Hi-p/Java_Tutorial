# Part 35. HTTP 클라이언트와 JSON

> Java 프로그램에서 **HTTP 요청을 보내고, 상태와 본문을 확인한 뒤 JSON을 객체로 변환**한다.

선행 학습: [Part 11 예외](part11_예외처리.md), [Part 23 REST API](part23_RestAPI.md), [Part 31 소켓](part31_소켓프로그래밍과통신.md)

이 파트의 HTTP 요청에는 JDK의 `HttpClient`, JSON 변환에는 **Jackson 2.19.1**을 사용한다. 외부 API 키 없이 로컬에서 연습할 수 있다. Jackson은 Java 표준 라이브러리가 아니므로 [Gradle 설정](examples/build.gradle)에 의존성을 선언했다.

---

## 35.1 제공하는 API와 호출하는 코드

| 역할 | 주요 코드 | 학습 위치 |
|---|---|---|
| API 서버 | 요청을 받아 업무 처리 후 응답 | Part 23 |
| HTTP 클라이언트 | 요청을 보내 응답을 사용 | 이 파트 |
| 직접 만든 소켓 프로토콜 | 바이트 흐름과 메시지 경계를 직접 처리 | Part 31 |

HTTP 클라이언트는 소켓에 임의 문자열을 보내는 에코 클라이언트와 다르다. 메소드·주소·헤더·본문 등 HTTP의 규칙을 사용한다.

| 요소 | 예 | 의미 |
|---|---|---|
| 메소드 | `GET`, `POST` | 요청의 목적 |
| URI | `/products/P001` | 요청할 자원 |
| 요청 헤더 | `Accept: application/json` | 받고 싶은 표현 형식 |
| 본문 형식 헤더 | `Content-Type: application/json` | 보내는 본문의 형식 |
| 응답 상태 | 200, 404, 500 | 요청 처리 결과 |

네트워크 연결에 성공했더라도 HTTP 404·500일 수 있다. 성공 응답이어도 본문이 비어 있거나 기대한 JSON 구조가 아닐 수 있다.

---

## 35.2 JSON을 DTO로 변환하기

```json
{"id":"P001","name":"노트","price":1200.00}
```

```java
public record ProductData(String id, String name, BigDecimal price) { }
```

DTO는 이 프로그램이 주고받을 데이터 구조다. 실제 [ProductData.java](examples/src/main/java/tutorial/part35/ProductData.java)는 생성자에서 필수값·빈 문자열·음수 가격을 검사한다.

[JsonCodec.java](examples/src/main/java/tutorial/part35/JsonCodec.java)의 핵심은 다음과 같다.

```java
ProductData product = mapper.readValue(json, ProductData.class);
String encoded = mapper.writeValueAsString(product);
```

JSON은 쉼표로 단순 `split()`하거나 문자열 덧셈으로 직접 조립하지 않는다. 따옴표·이스케이프·배열·중첩 구조 때문에 파서를 사용해야 한다.

이 예제는 Jackson 2 계열의 `com.fasterxml.jackson.*` 패키지를 사용한다. 다른 메이저 버전의 예제를 섞어 사용하지 않는다. 공유 `ObjectMapper`는 초기 설정 후 사용하며, 요청마다 설정을 바꾸지 않는다.

---

## 35.3 HTTP GET 요청과 오류 처리

전체 코드: [HttpJsonExample.java](examples/src/main/java/tutorial/part35/HttpJsonExample.java)

```java
HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();
HttpRequest request = HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(3))
        .header("Accept", "application/json")
        .GET().build();
HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
```

처리 순서:

1. 주소·연결 타임아웃·요청 타임아웃을 설정한다.
2. 요청을 보내고 응답을 받는다.
3. 상태 코드가 허용한 성공 범위인지 확인한다.
4. `Content-Type`이 기대한 형식인지 확인한다.
5. JSON 구문과 필수 필드·업무 값을 검증한다.

예제는 2xx 응답과 `application/json`만 허용한다. 상태 오류는 `IOException`, 잘못된 JSON은 Jackson의 예외, DTO 값 검증 실패는 변환 예외의 원인 등으로 드러난다. 헤더가 맞다고 JSON이 올바르다는 보장은 없다.

`send()`의 `InterruptedException`은 호출자에게 전파하거나 인터럽트 상태를 복구한 뒤 작업을 종료하는 등 정책을 정한다. 실제 애플리케이션에서는 목적에 맞는 수명으로 `HttpClient`를 재사용할 수 있다. 여기의 단발 호출 예제는 메소드 안에서 만든다.

---

## 35.4 POST, 쿼리 파라미터, 비동기 처리

다음은 요청을 만드는 방식의 예이며, 제공한 로컬 서버에는 POST 주문 처리 기능이 없다.

```java
String json = mapper.writeValueAsString(product);
HttpRequest request = HttpRequest.newBuilder(uri)
        .timeout(Duration.ofSeconds(3))
        .header("Content-Type", "application/json; charset=UTF-8")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
        .build();
```

검색어처럼 쿼리 값에 한글·공백·`&`가 있으면 적절히 인코딩한다. `URLEncoder`는 폼 형식의 인코딩이며 값 부분에 사용할 수 있다. 전체 URL이나 경로를 통째로 인코딩하는 방법과 구분한다. API가 정한 인코딩 규칙을 따른다.

```java
CompletableFuture<HttpResponse<String>> pending = client.sendAsync(
        request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
```

비동기 호출에서도 상태 코드와 JSON 검증은 동일하게 필요하다. `thenApply()`로 응답을 처리하고 실패 경로도 연결한다. 세부적인 실행·대기는 [Part 16](part16_멀티스레드기초.md) 참고.

---

## 35.5 인터넷 없이 JSON 변환 연습

저장소 루트에서 아래 명령을 실행한다. **처음에는 Gradle 의존성 다운로드에 인터넷이 필요**하다. 의존성이 준비된 후 JSON 예제 자체는 HTTP 요청을 보내지 않는다.

```bash
cd examples
gradle -q runPart35
```

전체 코드: [JsonExample.java](examples/src/main/java/tutorial/part35/JsonExample.java)

```text
상품: P001 / 노트
가격: 1200.00
JSON 왕복 변환: true
```

JSON 필드 순서는 의미가 없으므로 출력 문자열의 순서 대신 객체로 다시 변환한 값이 같은지 확인한다.

---

## 35.6 로컬 HTTP 통신 실습

[JDK 내장 HTTP 서버 예제](examples/src/main/java/tutorial/part35/HttpFixtureServer.java)를 사용한다. JDK의 `jdk.httpserver` 모듈에 포함된 학습용 서버이며 운영 서비스 구조를 갖춘 것은 아니다.

터미널 1 — `examples` 폴더:

```bash
gradle -q runHttpServer
```

```text
HTTP 서버: http://127.0.0.1:18080 (종료: Ctrl+C)
```

터미널 2 — 동일한 `examples` 폴더:

```bash
gradle -q runHttpClient
```

```text
HTTP 조회: P001 / 노트
가격: 1200.00
```

404도 확인해 보자.

```bash
gradle -q runHttpClient -Purl=http://127.0.0.1:18080/missing
```

이 명령은 의도적으로 실패하며 예외에 `HTTP 오류: 404`가 표시된다. Gradle 작업도 실패로 끝난다. 서버 종료는 터미널 1에서 `Ctrl+C`를 누른다.

사용자 컴퓨터의 인터넷 API에 적용할 때는 응답 DTO, 인증, 호출 제한, 오류 정책을 해당 서비스 규격에 맞춘다. 어떤 URL을 넣어도 이 상품 DTO로 해석할 수 있는 것은 아니다.

---

## 35.7 테스트와 주의점

- JSON 테스트: [JsonCodecTest.java](examples/src/test/java/tutorial/part35/JsonCodecTest.java)
- HTTP 테스트: [HttpJsonIntegrationTest.java](examples/src/test/java/tutorial/part35/HttpJsonIntegrationTest.java)

```bash
cd examples
gradle test --tests tutorial.part35.JsonCodecTest
gradle test -PnetworkTests --tests tutorial.part35.HttpJsonIntegrationTest
```

HTTP 테스트는 빈 로컬 포트를 자동 배정받고 테스트 후 서버를 닫는다. 네트워크·로컬 포트 제한이 있는 환경에서는 실행할 수 없다.

| 상황 | 고려할 내용 |
|---|---|
| 요청 시간 초과 | 서버가 처리했는지는 별개. 변경 요청을 무조건 재시도하지 않기 |
| 인증 정보 | 코드·공개 저장소에 키를 적지 않고 실행 환경에서 주입 |
| 응답 형식 변경 | 필수 필드·알 수 없는 필드의 허용 정책 결정 |
| 큰 응답 | 문자열 전체를 메모리에 받는 예제의 한계. 크기 제한·스트리밍 고려 |
| 같은 요청의 중복 실행 | 업무 요청 ID·멱등성 정책 검토 |

현재 JSON 설정은 **알 수 없는 필드를 오류로 처리**한다. 외부 API에서 필드 추가를 허용하려면 허용 정책을 명시적으로 바꾸고 테스트한다. 누락·null·음수 가격은 별도 검증한다.

---

## 35.8 연습문제

1. 로컬 서버의 상품명을 따옴표가 들어간 한글 문자열로 바꾸고 왕복 변환을 확인하자.
2. HTTP 200이지만 JSON이 잘못된 응답을 만들어 실패 경로를 테스트하자.
3. 요청 타임아웃이 주문 미처리를 보장하지 않는 이유는?
4. 상품 배열 응답을 받는 DTO 또는 타입 정보를 설계해 보자.

<details>
<summary>풀이 방향</summary>

1. 수작업 이스케이프 대신 `JsonCodec.writeProduct()`를 사용한다.
2. 상태 코드 검사와 JSON 파싱 검사가 별개임을 확인한다.
3. 서버는 이미 처리했지만 응답 전달이 지연·실패했을 수 있다.
4. 배열이면 `ProductData[]`, 컬렉션이면 Jackson의 `TypeReference<List<ProductData>>` 등을 고려한다.

</details>

공식 참고: [HttpClient](https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html), [Jackson Databind](https://github.com/FasterXML/jackson-databind)

[목차](README.md) · [다음: 설계와 리팩토링](part36_객체지향설계와리팩토링.md)
