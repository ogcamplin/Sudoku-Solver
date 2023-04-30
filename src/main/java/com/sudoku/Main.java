package com.sudoku;

import java.io.IOException;

import com.sudoku.model.Puzzle;
import com.sudoku.solver.SolutionCallback;
import com.sudoku.solver.SolutionException;
import com.sudoku.solver.SudokuSolver;

class Main {
    public static void main(String[] args) {
        try {
            new SudokuSolver(Puzzle.fromFile("sample.sudo"), new SolvedAction());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SolvedAction implements SolutionCallback {
        @Override
        public void onSuccess(Puzzle solvedBoard) {
            System.out.println("Solution:");
            System.out.println(solvedBoard.toString());
        }

        @Override
        public void onFailure(SolutionException exception) {
            try {
                throw exception;
            } catch(SolutionException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

