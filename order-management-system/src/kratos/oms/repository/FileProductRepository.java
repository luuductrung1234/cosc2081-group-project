package kratos.oms.repository;

import kratos.oms.domain.Account;
import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileProductRepository extends BaseFileRepository implements ProductRepository {
    public FileProductRepository(String fileUrl) {
        super(fileUrl);
    }

    @Override
    public List<Product> listAll(String name, Category category, double fromPrice, double toPrice) {
        try {
            ArrayList<Product> products=new ArrayList<>(this.read(Product.class));

            products.stream()
                    .filter(a -> a.getName().contains(name))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            products.stream()
                    .filter(a -> a.getPrice()>=fromPrice)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            products.stream()
                    .filter(a -> a.getPrice()<=toPrice)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            return products;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }
    public List<Product> listAll() {
        try {
            return new ArrayList<>(this.read(Product.class));
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return listAll().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public boolean add(Product product) {
        List<Product> products = listAll();
        products.add(product);
        try {
            this.write(products);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }
    public boolean update(String name, double price){
        List<Product> products = listAll();
        products.removeIf(a -> a.getId().equals(name));
        products.add(new Product(name, price,"vnd",products.stream().filter(item -> item.getName().equals(name)).findFirst().get().getCategory()));
        try{
            this.write(products);
            return true;
        }catch (IOException e){
            Logger.printError(this.getClass().getName(),"update",e);
            return false;
        }
    }

    @Override
    public boolean delete(String name) {
        List<Product> products = listAll();
        products.removeIf(a -> a.getName().equals(name));
        try {
            this.write(products);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }
}
