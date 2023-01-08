package kratos.oms.service;

import kratos.oms.domain.Order;
import kratos.oms.domain.OrderItem;
import kratos.oms.domain.OrderStatus;
import kratos.oms.domain.Product;
import kratos.oms.model.statistic.OrderRevenue;
import kratos.oms.model.statistic.TopSaleProduct;
import kratos.oms.repository.OrderRepository;
import kratos.oms.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public StatisticService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get a list of orders in particular date and calculate total revenue
     * @param date date to filter completed orders
     * @return a list of order and revenue
     */
    public OrderRevenue getRevenue(Instant date) {
        List<Order> orders = getPaidOrder(date);
        BigDecimal revenue = BigDecimal.ZERO;
        for (Order order : orders) {
            revenue = revenue.add(BigDecimal.valueOf(order.getTotalAmount()));
        }
        return new OrderRevenue(revenue, orders.stream()
                .sorted(Comparator.comparing(Order::getTotalAmount).reversed())
                .collect(Collectors.toList()));
    }

    /**
     * Get top sale products had been bought by customer in particular date (sorted by quantity)
     * @param date date to filter completed orders
     * @return a list of top sale products
     */
    public List<TopSaleProduct> getTopSaleProducts(Instant date) {
        List<TopSaleProduct> topSaleProducts = new ArrayList<>();
        List<Product> products = productRepository.listAll();
        List<OrderItem> orderItems = getPaidOrder(date).stream()
                .flatMap(o -> o.getItems().stream()).collect(Collectors.toList());
        for (Product product : products) {
            List<OrderItem> items = orderItems.stream().filter(i ->
                    i.getProductId().equals(product.getId())).collect(Collectors.toList());
            int quantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
            BigDecimal amount = BigDecimal.ZERO;
            for (OrderItem item : items) {
                amount = amount.add(BigDecimal.valueOf(item.getQuantity() * item.getProductPrice()));
            }
            topSaleProducts.add(new TopSaleProduct(product.getId(), product.getName(),
                    product.getCategory().getName(),
                    product.getPrice(),
                    quantity, amount));
        }
        return topSaleProducts.stream()
                .sorted(Comparator.comparing(TopSaleProduct::getQuantity).reversed())
                .collect(Collectors.toList());
    }

    private List<Order> getPaidOrder(Instant date) {
        LocalDate localDate = LocalDate.ofInstant(date, ZoneId.systemDefault());
        return orderRepository.listAll().stream()
                .filter(o -> {
                    LocalDate localOrderDate = LocalDate.ofInstant(o.getOrderDate(), ZoneId.systemDefault());
                    return localOrderDate.equals(localDate) && o.getStatus().equals(OrderStatus.PAID);
                })
                .collect(Collectors.toList());
    }
}
