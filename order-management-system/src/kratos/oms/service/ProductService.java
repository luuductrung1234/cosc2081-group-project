package kratos.oms.service;

import kratos.oms.domain.Product;
import kratos.oms.model.ProductModel;
import kratos.oms.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;
    private Optional<Product> productDetail;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> search(ProductModel model){
        List<Product> products= (ArrayList<Product>) productRepository.listAll(model.getName(), model.getCategory(), model.getPriceFrom(), model.getPriceTo());
        if(model.getSortedBy().equals("0")){
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice()));
        }else if(model.getSortedBy().equals("1")){
            Collections.sort(products, (o1, o2) -> - (int) (o1.getPrice() - o2.getPrice()));
        }
return products;
//sort item
    }
    public void productDetail(ProductModel model ){
        productDetail=productRepository.findById(model.getProductID());
        System.out.println(productDetail);
    }


}
