package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SudokuSolver {
    Board boardToSolve;
    Board solvedBoard;
    
    public SudokuSolver(Board boardToSolve, SolvedCallback solvedCallback) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();

            System.out.println("Puzzle to solve:");
            System.out.println(boardToSolve.toString());
            
            executorService.submit(new SolverWorker(boardToSolve.clone(), board -> {
                this.solvedBoard = board;
                solvedCallback.onSolved(board);
                executorService.shutdown();
            }, executorService));

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
