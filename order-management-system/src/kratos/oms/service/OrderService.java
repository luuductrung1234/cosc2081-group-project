package kratos.oms.service;

import kratos.oms.domain.*;
import kratos.oms.model.OrderModel;
import kratos.oms.model.ProductModel;
import kratos.oms.repository.OrderRepository;

import java.sql.Timestamp;
import java.util.*;

public class OrderService {
    private final OrderRepository orderRepository;
    private Optional<Order> orderDetail;
    private List<Order> orders;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> search(OrderModel model){
        List<Order> orders= (ArrayList<Order>) orderRepository.listAll(model.getAccountId(), model.getStatus(), model.getSortedBy());
        if(model.getSortedBy().equals("0")){
            Collections.sort(orders, (o1, o2) -> Timestamp.valueOf(o1.getOrderDate()).compareTo(Timestamp.valueOf(o2.getOrderDate())));
        }else if(model.getSortedBy().equals("1")){
            Collections.sort(orders, (o1, o2) -> -Timestamp.valueOf(o1.getOrderDate()).compareTo(Timestamp.valueOf(o2.getOrderDate())));
        }
        return orders;
    }

    public Optional<Order> showOrderDetail(OrderModel model){
        orderDetail=orderRepository.findById(model.getAccountId());
        return orderDetail;
    }
    public void checkOutOrder(Cart cart){
        List<OrderItem> orderItems= new List<OrderItem>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<OrderItem> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(OrderItem orderItem) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends OrderItem> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends OrderItem> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public OrderItem get(int index) {
                return null;
            }

            @Override
            public OrderItem set(int index, OrderItem element) {
                return null;
            }

            @Override
            public void add(int index, OrderItem element) {

            }

            @Override
            public OrderItem remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<OrderItem> listIterator() {
                return null;
            }

            @Override
            public ListIterator<OrderItem> listIterator(int index) {
                return null;
            }

            @Override
            public List<OrderItem> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        for(int i=0;i<cart.getItems().size();i++){
            orderItems.add(new OrderItem(cart.getItems().get(i).getProductId(), cart.getItems().get(i).getQuantity()));
        }
        Order newOrder= new Order(cart.getAccountId(),orderItems,cart.getDiscount());
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        newOrder.setOrderDate(String.valueOf(timestamp));
        orderRepository.add(newOrder);
    }
    public boolean updateOrder(UUID orderID){
        load();
        Optional<Order> orderOpt =orders.stream().filter(item -> item.getId().equals(orderID)).findFirst();
        if(orderOpt==null){
            return false;
        }
//        I assumed customer can change their order before it is delivered, but maybe we do not need this part.
        orderRepository.update(orderOpt.get());
        return true;
    }
    public boolean deleteOrder(UUID orderID){
        load();
        Optional<Order> orderOpt =orders.stream().filter(item -> item.getId().equals(orderID)).findFirst();
        if(orderOpt==null){
            return false;
        }
        orderRepository.delete(orderOpt.get());
        return true;

    }

    public void load(){
        this.orders=orderRepository.listAll();
    }

}
