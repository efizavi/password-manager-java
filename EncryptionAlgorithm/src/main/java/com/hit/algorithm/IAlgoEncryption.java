package com.hit.algorithm;

public interface IAlgoEncryption {
    byte[] encrypt(String plainText);
    String decrypt(byte[] cipher);
    String getAlgorithmName();
    String getCipherName();
    Integer getKeyLength();
}
