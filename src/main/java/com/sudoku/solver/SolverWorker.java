package com.sudoku.solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolverWorker implements Runnable {
    Map<Coord, HashSet<Integer>> possibilitiesMap = new HashMap<>();

    private char grid[][];

    public SolverWorker(char[][] grid) {
        this.grid = grid;
    }

    public void computePossibilities() {
        for(int row=0; row<Board.SIZE; row++) {
            for(int col=0; col<Board.SIZE; col++) {
                if (grid[col][row] == '.') {
                    possibilitiesMap.put(Coord.from(row, col), new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9)));
                }
            }
        }
        
        // get all the unfilled cells and compute possible options
        for(Coord c : possibilitiesMap.keySet()) {
            Set<Integer> possibilitiesSet = possibilitiesMap.get(Coord.from(c.x, c.y));
            Coord[] bounds = this.getSquareBounds(c);
            
            // look at all other numbers in the same square
            for(int col=bounds[0].x; col<bounds[1].x; col++) {
                for(int row=bounds[0].y; row<bounds[1].y; row++) {
                    possibilitiesSet.remove(Character.getNumericValue(grid[col][row]));
                }
            }

            // look at all other numbers in the same column
            for(int col=0; col<Board.SIZE; col++) {
                possibilitiesSet.remove(Character.getNumericValue(grid[col][c.y]));
            }

            // look at all other numbers in the same row
            for(int row=0; row<Board.SIZE; row++) {
                possibilitiesSet.remove(Character.getNumericValue(grid[c.x][row]));
            }
        }
    }

    private Coord[] getSquareBounds(Coord c) {
        int xLower = 0;
        int xUpper = 0;
        int yLower = 0;
        int yUpper = 0;
        
        if(c.x < 3) {
            xLower = 0;
            xUpper = 3;
        } else if(c.x < 6) {
            xLower = 3;
            xUpper = 6;
        } else if (c.x < 9) {
            xLower = 6;
            xUpper = 9;
        }

        if(c.y < 3) {
            yLower = 0;
            yUpper = 3;
        } else if(c.y < 6) {
            yLower = 3;
            yUpper = 6;
        } else if (c.y < 9) {
            yLower = 6;
            yUpper = 9;
        }

        return new Coord[]{Coord.from(xLower, yLower), Coord.from(xUpper, yUpper)};
    }

    @Override
    public void run() {
        computePossibilities();
    }
}
