package com.sudoku.model;

import java.util.HashMap;
import java.util.Map;

public class Position {
    private static Map<Integer, Map<Integer, Position>> cache = new HashMap<>();
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position from(int x, int y) {
        if (!cache.containsKey(x)) {
            cache.put(x, new HashMap<>());
        }
        Map<Integer, Position> yCache = cache.get(x);
        if (!yCache.containsKey(y)) {
            yCache.put(y, new Position(x, y));
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
        Position other = (Position) obj;
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
