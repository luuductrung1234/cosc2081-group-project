package kratos.oms.repository;

import kratos.oms.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    List<Category> listAll();
    Optional<Category> findById(UUID id);
    boolean add(Category category);
    boolean delete(UUID id);
}
