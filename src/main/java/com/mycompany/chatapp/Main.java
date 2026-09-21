package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("=== Welcome to the Chat App Registration ===");

        loginSystem.registerUserWithRetry(scanner);

        System.out.println("\n=== Login ===");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean isLoggedIn = loginSystem.loginUser(loginUser, loginPass);
        String loginStatus = loginSystem.returnLoginStatus(isLoggedIn);

        System.out.println("\n" + loginStatus);

        scanner.close();
    }
}