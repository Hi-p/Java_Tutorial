# Part 33. 불변 객체와 객체 복사

> 같은 객체를 여러 곳에서 참조할 때 생기는 변경 전파를 이해하고, 주문 당시 정보를 안전하게 보관한다.

선행 학습: [Part 17 메모리](part17_메모리구조.md), [Part 26 record](part26_최신Java문법.md), [Part 32 객체 비교](part32_Object와객체비교.md)

---

## 33.1 final과 불변 객체는 다르다

```java
final List<String> names = new ArrayList<>();
names.add("노트"); // 가능: 같은 리스트의 내용 변경
// names = new ArrayList<>(); // 불가: 참조 재대입
```

참조형 필드의 `final`은 **다른 객체로 참조를 바꾸지 못하게** 한다. 참조한 객체의 내부 상태까지 얼리는 것은 아니다.

불변 객체를 설계할 때는 생성 이후 외부에서 관찰할 수 있는 상태가 바뀌지 않도록 한다.

- 필드를 외부에서 직접 수정하지 못하게 하고 생성 시 유효성을 검사한다.
- setter처럼 상태를 바꾸는 메소드를 제공하지 않는다.
- 가변 객체를 입력받거나 반환할 때 참조 노출을 막는다.
- 상속으로 불변성을 깨지 못하게 하는 방법도 고려한다.

불변 객체는 공유와 추론에 도움이 되지만, 상태 변경마다 새 객체를 만들 수 있어 비용도 고려한다. 모든 클래스를 무조건 불변으로 바꾸는 것이 목표는 아니다.

---

## 33.2 참조 복사·얕은 복사·깊은 복사

| 방식 | 바깥 객체 | 내부 가변 객체 |
|---|---|---|
| `b = a` | 같은 객체 | 공유 |
| `new ArrayList<>(a)` | 새 리스트 | 요소 참조는 공유 |
| 요소까지 필요한 만큼 복사 | 새 리스트 | 복사한 가변 상태는 분리 |

```java
List<MutableProduct> shallow = new ArrayList<>(original);
List<MutableProduct> deep = original.stream()
        .map(p -> new MutableProduct(p.getId(), p.getPrice()))
        .toList();
```

두 번째 코드는 이 예제처럼 요소가 `String`과 `int`만 가지는 단순 구조에서 가변 상태를 분리한다. 내부에 다른 가변 리스트가 있으면 그 상태도 살펴야 한다. 복잡한 그래프의 순환 참조·공유 관계를 무시하고 무조건 재귀 복사하면 문제가 생길 수 있다.

`Object.clone()`이 자동으로 깊은 복사를 하는 것은 아니다. 학습 단계에서는 복사 생성자나 변환 메소드로 복사 범위를 명시하는 편이 이해하기 쉽다.

---

## 33.3 수정 불가 목록과 불변 요소

| 코드 | 원본 리스트의 이후 변경 | 요소 객체 변경 |
|---|---|---|
| `new ArrayList<>(source)` | 목록 구조는 분리 | 가변 요소는 공유 |
| `Collections.unmodifiableList(source)` | 원본의 변경이 뷰에 반영 | 가변 요소는 공유 |
| `List.copyOf(source)` | 목록 스냅샷으로 분리 | 가변 요소는 공유 |

`List.copyOf()`는 이미 적합한 수정 불가 목록이면 인스턴스를 재사용할 수도 있다. 중요한 것은 “항상 새 객체인가”보다 제공하는 동작 계약이다. null 목록·null 요소는 허용하지 않는다.

**수정 불가 리스트 안에 가변 객체가 들어 있으면 전체가 불변인 것은 아니다.** 원소를 제거할 수 없더라도 원소의 `setPrice()`는 호출될 수 있다.

---

## 33.4 record와 방어적 복사

record의 컴포넌트에 대응하는 필드는 `private final`이지만, 그 필드가 가변 객체를 참조하면 내부 상태는 변할 수 있다. 따라서 record는 기본적으로 **얕게 불변**이다.

```java
public record OrderLine(String productId, int unitPrice, int quantity) {
    public OrderLine {
        Objects.requireNonNull(productId);
        if (unitPrice < 0 || quantity <= 0) {
            throw new IllegalArgumentException("금액·수량 오류");
        }
    }
}

public record OrderSnapshot(List<OrderLine> lines) {
    public OrderSnapshot {
        lines = List.copyOf(lines);
    }
}
```

이 설계는 두 층을 함께 다룬다.

1. 입력 리스트를 방어적으로 복사해 호출자가 목록을 바꿔도 영향을 받지 않게 한다.
2. 요소인 `OrderLine`도 불변인 `String`과 기본형만 가지게 한다.

생성자에서 복사해도 getter로 내부 가변 객체를 그대로 반환하면 다시 노출될 수 있다. 이 예제는 수정 불가 목록과 불변 요소를 반환하므로 두 경로를 함께 보호한다.

---

## 33.5 주문 시점의 스냅샷

상품의 현재 가격과 과거 주문의 가격은 목적이 다르다. 주문 당시 가격·수량을 별도 값으로 저장하면 이후 상품 가격이 변경되어도 과거 주문 정보를 유지할 수 있다.

전체 코드: [ImmutabilityExample.java](examples/src/main/java/tutorial/part33/ImmutabilityExample.java)

```bash
bash examples/run-core.sh 33
```

```text
원본 리스트 크기: 0
얕은 복사 가격: 2000
깊은 복사 가격: 1000
주문 당시 가격: 1000
주문 항목 수: 1
주문 목록 직접 수정: 차단
```

얕은 복사 가격은 원본 상품과 함께 2000으로 바뀌지만, 분리된 복사와 주문 시점의 값은 1000을 유지한다.

검증 코드: [ImmutabilityTest.java](examples/src/test/java/tutorial/part33/ImmutabilityTest.java)

---

## 33.6 연습문제

1. `record Basket(List<String> items)`만 선언하면 리스트 내용까지 불변일까?
2. `Collections.unmodifiableList(source)` 뒤에 `source.clear()`를 호출하면 뷰는 어떻게 될까?
3. 주문 항목에 가변 `Date` 필드를 추가하면 무엇을 더 고려해야 할까?
4. 불변 객체에서 가격을 바꾸는 기능은 어떻게 표현할까?

<details>
<summary>정답과 풀이 방향</summary>

1. 아니다. 생성 시 `List.copyOf()` 등으로 목록 구조를 보호해야 한다. 여기의 String 요소는 불변이다.
2. 원본을 보는 뷰이므로 비워진 상태가 보인다.
3. 입력과 반환 경로의 방어적 복사를 고려하거나 불변인 `java.time` 타입을 사용한다.
4. 새 가격으로 새 객체를 반환한다. 기존 객체를 참조하는 코드에는 기존 값이 유지된다.

</details>

공식 참고: [Record](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Record.html), [List.copyOf](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))

[목차](README.md) · [다음: 날짜·시간과 금액](part34_날짜시간과금액처리.md)
