package com.hit.controller;

import com.hit.algorithm.AESSymmetricAlgoEncryptionImpl;
import com.hit.algorithm.AbstractAsymmetricAlgoEncryption;
import com.hit.algorithm.AbstractSymmetricAlgoEncryption;
import com.hit.algorithm.RSAAsymmetricAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.server.Request;
import com.hit.server.Response;
import com.hit.server.ResponseType;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ControllerFactory {


    private static LoginController loginController;
    private static SecretController secretController;

    static {
        IDao dao = new DaoFileImpl();

        AbstractAsymmetricAlgoEncryption rsa;

        // Read ASym KeyPair from files, necessary to keep consistent encryption after server restart.
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Files.readAllBytes(Paths.get("src\\main\\resources\\privatekey.der"));
            PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey prvKey =  kf.generatePrivate(prvSpec);
            keyBytes = Files.readAllBytes(Paths.get("src\\main\\resources\\publickey.der"));
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(keyBytes);
            PublicKey pubKey = kf.generatePublic(pubSpec);
            KeyPair kp = new KeyPair(pubKey, prvKey);
            rsa = new RSAAsymmetricAlgoCacheImpl(kp);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Failed to read RSA KeyPair from files! resorting to using SecureRandom for encryption");
            rsa = new RSAAsymmetricAlgoCacheImpl();
        }

        loginController = new LoginController(dao, rsa);
        secretController = new SecretController(dao);
    }


    // This method is marked "synchronized" for thread safety!
    public static synchronized Response ForwardRequest(Request request)
    {
        switch (request.getAction()) {
            case Register:
                return loginController.register(request.getData());
            case Unregister:
                return loginController.unregister(request.getData());
            case Authenticate:
                return loginController.authenticate(request.getData());
            case ChangePassword:
                return loginController.changePassword(request.getData());
            case AddCardSecret:
                return secretController.addCardSecret(request.getData());
            case AddTextSecret:
                return secretController.addTextSecret(request.getData());
            case AddLoginSecret:
                return secretController.addLoginSecret(request.getData());
            case AddIdentitySecret:
                return secretController.addIdentitySecret(request.getData());
            case GetSecrets:
                return secretController.getSecrets(request.getData());
            case UpdateCardSecret:
                return secretController.updateCardSecret(request.getData());
            case UpdateIdentitySecret:
                return secretController.updateIdentitySecret(request.getData());
            case UpdateLoginSecret:
                return secretController.updateLoginSecret(request.getData());
            case UpdateTextSecret:
                return secretController.updateTextSecret(request.getData());
            case RemoveSecret:
                    return secretController.removeSecret(request.getData());
            default:
                System.out.println("Unrecognized action attempted: "+request.getAction().toString());
                Response invalidResponse = new Response();
                invalidResponse.setAction(ResponseType.BooleanResult);
                invalidResponse.setData(String.valueOf(false));
                return invalidResponse;
        }

    }

}
