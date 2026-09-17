package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();
        
        System.out.println("=== Welcome to the Chat App Registration ===");

        System.out.print("Enter Username (must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter Password (>=8 chars, 1 Capital, 1 Number, 1 Special): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
        String cellNumber = scanner.nextLine();

        String registrationMessage = loginSystem.registerUser(username, password, cellNumber);
        System.out.println("\n" + registrationMessage);
        
        // If registration was successful, proceed to login
        if (registrationMessage.equals("User successfully registered.")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String loginPass = scanner.nextLine();
            
            boolean isLoggedIn = loginSystem.loginUser(loginUser, loginPass);
            String loginStatus = loginSystem.returnLoginStatus(isLoggedIn);
            
            System.out.println("\n" + loginStatus);
        }
        
        scanner.close();
    }
}