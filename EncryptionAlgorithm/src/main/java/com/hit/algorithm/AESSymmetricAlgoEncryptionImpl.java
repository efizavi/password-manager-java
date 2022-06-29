package com.hit.algorithm;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESSymmetricAlgoEncryptionImpl extends AbstractSymmetricAlgoEncryption{

    @Override
    public String getAlgorithmName() {
        return "AES";
    }

    @Override
    public String getCipherName() { return "AES/CBC/PKCS5PADDING";}

    @Override
    public Integer getKeyLength() {
        return 256;
    }

    public AESSymmetricAlgoEncryptionImpl() {
        try { setSecretKey(createKey()); }
        catch (NoSuchAlgorithmException ex)
        {
            System.out.println("Failed to generate new secret key - encryption algorithm was not recognized." +
                    " Please call setSecretKey manually to be able to use this object instance");
        }

        setInitializationVector();
    }

    public AESSymmetricAlgoEncryptionImpl(SecretKey key) {
        setSecretKey(key);
        setInitializationVector();
    }
}
