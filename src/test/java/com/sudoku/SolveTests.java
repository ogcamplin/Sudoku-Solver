package com.sudoku;

import org.junit.Test;

import com.sudoku.model.Puzzle;
import com.sudoku.solver.SolutionException;
import com.sudoku.solver.SolutionCallback;
import com.sudoku.solver.SudokuSolver;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SolveTests extends SudokuTestBase {
    @Test
    public void Should_Solve_When_SolveableSimplePuzzles() throws Exception {
        File[] files = getSudokuFilesFromFolder("simple");
        checkSolveFromFiles(files);
    }

    @Test
    public void Should_Solve_When_SolveableEasyPuzzles() throws Exception {
        File[] files = getSudokuFilesFromFolder("easy");
        checkSolveFromFiles(files);
    }

    @Test
    public void Should_Solve_When_SolveableIntermediatePuzzles() throws Exception {
        File[] files = getSudokuFilesFromFolder("intermediate");
        checkSolveFromFiles(files);
    }

    @Test
    public void Should_Solve_When_SolveableExpertPuzzles() throws Exception {
        File[] files = getSudokuFilesFromFolder("expert");
        checkSolveFromFiles(files);
    }
    
    /* ---------- Helpers ---------- */

    private void checkSolveFromFiles(File[] files) throws Exception {
        for(File f : files) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            Puzzle toSolve = Puzzle.fromString(br.readLine());
            String solution = br.readLine();

            CountDownLatch callbackLatch = new CountDownLatch(1);
            SolutionCallbackTestWrapper cb = new SolutionCallbackTestWrapper(callbackLatch);
    
            new SudokuSolver(toSolve, cb);

            callbackLatch.await();
            SolutionException ex = cb.exceptionRef.get();
            assertFalse(ex != null? ex.getMessage() : null, cb.isFailureCalled.get());
            assertTrue(cb.isSuccessCalled.get());
            assertEquals(solution, cb.puzzleRef.get().toResultString());
            br.close();
        }
    }

    class SolutionCallbackTestWrapper implements SolutionCallback {
        private CountDownLatch callbackLatch;
        AtomicReference<SolutionException> exceptionRef = new AtomicReference<>(null);
        AtomicReference<Puzzle> puzzleRef = new AtomicReference<>(null);
        AtomicBoolean isFailureCalled = new AtomicBoolean(false);
        AtomicBoolean isSuccessCalled = new AtomicBoolean(false);

        public SolutionCallbackTestWrapper(CountDownLatch callbackLatch) {
            this.callbackLatch = callbackLatch;
        }

        @Override
        public void onSuccess(Puzzle solvedPuzzle) {
            isSuccessCalled.set(true);
            puzzleRef.set(solvedPuzzle);
            callbackLatch.countDown();
        }

        @Override
        public void onFailure(SolutionException exception) {
            isFailureCalled.set(true);
            exceptionRef.set(exception);
            callbackLatch.countDown();
        }
    }
}
