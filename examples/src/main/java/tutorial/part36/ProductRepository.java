package tutorial.part36;

import java.util.Optional;

@FunctionalInterface
public interface ProductRepository {
    Optional<Product> findById(String id);
}
