package com.sudoku.solver;


public class SudokuSolver {
    Board boardToSolve;
    Board solvedBoard;
    
    public SudokuSolver() {
        try {
            this.boardToSolve = Board.fromFile("sample.sudo");
            System.out.println("Puzzle to solve:");
            System.out.println(boardToSolve.toString());
            
            new Thread(new SolverWorker(boardToSolve.clone(), board -> {
                this.solvedBoard = board;
                System.out.println("Solution:");
                System.out.println(solvedBoard.toString());
            })).start();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
