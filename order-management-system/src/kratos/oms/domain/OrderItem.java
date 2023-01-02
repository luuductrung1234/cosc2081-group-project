/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement: n/a
*/

package kratos.oms.domain;

import kratos.oms.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderItem extends Domain<UUID> {
    private final UUID orderId;
    private final UUID productId;
    private final String productName;
    private final double productPrice;
    private final String productCurrency;
    private final int quantity;

    public OrderItem(UUID id, UUID orderId, UUID productId, String productName, double productPrice, String productCurrency, int quantity) {
        super(id);
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCurrency = productCurrency;
        this.quantity = quantity;
    }

    public OrderItem(UUID orderId, UUID productId, String productName, double productPrice, String productCurrency, int quantity) {
        this(UUID.randomUUID(), orderId, productId, productName, productPrice, productCurrency, quantity);
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(orderId.toString());
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
     * @return new instance of OrderItem
     */
    public static OrderItem deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 7);
        return new OrderItem(UUID.fromString(fields[0]),
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

    public UUID getOrderId() {
        return orderId;
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
}
