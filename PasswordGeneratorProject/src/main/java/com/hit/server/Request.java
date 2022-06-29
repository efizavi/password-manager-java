package com.hit.server;

import java.util.HashMap;

public class Request {

    private RequestType action;

    public RequestType getAction() {
        return action;
    }

    public void setAction(RequestType action) {
        this.action = action;
    }

    private HashMap<String,String> data;

    public HashMap<String,String> getData() {
        return data;
    }

    public void setData(HashMap<String,String> data) {
        this.data = data;
    }

    private String uuid;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public Request() { }
}
