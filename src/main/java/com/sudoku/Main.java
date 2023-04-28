package com.sudoku;

import java.io.IOException;

import com.sudoku.solver.Board;
import com.sudoku.solver.SolvedCallback;
import com.sudoku.solver.SudokuSolver;

class Main {
    public static void main(String[] args) {
        try {
            new SudokuSolver(Board.fromFile("sample.sudo"), new SolvedAction());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SolvedAction implements SolvedCallback {
        @Override
        public void onSolved(Board solvedBoard) {
            System.out.println("Solution:");
            System.out.println(solvedBoard.toString());
        }
    }
}

