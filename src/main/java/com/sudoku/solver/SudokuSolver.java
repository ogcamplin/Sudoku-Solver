package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sudoku.model.Puzzle;

public class SudokuSolver {
    Puzzle puzzleToSolve;
    Puzzle solvedPuzzle;
    
    public SudokuSolver(Puzzle puzzleToSolve, SolvedCallback solvedCallback) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();

            System.out.println("Puzzle to solve:");
            System.out.println(puzzleToSolve.toString());
            
            executorService.submit(new SolverWorker(puzzleToSolve.copy(), puzzle -> {
                this.solvedPuzzle = puzzle;
                solvedCallback.onSolved(puzzle);
                executorService.shutdown();
            }, executorService));

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
