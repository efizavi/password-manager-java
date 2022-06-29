package com.hit.service;

import com.hit.algorithm.AbstractAsymmetricAlgoEncryption;
import com.hit.algorithm.IAlgoEncryption;
import com.hit.dao.IDao;
import com.hit.dm.AbstractLogin;
import com.hit.dm.UserLogin;
import com.hit.service.exceptions.*;

import java.util.Arrays;

public class LoginManagementService extends AbstractServiceBase{
    public LoginManagementService(IDao dao, AbstractAsymmetricAlgoEncryption algo) {
        super(dao);
        this.algo = algo;
    }

    // Settings - these would normally be in some configuration file
    // But since we are limited on time for this project,
    // They are implemented as constant members
    private static final int minUsernameLength = 5;
    private static final int maxUsernameLength = 15;
    private static final int minPasswordLength = 8;
    private static final int maxPasswordLength = 20;

    private IAlgoEncryption algo;
    
    public boolean authenticate(String username, String password) {
        // Encrypt password to compare with securely-stored data
        byte[] encryptedPassword = algo.encrypt(password);

        // Get login with matching username, and compare encrypted passwords
        AbstractLogin login = getLoginWithUsername(username);
        if (login != null) {
            // Return whether passwords match
            return Arrays.equals(login.getPassword(), encryptedPassword);
        }

        // No such username - return false
        return false;
    }

    public void register(String username, String password) {
        // Check if username is valid
        if (username == null || username.length() < minUsernameLength)
            throw new UsernameTooShortException();
        if (username.length() > maxUsernameLength)
            throw new UsernameTooLongException();
        if (getLoginWithUsername(username) != null)
            throw new UsernameTakenException();

        // Check if password is valid
        if (password == null || password.length() < minPasswordLength)
            throw new PasswordTooShortException();
        if (password.length() > maxPasswordLength)
            throw new PasswordTooLongException();

        // Encrypt password for secure storage
        byte[] encryptedPassword = algo.encrypt(password);

        // Generate new login object for regular user
        UserLogin login = new UserLogin();
        login.setUserName(username);
        login.setPassword(encryptedPassword);

        // Write object in data layer, return success
        dao.addLogin(login);
    }

    public boolean unregister(String username) {
        // Check if user exists
        if (getLoginWithUsername(username) != null) {

            // Delete login from data layer and return success
            dao.removeLogin(username);
            return true;
        }

        // User not found - return false
        return false;
    }

    public boolean changePassword(String username, String newPassword) {
        // Find matching login
        AbstractLogin login = getLoginWithUsername(username);
        if (login != null) {

            // Encrypt password and update found user
            byte[] encryptedPassword = algo.encrypt(newPassword);
            login.setPassword(encryptedPassword);

            // Write updated login to data layer and return success
            dao.updateLogin(username, login);
            return true;
        }

        return false;
    }
}
