package kratos.oms.repository;

import kratos.oms.domain.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByAccountId(UUID accountId);
    boolean addOrUpdate(Cart cart);
}
