package com.battleship;

// This class represents the Board layout, which players will have access too when they place ships and begin to attack
// It will know the location of ships and can convey when a hit is taken on a ship.

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size = 10; // Standard Battleship board size
    private List<Ship> ships = new ArrayList<>(); // All ships placed on this board
    private char[][] displayGrid = new char[size][size]; // What gets printed: ' ', 'O', 'X'

    public Board() {
        // Initialize the display grid with empty spaces
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                displayGrid[r][c] = ' ';
            }
        }
    }

    // Try placing a ship on the board; only succeeds if all parts are valid
    public boolean placeShip(Ship ship) {
        for (Coordinate c : ship.getCoordinates()) {
            if (!isInBounds(c) || isOccupied(c)) return false;
        }
        ships.add(ship);
        return true;
    }

    // Check if a coordinate is inside the board
    public boolean isInBounds(Coordinate c) {
        return c.row >= 0 && c.row < size && c.col >= 0 && c.col < size;
    }

    // Check if a coordinate is already occupied by another ship
    public boolean isOccupied(Coordinate c) {
        return ships.stream().anyMatch(ship -> ship.occupies(c));
    }

    // Process an attack on a cell, and update display accordingly
    public String attack(Coordinate c) {
        for (Ship ship : ships) {
            if (ship.occupies(c)) {
                ship.registerHit(c);
                displayGrid[c.row][c.col] = 'X'; // Hit
                return ship.isSunk() ? "SUNK" : "HIT";
            }
        }
        displayGrid[c.row][c.col] = 'O'; // Miss
        return "MISS";
    }

    // Returns true if all ships have been sunk
    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    // Print the board to console. If showShips is true, ship locations are shown as 'S'
    public void printBoard(boolean showShips) {
        System.out.println("  A B C D E F G H I J");
        for (int r = 0; r < size; r++) {
            System.out.print((r + 1) + (r + 1 < 10 ? " " : "")); // Add spacing for single-digit rows
            for (int c = 0; c < size; c++) {
                char ch = displayGrid[r][c];
                // If it's an unmarked space and we want to show ships, show 'S'
                if (ch == ' ' && showShips && isOccupied(new Coordinate(r, c))) {
                    System.out.print("S ");
                } else {
                    System.out.print(ch + " ");
                }
            }
            System.out.println();
        }
    }
}