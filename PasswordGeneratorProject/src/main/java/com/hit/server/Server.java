package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static boolean serverUp;
    private ServerSocket serverSocket;

    static {
        Server.serverUp = true;
    }

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("An error occurred while setting up server socket - " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Server started running!");
        try {
            while (serverUp) {
                System.out.println("Waiting for requests...");
                final Socket client = serverSocket.accept();
                System.out.println("Handling request...");
                new Thread(new HandleRequest(client)).start();
            }
            System.out.println("Server is closing...");
            serverSocket.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred while accepting a socket message - " + e.getMessage());
        }
    }
}
