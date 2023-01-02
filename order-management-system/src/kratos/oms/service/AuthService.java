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
import kratos.oms.domain.Membership;
import kratos.oms.domain.Profile;
import kratos.oms.domain.Role;
import kratos.oms.model.account.CreateAccountModel;
import kratos.oms.model.account.LoginModel;
import kratos.oms.repository.AccountRepository;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.Logger;
import kratos.oms.seedwork.Principal;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Simple implementation of Authentication and Authorization service
 */
public class AuthService {
    private final AccountRepository accountRepository;
//    I guess final should be all capital letters
    private Principal principal;    // logged-in user's principal/claims

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean login(LoginModel model) {
        if (principal != null)
            throw new IllegalStateException("Already logged in!");
        Optional<Account> accountOpt = accountRepository.findByUsername(model.getUsername());
        if (accountOpt.isEmpty())
            return false;
        Account account = accountOpt.get();
        try {
            if (!account.getHashedPassword().equals(Helpers.passwordHash(model.getPassword())))
                return false;
            principal = new Principal(account.getId(),
                    account.getUsername(),
                    account.getRole(),
                    account.getProfile() == null
                            ? null
                            : account.getProfile().getMembership());
            return true;
        } catch (NoSuchAlgorithmException e) {
            Logger.printError(this.getClass().getName(), "login", e);
            return false;
        }
    }

    public void logout() {
        if (principal == null)
            throw new IllegalStateException("Already logged out!");
        principal = null;
    }

    public boolean register(CreateAccountModel model) {
        Profile profile = model.getRole() == Role.ADMIN
                ? null : new Profile(model.getPhone(), model.getEmail(), model.getAddress());
        try {
            Account account = new Account(model.getUsername(),
                    Helpers.passwordHash(model.getPassword()),
                    model.getFullName(),
                    model.getRole(),
                    profile);
            return accountRepository.add(account);
        } catch (NoSuchAlgorithmException e) {
            Logger.printError(this.getClass().getName(), "register", e);
            return false;
        }
    }

    public Account getCurrencyAccount() {
        if(!isAuthenticated())
            throw new IllegalStateException("Login is required before get current account!");
        return accountRepository.findByUsername(principal.getUsername()).get();
    }

    public boolean isUsernameExisted(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    public Principal getPrincipal() {
        return principal;
    }

    public boolean isAuthenticated() {
        return principal != null;
    }

    public void updateMembership(Membership membership) {
        if (this.principal.getMembership() != membership) {
            this.principal.setMembership(membership);
        }
    }
}
