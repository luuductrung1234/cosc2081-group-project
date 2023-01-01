package kratos.oms.service;

import kratos.oms.domain.Account;
import kratos.oms.domain.Role;
import kratos.oms.model.customer.SearchCustomerModel;
import kratos.oms.repository.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerService {
    private final AccountRepository accountRepository;

    public CustomerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

    public boolean delete(UUID id) {
        return accountRepository.delete(id);
    }
}
