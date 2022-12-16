package kratos.oms.domain;

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
        return null;
    }

    /**
     * override static method Domain.deserialize
     * @param data serialized string data
     * @return new instance of Cart
     */
    public static Cart deserialize(String data) {
        return null;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }
}
