package kratos.oms.service;

import kratos.oms.domain.Order;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> search(SearchOrderModel model) {
        List<Order> orders = orderRepository.listAll(model.getAccountId(), model.getStatus(), model.getSortedBy());
//        if(model.getSortedBy().equals("0")){
//            Collections.sort(orders, (o1, o2) -> (int) (o1.getOrderDate() - o2.getOrderDate())*1000);
//        }else if(model.getSortedBy().equals("1")){
//            Collections.sort(orders, (o1, o2) -> -(int)(o2.getOrderDate()- o1.getOrderDate())*1000);
//        }
//        need to figure out how to get the order time
//        TODO: Trung will check later

        return orders;
    }

    public Optional<Order> orderDetail(SearchOrderModel model) {
        return orderRepository.findById(model.getAccountId());
    }
}
