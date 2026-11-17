# Part 32. Object와 객체 비교

> “두 변수가 같은 객체를 가리킨다”와 “서로 다른 객체의 값이 같다”를 구분하고, 해시 컬렉션에서 올바르게 사용하는 법을 익힌다.

선행 학습: [Part 7 객체지향](part7_객체지향기초.md), [Part 10 컬렉션](part10_컬렉션.md)

---

## 32.1 Object와 동일성·동등성

모든 Java 클래스는 직접 또는 간접적으로 `Object`를 상속한다. 배열도 객체이며 `Object`의 메소드를 사용할 수 있다.

| 비교 | 질문 | 객체에서 사용하는 방식 |
|---|---|---|
| 동일성(identity) | 바로 그 하나의 객체인가? | `==` |
| 동등성(equality) | 정한 기준에서 같은 값인가? | `equals()` |

기본형의 `==`는 기본형 값 비교다. 참조형의 `==`는 같은 객체를 참조하는지 비교한다. Java 코드에서 실제 메모리 주소를 직접 비교·관리한다고 이해할 필요는 없다.

```java
String a = new String("노트");
String b = new String("노트");
System.out.println(a == b);      // false
System.out.println(a.equals(b)); // true
```

`String`이 내용으로 비교되는 것은 `equals()`를 재정의했기 때문이다. **기본 `Object.equals()`는 `==`와 같은 동일성 비교**를 한다. 직접 만든 클래스가 자동으로 모든 필드를 비교하는 것은 아니다.

문자열 리터럴의 공유 때문에 `==`가 우연히 true인 예를 일반화하지 않는다. 배열 내용은 `Arrays.equals()`, 중첩 배열 내용은 필요에 따라 `Arrays.deepEquals()` 등을 검토한다.

---

## 32.2 상품의 비교 기준 정하기

이 파트의 정책은 **상품 ID가 같으면 같은 상품**이다. 상품명이 바뀌어도 ID가 같으면 동등하게 취급한다. 이는 예제의 설계 선택이지 모든 도메인의 정답은 아니다.

전체 구현: [Product.java](examples/src/main/java/tutorial/part32/Product.java)

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    return obj instanceof Product other && id.equals(other.id);
}

@Override
public int hashCode() {
    return id.hashCode();
}
```

클래스를 `final`로 선언하고 ID를 변경 불가 필드로 둔다. 비교에 필요한 ID는 생성 시 null·빈 문자열을 거부한다. 상속 가능한 클래스에서는 부모·자식의 동등성 기준이 대칭성을 깨지 않는지도 설계해야 한다.

| equals 규칙 | 의미 |
|---|---|
| 반사성 | `a.equals(a)`는 true |
| 대칭성 | a와 b의 비교 방향이 바뀌어도 결과가 같음 |
| 추이성 | a=b이고 b=c이면 a=c |
| 일관성 | 비교 정보가 같다면 반복 비교 결과도 같음 |
| null 비교 | null이 아닌 객체의 `equals(null)`은 false |

`equals(Product other)`만 정의하면 재정의가 아니라 **오버로딩**이 될 수 있다. `equals(Object obj)`와 `@Override`를 사용해 실수를 잡는다.

---

## 32.3 hashCode를 함께 구현하는 이유

`HashMap`·`HashSet`은 해시값으로 검색 범위를 좁히고 동등성 비교를 이용해 키·요소를 구별한다.

- `a.equals(b)`가 true이면 `a.hashCode() == b.hashCode()`도 true여야 한다.
- 해시값이 같아도 서로 동등하지 않을 수 있다. 이것이 **해시 충돌**이다.
- 서로 다른 객체의 해시값이 반드시 달라야 하는 것은 아니다.
- `hashCode()`를 객체의 고유 번호나 실제 메모리 주소라고 생각하지 않는다.

`equals()`만 바꾸고 `hashCode()`를 그대로 두면 같은 ID의 상품이 `HashSet`에 중복으로 들어가거나 `HashMap`에서 기대한 키로 조회되지 않을 수 있다.

**키를 넣은 뒤 비교에 사용하는 필드를 바꾸지 않는다.** 현재 예제의 ID가 `final`인 이유다. 가변 객체가 필요하다면 변경되지 않는 ID 자체를 키로 사용하는 설계도 가능하다.

---

## 32.4 toString과 Objects

```java
@Override
public String toString() {
    return "Product[id=" + id + ", name=" + name + "]";
}
```

`toString()`은 객체를 로그·디버거 등에서 이해하기 쉽게 표현한다. 비밀번호 같은 민감한 값은 출력하지 않는다. 네트워크 메시지나 파일 저장 형식이 필요하면 명시적인 JSON 등의 형식을 사용한다.

`Objects.equals(a, b)`는 null을 고려한 비교에, `Objects.requireNonNull(value)`는 필수값 확인에 유용하다. 여러 필드로 해시값을 만들 때는 `Objects.hash(field1, field2)`를 사용할 수 있다.

---

## 32.5 실행과 예상 결과

전체 실행 코드: [EqualityExample.java](examples/src/main/java/tutorial/part32/EqualityExample.java)

저장소 루트에서 실행한다.

```bash
bash examples/run-core.sh 32
```

```text
first == second: false
first == alias: true
equals: true
같은 hashCode: true
중복 제거 후 크기: 1
동등한 키로 조회: 10
Product[id=P001, name=노트]
```

이름이 다른 두 상품도 같은 ID라서 Set에는 하나만 남는다. 컬렉션이 어떤 이름을 “최신 정보”로 바꿔 주는 것은 아니다. 중복 판별과 데이터 갱신 정책은 구분한다.

검증 코드: [ProductTest.java](examples/src/test/java/tutorial/part32/ProductTest.java)

```bash
cd examples
gradle test --tests tutorial.part32.ProductTest
```

---

## 32.6 연습문제

1. ID가 다른데 이름이 같은 상품 두 개는 동등해야 할까?
2. `equals()`에서 이름도 비교하도록 바꾸면 `hashCode()`는 어떻게 수정할까?
3. 항상 1을 반환하는 `hashCode()`는 동등성 계약을 위반할까?
4. 동일성 확인에는 JUnit의 어떤 메소드를 사용할까?

<details>
<summary>정답</summary>

1. 이 파트의 ID 기준에서는 동등하지 않다.
2. 동일한 비교 필드를 사용해 `Objects.hash(id, name)` 등을 구현한다.
3. 동등한 객체의 해시값도 같으므로 해당 계약 자체는 만족하지만 충돌이 많아 성능이 나빠질 수 있다.
4. `assertSame()`을 사용한다. `assertEquals()`와 구분한다.

</details>

공식 참고: [Object](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html)

[목차](README.md) · [다음: 불변 객체와 복사](part33_불변객체와객체복사.md)
