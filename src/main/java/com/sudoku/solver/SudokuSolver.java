package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sudoku.model.Puzzle;

public class SudokuSolver {
    Puzzle puzzleToSolve;
    
    public SudokuSolver(Puzzle puzzleToSolve, SolutionCallback solvedCallback) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();

            System.out.println("Puzzle to solve:");
            System.out.println(puzzleToSolve.toString());
            
            executorService.submit(new SolverWorker(puzzleToSolve.copy(), solvedCallback, executorService));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
