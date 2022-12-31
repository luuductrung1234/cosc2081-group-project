package kratos.oms.model.product;

import kratos.oms.seedwork.*;

import java.util.UUID;

public class UpdateProductModel {
    private UUID productId;
    @NotNull
    @GreaterThan(value = 1, message = "Given price must be greater than 1")
    private Double price;
    @NotBlank
    @Length(max = 3, min = 3, message = "Given currency must have valid length with 3 characters.")
    private String currency;
    @NotNull
    @NotEmpty(message = "Category Id is not valid")
    private UUID categoryId;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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
