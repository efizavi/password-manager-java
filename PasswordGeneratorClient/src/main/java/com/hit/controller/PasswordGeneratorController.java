package com.hit.controller;

import com.hit.dm.AbstractSecretDTO;
import com.hit.dm.CardType;
import com.hit.dm.IdentityType;
import com.hit.dm.SecretCategory;
import com.hit.model.LoginModel;
import com.hit.model.RequestType;
import com.hit.model.SecretModel;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;

public class PasswordGeneratorController {

    private static final String ip = "127.0.0.1";
    private static final int port = 34567;
    private String password;

    public PasswordGeneratorController() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String username, String password) throws IOException {
        return forwardLoginRequest(username, password, RequestType.Authenticate);
    }

    public boolean register(String username, String password) throws IOException {
        return forwardLoginRequest(username, password, RequestType.Register);
    }

    public boolean unregister(String username) throws  IOException {
        return forwardLoginRequest(username, "", RequestType.Unregister);
    }

    public boolean changePassword(String username, String password) throws  IOException {
        return forwardLoginRequest(username, password, RequestType.ChangePassword);
    }

    public ArrayList<AbstractSecretDTO> getSecrets(String username) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        ArrayList<AbstractSecretDTO> result = model.sendGetSecretRequest(username, password);
        model.closeConnection();
        return result;
    }

    public boolean addCardSecret(String username, String domain, String secretName,
                                 SecretCategory category, String remark, String owner, long cardNumber,
                                 byte cardExpDay, Month cardExpMonth, short cardExpYear,
                                 CardType cardType, short cvv) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateCardSecret(username, domain, secretName, category, remark, owner,
                cardNumber, cardExpDay, cardExpMonth, cardExpYear, cardType, cvv, null, password);
        model.closeConnection();
        return result;
    }

    public boolean updateCardSecret(String username, String domain, String secretName,
                                    SecretCategory category, String remark, String owner, long cardNumber,
                                    byte cardExpDay, Month cardExpMonth, short cardExpYear, CardType cardType, short cvv,
                                    String originalSecretName) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateCardSecret(username, domain, secretName, category, remark, owner,
                cardNumber, cardExpDay, cardExpMonth, cardExpYear, cardType, cvv, originalSecretName, password);
        model.closeConnection();
        return result;
    }

    public boolean addIdentitySecret(String username, String domain, String secretName, SecretCategory category,
                                     String remark, String firstName, String middleName, String lastName, IdentityType type,
                                     String company, long license, long passport, String email, String phone,
                                     String address, int postal) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateIdentitySecret(username, domain, secretName, category, remark,
                firstName, middleName, lastName, type, company, license, passport, email, phone, address, postal,
                null, password);
        model.closeConnection();
        return result;
    }

    public boolean updateIdentitySecret(String username, String domain, String secretName, SecretCategory category,
                                        String remark, String firstName, String middleName, String lastName, IdentityType type,
                                        String company, long license, long passport, String email, String phone,
                                        String address, int postal, String originalSecretName) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateIdentitySecret(username, domain, secretName, category, remark,
                firstName, middleName, lastName, type, company, license, passport, email, phone, address, postal,
                originalSecretName, password);
        model.closeConnection();
        return result;
    }

    public boolean addLoginSecret(String username, String domain, String secretName, SecretCategory category,
                                  String remark, String secretUsername, String secretPassword) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateLoginSecret(username, domain, secretName, category, remark, secretUsername, secretPassword,
                null, password);
        model.closeConnection();
        return result;
    }

    public boolean updateLoginSecret(String username, String domain, String secretName, SecretCategory category,
                                     String remark, String secretUsername, String secretPassword, String originalSecretName)
                                     throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateLoginSecret(username, domain, secretName, category, remark, secretUsername, secretPassword,
                originalSecretName, password);
        model.closeConnection();
        return result;
    }

    public boolean addTextSecret(String username, String domain, String secretName, SecretCategory category,
                                    String remark, String content) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateTextSecret(username, domain, secretName, category, remark, content,
                null, password);
        model.closeConnection();
        return result;
    }

    public boolean updateTextSecret(String username, String domain, String secretName, SecretCategory category,
                                    String remark, String content, String originalSecretName) throws IOException {
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendCreateOrUpdateTextSecret(username, domain, secretName, category, remark, content,
                originalSecretName, password);
        model.closeConnection();
        return result;
    }

    public boolean removeSecret(String username, String secretName) throws IOException{
        SecretModel model = new SecretModel(ip, port);
        boolean result = model.sendRemoveSecret(username, secretName);
        model.closeConnection();
        return result;
    }

    private boolean forwardLoginRequest(String username, String password, RequestType type)
            throws IOException {
        LoginModel model = new LoginModel(ip, port);
        boolean result = model.sendRequest(username, password, type);
        model.closeConnection();
        return result;
    }
}
