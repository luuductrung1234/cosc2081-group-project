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

import java.util.UUID;

public class OrderItem extends Domain<UUID> {
    private UUID orderId;
    private UUID productId;

    public OrderItem(UUID id, UUID orderId, UUID productId) {
        super(id);
        this.orderId = orderId;
        this.productId = productId;
    }

    public OrderItem(UUID orderId, UUID productId) {
        this(UUID.randomUUID(), orderId, productId);
    }

    @Override
    public String serialize() {
        return null;
    }

    /**
     * override static method Domain.deserialize
     * @param data serialized string data
     * @return new instance of Account
     */
    public static Account deserialize(String data) {
        return null;
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
}
