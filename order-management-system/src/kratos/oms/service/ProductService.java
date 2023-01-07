package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.model.product.CreateProductModel;
import kratos.oms.model.product.ProductSort;
import kratos.oms.model.product.UpdateProductModel;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.repository.CartRepository;
import kratos.oms.repository.CategoryRepository;
import kratos.oms.repository.ProductRepository;
import kratos.oms.seedwork.Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
    }

    public List<Product> search(SearchProductModel model) {
        Stream<Product> stream = productRepository.listAll().stream();
        if(!Helpers.isNullOrEmpty(model.getName()))
            stream = stream.filter(p -> p.getName().toUpperCase().contains(model.getName().toUpperCase()));
        if(model.getFromPrice() != null)
            stream = stream.filter(p -> p.getPrice() >= model.getFromPrice());
        if(model.getToPrice() != null)
            stream = stream.filter(p -> p.getPrice() <= model.getToPrice());
        if(model.getCategoryId() != null)
            stream = stream.filter(p -> p.getCategory().getId().equals(model.getCategoryId()));
        if(model.getSortedBy() != null) {
            switch (model.getSortedBy()) {
                case NameAscending:
                    stream = stream.sorted(Comparator.comparing(Product::getName));
                    break;
                case NameDescending:
                    stream = stream.sorted(Comparator.comparing(Product::getName).reversed());
                    break;
                case PriceAscending:
                    stream = stream.sorted(Comparator.comparingDouble(Product::getPrice));
                    break;
                case PriceDescending:
                    stream = stream.sorted(Comparator.comparingDouble(Product::getPrice).reversed());
                    break;
            }
        }
        return stream.collect(Collectors.toList());
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
        productRepository.update(product);
        cartRepository.updateItems(product);
        return product;
    }

    public boolean delete(UUID id) {
        return productRepository.delete(id);
    }
}
