package com.mycompany.chatapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    
    Login login = new Login();

    @Test
    public void testUsernameCorrectlyFormatted() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("User successfully registered.", result); 
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        String result = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testPasswordMeetsComplexity() {
        boolean result = login.checkPasswordComplexity("Ch&&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        boolean result = login.checkPasswordComplexity("password");
        assertFalse(result);
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        boolean result = login.checkCellPhoneNumber("+27838968976");
        assertTrue(result);
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        boolean result = login.checkCellPhoneNumber("08966553");
        assertFalse(result);
    }

    @Test
    public void testLoginSuccessful() {
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