package kratos.oms.service;

import kratos.oms.domain.*;
import kratos.oms.model.customer.SearchCustomerModel;
import kratos.oms.model.customer.UpdateProfileModel;
import kratos.oms.repository.AccountRepository;
import kratos.oms.repository.OrderRepository;
import kratos.oms.seedwork.Helpers;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerService {
    private static final int SILVER_SPENDING = 5000000;
    private static final int GOLD_SPENDING = 10000000;
    private static final int PLATINUM_SPENDING = 25000000;

    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;

    public CustomerService(AccountRepository accountRepository, OrderRepository orderRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
    }

    public List<Account> search(SearchCustomerModel model) {
        Stream<Account> stream = accountRepository.listAll().stream().filter(a -> a.getRole().equals(Role.CUSTOMER));
        if (!Helpers.isNullOrEmpty(model.getSearchText())) {
            String searchText = model.getSearchText().toUpperCase();
            stream = stream.filter(c -> c.getFullName().toUpperCase().contains(searchText)
                    || (c.getProfile().getPhone() != null && c.getProfile().getPhone().toUpperCase().contains(searchText))
                    || (c.getProfile().getEmail() != null && c.getProfile().getEmail().toUpperCase().contains(searchText)));
        }
        if (model.getSortedBy() != null) {
            switch (model.getSortedBy()) {
                case NameDescending:
                    stream = stream.sorted(Comparator.comparing(Account::getFullName).reversed());
                    break;
                case NameAscending:
                    stream = stream.sorted(Comparator.comparing(Account::getFullName));
                    break;
            }
        } else {
            stream = stream.sorted(Comparator.comparing(Account::getFullName));
        }
        return stream.collect(Collectors.toList());
    }

    public Optional<Account> getDetail(UUID customerId) {
        return accountRepository.listAll().stream()
                .filter(a -> a.getRole().equals(Role.CUSTOMER) && a.getId().equals(customerId))
                .findFirst();
    }

    public void updateMembership(UUID customerId) {
        BigDecimal totalSpending = orderRepository.listAll().stream()
                .filter(o -> o.getAccountId().equals(customerId) && o.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Membership newMembership = Membership.NONE;
        if (totalSpending.compareTo(BigDecimal.valueOf(PLATINUM_SPENDING)) >= 0) {
            newMembership = Membership.PLATINUM;
        } else if (totalSpending.compareTo(BigDecimal.valueOf(GOLD_SPENDING)) >= 0) {
            newMembership = Membership.GOLD;
        } else if (totalSpending.compareTo(BigDecimal.valueOf(SILVER_SPENDING)) >= 0) {
            newMembership = Membership.SILVER;
        }
        Optional<Account> customerOpt = getDetail(customerId);
        if (customerOpt.isEmpty())
            throw new IllegalStateException("Customer with id " + customerId + " is not found");
        Account customer = customerOpt.get();
        customer.setMembership(newMembership);
        accountRepository.update(customer);
    }

    public void updateProfile(UpdateProfileModel model) {
        Optional<Account> customerOpt = getDetail(model.getCustomerId());
        if (customerOpt.isEmpty())
            throw new IllegalStateException("Customer with id " + model.getCustomerId() + " is not found");
        Account customer = customerOpt.get();
        customer.update(model.getFullName(), model.getPhone(), model.getEmail(), model.getAddress());
        accountRepository.update(customer);
    }

    public boolean delete(UUID id) {
        return accountRepository.delete(id);
    }
}
