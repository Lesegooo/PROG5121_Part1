package com.mycompany.chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        //Part 1: registration and login
        System.out.println("=== Welcome to the Chat App Registration ===");
        loginSystem.registerUserWithRetry(scanner);

        System.out.println("\n=== Login ===");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean isLoggedIn = loginSystem.loginUser(loginUser, loginPass);
        System.out.println("\n" + loginSystem.returnLoginStatus(isLoggedIn));

        // Messages can only be sent after a successful login
        if (!isLoggedIn) {
            scanner.close();
            return;
        }

        //Part 2: QuickChat
        System.out.println("\nWelcome to QuickChat.");

        int maxMessages = readPositiveInt(scanner,
                "How many messages do you wish to enter? ");
        int entered = 0;          
        boolean running = true;

        // While loop: keeps the menu running until the user chooses Quit
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered all "
                                + maxMessages + " messages.");
                    } else {
                        entered = sendMessages(scanner, maxMessages, entered);
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        scanner.close();
    }

    /**
     * For loop: lets the user enter the remaining messages.
     * The loop counter doubles as the message number (starts at 0).
     * Returns how many messages have been entered after this run.
     */
    private static int sendMessages(Scanner scanner, int maxMessages, int entered) {
        for (int i = entered; i < maxMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + maxMessages + " ---");

            // Recipient: ask again until the number is valid
            String recipient;
            while (true) {
                System.out.print("Recipient cell number (e.g. +27718693002): ");
                recipient = scanner.nextLine().trim();
                String cellResult = Message.checkRecipientCell(recipient);
                System.out.println(cellResult);
                if (cellResult.startsWith("Cell phone number successfully")) {
                    break;
                }
            }

            // Message text: ask again until it is within 250 characters
            String text;
            while (true) {
                System.out.print("Enter your message: ");
                text = scanner.nextLine();
                if (text.trim().isEmpty()) {
                    System.out.println("The message cannot be empty.");
                } else if (text.length() > 250) {
                    System.out.println("Please enter a message of less than 250 characters.");
                } else {
                    System.out.println("Message sent");
                    break;
                }
            }

            Message msg = new Message(i, recipient, text);

            System.out.println("\n1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int choice = readChoice(scanner, "Choose an option: ", 1, 3);

            System.out.println(msg.sentMessage(choice));

            if (choice == 1) {
                // Full details, in the required order
                System.out.println("\nMessage ID: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
            } else if (choice == 2) {
                System.out.print("> ");
                if (scanner.nextLine().trim().equals("0")) {
                    System.out.println("Message deleted.");
                }
            }
        }
        return maxMessages;
    }

    // Keeps asking until the user types a whole number of 1 or more
    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    // Keeps asking until the user types a number between min and max
    private static int readChoice(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }
}