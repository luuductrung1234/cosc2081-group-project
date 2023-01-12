package kratos.oms.model.statistic;

import kratos.oms.domain.Membership;

public class MembershipNumber {
    private final Membership membership;
    private final long customerCount;

    public MembershipNumber(Membership membership, long customerCount) {
        this.membership = membership;
        this.customerCount = customerCount;
    }

    public Membership getMembership() {
        return membership;
    }

    public long getCustomerCount() {
        return customerCount;
    }
}
