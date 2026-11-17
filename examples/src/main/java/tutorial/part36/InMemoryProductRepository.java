package tutorial.part36;

import java.util.Map;
import java.util.Optional;

public final class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products;
    public InMemoryProductRepository(Map<String, Product> products) {
        this.products = Map.copyOf(products);
    }
    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }
}
