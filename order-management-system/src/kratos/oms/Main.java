/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement: n/a
*/

package kratos.oms;

import kratos.oms.repository.AccountRepository;
import kratos.oms.repository.FileAccountRepositoryImpl;
import kratos.oms.service.AuthService;
import kratos.oms.service.MenuService;

public class Main {
    /**
     * Entry point of Order-Management-System program
     */
    public static void main(String[] args) {
        AccountRepository accountRepository = new FileAccountRepositoryImpl("accounts.txt");
        AuthService authService = new AuthService(accountRepository);
        MenuService menuService = new MenuService(authService);
        while (true)
            menuService.welcomeScreen();
    }
}