package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SudokuSolver {
    Board boardToSolve;
    Board solvedBoard;
    
    public SudokuSolver() {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();

            this.boardToSolve = Board.fromFile("sample.sudo");
            System.out.println("Puzzle to solve:");
            System.out.println(boardToSolve.toString());
            
            executorService.submit(new SolverWorker(boardToSolve.clone(), board -> {
                this.solvedBoard = board;
                System.out.println("Solution:");
                System.out.println(solvedBoard.toString());
                executorService.shutdown();
            }, executorService));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
