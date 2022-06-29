package com.hit.controller;

import com.hit.algorithm.AbstractAsymmetricAlgoEncryption;
import com.hit.dao.IDao;
import com.hit.server.Response;
import com.hit.server.ResponseType;
import com.hit.service.LoginManagementService;

import java.util.HashMap;

public class LoginController {
    LoginManagementService srv;

    public LoginController(IDao dao, AbstractAsymmetricAlgoEncryption algo) {
        srv = new LoginManagementService(dao, algo);
    }

    public Response authenticate(HashMap<String, String> data) {
        boolean result = srv.authenticate(data.get("username"), data.get("password"));
        return prepareResponse(result);
    }

    public Response register(HashMap<String, String> data) {
        boolean success = true;
        try {
            srv.register(data.get("username"), data.get("password"));
        }
        catch (Exception e) {
            System.out.println("Registration error - " + e.getMessage());
            success = false;
        }
        return prepareResponse(success);
    }

    public Response unregister(HashMap<String, String> data) {
        boolean result = srv.unregister(data.get("username"));
        return prepareResponse(result);
    }

    public Response changePassword(HashMap<String, String> data) {
        boolean result = srv.changePassword(data.get("username"), data.get("password"));
        return prepareResponse(result);
    }

    private Response prepareResponse(boolean result) {
        Response resp = new Response();
        resp.setAction(ResponseType.BooleanResult);
        resp.setData(String.valueOf(result));
        return resp;
    }
}
