//This class handles user registration and login logic
package com.mycompany.chatapp;

import java.util.Scanner;

public class Login {

    private String storedUsername;
    private String storedPassword;
    private String storedCellNumber;
    private String firstName;
    private String lastName;

    // 1. Check Username: Must contain '_' and be <= 5 chars
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // 2. Check Password Complexity: >=8 chars, 1 capital, 1 number, 1 special char
    public boolean checkPasswordComplexity(String password) {
        boolean hasLength = password.length() >= 8;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasLength && hasCapital && hasNumber && hasSpecial;
    }

    // 3. Check Cell Phone Number and must contain international code (+27

    // Oracle (2024) 'Pattern (Java SE 21 & JDK 21)', Oracle Help Center.
    // Available at: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
    // (Accessed: 18 September 2026).
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27\\d{9,10}$");
    }

    // 4. Register User and returns a message based on the checks.
    public String registerUser(String username, String password, String cellNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;

        return "Username successfully captured." + System.lineSeparator()
                + "Password successfully captured." + System.lineSeparator()
                + "Cell number successfully captured." + System.lineSeparator()
                + "User registered successfully.";
    }

    // 5. Register User with a retry loop

    public String registerUserWithRetry(Scanner scanner) {
        String result;
        String username;
        String password;
        String cellNumber;

        do {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter Username (must contain '_' and be <= 5 chars): ");
            username = scanner.nextLine();

            System.out.print("Enter Password (>=8 chars, 1 Capital, 1 Number, 1 Special): ");
            password = scanner.nextLine();

            System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
            cellNumber = scanner.nextLine();

            result = registerUser(username, password, cellNumber, firstName, lastName);
            System.out.println("\n" + result);

            boolean success = checkUserName(username)
                    && checkPasswordComplexity(password)
                    && checkCellPhoneNumber(cellNumber);

            if (!success) {
                System.out.println("Let's try that again.\n");
            }

        } while (!(checkUserName(username) && checkPasswordComplexity(password) && checkCellPhoneNumber(cellNumber)));

        return result;
    }

    // 5. Login User to verify credentials against stored data
    public boolean loginUser(String username, String password) {
        if (this.storedUsername == null || this.storedPassword == null) {
            return false;
        }
        return this.storedUsername.equals(username) && this.storedPassword.equals(password);
    }

    // 6. Returns the specific message required
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}