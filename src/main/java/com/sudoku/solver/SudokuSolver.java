package com.sudoku.solver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sudoku.model.Puzzle;

public class SudokuSolver {
    Puzzle puzzleToSolve;
    
    public SudokuSolver(Puzzle puzzleToSolve, SolutionCallback solutionCallback) {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            System.out.println("Puzzle to solve:");
            System.out.println(puzzleToSolve.toString());
            SolutionCallbackSolverWrapper cb = new SolutionCallbackSolverWrapper(executorService, solutionCallback);

            Future<Boolean> solvedFuture = executorService.submit(new SolverWorker(puzzleToSolve.copy(), cb, executorService));

            Boolean solvedResult = solvedFuture.get();

            if(!solvedResult) {
                cb.onFailure(new SolutionException("Unsolvable Puzzle"));
            } else {
                executorService.shutdown();
            }
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
            this.executorService.shutdown();
            this.solutionCallback.onSuccess(solvedPuzzle);
        }

        @Override
        public void onFailure(SolutionException exception) {
            this.executorService.shutdown();
            this.solutionCallback.onFailure(exception);
        }
    }
}
