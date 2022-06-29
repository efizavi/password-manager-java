package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.controller.ControllerFactory;
import com.hit.dao.JSONAbstractAdapter;
import com.hit.dm.AbstractLogin;
import com.hit.dm.AbstractSecretEncrypted;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandleRequest implements Runnable {
    private Socket client;
    private Gson gson;

    public HandleRequest(final Socket socket) {
        client = socket;
        // Init serialization helper (GSON)
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gson = gsonBuilder.create();
    }

    @Override
    public void run() {
        try {
            // Read JSON request from socket input stream
            final ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            final ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            final String requestJson = (String)input.readObject();

            // Convert JSON to Request
            Request request = gson.fromJson(requestJson, Request.class);

            // Forward request and reply
            Response response = ControllerFactory.ForwardRequest(request);
            response.setUUID(request.getUUID());
            String responseString = gson.toJson(response);
            output.writeObject(responseString);
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while reading server request - " + e.getMessage());
        }
    }
}
