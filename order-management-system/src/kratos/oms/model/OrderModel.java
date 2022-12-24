package kratos.oms.model;

import kratos.oms.domain.OrderStatus;

import java.util.UUID;

public class OrderModel {
    private UUID accountId;
    private OrderStatus status;
    private String sortedBy;

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }
}
