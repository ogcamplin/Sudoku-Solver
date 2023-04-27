package com.sudoku.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Board {
    public static final int SIZE = 9;

    private char grid[][];
    
    public Board(char[][] grid) {
        this.grid = grid;
    }

    private void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public char[][] getGrid() {
        return grid;
    }

    public static Board fromFile(String filename) throws IOException {
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
        return new Board(grid);
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
