package kratos.oms.repository;

import kratos.oms.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    List<Order> listAll();
    Optional<Order> findById(UUID id);
    boolean add(Order order);
    boolean update(Order order);
    boolean delete(UUID id);
}
