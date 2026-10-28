package com.mycompany.chatapp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import org.junit.After;

public class MessageTest {

    private static final String MSG1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String MSG2 = "Hi Keegan, did you receive the payment?";

    // Start every test with clean counters
    @Before
    public void reset() {
       Message.setStorageFile("test_messages.json");
       new File("test_messages.json").delete();
       Message.resetCounters();
    }
    @After
    public void cleanUp(){
        new File("test_messages.json").delete();
    }

    private String makeText(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

    // --- Message length ---
    @Test
    public void messageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(MSG1));
    }

    @Test
    public void messageLengthExactly250IsAccepted() {
        assertEquals("Message ready to send.", Message.checkMessageLength(makeText(250)));
    }

    @Test
    public void messageLengthFailure() {
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                Message.checkMessageLength(makeText(255)));
    }

    // --- Recipient number ---
    @Test
    public void recipientSuccess() {
        assertEquals("Cell phone number successfully captured.",
                Message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void recipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                Message.checkRecipientCell("08575975889"));
    }

    // --- Message hash ---
    @Test
    public void hashTestCase1() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    // Remaining hashes tested in a loop, as the brief asks
    @Test
    public void hashesInLoop() {
        String[] texts = { MSG1, MSG2 };
        String[] expected = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };
        for (int i = 0; i < texts.length; i++) {
            Message m = new Message("0012345678", i, "+27718693002", texts[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    // --- Message ID ---
    @Test
    public void messageIdIsCreated() {
        Message m = new Message(0, "+27718693002", MSG1);
        assertTrue(m.checkMessageID());
        assertTrue(m.getMessageID().matches("\\d{10}"));
        System.out.println("Message ID generated: " + m.getMessageID());
    }

    @Test
    public void sendMessageOption() {
        Message m = new Message(0, "+27718693002", MSG1);
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    public void disregardMessageOption() {
        Message m = new Message(1, "08575975889", MSG2);
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    public void storeMessageOption() {
        Message m = new Message(0, "+27718693002", MSG1);
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    // --- Totals ---
    // Test data: message 1 is sent, message 2 is discarded, so the total is 1
    @Test
    public void totalMessagesSent() {
        new Message(0, "+27718693002", MSG1).sentMessage(1);
        new Message(1, "08575975889", MSG2).sentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void printMessagesContainsSentDetails() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        m.sentMessage(1);
        String output = Message.printMessages();
        assertTrue(output.contains("Message ID: 0012345678"));
        assertTrue(output.contains("Message Hash: 00:0:HITONIGHT"));
        assertTrue(output.contains("Recipient: +27718693002"));
        assertTrue(output.contains("Message: " + MSG1));
    }
}