package com.mycompany.chatapp;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageManagerTest {

    private static final String TEXT1 = "Did you get the cake?";
    private static final String TEXT2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT3 = "Yohoooo, I am at your gate.";
    private static final String TEXT4 = "It is dinner time!";
    private static final String TEXT5 = "Ok, I am leaving without you.";

    private MessageManager manager;
    private Message msg1;
    private Message msg2;
    private Message msg4;

    // Fills the arrays with the five test messages before every test
    @Before
    public void setUp() {
        Message.setStorageFile("test_messages.json");
        new File("test_messages.json").delete();
        Message.resetCounters();

        manager = new MessageManager("Developer");
        msg1 = new Message("0000000001", 0, "+27834557896", TEXT1);
        msg2 = new Message("0000000002", 1, "+27838884567", TEXT2);
        Message msg3 = new Message("0000000003", 2, "+27834484567", TEXT3);
        msg4 = new Message("0838884567", 3, "0838884567", TEXT4);
        Message msg5 = new Message("0000000005", 4, "+27838884567", TEXT5);

        manager.addMessage(msg1, MessageManager.SENT);
        manager.addMessage(msg2, MessageManager.STORED);
        manager.addMessage(msg3, MessageManager.DISREGARD);
        manager.addMessage(msg4, MessageManager.SENT);
        manager.addMessage(msg5, MessageManager.STORED);
    }

    @After
    public void cleanUp() {
        new File("test_messages.json").delete();
    }

    @Test
    public void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT1, TEXT4), manager.getSentMessages());
    }

    @Test
    public void disregardedAndStoredArraysCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT3), manager.getDisregardedMessages());
        assertEquals(Arrays.asList(TEXT2, TEXT5), manager.getStoredMessages());
    }

    @Test
    public void hashAndIdArraysCorrectlyPopulated() {
        assertEquals(5, manager.getMessageHashes().size());
        assertEquals(5, manager.getMessageIDs().size());
        assertEquals(msg2.getMessageHash(), manager.getMessageHashes().get(1));
        assertEquals("0838884567", manager.getMessageIDs().get(3));
    }

    @Test
    public void displayLongestMessage() {
        assertEquals(TEXT2, manager.longestStoredMessage());
    }

    @Test
    public void searchForMessageID() {
        assertEquals(TEXT4, manager.searchByMessageID("0838884567"));
        assertEquals("0838884567", manager.recipientForMessageID("0838884567"));
    }

    @Test
    public void searchForMessageIDNotFound() {
        assertEquals("Message ID not found.", manager.searchByMessageID("9999999999"));
    }

    @Test
    public void searchMessagesForRecipient() {
        assertEquals(Arrays.asList(TEXT2, TEXT5), manager.searchByRecipient("+27838884567"));
    }

    @Test
    public void deleteMessageUsingHash() {
        String result = manager.deleteByHash(msg2.getMessageHash());
        assertEquals("Message: \"" + TEXT2 + "\" successfully deleted.", result);
        assertFalse(manager.getStoredMessages().contains(TEXT2));
        assertEquals(4, manager.getMessageHashes().size());
    }

    @Test
    public void displayReportShowsSentMessages() {
        String report = manager.displayReport(MessageManager.SENT);
        assertTrue(report.contains("Message Hash: " + msg1.getMessageHash()));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: " + TEXT1));
        assertTrue(report.contains("Message Hash: " + msg4.getMessageHash()));
        assertTrue(report.contains("Message: " + TEXT4));
    }

    // Stores a message to the JSON file, then reads it back into the arrays
    @Test
    public void storedMessagesReadFromJsonFile() {
        msg2.storeMessage();
        MessageManager fresh = new MessageManager("Developer");
        assertEquals(1, fresh.loadStoredFromJson());
        assertEquals(Arrays.asList(TEXT2), fresh.getStoredMessages());
    }
}