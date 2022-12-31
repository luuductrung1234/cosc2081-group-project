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
    private UUID orderId;
    private UUID productId;
    private int quantity;

    public OrderItem(UUID id, UUID orderId, UUID productId, int quantity) {
        super(id);
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItem(UUID orderId, UUID productId, int quantity) {
        this(UUID.randomUUID(), orderId, productId, quantity);
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(orderId.toString());
            add(productId.toString());
            add(String.valueOf(quantity));
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     * @param data serialized string data
     * @return new instance of OrderItem
     */
    public static OrderItem deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 4);
        return new OrderItem(UUID.fromString(fields[0]),
                UUID.fromString(fields[1]),
                UUID.fromString(fields[2]),
                Integer.parseInt(fields[3]));
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

    public int getQuantity() {
        return quantity;
    }
}
