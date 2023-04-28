package com.sudoku.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Puzzle {
    public static final int SIZE = 9;

    private char grid[][];
    
    public Puzzle(char[][] grid) {
        this.grid = grid;
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

    public Puzzle copy() {
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
    public static Puzzle fromFile(String filename) throws IOException {
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

    public static Puzzle fromString(String puzzleString) {
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
