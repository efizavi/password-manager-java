package com.hit.service;

import com.hit.algorithm.IAlgoEncryption;
import com.hit.dao.IDao;
import com.hit.dm.AbstractLogin;

import java.util.ArrayList;

public abstract class AbstractServiceBase {
    IDao dao;

    protected AbstractServiceBase(IDao dao) {
        this.dao = dao;
    }

    protected AbstractLogin getLoginWithUsername(String username) {
        // Get all logins
        ArrayList<AbstractLogin> logins = dao.getLogins();

        // Search logins for a username match
        for (AbstractLogin login : logins) {
            if (login.getUserName().equals(username)) {
                // Return matching user
                return login;
            }
        }

        // A matching user was not found - return null
        return null;
    }
}
