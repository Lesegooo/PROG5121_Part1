//This class handles user registration and login logic
package com.mycompany.chatapp; 
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
    boolean hasCapital = !password.equals(password.toLowerCase());
    boolean hasNumber = password.matches(".*\\d.*");
    boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
    
    return hasLength && hasCapital && hasNumber && hasSpecial;
}
    // 3. Check Cell Phone Number: Must contain international code (+27) and <= 10 digits after
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27\\d{9,10}$");
    }

    // 4. Register User: Returns a message based on the checks
    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
        
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellNumber = cellNumber;
        
        this.firstName = "User"; 
        this.lastName = "One";

        return "User successfully registered.";
    }

    // 5. Login User: Verifies credentials against stored data
    public boolean loginUser(String username, String password) {
        if (this.storedUsername == null || this.storedPassword == null) {
            return false;
        }
        return this.storedUsername.equals(username) && this.storedPassword.equals(password);
    }

    // 6. Return Login Status: Returns the specific message required
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}