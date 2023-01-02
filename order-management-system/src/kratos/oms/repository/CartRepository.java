package kratos.oms.repository;

import kratos.oms.domain.Cart;
import kratos.oms.domain.Product;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByAccountId(UUID accountId);
    boolean addOrUpdate(Cart cart);
    boolean updateItems(Product product);
}
