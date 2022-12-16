package kratos.oms.repository;

import kratos.oms.domain.Account;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileAccountRepositoryImpl extends BaseFileRepository<UUID, Account> implements AccountRepository {
    public FileAccountRepositoryImpl(String fileUrl) {
        super(fileUrl);
    }

    @Override
    public List<Account> listAll() {
        try {
            return new ArrayList<>(this.read(Account.class));
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
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
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean update(Account account) {
        List<Account> accounts = listAll();
        accounts.removeIf(a -> a.getId().equals(account.getId()));
        accounts.add(account);
        try {
            this.write(accounts);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }
}
