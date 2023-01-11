package kratos.oms.service;

import kratos.oms.domain.Cart;
import kratos.oms.domain.Order;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.repository.OrderRepository;

import java.util.Comparator;
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
        Stream<Order> stream = orderRepository.listAll().stream();
        if(model.getCode() != null)
            stream = stream.filter(o -> o.getCode().contains(model.getCode()));
        if(model.getCustomerId() != null)
            stream = stream.filter(o -> o.getAccountId().equals(model.getCustomerId()));
        if(model.getStatus() != null)
            stream = stream.filter(o -> o.getStatus().equals(model.getStatus()));
        if(model.getSortedBy() != null) {
            switch (model.getSortedBy()) {
                case AmountAscending:
                    stream = stream.sorted(Comparator.comparingDouble(Order::getTotalAmount));
                    break;
                case AmountDescending:
                    stream = stream.sorted(Comparator.comparingDouble(Order::getTotalAmount).reversed());
                    break;
                case DateAscending:
                    stream = stream.sorted(Comparator.comparing(Order::getOrderDate));
                    break;
                case DateDescending:
                    stream = stream.sorted(Comparator.comparing(Order::getOrderDate).reversed());
                    break;
            }
        } else {
            stream = stream.sorted(Comparator.comparing(Order::getOrderDate).reversed());
        }
        return stream.collect(Collectors.toList());
    }

    public Optional<Order> getDetail(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public boolean add(Cart cart) {
        Order order = Order.getInstance(cart);
        return orderRepository.add(order);
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
