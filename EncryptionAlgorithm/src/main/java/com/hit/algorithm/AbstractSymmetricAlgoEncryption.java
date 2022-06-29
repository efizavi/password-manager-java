package com.hit.algorithm;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class AbstractSymmetricAlgoEncryption implements IAlgoEncryption {
    SecretKey createKey() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGen = KeyGenerator.getInstance(getAlgorithmName());
        keyGen.init(getKeyLength(), secureRandom);

        return keyGen.generateKey();
    }

    private static final int SEED_SIZE = 16;
    private byte[] initializationVector;
    private SecretKey secretKey;

    protected SecretKey getSecretKey()
    {
        return secretKey;
    }
    protected  void setSecretKey(SecretKey key)
    {
        this.secretKey = key;
    }

    protected void setInitializationVector()
    {
        // Generate random seed for algorithm to avoid repetition with other instances
        initializationVector = new byte[SEED_SIZE];
        //SecureRandom secureRandom = new SecureRandom();
        //secureRandom.nextBytes(initializationVector);

        // Needed for consistent encryption results:
        initializationVector = new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    }

    private byte[] encryptOrDecrypt(boolean isEncrypt, byte[] content) {
        // Build error message prefix according to selected action
        String errorPrefix = "Failed to ";
        errorPrefix += isEncrypt ? "encrypt plaintext - " : "decrypt cipher - ";

        // Create an AES cipher
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(getCipherName());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(errorPrefix + "cipher was not recognized");
            return null;
        } catch (NoSuchPaddingException e) {
            System.out.println(errorPrefix + "cipher padding was not recognized");
            return null;
        }

        // Set current seed as cipher parameters
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);

        // Initialize cipher with seed and secret key
        try {
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                    getSecretKey(), ivParameterSpec);
        } catch (InvalidKeyException e) {
            System.out.println(errorPrefix + "class holds an invalid secret key");
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println(errorPrefix + "class holds an invalid algorithm parameter");
        }

        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            System.out.println(errorPrefix + "encryption was attempted with an invalid block size");
            return null;
        } catch (BadPaddingException e) {
            System.out.println(errorPrefix + "encryption was attempted with an invalid padding - "+e.getMessage());
            return null;
        }
    }

    @Override
    public byte[] encrypt(String plainText) {
        if (plainText == null)
            return  null;
        return encryptOrDecrypt(true, plainText.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decrypt(byte[] cipher) {
        if (cipher == null)
            return null;
        return new String(encryptOrDecrypt(false, cipher));
    }
}
