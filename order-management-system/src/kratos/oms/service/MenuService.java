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

import kratos.oms.domain.*;
import kratos.oms.model.account.CreateAccountModel;
import kratos.oms.model.account.LoginModel;
import kratos.oms.model.category.CreateCategoryModel;
import kratos.oms.model.category.SearchCategoryModel;
import kratos.oms.model.customer.SearchCustomerModel;
import kratos.oms.model.customer.UpdateProfileModel;
import kratos.oms.model.product.CreateProductModel;
import kratos.oms.model.product.UpdateProductModel;
import kratos.oms.model.product.SearchProductModel;
import kratos.oms.seedwork.*;

import java.util.*;
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

    /**
     * this method intentionally have infinite loop. Users are always go back to main screen (home screen)
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void homeScreen() {
        while (true) {
            if (!authService.isAuthenticated()) {
                Helpers.requestSelectAction(scanner, "Your choice [0-3]: ", new ArrayList<>() {{
                    add(new ActionOption<>("login", () -> loginScreen()));
                    add(new ActionOption<>("register as customer", () -> registrationScreen(Role.CUSTOMER)));
                    // According to requirements, there is only 1 predefined admin account
                    //add(new ActionOption<>("register as admin", () -> registrationScreen(Role.ADMIN)));
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
                    cartService.unload();
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
                        add(new ActionOption<>("your cart", () -> cartScreen()));
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
        if(authService.getPrincipal().getId().equals(null)){
            throw new IllegalStateException("Please log in first to see the profile");
        }
        Optional<Account> profile=customerService.getProfile(authService.getPrincipal().getId());
        System.out.println(profile.get().getFullName());
        System.out.println(profile.get().getId());
        System.out.println(profile.get().getRole());

        // TODO: implement
    }
    public void profileUpdateScreen(){
        AtomicReference<UpdateProfileModel> profileModel = new AtomicReference<>(new UpdateProfileModel());

        List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
            add(new ActionOption<>("edit", () -> {
                try {
                    UpdateProfileModel newUpdateProfileModel = new UpdateProfileModel();
                    Helpers.requestStringInput(scanner, "Full Name : ", "name", newUpdateProfileModel);
                    Helpers.requestStringInput(scanner, "New Address : ", "address", newUpdateProfileModel);
                    Helpers.requestStringInput(scanner, "New Phone Number: ", "phoneNumber", newUpdateProfileModel);
                    Helpers.requestStringInput(scanner, "New Email: ", "email", newUpdateProfileModel);
//todo:                 password update?
                    profileModel.set(newUpdateProfileModel);
                } catch (RuntimeException e) {
                    Logger.printError(this.getClass().getName(), "productScreen", e);
                }
            }));
        }};
    }

    public void productScreen() {
        AtomicReference<SearchProductModel> searchModel = new AtomicReference<>(new SearchProductModel());
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("product list");
            List<Product> products = productService.search(searchModel.get());
            List<Category> categories = categoryService.search(new SearchCategoryModel());

            System.out.printf("search by \n\t name: %-20s \n\t category: %-20s \n\t price from: %-5s \n\t price to: %-5s\n",
                    Helpers.isNullOrEmpty(searchModel.get().getName()) ? "n/a" : searchModel.get().getName(),
                    searchModel.get().getCategoryId() == null ? "n/a" : categories.stream()
                            .filter(c -> c.getId().equals(searchModel.get().getCategoryId())).findFirst().get().getName(),
                    searchModel.get().getFromPrice() == null ? "n/a" : Helpers.toString(searchModel.get().getFromPrice()),
                    searchModel.get().getToPrice() == null ? "n/a" : Helpers.toString(searchModel.get().getToPrice()));
            System.out.printf("sort by: %s\n\n",
                    Helpers.isNullOrEmpty(searchModel.get().getSortedBy()) ? "n/a" : searchModel.get().getSortedBy());

            // TODO: maybe do it later, filter price in VND by default, then try to convert VND to any other currencies before searching?

            System.out.printf("%-7s %-30s %-15s %-10s\n", "No.", "name", "category", "price");
            System.out.println("-".repeat(75));
            if (products.isEmpty())
                Logger.printInfo("No product found...");
            if (categories.isEmpty() && authService.getPrincipal().getRole() == Role.ADMIN)
                Logger.printWarning("No category found. Please add new category first!");
            for (int productNo = 0; productNo < products.size(); productNo++) {
                Product product = products.get(productNo);
                System.out.printf("%-7d %-30s %-15s %-10s\n", productNo, product.getName(), product.getCategory().getName(),
                        Helpers.toString(product.getPrice(), product.getCurrency(), true));
            }

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("search", () -> {
                    try {
                        SearchProductModel newSearchModel = new SearchProductModel();

                        List<ValueOption<UUID>> categoryOptions = categories.stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList());
                        categoryOptions.add(new ValueOption<>("Skip", null));
                        List<ValueOption<String>> sortOptions=new ArrayList<>();
                        sortOptions.add(new ValueOption<>("1. Sort by name(Alphabetically)", "1"));
                        sortOptions.add(new ValueOption<>("2. Sort by name(Reverse Alphabet)", "2"));
                        sortOptions.add(new ValueOption<>("1. Sort by price(High to low)", "3"));
                        sortOptions.add(new ValueOption<>("1. Sort by price(low to high)", "4"));



                        Helpers.requestStringInput(scanner, "Search by name: ", "name", newSearchModel);
                        Helpers.requestDoubleInput(scanner, "Filter from price: ", "fromPrice", newSearchModel);
                        Helpers.requestDoubleInput(scanner, "Filter to price: ", "toPrice", newSearchModel);
                        Helpers.requestSelectValue(scanner, "Filter by category: ", categoryOptions, "categoryId", newSearchModel, 3);
                        Helpers.requestSelectValue(scanner, "Sort by: ", sortOptions,"sortedBy", newSearchModel);
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
                    productDetailScreen(products.get(productNo).getId());
                }));

            addCommonActions(actionOptions, goBack);
            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void productDetailScreen(UUID productId) {
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("product detail");
            Optional<Product> productOpt = productService.getDetail(productId);
            if (productOpt.isEmpty())
                throw new IllegalStateException("Product with id: " + productId + " not found!");
            productOpt.get().printDetail();

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>();
            if (authService.getPrincipal().getRole() == Role.ADMIN) {
                actionOptions.add(new ActionOption<>("edit product", () -> {
                    addOrUpdateProductScreen(productId);
                }));
                actionOptions.add(new ActionOption<>("delete product", () -> {
                    Boolean isDelete = Helpers.requestBooleanInput(scanner, "Do you want to delete this product [y/n]? ");
                    if (isDelete) {
                        productService.delete(productId);
                        Logger.printSuccess("Delete product successfully!");
                        goBack.set(true);
                    }
                }));
            } else
                actionOptions.add(new ActionOption<>("add to cart", () -> {
                    cartService.addItem(productOpt.get(), 1);
                    Logger.printSuccess("Add item to cart successfully!");
                    goBack.set(true);
                }));

            actionOptions.add(new ActionOption<>("go back", () -> goBack.set(true)));

            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void addOrUpdateProductScreen(UUID productId) {
        try {
            if (productId == null) {
                banner("add product");
                CreateProductModel model = new CreateProductModel();
                Helpers.requestStringInput(scanner, "Enter product name: ", "name", model);
                Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
                // VND by default
                model.setCurrency("VND");
                //Helpers.requestStringInput(scanner, "Enter product currency: ", "currency", model);
                Helpers.requestSelectValue(scanner, "Enter product category: ",
                        categoryService.search(new SearchCategoryModel()).stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList()),
                        "categoryId", model);
                productService.add(model);
                Logger.printSuccess("Add new product successfully!");
            } else {
                banner("edit product");
                UpdateProductModel model = new UpdateProductModel();
                Optional<Product> productOpt = productService.getDetail(productId);
                if (productOpt.isEmpty())
                    throw new IllegalStateException("Product with id: " + productId + " not found!");
                Product product = productOpt.get();
                model.setProductId(product.getId());
                Logger.printInfo(String.format("Old price: %s", Helpers.toString(product.getPrice(), product.getCurrency(), false)));
                Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
                if (model.getPrice() == null)
                    model.setPrice(product.getPrice());
                // VND by default
                model.setCurrency("VND");
                // Logger.printInfo(String.format("Old currency: %s", product.getCurrency()));
                // Helpers.requestStringInput(scanner, "Enter product currency: ", "currency", model);
                // if (model.getCurrency() == null)
                //     model.setCurrency(product.getCurrency());
                Logger.printInfo(String.format("Old category: %s", product.getCategory().getName()));
                Helpers.requestSelectValue(scanner, "Enter product category: ",
                        categoryService.search(new SearchCategoryModel()).stream()
                                .map(c -> new ValueOption<>(c.getName(), c.getId())).collect(Collectors.toList()),
                        "categoryId", model, 3);
                if (model.getCategoryId() == null)
                    model.setCategoryId(product.getCategory().getId());
                productService.update(model);
                Logger.printSuccess("Update product successfully!");
            }
        } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "addOrUpdateProductScreen", e);
        }
    }

    public void categoryScreen() {
        AtomicReference<SearchCategoryModel> searchModel = new AtomicReference<>(new SearchCategoryModel());
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("category list");
            List<Category> categories = categoryService.search(searchModel.get());

            System.out.printf("search by name: %-5s\n", Helpers.isNullOrEmpty(searchModel.get().getName()) ? "n/a" : searchModel.get().getName());

            System.out.printf("%-7s %-10s\n", "No.", "name");
            System.out.println("-".repeat(25));
            if (categories.isEmpty())
                Logger.printInfo("No category found...");
            for (int categoryNo = 0; categoryNo < categories.size(); categoryNo++) {
                Category category = categories.get(categoryNo);
                System.out.printf("%-7d %-10s\n", categoryNo, category.getName());
            }

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("search", () -> {
                    try {
                        SearchCategoryModel newSearchModel = new SearchCategoryModel();
                        Helpers.requestStringInput(scanner, "Search by name: ", "name", newSearchModel);
                        searchModel.set(newSearchModel);
                    } catch (RuntimeException e) {
                        Logger.printError(this.getClass().getName(), "productScreen", e);
                    }
                }));
                add(new ActionOption<>("clear search", () -> searchModel.set(new SearchCategoryModel())));
            }};

            if (authService.getPrincipal().getRole() == Role.ADMIN)
                actionOptions.add(new ActionOption<>("add category", this::addCategoryScreen));

            if (categories.size() > 0)
                actionOptions.add(new ActionOption<>("view detail", () -> {
                    int categoryNo = Helpers.requestIntInput(scanner, "Enter category No. to view detail: ", (value) -> {
                        if (value < 0 || value >= categories.size()) {
                            return ValidationResult.inValidInstance("Given category No. is out of index.");
                        }
                        return ValidationResult.validInstance();
                    });
                    categoryDetailScreen(categories.get(categoryNo));
                }));

            addCommonActions(actionOptions, goBack);
            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void categoryDetailScreen(Category category) {
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("category detail");
            category.printDetail();

            // only admin can access to this screen, so no need to check account's role
            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("delete category", () -> {
                    Boolean isDelete = Helpers.requestBooleanInput(scanner, "Do you want to delete this category [y/n]? ");
                    if (isDelete) {
                        categoryService.delete(category.getId());
                        Logger.printSuccess("Delete category successfully!");
                        goBack.set(true);
                    }
                }));
                add(new ActionOption<>("go back", () -> goBack.set(true)));
            }};

            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void addCategoryScreen() {
        try {
            banner("add category");
            CreateCategoryModel model = new CreateCategoryModel();
            Helpers.requestStringInput(scanner, "Enter category name: ", "name", model);
            categoryService.add(model);
            Logger.printSuccess("Add new category successfully!");
        } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "addOrUpdateProductScreen", e);
        }
    }

    public void customerScreen() {
        AtomicReference<SearchCustomerModel> searchModel = new AtomicReference<>(new SearchCustomerModel());
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("customer list");
            List<Account> customers = customerService.search(searchModel.get());

            System.out.printf("search by name/phone/email: %-5s\n",
                    Helpers.isNullOrEmpty(searchModel.get().getSearchText()) ? "n/a" : searchModel.get().getSearchText());
            System.out.printf("sort by: %s\n\n",
                    Helpers.isNullOrEmpty(searchModel.get().getSortedBy()) ? "n/a" : searchModel.get().getSortedBy());

            System.out.printf("%-7s %-25s %-25s %-20s %-10s\n", "No.", "Username", "Name", "Phone", "Email");
            System.out.println("-".repeat(100));
            if (customers.isEmpty())
                Logger.printInfo("No customer found...");
            for (int customerNo = 0; customerNo < customers.size(); customerNo++) {
                Account customer = customers.get(customerNo);
                System.out.printf("%-7d %-25s %-25s %-20s %-10s\n", customerNo, customer.getUsername(), customer.getFullName(),
                        customer.getProfile().getPhone(), customer.getProfile().getEmail());
            }

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("search", () -> {
                    try {
                        SearchCustomerModel newSearchModel = new SearchCustomerModel();
                        Helpers.requestStringInput(scanner, "Search by name/phone/email: ", "searchText", newSearchModel);
                        Helpers.requestStringInput(scanner, "Sort by: ", "sortedBy", newSearchModel);
                        searchModel.set(newSearchModel);
                    } catch (RuntimeException e) {
                        Logger.printError(this.getClass().getName(), "productScreen", e);
                    }
                }));
                add(new ActionOption<>("clear search", () -> searchModel.set(new SearchCustomerModel())));
            }};

            if (customers.size() > 0)
                actionOptions.add(new ActionOption<>("view detail", () -> {
                    int customerNo = Helpers.requestIntInput(scanner, "Enter customer No. to view detail: ", (value) -> {
                        if (value < 0 || value >= customers.size()) {
                            return ValidationResult.inValidInstance("Given customer No. is out of index.");
                        }
                        return ValidationResult.validInstance();
                    });
                    customerDetailScreen(customers.get(customerNo));
                }));

            addCommonActions(actionOptions, goBack);
            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void customerDetailScreen(Account customer) {
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("customer detail");
            customer.printDetail();

            // only admin can access to this screen, so no need to check account's role
            List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
                add(new ActionOption<>("delete customer", () -> {
                    Boolean isDelete = Helpers.requestBooleanInput(scanner, "Do you want to delete this customer [y/n]? ");
                    if (isDelete) {
                        customerService.delete(customer.getId());
                        Logger.printSuccess("Delete customer successfully!");
                        goBack.set(true);
                    }
                }));
                add(new ActionOption<>("go back", () -> goBack.set(true)));
            }};

            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void cartScreen() {
        AtomicBoolean goBack = new AtomicBoolean(false);
        do {
            banner("your cart");
            Cart cart = cartService.getCachedCart();
            cart.printDetail();

            List<ActionOption<Runnable>> actionOptions = new ArrayList<>();
            if (!cart.getItems().isEmpty())
                actionOptions.addAll(new ArrayList<>() {{
                    add(new ActionOption<>("place order", () -> {
                        Boolean isPlaceOrder = Helpers.requestBooleanInput(scanner, "Do you want to submit order [y/n]? ");
                        if (isPlaceOrder) {
                            if(!orderService.add(cart)){
                                Logger.printWarning("Fail to submit order!");
                                return;
                            }
                            customerService.updateMembership();
                            cartService.reset();
                            Logger.printSuccess("Submit order successfully!");
                            goBack.set(true);
                        }
                    }));
                    add(new ActionOption<>("update quantity", () -> {
                        int itemNo = Helpers.requestIntInput(scanner, "Enter item No.: ", (value) -> {
                            if (value < 0 || value >= cart.getItems().size()) {
                                return ValidationResult.inValidInstance("Given item No. is out of index.");
                            }
                            return ValidationResult.validInstance();
                        });
                        int quantity = Helpers.requestIntInput(scanner, "Enter quantity: ", (value) -> {
                            if(value < 0)
                                return ValidationResult.inValidInstance("Quantity must not a negative number!");
                            return ValidationResult.validInstance();
                        });
                        cartService.updateItem(itemNo, quantity);
                    }));
                    add(new ActionOption<>("remove item", () -> {
                        int itemNo = Helpers.requestIntInput(scanner, "Enter item No.: ", (value) -> {
                            if (value < 0 || value >= cart.getItems().size()) {
                                return ValidationResult.inValidInstance("Given item No. is out of index.");
                            }
                            return ValidationResult.validInstance();
                        });
                        cartService.removeItem(itemNo);
                    }));
                }});

            addCommonActions(actionOptions, goBack);
            Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
        } while (!goBack.get());
    }

    public void orderScreen() {
        banner("order list");
        // TODO: implement
    }

    public void orderDetailScreen() {
        banner("order detail");
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
            boolean isExisted;
            do {
                Helpers.requestStringInput(scanner, "Enter your username: ", "username", model);
                isExisted = authService.isUsernameExisted(model.getUsername());
                if (isExisted)
                    Logger.printWarning("Username is already existed!");
            } while (isExisted);
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
        cartService.unload();
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
        Boolean answer = Helpers.requestBooleanInput(scanner, "Do you want to continue? [y/n]: ");
        if (answer)
            homeScreen();
        else
            exitScreen();
    }

    private List<ActionOption<Runnable>> addCommonActions(List<ActionOption<Runnable>> actionOptions, AtomicBoolean goBack) {
        actionOptions.addAll(new ArrayList<>() {{
            add(new ActionOption<>("go back", () -> goBack.set(true)));
            add(new ActionOption<>("logout", () -> {
                authService.logout();
                cartService.unload();
                System.out.println();
                Logger.printSuccess("Logout successfully.");
                goBack.set(true);
            }));
            add(new ActionOption<>("exit", () -> exitScreen()));
        }});
        return actionOptions;
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
