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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends Domain<UUID> {
    private UUID accountId;
    private OrderStatus status;
    private List<OrderItem> items;
    private double discount;
    private String paidBy;
    private Instant paidOn;
    private String completedBy;
    private Instant completedOn;
    private Instant orderDate;

    public Order(UUID id, UUID accountId, List<OrderItem> items, double discount, Instant orderDate) {
        super(id);
        this.accountId = accountId;
        this.orderDate = orderDate;
        this.status = OrderStatus.CREATED;
        this.items = items;
        this.discount = discount;
    }

    public Order(UUID accountId, List<OrderItem> items, double discount, Instant orderDate) {
        this(UUID.randomUUID(), accountId, items, discount, orderDate);
    }

    public Order(UUID accountId, double discount) {
        this(UUID.randomUUID(), accountId, new ArrayList<>(), discount, Instant.now());
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addItems(List<OrderItem> items) {
        this.items.addAll(items);
    }

    public void paid(String paidBy) {
        this.status = OrderStatus.DELIVERED;
        this.paidBy = paidBy;
        this.paidOn = Instant.now();
    }

    public void complete(String completedBy) {
        this.status = OrderStatus.PAID;
        this.completedBy = completedBy;
        this.completedOn = Instant.now();
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

    public UUID getAccountId() {
        return accountId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public Instant getPaidOn() {
        return paidOn;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public Instant getCompletedOn() {
        return completedOn;
    }

    public Instant getOrderDate() {
        return orderDate;
    }
}
