# Part 30. 연습문제

> 파트별 종합 연습문제. 직접 풀어보고 해설과 비교하자.

---

## 🟢 초급

### 문제 1. 계산기
두 정수와 연산자(`+`, `-`, `*`, `/`)를 입력받아 결과를 출력하세요.

```
입력: 10 + 3
출력: 13
```

### 문제 2. 별 찍기
정수 n을 입력받아 직각삼각형 모양으로 별을 출력하세요.

```
입력: 4
출력:
*
**
***
****
```

### 문제 3. 최대공약수
두 정수의 최대공약수(GCD)를 구하세요. (유클리드 호제법)

### 문제 4. 회문 검사
문자열을 입력받아 회문(앞뒤 같은 단어)인지 판별하세요.

```
입력: "level"
출력: true
```

### 문제 5. 배열 정렬
정수 배열을 입력받아 오름차순으로 정렬하여 출력하세요. (직접 구현)

---

## 🔵 심화

### 문제 6. 학생 관리
`Student` 클래스를 만들고, `ArrayList<Student>`에 학생을 추가/삭제/조회하는 프로그램을 작성하세요.
- 필드: 이름, 학번, 점수
- 메소드: 전체 출력, 평균 계산, 최고 점수 학생 찾기

### 문제 7. 도형 넓이 (다형성)
`Shape` 추상클래스를 상속하는 `Circle`, `Rectangle`, `Triangle`을 만들고, 배열에 넣어 반복문으로 각 넓이를 출력하세요.

### 문제 8. 단어 빈도수
문장을 입력받아 각 단어의 등장 횟수를 `HashMap`으로 세어 출력하세요.

```
입력: "the cat sat on the mat the cat"
출력: {the=3, cat=2, sat=1, on=1, mat=1}
```

### 문제 9. 스트림 활용
정수 리스트에서 짝수만 골라 제곱한 뒤 내림차순 정렬하여 출력하세요. (Stream API)

### 문제 10. 파일 단어 수 세기
텍스트 파일을 읽어서 총 단어 수, 줄 수, 문자 수를 출력하세요.

---

## 해설

### 1. 계산기
```java
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
String op = sc.next();
int b = sc.nextInt();

int result = switch (op) {
    case "+" -> a + b;
    case "-" -> a - b;
    case "*" -> a * b;
    case "/" -> a / b;
    default -> throw new IllegalArgumentException("알 수 없는 연산자");
};
System.out.println(result);
```

### 2. 별 찍기
```java
int n = new Scanner(System.in).nextInt();
for (int i = 1; i <= n; i++) {
    System.out.println("*".repeat(i));
}
```

### 3. 최대공약수
```java
public static int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}
```

### 4. 회문 검사
```java
String s = new Scanner(System.in).nextLine();
String reversed = new StringBuilder(s).reverse().toString();
System.out.println(s.equals(reversed));
```

### 5. 배열 정렬 (버블 정렬)
```java
int[] arr = {5, 3, 8, 1, 9};
for (int i = 0; i < arr.length - 1; i++) {
    for (int j = 0; j < arr.length - 1 - i; j++) {
        if (arr[j] > arr[j + 1]) {
            int temp = arr[j];
            arr[j] = arr[j + 1];
            arr[j + 1] = temp;
        }
    }
}
```

### 6. 학생 관리
```java
public class Student {
    private String name;
    private int id;
    private int score;

    Student(String name, int id, int score) {
        this.name = name; this.id = id; this.score = score;
    }

    // getter, toString 생략
}

ArrayList<Student> students = new ArrayList<>();
students.add(new Student("민수", 1, 90));
students.add(new Student("영희", 2, 85));

// 평균
double avg = students.stream()
    .mapToInt(Student::getScore).average().orElse(0);

// 최고 점수
Student top = students.stream()
    .max(Comparator.comparingInt(Student::getScore))
    .orElseThrow();
```

### 7. 도형 넓이
```java
abstract class Shape {
    abstract double area();
}
class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    double area() { return Math.PI * r * r; }
}
class Rectangle extends Shape {
    double w, h;
    Rectangle(double w, double h) { this.w = w; this.h = h; }
    double area() { return w * h; }
}

Shape[] shapes = { new Circle(5), new Rectangle(3, 4) };
for (Shape s : shapes) {
    System.out.printf("%.2f%n", s.area());
}
```

### 8. 단어 빈도수
```java
String input = "the cat sat on the mat the cat";
HashMap<String, Integer> freq = new HashMap<>();
for (String word : input.split(" ")) {
    freq.merge(word, 1, Integer::sum);
}
System.out.println(freq);
```

### 9. 스트림 활용
```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> result = nums.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
System.out.println(result);  // [100, 64, 36, 16, 4]
```

### 10. 파일 단어 수 세기
```java
Path path = Path.of("sample.txt");
List<String> lines = Files.readAllLines(path);
long lineCount = lines.size();
long wordCount = lines.stream()
    .flatMap(line -> Arrays.stream(line.split("\\s+")))
    .count();
long charCount = lines.stream()
    .mapToInt(String::length).sum();

System.out.printf("줄: %d, 단어: %d, 문자: %d%n", lineCount, wordCount, charCount);
```
