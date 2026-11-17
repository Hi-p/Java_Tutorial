# Part 16. 멀티스레드 기초

> 여러 작업을 **동시에** 처리하는 기법. 현업 서버/앱 개발의 필수 개념.

---

## 16.1 스레드란?

프로세스 안에서 **독립적으로 실행되는 흐름**. 하나의 프로그램이 여러 일을 동시에 처리.

---

## 16.2 스레드 만들기

**방법 1: Thread 상속**

```java
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + ": " + i);
        }
    }
}

MyThread t = new MyThread();
t.start();  // run()이 아니라 start()!
```

**방법 2: Runnable 구현 (추천)**

```java
Runnable task = () -> {
    for (int i = 0; i < 5; i++) {
        System.out.println(Thread.currentThread().getName() + ": " + i);
    }
};

Thread t = new Thread(task);
t.start();
```

> `start()`를 호출해야 **새 스레드**에서 실행됨. `run()`을 직접 호출하면 그냥 메인 스레드에서 실행.

---

## 16.3 동기화 — `synchronized`

여러 스레드가 같은 데이터에 접근하면 충돌 발생 (Race Condition).

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() { return count; }
}
```

> 인스턴스 `synchronized` 메소드는 같은 객체의 잠금을 공유한다. 같은 잠금을 사용하는 코드끼리 상호 배제와 변경 내용의 가시성을 확보한다. 객체가 다르면 잠금도 다르다.

---

## 16.4 Thread.sleep() — 일시 정지

```java
try {
    Thread.sleep(1000);  // 1초 대기
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // 인터럽트 상태를 복구하고 작업 종료
    return;
}
```

---

## 16.5 ExecutorService (실무 방식)

스레드를 직접 만들지 않고 **스레드 풀**을 사용.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

ExecutorService executor = Executors.newFixedThreadPool(3);

for (int i = 0; i < 10; i++) {
    final int taskId = i;
    executor.submit(() -> {
        System.out.println("Task " + taskId + " - " + Thread.currentThread().getName());
    });
}

executor.shutdown();
```

> `ExecutorService`로 작업 제출과 스레드 관리를 분리할 수 있다. 이 간단한 예제의 출력 순서는 실행마다 달라질 수 있다. Java 17에서는 사용 후 종료를 직접 관리한다.

---

## 16.6 자주 빠지는 함정 ⚠️

1. **`run()` vs `start()`** — `run()`은 새 스레드 안 만듦. 반드시 `start()`
2. **공유 데이터 접근** — 동기화 안 하면 예측 불가능한 결과
3. **데드락** — 두 스레드가 서로의 잠금을 기다려서 영원히 멈춤
4. **스레드는 순서 보장 안 됨** — 실행 순서는 OS가 결정

---

## 16.7 동시성, 병렬성, 가시성과 원자성

- **동시성:** 여러 작업이 진행되는 시간을 겹쳐 관리한다.
- **병렬성:** 여러 실행 자원에서 실제로 동시에 계산한다.
- **가시성:** 한 스레드의 변경을 다른 스레드가 관찰할 수 있는가?
- **원자성:** 연산 도중의 중간 상태가 다른 연산과 섞이지 않는가?

`count++`는 읽기 → 계산 → 쓰기의 복합 연산이다. `volatile int count`로 바꾸더라도 이 전체가 원자적으로 묶이지 않는다.

| 도구 | 주된 역할 | 해결하지 못하는 것 |
|---|---|---|
| `volatile` | 변경 가시성과 관련 순서 보장 | `++`, 확인 후 차감 같은 복합 연산의 원자성 |
| `synchronized` | 같은 잠금의 코드에 상호 배제·가시성 제공 | 서로 다른 잠금으로 보호한 상태의 일관성 |
| `AtomicInteger` | 증가·비교 후 교체 등 개별 원자 연산 | 여러 원자 객체에 걸친 업무 처리 전체 |
| `ConcurrentHashMap` | 스레드 안전한 맵 연산 | 여러 호출·여러 키를 묶은 트랜잭션 |

```java
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();

ConcurrentHashMap<String, Integer> orders = new ConcurrentHashMap<>();
orders.merge("P001", 1, Integer::sum);
```

`orders.put(id, orders.get(id) + 1)`은 조회와 수정 사이에 다른 작업이 끼어들 수 있다. `merge()`·`compute()` 등 단일 키의 원자적 갱신 기능을 사용한다. 이 콜백 안에서 느린 네트워크 호출 등을 수행하지 않는다.

재고 확인과 차감처럼 여러 단계의 업무 규칙은 하나의 임계 구역에서 처리한다. [Part 24의 StockService](examples/src/main/java/tutorial/part24/StockService.java)가 그 예다.

---

## 16.8 Future와 CompletableFuture

