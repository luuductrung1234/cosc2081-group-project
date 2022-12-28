package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.model.category.SearchCategoryModel;
import kratos.oms.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> search(SearchCategoryModel searchModel) {
        return categoryRepository.listAll();
    }

    public Optional<Category> getDetail(UUID id) {
        return categoryRepository.listAll().stream().findFirst();
    }
}
