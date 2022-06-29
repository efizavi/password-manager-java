package com.hit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.AbstractSecretDTO;
import com.hit.dm.CardType;
import com.hit.dm.IdentityType;
import com.hit.dm.SecretCategory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SecretModel {
    private final Socket clientSocket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Gson gson;
    private final Type secretListType = new TypeToken<ArrayList<AbstractSecretDTO>>(){}.getType();

    public SecretModel(String ip, int port) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Configure GSON to deserialize abstract classes to inherited type acc. to "type" attribute (see adapter impl.)
        gsonBuilder.registerTypeAdapter(AbstractSecretDTO.class, new JSONAbstractAdapter<AbstractSecretDTO>());

        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        clientSocket = new Socket(ip, port);
        output =  new ObjectOutputStream(clientSocket.getOutputStream());
        input =  new ObjectInputStream(clientSocket.getInputStream());
    }

    public ArrayList<AbstractSecretDTO> sendGetSecretRequest(String username, String password)
    {
        // Create request
        System.out.println("Creating request to get all secrets for "+username);
        Request request = new Request();
        request.setAction(RequestType.GetSecrets);
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
            String secretsJson = response.getData();
            if (secretsJson.equals(""))
                return new ArrayList<AbstractSecretDTO>();
            return gson.fromJson(secretsJson, secretListType);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client socket IO operation failed - " + e.getMessage());
        }
        return new ArrayList<AbstractSecretDTO>();
    }

    public boolean sendCreateOrUpdateCardSecret(String username, String domain, String secretName,
                                                SecretCategory category, String remark, String owner, long cardNumber,
                                                byte cardExpDay, Month cardExpMonth, short cardExpYear,
                                                CardType cardType, short cvv, String originalSecretName, String password) {

        // Create request
        System.out.println("Creating request to update or create card secret...");
        Request request = new Request();
        request.setAction(originalSecretName == null ? RequestType.AddCardSecret : RequestType.UpdateCardSecret);

        // Build request data
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("domain", domain);
        requestData.put("secretname", secretName);
        requestData.put("category", category.name());
        requestData.put("remark", remark);
        requestData.put("ownerusername", username);
        requestData.put("ownername", owner);
        requestData.put("number", String.valueOf(cardNumber));
        requestData.put("day", String.valueOf(cardExpDay));
        requestData.put("month", cardExpMonth.name());
        requestData.put("year", String.valueOf(cardExpYear));
        requestData.put("cardtype", cardType.name());
        requestData.put("cvv", String.valueOf(cvv));
        requestData.put("password", password);

        if (originalSecretName != null) {
            requestData.put("originalname", originalSecretName);
        }

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
        return  false;
    }

    public boolean sendCreateOrUpdateIdentitySecret(String username, String domain, String secretName, SecretCategory category,
                                                    String remark, String firstName, String middleName, String lastName,
                                                    IdentityType type, String company, long license, long passport, String email,
                                                    String phone, String address, int postal, String originalSecretName, String password) {

        // Create request
        System.out.println("Creating request to update or create card identity...");
        Request request = new Request();
        request.setAction(originalSecretName == null ? RequestType.AddIdentitySecret : RequestType.UpdateIdentitySecret);

        // Build request data
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("domain", domain);
        requestData.put("secretname", secretName);
        requestData.put("category", category.name());
        requestData.put("remark", remark);
        requestData.put("ownerusername", username);
        requestData.put("firstname", firstName);
        requestData.put("middlename", middleName);
        requestData.put("lastname", lastName);
        requestData.put("identitytype", type.name());
        requestData.put("company", company);
        requestData.put("license", String.valueOf(license));
        requestData.put("passport", String.valueOf(passport));
        requestData.put("email", email);
        requestData.put("phone", phone);
        requestData.put("address", address);
        requestData.put("postal", String.valueOf(postal));
        requestData.put("password", password);

        if (originalSecretName != null) {
            requestData.put("originalname", originalSecretName);
        }

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
        return  false;
    }

    public boolean sendCreateOrUpdateLoginSecret(String username, String domain, String secretName,
                                                 SecretCategory category, String remark, String secretUsername,
                                                 String secretPassword, String originalSecretName, String password) {
        // Create request
        System.out.println("Creating request to update or create login secret...");
        Request request = new Request();
        request.setAction(originalSecretName == null ? RequestType.AddLoginSecret : RequestType.UpdateLoginSecret);

        // Build request data
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("domain", domain);
        requestData.put("secretname", secretName);
        requestData.put("category", category.name());
        requestData.put("remark", remark);
        requestData.put("ownerusername", username);
        requestData.put("secretusername", secretUsername);
        requestData.put("secretpassword", secretPassword);
        requestData.put("password", password);

        if (originalSecretName != null) {
            requestData.put("originalname", originalSecretName);
        }

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
        return  false;
    }

    public boolean sendCreateOrUpdateTextSecret(String username, String domain, String secretName,
                                                 SecretCategory category, String remark, String content,
                                                 String originalSecretName, String password) {

        // Create request
        System.out.println("Creating request to update or create text secret...");
        Request request = new Request();
        request.setAction(originalSecretName == null ? RequestType.AddTextSecret : RequestType.UpdateTextSecret);

        // Build request data
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("domain", domain);
        requestData.put("secretname", secretName);
        requestData.put("category", category.name());
        requestData.put("remark", remark);
        requestData.put("ownerusername", username);
        requestData.put("content", content);
        requestData.put("password", password);

        if (originalSecretName != null) {
            requestData.put("originalname", originalSecretName);
        }

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
        return  false;
    }

    public boolean sendRemoveSecret(String username, String secretName) {
        // Create request
        System.out.println("Creating request to remove secret "+secretName);
        Request request = new Request();
        request.setAction(RequestType.RemoveSecret);

        // Build request data
        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("secretname", secretName);

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
        return  false;
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
