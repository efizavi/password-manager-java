package com.hit.dm;

import java.util.ArrayList;

public class UserLogin extends AbstractLogin {

    public UserLogin() {
        this.secretList = new ArrayList<>();
    }

    private ArrayList<AbstractSecretEncrypted> secretList;
    public ArrayList<AbstractSecretEncrypted> getSecretList() {
        return secretList;
    }
    public void setSecretList(ArrayList<AbstractSecretEncrypted> secretList) {
        this.secretList = secretList;
    }

    public void pushSecret(AbstractSecretEncrypted secret) {
        secret.setOwnerUserName(this.getUserName());
        secretList.add(secret);
    }
}
