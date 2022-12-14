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

public class OrderItem {
    private UUID id;
    private UUID orderId;
    private UUID productId;

    public OrderItem(UUID id, UUID orderId, UUID productId) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
    }

    public OrderItem(UUID orderId, UUID productId) {
        this(UUID.randomUUID(), orderId, productId);
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
