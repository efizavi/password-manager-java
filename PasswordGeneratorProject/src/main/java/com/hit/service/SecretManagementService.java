package com.hit.service;

import com.hit.algorithm.AESSymmetricAlgoEncryptionImpl;
import com.hit.algorithm.AbstractSymmetricAlgoEncryption;
import com.hit.algorithm.IAlgoEncryption;
import com.hit.dao.IDao;
import com.hit.dm.*;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Month;
import java.util.ArrayList;
import java.util.Base64;

public class SecretManagementService extends AbstractServiceBase {
    public SecretManagementService(IDao dao) {
        super(dao);
    }

    private static final byte[] salt = new byte[]{(byte) 187, (byte) 196, (byte) 71, (byte) 179,
            (byte) 161, (byte) 181, (byte) 36, (byte) 237};

    private void setNonEncryptedFields(AbstractSecretDTO source, AbstractSecretEncrypted target) {
        target.setDomain(source.getDomain());
        target.setSecretName(source.getSecretName());
        target.setSecretCategory(source.getSecretCategory());
        target.setRemark(source.getRemark());
        target.setOwnerUserName(source.getOwnerUserName());
    }

    private void setNonEncryptedFields(AbstractSecretEncrypted source, AbstractSecretDTO target) {
        target.setDomain(source.getDomain());
        target.setSecretName(source.getSecretName());
        target.setSecretCategory(source.getSecretCategory());
        target.setRemark(source.getRemark());
        target.setOwnerUserName(source.getOwnerUserName());
    }

    private CardSecretEncrypted encryptCardSecret(CardSecret secret, String password) {
        CardSecretEncrypted encryptedSecret = new CardSecretEncrypted();
        setNonEncryptedFields(secret, encryptedSecret);

        IAlgoEncryption algo = prepareAlgo(password);
        encryptedSecret.setCardOwnerName(algo.encrypt(String.valueOf(secret.getCardOwnerName())));
        encryptedSecret.setCardNumber(algo.encrypt(String.valueOf(secret.getCardNumber())));
        encryptedSecret.setExpirationDay(algo.encrypt(String.valueOf(secret.getExpirationDay())));
        encryptedSecret.setExpirationMonth(algo.encrypt(String.valueOf(secret.getExpirationMonth())));
        encryptedSecret.setExpirationYear(algo.encrypt(String.valueOf(secret.getExpirationYear())));
        encryptedSecret.setCardType(algo.encrypt(String.valueOf(secret.getCardType())));
        encryptedSecret.setCvv(algo.encrypt(String.valueOf(secret.getCvv())));
        return encryptedSecret;
    }

    private CardSecret decryptCardSecret(CardSecretEncrypted encryptedSecret, String password) {
        CardSecret secret = new CardSecret();
        setNonEncryptedFields(encryptedSecret, secret);

        IAlgoEncryption algo = prepareAlgo(password);
        secret.setCardOwnerName(algo.decrypt(encryptedSecret.getCardOwnerName()));
        secret.setCardNumber(Long.parseLong(algo.decrypt(encryptedSecret.getCardNumber())));
        secret.setExpirationDay(Byte.parseByte(algo.decrypt(encryptedSecret.getExpirationDay())));
        secret.setExpirationMonth(Month.valueOf(algo.decrypt(encryptedSecret.getExpirationMonth())));
        secret.setExpirationYear(Short.parseShort(algo.decrypt(encryptedSecret.getExpirationYear())));
        secret.setCardType(CardType.valueOf(algo.decrypt(encryptedSecret.getCardType())));
        secret.setCvv(Short.parseShort(algo.decrypt(encryptedSecret.getCvv())));
        return secret;
    }

    private IdentitySecretEncrypted encryptIdentitySecret(IdentitySecret secret, String password) {
        IdentitySecretEncrypted encryptedSecret = new IdentitySecretEncrypted();
        setNonEncryptedFields(secret, encryptedSecret);

        IAlgoEncryption algo = prepareAlgo(password);
        encryptedSecret.setFirstName(algo.encrypt(String.valueOf(secret.getFirstName())));
        encryptedSecret.setMiddleName(algo.encrypt(String.valueOf(secret.getMiddleName())));
        encryptedSecret.setLastName(algo.encrypt(String.valueOf(secret.getLastName())));
        encryptedSecret.setIdentityType(algo.encrypt(String.valueOf(secret.getIdentityType())));
        encryptedSecret.setCompany(algo.encrypt(String.valueOf(secret.getCompany())));
        encryptedSecret.setLicenseNumber(algo.encrypt(String.valueOf(secret.getLicenseNumber())));
        encryptedSecret.setPassportNumber(algo.encrypt(String.valueOf(secret.getPassportNumber())));
        encryptedSecret.setEmail(algo.encrypt(String.valueOf(secret.getEmail())));
        encryptedSecret.setPhoneNumber(algo.encrypt(String.valueOf(secret.getPhoneNumber())));
        encryptedSecret.setAddress(algo.encrypt(String.valueOf(secret.getAddress())));
        encryptedSecret.setPostalCode(algo.encrypt(String.valueOf(secret.getPostalCode())));
        return encryptedSecret;
    }

