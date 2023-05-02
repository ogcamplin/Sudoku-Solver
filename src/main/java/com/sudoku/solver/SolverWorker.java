package com.sudoku.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.sudoku.model.Puzzle;
import com.sudoku.model.Position;

public class SolverWorker implements Runnable {
    private Map<Position, HashSet<Integer>> possibilitiesMap;
    private Puzzle puzzle;
    private SolutionCallback cb;
    private ExecutorService executorService;

    public SolverWorker(Puzzle puzzle, SolutionCallback callback, ExecutorService executorService) {
        this.possibilitiesMap = new HashMap<>();
        this.puzzle = puzzle;
        this.cb = callback;
        this.executorService = executorService;
    }

    /**
     * Gets the position within the possibilities map with the lowest number of possibilities
     * @return Position with the lowest number of possibilities
     */
    private Position getLowestPossibilityPosition() {
        List<Position> openPositions = new ArrayList<>(possibilitiesMap.keySet());
        Position minPosition = openPositions.remove(0);

        for(Position pos : openPositions) {
            minPosition = possibilitiesMap.get(pos).size() < possibilitiesMap.get(minPosition).size() ? pos : minPosition;        
        }
        return minPosition;
    }

    /**
     * Fills the possibilities map with the default set of all possibilites (1-9) for each empty position.
     * @param grid
     */
    private void fillAllPossibilitiesFromGrid(char[][] grid) {
        for(int row=0; row<Puzzle.SIZE; row++) {
            for(int col=0; col<Puzzle.SIZE; col++) {
                if (grid[col][row] == '.') {
                    possibilitiesMap.put(Position.from(col, row), new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9)));
                }
            }
        }
    }

    /**
     * Loop through all the positions on the grid that have possibilities. 
     * - Remove the possibilites associated with the position that are also found within the same square, row, and column.
     * - If there is only one possibility left, fill that position, and remove the entry from the possibilites map
     * - If there has been a position that has been filled, loop through all possibilites again.
     */
    private void computeAndSolvePossibilities() throws SolutionException {
        char grid[][] = this.puzzle.getGrid();
        fillAllPossibilitiesFromGrid(grid);

        List<Position> filledPositions = new ArrayList<>();
        boolean didFill;
        
        do {
            didFill = false;
            // get all the unfilled cells and compute possible options
            for(Position pos : possibilitiesMap.keySet()) {
                Set<Integer> possibilitiesSet = possibilitiesMap.get(Position.from(pos.x, pos.y));
                Position[] bounds = this.getSquareBoundsForPosition(pos);
                
                // look at all other numbers in the same square
                for(int col=bounds[0].x; col<bounds[1].x; col++) {
                    for(int row=bounds[0].y; row<bounds[1].y; row++) {
                        possibilitiesSet.remove(Character.getNumericValue(grid[col][row]));
                    }
                }

                // look at all other numbers in the same column
                for(int col=0; col<Puzzle.SIZE; col++) {
                    possibilitiesSet.remove(Character.getNumericValue(grid[col][pos.y]));
                }

                // look at all other numbers in the same row
                for(int row=0; row<Puzzle.SIZE; row++) {
                    possibilitiesSet.remove(Character.getNumericValue(grid[pos.x][row]));
                }

                if(possibilitiesSet.size() == 1) {
                    grid[pos.x][pos.y] = ((Integer)possibilitiesSet.toArray()[0]).toString().charAt(0);
                    filledPositions.add(pos);
                    didFill = true;
                };
            }
            filledPositions.forEach(pos -> possibilitiesMap.remove(pos));
            filledPositions.clear();
        } while(didFill); 
    }

    /**
     * Get the lower and upper x,y coordinates for the square containing the given position.
     * @param pos
     * @return [(lower x, lower y), (upper x, upper y)]
     */
    private Position[] getSquareBoundsForPosition(Position pos) {
        int xLower = 0;
        int xUpper = 0;
        int yLower = 0;
        int yUpper = 0;
        
        if(pos.x < 3) {
            xLower = 0;
            xUpper = 3;
        } else if(pos.x < 6) {
            xLower = 3;
            xUpper = 6;
        } else if (pos.x < 9) {
            xLower = 6;
            xUpper = 9;
        }

        if(pos.y < 3) {
            yLower = 0;
            yUpper = 3;
        } else if(pos.y < 6) {
            yLower = 3;
            yUpper = 6;
        } else if (pos.y < 9) {
            yLower = 6;
            yUpper = 9;
        }

        return new Position[]{Position.from(xLower, yLower), Position.from(xUpper, yUpper)};
    }

    @Override
    public void run() {
        try {
            computeAndSolvePossibilities();

            if(possibilitiesMap.size() == 0) { // no more possibilities to explore, callback
                this.cb.onSuccess(this.puzzle); 
                return;
            } else {
                Position lowPos = getLowestPossibilityPosition();

                if(possibilitiesMap.get(lowPos).size() == 0) { // no possible options here, quit thread because its no good!
                    return;
                } else {
                    for (Integer i : possibilitiesMap.get(lowPos)) {
                        Puzzle puzzleCopy = puzzle.copy();
                        puzzleCopy.setNumberAt(lowPos.x, lowPos.y, i.toString().charAt(0));
                        if(executorService.isShutdown()) { return; }
                        executorService.submit(new SolverWorker(puzzleCopy, this.cb, this.executorService));
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
