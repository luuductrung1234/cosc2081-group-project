package kratos.oms.service;

import kratos.oms.domain.Order;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> search(SearchOrderModel model) {
        return orderRepository.listAll();
    }

    public Optional<Order> orderDetail(SearchOrderModel model) {
        return orderRepository.findById(model.getAccountId());
    }
}
