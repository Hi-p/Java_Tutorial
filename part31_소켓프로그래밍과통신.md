# Part 31. 소켓 프로그래밍과 통신

> **서버와 클라이언트가 데이터를 주고받는 원리**를 익히고, Java로 TCP/UDP 통신을 직접 구현한다.

**선행 학습:** [예외 처리](part11_예외처리.md), [파일 입출력](part14_파일입출력.md), [멀티스레드](part16_멀티스레드기초.md)

**실습 환경:** JDK 17 이상, 외부 라이브러리 없음. 아래 완성 예제는 표시된 이름의 `.java` 파일로 저장한다. 터미널은 해당 파일들이 있는 폴더에서 연다.

---

## 31.1 통신의 기본 개념

| 개념 | 의미 | 예시 |
|---|---|---|
| IP 주소 | 통신할 호스트의 네트워크 주소 | `127.0.0.1` |
| 포트 | 호스트 안에서 통신 끝점을 구분하는 번호 | 서버의 TCP `5000`번 |
| 소켓(Socket) | 프로그램이 네트워크 데이터를 보내고 받는 끝점 | Java의 `Socket` 객체 |
| 서버 | 특정 주소와 포트에서 요청을 기다리는 역할 | 주문을 처리하는 프로그램 |
| 클라이언트 | 서버로 연결하거나 요청을 보내는 역할 | 주문을 보내는 프로그램 |
| 프로토콜 | 데이터 형식, 의미, 순서 등에 관한 약속 | `QUIT`을 받으면 `BYE` 응답 후 종료 |

- 서버와 클라이언트는 **프로그램의 역할**이다. 한 컴퓨터에서 둘 다 실행할 수 있다.
- `localhost`와 `127.0.0.1`은 **현재 실행 중인 자기 컴퓨터**를 가리킨다.
- 서버 포트를 `5000`으로 정하면 클라이언트는 그 포트로 접속한다. 클라이언트 자신의 포트는 보통 운영체제가 자동 배정한다.
- TCP `5000`과 UDP `5000`은 별개이다. 이 문서에서는 구별하기 쉽게 UDP에 `5001`을 쓴다.

### TCP와 UDP 비교

| 항목 | TCP | UDP |
|---|---|---|
| 연결 | 연결을 맺은 뒤 통신 | TCP와 같은 연결 수립 과정 없음 |
| 데이터 단위 | 연속된 바이트 스트림 | 데이터그램(메시지 단위) |
| 신뢰성 | 순서 유지와 손실 시 재전송 제공. 장애 시 연결 실패 가능 | 전달·순서·중복 방지를 보장하지 않음 |
| 메시지 경계 | 애플리케이션에서 정의해야 함 | 데이터그램 단위의 경계가 있음 |
| Java 클래스 | `ServerSocket`, `Socket` | `DatagramSocket`, `DatagramPacket` |
| 활용 예 | 순서 있는 데이터 전달이 필요한 서비스 | 일부 실시간 서비스, DNS 등 |

> UDP가 항상 빠르거나 TCP가 항상 느린 것은 아니다. 필요한 신뢰성, 지연, 재전송 방식에 따라 선택한다. TCP도 상대 애플리케이션이 **주문 처리를 완료했는지**까지 보장하지는 않는다.

---

## 31.2 TCP 서버와 클라이언트의 동작

1. 서버가 `ServerSocket`을 만들고 주소와 포트에 **바인딩(bind)**한다.
2. 서버가 `accept()`를 호출해 연결을 기다린다.
3. 클라이언트가 `Socket.connect()`로 서버에 연결한다.
4. 서버의 `accept()`가 **해당 클라이언트와 통신할 새로운 `Socket`**을 반환한다.
5. 양쪽이 그 연결의 입력·출력 스트림으로 데이터를 주고받는다.
6. 통신이 끝나면 소켓과 스트림을 닫는다.

**`ServerSocket`은 연결을 받아들이고, `Socket`은 실제 데이터를 주고받는다.** 여러 클라이언트가 접속하면 서버에는 연결별 `Socket`이 생긴다. 연결은 양쪽 IP와 포트의 조합으로 구별된다.

