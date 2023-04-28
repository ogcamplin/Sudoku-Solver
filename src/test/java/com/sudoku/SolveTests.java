package test.java.com.sudoku;

import org.junit.Test;

import com.sudoku.model.Puzzle;
import com.sudoku.solver.SolvedCallback;
import com.sudoku.solver.SudokuSolver;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

public class SolveTests {

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
    
    private Puzzle parseFileToPuzzle(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return Puzzle.fromString(br.readLine());
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
}
