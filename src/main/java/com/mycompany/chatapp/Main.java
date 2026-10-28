package com.mycompany.chatapp;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for QuickChat. Handles registration, login, the numeric menu,
 * sending messages and the stored-messages features.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        // ---------- Part 1: registration and login ----------
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

        // ---------- Part 3: arrays, filled from this run and from messages.json ----------
        MessageManager manager = new MessageManager(loginUser);
        int loaded = manager.loadStoredFromJson();
        if (loaded > 0) {
            System.out.println(loaded + " stored message(s) loaded from messages.json.");
        }

        // ---------- Part 2: QuickChat ----------
        System.out.println("\nWelcome to QuickChat.");

        int maxMessages = readPositiveInt(scanner,
                "How many messages do you wish to enter? ");
        int entered = 0;          // how many messages have been entered so far
        boolean running = true;

        // While loop: keeps the menu running until the user chooses Quit
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered all "
                                + maxMessages + " messages.");
                    } else {
                        entered = sendMessages(scanner, maxMessages, entered, manager);
                    }
                    break;
                case "2":
                    System.out.println("\n--- Sent messages ---");
                    System.out.println(manager.displayReport(MessageManager.SENT));
                    break;
                case "3":
                    running = false;
                    break;
                case "4":
                    storedMessagesMenu(scanner, manager);
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3 or 4.");
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        scanner.close();
    }

    /**
     * For loop: lets the user enter the remaining messages.
     * The loop counter doubles as the message number (starts at 0).
     * Every message is recorded in the arrays with its flag.
     */
    private static int sendMessages(Scanner scanner, int maxMessages, int entered,
                                    MessageManager manager) {
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

            // Send / disregard / store
            System.out.println("\n1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int choice = readChoice(scanner, "Choose an option: ", 1, 3);

            System.out.println(msg.sentMessage(choice));

            if (choice == 1) {
                manager.addMessage(msg, MessageManager.SENT);
                System.out.println("\nMessage ID: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
            } else if (choice == 2) {
                manager.addMessage(msg, MessageManager.DISREGARD);
                System.out.print("> ");
                if (scanner.nextLine().trim().equals("0")) {
                    System.out.println("Message deleted.");
                }
            } else {
                manager.addMessage(msg, MessageManager.STORED);
            }
        }
        return maxMessages;
    }

    /** Option 4: features that search and manage the message arrays. */
    private static void storedMessagesMenu(Scanner scanner, MessageManager manager) {
        boolean back = false;
        while (back == false) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Sender and recipient of all stored messages");
            System.out.println("b) Longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search messages for a recipient");
            System.out.println("e) Delete a message using its hash");
            System.out.println("f) Report of all messages");
            System.out.println("g) Back to main menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim().toLowerCase();

            switch (option) {
                case "a":
                    System.out.println(manager.displayStoredSendersAndRecipients());
                    break;
                case "b":
                    System.out.println("Longest stored message: " + manager.longestStoredMessage());
                    break;
                case "c":
                    System.out.print("Enter the message ID: ");
                    String id = scanner.nextLine().trim();
                    System.out.println("Recipient: " + manager.recipientForMessageID(id));
                    System.out.println("Message: " + manager.searchByMessageID(id));
                    break;
                case "d":
                    System.out.print("Enter the recipient number: ");
                    ArrayList<String> found = manager.searchByRecipient(scanner.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    }
                    for (String text : found) {
                        System.out.println("- " + text);
                    }
                    break;
                case "e":
                    System.out.print("Enter the message hash: ");
                    System.out.println(manager.deleteByHash(scanner.nextLine().trim().toUpperCase()));
                    break;
                case "f":
                    System.out.println("\n--- Sent messages ---");
                    System.out.println(manager.displayReport(MessageManager.SENT));
                    System.out.println("--- Stored messages ---");
                    System.out.println(manager.displayReport(MessageManager.STORED));
                    break;
                case "g":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a letter from a to g.");
            }
        }
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