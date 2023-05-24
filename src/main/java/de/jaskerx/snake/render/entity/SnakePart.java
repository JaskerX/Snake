package de.jaskerx.snake.render.entity;

import de.jaskerx.snake.Direction;
import de.jaskerx.snake.Snake;

import java.awt.*;

public class SnakePart extends Entity {

    private Direction direction;

    public SnakePart(double x, double y) {
        super(x, y, 30, 30);
        this.direction = Direction.RIGHT;
    }

    public SnakePart(double x, double y, Direction direction) {
        super(x, y, 30, 30);
        this.direction = direction;
    }

    public SnakePart(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.direction = Direction.RIGHT;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if(!Snake.debug) {
            graphics2D.setColor(Color.GREEN);
        } else {
            if (this.width == 30 && this.height == 30) {
                graphics2D.setColor(Color.GREEN);
            } else {
                graphics2D.setColor(Color.RED);
            }
        }
        graphics2D.fillRect((int) this.x, (int) this.y, (int) this.width, (int) this.height);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

}
