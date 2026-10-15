package com.mycompany.chatapp;

public class Message {
    
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    
    // Constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }
    
    // 1. Generate a random 10-digit Message ID
    private String generateMessageID() {
        long id = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }
    
    // 2. Check Message ID is not more than 10 characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }
    
    // 3. Check Recipient Cell (max 10 chars and starts with a code)
    public boolean checkRecipientCell() {
        // Starts with + and total length <= 10
        return recipient.matches("^\\+\\d{1,9}$") && recipient.length() <= 10;
    }
    
    // 4. Create Message Hash
    public String createMessageHash() {
        String idPart = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        return (idPart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }
    
    // 5. sentMessage - returns status based on user choice
    public String sentMessage(int choice) {
        switch (choice) {
            case 1: return "Message successfully sent.";
            case 2: return "Press 0 to delete the message.";
            case 3: return "Message successfully stored.";
            default: return "Invalid option.";
        }
    }
    
    // 6. printMessages - returns message details
    public String printMessages() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }
    
    // 7. returnTotalMessages
    public int returnTotalMessages() {
        return messageNumber;
    }
    
    // 8. storeMessage - JSON storage
    public void storeMessage() {
        // Will implement later
        System.out.println("Message stored successfully.");
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
}