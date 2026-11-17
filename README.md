# 자바 튜토리얼

---

## 목차

### 🟢 초급

| 파트 | 제목 |
|---|---|
| [Part 0](part0_환경세팅.md) | 환경 세팅 |
| [Part 1](part1_입출력과변수.md) | 입출력과 변수 |
| [Part 2](part2_연산자.md) | 연산자 |
| [Part 3](part3_조건문.md) | 조건문 |
| [Part 4](part4_반복문.md) | 반복문 |
| [Part 5](part5_배열과문자열.md) | 배열과 문자열 |
| [Part 6](part6_메소드.md) | 메소드 |

### 🔵 심화

| 파트 | 제목 |
|---|---|
| [Part 7](part7_객체지향기초.md) | 객체지향 기초 |
| [Part 8](part8_상속과다형성.md) | 상속과 다형성 |
| [Part 9](part9_인터페이스와추상클래스.md) | 인터페이스와 추상클래스 |
| [Part 10](part10_컬렉션.md) | 컬렉션 프레임워크 |
| [Part 11](part11_예외처리.md) | 예외 처리 |
| [Part 12](part12_제네릭.md) | 제네릭 |
| [Part 13](part13_람다와스트림.md) | 람다와 스트림 |
| [Part 14](part14_파일입출력.md) | 파일 입출력 |
| [Part 15](part15_Enum과어노테이션.md) | Enum과 어노테이션 |
| [Part 16](part16_멀티스레드기초.md) | 멀티스레드 기초 |
| [Part 17](part17_메모리구조.md) | Java 메모리 구조 |
| [Part 18](part18_Optional과Null처리.md) | Optional과 Null 처리 |
| [Part 19](part19_디자인패턴기초.md) | 디자인 패턴 기초 |

### 🟠 실무

| 파트 | 제목 |
|---|---|
| [Part 20](part20_빌드도구.md) | 빌드 도구 (Maven/Gradle) |
| [Part 21](part21_JDBC와데이터베이스.md) | JDBC와 데이터베이스 |
| [Part 22](part22_SpringBoot입문.md) | Spring Boot 입문 |
| [Part 23](part23_RestAPI.md) | REST API 만들기 |
| [Part 24](part24_테스트.md) | 테스트 (JUnit 5) |
| [Part 25](part25_코딩컨벤션과실무팁.md) | 코딩 컨벤션과 실무 팁 |
| [Part 26](part26_최신Java문법.md) | 최신 Java 문법 |
| [Part 27](part27_디버깅테크닉.md) | 디버깅 테크닉 |
| [Part 28](part28_Docker기초.md) | Docker 기초 |

### 📋 마무리

| 파트 | 제목 |
|---|---|
| [Part 29](part29_요약.md) | 요약 |
| [Part 30](part30_연습문제.md) | 연습문제 |

### 🌐 네트워크 확장

| 파트 | 제목 |
|---|---|
| [Part 31](part31_소켓프로그래밍과통신.md) | 소켓 프로그래밍과 통신 (TCP/UDP, 멀티클라이언트) |

> Part 11 예외 처리 → Part 14 입출력 → Part 16 멀티스레드를 학습한 뒤 Part 31로 진행하면 좋다.

### 🧩 객체·데이터·설계 심화

| 파트 | 제목 |
|---|---|
| [Part 32](part32_Object와객체비교.md) | Object와 객체 비교 |
| [Part 33](part33_불변객체와객체복사.md) | 불변 객체와 객체 복사 |
| [Part 34](part34_날짜시간과금액처리.md) | 날짜·시간과 금액 처리 |
| [Part 35](part35_HTTP클라이언트와JSON.md) | HTTP 클라이언트와 JSON |
| [Part 36](part36_객체지향설계와리팩토링.md) | 객체지향 설계와 리팩토링 |

## 실행하면서 공부하기

Part 10·16·24와 Part 32~36은 [실행 예제 안내](examples/README.md), 완성된 `.java` 소스, [예상 출력](examples/expected)을 함께 제공한다.

```bash
# 저장소 루트에서, JDK 17 이상
bash examples/run-core.sh 32
```

- 객체·컬렉션: Part 7·8·9 → Part 10 → Part 32·33
- 시간·데이터: Part 11·26 → Part 34·35
- 동시성: Part 16 → Part 31 → Part 24의 동시 주문 테스트
- 설계: Part 19·24 → Part 36

문서의 코드 조각과 실행용 전체 소스를 구분하고, 실행 전 결과를 예상한 뒤 실제 출력과 비교해 보자. HTTP·JUnit 예제의 Gradle 실행법과 검증 범위는 실행 안내에 정리했다.
