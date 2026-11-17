package tutorial.part32;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EqualityExample {
    public static void main(String[] args) {
        Product first = new Product("P001", "노트");
        Product second = new Product("P001", "노트 개정판");
        Product alias = first;
        System.out.println("first == second: " + (first == second));
        System.out.println("first == alias: " + (first == alias));
        System.out.println("equals: " + first.equals(second));
        System.out.println("같은 hashCode: " + (first.hashCode() == second.hashCode()));
        Set<Product> products = new HashSet<>(List.of(first, second));
        System.out.println("중복 제거 후 크기: " + products.size());
        Map<Product, Integer> stocks = new HashMap<>();
        stocks.put(first, 10);
        System.out.println("동등한 키로 조회: " + stocks.get(second));
        System.out.println(first);
    }
}
