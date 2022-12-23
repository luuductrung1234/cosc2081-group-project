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
import kratos.oms.service.AuthService;
import kratos.oms.service.CartService;
import kratos.oms.service.MenuService;
import kratos.oms.service.ProductService;

public class Main {
    /**
     * Entry point of Order-Management-System program
     */
    public static void main(String[] args) {
        AccountRepository accountRepository = new FileAccountRepositoryImpl("data/accounts.txt");
        CartRepository cartRepository = new FileCartRepositoryImpl("data/carts.txt");
        ProductRepository productRepository=new FileProductRepository("data/product.txt");
        AuthService authService = new AuthService(accountRepository);
        CartService cartService = new CartService(authService, cartRepository);
        ProductService productService=new ProductService(productRepository);
        MenuService menuService = new MenuService(authService, cartService, productService);
        menuService.welcomeScreen();
    }
}