package kratos.oms.service;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.model.product.CreateProductModel;
import kratos.oms.model.product.UpdateProductModel;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.repository.CartRepository;
import kratos.oms.repository.CategoryRepository;
import kratos.oms.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
    }

    public List<Product> search(SearchProductModel searchModel) {

        List<Product> products= (ArrayList<Product>) productRepository.listAll();

        products.stream().filter(a ->a.getName().contains(searchModel.getName())).collect(Collectors.toList());
        products.stream().filter(a ->a.getPrice() >= searchModel.getFromPrice()).collect(Collectors.toList());
        products.stream().filter(a ->a.getPrice() <= searchModel.getToPrice()).collect(Collectors.toList());
        products.stream().filter(a->a.getCategory().equals(searchModel.getCategoryId())).collect(Collectors.toList());
        if(searchModel.getSortedBy().equals("1")){
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
        }else if(searchModel.getSortedBy().equals("2")){
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> - (int) (o1.getPrice() - o2.getPrice())*1000);
        }else if(searchModel.getSortedBy().equals("3")){
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
        }else if(searchModel.getSortedBy().equals("4")){
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
            Collections.sort(products, (o1, o2) -> o1.getName().compareTo(o2.getName()));}
        return products;
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
