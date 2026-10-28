package com.mycompany.chatapp;

import java.util.ArrayList;

/**
 * Holds the message arrays for QuickChat and the features that search them.
 * messageIDs, messageHashes, recipients, messageTexts and flags are parallel
 * arrays: the same index in each one refers to the same message.
 */
public class MessageManager {

    public static final String SENT = "Sent";
    public static final String STORED = "Stored";
    public static final String DISREGARD = "Disregard";

    private final String sender;

    // Arrays required by the brief
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();

    // Parallel arrays used for searching
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> messageTexts = new ArrayList<>();
    private final ArrayList<String> flags = new ArrayList<>();

    public MessageManager(String sender) {
        this.sender = sender;
    }

    // Adds one message to the parallel arrays and to its category array
    public void addMessage(String id, String hash, String recipient, String text, String flag) {
        messageIDs.add(id);
        messageHashes.add(hash);
        recipients.add(recipient);
        messageTexts.add(text);
        flags.add(flag);

        if (flag.equals(SENT)) {
            sentMessages.add(text);
        } else if (flag.equals(STORED)) {
            storedMessages.add(text);
        } else if (flag.equals(DISREGARD)) {
            disregardedMessages.add(text);
        }
    }

    public void addMessage(Message m, String flag) {
        addMessage(m.getMessageID(), m.getMessageHash(), m.getRecipient(), m.getMessageText(), flag);
    }

    // Reads messages.json and adds any message not already in the arrays
    public int loadStoredFromJson() {
        int added = 0;
        for (Message m : Message.readStoredMessages()) {
            if (!messageIDs.contains(m.getMessageID())) {
                addMessage(m, STORED);
                added++;
            }
        }
        return added;
    }

    // a. Sender and recipient of every stored message
    public String displayStoredSendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED)) {
                sb.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipients.get(i)).append("\n");
            }
        }
        return sb.length() == 0 ? "No stored messages." : sb.toString();
    }

    // b. The longest stored message
    public String longestStoredMessage() {
        String longest = "";
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED) && messageTexts.get(i).length() > longest.length()) {
                longest = messageTexts.get(i);
            }
        }
        return longest.isEmpty() ? "No stored messages." : longest;
    }

    // c. Search by message ID: the message text, or a not-found message
    public String searchByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? "Message ID not found." : messageTexts.get(index);
    }

    public String recipientForMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? "Message ID not found." : recipients.get(index);
    }

    // d. Every sent or stored message for one recipient
    public ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            boolean notDisregarded = !flags.get(i).equals(DISREGARD);
            if (recipients.get(i).equals(recipient) && notDisregarded) {
                found.add(messageTexts.get(i));
            }
        }
        return found;
    }

    // e. Delete a message using its hash, removing it from every array
    public String deleteByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index == -1) {
            return "Message hash not found.";
        }
        String text = messageTexts.get(index);
        String flag = flags.get(index);

        if (flag.equals(SENT)) {
            sentMessages.remove(text);
        } else if (flag.equals(STORED)) {
            storedMessages.remove(text);
        } else if (flag.equals(DISREGARD)) {
            disregardedMessages.remove(text);
        }
        messageIDs.remove(index);
        messageHashes.remove(index);
        recipients.remove(index);
        messageTexts.remove(index);
        flags.remove(index);

        return "Message: \"" + text + "\" successfully deleted.";
    }

    // f. Report of every message with the given flag (Sent or Stored)
    public String displayReport(String flag) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(flag)) {
                sb.append("Message ID: ").append(messageIDs.get(i)).append("\n")
                  .append("Message Hash: ").append(messageHashes.get(i)).append("\n")
                  .append("Recipient: ").append(recipients.get(i)).append("\n")
                  .append("Message: ").append(messageTexts.get(i)).append("\n\n");
            }
        }
        return sb.length() == 0 ? "No " + flag.toLowerCase() + " messages." : sb.toString();
    }

    public ArrayList<String> getSentMessages() { return sentMessages; }
    public ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<String> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHashes() { return messageHashes; }
    public ArrayList<String> getMessageIDs() { return messageIDs; }
}