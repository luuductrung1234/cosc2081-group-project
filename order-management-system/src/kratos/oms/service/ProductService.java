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
        List<Product> products = productRepository.listAll(model.getName(), model.getCategory(), model.getPriceFrom(), model.getPriceTo());
        if (model.getSortedBy().equals("0")) {
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()) * 1000);
        } else if (model.getSortedBy().equals("1")) {
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> -(int) (o1.getPrice() - o2.getPrice()) * 1000);
        } else if (model.getSortedBy().equals("2")) {
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()) * 1000);
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
        } else if (model.getSortedBy().equals("3")) {
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()) * 1000);
            Collections.sort(products, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        return products;
    }

    public Optional<Product> productDetail(SearchProductModel model) {
        return productRepository.findById(model.getProductID());
    }


}
