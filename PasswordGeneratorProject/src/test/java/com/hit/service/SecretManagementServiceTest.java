package com.hit.service;

import com.hit.algorithm.AESSymmetricAlgoEncryptionImpl;
import com.hit.algorithm.AbstractAsymmetricAlgoEncryption;
import com.hit.algorithm.AbstractSymmetricAlgoEncryption;
import com.hit.algorithm.RSAAsymmetricAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.*;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Month;
import java.util.ArrayList;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SecretManagementServiceTest {

    private static final String username = "testingUser";
    private static final String password = "testingPw";
    private static final String name = "Test secret name";
    private static final SecretCategory category = SecretCategory.Finance;
    private static final String domain = "Test secret domain";
    private static final String remark = "Test secret remark";
    private static final byte[] salt = new byte[] {(byte)187, (byte)196, (byte)71, (byte)179,
            (byte)161, (byte)181,  (byte)36, (byte)237 };

    @BeforeAll
    static void beforeAll() {
        // Create test user for testing secrets
        IDao dao = new DaoFileImpl();
        AbstractAsymmetricAlgoEncryption rsa = new RSAAsymmetricAlgoCacheImpl();
        AbstractLogin login = new UserLogin();
        login.setUserName(username);
        login.setPassword(rsa.encrypt(password));
        dao.addLogin(login);
    }

    @AfterAll
    static void afterAll() {
        // Remove test user
        IDao dao = new DaoFileImpl();
        dao.removeLogin(username);
    }

    private void setCommonSecretFields(AbstractSecretDTO secret) {
        secret.setSecretName(name);
        secret.setSecretCategory(category);
        secret.setDomain(domain);
        secret.setRemark(remark);
        secret.setOwnerUserName(username);
    }

    private boolean validateCommonSecretFields(AbstractSecretDTO secret) {
        return secret.getSecretName().equals(name) &&
                secret.getSecretCategory() == category &&
                secret.getDomain().equals(domain) &&
                secret.getRemark().equals(remark) &&
                secret.getOwnerUserName().equals(username);
    }


    @Test
    void addCardSecret() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();

        SecretKey secKey = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            secKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assertions.fail("Failed to generate AES secret key");
        }

        AbstractSymmetricAlgoEncryption aes = new AESSymmetricAlgoEncryptionImpl(secKey);
        SecretManagementService srv = new SecretManagementService(dao);

        // Declare fields for card secret
        long cardNumber = 1234123412341234L;
        CardType cardType = CardType.MasterCard;
        String cardOwner = "Efi Zavilevich";
        short cardCvv = 123;
        byte cardExpDay = 31;
        Month cardExpMonth = Month.APRIL;
        short cardExpYear = 2025;

        // Create card secret, unencrypted
        CardSecret secret = new CardSecret();
        setCommonSecretFields(secret);
        secret.setCardNumber(cardNumber);
        secret.setCardType(cardType);
        secret.setCardOwnerName(cardOwner);
        secret.setCvv(cardCvv);
        secret.setExpirationDay(cardExpDay);
        secret.setExpirationMonth(cardExpMonth);
        secret.setExpirationYear(cardExpYear);

        // Call service to encrypt and save secret
        srv.addCardSecret(secret, username, password);

        // Get last saved secret and cast to encrypted secret
        ArrayList<AbstractSecretEncrypted> secrets =  dao.getSecrets(username);
        CardSecretEncrypted encryptedSecret = (CardSecretEncrypted) secrets.remove(secrets.size()-1);

        // Check common non-encrypted secret fields
        Assertions.assertEquals(validateCommonSecretFields(encryptedSecret), true);

        // Decrypt and verify card data
        Assertions.assertEquals(Long.parseLong(aes.decrypt(encryptedSecret.getCardNumber())), cardNumber);
        Assertions.assertEquals(CardType.valueOf(aes.decrypt(encryptedSecret.getCardType())), cardType);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getCardOwnerName()), cardOwner);
        Assertions.assertEquals(Short.parseShort(aes.decrypt(encryptedSecret.getCvv())), cardCvv);
        Assertions.assertEquals(Byte.parseByte(aes.decrypt(encryptedSecret.getExpirationDay())), cardExpDay);
        Assertions.assertEquals(Month.valueOf(aes.decrypt(encryptedSecret.getExpirationMonth())), cardExpMonth);
        Assertions.assertEquals(Short.parseShort(aes.decrypt(encryptedSecret.getExpirationYear())), cardExpYear);
    }

    @Test
    void addIdentitySecret() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();

        SecretKey secKey = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            secKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assertions.fail("Failed to generate AES secret key");
        }

        AbstractSymmetricAlgoEncryption aes = new AESSymmetricAlgoEncryptionImpl(secKey);
        SecretManagementService srv = new SecretManagementService(dao);

        // Declare fields for identity secret
        String firstName = "Noa";
        String middleName = "Sarah";
        String lastName = "Lavon";
        IdentityType type = IdentityType.Ms;
        String company = "Impax Ltd.";
        long license = 112233445566778899L;
        long passport = 998877665544332211L;
        String email = "notarealemail@gmail.com";
        String phoneNumber = "+972123456789";
        String address = "Streety Street 1, Dimona, Israel";
        int postalCode = 4999999;

        // Create identity secret, unencrypted
        IdentitySecret secret = new IdentitySecret();
        setCommonSecretFields(secret);
        secret.setFirstName(firstName);
        secret.setMiddleName(middleName);
        secret.setLastName(lastName);
        secret.setIdentityType(type);
        secret.setCompany(company);
        secret.setLicenseNumber(license);
        secret.setPassportNumber(passport);
        secret.setEmail(email);
        secret.setPhoneNumber(phoneNumber);
        secret.setAddress(address);
        secret.setPostalCode(postalCode);

        // Call service to encrypt and save secret
        srv.addIdentitySecret(secret, username, password);

        // Get last saved secret and cast to encrypted secret
        ArrayList<AbstractSecretEncrypted> secrets =  dao.getSecrets(username);
        IdentitySecretEncrypted encryptedSecret = (IdentitySecretEncrypted) secrets.remove(secrets.size()-1);

        // Check common non-encrypted secret fields
        Assertions.assertEquals(validateCommonSecretFields(encryptedSecret), true);

        // Decrypt and verify identity data
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getFirstName()), firstName);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getMiddleName()), middleName);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getLastName()), lastName);
        Assertions.assertEquals(IdentityType.valueOf(aes.decrypt(encryptedSecret.getIdentityType())), type);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getCompany()), company);
        Assertions.assertEquals(Long.parseLong(aes.decrypt(encryptedSecret.getLicenseNumber())), license);
        Assertions.assertEquals(Long.parseLong(aes.decrypt(encryptedSecret.getPassportNumber())), passport);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getEmail()), email);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getPhoneNumber()), phoneNumber);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getAddress()), address);
        Assertions.assertEquals(Integer.parseInt(aes.decrypt(encryptedSecret.getPostalCode())), postalCode);
    }

    @Test
    void addLoginSecret(){
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();

        SecretKey secKey = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            secKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assertions.fail("Failed to generate AES secret key");
        }
        AbstractSymmetricAlgoEncryption aes = new AESSymmetricAlgoEncryptionImpl(secKey);
        SecretManagementService srv = new SecretManagementService(dao);

        // Declare fields for identity secret
        String secretUsername = "macaronieater123";
        String secretPassword = "iactuallyhatepasta";

        // Create login secret, unencrypted
        LoginSecret secret = new LoginSecret();
        setCommonSecretFields(secret);
        secret.setUserName(secretUsername);
        secret.setPassword(secretPassword);

        // Call service to encrypt and save secret
        srv.addLoginSecret(secret, username, password);

        // Get last saved secret and cast to encrypted secret
        ArrayList<AbstractSecretEncrypted> secrets =  dao.getSecrets(username);
        LoginSecretEncrypted encryptedSecret = (LoginSecretEncrypted) secrets.remove(secrets.size()-1);

        // Check common non-encrypted secret fields
        Assertions.assertEquals(validateCommonSecretFields(encryptedSecret), true);

        // Decrypt and verify identity data
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getUserName()), secretUsername);
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getPassword()), secretPassword);
    }

    @Test
    void addTextSecret() {
        // Initialize encryption algorithm, DAO and service
        IDao dao = new DaoFileImpl();

        SecretKey secKey = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            secKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assertions.fail("Failed to generate AES secret key");
        }
        AbstractSymmetricAlgoEncryption aes = new AESSymmetricAlgoEncryptionImpl(secKey);
        SecretManagementService srv = new SecretManagementService(dao);

        // Declare fields for identity secret
        String secretText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In id massa vitae tortor aliquet bibendum. Nulla ut ante dolor. Aliquam eleifend pulvinar turpis, eget posuere diam ultricies vitae. Sed et libero sed nisi scelerisque pellentesque in nec sapien. Integer egestas nec risus vitae sollicitudin. Vestibulum condimentum accumsan gravida. Suspendisse sed neque sapien. Duis eget est ut quam tempor euismod. Phasellus ut accumsan est. Nam accumsan lectus at sapien consequat, non condimentum augue ultricies. Fusce sed ex hendrerit urna sollicitudin sollicitudin et ut ante. Aenean eget orci at nibh cursus vehicula eu non sem.";

        // Create text secret, unencrypted
        TextSecret secret = new TextSecret();
        setCommonSecretFields(secret);
        secret.setContent(secretText);

        // Call service to encrypt and save secret
        srv.addTextSecret(secret, username, password);

        // Get last saved secret and cast to encrypted secret
        ArrayList<AbstractSecretEncrypted> secrets =  dao.getSecrets(username);
        TextSecretEncrypted encryptedSecret = (TextSecretEncrypted) secrets.remove(secrets.size()-1);

        // Check common non-encrypted secret fields
        Assertions.assertEquals(validateCommonSecretFields(encryptedSecret), true);

        // Decrypt and verify identity data
        Assertions.assertEquals(aes.decrypt(encryptedSecret.getContent()), secretText);
    }

    private byte[] hexStringToByteArray(String s) {

        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;

    }
}