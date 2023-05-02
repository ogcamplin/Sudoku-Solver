package com.sudoku.model;

public class InvalidPuzzleException extends Exception {
    public InvalidPuzzleException() {
    }

    public InvalidPuzzleException(String message) {
        super(message);
    }
}
