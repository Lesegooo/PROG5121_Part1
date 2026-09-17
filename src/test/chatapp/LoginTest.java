package com.mycompany.chatapp;

import com.mycompany.chatapp.Login;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    
    // Create an instance of the Login class to test
    Login login = new Login();

    // --- assertEquals Tests (From the rubric table) ---

    @Test
    public void testUsernameCorrectlyFormatted() {
        // The rubric says use "kyl_1" and expect a specific welcome message.
        // We must register the user first to trigger the message.
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        // The rubric text says: "Welcome <user first name>, <user last name> it is great to see you again."
        // But for assertEquals on registration, it expects the registration success message.
        // *Note: The rubric table is slightly confusing here. It shows a Login message for a Registration test.
        // We will test the registration success message as it matches the logic.
        assertEquals("User successfully registered.", result); 
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        // Data: "kyle!!!!!!!" 
        String result = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testPasswordMeetsComplexity() {
        // Data: "Ch&&sec@ke99!"
        boolean result = login.checkPasswordComplexity("Ch&&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        // Data: "password"
        boolean result = login.checkPasswordComplexity("password");
        assertFalse(result);
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        // Data: "+27838968976"
        boolean result = login.checkCellPhoneNumber("+27838968976");
        assertTrue(result);
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        // Data: "08966553"
        boolean result = login.checkCellPhoneNumber("08966553");
        assertFalse(result);
    }

    // --- assertTrue/False Tests (From the rubric table) ---

    @Test
    public void testLoginSuccessful() {
        // Register first, then try to login
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testLoginFailed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        boolean result = login.loginUser("kyl_1", "WrongPassword123!");
        assertFalse(result);
    }

    @Test
    public void testUsernameCorrectlyFormattedAssertTrue() {
        boolean result = login.checkUserName("kyl_1");
        assertTrue(result);
    }

    @Test
    public void testUsernameIncorrectlyFormattedAssertFalse() {
        boolean result = login.checkUserName("kyle!!!!!!!");
        assertFalse(result);
    }
}