### 블로킹(Blocking)

`accept()`는 연결이 올 때까지, 소켓 읽기는 데이터나 연결 종료를 확인할 때까지 호출한 스레드를 기다리게 할 수 있다. 프로그램이 멈춘 것처럼 보여도 정상적으로 대기 중일 수 있다.

| 호출 | 의미 |
|---|---|
| `socket.connect(address, 3000)` | 연결 수립을 최대 3초 기다림 |
| `socket.setSoTimeout(5000)` | 이후 소켓 입력의 블로킹 읽기 대기를 제한 |
| `serverSocket.setSoTimeout(5000)` | `accept()`의 대기 시간을 제한 |

> 단위는 밀리초다. `Socket`의 읽기 타임아웃은 연결·쓰기·키보드 입력 시간 제한이 아니다. 여러 번 읽어서 만드는 **메시지 전체의 처리 기한**과도 다르다. 예제에서는 읽기 타임아웃이 발생하면 해당 연결을 닫는다.

---

## 31.3 메시지 경계, 인코딩, flush

TCP에서 `write()`를 두 번 했다고 상대의 `read()`가 두 번 반환되는 것은 아니다. 데이터가 합쳐지거나 나뉘어 읽힐 수 있으므로 **어디까지가 메시지 한 개인지** 약속해야 한다.

| 경계 구분 방식 | 예 |
|---|---|
| 구분자 | 한 줄이 메시지 하나: `안녕\n` |
| 길이 접두사 | 먼저 길이 100을 보내고 본문 100바이트를 읽기 |
| 고정 길이 | 매번 정해진 바이트 수만큼 읽기 |

이 문서의 TCP 예제는 다음 규칙을 사용한다.

- 양쪽 모두 **UTF-8**로 문자를 바이트로 바꾸고 다시 해석한다.
- 한 메시지는 한 줄이며, 끝에 줄바꿈을 보낸다. 메시지 본문에 줄바꿈을 넣지 않는다.
- 수신은 `BufferedReader.readLine()`, 송신은 `BufferedWriter.write()` + `newLine()` + `flush()`를 사용한다.
- 일반 문자열에는 `ECHO: 원문`, `QUIT`에는 `BYE`를 응답한다. 클라이언트는 한 요청의 응답을 받은 뒤 다음 요청을 보낸다.

`readLine()`은 줄바꿈을 제외한 문자열을 돌려준다. EOF(입력 스트림 끝) 전에 읽은 문자가 있다면 마지막 줄을 반환할 수 있고, 더 읽을 문자 없이 EOF를 만나면 `null`을 반환한다. **연결을 유지하면서 줄바꿈을 보내지 않으면 계속 기다릴 수 있다.**

`flush()`는 자바 출력 버퍼의 데이터를 아래쪽 스트림으로 밀어낸다. 상대가 받았거나 업무 처리를 끝냈다는 확인은 아니므로, 필요한 경우 응답 메시지로 확인한다.

> `PrintWriter`를 쓴다면 `autoFlush=true`라도 모든 `write()`에서 자동 flush가 되는 것은 아니다. 또 쓰기 오류가 보통 `IOException`으로 직접 전달되지 않아 `checkError()` 확인이 필요하다. 여기서는 오류를 예외로 다루기 쉬운 `BufferedWriter`를 사용한다.

---

## 31.4 TCP 에코 서버와 클라이언트

에코(Echo) 서버는 받은 문자열을 응답으로 돌려주는 연습용 서버다.

