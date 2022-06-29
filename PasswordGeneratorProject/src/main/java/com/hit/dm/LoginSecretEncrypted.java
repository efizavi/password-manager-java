package com.hit.dm;

public class LoginSecretEncrypted extends AbstractSecretEncrypted {

    private byte[] userName;

    public byte[] getUserName() {
        return userName;
    }

    public void setUserName(byte[] userName) {
        this.userName = userName;
    }

    private byte[] password;

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
