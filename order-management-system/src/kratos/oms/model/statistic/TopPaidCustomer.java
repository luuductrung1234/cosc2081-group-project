package kratos.oms.model.statistic;

import java.math.BigDecimal;
import java.util.UUID;

public class TopPaidCustomer {
    private final UUID id;
    private final String name;
    private final BigDecimal totalSpending;

    public TopPaidCustomer(UUID id, String name, BigDecimal totalSpending) {
        this.id = id;
        this.name = name;
        this.totalSpending = totalSpending;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotalSpending() {
        return totalSpending;
    }
}
