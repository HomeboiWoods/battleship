package com.battleship;

// This class Client will connect to the Host and begin playing the game Battleship with them. They will connect through TCP.

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        // define the ip address and port to connect on.
        
        String hostAddress = "localhost"; // Replace with server IP for remote play
        int port = 5000;

        try (Socket socket = new Socket(hostAddress, port)) {
            System.out.println("Connected to host at " + hostAddress + ":" + port);

            // Setup streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read message from host
            String welcome = in.readLine();
            System.out.println("Host says: " + welcome);

            // Send response
            out.println("Hello from Client!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}