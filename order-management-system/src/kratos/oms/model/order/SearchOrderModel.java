package kratos.oms.model.order;

import kratos.oms.domain.OrderStatus;
import kratos.oms.model.SortDirection;

import java.util.UUID;

public class SearchOrderModel {
    private UUID accountId;
    private OrderStatus status;
    private String sortedBy;
    private SortDirection direction;

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

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }
}
