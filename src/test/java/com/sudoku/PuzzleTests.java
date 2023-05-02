package com.sudoku;

import org.junit.Test;

import com.sudoku.model.InvalidPuzzleException;
import static org.junit.Assert.*;

import org.junit.Assert;

public class PuzzleTests extends SudokuTestBase {
    @Test
    public void InvalidPuzzleEmptyTest() throws Exception {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/empty.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Insufficient number of given values. Expected at least 17, but found 0", ex.getMessage());
    }

    @Test
    public void InvalidPuzzleSingleGivenTest() throws Exception {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/single-given.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Insufficient number of given values. Expected at least 17, but found 1", ex.getMessage());
    }

    @Test
    public void InvalidPuzzleInsufficientGivenTest() {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/insufficient-given.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Insufficient number of given values. Expected at least 17, but found 16", ex.getMessage());
    }

    @Test
    public void InvalidPuzzleDuplicateGivenBoxTest() throws Exception {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/duplicate-given-box.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Duplicates found at box 5", ex.getMessage());   
    }

    @Test
    public void InvalidPuzzleDuplicateGivenColumnTest() throws Exception {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/duplicate-given-column.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Duplicates found at column 5", ex.getMessage()); 
        
    }

    @Test
    public void InvalidPuzzleDuplicateGivenRowTest() throws Exception {
        Exception ex = Assert.assertThrows(Exception.class, () -> {
            parseFileToPuzzle(getSudokuFile("invalid/duplicate-given-row.sudo"));
        });
        
        assertTrue(ex instanceof InvalidPuzzleException);
        assertEquals("Duplicates found at row 5", ex.getMessage()); 
    }



}
