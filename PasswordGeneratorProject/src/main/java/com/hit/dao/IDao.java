package com.hit.dao;

import com.hit.dm.AbstractLogin;
import com.hit.dm.AbstractSecretEncrypted;

import java.util.ArrayList;
public interface IDao {
    ArrayList<AbstractLogin> getLogins();
    void saveLogins(ArrayList<AbstractLogin> logins);
    void addLogin(AbstractLogin login);
    void updateLogin(String originalUserName, AbstractLogin login);
    void removeLogin(String userName);

    ArrayList<AbstractSecretEncrypted> getSecrets(String userName);
    void addSecret(String userName, AbstractSecretEncrypted secret);
    boolean removeSecret(String userName, String secretName);
    void updateSecret(String userName, String originalSecretName, AbstractSecretEncrypted secret);
}
