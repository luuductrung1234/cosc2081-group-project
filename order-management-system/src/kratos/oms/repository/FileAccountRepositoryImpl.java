package kratos.oms.repository;

import kratos.oms.domain.Account;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileAccountRepositoryImpl extends BaseFileRepository implements AccountRepository {
    private final static String DATA_FILE_NAME = "accounts.txt";

    public FileAccountRepositoryImpl(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public List<Account> listAll() {
        try {
            return this.read(DATA_FILE_NAME, Account.class);
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
        List<Account> accounts = listAll();
        Optional<Account> existingAccount = accounts.stream()
                .filter(a -> a.getId().equals(account.getId())
                        || a.getUsername().equals(account.getUsername())).findFirst();
        if (existingAccount.isPresent())
            return false;
        accounts.add(account);
        try {
            this.write(DATA_FILE_NAME, accounts);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean update(Account account) {
        List<Account> accounts = listAll();
        Optional<Account> existingAccount = accounts.stream()
                .filter(a -> a.getId().equals(account.getId())).findFirst();
        if (existingAccount.isEmpty())
            return false;
        accounts.removeIf(a -> a.getId().equals(account.getId()));
        accounts.add(account);
        try {
            this.write(DATA_FILE_NAME, accounts);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        List<Account> accounts = listAll();
        try {
            if (accounts.removeIf(a -> a.getId().equals(id))) {
                this.write(DATA_FILE_NAME, accounts);
                return true;
            }
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "delete", e);
        }
        return false;
    }
}
