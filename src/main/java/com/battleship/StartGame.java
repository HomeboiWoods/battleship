package com.battleship;

// This class represents the first place we go when starting up the BattleShip game. When starting, a user defines if they want to host or 
// be a client, which will then send them to the correct respective NetworkedGame board.

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class StartGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        NetworkedGame.Role role = null;

        try {
            // Ask user: Host or Client?
            // Adding something for gui branch
            System.out.print("Are you hosting or connecting? (h/c): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("h")) {
                role = NetworkedGame.Role.HOST;

                // Prompt for port number to host on
                System.out.print("Enter port to host on (e.g., 5000 for localhost): ");
                int port = Integer.parseInt(scanner.nextLine());

                // Set up a server socket and wait for incoming connection
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Waiting for a connection on port " + port + "...");
                socket = serverSocket.accept(); // Blocking until client connects
                System.out.println("Client connected from " + socket.getInetAddress());

            } else if (choice.equals("c")) {
                role = NetworkedGame.Role.CLIENT;

                // Prompt for host IP and port
                System.out.print("Enter host IP address (e.g., 127.0.0.1): ");
                String ip = scanner.nextLine();
                System.out.print("Enter port to connect to (e.g., 5000): ");
                int port = Integer.parseInt(scanner.nextLine());

                // Connect to the host
                socket = new Socket(ip, port);
                System.out.println("Connected to host at " + ip + ":" + port);
            } else {
                System.out.println("Invalid choice. Exiting.");
                return;
            }

            // Launch the game using the shared game class
            NetworkedGame game = new NetworkedGame(role, socket);
            game.start();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}