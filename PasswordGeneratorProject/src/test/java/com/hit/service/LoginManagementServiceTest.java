package com.hit.service;

import com.hit.algorithm.AbstractAsymmetricAlgoEncryption;
import com.hit.algorithm.RSAAsymmetricAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.AbstractLogin;
import com.hit.dm.UserLogin;
import com.hit.service.exceptions.*;
import com.hit.util.BackupAndRestore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class LoginManagementServiceTest {
    private static final String usernameValid = "foobarbaz";
    private static final String usernameNull = null;
    private static final String usernameLong = "abcd efghij klmnop qrstuv wxyzzzzzzzzz";
    private static final String usernameEmpty = "";
    private static final String usernameShort = "foo";
    private static final String passwordValid = "Aa123456";
    private static final String passwordValid2 = "Efi&NoaBestProj";
    private static final String passwordNull = null;
    private static final String passwordLong = "Abcdefghijklmnopqrstuvwxyz1234567890";
    private static final String passwordEmpty = "";
    private static final String passwordShort = "123";

    // NOTE! THIS TEST IF FOR THE EXAM!!
    @Test
    void backupAndRestore() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        LoginManagementService srv = new LoginManagementService(dao, rsa);

        // Create a user for testing
        createTestUser(dao, rsa);

        // Create backup service
        BackupAndRestore backupService = new BackupAndRestore();
        backupService.backup("src\\main\\resources\\datasource.txt", "src\\user_backup.txt", 0, 1000);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }
        String backupContent = (String)backupService.restore("src\\user_backup.txt");
        String originalContent = "";
        try {
            for (String line : Files.readAllLines(Paths.get("src\\main\\resources\\datasource.txt"))) {
                originalContent += line;
            }
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertEquals(backupContent, originalContent);

        // Cleanup
        dao.removeLogin(usernameValid);
    }

    @Test
    void register() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        LoginManagementService srv = new LoginManagementService(dao, rsa);

        // Check that invalid usernames throw correct exceptions
        Assertions.assertThrows(UsernameTooShortException.class, () -> srv.register(usernameNull, passwordValid));
        Assertions.assertThrows(UsernameTooShortException.class, () -> srv.register(usernameEmpty, passwordValid));
        Assertions.assertThrows(UsernameTooShortException.class, () -> srv.register(usernameShort, passwordValid));
        Assertions.assertThrows(UsernameTooLongException.class, () -> srv.register(usernameLong, passwordValid));

        // Check that invalid password throw correct exceptions
        Assertions.assertThrows(PasswordTooShortException.class, () -> srv.register(usernameValid, passwordNull));
        Assertions.assertThrows(PasswordTooShortException.class, () -> srv.register(usernameValid, passwordEmpty));
        Assertions.assertThrows(PasswordTooShortException.class, () -> srv.register(usernameValid, passwordShort));
        Assertions.assertThrows(PasswordTooLongException.class, () -> srv.register(usernameValid, passwordLong));

        // Check successful registration
        srv.register(usernameValid, passwordValid);
        AbstractLogin login = srv.getLoginWithUsername(usernameValid);
        Assertions.assertNotNull(login);
        Assertions.assertEquals(rsa.decrypt(login.getPassword()), passwordValid);

        // Check that username conflicts throw correct exceptions
        Assertions.assertThrows(UsernameTakenException.class, () -> srv.register(usernameValid, passwordValid));

        // Cleanup
        dao.removeLogin(usernameValid);
    }

    @Test
    void authenticate() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        LoginManagementService srv = new LoginManagementService(dao, rsa);

        // Create a user for testing
        createTestUser(dao, rsa);

        // Test authentication with correct and incorrect passwords
        Assertions.assertTrue(srv.authenticate(usernameValid, passwordValid));
        Assertions.assertFalse(srv.authenticate(usernameValid, passwordLong));

        // Cleanup
        dao.removeLogin(usernameValid);
    }

    @Test
    void unregister() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        LoginManagementService srv = new LoginManagementService(dao, rsa);

        // Create a user for testing
        createTestUser(dao, rsa);

        // Check that unregister fails for non-existing/invalid usernames
        Assertions.assertFalse(srv.unregister(usernameLong));

        // Check that unregisters succeeds once, and no more than once since user is removed
        Assertions.assertTrue(srv.unregister(usernameValid));
        Assertions.assertFalse(srv.unregister(usernameValid));

        // Verify that user is deleted from DAO
        Assertions.assertNull(srv.getLoginWithUsername(usernameValid));
    }

    @Test
    void changePassword() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        LoginManagementService srv = new LoginManagementService(dao, rsa);

        // Create a user for testing
        createTestUser(dao, rsa);

        // Check that changing password fails for non-existing/invalid usernames
        Assertions.assertFalse(srv.changePassword(usernameLong, passwordValid));
        Assertions.assertTrue(srv.changePassword(usernameValid, passwordValid2));

        // Check that the user's password changed successfully
        AbstractLogin login = srv.getLoginWithUsername(usernameValid);
        Assertions.assertEquals(rsa.decrypt(login.getPassword()), passwordValid2);

        // Cleanup
        dao.removeLogin(usernameValid);
    }

    private void createTestUser(IDao dao, AbstractAsymmetricAlgoEncryption algo) {
        // Create a user for testing
        AbstractLogin login = new UserLogin();
        login.setUserName(usernameValid);
        login.setPassword(algo.encrypt(passwordValid));
        dao.addLogin(login);
    }
}