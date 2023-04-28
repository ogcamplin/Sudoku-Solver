package com.sudoku.solver;

import com.sudoku.model.Puzzle;

public interface SolvedCallback {
    void onSolved(Puzzle solvedPuzzle);
}