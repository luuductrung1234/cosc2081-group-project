package kratos.oms.service;

import kratos.oms.domain.Cart;
import kratos.oms.domain.CartItem;
import kratos.oms.domain.Product;
import kratos.oms.domain.Role;
import kratos.oms.repository.CartRepository;

import java.util.Optional;
import java.util.UUID;

public class CartService {
    private static final int SILVER_DISCOUNT = 5;
    private static final int GOLD_DISCOUNT = 10;
    private static final int PLATINUM_DISCOUNT = 15;

    private final AuthService authService;
    private final CartRepository cartRepository;
    private Cart cachedCart;

    public CartService(AuthService authService, CartRepository cartRepository) {
        this.authService = authService;
        this.cartRepository = cartRepository;
    }

    public void addItem(Product product, int quantity) {
        if(authService.getPrincipal().getRole() != Role.CUSTOMER)
            throw new IllegalStateException("Logged in user is not allow to add item to cart");
        if (cachedCart == null)
            throw new IllegalStateException("cache cart is not loaded");
        Optional<CartItem> itemOpt = cachedCart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();
        if (itemOpt.isEmpty()) {
            cachedCart.addItem(new CartItem(cachedCart.getId(), product, quantity));
        } else {
            CartItem item = itemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartRepository.addOrUpdate(cachedCart);
    }

    public void updateItem(int index, int quantity) {
        if(index < 0 || index >= cachedCart.getItems().size())
            throw new IllegalArgumentException("Invalid cart item index");
        if(authService.getPrincipal().getRole() != Role.CUSTOMER)
            throw new IllegalStateException("Logged in user is not allow to update item in cart");
        if (cachedCart == null)
            throw new IllegalStateException("cache cart is not loaded");
        cachedCart.getItems().get(index).setQuantity(quantity);
        cartRepository.addOrUpdate(cachedCart);
    }

    public void removeItem(int index) {
        if(index < 0 || index >= cachedCart.getItems().size())
            throw new IllegalArgumentException("Invalid cart item index");
        if(authService.getPrincipal().getRole() != Role.CUSTOMER)
            throw new IllegalStateException("Logged in user is not allow to remove item from cart");
        if (cachedCart == null)
            throw new IllegalStateException("cache cart is not loaded");
        cachedCart.getItems().remove(index);
        cartRepository.addOrUpdate(cachedCart);
    }

    public void load() {
        if(authService.getPrincipal().getRole() != Role.CUSTOMER)
            return;
        UUID accountId = authService.getPrincipal().getId();
        Optional<Cart> cart = cartRepository.findByAccountId(accountId);
        if(cart.isPresent()) {
            cachedCart = cart.get();
            cachedCart.setDiscount(getDiscount());
        }
        else
            cachedCart = new Cart(accountId, getDiscount());
        cartRepository.addOrUpdate(cachedCart);
    }

    public void reset() {
        cachedCart.reset(getDiscount());
        cartRepository.addOrUpdate(cachedCart);
    }

    public void unload() {
        if (cachedCart == null)
            return;
        cachedCart = null;
    }

    public Cart getCachedCart() {
        return cachedCart;
    }

    private double getDiscount() {
        double discount = 0;
        switch (authService.getPrincipal().getMembership()) {
            case SILVER:
                discount = SILVER_DISCOUNT;
                break;
            case GOLD:
                discount = GOLD_DISCOUNT;
                break;
            case PLATINUM:
                discount = PLATINUM_DISCOUNT;
                break;
        }
        return discount;
    }
}
