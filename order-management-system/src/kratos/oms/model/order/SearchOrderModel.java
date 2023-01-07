package kratos.oms.model.order;

import kratos.oms.domain.OrderStatus;

import java.util.UUID;

public class SearchOrderModel {
    private String code;
    private UUID customerId;
    private OrderStatus status;
    private OrderSort sortedBy;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderSort getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(OrderSort sortedBy) {
        this.sortedBy = sortedBy;
    }
}
