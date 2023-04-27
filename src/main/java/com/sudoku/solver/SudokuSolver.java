package com.sudoku.solver;


public class SudokuSolver {
    Board boardToSolve;
    
    public SudokuSolver() {
        try {
            this.boardToSolve = Board.fromFile("sample.sudo");
            System.out.println(boardToSolve.toString());

            new SolverWorker(boardToSolve.getGrid()).run();;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
