package kratos.oms.model.product;

import java.util.UUID;

public class SearchProductModel {
    private String name;
    private Double fromPrice;
    private Double toPrice;
    private UUID categoryId;
    private ProductSort sortedBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(Double fromPrice) {
        this.fromPrice = fromPrice;
    }

    public Double getToPrice() {
        return toPrice;
    }

    public void setToPrice(Double toPrice) {
        this.toPrice = toPrice;
    }

    public ProductSort getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(ProductSort sortedBy) {
        this.sortedBy = sortedBy;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
