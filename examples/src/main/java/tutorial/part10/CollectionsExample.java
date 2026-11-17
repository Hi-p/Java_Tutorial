package tutorial.part10;

import java.util.*;

public class CollectionsExample {
    // record 자체는 Part 26, 객체 비교 규칙은 Part 32에서 설명한다.
    record Product(String id, int price) implements Comparable<Product> {
        @Override
        public int compareTo(Product other) {
            int byId = id.compareTo(other.id);
            return byId != 0 ? byId : Integer.compare(price, other.price);
        }
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4));
        numbers.remove(Integer.valueOf(2)); // 인덱스가 아닌 값 2 제거
        numbers.removeIf(n -> n % 2 == 0);
        System.out.println("남은 수: " + numbers);

        Queue<String> waiting = new ArrayDeque<>();
        waiting.offer("order-1");
        waiting.offer("order-2");
        System.out.println("FIFO: " + waiting.poll() + ", " + waiting.poll());

        Deque<String> undo = new ArrayDeque<>();
        undo.push("상품 추가");
        undo.push("수량 변경");
        System.out.println("실행 취소: " + undo.pop());

        PriorityQueue<Integer> priority = new PriorityQueue<>(List.of(30, 10, 20));
        List<Integer> ordered = new ArrayList<>();
        while (!priority.isEmpty()) ordered.add(priority.poll());
        System.out.println("우선순위: " + ordered);

        List<Product> products = new ArrayList<>(List.of(
                new Product("P003", 1000), new Product("P001", 2000),
                new Product("P002", 1000)));
        products.sort(Comparator.naturalOrder());
        System.out.println("기본 정렬: " + products.stream().map(Product::id).toList());
        products.sort(Comparator.comparingInt(Product::price).thenComparing(Product::id));
        System.out.println("가격·ID 정렬: " + products.stream().map(Product::id).toList());

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String id : List.of("P002", "P001", "P002")) counts.merge(id, 1, Integer::sum);
        System.out.println("삽입 순서: " + counts);
        System.out.println("키 정렬: " + new TreeMap<>(counts));
    }
}
