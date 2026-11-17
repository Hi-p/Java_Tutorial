# Part 10. 컬렉션 프레임워크

> **컬렉션**으로 여러 데이터를 저장·조회·정렬한다. 구현에 따라 순서, 중복, 수정 가능 여부와 연산 비용이 다르다.

---

## 10.1 ArrayList — 동적 배열

```java
import java.util.ArrayList;

ArrayList<String> list = new ArrayList<>();
list.add("사과");
list.add("바나나");
list.add("포도");

System.out.println(list.get(0));   // 사과
System.out.println(list.size());   // 3

list.remove(1);  // 바나나 제거
System.out.println(list);  // [사과, 포도]
```

| 메소드 | 설명 |
|---|---|
| `.add(값)` | 추가 |
| `.get(인덱스)` | 조회 |
| `.set(인덱스, 값)` | 수정 |
| `.remove(인덱스)` | 삭제 |
| `.size()` | 크기 |
| `.contains(값)` | 포함 여부 |
| `.isEmpty()` | 비어있는지 |

---

## 10.2 HashMap — 키-값 쌍

```java
import java.util.HashMap;

HashMap<String, Integer> scores = new HashMap<>();
scores.put("민수", 90);
scores.put("영희", 85);
scores.put("철수", 95);

System.out.println(scores.get("민수"));       // 90
System.out.println(scores.containsKey("영희")); // true
System.out.println(scores.size());            // 3

// 순회
for (String key : scores.keySet()) {
    System.out.println(key + ": " + scores.get(key));
}
```

| 메소드 | 설명 |
|---|---|
| `.put(키, 값)` | 추가/수정 |
| `.get(키)` | 조회 |
| `.remove(키)` | 삭제 |
| `.containsKey(키)` | 키 존재 여부 |
| `.keySet()` | 모든 키 |
| `.values()` | 모든 값 |

---

## 10.3 HashSet — 중복 없는 집합

```java
import java.util.HashSet;

HashSet<String> set = new HashSet<>();
set.add("사과");
set.add("바나나");
set.add("사과");  // 중복 → 무시됨

System.out.println(set.size());      // 2
System.out.println(set.contains("사과")); // true
```

---

## 10.4 컬렉션 순회

```java
ArrayList<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5));

// for-each
for (int n : nums) {
    System.out.print(n + " ");
}

// Iterator
Iterator<Integer> it = nums.iterator();
while (it.hasNext()) {
    System.out.print(it.next() + " ");
}
```

---

## 10.5 배열 vs ArrayList

| | 배열 | ArrayList |
|---|---|---|
| 크기 | 고정 | 동적 |
| 타입 | 기본형 가능 (`int[]`) | 참조형만 (`Integer`) |
| 특징 | 기본형을 직접 저장 가능 | 용량 관리와 다양한 메소드 제공 |
| 편의성 | 낮음 | 높음 (메소드 풍부) |

> 순서 있는 목록에는 `ArrayList`가 흔한 선택이다. 조회·삽입·삭제 방식과 데이터 크기에 따라 구현을 고른다.

---

## 10.6 자주 빠지는 함정 ⚠️

1. **제네릭 타입** — `ArrayList<int>` (✗) → `ArrayList<Integer>` (✓). 기본형 사용 불가
2. **순회 중 구조 변경** — 일반 컬렉션을 for-each로 순회하며 직접 삭제하면 `ConcurrentModificationException`이 발생할 수 있다. `Iterator.remove()`나 `removeIf()` 사용. 예외 발생 자체를 프로그램의 정확성 보장 수단으로 삼지 않는다.
3. **HashMap 순서 보장 안 됨** — 순서가 필요하면 `LinkedHashMap` 사용

---

## 10.7 연습문제

**1)** ArrayList에 정수 10개를 넣고 짝수만 새 리스트에 모아서 출력

**2)** 문자열 배열에서 각 단어의 등장 횟수를 HashMap으로 세기

---

## 10.8 인터페이스와 구현 선택

```java
List<String> names = new ArrayList<>();
Map<String, Integer> stocks = new HashMap<>();
```

