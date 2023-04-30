package com.sudoku.solver;

import com.sudoku.model.Puzzle;

public interface SolutionCallback {
    void onSuccess(Puzzle solvedPuzzle);
    void onFailure(SolutionException exception);
}