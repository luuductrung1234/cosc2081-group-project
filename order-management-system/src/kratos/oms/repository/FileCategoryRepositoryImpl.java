package kratos.oms.repository;

import kratos.oms.domain.Category;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileCategoryRepositoryImpl extends BaseFileRepository implements CategoryRepository {
    private final static String DATA_FILE_NAME = "categories.txt";

    public FileCategoryRepositoryImpl(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public List<Category> listAll() {
        try {
            return this.read(DATA_FILE_NAME, Category.class);
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return listAll().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public boolean add(Category category) {
        List<Category> categories = listAll();
        Optional<Category> existingCategory = categories.stream()
                .filter(c -> c.getId().equals(category.getId())).findFirst();
        if (existingCategory.isPresent())
            return false;
        categories.add(category);
        try {
            this.write(DATA_FILE_NAME, categories);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        List<Category> categories = listAll();
        try {
            if (categories.removeIf(a -> a.getId().equals(id))) {
                this.write(DATA_FILE_NAME, categories);
                return true;
            }
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "delete", e);
        }
        return false;
    }
}
