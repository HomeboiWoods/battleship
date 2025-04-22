package com.battleship;

// This class Host serves as the role of the host for wireless connection. The host will wait for a client to join. Only then will the game begin.

import java.io.*;
import java.net.*;

public class Host {
    public static void main(String[] args) {
        // Define the port the game will be hosted on
        int port = 5000;

        // Now open a new server socket for hosting
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Host is waiting for connection on port " + port + "...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from: " + clientSocket.getInetAddress());

            // Setup streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send welcome message
            out.println("Hello from Host!");

            // Receive response
            String response = in.readLine();
            System.out.println("Client says: " + response);

            // Close connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}