package kratos.oms.repository;

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileProductRepository extends BaseFileRepository implements ProductRepository {
    private final static String DATA_FILE_NAME = "products.txt";

    public FileProductRepository(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public List<Product> listAll(String name, Category category, double fromPrice, double toPrice) {
        return new ArrayList<>();
    }

    public List<Product> listAll() {
        try {
            return this.read(DATA_FILE_NAME, Product.class);
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
            this.write(DATA_FILE_NAME, products);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        List<Product> products = listAll();
        try {
            if (products.removeIf(a -> a.getId().equals(id))) {
                this.write(DATA_FILE_NAME, products);
                return true;
            }
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
        }
        return false;
    }
}
