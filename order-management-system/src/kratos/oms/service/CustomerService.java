package kratos.oms.service;

import kratos.oms.domain.*;
import kratos.oms.model.customer.UpdateProfileModel;
import kratos.oms.model.customer.SearchCustomerModel;
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

    public Optional<Account> getDetail() {
        return accountRepository.listAll().stream()
                .filter(a -> a.getRole().equals(Role.CUSTOMER))
                .findFirst();
    }
    public Optional<Account> getProfile(UUID id) {
        return accountRepository.listAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public Account editProfile(UpdateProfileModel model) {
        Optional<Account> accountOpt = accountRepository.listAll().stream().filter(a -> a.getId().equals(model.getId())).findFirst();
        if(accountOpt.isEmpty())
            throw new IllegalStateException("Account with id: " + model.getId() + " is not found!");
        Account account = accountOpt.get();
        account.updateProfile(model.getPhone(), model.getEmail(), model.getAddress());
        accountRepository.update(account);
        return account;
    }

    public Membership updateMembership() {
        if (authService.getPrincipal().getRole() != Role.CUSTOMER)
            throw new IllegalStateException("Logged in user not allow to calculate/update Membership");
        double totalSpending = orderRepository.listAll().stream()
                .filter(o -> o.getAccountId().equals(authService.getPrincipal().getId()))
                .mapToDouble(Order::getTotalAmount).sum();
        Membership newMembership = Membership.NONE;
        if (totalSpending >= PLATINUM_SPENDING) {
            newMembership = Membership.PLATINUM;
        } else if (totalSpending >= GOLD_SPENDING) {
            newMembership = Membership.GOLD;
        } else if (totalSpending >= SILVER_SPENDING) {
            newMembership = Membership.SILVER;
        }
        Account account = accountRepository.findByUsername(authService.getPrincipal().getUsername()).get();
        account.setMembership(newMembership);
        accountRepository.update(account);
        authService.updateMembership(newMembership);
        return newMembership;
    }

    public boolean delete(UUID id) {
        return accountRepository.delete(id);
    }
}
