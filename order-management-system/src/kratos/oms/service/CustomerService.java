package kratos.oms.service;

import kratos.oms.domain.*;
import kratos.oms.model.customer.SearchCustomerModel;
import kratos.oms.model.customer.UpdateProfileModel;
import kratos.oms.repository.AccountRepository;
import kratos.oms.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerService {
    private static final int SILVER_SPENDING = 5000000;
    private static final int GOLD_SPENDING = 10000000;
    private static final int PLATINUM_SPENDING = 25000000;

    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;

    public CustomerService(AuthService authService, AccountRepository accountRepository, OrderRepository orderRepository) {
        this.authService = authService;
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
    }

    public List<Account> search(SearchCustomerModel model) {
        // TODO: implement customer searching
        return accountRepository.listAll().stream()
                .filter(a -> a.getRole().equals(Role.CUSTOMER))
                .collect(Collectors.toList());
    }

    public Optional<Account> getDetail(UUID customerId) {
        return accountRepository.listAll().stream()
                .filter(a -> a.getRole().equals(Role.CUSTOMER) && a.getId().equals(customerId))
                .findFirst();
    }

    public void updateMembership(UUID customerId) {
        double totalSpending = orderRepository.listAll().stream()
                .filter(o -> o.getAccountId().equals(customerId) && o.getStatus() == OrderStatus.PAID)
                .mapToDouble(Order::getTotalAmount).sum();
        Membership newMembership = Membership.NONE;
        if (totalSpending >= PLATINUM_SPENDING) {
            newMembership = Membership.PLATINUM;
        } else if (totalSpending >= GOLD_SPENDING) {
            newMembership = Membership.GOLD;
        } else if (totalSpending >= SILVER_SPENDING) {
            newMembership = Membership.SILVER;
        }
        Optional<Account> customerOpt = getDetail(customerId);
        if(customerOpt.isEmpty())
            throw new IllegalStateException("Customer with id " + customerId + " is not found");
        Account customer = customerOpt.get();
        customer.setMembership(newMembership);
        accountRepository.update(customer);
    }

    public void updateProfile(UpdateProfileModel model) {
        Optional<Account> customerOpt = getDetail(model.getCustomerId());
        if(customerOpt.isEmpty())
            throw new IllegalStateException("Customer with id " + model.getCustomerId() + " is not found");
        Account customer = customerOpt.get();
        customer.update(model.getFullName(), model.getPhone(), model.getEmail(), model.getAddress());
        accountRepository.update(customer);
    }

    public boolean delete(UUID id) {
        return accountRepository.delete(id);
    }
}
