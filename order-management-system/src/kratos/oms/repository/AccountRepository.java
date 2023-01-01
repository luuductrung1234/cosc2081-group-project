package kratos.oms.repository;

import kratos.oms.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    List<Account> listAll();
    Optional<Account> findByUsername(String username);
    boolean add(Account account);
    boolean update(Account account);
    boolean delete(UUID id);
}
