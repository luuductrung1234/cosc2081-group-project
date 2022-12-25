package kratos.oms.service;

import kratos.oms.domain.CartItem;
import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.model.ProductModel;
import kratos.oms.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;
    private Optional<Product> productDetail;
    private List <Product> products;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> search(ProductModel model){
        List<Product> products= (ArrayList<Product>) productRepository.listAll(model.getName(), model.getCategory(), model.getPriceFrom(), model.getPriceTo());
        if(model.getSortedBy().equals("0")){
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
        }else if(model.getSortedBy().equals("1")){
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
            Collections.sort(products, (o1, o2) -> - (int) (o1.getPrice() - o2.getPrice())*1000);
        }else if(model.getSortedBy().equals("2")){
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
            Collections.sort(products, (o1, o2) -> o2.getName().compareTo(o1.getName()));
        }else if(model.getSortedBy().equals("3")){
            Collections.sort(products, (o1, o2) -> (int) (o1.getPrice() - o2.getPrice())*1000);
            Collections.sort(products, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        return products;
    }
    public Optional<Product> showProductDetail(ProductModel model ){
        productDetail=productRepository.findById(model.getProductID());
        return productDetail;
    }

    public boolean addItem(String name, double price, Category category){
        load();
        Optional<Product> productOpt = products.stream().filter( item -> item.getId().equals(name)).findFirst();
        if(productOpt.isEmpty()){
            productRepository.add(new Product(name, price,"vnd", category));
            return true;
        }
        return false;
    }
    public boolean updateItem(String name, double price){
        load();
        Optional <Product> productOpt =products.stream().filter(item -> item.getName().equals(name)).findFirst();
        if(productOpt==null){
            return false;
        }
        productRepository.update(name,price);
        return true;
    }
    public boolean removeItem(String name){
        load();
        Optional <Product> productOpt = products.stream().filter(item->item.getName().equals(name)).findFirst();
        if(productOpt!=null){
            productRepository.delete(name);
            return true;
        }return false;
    }
    public void load(){
        products=productRepository.listAll();
    }




}
