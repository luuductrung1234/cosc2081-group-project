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
import kratos.oms.seedwork.RandomString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Order extends Domain<UUID> {
    private final String code;
    private final UUID accountId;
    private OrderStatus status;
    private final List<OrderItem> items;
    private final double discount;
    private final Instant orderDate;
    private String paidBy;
    private Instant paidOn;
    private String completedBy;
    private Instant completedOn;

    public Order(UUID uuid, String code, UUID accountId, OrderStatus status, List<OrderItem> items, double discount, Instant orderDate, String paidBy, Instant paidOn, String completedBy, Instant completedOn) {
        super(uuid);
        this.code = code;
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
        this(id, RandomString.getEasyCode(), accountId, OrderStatus.CREATED, items, discount, orderDate, null, null, null, null);
    }

    public Order(UUID accountId, List<OrderItem> items, double discount, Instant orderDate) {
        this(UUID.randomUUID(), accountId, items, discount, orderDate);
    }

    public Order(UUID accountId, double discount) {
        this(UUID.randomUUID(), accountId, new ArrayList<>(), discount, Instant.now());
    }

    public static Order getInstance(Cart cart) {
        Order order = new Order(cart.getAccountId(), cart.getDiscount());
        order.addItems(cart.getItems().stream().map(i ->
                new OrderItem(order.getId(), i.getProductId(), i.getProductName(),
                        i.getProductPrice(), i.getProductCurrency(), i.getQuantity())).collect(Collectors.toList()));
        return order;
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

    public int getTotalCount() {
        return this.items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    /**
     * Get total amount in VND
     */
    public double getTotalAmount() {
        double totalAmount = 0;
        for (OrderItem item : items) {
            // TODO: convert price that currency is not VND
            totalAmount += item.getProductPrice() * item.getQuantity();
        }
        totalAmount = (totalAmount * (100 - discount)) / 100;
        return totalAmount;
    }

    @Override
    public String serialize() {
        List<String> fields = new ArrayList<>() {{
            add(id.toString());
            add(code);
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
        String[] fields = data.split(",", 10);
        String paidBy = null;
        Instant paidOn = null;
        String completedBy = null;
        Instant completedOn = null;
        if (!Helpers.isNullOrEmpty(fields[6]) && !Helpers.isNullOrEmpty(fields[7])) {
            paidBy = fields[6];
            paidOn = Instant.parse(fields[7]);
        }
        if (!Helpers.isNullOrEmpty(fields[8]) && !Helpers.isNullOrEmpty(fields[9])) {
            completedBy = fields[8];
            completedOn = Instant.parse(fields[9]);
        }
        return new Order(UUID.fromString(fields[0]),
                fields[1],
                UUID.fromString(fields[2]),
                OrderStatus.valueOf(fields[3]),
                new ArrayList<>(),
                Double.parseDouble(fields[4]),
                Instant.parse(fields[5]),
                paidBy, paidOn,
                completedBy, completedOn);
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
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

    public void printDetail() {
        System.out.printf("%-5s: %s \n", "Id", this.getId());
        System.out.printf("%-5s: %s \n", "Code", this.getCode());
        System.out.printf("%-5s: %s\n", "Status", this.getStatus());
        System.out.printf("%-5s: %.2f%%\n", "Discount", this.getDiscount());
        System.out.printf("%-5s: %s\n", "Date", this.getOrderDate());
        System.out.printf("%-10s: %-20s %-10s: %-20s\n", "Paid By", this.getPaidBy(),
                "Paid On", this.getPaidOn());
        System.out.printf("%-10s: %-20s %-10s: %-20s\n", "Completed By", this.getCompletedBy(),
                "Completed On", this.getCompletedOn());
        System.out.printf("There are %d item(s) in order\n\n", this.getTotalCount());
        System.out.printf("%-7s %-30s %-10s %-10s\n", "No.", "Product", "Quantity", "Price");
        System.out.println("-".repeat(70));
        int itemNo = 0;
        for (OrderItem item : items) {
            System.out.printf("%-7s %-30s %-10s %-10s\n", itemNo, item.getProductName(), item.getQuantity(),
                    Helpers.toString(item.getProductPrice(), item.getProductCurrency(), true));
            itemNo++;
        }
        System.out.println("-".repeat(70));
        // VND by default
        System.out.printf("%-38s %-10s %-10s\n", "", "Total:",
                Helpers.toString(this.getTotalAmount(), "VND", true));
    }
}
