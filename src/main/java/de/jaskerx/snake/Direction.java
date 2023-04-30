package de.jaskerx.snake;

public enum Direction {

    RIGHT (true),
    UP (false),
    LEFT (true),
    DOWN (false);

    private final boolean horizontal;

    Direction(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

}
