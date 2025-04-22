package com.battleship;

// This class Coordinate is just a simple coordinate system that will be used by other classes to manage placements, hits, misses, etc.
// Ships will be placed on this coordinate system that we can define below.

// A simple immutable coordinate (row, col) used for ship placement and attacks
public class Coordinate {
    public final int row;
    public final int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Equality check to compare two coordinates
    // We override the original equals function, to instead use this one for coordinates specifically
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) obj;
        return this.row == other.row && this.col == other.col;
    }

    // Hash function to allow Coordinate to be used in hash-based collections
    @Override
    public int hashCode() {
        return row * 31 + col;
    }

    // String representation like (4,5)
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}