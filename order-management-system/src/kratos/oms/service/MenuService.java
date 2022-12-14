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

import java.util.Scanner;

public class MenuService {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;

    public MenuService(AuthService authService) {
        this.authService = authService;
    }

    public void homeScreen() {
        welcomeScreen();
        if (!authService.isAuthenticated()) {
            loginScreen();
        }
        System.out.println("=============================");
        System.out.println("=            HOME           =");
        System.out.println("=============================");
    }

    public void loginScreen() {
        System.out.println("=============================");
        System.out.println("=           LOGIN           =");
        System.out.println("=============================");
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        authService.login(username, password);
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
    }
}
