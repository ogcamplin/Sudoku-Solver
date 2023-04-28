package com.sudoku;

import java.io.IOException;

import com.sudoku.model.Puzzle;
import com.sudoku.solver.SolvedCallback;
import com.sudoku.solver.SudokuSolver;

class Main {
    public static void main(String[] args) {
        try {
            new SudokuSolver(Puzzle.fromFile("sample.sudo"), new SolvedAction());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SolvedAction implements SolvedCallback {
        @Override
        public void onSolved(Puzzle solvedBoard) {
            System.out.println("Solution:");
            System.out.println(solvedBoard.toString());
        }
    }
}

