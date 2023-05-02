package com.sudoku.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Puzzle {
    public static final int SIZE = 9;
    private char grid[][];
    private int givenCount;
    
    public Puzzle(char[][] grid) throws InvalidPuzzleException {
        this.givenCount = 0;
        validatePuzzle(grid);
        this.grid = grid;
    }

    private void validatePuzzle(char[][] grid) throws InvalidPuzzleException {
        boolean[][] visited = new boolean[SIZE][SIZE];
        for (int i = 0; i < visited.length; i++) {
            Arrays.fill(visited[i], false);
        }

        // Check rows
        for (int row = 0; row < SIZE; row++) {
            if (!isValidRow(grid, row, visited)) {
                throw new InvalidPuzzleException("Duplicates found at row " + (row+1));
            }
        }

        // Check columns
        for (int col = 0; col < SIZE; col++) {
            if (!isValidColumn(grid, col, visited)) {
                throw new InvalidPuzzleException("Duplicates found at column " + (col+1));
            }
        }

        // Check boxes
        for (int box = 0; box < SIZE; box++) {
            if (!isValidBox(grid, box, visited)) {
                throw new InvalidPuzzleException("Duplicates found at box " + (box+1));
            }
        }

        if(givenCount < 17) {
            throw new InvalidPuzzleException("Insufficient number of given values. Expected at least 17, but found " + givenCount);
        }
    }

    private boolean isValidBox(char[][] grid, int box, boolean[][] visited) {
        Set<Integer> foundSet = new HashSet<>();

        int startRow = (box / 3) * 3;
        int startCol = (box % 3) * 3;

        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                int num = Character.getNumericValue(grid[row][col]);

                if(num != -1) {
                    if(foundSet.contains(num)) {
                        return false;
                    }
                    foundSet.add(num);
                    if(!visited[col][row]) { givenCount++; }
                }

                visited[col][row] = true;
            }
        }
        return true;
    }

    private boolean isValidRow(char[][] grid, int row, boolean[][] visited) {
        Set<Integer> foundSet = new HashSet<>();

        for(int col=0; col<SIZE; col++) {
            int num = Character.getNumericValue(grid[col][row]);

            if(num != -1) {
                if(foundSet.contains(num)) {
                    return false;
                }
                foundSet.add(num);
                if(!visited[col][row]) { givenCount++; }
            }

            visited[col][row] = true;
        }
        return true;
    }

    private boolean isValidColumn(char[][] grid, int col, boolean[][] visited) {
        Set<Integer> foundSet = new HashSet<>();

        for(int row=0; row<SIZE; row++) {
            int num = Character.getNumericValue(grid[col][row]);

            if(num != -1) {
                if(foundSet.contains(num)) {
                    return false;
                }
                foundSet.add(num);
                if(!visited[col][row]) { givenCount++; }
            }
            
            visited[col][row] = true;
        }
        return true;
    }

    public void setNumberAt(int x, int y, char num) {
        grid[x][y] = num;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public char[][] getGrid() {
        return grid;
    }

    public Puzzle copy() throws InvalidPuzzleException {
        char[][] newGrid = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        return new Puzzle(newGrid);
    }

    /**
     * Create a new puzzle from a given file.
     * @param filename
     * @return
     * @throws IOException
     */
    public static Puzzle fromFile(String filename) throws IOException, InvalidPuzzleException {
        String basePath = new File("").getAbsolutePath();
        FileReader f = new FileReader(basePath + "/" + filename);
        char grid[][] = new char[SIZE][SIZE];

        int character;
        char num;
        int row=0; 
        int col=0;

        while ((character = f.read()) > 31) {
            num = (char) character;
            grid[col][row] = num;
            col++;

            if(col%SIZE==0) {
                col=0;
                row++;
            } 
        }

        f.close();
        return new Puzzle(grid);
    }

    public static Puzzle fromString(String puzzleString) throws InvalidPuzzleException {
        char grid[][] = new char[SIZE][SIZE];

        int row=0; 
        int col=0;

        for(char num : puzzleString.toCharArray()) {
            grid[col][row] = num;
            col++;

            if(col%SIZE==0) {
                col=0;
                row++;
            } 
        }

        return new Puzzle(grid);
    }

    public String toResultString() {
        String result = "";

        for (int row=0; row<grid[0].length; row++) {
            for (int col=0; col<grid.length; col++) {
                result += grid[col][row];
            }
        }

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        
        for (int row=0; row<grid[0].length; row++) {
            if (row!=0 && row % 3==0) {
                sb.append("---------|---------|---------\n");
            }

            for (int col=0; col<grid.length; col++) {
                if (col != 0 && col % 3 == 0) {
                    sb.append("|");
                }
                sb.append(" " + grid[col][row] + " ");
                
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
