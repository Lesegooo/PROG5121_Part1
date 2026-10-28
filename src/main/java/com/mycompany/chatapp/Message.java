package com.mycompany.chatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.util.Arrays;

/**
 * Represents one QuickChat message and keeps track of all
 * sent and stored messages while the program runs.
 */
public class Message {

    // Shared by all messages
    private static int totalSent = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Normal constructor: the ID is generated randomly
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // Test constructor: lets unit tests supply a known message ID
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Builds a random ten-digit ID, one digit per loop pass
    private String generateMessageID() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append((int) (Math.random() * 10));
        }
        return id.toString();
    }

    // The ID must be no more than ten characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Message must not exceed 250 characters
    public static String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = text.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    // Country code (+ and 1-3 digits) followed by at most ten digits
    public static String checkRecipientCell(String cell) {
        // Regex adapted from: Google (2023) Gson User Guide. Available at: https://github.com/google/gson/blob/main/UserGuide.md (Accessed: 15 October 2026).
        if (cell != null && cell.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
             + "international code. Please correct the number and try again.";
    }

    // First two ID digits : message number : FIRSTWORDLASTWORD (capitals)
    public String createMessageHash() {
        String idPart = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        return (idPart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // choice: 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Returns every message sent so far, in the required order
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n")
              .append("Message Hash: ").append(m.messageHash).append("\n")
              .append("Recipient: ").append(m.recipient).append("\n")
              .append("Message: ").append(m.messageText).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalSent;
    }
// Where stored messages are saved; tests can point this at a temporary file
    private static String storageFile = "messages.json";

    public static void setStorageFile(String fileName) {
        storageFile = fileName;
    }
//Reads the JSON file into an array and return it as a list.
//JSON reading adapted from: Shah, A.U.R. (2019)How to read JSON from a file 
//using Gsin in Java. Available at" https://attacomsian.com/blog/gson-read-json-file
//(Accessed: 26 October 2026).        
    public static List<Message> readStoredMessages() {
        List<Message> stored = new ArrayList<>();
        try (FileReader reader = new FileReader(storageFile)) {
            Message[] fromFile = new Gson().fromJson(reader, Message[].class);
            if (fromFile != null) {
                stored.addAll(Arrays.asList(fromFile));
            }
        } catch (IOException e) {
            // No file yet means nothing is stored, so return the empty list
        }
        return stored;
    }

    public void storeMessage() {
        List<Message> stored = readStoredMessages();   // keep earlier runs' messages
        stored.add(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(storageFile)) {
            gson.toJson(stored, writer);
        } catch (IOException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }
    // For unit tests only
    public static void resetCounters() {
        totalSent = 0;
        sentMessages.clear();
    }
    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
}