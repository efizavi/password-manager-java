package com.hit.algorithm;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class RSAAsymmetricAlgoCacheImpl extends AbstractAsymmetricAlgoEncryption{

    @Override
    public String getAlgorithmName() { return "RSA"; }

    @Override
    public String getCipherName() { return getAlgorithmName(); }

    @Override
    public Integer getKeyLength() { return 2048; }

    public RSAAsymmetricAlgoCacheImpl(KeyPair keyPair) {
        setKeyPair(keyPair);
    }

    public RSAAsymmetricAlgoCacheImpl() {
        try {
            setKeyPair(generateKeyPair());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Failed to generate new keypair - encryption algorithm was not recognized." +
                    " Please call setKeyPair manually to be able to use this object instance");
        }
    }
}
