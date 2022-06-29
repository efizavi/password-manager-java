package com.hit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class LoginModel {
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Gson gson;

    public LoginModel(String ip, int port) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        clientSocket = new Socket(ip, port);
        output =  new ObjectOutputStream(clientSocket.getOutputStream());
        input =  new ObjectInputStream(clientSocket.getInputStream());
    }

    public boolean sendRequest(String username, String password, RequestType type)
    {
        // Create request
        System.out.println("Creating login request of type "+type.name());
        Request request = new Request();
        request.setAction(type);
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        request.setData(requestData);
        String uuid = UUID.randomUUID().toString();
        request.setUUID(uuid);
        String requestString = gson.toJson(request);

        // Send request
        System.out.println("Sending request...");
        try {
            output.writeObject(requestString);

            System.out.println("Waiting for response...");
            String respString = (String)input.readObject();
            Response response;
            do {
                response = gson.fromJson(respString, Response.class);
            }
            while (!response.getUUID().equals(uuid));
            System.out.println("Response met");
            return Boolean.parseBoolean(response.getData());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client socket IO operation failed - " + e.getMessage());
        }
        return false;
    }

    public void closeConnection() {
        // Close I/O streams & socket
        try {
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close client socket " + e.getMessage());
        }
    }
}
