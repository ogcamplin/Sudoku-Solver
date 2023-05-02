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
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SolveTests {
    /* ---------- Valid ---------- */

    @Test
    public void ValidSimplePuzzlesSolveTest() throws Exception {
        File[] files = getSudokuFilesFromFolder("simple");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidEasyPuzzlesSolveTest() throws Exception {
        File[] files = getSudokuFilesFromFolder("easy");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidIntermediatePuzzlesSolveTest() throws Exception {
        File[] files = getSudokuFilesFromFolder("intermediate");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidExpertPuzzlesSolveTest() throws Exception {
        File[] files = getSudokuFilesFromFolder("expert");
        checkSolveFromFiles(files);
    }
    
    /* ---------- Invalid ---------- */

    @Test
    public void InvalidPuzzleEmptyTest() throws Exception {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/empty.sudo"));
        CountDownLatch callbackLatch = new CountDownLatch(1);
        
        SolutionCallbackTestWrapper cb = new SolutionCallbackTestWrapper(callbackLatch);
        new SudokuSolver(toSolve, cb);

        callbackLatch.await();
        assertTrue(cb.isFailureCalled.get());
        assertFalse(cb.isSuccessCalled.get());
        assertEquals("not enough givens / multiple solutions: (0 givens)", cb.exceptionRef.get().getMessage());
    }

    @Test
    public void InvalidPuzzleSingleGivenTest() throws Exception {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/single-given.sudo"));
        CountDownLatch callbackLatch = new CountDownLatch(1);
        
        SolutionCallbackTestWrapper cb = new SolutionCallbackTestWrapper(callbackLatch);
        new SudokuSolver(toSolve, cb);

        callbackLatch.await();
        assertTrue(cb.isFailureCalled.get());
        assertFalse(cb.isSuccessCalled.get());
        assertEquals("not enough givens / multiple solutions: (1 givens)", cb.exceptionRef.get().getMessage());
    }

    @Test
    public void InvalidPuzzleInsufficentGivenTest() throws Exception {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/insufficient-given.sudo"));
        CountDownLatch callbackLatch = new CountDownLatch(1);
        
        SolutionCallbackTestWrapper cb = new SolutionCallbackTestWrapper(callbackLatch);
        new SudokuSolver(toSolve, cb);

        callbackLatch.await();
        assertTrue(cb.isFailureCalled.get());
        assertFalse(cb.isSuccessCalled.get());
        assertEquals("not enough givens / multiple solutions: (16 givens)", cb.exceptionRef.get().getMessage());
    }
    
    /* ---------- Helpers ---------- */

    private Puzzle parseFileToPuzzle(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return Puzzle.fromString(br.readLine());
    }

    private void checkSolveFromFiles(File[] files) throws IOException, InterruptedException {
        for(File f : files) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            Puzzle toSolve = Puzzle.fromString(br.readLine());
            String solution = br.readLine();

            CountDownLatch callbackLatch = new CountDownLatch(1);
            SolutionCallbackTestWrapper cb = new SolutionCallbackTestWrapper(callbackLatch);
    
            new SudokuSolver(toSolve, cb);

            callbackLatch.await();

            assertFalse(cb.isFailureCalled.get());
            assertTrue(cb.isSuccessCalled.get());
            assertEquals(solution, cb.puzzleRef.get().toResultString());
        }
    }

    private File getSudokuFile(String filePath) {
        return getResoucePath().resolve(filePath).toFile();
    }

    private File[] getSudokuFilesFromFolder(String folderPath) {
        File folder = getResoucePath().resolve(folderPath).toFile();
        return folder.listFiles();
    }

    private Path getResoucePath() {
        return Path.of(getClass().getResource("../../resources/").getPath());
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
