package kratos.oms.repository;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> listAll(String name, Category category, double fromPrice, double toPrice, String sortedBy);
    Optional<Product> findById(UUID id);
    boolean add(Product product);
    boolean delete(UUID id);
}
