# Part 5. 배열과 문자열

> 데이터를 묶어서 관리하는 두 가지 핵심 도구.

---

## 5.1 배열 — 같은 타입 여러 개 묶기

```java
int[] scores = {90, 85, 70, 95, 80};
System.out.println(scores[0]);   // 90 (인덱스는 0부터!)
System.out.println(scores[4]);   // 80
System.out.println(scores.length); // 5
```

**배열 선언 방법**

```java
// 방법 1: 값을 바로 넣기
int[] a = {1, 2, 3, 4, 5};

// 방법 2: 크기만 지정 (0으로 초기화됨)
int[] b = new int[5];
b[0] = 10;
b[1] = 20;
```

**배열 순회**

```java
int[] nums = {10, 20, 30, 40, 50};

// for문
for (int i = 0; i < nums.length; i++) {
    System.out.print(nums[i] + " ");
}

// 향상된 for문 (for-each)
for (int n : nums) {
    System.out.print(n + " ");
}
```

```
10 20 30 40 50
```

**배열 합계/최대값**

```java
int[] arr = {3, 7, 1, 9, 4};
int sum = 0, max = arr[0];

for (int n : arr) {
    sum += n;
    if (n > max) max = n;
}
System.out.println("합계: " + sum);  // 24
System.out.println("최대: " + max);  // 9
```

---

## 5.2 2차원 배열

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};

System.out.println(matrix[0][1]);  // 2
System.out.println(matrix[1][2]);  // 6

for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}
```

```
1 2 3
4 5 6
```

---

## 5.3 String — 문자열 다루기

```java
String s = "Hello, Java!";

System.out.println(s.length());         // 12
System.out.println(s.charAt(0));        // H
System.out.println(s.substring(7));     // Java!
System.out.println(s.substring(0, 5));  // Hello
System.out.println(s.toUpperCase());    // HELLO, JAVA!
System.out.println(s.toLowerCase());    // hello, java!
System.out.println(s.contains("Java")); // true
System.out.println(s.indexOf("Java"));  // 7
System.out.println(s.replace("Java", "World")); // Hello, World!
```

**주요 String 메소드**

| 메소드 | 반환 | 설명 |
|---|---|---|
| `.length()` | `int` | 문자열 길이 |
| `.charAt(i)` | `char` | i번째 문자 |
| `.substring(a, b)` | `String` | a~b-1 잘라내기 |
| `.equals(s)` | `boolean` | 내용 비교 |
| `.contains(s)` | `boolean` | 포함 여부 |
| `.indexOf(s)` | `int` | 위치 (-1이면 없음) |
| `.split(구분자)` | `String[]` | 쪼개서 배열로 |
| `.trim()` | `String` | 앞뒤 공백 제거 |
| `.replace(a, b)` | `String` | a를 b로 교체 |
| `.toCharArray()` | `char[]` | 문자 배열로 변환 |

**문자열 쪼개기 (split)**

```java
String csv = "사과,바나나,포도";
String[] fruits = csv.split(",");
for (String f : fruits) {
    System.out.println(f);
}
```

```
사과
바나나
포도
```

---

## 5.4 String은 불변(immutable)

```java
String s = "Hello";
s.toUpperCase();        // s는 바뀌지 않음!
s = s.toUpperCase();    // 새 문자열을 s에 대입해야 반영
System.out.println(s);  // HELLO
```

> String 메소드는 **새 문자열을 반환**할 뿐, 원본을 바꾸지 않는다.

---

## 5.5 자주 빠지는 함정 ⚠️

1. **인덱스 0부터** — `arr[5]`는 6번째 요소. 5개짜리 배열이면 에러!
2. **ArrayIndexOutOfBoundsException** — 배열 범위 초과 시 터짐
3. **배열 크기 변경 불가** — 생성 후 크기 고정. 유동적이면 `ArrayList` 사용
4. **문자열 비교는 `.equals()`** — `==` 쓰면 안 됨
5. **String 불변** — 메소드 결과를 변수에 다시 대입해야 함

---

## 5.6 연습문제

**1)** 배열 {5, 3, 8, 1, 9}의 최솟값 출력

**2)** 문자열을 입력받아 뒤집어서 출력

**3)** 문자열에서 특정 문자의 개수 세기

**4)** 정수 배열을 입력받아 평균 출력

---

## 해설

**1)**
```java
int[] arr = {5, 3, 8, 1, 9};
int min = arr[0];
for (int n : arr) if (n < min) min = n;
System.out.println(min);  // 1
```

**2)**
```java
String s = new Scanner(System.in).nextLine();
StringBuilder sb = new StringBuilder(s);
System.out.println(sb.reverse());
```

**3)**
```java
String s = "hello world";
char target = 'l';
int count = 0;
for (char c : s.toCharArray()) {
    if (c == target) count++;
}
System.out.println(count);  // 3
```

**4)**
```java
int[] arr = {10, 20, 30, 40, 50};
int sum = 0;
for (int n : arr) sum += n;
System.out.println((double) sum / arr.length);  // 30.0
```
