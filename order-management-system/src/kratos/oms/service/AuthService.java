/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement: n/a
*/

package kratos.oms.service;

import kratos.oms.domain.Account;
import kratos.oms.domain.Profile;
import kratos.oms.model.CreateAccountModel;
import kratos.oms.model.LoginModel;
import kratos.oms.repository.AccountRepository;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.Principal;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Simple implementation of Authentication and Authorization service
 */
public class AuthService {
    private final AccountRepository accountRepository;
    private Principal principal;    // logged-in user's principal/claims

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean login(LoginModel model) {
        if(principal != null)
            throw new IllegalStateException("Already logged in!");
        Optional<Account> accountOpt = accountRepository.findByUsername(model.getUsername());
        if(accountOpt.isEmpty())
            return false;
        Account account = accountOpt.get();
        try {
            return account.getHashedPassword().equals(Helpers.passwordHash(model.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public void logout() {
        if(principal == null)
            throw new IllegalStateException("Already logged out!");
        principal = null;
    }

    public boolean register(CreateAccountModel model) {
        try {
            Account account = new Account(model.getUsername(),
                    Helpers.passwordHash(model.getPassword()),
                    model.getFullName(),
                    model.getRole(),
                    new Profile(model.getPhone(),
                            model.getEmail(),
                            model.getAddress()));
            return accountRepository.add(account);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public boolean isAuthenticated() {
        return principal != null;
    }
}
