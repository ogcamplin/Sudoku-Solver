package test.java.com.sudoku;

import org.junit.Test;

import com.sudoku.model.Puzzle;
import com.sudoku.solver.SolvedCallback;
import com.sudoku.solver.SudokuSolver;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SolveTests {
    @Test
    public void ValidSimplePuzzlesSolveTest() throws Exception {
        File[] files = getResourceFolderFiles("simple");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidEasyPuzzlesSolveTest() throws Exception {
        File[] files = getResourceFolderFiles("easy");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidIntermediatePuzzlesSolveTest() throws Exception {
        File[] files = getResourceFolderFiles("intermediate");
        checkSolveFromFiles(files);
    }

    @Test
    public void ValidExpertPuzzlesSolveTest() throws Exception {
        File[] files = getResourceFolderFiles("expert");
        checkSolveFromFiles(files);
    }

    private void checkSolveFromFiles(File[] files) throws Exception {
        for(File f : files) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            Puzzle toSolve = Puzzle.fromString(br.readLine());
            String solution = br.readLine();
    
            new SudokuSolver(toSolve, new SolvedCallback() {
                @Override
                public void onSolved(Puzzle solvedPuzzle) {
                    assertEquals(solution, solvedPuzzle.toResultString());
                }
            });
        }
    }

    private File[] getResourceFolderFiles(String folderPath) {
        File folder = new File(getClass().getResource("../../../resources/" + folderPath).getPath());
        return folder.listFiles();
    }
}
