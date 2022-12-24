package kratos.oms.repository;

import kratos.oms.domain.Order;
import kratos.oms.domain.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    List<Order> listAll(UUID accountId, OrderStatus status, String sortedBy);
    Optional<Order> findById(UUID id);
    boolean add(Order order);
    boolean update(Order order);
    boolean delete(Order order);
//    can I add update and delete here?
}
