package kratos.oms.service;

import kratos.oms.domain.*;
import kratos.oms.model.statistic.OrderRevenue;
import kratos.oms.model.statistic.TopPaidCustomer;
import kratos.oms.model.statistic.TopSaleProduct;
import kratos.oms.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    public StatisticService(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Get a list of orders in particular date and calculate total revenue
     *
     * @param date date to filter completed orders
     * @return a list of order and revenue
     */
    public OrderRevenue getRevenue(Instant date) {
        List<Order> orders = getPaidOrder(date);
        BigDecimal revenue = orders.stream().map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderRevenue(revenue, orders.stream()
                .sorted(Comparator.comparing(Order::getTotalAmount).reversed())
                .collect(Collectors.toList()));
    }

    /**
     * Get top sale products had been bought by customer in particular date (sorted by quantity)
     *
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

    public List<TopPaidCustomer> getTopPaidCustomers() {
        List<TopPaidCustomer> topPaidCustomers = new ArrayList<>();
        List<Account> customers = accountRepository.listAll().stream()
                .filter(a -> a.getRole().equals(Role.CUSTOMER)).collect(Collectors.toList());
        List<Order> paidOrders = getPaidOrder(null);
        for (Account customer : customers) {
            BigDecimal totalSpending = paidOrders.stream()
                    .filter(o -> o.getAccountId().equals(customer.getId()))
                    .map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            topPaidCustomers.add(new TopPaidCustomer(customer.getId(), customer.getFullName(), totalSpending));
        }
        return topPaidCustomers.stream()
                .sorted(Comparator.comparing(TopPaidCustomer::getTotalSpending).reversed())
                .collect(Collectors.toList());
    }

    private List<Order> getPaidOrder(Instant date) {
        Stream<Order> stream = orderRepository.listAll().stream();

        if (date == null)
            return stream.filter(o -> o.getStatus().equals(OrderStatus.DELIVERED)).collect(Collectors.toList());

        LocalDate localDate = LocalDate.ofInstant(date, ZoneId.systemDefault());
        return stream.filter(o -> {
                    LocalDate localOrderDate = LocalDate.ofInstant(o.getOrderDate(), ZoneId.systemDefault());
                    return localOrderDate.equals(localDate) && o.getStatus().equals(OrderStatus.DELIVERED);
                })
                .collect(Collectors.toList());
    }
}
