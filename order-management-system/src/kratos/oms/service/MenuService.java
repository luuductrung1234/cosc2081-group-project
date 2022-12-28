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

import kratos.oms.domain.Category;
import kratos.oms.domain.Product;
import kratos.oms.domain.Role;
import kratos.oms.model.account.CreateAccountModel;
import kratos.oms.model.account.LoginModel;
import kratos.oms.model.category.SearchCategoryModel;
import kratos.oms.model.product.CreateProductModel;
import kratos.oms.model.product.UpdateProductModel;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.seedwork.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MenuService {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final CustomerService customerService;

    public MenuService(AuthService authService, CartService cartService,
                       ProductService productService, OrderService orderService,
                       CategoryService categoryService, CustomerService customerService) {
        this.authService = authService;
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.customerService = customerService;
    }

    public void homeScreen() {
        while (true) {
            if (!authService.isAuthenticated()) {
                Helpers.requestSelectAction(scanner, "Your choice [0-3]: ", new ArrayList<>() {{
                    add(new ActionOption<>("login", () -> loginScreen()));
                    add(new ActionOption<>("register as customer", () -> registrationScreen(Role.CUSTOMER)));
                    add(new ActionOption<>("register as admin", () -> registrationScreen(Role.ADMIN)));
                    add(new ActionOption<>("exit", () -> exitScreen()));
                }});
                continue;
            }
            banner("home");
            Logger.printInfo("Welcome back %s! checkout what we can help you...", authService.getPrincipal().getUsername());
            List<ActionOption<Runnable>> commonOptions = new ArrayList<>() {{
                add(new ActionOption<>("profile", () -> profileScreen()));
                add(new ActionOption<>("logout", () -> {
                    authService.logout();
                    cartService.save();
                    System.out.println();
                    Logger.printSuccess("Logout successfully.");
                }));
                add(new ActionOption<>("exit", () -> exitScreen()));
            }};
            switch (authService.getPrincipal().getRole()) {
                case ADMIN:
                    Helpers.requestSelectAction(scanner, "Your choice [0-7]: ", new ArrayList<>() {{
                        add(new ActionOption<>("statistic", () -> statisticScreen()));
                        add(new ActionOption<>("order list", () -> orderScreen()));
                        add(new ActionOption<>("product list", () -> productScreen()));
                        add(new ActionOption<>("category list", () -> categoryScreen()));
                        add(new ActionOption<>("customer list", () -> customerScreen()));
                        addAll(commonOptions);
                    }});
                    break;
                case CUSTOMER:
                    Logger.printInfo("You have %d item(s) in cart", cartService.getCachedCart().getTotalCount());
                    Helpers.requestSelectAction(scanner, "Your choice [0-5]: ", new ArrayList<>() {{
                        add(new ActionOption<>("product list", () -> productScreen()));
                        add(new ActionOption<>("order list", () -> orderScreen()));
                        add(new ActionOption<>("check membership", () -> {
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
        banner("my profile");
        // TODO: implement
    }

    public void productScreen() {
        AtomicReference<SearchProductModel> searchModel = new AtomicReference<>(new SearchProductModel());
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("product list");
            List<Product> products = productService.search(searchModel.get());
            List<Category> categories = categoryService.search(new SearchCategoryModel());
            System.out.printf("%-10s %-10s %-10s %-10s\n", "No.", "name", "category", "price");
            if (products.isEmpty())
                Logger.printInfo("No product found...");
            for (int productNo = 0; productNo < products.size(); productNo++) {
                Product product = products.get(productNo);
                System.out.printf("%-10d %-10s %-10s %-10s\n", productNo, product.getName(), product.getCategory().getName(),
                        String.format("%f (%s)", product.getPrice(), product.getCurrency()));
            }

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("search", () -> {
                    try {
                        SearchProductModel newSearchModel = new SearchProductModel();

                        List<ValueOption<UUID>> categoryOptions = categories.stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList());
                        categoryOptions.add(new ValueOption<>("Skip", null));

                        Helpers.requestStringInput(scanner, "Enter product name: ", "name", newSearchModel);
                        Helpers.requestDoubleInput(scanner, "Enter from price: ", "fromPrice", newSearchModel);
                        Helpers.requestDoubleInput(scanner, "Enter to price: ", "toPrice", newSearchModel);
                        Helpers.requestSelectValue(scanner, "Choose category: ", categoryOptions, "categoryId", newSearchModel);
                        Helpers.requestStringInput(scanner, "Enter sort field: ", "sortedBy", newSearchModel);
                        searchModel.set(newSearchModel);
                    } catch (RuntimeException e) {
                        Logger.printError(this.getClass().getName(), "productScreen", e);
                    }
                }));
                add(new ActionOption<>("clear search", () -> searchModel.set(new SearchProductModel())));
            }};

            if (authService.getPrincipal().getRole() == Role.ADMIN && !categories.isEmpty())
                // Only admin can add product
                // There is at least 1 category to add product
                actionOptions.add(new ActionOption<>("add product", () -> addOrUpdateProductScreen(null)));

            if (products.size() > 0)
                // There is at least 1 product to view detail
                actionOptions.add(new ActionOption<>("view detail", () -> {
                    int productNo = Helpers.requestIntInput(scanner, "Enter product No. to view detail: ", (value) -> {
                        if (value < 0 || value >= products.size()) {
                            return ValidationResult.inValidInstance("Given product No. is out of index.");
                        }
                        return ValidationResult.validInstance();
                    });
                    productDetailScreen(products.get(productNo));
                }));

            actionOptions.addAll(new ArrayList<>() {{
                add(new ActionOption<>("go back", () -> goBack.set(true)));
                add(new ActionOption<>("logout", () -> {
                    authService.logout();
                    cartService.save();
                    System.out.println();
                    Logger.printSuccess("Logout successfully.");
                    goBack.set(true);
                }));
                add(new ActionOption<>("exit", () -> exitScreen()));
            }});

            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void productDetailScreen(Product product) {
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("product detail");
            System.out.printf("%-5s: %-10s %-5s %-10s\n", "Name", product.getName(), "Category", product.getCategory());
            System.out.printf("%-5s: %-10f %-5s %-10s\n", "Price", product.getPrice(), "Currency", product.getCurrency());

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>();
            if (authService.getPrincipal().getRole() == Role.ADMIN)
                actionOptions.add(new ActionOption<>("edit product", () -> addOrUpdateProductScreen(product)));
            else
                actionOptions.add(new ActionOption<>("add to cart", () -> cartService.addItem(product, 1)));
            actionOptions.add(new ActionOption<>("go back", () -> goBack.set(true)));

            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void addOrUpdateProductScreen(Product product) {
        try {
            if (product == null) {
                banner("add product");
                CreateProductModel model = new CreateProductModel();
                Helpers.requestStringInput(scanner, "Enter product name: ", "name", model);
                Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
                Helpers.requestStringInput(scanner, "Enter product currency: ", "currency", model);
                Helpers.requestSelectValue(scanner, "Enter product category: ",
                        categoryService.search(new SearchCategoryModel()).stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList()),
                        "currency", model);
                productService.add(model);
            } else {
                banner("edit product");
                UpdateProductModel model = new UpdateProductModel();
                model.setProductId(product.getId());
                Logger.printInfo(String.format("Old price: %f%n", product.getPrice()));
                Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
                if (model.getPrice() == null)
                    model.setPrice(product.getPrice());
                Logger.printInfo(String.format("Old currency: %s%n", product.getCurrency()));
                Helpers.requestStringInput(scanner, "Enter product currency: ", "currency", model);
                if (model.getCurrency() == null)
                    model.setCurrency(product.getCurrency());
                Logger.printInfo(String.format("Old category: %s%n", product.getCategory().getName()));
                Helpers.requestSelectValue(scanner, "Enter product category: ",
                        categoryService.search(new SearchCategoryModel()).stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList()),
                        "currency", model);
                if (model.getCategoryId() == null)
                    model.setCategoryId(product.getCategory().getId());
                productService.update(model);
            }
        } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "addOrUpdateProductScreen", e);
        }
    }

    public void categoryScreen() {
        banner("category list");
        // TODO: implement
    }

    public void categoryDetailScreen() {
        banner("category detail");
        // TODO: implement
    }

    public void orderScreen() {
        banner("order list");
        // TODO: implement
    }

    public void orderDetailScreen() {
        banner("order detail");
        // TODO: implement
    }

    public void customerScreen() {
        banner("customer list");
        // TODO: implement
    }

    public void customerDetailScreen() {
        banner("customer detail");
        // TODO: implement
    }

    public void statisticScreen() {
        banner("statistic");
        // TODO: implement
    }

    public void loginScreen() {
        banner("login");
        LoginModel model = new LoginModel();
        Helpers.loopAction(scanner, () -> {
            try {
                Helpers.requestStringInput(scanner, "Enter your username: ", "username", model);
                Helpers.requestStringInput(scanner, "Enter your password: ", "password", model);
                if (authService.login(model)) {
                    cartService.load();
                    return true;
                }
                Logger.printDanger("Invalid username or password!");
            } catch (RuntimeException e) {
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
            Helpers.requestStringInput(scanner, "Enter your username: ", "username", model);
            Helpers.requestStringInput(scanner, "Enter your password: ", "password", model);
            Helpers.requestStringInput(scanner, "Enter your full name: ", "fullName", model);
            if (role == Role.CUSTOMER) {
                Helpers.requestStringInput(scanner, "Enter your phone: ", "phone", model);
                Helpers.requestStringInput(scanner, "Enter your email: ", "email", model);
                Helpers.requestStringInput(scanner, "Enter your address: ", "address", model);
            }
            authService.register(model);
            Logger.printSuccess("Register new account successfully.");
        } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "registrationScreen", e);
        }
    }

    public void exitScreen() {
        cartService.save();
        System.out.println();
        Logger.printInfo("Goodbye! See you again.");
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
