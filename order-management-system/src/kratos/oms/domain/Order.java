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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends Domain<UUID> {
    private UUID accountId;
    private OrderStatus status;
    private List<OrderItem> items;
    private double discount;
    private Instant orderDate;
    private String paidBy;
    private Instant paidOn;
    private String completedBy;
    private Instant completedOn;

    public Order(UUID uuid, UUID accountId, OrderStatus status, List<OrderItem> items, double discount, Instant orderDate, String paidBy, Instant paidOn, String completedBy, Instant completedOn) {
        super(uuid);
        this.accountId = accountId;
        this.status = status;
        this.items = items;
        this.discount = discount;
        this.paidBy = paidBy;
        this.paidOn = paidOn;
        this.completedBy = completedBy;
        this.completedOn = completedOn;
        this.orderDate = orderDate;
    }

    public Order(UUID id, UUID accountId, List<OrderItem> items, double discount, Instant orderDate) {
        this(id, accountId, OrderStatus.CREATED, items, discount, orderDate, null, null, null, null);
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
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(accountId.toString());
            add(status.toString());
            add(String.valueOf(discount));
            add(orderDate.toString());
            add(Helpers.isNullOrEmpty(paidBy) ? "" : paidBy);
            add(paidOn == null ? "" : paidOn.toString());
            add(Helpers.isNullOrEmpty(completedBy) ? "" : completedBy);
            add(completedOn == null ? "" : completedOn.toString());
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     *
     * @param data serialized string data
     * @return new instance of Order
     */
    public static Order deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 9);
        String paidBy = null;
        Instant paidOn = null;
        String completedBy = null;
        Instant completedOn = null;
        if (!Helpers.isNullOrEmpty(fields[5]) && !Helpers.isNullOrEmpty(fields[6])) {
            paidBy = fields[5];
            paidOn = Instant.parse(fields[6]);
        }
        if (!Helpers.isNullOrEmpty(fields[7]) && !Helpers.isNullOrEmpty(fields[8])) {
            completedBy = fields[7];
            completedOn = Instant.parse(fields[8]);
        }
        return new Order(UUID.fromString(fields[0]),
                UUID.fromString(fields[1]),
                OrderStatus.valueOf(fields[2]),
                new ArrayList<>(),
                Double.parseDouble(fields[3]),
                Instant.parse(fields[4]),
                paidBy, paidOn,
                completedBy, completedOn);
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
