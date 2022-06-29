package com.hit.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.algorithm.AbstractSymmetricAlgoEncryption;
import com.hit.dao.IDao;
import com.hit.dm.*;
import com.hit.server.Response;
import com.hit.server.ResponseType;
import com.hit.service.SecretManagementService;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class SecretController {
    SecretManagementService srv;
    private final Gson gson;

    public SecretController(IDao dao) {
        srv = new SecretManagementService(dao);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    private void setCommonFields(HashMap<String, String> data, AbstractSecretDTO target) {
        target.setDomain(data.get("domain"));
        target.setSecretName(data.get("secretname"));
        target.setSecretCategory(SecretCategory.valueOf(data.get("category")));
        target.setRemark(data.get("remark"));
        target.setOwnerUserName(data.get("ownerusername"));
    }

    private CardSecret prepareCardSecret(HashMap<String, String> data) {
        CardSecret secret = new CardSecret();
        setCommonFields(data, secret);
        secret.setCardOwnerName(data.get("ownername"));
        secret.setCardNumber(Long.parseLong(data.get("number")));
        secret.setExpirationDay(Byte.parseByte(data.get("day")));
        secret.setExpirationMonth(Month.valueOf(data.get("month")));
        secret.setExpirationYear(Short.parseShort(data.get("year")));
        secret.setCardType(CardType.valueOf(data.get("cardtype")));
        secret.setCvv(Short.parseShort(data.get("cvv")));
        return secret;
    }

    private IdentitySecret prepareIdentitySecret(HashMap<String, String> data) {
        IdentitySecret secret = new IdentitySecret();
        setCommonFields(data, secret);
        secret.setFirstName(data.get("firstname"));
        secret.setMiddleName(data.get("middlename"));
        secret.setLastName(data.get("lastname"));
        secret.setIdentityType(IdentityType.valueOf(data.get("identitytype")));
        secret.setCompany(data.get("company"));
        secret.setLicenseNumber(Long.parseLong(data.get("license")));
        secret.setPassportNumber(Long.parseLong(data.get("passport")));
        secret.setEmail(data.get("email"));
        secret.setPhoneNumber(data.get("phone"));
        secret.setAddress(data.get("address"));
        secret.setPostalCode(Integer.parseInt(data.get("postal")));
        return secret;
    }

    private LoginSecret prepareLoginSecret(HashMap<String, String> data) {
        LoginSecret secret = new LoginSecret();
        setCommonFields(data, secret);
        secret.setUserName(data.get("secretusername"));
        secret.setPassword(data.get("secretpassword"));
        return secret;
    }

    private TextSecret prepareTextSecret(HashMap<String, String> data) {
        TextSecret secret = new TextSecret();
        setCommonFields(data, secret);
        secret.setContent(data.get("content"));
        return secret;
    }

    public Response addCardSecret(HashMap<String, String> data) {
        srv.addCardSecret(prepareCardSecret(data), data.get("username"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response updateCardSecret(HashMap<String, String> data) {
        srv.updateCardSecret(prepareCardSecret(data), data.get("username"), data.get("originalname"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response addIdentitySecret(HashMap<String, String> data) {
        srv.addIdentitySecret(prepareIdentitySecret(data), data.get("username"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response updateIdentitySecret(HashMap<String, String> data) {
        srv.updateIdentitySecret(prepareIdentitySecret(data), data.get("username"), data.get("originalname"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response addLoginSecret(HashMap<String, String> data) {
        srv.addLoginSecret(prepareLoginSecret(data), data.get("username"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response updateLoginSecret(HashMap<String, String> data) {
        srv.updateLoginSecret(prepareLoginSecret(data), data.get("username"), data.get("originalname"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response addTextSecret(HashMap<String, String> data) {
        srv.addTextSecret(prepareTextSecret(data), data.get("username"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response updateTextSecret(HashMap<String, String> data) {
        srv.updateTextSecret(prepareTextSecret(data), data.get("username"), data.get("originalname"), data.get("password"));
        return prepareBoolResponse();
    }

    public Response getSecrets(HashMap<String, String> data) {
        Response resp = new Response();
        resp.setAction(ResponseType.SecretResult);
        ArrayList<AbstractSecretDTO> secrets = srv.getSecrets(data.get("username"), data.get("password"));
        resp.setData(gson.toJson(secrets));
        return  resp;
    }

    public Response removeSecret(HashMap<String, String> data) {
        Response resp = new Response();
        resp.setAction(ResponseType.SecretResult);
        boolean result = srv.removeSecret(data.get("username"), data.get("secretname"));
        resp.setData(String.valueOf(result));
        return resp;
    }

    private Response prepareBoolResponse() {
        Response resp = new Response();
        resp.setAction(ResponseType.BooleanResult);
        resp.setData(String.valueOf(true));
        return resp;
    }
}
