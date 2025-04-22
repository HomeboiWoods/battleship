package com.battleship;

// This class Ship represents the Ship objects players will create. It will know the position (coordinates) of ships
// and will know its size, orentation, and track hits.

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private List<Coordinate> coordinates; // List of all cells this ship occupies
    private List<Boolean> hits;           // Parallel list of booleans for hit status

    public Ship(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
        this.hits = new ArrayList<>();
        // Initially, no parts of the ship are hit
        for (int i = 0; i < coordinates.size(); i++) {
            hits.add(false);
        }
    }

    // Returns true if the ship occupies the given coordinate
    public boolean occupies(Coordinate c) {
        return coordinates.contains(c);
    }

    // Register a hit if the coordinate matches one of the ship's parts
    public boolean registerHit(Coordinate c) {
        for (int i = 0; i < coordinates.size(); i++) {
            if (coordinates.get(i).equals(c)) {
                hits.set(i, true); // Mark the corresponding part as hit
                return true;
            }
        }
        return false;
    }

    // Returns true if all parts of the ship are hit
    public boolean isSunk() {
        return hits.stream().allMatch(hit -> hit);
    }

    // Expose the list of coordinates this ship occupies (used during placement)
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
}