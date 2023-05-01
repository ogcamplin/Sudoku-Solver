package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.sudoku.model.Puzzle;

public class SudokuSolver {
    Puzzle puzzleToSolve;
    
    public SudokuSolver(Puzzle puzzleToSolve, SolutionCallback solutionCallback) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Puzzle to solve:");
            System.out.println(puzzleToSolve.toString());
            
            executorService.submit(new SolverWorker(puzzleToSolve.copy(), new SolutionCallbackSolverWrapper(executorService, solutionCallback), executorService));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    class SolutionCallbackSolverWrapper implements SolutionCallback {
        ExecutorService executorService;
        SolutionCallback solutionCallback;

        public SolutionCallbackSolverWrapper(ExecutorService executorService, SolutionCallback solutionCallback) {
            this.executorService = executorService;
            this.solutionCallback = solutionCallback;
        }

        @Override
        public void onSuccess(Puzzle solvedPuzzle) {
            this.solutionCallback.onSuccess(solvedPuzzle);
            this.executorService.shutdown();
        }

        @Override
        public void onFailure(SolutionException exception) {
            this.solutionCallback.onFailure(exception);
            this.executorService.shutdown();;
        }
    }
}
