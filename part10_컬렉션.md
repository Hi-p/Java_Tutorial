# Part 10. 컬렉션 프레임워크

> 배열은 크기 고정. **컬렉션**은 크기가 알아서 늘어나는 자료구조 모음.

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
| 성능 | 빠름 | 약간 느림 |
| 편의성 | 낮음 | 높음 (메소드 풍부) |

> 실무에서는 대부분 `ArrayList`를 쓴다.

---

## 10.6 자주 빠지는 함정 ⚠️

1. **제네릭 타입** — `ArrayList<int>` (✗) → `ArrayList<Integer>` (✓). 기본형 사용 불가
2. **ConcurrentModificationException** — for-each 안에서 `remove()` 하면 터짐 → `Iterator` 사용
3. **HashMap 순서 보장 안 됨** — 순서가 필요하면 `LinkedHashMap` 사용

---

## 10.7 연습문제

**1)** ArrayList에 정수 10개를 넣고 짝수만 새 리스트에 모아서 출력

**2)** 문자열 배열에서 각 단어의 등장 횟수를 HashMap으로 세기
