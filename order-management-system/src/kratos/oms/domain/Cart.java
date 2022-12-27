package kratos.oms.domain;

import kratos.oms.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart extends Domain<UUID> {
    private UUID accountId;
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
}
