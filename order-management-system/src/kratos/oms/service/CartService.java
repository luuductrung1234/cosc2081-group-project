package kratos.oms.service;

import kratos.oms.domain.Cart;
import kratos.oms.domain.CartItem;
import kratos.oms.domain.Product;
import kratos.oms.domain.Role;
import kratos.oms.repository.CartRepository;

import java.util.Optional;
import java.util.UUID;

public class CartService {
    private final AuthService authService;
    private final CartRepository cartRepository;
    private Cart cachedCart;

    public CartService(AuthService authService, CartRepository cartRepository) {
        this.authService = authService;
        this.cartRepository = cartRepository;
    }

    public void addItem(Product product, int quantity) {
        if (cachedCart == null)
            load();
        Optional<CartItem> itemOpt = cachedCart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();
        if (itemOpt.isEmpty()) {
            cachedCart.addItem(new CartItem(product.getId(), quantity));
            return;
        }
        CartItem item = itemOpt.get();
        item.increase(quantity);
    }

    public void removeItem(UUID productId, int quantity) {
        if (cachedCart == null)
            load();
        Optional<CartItem> itemOpt = cachedCart.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst();
        if (itemOpt.isEmpty())
            return;
        CartItem item = itemOpt.get();
        if (quantity >= item.getQuantity()) {
            removeItem(productId);
            return;
        }
        item.decrease(quantity);
    }

    public void removeItem(UUID productId) {
        if (cachedCart == null)
            load();
        cachedCart.getItems().removeIf(item -> item.getProductId().equals(productId));
    }

    public void load() {
        if(authService.getPrincipal().getRole() != Role.CUSTOMER)
            return;
        UUID accountId = authService.getPrincipal().getId();
        double discount = 0;
        switch (authService.getPrincipal().getMembership()) {
            case SILVER:
                discount = 5;
                break;
            case GOLD:
                discount = 10;
                break;
            case PLATINUM:
                discount = 15;
                break;
        }
        cachedCart = cartRepository.findByAccountId(accountId)
                .orElse(new Cart(accountId, discount));
    }

    public void save() {
        if (cachedCart == null)
            return;
        cartRepository.addOrUpdate(cachedCart);
        cachedCart = null;
    }

    public Cart getCachedCart() {
        return cachedCart;
    }
}
