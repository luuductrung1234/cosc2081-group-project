package kratos.oms.domain;

import java.util.UUID;

public class CartItem {
    private UUID productId;
    private int quantity;

    public CartItem(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void increase(int quantity) {
        this.quantity += quantity;
    }

    public void decrease(int quantity) {
        this.quantity -= quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
