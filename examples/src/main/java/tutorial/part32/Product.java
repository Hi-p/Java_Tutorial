package tutorial.part32;

import java.util.Objects;

/** 이 예제는 상품 ID가 같으면 같은 상품이라는 비교 정책을 사용한다. */
public final class Product {
    private final String id;
    private final String name;

    public Product(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        if (id.isBlank()) throw new IllegalArgumentException("상품 ID가 비어 있습니다.");
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Product other && id.equals(other.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() { return "Product[id=" + id + ", name=" + name + "]"; }
}
