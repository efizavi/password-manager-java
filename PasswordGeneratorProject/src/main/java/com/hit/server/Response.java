package com.hit.server;

public class Response {
    private ResponseType action;

    public ResponseType getAction() {
        return action;
    }

    public void setAction(ResponseType action) {
        this.action = action;
    }

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String uuid;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
