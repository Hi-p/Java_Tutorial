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

    public int getCount() { return count; }
}
```

> `synchronized`: 한 번에 하나의 스레드만 접근 가능하게 **잠금**.

---

## 16.4 Thread.sleep() — 일시 정지

```java
try {
    Thread.sleep(1000);  // 1초 대기
} catch (InterruptedException e) {
    e.printStackTrace();
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

> 실무에서는 `new Thread()` 대신 **ExecutorService**를 사용한다.

---

## 16.6 자주 빠지는 함정 ⚠️

1. **`run()` vs `start()`** — `run()`은 새 스레드 안 만듦. 반드시 `start()`
2. **공유 데이터 접근** — 동기화 안 하면 예측 불가능한 결과
3. **데드락** — 두 스레드가 서로의 잠금을 기다려서 영원히 멈춤
4. **스레드는 순서 보장 안 됨** — 실행 순서는 OS가 결정
