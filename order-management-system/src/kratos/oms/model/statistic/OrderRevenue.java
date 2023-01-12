package kratos.oms.model.statistic;

import kratos.oms.domain.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderRevenue {
    private final BigDecimal revenue;
    private final List<Order> orders;

    public OrderRevenue(BigDecimal revenue, List<Order> orders) {
        this.revenue = revenue;
        this.orders = orders;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
