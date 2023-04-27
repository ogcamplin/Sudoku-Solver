package com.sudoku.solver;

import java.util.HashMap;
import java.util.Map;

public class Coord {
    private static Map<Integer, Map<Integer, Coord>> cache = new HashMap<>();
    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coord from(int x, int y) {
        if (!cache.containsKey(x)) {
            cache.put(x, new HashMap<>());
        }
        Map<Integer, Coord> yCache = cache.get(x);
        if (!yCache.containsKey(y)) {
            yCache.put(y, new Coord(x, y));
        }
        return yCache.get(y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coord other = (Coord) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
