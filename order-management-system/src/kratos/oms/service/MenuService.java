/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2022C
  Assessment: Assignment 3
  Author: Luu Duc Trung
  ID: s3951127
  Acknowledgement:
    - WhiteFang34, "How to print color in console using System.out.println?", Stackoverflow, https://stackoverflow.com/a/5762502
*/

package kratos.oms.service;

import kratos.oms.domain.Role;
import kratos.oms.model.account.CreateAccountModel;
import kratos.oms.model.account.LoginModel;
import kratos.oms.model.order.SearchOrderModel;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.InputOption;
import kratos.oms.seedwork.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuService {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;

    public MenuService(AuthService authService, CartService cartService, ProductService productService, OrderService orderService) {
        this.authService = authService;
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
    }

    public void homeScreen() {
        while (true) {
            if (!authService.isAuthenticated()) {
                Helpers.requestSelect(scanner, "Your choice [0-3]: ", new ArrayList<>() {{
                    add(new InputOption<>("login", () -> loginScreen()));
                    add(new InputOption<>("register as customer", () -> registrationScreen(Role.CUSTOMER)));
                    add(new InputOption<>("register as admin", () -> registrationScreen(Role.ADMIN)));
                    add(new InputOption<>("exit", () -> exitScreen()));
                }});
                continue;
            }
            banner("home");
            Logger.printInfo("Welcome back %s! checkout what we can help you...", authService.getPrincipal().getUsername());
            List<InputOption<Runnable>> commonOptions = new ArrayList<>() {{
                add(new InputOption<>("profile", () -> profileScreen()));
                add(new InputOption<>("logout", () -> {
                    authService.logout();
                    cartService.save();
                    Logger.printSuccess("Logout successfully.");
                }));
                add(new InputOption<>("exit", () -> exitScreen()));
            }};
            switch (authService.getPrincipal().getRole()) {
                case ADMIN:
                    Helpers.requestSelect(scanner, "Your choice [0-7]: ", new ArrayList<>() {{
                        add(new InputOption<>("statistic", () -> statisticScreen()));
                        add(new InputOption<>("order list", () -> orderScreen()));
                        add(new InputOption<>("product list", () -> productScreen()));
                        add(new InputOption<>("category list", () -> categoryScreen()));
                        add(new InputOption<>("customer list", () -> customerScreen()));
                        addAll(commonOptions);
                    }});
                    break;
                case CUSTOMER:
                    Logger.printInfo("You have %d item(s) in cart", cartService.getCachedCart().getTotalCount());
                    Helpers.requestSelect(scanner, "Your choice [0-1]: ", new ArrayList<>() {{
                        add(new InputOption<>("product list", () -> productScreen()));
                        add(new InputOption<>("order list", () -> orderScreen()));
                        add(new InputOption<>("check membership", () -> {
                            System.out.printf("Your membership is: %s%n", authService.getPrincipal().getMembership());
                        }));
                        addAll(commonOptions);
                    }});
                    break;
                default:
                    throw new IllegalStateException(String.format("Role: %s is not supported", authService.getPrincipal().getRole()));
            }
        }
    }

    public void profileScreen() {
        banner("profile");
        // TODO: implement
    }

    public void productScreen() {
        banner("products");
        // TODO: implement
    }


    public void productDetailScreen() {
        banner("product");
        // TODO: implement
    }

    public void categoryScreen() {
        banner("categories");
        // TODO: implement
    }

    public void categoryDetailScreen() {
        banner("category");
        // TODO: implement
    }

    public void orderScreen() {
        banner("orders");
        SearchOrderModel model = new SearchOrderModel();
        try {
            Helpers.requestInput(scanner, "Enter Order ID: ", "accountID", model);
            Helpers.requestSelectProduct(scanner, "Select Order Statue [0-2]: ", new ArrayList<>() {{
                add(new InputOption<>("[0] ordered", () -> model.setSortedBy("0")));
                add(new InputOption<>("[1] delivering", () -> model.setSortedBy("1")));
                add(new InputOption<>("[2] delivered", () -> model.setSortedBy("2")));
            }});
//            Helpers.requestSelectProduct(scanner, "How would you like to sort the items [0-1]: ", new ArrayList<>() {{
//                add(new InputOption<>("[0] latest to oldest", () -> model.setSortedBy("0")));
//                add(new InputOption<>("[1] oldest to latest", () -> model.setSortedBy("1")));
//            }});

        } catch (NoSuchFieldException ex) {
            Logger.printError(this.getClass().getName(), "productScreen", ex);
        }
    }

    public void orderDetailScreen() {
        banner("order");
        SearchOrderModel model = new SearchOrderModel();
        try {
            Helpers.requestInput(scanner, "Enter Order ID: ", "accountID", model);
            orderService.orderDetail(model);
        } catch (NoSuchFieldException ex) {
            Logger.printError(this.getClass().getName(), "orderScreen", ex);
        }
    }

    public void customerScreen() {
        banner("customers");
        // TODO: implement
    }

    public void customerDetailScreen() {
        banner("customer");
        // TODO: implement
    }

    public void statisticScreen() {
        banner("statistic");
        // TODO: implement
    }

    public void loginScreen() {
        banner("login");
        LoginModel model = new LoginModel();
        Helpers.loopRequest(scanner, () -> {
            try {
                Helpers.requestInput(scanner, "Enter your username: ", "username", model);
                Helpers.requestInput(scanner, "Enter your password: ", "password", model);
                if (authService.login(model)) {
                    cartService.load();
                    return true;
                }
                Logger.printDanger("Invalid username or password!");
            } catch (NoSuchFieldException e) {
                Logger.printError(this.getClass().getName(), "loginScreen", e);
            }
            return false;
        });
    }

    public void registrationScreen(Role role) {
        banner("register");
        CreateAccountModel model = new CreateAccountModel();
        model.setRole(role);
        try {
            Helpers.requestInput(scanner, "Enter your username: ", "username", model);
            Helpers.requestInput(scanner, "Enter your password: ", "password", model);
            Helpers.requestInput(scanner, "Enter your full name: ", "fullName", model);
            if (role == Role.CUSTOMER) {
                Helpers.requestInput(scanner, "Enter your phone: ", "phone", model);
                Helpers.requestInput(scanner, "Enter your email: ", "email", model);
                Helpers.requestInput(scanner, "Enter your address: ", "address", model);
            }
            authService.register(model);
            Logger.printSuccess("Register new account successfully.");
        } catch (NoSuchFieldException e) {
            Logger.printError(this.getClass().getName(), "registrationScreen", e);
        }
    }

    public void exitScreen() {
        cartService.save();
        System.out.println("Goodbye! See you again.");
        System.exit(0);
    }

    public void welcomeScreen() {
        banner("welcome", "", "*");
        System.out.println("*\tCOSC2081 GROUP ASSIGNMENT");
        System.out.println("*\tSTORE ORDER MANAGEMENT SYSTEM");
        System.out.println("*\tInstructor: Mr. Tom Huynh & Dr. Phong Ngo");
        System.out.println("*\tGroup: Kratos");
        System.out.println("*\ts3951127, Luu Duc Trung");
        System.out.println("*\ts3891941, Yunjae Kim");
        System.out.println("*\ts3938007, Pham Hoang Long");
        System.out.println("*\ts3915034, Do Phan Nhat Anh");
        System.out.println();
        System.out.print("Do you want to continue? [y/n]: ");
        String answer = scanner.nextLine();
        if (answer != null && (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")))
            homeScreen();
        else
            exitScreen();
    }

    private void banner(String title) {
        banner(title, 10, "", "");
    }

    private void banner(String title, String topText, String bottomText) {
        banner(title, 10, topText, bottomText);
    }

    private void banner(String title, int padding, String topText, String bottomText) {
        String body = "*" + " ".repeat(padding) + title.toUpperCase() + " ".repeat(padding) + "*";
        String border = "*".repeat(body.length());
        System.out.println(topText);
        System.out.println(border);
        System.out.println(body);
        System.out.println(border);
        System.out.println(bottomText);
    }
}
