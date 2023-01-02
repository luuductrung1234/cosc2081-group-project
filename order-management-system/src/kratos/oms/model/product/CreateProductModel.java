package kratos.oms.model.product;

import kratos.oms.seedwork.*;

import java.util.UUID;

public class CreateProductModel {
    @NotNull
    @Length(max = 50, min = 1, message = "Given name must have valid length between 1 and 50 characters.")
    @NotContain(value = ",", message = "Given name should not contains ',' character")
    private String name;
    @NotNull
    @GreaterThan(value = 1, message = "Given price must be greater than 1")
    private Double price;
    @NotNull
    @Length(max = 3, min = 3, message = "Given currency must have valid length with 3 characters.")
    private String currency;
    @NotNull
    @NotEmpty(message = "Category Id is not valid")
    private UUID categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
