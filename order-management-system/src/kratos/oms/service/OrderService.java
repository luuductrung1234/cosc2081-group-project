package kratos.oms.service;

import kratos.oms.domain.Cart;
import kratos.oms.domain.Order;
import kratos.oms.domain.Product;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.repository.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> search(SearchOrderModel model) {
        List<Order> orders= (ArrayList<Order>) orderRepository.listAll();

        orders.stream().filter(a ->a.getAccountId().equals(model.getAccountId())).filter(Objects::nonNull).collect(Collectors.toList());
        orders.stream().filter(a ->a.getStatus().equals(model.getStatus())).filter(Objects::nonNull).collect(Collectors.toList());

        if(model.getSortedBy().equals("1")){
            Collections.sort(orders, (o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
        }else if(model.getSortedBy().equals("2")){
            Collections.sort(orders, (o1, o2) -> o1.getOrderDate().compareTo(o2.getOrderDate()));
        }
        return orders;
    }

    public Optional<Order> orderDetail(SearchOrderModel model) {
        return orderRepository.findById(model.getAccountId());
    }

    public boolean add(Cart cart) {
        Order order = Order.getInstance(cart);
        return orderRepository.add(order);
    }
}