### 서버 — `TcpEchoServer.java`

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class TcpEchoServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket()) {
            // 같은 컴퓨터에서만 접속할 수 있도록 바인딩
            server.bind(new InetSocketAddress("127.0.0.1", 5000));
            System.out.println("TCP 서버 대기: 127.0.0.1:5000");
            handle(server.accept());
            // 이 기본 서버는 한 연결을 처리한 뒤 종료한다.
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        }
    }

    // 다음 절의 멀티클라이언트 서버에서도 이 메소드를 사용한다.
    public static void handle(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8))) {
            socket.setSoTimeout(60_000);
            String message;
            while ((message = in.readLine()) != null) {
                if ("QUIT".equals(message)) {
                    out.write("BYE");
                    out.newLine();
                    out.flush();
                    break;
                }
                out.write("ECHO: " + message);
                out.newLine();
                out.flush();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("읽기 시간 초과: 연결 종료");
        } catch (IOException e) {
            System.err.println("클라이언트 통신 오류: " + e.getMessage());
        }
    }
}
```

### 클라이언트 — `TcpEchoClient.java`

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class TcpEchoClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, 5000), 3000);
            socket.setSoTimeout(5000);

            try (BufferedReader keyboard = new BufferedReader(new InputStreamReader(
                         System.in, StandardCharsets.UTF_8));
                 BufferedReader in = new BufferedReader(new InputStreamReader(
                         socket.getInputStream(), StandardCharsets.UTF_8));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                         socket.getOutputStream(), StandardCharsets.UTF_8))) {
                System.out.println("문자열 입력 (종료: QUIT)");
                String message;
                while ((message = keyboard.readLine()) != null) {
                    out.write(message);
                    out.newLine();
                    out.flush();

                    String response = in.readLine();
                    if (response == null) {
                        System.out.println("서버가 연결을 종료했습니다.");
                        break;
                    }
                    System.out.println("서버 응답: " + response);
                    if ("QUIT".equals(message)) {
                        break;
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("연결 또는 응답 대기 시간 초과");
        } catch (IOException e) {
            System.err.println("통신 오류: " + e.getMessage());
        }
    }
}
```

### 실행 순서

두 파일을 같은 폴더에 저장하고 컴파일한다.

```bash
javac -encoding UTF-8 --release 17 TcpEchoServer.java TcpEchoClient.java
```

**터미널 1: 서버를 먼저 실행**

```bash
java TcpEchoServer
```

**터미널 2: 같은 폴더에서 클라이언트 실행**

```bash
java TcpEchoClient
```

클라이언트에서 입력해 본다.

```text
문자열 입력 (종료: QUIT)
안녕하세요
서버 응답: ECHO: 안녕하세요
QUIT
서버 응답: BYE
```

- `QUIT`은 대문자로 입력한다. 처리 후 양쪽 프로그램이 종료된다.
- 클라이언트의 표준 입력을 끝내도 소켓을 닫는다. 서버는 EOF를 확인해 종료한다.
- 서버는 입력을 기다리는 소켓 읽기가 60초 동안 진행되지 않으면 연결을 종료한다. 타임아웃 후에는 서버부터 다시 실행한다.
- 한글이 깨지면 파일 저장 형식과 터미널 입출력 인코딩도 UTF-8인지 확인한다.

> `try-with-resources`는 정상 종료와 예외 발생 시 자원을 닫는다. 연결된 `Socket`을 닫으면 관련 입력·출력 스트림도 닫힌다.

---

## 31.5 여러 클라이언트 처리하기

`accept()` 직후 메인 스레드에서 한 클라이언트와 계속 대화하면 다음 연결을 받아 처리하기 어렵다. **연결 수락은 메인 스레드**, **연결별 통신은 작업 스레드**로 분리한다.

### 멀티클라이언트 서버 — `MultiClientServer.java`

