package kratos.oms.domain;

import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart extends Domain<UUID> {
    private final UUID accountId;
    private List<CartItem> items = new ArrayList<>();
    private double discount;

    public Cart(UUID uuid, UUID accountId, double discount) {
        super(uuid);
        this.accountId = accountId;
        this.discount = discount;
    }

    public Cart(UUID accountId, double discount) {
        this(UUID.randomUUID(), accountId, discount);
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void addItems(List<CartItem> items) {
        this.items.addAll(items);
    }

    public int getTotalCount() {
        return this.items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void reset(double discount) {
        this.discount = discount;
        this.items = new ArrayList<>();
    }

    /**
     * Get total amount in VND
     */
    public double getTotalAmount() {
        double totalAmount = 0;
        for (CartItem item : items) {
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
            add(accountId.toString());
            add(String.valueOf(discount));
        }};
        return String.join(",", fields);
    }

    /**
     * override static method Domain.deserialize
     *
     * @param data serialized string data
     * @return new instance of Cart
     */
    public static Cart deserialize(String data) {
        if (Helpers.isNullOrEmpty(data))
            throw new IllegalArgumentException("data to deserialize should not be empty!");
        String[] fields = data.split(",", 3);
        return new Cart(UUID.fromString(fields[0]),
                UUID.fromString(fields[1]),
                Double.parseDouble(fields[2]));
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public double getDiscount() {
        return discount;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void printDetail() {
        System.out.printf("%-5s: %.2f%%\n", "Discount", this.getDiscount());
        System.out.printf("You have %d item(s) in cart\n\n", this.getTotalCount());
        System.out.printf("%-7s %-30s %-10s %-10s\n", "No.", "Product", "Quantity", "Price");
        System.out.println("-".repeat(70));
        if (items.isEmpty())
            Logger.printInfo("empty cart, let's buy something...");
        int itemNo = 0;
        for (CartItem item : items) {
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
