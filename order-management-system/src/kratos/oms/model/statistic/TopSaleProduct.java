package kratos.oms.model.statistic;

import java.math.BigDecimal;
import java.util.UUID;

public class TopSaleProduct {
    private final UUID id;
    private final String name;
    private final String category;
    private final double price;
    private final int quantity;
    private final BigDecimal totalSale;

    public TopSaleProduct(UUID id, String name, String category, double price, int quantity, BigDecimal totalSale) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalSale = totalSale;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalSale() {
        return totalSale;
    }
}
