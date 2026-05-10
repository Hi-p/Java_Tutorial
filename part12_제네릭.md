# Part 12. 제네릭

> **타입을 매개변수로** 만든다. 타입 안전성 + 코드 재사용의 핵심.

---

## 12.1 왜 제네릭?

```java
// 제네릭 없이
ArrayList list = new ArrayList();
list.add("hello");
list.add(123);       // 아무거나 들어감
String s = (String) list.get(1);  // 런타임 에러! 💥

// 제네릭 사용
ArrayList<String> list2 = new ArrayList<>();
list2.add("hello");
// list2.add(123);   // 컴파일 에러! ← 실행 전에 잡아줌
```

> 컴파일 시점에 타입 오류를 잡아준다.

---

## 12.2 제네릭 클래스

```java
public class Box<T> {
    private T item;

    public void set(T item) { this.item = item; }
    public T get() { return item; }
}
```

```java
Box<String> strBox = new Box<>();
strBox.set("안녕");
System.out.println(strBox.get());  // 안녕

Box<Integer> intBox = new Box<>();
intBox.set(42);
System.out.println(intBox.get());  // 42
```

> `T`는 타입 파라미터. 사용할 때 실제 타입으로 대체됨.

---

## 12.3 제네릭 메소드

```java
public static <T> void printArray(T[] arr) {
    for (T item : arr) {
        System.out.print(item + " ");
    }
    System.out.println();
}

String[] names = {"가", "나", "다"};
Integer[] nums = {1, 2, 3};
printArray(names);  // 가 나 다
printArray(nums);   // 1 2 3
```

---

## 12.4 타입 제한 — `extends`

```java
// T는 Number의 하위 타입만 허용
public static <T extends Number> double sum(T a, T b) {
    return a.doubleValue() + b.doubleValue();
}

System.out.println(sum(3, 5));      // 8.0
System.out.println(sum(1.5, 2.5));  // 4.0
// sum("a", "b");  // 컴파일 에러!
```

---

## 12.5 와일드카드 — `?`

```java
// 모든 타입의 리스트 출력
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.print(item + " ");
    }
}

// Number 이하만 허용
public static double sumList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) sum += n.doubleValue();
    return sum;
}
```

| 와일드카드 | 의미 |
|---|---|
| `<?>` | 모든 타입 |
| `<? extends T>` | T 또는 T의 하위 타입 |
| `<? super T>` | T 또는 T의 상위 타입 |

---

## 12.6 관례적 타입 파라미터 이름

| 이름 | 의미 |
|---|---|
| `T` | Type |
| `E` | Element (컬렉션) |
| `K` | Key |
| `V` | Value |
| `N` | Number |

---

## 12.7 자주 빠지는 함정 ⚠️

1. **기본형 사용 불가** — `Box<int>` (✗) → `Box<Integer>` (✓)
2. **`new T()` 불가** — 제네릭 타입으로 객체 생성 불가 (타입 소거 때문)
3. **`static` 필드에 제네릭 타입 사용 불가** — `static T field` (✗)
