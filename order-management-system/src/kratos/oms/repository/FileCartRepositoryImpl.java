package kratos.oms.repository;

import kratos.oms.domain.Cart;
import kratos.oms.domain.CartItem;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileCartRepositoryImpl extends BaseFileRepository implements CartRepository {
    private final static String DATA_FILE_NAME = "carts.txt";
    private final static String DATA_ITEM_FILE_NAME = "cartItems.txt";

    public FileCartRepositoryImpl(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public Optional<Cart> findByAccountId(UUID accountId) {
        return listAll().stream().filter(a -> a.getAccountId().equals(accountId)).findFirst();
    }

    @Override
    public boolean addOrUpdate(Cart cart) {
        List<Cart> carts = listAll();
        Optional<Cart> exitingCart = findByAccountId(cart.getAccountId());
        exitingCart.ifPresent(this::delete);
        carts.add(cart);
        List<CartItem> cartItems = carts.stream()
                .flatMap(c -> c.getItems().stream()).collect(Collectors.toList());
        try {
            this.write(DATA_FILE_NAME, carts);
            this.write(DATA_ITEM_FILE_NAME, cartItems);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "addOrUpdate", e);
            return false;
        }
    }

    private List<Cart> listAll() {
        try {
            List<Cart> carts = this.read(DATA_FILE_NAME, Cart.class);
            List<CartItem> items = this.read(DATA_ITEM_FILE_NAME, CartItem.class);
            for (var cart : carts) {
                List<CartItem> cartItems = items.stream()
                        .filter(i -> i.getCartId().equals(cart.getId()))
                        .collect(Collectors.toList());
                cart.addItems(cartItems);
            }
            return carts;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }

    private void delete(Cart cart) {
        try {
            List<Cart> carts = this.read(DATA_FILE_NAME, Cart.class);
            List<CartItem> items = this.read(DATA_ITEM_FILE_NAME, CartItem.class);
            carts.removeIf(c -> c.getId().equals(cart.getId()));
            List<UUID> cartItemIds = cart.getItems().stream().map(CartItem::getId).collect(Collectors.toList());
            items.removeIf(i -> cartItemIds.contains(i.getId()));
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "delete", e);
        }
    }
}
