package kratos.oms.service;

import kratos.oms.domain.Product;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(SearchProductModel model) {
        return productRepository.listAll();
    }

    public Optional<Product> productDetail(UUID productId) {
        return productRepository.findById(productId);
    }
}