아래는 동시 처리 수를 4개로 제한하고 대기 작업을 쌓지 않는 예제다. 앞에서 만든 `TcpEchoServer.handle()`을 재사용한다.

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiClientServer {
    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                4, 4, 0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy());

        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress("127.0.0.1", 5000));
            System.out.println("멀티클라이언트 서버 대기: 최대 4개 연결 처리");
            while (true) {
                Socket client = server.accept();
                try {
                    pool.execute(() -> TcpEchoServer.handle(client));
                } catch (RejectedExecutionException e) {
                    // 처리할 스레드가 없으면 방금 수락한 연결을 닫는다.
                    try (client) {
                        System.err.println("서버 사용 중: 추가 연결 종료");
                    } catch (IOException closeError) {
                        System.err.println("연결 정리 오류: " + closeError.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
```

```bash
javac -encoding UTF-8 --release 17 TcpEchoServer.java TcpEchoClient.java MultiClientServer.java
java MultiClientServer
```

기본 `TcpEchoServer`가 실행 중이면 먼저 종료한다. 두 서버가 동시에 같은 TCP 주소·포트를 사용할 수는 없다. 그다음 다른 터미널 여러 개에서 `java TcpEchoClient`를 실행한다.

- 각 클라이언트가 자기 메시지에 대한 응답을 받는다. 이 코드는 전체에게 보내는 **채팅 브로드캐스트**까지 구현하지는 않는다.
- 4개 작업 스레드가 모두 사용 중이면 새 연결을 닫는다. 이때 클라이언트는 연결 종료나 통신 오류를 볼 수 있다. 연결 성공과 서비스 이용 가능은 별개다.
- 한 클라이언트가 `QUIT`을 입력해도 서버는 계속 새 연결을 받는다. 서버 종료는 해당 터미널의 `Ctrl+C`로 한다.
- `shutdown()`은 진행 중인 소켓 읽기를 즉시 취소하지 않는다. 서버를 정상 종료하는 기능을 추가할 때는 수신 대기용 `ServerSocket`과 활성 `Socket`을 닫고 작업 종료도 기다려야 한다.

> `Executors.newFixedThreadPool(4)`는 간단하지만 기본 작업 대기열에 제한이 없다. 여기서는 연결을 계속 쌓는 문제를 피하려고 `SynchronousQueue`로 대기 작업 없이 전달한다. 이 예제도 운영용 서버 전체 설계는 아니다. 특히 `readLine()`에는 메시지 길이 제한이 없으므로 외부 입력을 받는 서비스에서는 최대 길이와 메시지 수신 기한 등을 별도로 정해야 한다.

---

## 31.6 주문·재고 프로그램으로 확장하기

### 업무 프로토콜 정하기

StockFlow 같은 주문·재고 프로그램에서는 문자열을 보내는 것에 더해 **명령과 응답의 의미**를 정해야 한다. 다음은 확장을 위한 설계 예시이며, 위 에코 서버에 구현된 명령은 아니다.

| 방향 | 메시지 예 | 의미 |
|---|---|---|
| 요청 | `STOCK\|P001` | 상품 재고 조회 |
| 요청 | `ORDER\|req-001\|P001\|2` | 요청 ID, 상품 ID, 주문 수량 |
| 응답 | `OK\|req-001\|order-101\|8` | 요청 성공, 주문 ID, 남은 재고 |
| 응답 | `ERROR\|req-001\|OUT_OF_STOCK` | 재고 부족 |
| 알림 | `STOCK_CHANGED\|P001\|8\|12` | 상품 재고 8, 재고 버전 12 |

프로토콜을 정할 때는 구분자 포함 문자열 처리, 필드 수, 숫자 변환 실패, 존재하지 않는 상품, 0 이하 수량, 최대 메시지 길이도 함께 정한다. Java에서 `|`로 나눌 때는 정규식 메타문자이므로 `split("\\|", -1)`처럼 사용한다. `-1`은 마지막 빈 필드도 유지해 누락 검사를 돕는다.

### TCP와 공유 데이터 동기화는 별개

재고가 1개일 때 두 스레드가 동시에 재고를 확인하면 둘 다 주문을 성공시키는 오류가 생길 수 있다. **재고 확인과 차감 전체를 하나의 임계 구역**으로 묶어야 한다.

다음 `Inventory.java`는 동기화 개념을 보여 주는 독립 클래스다. 에코 서버에는 연결되어 있지 않다.

```java
public class Inventory {
    private int stock = 10;

    public synchronized boolean order(int quantity) {
        if (quantity <= 0 || stock < quantity) {
            return false;
        }
        stock -= quantity;
        return true;
    }

    public synchronized int getStock() {
        return stock;
    }
}
```

- 서버의 모든 연결 처리기가 **같은 `Inventory` 인스턴스**를 공유해야 같은 잠금으로 보호된다. 연결마다 새로 만들면 재고도 따로 생긴다.
- `stock`을 `volatile`로 바꾸는 것만으로는 확인과 차감이 원자적으로 묶이지 않는다.
- `synchronized`는 해당 JVM의 공유 객체를 보호한다. 서버가 여러 프로세스라면 데이터베이스 트랜잭션·조건부 갱신 등의 별도 설계가 필요하다.
- 클라이언트 화면의 재고는 오래된 값일 수 있다. **주문 가능 여부는 서버가 최종 확인**한다.
- 응답을 못 받았다고 주문이 실패한 것은 아니다. 서버가 처리한 직후 연결이 끊길 수도 있으므로 재시도에는 요청 ID와 중복 처리 방지 설계가 필요하다.

### 실시간 재고 알림과 GUI

1. 서버가 재고 변경을 확정하고 변경 버전을 기록한다.
2. 확정한 재고 스냅샷과 버전을 접속한 클라이언트에 알린다.
3. 클라이언트는 이미 적용한 버전보다 오래된 알림을 무시하고 화면을 갱신한다.

네트워크 전송은 느려질 수 있으므로 재고 잠금을 잡은 채 전체 클라이언트에 보내지 않는다. 한 소켓에 여러 스레드가 쓰는 경우에는 메시지 전체 단위로 직렬화하거나 연결별 송신 대기열을 두어 내용이 섞이지 않게 한다. 단순히 동시 접근 가능한 컬렉션을 쓴다고 알림 순서나 송신 동기화까지 해결되는 것은 아니다.

현재 에코 클라이언트는 요청 직후 응답 하나를 읽는 구조다. 서버가 자발적으로 알림을 보내는 프로그램으로 확장하려면 **소켓을 읽는 전담 스레드 하나**를 두고 요청 ID와 메시지 종류로 응답·알림을 분배한다. 여러 스레드가 같은 입력 스트림을 경쟁해서 읽으면 안 된다.

Swing 프로그램에서는 네트워크 연결과 읽기를 화면 이벤트 스레드(EDT)에서 실행하지 않는다. 별도 작업 스레드에서 받은 결과로 화면을 바꿀 때는 `SwingUtilities.invokeLater(...)`를 사용한다.

---

## 31.7 UDP 데이터그램 통신

UDP는 TCP처럼 `accept()`로 연결을 받아들이지 않는다. 보내는 쪽은 목적지 주소·포트와 데이터를 `DatagramPacket`에 넣고, 받는 쪽은 `receive()`로 데이터그램을 받는다.

### 수신 프로그램 — `UdpReceiver.java`

```java
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UdpReceiver {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress("127.0.0.1", 5001));
            socket.setSoTimeout(30_000);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("UDP 수신 대기: 127.0.0.1:5001");
            socket.receive(packet);

            String message = new String(packet.getData(), packet.getOffset(),
                    packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("수신: " + message);
            System.out.println("송신자: " + packet.getAddress().getHostAddress()
                    + ":" + packet.getPort());
        } catch (SocketTimeoutException e) {
            System.err.println("30초 동안 수신한 데이터그램이 없습니다.");
        } catch (IOException e) {
            System.err.println("UDP 수신 오류: " + e.getMessage());
        }
    }
}
```

### 송신 프로그램 — `UdpSender.java`

```java
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpSender {
    public static void main(String[] args) {
        byte[] data = "안녕하세요, UDP!".getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, InetAddress.getByName("127.0.0.1"), 5001);
            socket.send(packet);
            System.out.println("송신 요청 완료 (수신 확인 응답은 없음)");
        } catch (IOException e) {
            System.err.println("UDP 송신 오류: " + e.getMessage());
        }
    }
}
```

### 실행

```bash
javac -encoding UTF-8 --release 17 UdpReceiver.java UdpSender.java
```

터미널 1에서 `java UdpReceiver`를 실행하고, 30초 안에 터미널 2에서 `java UdpSender`를 실행한다. 수신 프로그램은 메시지 **하나를 받은 뒤 종료**한다. 수신 결과는 `수신: 안녕하세요, UDP!`이다.

- 문자열 길이와 바이트 수는 다르다. 송신 길이는 `message.length()`가 아니라 인코딩한 `data.length`를 사용한다.
- 수신 문자열은 버퍼 전체가 아니라 실제 수신 길이인 `packet.getLength()`만 해석한다.
- 여기서 `1024`는 예제의 수신 버퍼 크기이지 UDP의 최대 메시지 크기가 아니다. 수신 공간보다 큰 데이터그램은 잘릴 수 있다.
- 반복 수신하도록 바꾸고 같은 패킷 객체를 재사용한다면 매번 `packet.setLength(buffer.length)`로 수신 가능 길이를 복원한다.
- `send()`가 끝났다는 이유만으로 상대의 수신 성공을 판단할 수 없다. 필요하면 응답·번호·재시도를 프로토콜에 추가한다.

---

## 31.8 다른 컴퓨터와 통신하기

위 예제는 `127.0.0.1`에 바인딩하므로 같은 컴퓨터에서만 접속할 수 있다. 같은 LAN의 다른 컴퓨터와 실습하려면 다음을 변경한다.

1. 서버 컴퓨터의 LAN IPv4 주소를 확인한다. 예: `192.168.0.10`.
2. TCP 서버의 `bind` 주소를 그 LAN IP 또는 `0.0.0.0`으로 바꾼 뒤 다시 컴파일한다. `0.0.0.0`은 모든 로컬 IPv4 인터페이스에서 받겠다는 의미다.
3. 클라이언트 컴퓨터에서 `java TcpEchoClient 192.168.0.10`을 실행한다. 클라이언트의 목적지에는 서버의 실제 IP를 사용한다.
4. 방화벽과 공유기/AP의 기기 간 통신 차단 설정을 확인한다. 같은 Wi-Fi 이름으로 접속해도 서로 접근이 제한될 수 있다.

UDP도 수신 프로그램의 바인딩 주소와 송신 프로그램의 목적지 주소를 각각 바꿔야 한다. 다른 네트워크에서 접근할 때는 사설 IP에 바로 접속할 수 없는 경우가 있어 NAT·라우팅 등을 추가로 고려한다.

### 소켓, HTTP, WebSocket 구분

- 이 에코 서버는 직접 정한 **줄 단위 프로토콜**을 사용한다. 브라우저 주소창에서 `http://주소:5000`을 입력해도 정상 웹 페이지가 되지는 않는다.
- [Part 23의 REST API](part23_RestAPI.md)는 HTTP 요청·응답을 사용한다. Spring Boot 등이 연결과 HTTP 메시지 해석을 처리한다.
- WebSocket은 브라우저와 서버 간 양방향 통신에 사용하는 별도의 애플리케이션 프로토콜이다. 여기의 `Socket` 예제와 호환되지 않으며, 브라우저 JavaScript에서 임의의 TCP 소켓을 직접 여는 방식도 아니다.

