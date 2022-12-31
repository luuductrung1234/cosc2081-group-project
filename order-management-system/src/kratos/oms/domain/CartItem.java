package kratos.oms.domain;

import kratos.oms.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartItem extends Domain<UUID> {

    private UUID cartId;
    private UUID productId;
    private int quantity;

    public CartItem(UUID id, UUID cartId, UUID productId, int quantity) {
        super(id);
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItem(UUID cartId, UUID productId, int quantity) {
        this(UUID.randomUUID(), cartId, productId, quantity);
    }

    public void increase(int quantity) {
        this.quantity += quantity;
    }

    public void decrease(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(cartId.toString());
            add(productId.toString());
            add(String.valueOf(quantity));
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     *
     * @param data serialized string data
     * @return new instance of CartItem
     */
    public static CartItem deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 4);
        return new CartItem(UUID.fromString(fields[0]),
                UUID.fromString(fields[1]),
                UUID.fromString(fields[2]),
                Integer.parseInt(fields[3]));
    }

    public UUID getId() {
        return id;
    }

    public UUID getCartId() {
        return cartId;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
