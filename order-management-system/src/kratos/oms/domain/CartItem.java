package kratos.oms.domain;

import kratos.oms.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartItem extends Domain<UUID> {

    private final UUID cartId;
    private final UUID productId;
    private final String productName;
    private double productPrice;
    private String productCurrency;
    private int quantity;

    public CartItem(UUID id, UUID cartId, UUID productId, String productName, double productPrice, String productCurrency, int quantity) {
        super(id);
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCurrency = productCurrency;
        this.quantity = quantity;
    }

    public CartItem(UUID cartId, UUID productId, String productName, double productPrice, String productCurrency, int quantity) {
        this(UUID.randomUUID(), cartId, productId, productName, productPrice, productCurrency, quantity);
    }

    public CartItem(UUID cartId, Product product, int quantity) {
        this(UUID.randomUUID(), cartId, product.getId(), product.getName(), product.getPrice(), product.getCurrency(), quantity);
    }

    public void update( double price, String currency) {
        this.productPrice = price;
        this.productCurrency = currency;
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(cartId.toString());
            add(productId.toString());
            add(productName);
            add(String.valueOf(productPrice));
            add(productCurrency);
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
        String[] fields = data.split(",", 7);
        return new CartItem(UUID.fromString(fields[0]),
                UUID.fromString(fields[1]),
                UUID.fromString(fields[2]),
                fields[3],
                Double.parseDouble(fields[4]),
                fields[5],
                Integer.parseInt(fields[6]));
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

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