> 실습의 TCP/UDP 문자열은 암호화되지 않는다. 실제 사용자 정보를 주고받는 서비스에는 인증과 전송 암호화 등을 별도로 설계해야 한다.

---

## 31.9 자주 빠지는 함정 ⚠️

| 증상 / 예외 | 먼저 확인할 내용 |
|---|---|
| `ConnectException: Connection refused` | 서버가 실행 중인지, 주소·포트와 바인딩이 맞는지 |
| `BindException: Address already in use` | 같은 주소·포트를 다른 서버나 이전 실행이 사용 중인지 |
| `UnknownHostException` | 호스트 이름 오타 또는 DNS 문제 |
| `SocketTimeoutException` | 연결·읽기 중 어느 단계에서 제한 시간을 넘겼는지 |
| `readLine()`에서 기다림 | 상대가 줄바꿈과 flush를 했는지, 양쪽 모두 먼저 읽으려 하는지 |
| `readLine()`이 `null` 반환 | 더 읽을 데이터 없이 EOF를 만남. 반복을 끝내고 자원 정리 |
| `Connection reset` / `Broken pipe` 등 | 상대 종료나 네트워크 장애. 구체적인 메시지는 OS에 따라 다름 |
| 한글 깨짐 | 양쪽 UTF-8 지정, 소스 파일과 터미널 인코딩 |
| 새 클라이언트가 응답을 못 받음 | 단일 처리 구조인지, 스레드가 모두 사용 중인지 |
| GUI가 멈춤 | 화면 이벤트 스레드에서 블로킹 통신을 하고 있는지 |
| 연결돼 보이는데 전송 실패 | `isConnected()`는 과거 연결 성공 여부이며 현재 상대의 생존 보장이 아님 |

