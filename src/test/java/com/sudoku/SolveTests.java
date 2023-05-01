package test.java.com.sudoku;

import org.junit.Assert;
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

public class SolveTests {
    @Test
    public void ValidSimplePuzzlesSolveTest() throws IOException {
        File[] files = getSudokuFilesFromFolder("simple");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidEasyPuzzlesSolveTest() throws IOException {
        File[] files = getSudokuFilesFromFolder("easy");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidIntermediatePuzzlesSolveTest() throws IOException {
        File[] files = getSudokuFilesFromFolder("intermediate");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidExpertPuzzlesSolveTest() throws IOException {
        File[] files = getSudokuFilesFromFolder("expert");
        checkSolveFromFiles(files);
    }
    
    @Test
    public void InvalidPuzzleEmptyTest() throws IOException {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/empty.sudo"));
        new SudokuSolver(toSolve,  new FailSolutionCallback("not enough givens / multiple solutions: (0 givens)"));
    }

    @Test
    public void InvalidPuzzleSingleGivenTest() throws IOException {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/empty.sudo"));
        new SudokuSolver(toSolve,  new FailSolutionCallback("not enough givens / multiple solutions: (1 givens)"));
    }

    @Test
    public void InvalidPuzzleInsufficentGivenTest() throws IOException {
        Puzzle toSolve = parseFileToPuzzle(getSudokuFile("invalid/empty.sudo"));
        new SudokuSolver(toSolve,  new FailSolutionCallback("not enough givens / multiple solutions: (16 givens"));
    }
    
    private Puzzle parseFileToPuzzle(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return Puzzle.fromString(br.readLine());
    }

    private void checkSolveFromFiles(File[] files) throws IOException {
        for(File f : files) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            Puzzle toSolve = Puzzle.fromString(br.readLine());
            String solution = br.readLine();
    
            new SudokuSolver(toSolve, new SolutionCallback() {
                @Override
                public void onSuccess(Puzzle solvedPuzzle) {
                    assertEquals(solution, solvedPuzzle.toResultString());
                }

                public void onFailure(SolutionException ex) {
                    Assert.fail(ex.getMessage());
                }
            });
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
        return Path.of(getClass().getResource("../../../resources/").getPath());
    }

    class FailSolutionCallback implements SolutionCallback {
        private String expectedMessage;

        public FailSolutionCallback(String expectedMessage) {
            this.expectedMessage = expectedMessage;
        }

        @Override
        public void onSuccess(Puzzle solvedPuzzle) {
            Assert.fail();
        }

        @Override
        public void onFailure(SolutionException ex) {
            Assert.assertEquals(expectedMessage, ex.getMessage());
        }
    }
}