    private IdentitySecret decryptIdentitySecret(IdentitySecretEncrypted encryptedSecret, String password) {
        IdentitySecret secret = new IdentitySecret();
        setNonEncryptedFields(encryptedSecret, secret);

        IAlgoEncryption algo = prepareAlgo(password);
        secret.setFirstName(algo.decrypt(encryptedSecret.getFirstName()));
        secret.setMiddleName(algo.decrypt(encryptedSecret.getMiddleName()));
        secret.setLastName(algo.decrypt(encryptedSecret.getLastName()));
        secret.setIdentityType(IdentityType.valueOf(algo.decrypt(encryptedSecret.getIdentityType())));
        secret.setCompany(algo.decrypt(encryptedSecret.getCompany()));
        secret.setLicenseNumber(Long.parseLong(algo.decrypt(encryptedSecret.getLicenseNumber())));
        secret.setPassportNumber(Long.parseLong(algo.decrypt(encryptedSecret.getPassportNumber())));
        secret.setEmail(algo.decrypt(encryptedSecret.getEmail()));
        secret.setPhoneNumber(algo.decrypt(encryptedSecret.getPhoneNumber()));
        secret.setAddress(algo.decrypt(encryptedSecret.getAddress()));
        secret.setPostalCode(Integer.parseInt(algo.decrypt(encryptedSecret.getPostalCode())));
        return secret;
    }

    private LoginSecretEncrypted encryptLoginSecret(LoginSecret secret, String password) {
        LoginSecretEncrypted encryptedSecret = new LoginSecretEncrypted();
        setNonEncryptedFields(secret, encryptedSecret);

        IAlgoEncryption algo = prepareAlgo(password);
        encryptedSecret.setUserName(algo.encrypt(String.valueOf(secret.getUserName())));
        encryptedSecret.setPassword(algo.encrypt(String.valueOf(secret.getPassword())));
        return encryptedSecret;
    }

    private LoginSecret decryptLoginSecret(LoginSecretEncrypted encryptedSecret, String password) {
        LoginSecret secret = new LoginSecret();
        setNonEncryptedFields(encryptedSecret, secret);

        IAlgoEncryption algo = prepareAlgo(password);
        secret.setUserName(algo.decrypt(encryptedSecret.getUserName()));
        secret.setPassword(algo.decrypt(encryptedSecret.getPassword()));
        return secret;
    }

    private TextSecretEncrypted encryptTextSecret(TextSecret secret, String password) {
        TextSecretEncrypted encryptedSecret = new TextSecretEncrypted();
        setNonEncryptedFields(secret, encryptedSecret);
        IAlgoEncryption algo = prepareAlgo(password);
        encryptedSecret.setContent(algo.encrypt(String.valueOf(secret.getContent())));
        return encryptedSecret;
    }

    private TextSecret decryptTextSecret(TextSecretEncrypted encryptedSecret, String password) {
        TextSecret secret = new TextSecret();
        setNonEncryptedFields(encryptedSecret, secret);
        IAlgoEncryption algo = prepareAlgo(password);
        secret.setContent(algo.decrypt(encryptedSecret.getContent()));
        return secret;
    }

    public void addCardSecret(CardSecret secret, String username, String password) {
        // Add encrypted secret to data layer
        dao.addSecret(username, encryptCardSecret(secret, password));
    }

    public void addIdentitySecret(IdentitySecret secret, String username, String password) {
        dao.addSecret(username, encryptIdentitySecret(secret, password));
    }

    public void addLoginSecret(LoginSecret secret, String username, String password) {
        // Add encrypted secret to data layer
        dao.addSecret(username, encryptLoginSecret(secret, password));
    }

    public void addTextSecret(TextSecret secret, String username, String password) {
        // Add encrypted secret to data layer
        dao.addSecret(username, encryptTextSecret(secret, password));
    }

    public void updateCardSecret(CardSecret secret, String username, String originalSecretName, String password) {
        dao.updateSecret(username, originalSecretName, encryptCardSecret(secret, password));
    }

    public void updateIdentitySecret(IdentitySecret secret, String username, String originalSecretName, String password) {
        dao.updateSecret(username, originalSecretName, encryptIdentitySecret(secret, password));
    }

    public void updateLoginSecret(LoginSecret secret, String username, String originalSecretName, String password) {
        dao.updateSecret(username, originalSecretName, encryptLoginSecret(secret, password));
    }

    public void updateTextSecret(TextSecret secret, String username, String originalSecretName, String password) {
        dao.updateSecret(username, originalSecretName, encryptTextSecret(secret, password));
    }

    public ArrayList<AbstractSecretDTO> getSecrets(String username, String password) {
        ArrayList<AbstractSecretDTO> decryptedSecrets = new ArrayList<>();
        for (AbstractSecretEncrypted secret : dao.getSecrets(username)) {
            if (secret.type.equals(CardSecretEncrypted.class.getName())) {
                decryptedSecrets.add(decryptCardSecret((CardSecretEncrypted) secret, password));
            } else if (secret.type.equals(IdentitySecretEncrypted.class.getName())) {
                decryptedSecrets.add(decryptIdentitySecret((IdentitySecretEncrypted) secret, password));
            } else if (secret.type.equals(LoginSecretEncrypted.class.getName())) {
                decryptedSecrets.add(decryptLoginSecret((LoginSecretEncrypted) secret, password));
            } else if (secret.type.equals(TextSecretEncrypted.class.getName())) {
                decryptedSecrets.add(decryptTextSecret((TextSecretEncrypted) secret, password));
            } else {
                System.out.println("Error! Unrecognized encrypted secret type parsed from DAO: " + secret.type);
            }
        }
        return decryptedSecrets;
    }

    public boolean removeSecret(String username, String secretName) {
        return dao.removeSecret(username, secretName);
    }

    private AESSymmetricAlgoEncryptionImpl prepareAlgo(String password) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey secKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return new AESSymmetricAlgoEncryptionImpl(secKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.out.println("Error! Failed to initialize AES secret key. Key will be random " + ex.getMessage());
        }
        return new AESSymmetricAlgoEncryptionImpl();
    }
}