왼쪽은 사용할 기능의 범위(인터페이스), 오른쪽은 실제 구현이다. `List`, `Set`, `Queue`는 `Collection` 계열이고, `Map`은 별도의 인터페이스다.

| 요구사항 | 먼저 고려할 타입 | 주의점 |
|---|---|---|
| 인덱스로 읽는 목록 | `ArrayList` | 중간 삽입·삭제 시 뒤 요소 이동 |
| 키로 값 조회 | `HashMap` | 순서 보장 없음, 키의 비교 규칙 필요 |
| 중복 제거 | `HashSet` | `equals()`·`hashCode()` 기준 |
| 삽입 순서 유지 | `LinkedHashMap`, `LinkedHashSet` | 정렬된 순서와는 다름 |
| 키·요소를 정렬 상태로 유지 | `TreeMap`, `TreeSet` | 비교 결과가 0이면 같은 키·요소로 취급 |
| FIFO 대기열 / LIFO 되돌리기 | `ArrayDeque` | `null` 요소를 허용하지 않음 |
| 가장 높은 우선순위부터 꺼내기 | `PriorityQueue` | 기본은 작은 값 우선 |

`List.of()`는 수정 불가 리스트다. 정렬·추가·삭제가 필요하면 `new ArrayList<>(List.of(...))`처럼 복사한다. `Arrays.asList()`는 크기가 고정된 목록이므로 `set()`과 `add()`의 지원 여부가 다르다.

---

## 10.9 Queue, Deque, PriorityQueue

```java
Queue<String> queue = new ArrayDeque<>();
queue.offer("주문 A");
queue.offer("주문 B");
System.out.println(queue.poll()); // 주문 A

Deque<String> undo = new ArrayDeque<>();
undo.push("상품 추가");
undo.push("수량 변경");
System.out.println(undo.pop());   // 수량 변경
```

| 연산 | 실패 시 예외를 던지는 방식 | 특별한 값을 반환하는 방식 |
|---|---|---|
| 삽입 | `add(e)` | `offer(e)` |
| 맨 앞 제거 | `remove()` | `poll()` |
| 맨 앞 조회 | `element()` | `peek()` |

빈 큐의 `poll()`·`peek()`는 `null`을 반환한다. 크기 제한이 없는 일반 `ArrayDeque`에서는 용량 부족에 따른 `offer()` 실패 상황을 보통 다루지 않지만, 용량 제한 큐에서는 중요하다.

```java
PriorityQueue<Integer> queue = new PriorityQueue<>(List.of(30, 10, 20));
while (!queue.isEmpty()) System.out.println(queue.poll()); // 10, 20, 30
```

**`PriorityQueue`를 for-each나 `toString()`으로 순회한 결과는 정렬 순서를 보장하지 않는다.** 우선순위 순서가 필요하면 반복해서 `poll()`한다. 동일 우선순위의 처리 순서까지 필요하면 접수 번호 등을 추가 비교 기준으로 둔다.

---

## 10.10 Comparable과 Comparator

- `Comparable<T>`: 클래스 자체에 기본 정렬 기준을 둔다. `compareTo()` 구현.
- `Comparator<T>`: 필요할 때 별도의 정렬 기준을 전달한다. 가격순·이름순 등을 바꾸기 쉽다.

```java
products.sort(Comparator.comparingInt(Product::price)
        .thenComparing(Product::id)); // 가격이 같으면 ID로 비교
```

비교 결과는 음수·0·양수로 해석한다. `a.price - b.price`는 정수 오버플로가 날 수 있으므로 `Integer.compare()`나 `comparingInt()`를 사용한다.

`TreeSet`·`TreeMap`은 비교 결과 0을 중복 판단에 사용한다. 따라서 일반적인 집합·맵의 동등성 규칙을 지키려면 비교 기준을 `equals()`와 일관되게 설계한다. 단순 가격 비교만 쓰면 서로 다른 상품이 같은 가격이라는 이유로 하나로 취급될 수 있다.

---

## 10.11 연산별 시간복잡도

n은 저장된 요소 수다. 아래는 대표적인 구현 특성을 정리한 것으로 실제 시간은 데이터·해시 분포·메모리 할당 등에 영향을 받는다.

