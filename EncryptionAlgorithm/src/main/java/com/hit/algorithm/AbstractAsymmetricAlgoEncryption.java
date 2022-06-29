package com.hit.algorithm;

import javax.crypto.*;
import java.security.*;

public abstract class AbstractAsymmetricAlgoEncryption implements IAlgoEncryption {
    KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(getAlgorithmName());
        keyPairGen.initialize(getKeyLength(), secureRandom);

        return keyPairGen.generateKeyPair();
    }

    private KeyPair keyPair;
    protected KeyPair getKeyPair()
    {
        return keyPair;
    }
    protected  void setKeyPair(KeyPair keyPair)
    {
        this.keyPair = keyPair;
    }

    private byte[] encryptOrDecrypt(boolean isEncrypt, byte[] content) {
        // Build error message prefix according to selected action
        String errorPrefix = "Failed to ";
        errorPrefix += isEncrypt ? "encrypt plaintext - " : "decrypt cipher - ";

        // Create a cipher
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

        try {
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                    isEncrypt ? getKeyPair().getPrivate() : getKeyPair().getPublic());
        } catch (InvalidKeyException e) {
            System.out.println(errorPrefix + "class holds an invalid secret key");
            return null;
        }

        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            System.out.println(errorPrefix + "encryption was attempted with an invalid block size");
            return null;
        } catch (BadPaddingException e) {
            System.out.println(errorPrefix + "encryption was attempted with an invalid padding");
            return null;
        }
    }

    @Override
    public byte[] encrypt(String plainText) {
        if (plainText == null)
            return  null;
        return encryptOrDecrypt(true, plainText.getBytes());
    }

    @Override
    public String decrypt(byte[] cipher) {
        if (cipher == null)
            return  null;
        return new String(encryptOrDecrypt(false, cipher));
    }

}