타임아웃 자체가 언제나 소켓을 자동으로 닫는 것은 아니다. 이 문서의 코드는 예외를 처리하고 `try-with-resources`를 빠져나오면서 닫는다. 타임아웃 뒤 작업을 재시도할지는 업무 프로토콜과 중복 처리 가능성을 고려해 정한다.

---

## 31.10 연습문제

1. `ServerSocket`과 `Socket`의 역할 차이를 설명하자.
2. `write()`와 `flush()`를 했는데 `readLine()`이 계속 기다릴 수 있는 이유는?
3. 두 클라이언트가 각각 `사과`, `바나나`를 보내고 자기 응답만 받는지 확인하자.
4. `Inventory`의 초기 재고가 10일 때 여러 스레드가 1개씩 총 100회 주문하면 성공 횟수와 남은 재고는 어떻게 되어야 할까?
5. `ORDER`를 보낸 후 응답 전에 연결이 끊겼다. 같은 주문을 무조건 다시 보내면 어떤 문제가 생길까?
6. UDP 수신 프로그램을 반복 수신으로 바꾸고, 짧은 메시지 다음에 더 긴 한글 메시지를 보내 보자.

<details>
<summary>정답과 확인 기준</summary>

1. `ServerSocket`은 연결을 수락하고, 반환된 `Socket`은 특정 상대와 데이터를 주고받는다.
2. 연결이 유지되는 동안 줄바꿈이나 EOF가 없으면 줄의 끝을 알 수 없기 때문이다. `flush()`가 메시지 경계를 만들어 주지는 않는다.
3. `MultiClientServer`에서 각각 `ECHO: 사과`, `ECHO: 바나나`를 받아야 한다. 어느 한쪽이 종료해도 다른 연결은 동작해야 한다.
4. 같은 인스턴스를 공유하고 모든 주문이 끝날 때까지 기다렸다면 성공은 10회, 남은 재고는 0이어야 한다. 0이나 음수 주문은 실패해야 한다.
5. 첫 주문이 이미 처리되어 중복 차감될 수 있다. 요청 ID로 이전 처리 결과를 조회하거나 중복 처리 없이 동일 결과를 반환하도록 설계한다.
6. 반복마다 수신 패킷을 새로 만들거나 `setLength(buffer.length)`를 호출한다. 송신 바이트 수와 실제 수신 길이를 확인한다.

