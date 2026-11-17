# Part 17. Java 메모리 구조

> `NullPointerException`이 왜 생기는지, 객체가 어디에 저장되는지 이해하는 핵심.

---

## 17.1 JVM 메모리 영역

| 영역 | 저장하는 것 | 특징 |
|---|---|---|
| **Stack** | 지역변수, 매개변수, 메소드 호출 정보 | 메소드 종료 시 자동 제거 |
| **Heap** | 객체, 배열 (`new`로 만든 것) | GC가 관리 |
| **Method Area** | 클래스 정보, static 변수, 상수 | 프로그램 시작~끝 |

---

## 17.2 Stack vs Heap

```java
int x = 10;                    // Stack에 10 저장
String s = new String("hello"); // Stack에 주소, Heap에 "hello" 객체
int[] arr = {1, 2, 3};         // Stack에 주소, Heap에 배열
```

```
Stack                    Heap
┌──────────┐            ┌──────────────┐
│ x = 10   │            │ "hello"      │
│ s = 0x100│ ──────────>│ (주소 0x100) │
│ arr=0x200│ ──────────>│ {1, 2, 3}    │
└──────────┘            │ (주소 0x200) │
                        └──────────────┘
```

> **기본형**은 Stack에 값 자체를, **참조형**은 Stack에 주소를 저장하고 Heap에 실제 객체가 있다.

---

## 17.3 null이란?

```java
String s = null;  // s가 아무 객체도 가리키지 않음
System.out.println(s.length());  // NullPointerException! 💥
```

> `null` = "아무것도 참조하지 않는 상태". 여기에 메소드를 호출하면 터진다.

---

## 17.4 Garbage Collection (GC)

```java
String s = new String("hello");  // Heap에 객체 생성
s = null;                         // 참조 끊김 → GC 대상
// JVM이 알아서 메모리 회수
```

> 자바는 직접 메모리 해제를 하지 않아도 된다. GC가 **참조가 없는 객체**를 자동으로 정리.

---

## 17.5 Call by Value

자바는 **항상 값 복사**. 단, 참조형은 **주소값을 복사**.

```java
// 기본형: 값 복사 (원본 안 바뀜)
void change(int x) { x = 99; }

int a = 10;
change(a);
System.out.println(a);  // 10 (안 바뀜)

// 참조형: 주소 복사 (객체 내용은 바뀜)
void change(int[] arr) { arr[0] = 99; }

int[] b = {1, 2, 3};
change(b);
System.out.println(b[0]);  // 99 (바뀜!)
```

---

## 17.6 자주 빠지는 함정 ⚠️

1. **NullPointerException** — null 체크를 습관화. `if (s != null)` 또는 `Optional` 사용 (Part 18)
2. **`==` vs `equals()`** — 참조형의 `==`는 같은 객체인지 비교. 기본 `Object.equals()`도 동일성을 비교하며, 값 비교는 재정의 여부와 기준에 따라 달라진다. [Part 32](part32_Object와객체비교.md) 참고.
3. **메모리 누수** — 컬렉션에 객체를 넣고 빼지 않으면 GC가 못 치움
