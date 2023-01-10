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

import kratos.oms.repository.*;
import kratos.oms.service.*;

public class Main {
    private final static String DATA_DIRECTORY = "data";

    /**
     * Entry point of Order-Management-System program
     */
    public static void main(String[] args) {
        // instantiate repositories
        AccountRepository accountRepository = new FileAccountRepositoryImpl(DATA_DIRECTORY);
        CartRepository cartRepository = new FileCartRepositoryImpl(DATA_DIRECTORY);
        ProductRepository productRepository = new FileProductRepositoryImpl(DATA_DIRECTORY);
        OrderRepository orderRepository = new FileOrderRepositoryImpl(DATA_DIRECTORY);
        CategoryRepository categoryRepository = new FileCategoryRepositoryImpl(DATA_DIRECTORY);

        // instantiate services
        AuthService authService = new AuthService(accountRepository);
        CartService cartService = new CartService(authService, cartRepository);
        ProductService productService = new ProductService(productRepository, categoryRepository, cartRepository);
        OrderService orderService = new OrderService(orderRepository);
        CategoryService categoryService = new CategoryService(categoryRepository, productRepository);
        CustomerService customerService = new CustomerService(accountRepository, orderRepository);
        StatisticService statisticService = new StatisticService(orderRepository, productRepository);

        MenuService menuService = new MenuService(authService, cartService, productService,
                orderService, categoryService, customerService, statisticService);
        menuService.welcomeScreen();
    }

}