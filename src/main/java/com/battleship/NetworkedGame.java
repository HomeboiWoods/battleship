package com.battleship;

// Like Game.java, this class holds the logic for the battleship game. Its inputs are now dependent on socket rather than scanner, since we 
// will be taking TCP packets as inputs. Based on StartGame, the user will either be a host or client, which both have different roles.

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkedGame {
    public enum Role { HOST, CLIENT }

    private final Role role;
    private final Socket socket;
    private final Board myBoard;
    private final Board opponentBoard;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Scanner scanner;
    private final Set<Coordinate> attackHistory = new HashSet<>();

    public NetworkedGame(Role role, Socket socket) throws IOException {
        this.role = role;
        this.socket = socket;
        this.myBoard = new Board();
        this.opponentBoard = new Board(); // Used just for tracking hits/misses
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Setting up your board...");
        placeShips(myBoard);

        System.out.println("======================================");
        System.out.println("BATTLESHIP STARTED");
        System.out.println("Type coordinates to attack (e.g., B4)");
        System.out.println("Type EXIT at any time to disconnect");
        System.out.println("======================================\n");

        boolean myTurn = (role == Role.HOST); // Host goes first
        boolean gameOver = false;

        while (!gameOver) {
            if (myTurn) {
                // === YOUR TURN TO ATTACK ===
                System.out.println("\nYour Board:");
                myBoard.printBoard(true);

                System.out.println("\nOpponent Board (known hits/misses):");
                opponentBoard.printBoard(false);

                Coordinate target = getInputCoordinate();

                // Send attack
                out.println(serializeCoordinate(target));

                // Receive result
                try {
                    String result = in.readLine();
                    if (result == null || result.equalsIgnoreCase("DISCONNECT")) {
                        System.out.println("Opponent disconnected.");
                        closeConnection();
                        break;
                    }

                    System.out.println("Result: " + result);

                    // Update opponent board
                    if (result.equals("HIT") || result.equals("SUNK")) {
                        opponentBoard.attack(target);
                    } else if (result.equals("MISS")) {
                        opponentBoard.attack(target);
                    }

                    if (result.equals("WIN")) {
                        System.out.println("You win!");
                        gameOver = true;
                        closeConnection();
                        continue;
                    }

                } catch (IOException e) {
                    System.out.println("Connection lost.");
                    closeConnection();
                    break;
                }
            } else {
                // === OPPONENT'S TURN ===
                System.out.println("\nWaiting for opponent's move...");

                try {
                    String move = in.readLine();
                    if (move == null || move.equalsIgnoreCase("DISCONNECT")) {
                        System.out.println("Opponent disconnected.");
                        closeConnection();
                        break;
                    }

                    Coordinate coord = deserializeCoordinate(move);
                    String result = myBoard.attack(coord);

                    System.out.println("Opponent attacked " + move + ": " + result);

                    // Send result back
                    if (myBoard.allShipsSunk()) {
                        out.println("WIN");
                        System.out.println("You lost! All ships sunk.");
                        gameOver = true;
                        closeConnection();
                        continue;
                    } else {
                        out.println(result);
                    }

                } catch (IOException e) {
                    System.out.println("Connection lost.");
                    closeConnection();
                    break;
                }
            }

            // Switch turns
            myTurn = !myTurn;
        }
    }

    /** Randomly place standard ships on the board */
    private void placeShips(Board board) {
        int[] shipSizes = {5, 4, 3, 3, 2}; // Carrier, Battleship, Cruiser, Submarine, Destroyer

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = new Random().nextInt(10);
                int col = new Random().nextInt(10);
                boolean horizontal = new Random().nextBoolean();

                List<Coordinate> coords = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    int r = row + (horizontal ? 0 : i);
                    int c = col + (horizontal ? i : 0);
                    coords.add(new Coordinate(r, c));
                }

                Ship ship = new Ship(coords);
                placed = board.placeShip(ship);
            }
        }
    }

    /** Convert a Coordinate to a string like "B4" */
    private String serializeCoordinate(Coordinate c) {
        return (char) ('A' + c.col) + Integer.toString(c.row + 1);
    }

    /** Convert a string like "C5" to a Coordinate object */
    private Coordinate deserializeCoordinate(String s) {
        int col = s.charAt(0) - 'A';
        int row = Integer.parseInt(s.substring(1)) - 1;
        return new Coordinate(row, col);
    }

    /** Prompt player to enter attack coordinate, and ensure it's not a repeat */
    private Coordinate getInputCoordinate() {
        while (true) {
            System.out.print("Enter attack (e.g., B4) or type EXIT: ");
            String input = scanner.nextLine().toUpperCase();

            // Allow manual exit
            if (input.equalsIgnoreCase("EXIT")) {
                out.println("DISCONNECT");
                closeConnection();
                System.out.println("You exited the game.");
                System.exit(0);
            }

            if (input.length() < 2 || input.length() > 3) continue;

            char colChar = input.charAt(0);
            int row;

            try {
                row = Integer.parseInt(input.substring(1)) - 1;
            } catch (NumberFormatException e) {
                continue;
            }

            int col = colChar - 'A';
            if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                Coordinate c = new Coordinate(row, col);
                if (attackHistory.contains(c)) {
                    System.out.println("You already attacked that coordinate. Try again.");
                } else {
                    attackHistory.add(c); // Track new valid attack
                    return c;
                }
            } else {
                System.out.println("Invalid coordinate. Try again.");
            }
        }
    }

    /** Clean up resources and close socket gracefully */
    private void closeConnection() {
        try {
            System.out.println("Closing connection...");
            in.close();
            out.close();
            socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.out.println("Error while closing connection: " + e.getMessage());
        }
    }
}