package kratos.oms.repository;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileProductRepository extends BaseFileRepository<UUID, Product> implements ProductRepository {
    private final static String DATA_FILE_NAME = "products.txt";

    public FileProductRepository(String directoryUrl) {
        super(directoryUrl, DATA_FILE_NAME);
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

    @Override
    public boolean delete(UUID id) {
        List<Product> products = listAll();
        products.removeIf(a -> a.getId().equals(id));
        try {
            this.write(products);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }
}