| 구조 | 대표 연산 | 비용 |
|---|---|---|
| `ArrayList` | 인덱스 조회 / 끝 추가 | O(1) / 분할상환 O(1) |
| `ArrayList` | 중간 삽입·삭제 / 값 검색 | O(n) |
| `LinkedList` | 인덱스로 요소 찾기 | O(n) |
| `LinkedList` | 양끝 삽입·삭제 | O(1) |
| `HashMap`, `HashSet` | 조회·추가·삭제 | 적절한 해시 분포에서 기대 O(1) |
| `TreeMap`, `TreeSet` | 조회·추가·삭제 | O(log n) |
| `ArrayDeque` | 양끝 추가·제거 | 분할상환 O(1) |
| `PriorityQueue` | `offer()`·`poll()` / `peek()` | 보통 O(log n) / O(1) |
| `PriorityQueue` | 특정 값 검색·제거 | O(n) |

**분할상환**은 여러 연산의 총비용을 나눠 본다는 뜻이다. `ArrayList`의 한 번의 추가가 내부 배열 확장 때문에 O(n)이 될 수 있어도, 반복 추가 전체를 보면 건당 분할상환 O(1)이다. `PriorityQueue` 역시 내부 배열 확장 시 추가 비용이 생길 수 있다.

“`LinkedList`는 중간 삽입이 항상 빠르다”라고 외우면 안 된다. 삽입 위치까지 찾아가는 비용이 필요하다. 이미 해당 위치의 반복자를 확보한 경우와 인덱스로 찾는 경우를 구분한다.

---

## 10.12 자주 생기는 실제 오류

```java
List<Integer> values = new ArrayList<>(List.of(1, 2, 3));
values.remove(1);                  // 인덱스 1의 값 2 제거
values.remove(Integer.valueOf(1)); // 값 1 제거
values.removeIf(n -> n % 2 == 0);  // 조건으로 제거
```

- `map.get(key) == null`은 값이 없거나 값이 null인 경우 모두 가능하다. 구분하려면 `containsKey()`를 사용한다.
- `HashMap` 키와 `HashSet` 요소의 비교에 사용되는 필드는 저장 후 바꾸지 않는다. [Part 32](part32_Object와객체비교.md) 참고.
- 값과 키를 함께 쓸 때는 `entrySet()`으로 순회하면 키로 다시 조회할 필요가 없다.
- 일반 `ArrayList`·`HashMap`을 여러 스레드에서 함께 수정하려면 동기화 설계가 필요하다. [Part 16](part16_멀티스레드기초.md) 참고.

---

## 10.13 실행 예제와 예상 결과

전체 코드: [CollectionsExample.java](examples/src/main/java/tutorial/part10/CollectionsExample.java)

저장소 루트에서 실행한다. JDK 17 이상이면 추가 라이브러리가 필요 없다.

```bash
bash examples/run-core.sh 10
```

```text
남은 수: [1, 3]
FIFO: order-1, order-2
실행 취소: 수량 변경
우선순위: [10, 20, 30]
기본 정렬: [P001, P002, P003]
가격·ID 정렬: [P002, P003, P001]
삽입 순서: {P002=2, P001=1}
키 정렬: {P001=1, P002=2}
```

심화 연습:

1. 긴급도와 접수 번호로 정렬하는 주문 대기열을 만들어 보자.
2. 같은 가격의 상품도 유지하는 `TreeSet` 비교 기준을 설계해 보자.
3. 상품별 주문 횟수를 `merge()`로 집계하고 상품 ID순으로 출력하자.

<details>
<summary>풀이 방향</summary>

1. `Comparator.comparingInt(...).thenComparingLong(...)`을 전달한 `PriorityQueue`를 사용한다. 긴급도 숫자가 작을수록 우선인지 먼저 정한다.
2. 가격이 같으면 ID를 비교한다. 객체의 동등성 정책과 정렬 정책의 일관성도 확인한다.
3. `map.merge(id, 1, Integer::sum)`으로 집계하고 `new TreeMap<>(map)`으로 키 정렬한다.

</details>

참고: [PriorityQueue](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html), [Java Collections](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/package-summary.html)

[목차로 돌아가기](README.md)