</details>

---

## 31.11 핵심 정리와 참고 자료

- TCP는 바이트 흐름을 전달한다. **메시지 경계와 업무 성공 응답은 애플리케이션의 책임**이다.
- `ServerSocket.accept()`로 얻은 `Socket`에서 입력·출력 스트림을 사용한다.
- UTF-8, 줄바꿈, flush, 타임아웃, EOF와 자원 정리를 함께 다룬다.
- 여러 연결 처리와 공유 재고 동기화는 별도의 문제다.
- UDP는 데이터그램 단위로 통신하지만 전달·순서를 보장하지 않는다.

공식 문서로 메소드의 동작과 예외를 확인할 수 있다. 예제는 Java 17 API 범위에서 작성했으며, 아래 링크는 Java SE 21 문서다.

- [Java Socket](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/net/Socket.html)
- [Java ServerSocket](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/net/ServerSocket.html)
- [Java BufferedReader](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/BufferedReader.html)
- [Java BufferedWriter](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/BufferedWriter.html)
- [Java PrintWriter](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/PrintWriter.html)
- [Java ThreadPoolExecutor](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ThreadPoolExecutor.html)
- [Java DatagramSocket](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/net/DatagramSocket.html)
- [TCP 표준: RFC 9293](https://www.rfc-editor.org/rfc/rfc9293.html)
- [UDP 표준: RFC 768](https://www.rfc-editor.org/rfc/rfc768.html)

[목차로 돌아가기](README.md)
