package com.battleship;

// This class Game represents the main Local Game that the users will be interacting with. 
// This will hold the main CLI loop for player interactions locally, without using sockets.

import java.util.*;

public class LocalGame {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int BOARD_SIZE = 10;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Battleship CLI ===");

        // Initialize each player's board
        Board player1Board = new Board();
        Board player2Board = new Board();

        // Keep track of attack history to prevent duplicates
        Set<Coordinate> player1Attacks = new HashSet<>();
        Set<Coordinate> player2Attacks = new HashSet<>();

        // Place ships randomly for both players
        System.out.println("Placing ships for Player 1...");
        placeShips(player1Board);

        System.out.println("Placing ships for Player 2...");
        placeShips(player2Board);

        // Main game loop
        boolean gameOver = false;
        boolean player1Turn = true;

        while (!gameOver) {
            Board attackerBoard = player1Turn ? player1Board : player2Board;
            Board defenderBoard = player1Turn ? player2Board : player1Board;
            Set<Coordinate> attackHistory = player1Turn ? player1Attacks : player2Attacks;
            String currentPlayer = player1Turn ? "Player 1" : "Player 2";

            System.out.println("\n" + currentPlayer + "'s turn");

            // Optionally show the player's own board with ships
            System.out.print("Would you like to view your ships before attacking? (y/n): ");
            String viewOwn = scanner.nextLine().trim().toLowerCase();
            if (viewOwn.equals("y")) {
                System.out.println(currentPlayer + " — Your board (ships shown):");
                attackerBoard.printBoard(true);
            }

            // Show opponent's board (only known hits/misses)
            System.out.println("Opponent's board (hits/misses only):");
            defenderBoard.printBoard(false);

            // Prompt for attack, ensure no duplicates
            Coordinate target;
            while (true) {
                target = getInputCoordinate();
                if (attackHistory.contains(target)) {
                    System.out.println("You've already attacked that coordinate. Try again.");
                } else {
                    attackHistory.add(target);
                    break;
                }
            }

            // Process attack and display result
            String result = defenderBoard.attack(target);
            System.out.println("Result: " + result);

            // Check for victory
            if (defenderBoard.allShipsSunk()) {
                System.out.println(currentPlayer + " wins!");
                gameOver = true;
            } else {
                player1Turn = !player1Turn; // Switch turn
            }
        }
    }

    /**
     * Randomly places a set of ships on the board.
     * Ships follow the classic sizes: 5, 4, 3, 3, 2
     */
    private static void placeShips(Board board) {
        int[] shipSizes = {5, 4, 3, 3, 2}; // Carrier, Battleship, Cruiser, Submarine, Destroyer

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = new Random().nextInt(BOARD_SIZE);
                int col = new Random().nextInt(BOARD_SIZE);
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

    /**
     * Converts a string input (e.g. "C5") into a Coordinate object.
     * Handles invalid input safely.
     */
    private static Coordinate getInputCoordinate() {
        while (true) {
            System.out.print("Enter target (e.g., B4): ");
            String input = scanner.nextLine().toUpperCase();

            if (input.length() < 2 || input.length() > 3)
                continue;

            char colChar = input.charAt(0);
            int row;

            try {
                row = Integer.parseInt(input.substring(1)) - 1;
            } catch (NumberFormatException e) {
                continue;
            }

            int col = colChar - 'A';
            if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                return new Coordinate(row, col);
            }

            System.out.println("Invalid coordinate. Try again.");
        }
    }
}