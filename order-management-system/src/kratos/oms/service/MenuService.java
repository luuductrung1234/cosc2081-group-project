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

import kratos.oms.domain.Role;
import kratos.oms.model.CreateAccountModel;
import kratos.oms.model.LoginModel;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.InputOption;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuService {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;

    public MenuService(AuthService authService) {
        this.authService = authService;
    }

    public void homeScreen() {
        if (!authService.isAuthenticated()) {
            Helpers.requestSelect(scanner, "Your choice [0-1]: ", new ArrayList<>() {{
                add(new InputOption<>("login", () -> loginScreen()));
                add(new InputOption<>("register as customer", () -> registrationScreen(Role.CUSTOMER)));
                add(new InputOption<>("register as admin", () -> registrationScreen(Role.ADMIN)));
                add(new InputOption<>("go back", () -> welcomeScreen()));
            }});
        }
        System.out.println("=============================");
        System.out.println("=            HOME           =");
        System.out.println("=============================");
        Helpers.requestSelect(scanner, "Your choice [0-1]: ", new ArrayList<>() {{
            add(new InputOption<>("logout", authService::logout));
            add(new InputOption<>("go back", () -> welcomeScreen()));
        }});
    }

    public void profileScreen() {
        // TODO: implement
    }

    public void productScreen() {
        // TODO: implement
    }

    public void productDetailScreen() {
        // TODO: implement
    }

    public void categoryScreen() {
        // TODO: implement
    }

    public void categoryDetailScreen() {
        // TODO: implement
    }

    public void orderScreen() {
        // TODO: implement
    }

    public void orderDetailScreen() {
        // TODO: implement
    }

    public void customerScreen() {
        // TODO: implement
    }

    public void customerDetailScreen() {
        // TODO: implement
    }

    public void statisticScreen() {
        // TODO: implement
    }

    public void loginScreen() {
        System.out.println("=============================");
        System.out.println("=           LOGIN           =");
        System.out.println("=============================");
        LoginModel model = new LoginModel();
        Helpers.requestInput(scanner, "Enter your username: ", "username", model);
        Helpers.requestInput(scanner, "Enter your password: ", "password", model);
        authService.login(model);
    }

    public void registrationScreen(Role role) {
        System.out.println("=============================");
        System.out.println("=         REGISTER          =");
        System.out.println("=============================");
        CreateAccountModel model = new CreateAccountModel();
        model.setRole(role);
        Helpers.requestInput(scanner, "Enter your username: ", "username", model);
        Helpers.requestInput(scanner, "Enter your password: ", "password", model);
        Helpers.requestInput(scanner, "Enter your full name: ", "fullName", model);
        Helpers.requestInput(scanner, "Enter your phone: ", "phone", model);
        Helpers.requestInput(scanner, "Enter your email: ", "email", model);
        Helpers.requestInput(scanner, "Enter your address: ", "address", model);
        authService.register(model);
    }

    public void welcomeScreen() {
        System.out.println("=============================");
        System.out.println("=          WELCOME          =");
        System.out.println("=============================");
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("STORE ORDER MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Tom Huynh & Dr. Phong Ngo");
        System.out.println("Group: Kratos");
        System.out.println("s3951127, Luu Duc Trung");
        System.out.println("sXXXXXXX, Student Name");
        System.out.println("sXXXXXXX, Student Name");
        System.out.println("sXXXXXXX, Student Name");
        System.out.println();
        System.out.println("Press any button to continue...");
        scanner.next();
        homeScreen();
    }
}
