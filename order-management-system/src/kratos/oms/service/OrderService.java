package kratos.oms.service;

import kratos.oms.domain.Cart;
import kratos.oms.domain.Order;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> search(SearchOrderModel model) {
        List<Order> orders = orderRepository.listAll();
        Stream<Order> orderStream = orders.stream();
        if(model.getCode() != null)
            orderStream = orderStream.filter(o -> o.getCode().contains(model.getCode()));
        if(model.getCustomerId() != null)
            orderStream = orderStream.filter(o -> o.getAccountId().equals(model.getCustomerId()));
        if(model.getStatus() != null)
            orderStream = orderStream.filter(o -> o.getStatus().equals(model.getStatus()));

        // TODO: implement order sorting

        return orderStream.collect(Collectors.toList());
    }

    public Optional<Order> getDetail(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public boolean add(Cart cart) {
        Order order = Order.getInstance(cart);
        return orderRepository.add(order);
    }

    public boolean paid(UUID orderId, String paidBy) {
        Optional<Order> orderOpt = getDetail(orderId);
        if(orderOpt.isEmpty())
            throw new IllegalArgumentException("Order with id " + orderId + " is not found");
        Order order = orderOpt.get();
        order.paid(paidBy);
        return orderRepository.update(order);
    }

    public boolean complete(UUID orderId, String completedBy) {
        Optional<Order> orderOpt = getDetail(orderId);
        if(orderOpt.isEmpty())
            throw new IllegalArgumentException("Order with id " + orderId + " is not found");
        Order order = orderOpt.get();
        order.complete(completedBy);
        return orderRepository.update(order);
    }
}
