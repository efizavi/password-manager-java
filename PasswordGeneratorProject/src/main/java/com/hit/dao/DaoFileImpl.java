package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.AbstractLogin;
import com.hit.dm.AbstractSecretEncrypted;
import com.hit.dm.UserLogin;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
public class DaoFileImpl implements IDao{

    private final Gson gson;
    private final Type loginListType = new TypeToken<ArrayList<AbstractLogin>>(){}.getType();
    private final Path filePath = Paths.get("src\\main\\resources\\datasource.txt");

    public DaoFileImpl() {

        // Init serialization helper (GSON)
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Register abstract classes in GSON for serialization with extra member for base classes type
        // This allows us to point to which base class the abstract parent should be casted
        gsonBuilder.registerTypeAdapter(AbstractLogin.class, new JSONAbstractAdapter<AbstractLogin>());
        gsonBuilder.registerTypeAdapter(AbstractSecretEncrypted.class, new JSONAbstractAdapter<AbstractSecretEncrypted>());
        gsonBuilder.setPrettyPrinting();

        gson = gsonBuilder.create();
    }

    @Override
    public ArrayList<AbstractLogin> getLogins() {

        try {
            // Read all lines of datasource file
            String content = "";
            for (String line : Files.readAllLines(filePath)) {
                content += line;
            }

            // If the file is empty return without attempting deserialization
            if (content.equals(""))
                return new ArrayList<AbstractLogin>();

            // Deserialize file content to abstract login list
            return gson.fromJson(content, loginListType);

        } catch (IOException e) {
            System.out.println("IO exception occurred during parse of data file: " + e.getMessage());
        }
        return new ArrayList<AbstractLogin>();
    }

    @Override
    public void saveLogins(ArrayList<AbstractLogin> logins) {

        // Serialize new logins
        String json = gson.toJson(logins);

        // Save serialized login text to file
        try {
            FileWriter writer = new FileWriter(filePath.toFile(), false);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("IO exception occurred during writing of data file: " + e.getMessage());
        }

    }

    @Override
    public void addLogin(AbstractLogin login) {
        ArrayList<AbstractLogin> logins = getLogins();
        logins.add(login);
        saveLogins(logins);
    }

    @Override
    public void updateLogin(String originalUserName, AbstractLogin login) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getUserName().equals(originalUserName)) {
                // Replace login, save and return
                logins.set(i, login);
                saveLogins(logins);
                return;
            }
        }

        System.out.println("Could not find login to update with the name "+originalUserName);
    }

    @Override
    public void removeLogin(String userName) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).getUserName().equals(userName)) {
                // remove login, save and return
                logins.remove(i);
                saveLogins(logins);
                return;
            }
        }

        System.out.println("Could not find login to remove with the name "+userName);
    }

    @Override
    public ArrayList<AbstractSecretEncrypted> getSecrets(String userName) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            AbstractLogin loginIter = logins.get(i);
            // Return secrets of first user which is not admin and matches username
            if (loginIter.getUserName().equals(userName)) {
                if (loginIter instanceof UserLogin) {
                    return ((UserLogin)loginIter).getSecretList();
                }
                else {
                    // Admin users have no secrets
                    System.out.println("Failed to get secrets - username belongs to an admin");
                    return new ArrayList<>();
                }
            }
        }
        System.out.println("Failed to get secrets - Could not find login with the name "+userName);
        return new ArrayList<>();
    }

    @Override
    public void addSecret(String userName, AbstractSecretEncrypted secret) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            AbstractLogin loginIter = logins.get(i);
            // Find matching user
            if (loginIter.getUserName().equals(userName)) {
                if (loginIter instanceof UserLogin) {
                    // Add secret to user's list
                    ArrayList<AbstractSecretEncrypted> secrets = ((UserLogin)loginIter).getSecretList();
                    secrets.add(secret);
                    saveLogins(logins);
                    return;
                }
                else {
                    // Admin users have no secrets
                    System.out.println("Failed to add secret - username belongs to an admin");
                }
            }
        }
        System.out.println("Failed to add secret - Could not find login with the name "+userName);
    }

    @Override
    public boolean removeSecret(String userName, String secretName) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            AbstractLogin loginIter = logins.get(i);
            // Find matching user
            if (loginIter.getUserName().equals(userName)) {
                if (loginIter instanceof UserLogin) {
                    // Find matching secret
                    ArrayList<AbstractSecretEncrypted> secrets = ((UserLogin)loginIter).getSecretList();
                    for (int j = 0; j < secrets.size(); j++) {
                        AbstractSecretEncrypted secretIter = secrets.get(j);
                        if (secretIter.getSecretName().equals(secretName)) {
                            // Remove secret from user's list
                            secrets.remove(j);
                            saveLogins(logins);
                            return true;
                        }
                    }
                    System.out.println("Failed to remove secret - Could not find secret with the name"+secretName);
                }
                else {
                    // Admin users have no secrets
                    System.out.println("Failed to remove secret - username belongs to an admin");
                }
            }
        }
        System.out.println("Failed to remove secret - Could not find login with the name "+userName);
        return false;
    }

    @Override
    public void updateSecret(String userName, String originalSecretName, AbstractSecretEncrypted secret) {
        ArrayList<AbstractLogin> logins = getLogins();

        // Find first login with matching username
        for (int i = 0; i < logins.size(); i++) {
            AbstractLogin loginIter = logins.get(i);
            // Find matching user
            if (loginIter.getUserName().equals(userName)) {
                if (loginIter instanceof UserLogin) {
                    // Find matching secret
                    ArrayList<AbstractSecretEncrypted> secrets = ((UserLogin)loginIter).getSecretList();
                    for (int j = 0; j < secrets.size(); i++) {
                        AbstractSecretEncrypted secretIter = secrets.get(j);
                        if (secretIter.getSecretName().equals(originalSecretName)) {
                            // Replace matching secret
                            secrets.set(j, secret);
                            saveLogins(logins);
                            return;
                        }
                    }
                    System.out.println("Failed to update secret - Could not find secret with the name"+originalSecretName);
                }
                else {
                    // Admin users have no secrets
                    System.out.println("Failed to update secret - username belongs to an admin");
                }
            }
        }
        System.out.println("Failed to update secret - Could not find login with the name "+userName);
    }
}