| 기능 | 의미 |
|---|---|
| `submit(Callable<T>)` | 결과가 있는 작업을 제출하고 `Future<T>` 반환 |
| `future.get()` | 결과가 준비될 때까지 대기. 작업 실패는 `ExecutionException`으로 전달 |
| `future.get(timeout, unit)` | 결과를 기다리는 시간 제한. 시간 초과가 작업 자동 취소를 뜻하지 않음 |
| `thenApply()` | 앞선 결과를 다른 값으로 변환 |
| `thenCompose()` | 다음 비동기 작업으로 연결해 중첩 Future를 평탄화 |
| `thenCombine()` | 두 작업의 결과를 합치기 |
| `exceptionally()` | 예외 발생 시 대체 결과 계산 |

```java
CompletableFuture<Integer> price = CompletableFuture
        .supplyAsync(() -> 1200 * 3, pool)
        .thenApply(total -> total - 600);
System.out.println(price.get(5, TimeUnit.SECONDS)); // 3000
```

`thenApply()`는 반드시 다른 스레드에서 실행되는 것은 아니다. 별도 비동기 실행이 필요하면 `thenApplyAsync(..., pool)` 등을 사용한다. 실행기를 생략한 비동기 메소드는 보통 공용 풀을 사용하므로 느린 블로킹 작업을 많이 넣을 때는 영향을 고려한다.

`join()`도 결과를 기다릴 수 있다. `get()`과 달리 실패를 주로 `CompletionException` 같은 비검사 예외로 전달한다. 비동기 API를 썼어도 바로 기다리면 호출한 스레드는 대기한다.

---

## 16.9 작업 취소와 정상 종료

1. `shutdown()`으로 새 작업 접수를 중단한다.
2. `awaitTermination()`으로 이미 제출한 작업의 종료를 기다린다.
3. 기한을 넘기면 `shutdownNow()`로 대기 작업을 꺼내고 실행 중인 작업의 인터럽트를 시도한다.
4. 작업은 인터럽트·취소 신호를 확인하고 자원을 정리해야 한다.

**인터럽트는 강제 종료가 아니라 협력적인 중단 요청**이다. `Future.cancel(true)`도 작업이 실제로 끝났다는 보장은 아니다. 취소 상태와 작업 종료 확인을 구분한다.

```java
try {
    // 인터럽트에 반응하는 대기·작업
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    return;
}
```

예외를 호출자에게 전달할 수 있다면 `throws InterruptedException`으로 전파하는 방법도 있다. 예외를 삼키고 무조건 반복하면 종료 요청에 반응하지 않을 수 있다. 소켓 등 자원은 해당 API의 종료 방식도 함께 고려한다.

`CompletableFuture.cancel(true)`는 실행 중인 계산을 제어하는 `FutureTask`와 같지 않으며, `true`가 작업 스레드 인터럽트를 보장하지 않는다. 필요한 취소 정책을 별도로 설계한다.

---

## 16.10 실행 가능한 예제

전체 코드: [ConcurrencyExample.java](examples/src/main/java/tutorial/part16/ConcurrencyExample.java)

```bash
bash examples/run-core.sh 16
```

```text
완료 작업: 1000
P001 집계: 1000
Future 합계: 3600
비동기 할인 합계: 3000
```

예제는 작업 완료 후 값을 출력하므로 결과가 일정하다. 작업 내부의 실행 순서를 고정한 것은 아니다. 고정 풀의 기본 대기열은 크기 제한이 없으므로, 실제 서버에서는 대기열 크기·거절 정책도 정해야 한다.

취소 코드: [CancellationExample.java](examples/src/main/java/tutorial/part16/CancellationExample.java)

```bash
bash examples/run-core.sh cancellation
```

```text
취소 요청 수락: true
작업 종료 확인: true
Future 취소 상태: true
```

이 예제는 `CountDownLatch`로 작업 시작과 종료를 각각 확인한다. `sleep()`으로 “아마 끝났을 것”이라고 추측하지 않는다.

---

## 16.11 연습문제

1. `volatile int`에 여러 스레드가 `++`를 수행할 때 값이 누락될 수 있는 이유는?
2. 스레드 4개가 상품별 주문 횟수를 각각 1,000회 갱신하도록 확장하자.
3. `get(1, SECONDS)`가 시간 초과되면 작업이 끝난 것일까?
4. [동시 주문 테스트](examples/src/test/java/tutorial/part24/StockServiceTest.java)에서 동기화를 제거하면 어떤 문제가 가능한가?

<details>
<summary>정답과 확인 기준</summary>

1. 읽기·계산·쓰기 전체가 하나의 원자 연산이 아니기 때문이다.
2. `merge()` 등을 사용하고 모든 작업 완료 후 총 4,000회인지 확인한다.
3. 아니다. 대기자가 기다리기를 중단한 것이며, 작업 취소 여부는 따로 결정해야 한다.
4. 재고보다 많은 주문이 성공하거나 갱신이 누락될 수 있다. 한 번 테스트가 통과했다고 경쟁 상태가 없다고 증명되지는 않는다.

</details>

참고: [ExecutorService](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html), [ConcurrentHashMap](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html), [CompletableFuture](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html)

[목차로 돌아가기](README.md)
