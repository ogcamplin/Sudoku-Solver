package com.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

import com.sudoku.model.Puzzle;

public class SudokuTestBase {

    protected File getSudokuFile(String filePath) {
        return getResoucePath().resolve(filePath).toFile();
    }

    protected File[] getSudokuFilesFromFolder(String folderPath) {
        File folder = getResoucePath().resolve(folderPath).toFile();
        return folder.listFiles();
    }

    protected Path getResoucePath() {
        return Path.of(getClass().getResource("../../resources/").getPath());
    }

    protected Puzzle parseFileToPuzzle(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        Puzzle puzzle = Puzzle.fromString(br.readLine());
        br.close();
        return puzzle;
    }
}