package kratos.oms.repository;

import kratos.oms.domain.Account;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileAccountRepositoryImpl extends BaseFileRepository<UUID, Account> implements AccountRepository {
    public FileAccountRepositoryImpl(String fileUrl) {
        super(fileUrl);
    }

    @Override
    public List<Account> listAll() {
        try {
            return this.read().stream().collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return listAll().stream().filter(a -> a.getUsername().equals(username)).findFirst();
    }

    @Override
    public boolean add(Account account) {
        var accounts = listAll();
        accounts.add(account);
        try {
            this.write(accounts);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
