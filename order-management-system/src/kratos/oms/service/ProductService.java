package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.model.product.CreateProductModel;
import kratos.oms.model.product.UpdateProductModel;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.repository.CategoryRepository;
import kratos.oms.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> search(SearchProductModel searchModel) {
        return productRepository.listAll();
    }

    public Optional<Product> getDetail(UUID productId) {
        return productRepository.findById(productId);
    }

    public Product add(CreateProductModel model) {
        Optional<Category> categoryOpt = categoryRepository.findById(model.getCategoryId());
        if (categoryOpt.isEmpty())
            throw new IllegalStateException("Category is not existed");
        Product newProduct = new Product(model.getName(),
                model.getPrice(),
                model.getCurrency(),
                categoryOpt.get());
        productRepository.add(newProduct);
        return newProduct;
    }

    public Product update(UpdateProductModel model) {
        Optional<Category> categoryOpt = categoryRepository.findById(model.getCategoryId());
        if (categoryOpt.isEmpty())
            throw new IllegalStateException("Category is not existed");
        Optional<Product> productOpt = productRepository.findById(model.getProductId());
        if (productOpt.isEmpty())
            throw new IllegalStateException("Product is not existed");
        Product product = productOpt.get();
        product.setPrice(model.getPrice());
        product.setCurrency(model.getCurrency());
        product.setCategory(categoryOpt.get());
        return product;
    }
}
