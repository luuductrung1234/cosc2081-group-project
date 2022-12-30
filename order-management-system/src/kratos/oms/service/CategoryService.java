package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.model.category.CreateCategoryModel;
import kratos.oms.model.category.SearchCategoryModel;
import kratos.oms.repository.CategoryRepository;
import kratos.oms.seedwork.Helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> search(SearchCategoryModel searchModel) {
        List<Category> categories = categoryRepository.listAll();
        Stream<Category> categoryStream = categories.stream();
        if (!Helpers.isNullOrEmpty(searchModel.getName()))
            categoryStream = categoryStream.filter(c -> c.getName().toUpperCase().contains(searchModel.getName().toUpperCase()));
        return categoryStream.collect(Collectors.toList());
    }

    public Optional<Category> getDetail(UUID id) {
        return categoryRepository.listAll().stream().findFirst();
    }

    public Category add(CreateCategoryModel model) {
        Category newCategory = new Category(model.getName());
        categoryRepository.add(newCategory);
        return newCategory;
    }

    public boolean delete(UUID id) {
        return categoryRepository.delete(id);
    }
}
