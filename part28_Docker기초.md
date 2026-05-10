# Part 28. Docker 기초

> 자바 앱을 **어디서든 똑같이** 실행할 수 있게 패키징하는 컨테이너 기술.

---

## 28.1 왜 Docker?

| Docker 없이 | Docker 사용 |
|---|---|
| "내 컴퓨터에서는 되는데..." | 어디서든 동일하게 실행 |
| JDK 버전 충돌 | 컨테이너 안에 JDK 포함 |
| 서버마다 환경 설정 반복 | Dockerfile 하나로 자동화 |

---

## 28.2 핵심 개념

| 용어 | 설명 |
|---|---|
| **이미지** | 실행 환경의 **스냅샷** (설계도) |
| **컨테이너** | 이미지로 만든 **실행 인스턴스** (실체) |
| **Dockerfile** | 이미지를 만드는 **스크립트** |
| **Docker Hub** | 이미지 저장소 (공개 레지스트리) |

> 이미지 = 붕어빵 틀, 컨테이너 = 붕어빵 (클래스와 객체 관계와 비슷!)

---

## 28.3 Dockerfile — 자바 앱

```dockerfile
# 1단계: 빌드
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# 2단계: 실행 (멀티스테이지 빌드)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 28.4 기본 명령어

```bash
# 이미지 빌드
docker build -t my-java-app .

# 컨테이너 실행
docker run -p 8080:8080 my-java-app

# 실행 중인 컨테이너 확인
docker ps

# 컨테이너 중지
docker stop <컨테이너ID>

# 이미지 목록
docker images
```

---

## 28.5 docker-compose — 여러 컨테이너 관리

```yaml
# docker-compose.yml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
  
  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_PASSWORD=1234
    ports:
      - "5432:5432"
```

```bash
# 전체 실행
docker-compose up -d

# 전체 중지
docker-compose down
```

> 앱 + DB를 **한 번에** 실행/중지.

---

## 28.6 자주 빠지는 함정 ⚠️

1. **이미지 크기** — JDK 대신 JRE 사용, 멀티스테이지 빌드로 줄이기
2. **포트 매핑** — `-p 호스트포트:컨테이너포트` 순서 주의
3. **.dockerignore** — `build/`, `.gradle/`, `.git/` 등 제외하여 빌드 속도 향상
4. **환경 변수** — 비밀번호 등은 코드에 넣지 말고 환경 변수로 전달
