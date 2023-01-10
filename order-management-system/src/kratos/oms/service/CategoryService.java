package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.model.category.CreateCategoryModel;
import kratos.oms.model.category.SearchCategoryModel;
import kratos.oms.repository.CategoryRepository;
import kratos.oms.repository.ProductRepository;
import kratos.oms.seedwork.Helpers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> search(SearchCategoryModel searchModel) {
        List<Category> categories = categoryRepository.listAll();
        Stream<Category> categoryStream = categories.stream();
        if (!Helpers.isNullOrEmpty(searchModel.getName()))
            categoryStream = categoryStream.filter(c -> c.getName().toUpperCase().contains(searchModel.getName().toUpperCase()));
        return categoryStream.collect(Collectors.toList());
    }

    public Category add(CreateCategoryModel model) {
        Category newCategory = new Category(model.getName());
        categoryRepository.add(newCategory);
        return newCategory;
    }

    public boolean delete(UUID id) {
        List<Product> products = productRepository.listAll().stream()
                .filter(p -> p.getCategory().getId().equals(id)).collect(Collectors.toList());
        for (Product product : products) {
            product.setCategory(new Category(Helpers.emptyUuid(), "None"));
            productRepository.update(product);
        }
        return categoryRepository.delete(id);
    }
}
